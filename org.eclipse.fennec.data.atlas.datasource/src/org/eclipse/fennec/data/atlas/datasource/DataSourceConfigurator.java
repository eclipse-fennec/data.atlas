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
package org.eclipse.fennec.data.atlas.datasource;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.fennec.data.atlas.configuration.DataSource;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Realizes every {@code DataSource} configuration object the bootstrap
 * registers.
 *
 * <p>
 * A definition with a {@code filter} is <b>bound</b>: the deployment
 * configured the backend service itself and the input configurators target it
 * by that filter - this configurator only acknowledges it. A definition with
 * connection coordinates is <b>materialized</b>: translated into the backend's
 * Config Admin factory configurations (see {@link Materialization}), which are
 * deleted again when the definition goes away or changes (the bootstrap
 * re-registers a changed object, so change = remove + add).
 * </p>
 *
 * <p>
 * Materializing a definition that arrived over the network (Model Atlas mode)
 * connects this runtime - with its locally resolved credentials - to whatever
 * host the definition names. A deployment can restrict that with
 * {@value #PROP_HOST_ALLOWLIST} on this configurator's PID {@value #PID}: a
 * list of host names a materialized definition may use; unset means no
 * restriction (the file-mode default, where the configuration is as trusted as
 * the mounted Configurator files next to it).
 * </p>
 */
@Component(immediate = true, configurationPid = DataSourceConfigurator.PID,
		configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class DataSourceConfigurator implements Materialization.Environment {

	public static final String PID = "org.eclipse.fennec.data.atlas.datasource";
	/** Hosts a materialized definition may connect to (String or String[]; comma-separated accepted). */
	public static final String PROP_HOST_ALLOWLIST = "host.allowlist";

	private static final Logger LOG = System.getLogger(DataSourceConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ConfigurationAdmin configAdmin;

	// all access guarded by this
	private Set<String> hostAllowList = Set.of();
	private final Map<String, DataSource> sources = new LinkedHashMap<>();
	private final Map<String, List<Configuration>> realized = new LinkedHashMap<>();

	@Activate
	public DataSourceConfigurator(BundleContext bundleContext, @Reference ConfigurationAdmin configAdmin,
			Map<String, Object> settings) {
		this.bundleContext = bundleContext;
		this.configAdmin = configAdmin;
		this.hostAllowList = allowList(settings);
	}

	@Modified
	synchronized void modified(Map<String, Object> settings) {
		Set<String> allowList = allowList(settings);
		if (allowList.equals(hostAllowList)) {
			return;
		}
		hostAllowList = allowList;
		LOG.log(Level.INFO, () -> "Host allow-list changed to " + (allowList.isEmpty() ? "<unrestricted>" : allowList)
				+ ", re-evaluating " + sources.size() + " data source(s)");
		for (DataSource source : new ArrayList<>(sources.values())) {
			tearDown(source.getId());
			realize(source);
		}
	}

	@Deactivate
	synchronized void deactivate() {
		new ArrayList<>(realized.keySet()).forEach(this::tearDown);
		sources.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addDataSource(DataSource dataSource, Map<String, Object> serviceProps) {
		String id = dataSource.getId();
		if (id == null || id.isBlank()) {
			LOG.log(Level.WARNING, () -> "Ignoring " + dataSource.eClass().getName() + " without id: " + dataSource);
			return;
		}
		if (sources.containsKey(id)) {
			// the bootstrap unregisters before it re-registers, but be safe
			tearDown(id);
		}
		sources.put(id, dataSource);
		realize(dataSource);
	}

	synchronized void removeDataSource(DataSource dataSource, Map<String, Object> serviceProps) {
		String id = dataSource.getId();
		if (id != null && sources.remove(id) != null) {
			tearDown(id);
		}
	}

	private void realize(DataSource source) {
		String id = source.getId();
		try {
			if (Materialization.mode(source) == Materialization.Mode.BIND) {
				LOG.log(Level.INFO, () -> Materialization.describe(source) + " is bound by filter "
						+ source.getFilter().trim());
				return;
			}
			List<Configuration> configurations = new ArrayList<>();
			try {
				for (Materialization.FactoryConfiguration spec : Materialization.plan(source, this)) {
					Configuration configuration = configAdmin.getFactoryConfiguration(spec.factoryPid(), spec.name(),
							"?");
					configuration.update(spec.properties());
					configurations.add(configuration);
				}
			} catch (IOException | RuntimeException e) {
				delete(configurations);
				throw e;
			}
			realized.put(id, configurations);
			LOG.log(Level.INFO, () -> "Materialized " + Materialization.describe(source) + " as "
					+ configurations.stream().map(Configuration::getPid).toList());
		} catch (DataSourceDefinitionException e) {
			LOG.log(Level.ERROR, () -> "Not realized: " + e.getMessage()
					+ " - inputs referencing this data source stay down until the definition is corrected");
		} catch (IOException | RuntimeException e) {
			LOG.log(Level.ERROR, () -> "Unable to materialize " + Materialization.describe(source) + ": " + e.getMessage(),
					e);
		}
	}

	private void tearDown(String id) {
		List<Configuration> configurations = realized.remove(id);
		if (configurations != null) {
			delete(configurations);
			LOG.log(Level.INFO, () -> "Removed the materialization of data source '" + id + "'");
		}
	}

	private static void delete(Collection<Configuration> configurations) {
		for (Configuration configuration : configurations) {
			try {
				configuration.delete();
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.WARNING, () -> "Unable to delete configuration " + configuration.getPid(), e);
			}
		}
	}

	// --- Materialization.Environment ------------------------------------------

	@Override
	public boolean environmentVariablePresent(String name) {
		return System.getenv(name) != null;
	}

	@Override
	public Optional<Path> secretsDirectory() {
		String directory = bundleContext.getProperty(Placeholders.SECRETS_DIR_PROPERTY);
		return directory == null || directory.isBlank() ? Optional.empty() : Optional.of(Path.of(directory.trim()));
	}

	@Override
	public Set<String> hostAllowList() {
		return hostAllowList;
	}

	/** The allow-list setting: a String (comma-separated) or String[], lower-cased, blanks dropped. */
	static Set<String> allowList(Map<String, Object> settings) {
		Object value = settings == null ? null : settings.get(PROP_HOST_ALLOWLIST);
		Set<String> hosts = new LinkedHashSet<>();
		if (value instanceof String[] array) {
			for (String host : array) {
				addHosts(hosts, host);
			}
		} else if (value instanceof Collection<?> collection) {
			collection.forEach(host -> addHosts(hosts, String.valueOf(host)));
		} else if (value != null) {
			addHosts(hosts, String.valueOf(value));
		}
		return Set.copyOf(hosts);
	}

	private static void addHosts(Set<String> hosts, String value) {
		for (String host : value.split(",")) {
			if (!host.isBlank()) {
				hosts.add(host.trim().toLowerCase(Locale.ROOT));
			}
		}
	}
}
