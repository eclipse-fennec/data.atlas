/**
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.publication.dcat;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Instant;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.api.PublicationStatus;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.publication.dcat.DcatMapper.DatasetPlan;
import org.eclipse.fennec.data.atlas.publication.dcat.DcatMapper.DistributionPlan;
import org.eclipse.fennec.data.atlas.publication.dcat.DcatMapper.ProviderPlan;
import org.eclipse.fennec.dcat.atlas.client.api.DcatAtlasClient;
import org.eclipse.fennec.dcat.atlas.client.api.DcatCollection;
import org.eclipse.fennec.dcat.atlas.client.api.DcatModelConstraintException;
import org.eclipse.fennec.dcat.atlas.client.api.DcatShaclException;
import org.eclipse.fennec.dcat.atlas.client.api.DeleteMode;
import org.eclipse.fennec.dcat.atlas.client.api.NotFoundException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The DCAT publication handler (data.atlas issue #4): tracks {@code DataService}
 * configuration services whose configuration declares a {@code DcatPublication}
 * and keeps them registered with a DCAT.Atlas portal — DataService-first, then
 * its DataSets, their Distributions, and the membership links, on every sync,
 * because the portal's PUT replace semantics drop containment and membership
 * (DA-DCAT-16).
 *
 * <p>
 * The portal is never on the critical path (DA-DCAT-18): all portal I/O runs on
 * one worker thread off the config events; the data services serve regardless.
 * Failures are handled on their merits (DA-DCAT-19): SHACL/model-constraint
 * refusals mark the publication {@link PublicationStatus#STATE_ERROR} and stop
 * until the configuration changes; transport failures retry on a fixed
 * interval; a conflict means a concurrent writer and is retried like a
 * transport failure — the next unconditional sync re-asserts our truth.
 * </p>
 *
 * <p>
 * A provider that disappears from the configuration is withdrawn from the
 * portal (DA-DCAT-15). Component shutdown is not withdrawal: a stopping Data
 * Atlas leaves its portal entries alone.
 * </p>
 */
// registered as a service of its own class so the PUBLICATION_HANDLER marker
// property is visible in the service registry - that is what the bootstrap's
// DA-DCAT-3 diagnosis looks for (an immediate component without a service
// would carry the property nowhere)
@Component(name = DcatPublicationConfigurator.PID, immediate = true, //
		service = DcatPublicationConfigurator.class, //
		property = DataAtlasConstants.PUBLICATION_HANDLER + "=dcat")
@Designate(ocd = DcatPublicationConfigurator.Config.class)
public class DcatPublicationConfigurator {

	/** Component + configuration PID. */
	public static final String PID = "org.eclipse.fennec.data.atlas.publication.dcat";

	@ObjectClassDefinition(name = "Data Atlas DCAT Publication", //
			description = "Publishes declared DataServices/DataSets to a DCAT.Atlas portal. "
					+ "The portal endpoint itself is the dcat.atlas client's factory configuration "
					+ "(PID org.eclipse.fennec.dcat.atlas.client).")
	@interface Config {

		@AttributeDefinition(name = "Public base URL", description = "The public base URL this Data Atlas "
				+ "is reachable under from the portal's consumers, e.g. https://data.example.org. "
				+ "Deployment configuration - the Data Atlas cannot know it (DA-DCAT-13).")
		String public_base_url() default "";

		@AttributeDefinition(name = "Retry interval (ms)", description = "Delay before a transiently "
				+ "failed portal registration is retried.")
		long retry_interval_ms() default 30_000L;
	}

	private static final Logger LOG = System.getLogger(DcatPublicationConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor(runnable -> {
		Thread thread = new Thread(runnable, "data-atlas-dcat-publication");
		thread.setDaemon(true);
		return thread;
	});

	/** What the portal currently holds for a provider, kept for withdrawal. */
	private record Published(String portal, String catalog, String serviceId, Set<String> datasetIds) {
	}

	// all mutable state guarded by this
	private final Map<String, DataService> services = new HashMap<>();
	private final Map<String, DcatAtlasClient> clients = new HashMap<>();
	private final Map<String, Published> published = new HashMap<>();
	private final Map<String, StatusHolder> statuses = new HashMap<>();
	private volatile Config config;
	private volatile boolean closing;
	private long generation;

	@Activate
	public DcatPublicationConfigurator(BundleContext bundleContext, Config config) {
		this.bundleContext = bundleContext;
		this.config = config;
	}

	@Modified
	synchronized void modified(Config config) {
		this.config = config;
		reconcile();
	}

	@Deactivate
	synchronized void deactivate() {
		closing = true;
		worker.shutdownNow();
		statuses.values().forEach(StatusHolder::unregister);
		statuses.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addDataService(DataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id == null) {
			return;
		}
		services.put(id, service);
		reconcile();
	}

	synchronized void removeDataService(DataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id != null && services.remove(id) != null && !closing) {
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addDcatAtlasClient(DcatAtlasClient client, Map<String, Object> props) {
		clients.put(portalOf(props), client);
		reconcile();
	}

	synchronized void removeDcatAtlasClient(DcatAtlasClient client, Map<String, Object> props) {
		if (clients.remove(portalOf(props)) != null && !closing) {
			reconcile();
		}
	}

	private static String portalOf(Map<String, Object> props) {
		return props.get("dcat.portal") instanceof String portal ? portal : "";
	}

	private static String configObjectId(Map<String, Object> props, String fallback) {
		Object id = props.get(DataAtlasConstants.CONFIG_OBJECT_ID);
		return id instanceof String s ? s : fallback;
	}

	/**
	 * Recomputes the desired portal state and schedules the syncs and
	 * withdrawals that get there. Bumping the generation invalidates the retries
	 * of earlier reconciles: a stale retry finds itself outdated and does
	 * nothing, so a corrected configuration never races its predecessor's
	 * retry loop.
	 */
	private void reconcile() {
		generation++;
		long current = generation;
		// withdraw what was published but is no longer declared
		for (String id : Set.copyOf(published.keySet())) {
			DataService service = services.get(id);
			if (service == null || !DcatMapper.isPublished(service)) {
				Published record = published.get(id);
				worker.execute(() -> withdraw(current, id, record));
			}
		}
		// sync every declared provider
		services.forEach((id, service) -> {
			if (DcatMapper.isPublished(service)) {
				worker.execute(() -> sync(current, id));
			}
		});
		// drop stale ERROR/PENDING statuses of providers that lost their declaration
		statuses.keySet().removeIf(id -> {
			DataService service = services.get(id);
			if ((service == null || !DcatMapper.isPublished(service)) && !published.containsKey(id)) {
				statuses.get(id).unregister();
				return true;
			}
			return false;
		});
	}

	// --- worker-side (single thread) ----------------------------------------

	private void sync(long expectedGeneration, String id) {
		DataService service;
		Config currentConfig;
		synchronized (this) {
			if (closing || generation != expectedGeneration) {
				return;
			}
			service = services.get(id);
			currentConfig = config;
		}
		if (service == null || !DcatMapper.isPublished(service)) {
			return;
		}
		ProviderPlan plan;
		try {
			plan = DcatMapper.plan(service, currentConfig.public_base_url());
		} catch (PublicationConfigException e) {
			LOG.log(Level.ERROR, () -> "DCAT publication of '" + id + "' is misconfigured: " + e.getMessage());
			updateStatus(id, PublicationStatus.STATE_ERROR, e.getMessage());
			return;
		}
		DcatAtlasClient client = clientFor(id, plan.portal());
		if (client == null) {
			return; // status already set to PENDING/ERROR
		}
		if (!client.ready()) {
			LOG.log(Level.WARNING, () -> "DCAT portal for '" + id + "' is not ready, retrying in "
					+ currentConfig.retry_interval_ms() + " ms");
			updateStatus(id, PublicationStatus.STATE_PENDING, "portal not ready");
			scheduleRetry(expectedGeneration, id, currentConfig);
			return;
		}
		try {
			Set<String> previous = previousDatasetIds(id);
			register(client, plan);
			deleteLeftovers(client, plan, previous);
			synchronized (this) {
				published.put(id, new Published(plan.portal(), plan.catalog(), plan.serviceId(),
						datasetIds(plan)));
			}
			LOG.log(Level.INFO, () -> "Published '" + id + "' to DCAT catalog '" + plan.catalog() + "' ("
					+ plan.datasets().size() + " dataset(s))");
			updateStatus(id, PublicationStatus.STATE_REGISTERED,
					"published to catalog '" + plan.catalog() + "' as '" + plan.serviceId() + "'");
		} catch (DcatShaclException | DcatModelConstraintException refused) {
			LOG.log(Level.ERROR, () -> "The portal refused the publication of '" + id
					+ "' — the configuration is wrong, not retrying: " + refused.getMessage());
			updateStatus(id, PublicationStatus.STATE_ERROR, refused.getMessage());
		} catch (RuntimeException transientFailure) {
			// RetryableException, TransportException, a conflict from a concurrent
			// writer (the next unconditional sync re-asserts our truth), or any
			// other client failure: report, retry, keep serving (DA-DCAT-18/19)
			LOG.log(Level.WARNING, () -> "Publishing '" + id + "' failed (" + transientFailure.getMessage()
					+ "), retrying in " + currentConfig.retry_interval_ms() + " ms");
			updateStatus(id, PublicationStatus.STATE_RETRYING, transientFailure.getMessage());
			scheduleRetry(expectedGeneration, id, currentConfig);
		}
	}

	/**
	 * The three-step loop the client documents, run in full on every sync:
	 * register the service, register its datasets and their distributions,
	 * assert the membership links a PUT replace dropped.
	 */
	private void register(DcatAtlasClient client, ProviderPlan plan) {
		client.registerDataService(plan.serviceId(), plan.dcatService());
		for (DatasetPlan dataset : plan.datasets()) {
			client.registerDataset(dataset.datasetId(), dataset.dcatDataset());
			for (DistributionPlan distribution : dataset.distributions()) {
				client.registerDistribution(dataset.datasetId(), distribution.distributionId(),
						distribution.dcatDistribution());
			}
		}
		client.linkDataServiceToCatalog(plan.catalog(), plan.serviceId());
		for (DatasetPlan dataset : plan.datasets()) {
			client.linkDatasetToCatalog(dataset.catalog(), dataset.datasetId());
			client.linkDatasetToDataService(plan.serviceId(), dataset.datasetId());
			for (DistributionPlan distribution : dataset.distributions()) {
				client.linkAccessService(dataset.datasetId(), distribution.distributionId(), plan.serviceId());
			}
		}
	}

	/** Datasets a previous sync published that the current plan no longer has. */
	private void deleteLeftovers(DcatAtlasClient client, ProviderPlan plan, Set<String> previousDatasetIds) {
		Set<String> current = datasetIds(plan);
		for (String leftover : previousDatasetIds) {
			if (!current.contains(leftover)) {
				deleteQuietly(client, DcatCollection.DATASETS, leftover);
			}
		}
	}

	private void withdraw(long expectedGeneration, String id, Published record) {
		synchronized (this) {
			if (closing || generation != expectedGeneration || record == null) {
				return;
			}
		}
		DcatAtlasClient client = clientFor(id, record.portal());
		if (client == null) {
			return;
		}
		try {
			for (String datasetId : record.datasetIds()) {
				deleteQuietly(client, DcatCollection.DATASETS, datasetId);
			}
			deleteQuietly(client, DcatCollection.DATA_SERVICES, record.serviceId());
			synchronized (this) {
				published.remove(id);
				StatusHolder status = statuses.remove(id);
				if (status != null) {
					status.unregister();
				}
			}
			LOG.log(Level.INFO, () -> "Withdrew '" + id + "' from DCAT catalog '" + record.catalog() + "'");
		} catch (RuntimeException e) {
			Config currentConfig = config;
			LOG.log(Level.WARNING, () -> "Withdrawing '" + id + "' failed (" + e.getMessage() + "), retrying in "
					+ currentConfig.retry_interval_ms() + " ms");
			updateStatus(id, PublicationStatus.STATE_RETRYING, "withdrawal pending: " + e.getMessage());
			worker.schedule(() -> withdraw(expectedGeneration, id, record), currentConfig.retry_interval_ms(),
					TimeUnit.MILLISECONDS);
		}
	}

	private void deleteQuietly(DcatAtlasClient client, DcatCollection collection, String id) {
		try {
			client.delete(collection, id, DeleteMode.CASCADE);
		} catch (NotFoundException alreadyGone) {
			// withdrawal is idempotent: what is not there needs no removing
		}
	}

	/**
	 * The client for a plan's portal: the named one, or the single configured
	 * client when the publication names none. No client is a PENDING state —
	 * the portal is deployment configuration that may simply not be there yet —
	 * except an unnamed portal with several clients, which no deployment can
	 * resolve and is therefore a configuration error.
	 */
	private DcatAtlasClient clientFor(String id, String portal) {
		synchronized (this) {
			if (portal != null && !portal.isBlank()) {
				DcatAtlasClient client = clients.get(portal);
				if (client == null) {
					updateStatus(id, PublicationStatus.STATE_PENDING,
							"no dcat.atlas client for portal '" + portal + "' is configured (yet)");
				}
				return client;
			}
			if (clients.size() == 1) {
				return clients.values().iterator().next();
			}
			if (clients.isEmpty()) {
				updateStatus(id, PublicationStatus.STATE_PENDING, "no dcat.atlas client is configured (yet)");
				return null;
			}
			updateStatus(id, PublicationStatus.STATE_ERROR, "the publication names no portal but "
					+ clients.size() + " clients are configured — set DcatPublication.portal");
			return null;
		}
	}

	private void scheduleRetry(long expectedGeneration, String id, Config currentConfig) {
		worker.schedule(() -> sync(expectedGeneration, id), currentConfig.retry_interval_ms(), TimeUnit.MILLISECONDS);
	}

	private static Set<String> datasetIds(ProviderPlan plan) {
		Set<String> ids = new LinkedHashSet<>();
		plan.datasets().forEach(dataset -> ids.add(dataset.datasetId()));
		return ids;
	}

	private synchronized Set<String> previousDatasetIds(String id) {
		Published record = published.get(id);
		return record == null ? Set.of() : record.datasetIds();
	}

	// --- observability (DA-DCAT-20) -----------------------------------------

	private synchronized void updateStatus(String id, String state, String message) {
		if (closing) {
			return;
		}
		statuses.computeIfAbsent(id, providerId -> new StatusHolder(bundleContext, providerId)).update(state,
				message);
	}

	/**
	 * One registered {@link PublicationStatus} per published provider; the state
	 * is mirrored into a service property so a consumer can await it with a
	 * plain service filter.
	 */
	private static final class StatusHolder implements PublicationStatus {

		private final String providerId;
		private final ServiceRegistration<PublicationStatus> registration;
		private volatile String state = STATE_PENDING;
		private volatile String message = "";
		private volatile Instant lastChange = Instant.now();

		StatusHolder(BundleContext bundleContext, String providerId) {
			this.providerId = providerId;
			registration = bundleContext.registerService(PublicationStatus.class, this, properties());
		}

		void update(String state, String message) {
			this.state = state;
			this.message = message == null ? "" : message;
			this.lastChange = Instant.now();
			registration.setProperties(properties());
		}

		void unregister() {
			try {
				registration.unregister();
			} catch (IllegalStateException alreadyUnregistered) {
				// fine on shutdown races
			}
		}

		private Dictionary<String, Object> properties() {
			Dictionary<String, Object> props = new Hashtable<>();
			props.put(DataAtlasConstants.CONFIG_OBJECT_ID, providerId);
			props.put(STATE, state);
			return props;
		}

		@Override
		public String providerId() {
			return providerId;
		}

		@Override
		public String state() {
			return state;
		}

		@Override
		public String message() {
			return message;
		}

		@Override
		public Instant lastChange() {
			return lastChange;
		}
	}
}
