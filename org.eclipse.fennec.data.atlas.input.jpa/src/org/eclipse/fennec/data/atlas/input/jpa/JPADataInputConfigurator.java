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
package org.eclipse.fennec.data.atlas.input.jpa;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.JPADataInput;
import org.eclipse.fennec.data.atlas.configuration.JdbcDataSource;
import org.eclipse.fennec.persistence.eorm.EntityMappings;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Input configurator: translates every {@code JPADataInput} configuration
 * service into the fennec persistence factory configurations
 * <ol>
 * <li>{@code fennec.jpa.EORMMappingService} — derives an {@code EntityMappings}
 * from the input's {@code supportedEClasses} (only when the input carries no
 * {@code persistenceConfig}; a provided one is registered directly as an
 * {@code EntityMappings} service instead),</li>
 * <li>{@code fennec.jpa.EMPersistenceUnit} — binds the mappings and the
 * {@code DataSource} selected by the input's {@code JdbcDataSource.filter},</li>
 * <li>{@code fennec.repository.jpa} — publishes the read-only repository with
 * {@code persistence.repository.id} = the input's id, which is how the
 * endpoint configurators pick the input up.</li>
 * </ol>
 * All three configurations (and the mapping service, if registered) are removed
 * when the input's configuration service goes away. The upstream configuration
 * chain is documented in eclipse-fennec/emf.persistence-jpa (see issue #193 for
 * the end-to-end recipe).
 */
@Component(immediate = true)
public class JPADataInputConfigurator {

	private static final Logger LOG = System.getLogger(JPADataInputConfigurator.class.getName());

	// upstream factory PIDs and configuration keys; note the mandatory key
	// prefixes on the eorm/persistence-unit PIDs (unprefixed keys are ignored)
	private static final String PID_EORM_MAPPING = "fennec.jpa.EORMMappingService";
	private static final String PID_PERSISTENCE_UNIT = "fennec.jpa.EMPersistenceUnit";
	private static final String PID_REPOSITORY = "fennec.repository.jpa";

	private final BundleContext bundleContext;
	private final ConfigurationAdmin configAdmin;

	private record Realized(List<Configuration> configurations, ServiceRegistration<EntityMappings> mappings) {
	}

	private final Map<JPADataInput, Realized> realized = new IdentityHashMap<>();

	@Activate
	public JPADataInputConfigurator(BundleContext bundleContext, @Reference ConfigurationAdmin configAdmin) {
		this.bundleContext = bundleContext;
		this.configAdmin = configAdmin;
	}

	@Deactivate
	void deactivate() {
		synchronized (realized) {
			realized.values().forEach(JPADataInputConfigurator::tearDown);
			realized.clear();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addJPADataInput(JPADataInput input, Map<String, Object> serviceProps) {
		String id = input.getId();
		JdbcDataSource dataSource = input.getDataSource();
		if (id == null || dataSource == null || dataSource.getFilter() == null) {
			LOG.log(Level.WARNING, () -> "Ignoring JPADataInput without id or JdbcDataSource filter: " + input);
			return;
		}
		try {
			Realized result = realize(input, id, dataSource.getFilter());
			synchronized (realized) {
				realized.put(input, result);
			}
			LOG.log(Level.INFO, () -> "Realized JPADataInput '" + id + "' as persistence configurations (dataSource "
					+ dataSource.getFilter() + ")");
		} catch (IOException | RuntimeException e) {
			LOG.log(Level.ERROR, () -> "Unable to realize JPADataInput '" + id + "': " + e.getMessage(), e);
		}
	}

	void removeJPADataInput(JPADataInput input, Map<String, Object> serviceProps) {
		Realized result;
		synchronized (realized) {
			result = realized.remove(input);
		}
		if (result != null) {
			tearDown(result);
			LOG.log(Level.INFO, () -> "Removed persistence configurations of JPADataInput '" + input.getId() + "'");
		}
	}

	private Realized realize(JPADataInput input, String id, String dataSourceFilter) throws IOException {
		List<Configuration> configurations = new ArrayList<>();
		ServiceRegistration<EntityMappings> mappingRegistration = null;
		String mappingTarget;
		try {
			if (input.getPersistenceConfig() != null) {
				// a provided mapping is registered as-is; the copy detaches it
				// from the configuration model tree
				Dictionary<String, Object> props = new Hashtable<>();
				props.put(DataAtlasConstants.CONFIG_OBJECT_ID, id);
				mappingRegistration = bundleContext.registerService(EntityMappings.class,
						EcoreUtil.copy(input.getPersistenceConfig()), props);
				mappingTarget = "(" + DataAtlasConstants.CONFIG_OBJECT_ID + "=" + id + ")";
			} else {
				configurations.add(eormMappingConfiguration(input, id));
				mappingTarget = "(fennec.jpa.eorm.mapping=" + id + ")";
			}

			Configuration unit = configAdmin.getFactoryConfiguration(PID_PERSISTENCE_UNIT, id, "?");
			Dictionary<String, Object> unitProps = new Hashtable<>();
			unitProps.put("fennec.jpa.persistenceUnitName", id);
			unitProps.put("fennec.jpa.dataSource.target", dataSourceFilter);
			unitProps.put("fennec.jpa.mapping.target", mappingTarget);
			unit.update(unitProps);
			configurations.add(unit);

			Configuration repository = configAdmin.getFactoryConfiguration(PID_REPOSITORY, id, "?");
			Dictionary<String, Object> repositoryProps = new Hashtable<>();
			// these keys are deliberately unprefixed, unlike the fennec.jpa.* ones
			repositoryProps.put("repositoryId", id);
			repositoryProps.put("unit.target", "(osgi.unit.name=" + id + ")");
			repositoryProps.put("readOnly", Boolean.TRUE);
			repository.update(repositoryProps);
			configurations.add(repository);

			return new Realized(configurations, mappingRegistration);
		} catch (IOException | RuntimeException e) {
			tearDown(new Realized(configurations, mappingRegistration));
			throw e;
		}
	}

	/**
	 * Mapping-free path: derive the {@code EntityMappings} from the input's
	 * {@code supportedEClasses}. The EClass names must be listed explicitly
	 * (there is no "map all"), and the upstream component maps against exactly
	 * one EPackage — targeted by nsURI, never by the non-unique package name.
	 */
	private Configuration eormMappingConfiguration(JPADataInput input, String id) throws IOException {
		List<EClass> eClasses = input.getSupportedEClasses();
		if (eClasses.isEmpty()) {
			throw new IllegalStateException(
					"JPADataInput '" + id + "' has neither a persistenceConfig nor supportedEClasses");
		}
		Set<EPackage> ePackages = new LinkedHashSet<>();
		List<String> names = new ArrayList<>();
		for (EClass eClass : eClasses) {
			ePackages.add(eClass.getEPackage());
			names.add(eClass.getName());
		}
		if (ePackages.size() > 1 || ePackages.contains(null)) {
			throw new IllegalStateException("JPADataInput '" + id
					+ "': the derived mapping requires all supportedEClasses in one EPackage, found: " + ePackages);
		}
		String nsUri = ePackages.iterator().next().getNsURI();
		Configuration mapping = configAdmin.getFactoryConfiguration(PID_EORM_MAPPING, id, "?");
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + nsUri + ")");
		props.put("fennec.jpa.eorm.eClasses", names.toArray(String[]::new));
		props.put("fennec.jpa.eorm.mappingName", id);
		mapping.update(props);
		return mapping;
	}

	private static void tearDown(Realized result) {
		for (Configuration configuration : result.configurations()) {
			try {
				configuration.delete();
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.WARNING, () -> "Unable to delete configuration " + configuration.getPid(), e);
			}
		}
		if (result.mappings() != null) {
			try {
				result.mappings().unregister();
			} catch (IllegalStateException e) {
				// already unregistered
			}
		}
	}
}
