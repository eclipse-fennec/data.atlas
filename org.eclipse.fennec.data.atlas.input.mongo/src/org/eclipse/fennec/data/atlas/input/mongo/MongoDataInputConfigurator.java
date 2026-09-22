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
package org.eclipse.fennec.data.atlas.input.mongo;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.fennec.data.atlas.configuration.MongoDataInput;
import org.eclipse.fennec.data.atlas.configuration.MongoDataSource;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Input configurator: translates every {@code MongoDataInput} configuration
 * service into one {@code fennec.repository.mongo} factory configuration - the
 * read-only repository the fennec Mongo backend publishes with
 * {@code persistence.repository.id} = the input's id, which is how the
 * endpoint configurators pick the input up.
 *
 * <p>
 * The repository binds the {@code MongoDatabase} service of the input's
 * {@code MongoDataSource}: in BIND mode by the definition's own filter, in
 * MATERIALIZE mode by the alias the datasource configurator gave the database
 * it created ({@code dataAtlas.<dataSourceId>}). No mapping is involved - the
 * backend reads the collections through the BSON codec from the registered
 * EPackages. The repository appears when the database service does (the
 * client is liveness-gated) and disappears with it.
 * </p>
 */
@Component(immediate = true)
public class MongoDataInputConfigurator {

	private static final Logger LOG = System.getLogger(MongoDataInputConfigurator.class.getName());

	/** Upstream factory PID (org.eclipse.fennec.persistence.repository.mongo); its keys are unprefixed. */
	static final String PID_REPOSITORY = "fennec.repository.mongo";
	/** The alias prefix the datasource configurator uses for materialized Mongo databases. */
	static final String MATERIALIZED_ALIAS_PREFIX = "dataAtlas.";

	private final ConfigurationAdmin configAdmin;
	private final Map<MongoDataInput, Configuration> realized = new IdentityHashMap<>();

	@Activate
	public MongoDataInputConfigurator(@Reference ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	@Deactivate
	void deactivate() {
		synchronized (realized) {
			realized.values().forEach(MongoDataInputConfigurator::delete);
			realized.clear();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addMongoDataInput(MongoDataInput input, Map<String, Object> serviceProps) {
		String id = input.getId();
		MongoDataSource dataSource = input.getDataSource();
		if (id == null || dataSource == null || dataSource.getId() == null) {
			LOG.log(Level.WARNING, () -> "Ignoring MongoDataInput without id or MongoDataSource: " + input);
			return;
		}
		String target = databaseTarget(dataSource);
		try {
			Configuration repository = configAdmin.getFactoryConfiguration(PID_REPOSITORY, id, "?");
			Dictionary<String, Object> props = new Hashtable<>();
			props.put("repositoryId", id);
			props.put("database.target", target);
			props.put("readOnly", Boolean.TRUE);
			repository.update(props);
			synchronized (realized) {
				realized.put(input, repository);
			}
			LOG.log(Level.INFO, () -> "Realized MongoDataInput '" + id + "' as " + PID_REPOSITORY
					+ " configuration (database " + target + ")");
		} catch (IOException | RuntimeException e) {
			LOG.log(Level.ERROR, () -> "Unable to realize MongoDataInput '" + id + "': " + e.getMessage(), e);
		}
	}

	void removeMongoDataInput(MongoDataInput input, Map<String, Object> serviceProps) {
		Configuration configuration;
		synchronized (realized) {
			configuration = realized.remove(input);
		}
		if (configuration != null) {
			delete(configuration);
			LOG.log(Level.INFO, () -> "Removed the repository configuration of MongoDataInput '" + input.getId() + "'");
		}
	}

	/**
	 * The target filter of the {@code MongoDatabase} service: the definition's
	 * own filter (BIND), or the alias of the database the datasource
	 * configurator materialized for the definition.
	 */
	static String databaseTarget(MongoDataSource dataSource) {
		String filter = dataSource.getFilter();
		if (filter != null && !filter.isBlank()) {
			return filter.trim();
		}
		return "(mongo.database.alias=" + MATERIALIZED_ALIAS_PREFIX + dataSource.getId() + ")";
	}

	private static void delete(Configuration configuration) {
		try {
			configuration.delete();
		} catch (IOException | RuntimeException e) {
			LOG.log(Level.WARNING, () -> "Unable to delete configuration " + configuration.getPid(), e);
		}
	}
}
