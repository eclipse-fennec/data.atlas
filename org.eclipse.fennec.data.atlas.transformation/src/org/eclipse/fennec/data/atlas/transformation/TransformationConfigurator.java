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
package org.eclipse.fennec.data.atlas.transformation;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.atlas.configuration.qvto.QvtoDataTransformation;
import org.eclipse.fennec.data.atlas.api.ConfigurationUris;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.api.DataTransformer;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Transformation configurator: for every {@code QvtoDataTransformation}
 * configuration service (the QVT-O binding of the shared atlas configuration
 * model's {@code DataTransformation}) it registers one ready-to-execute
 * {@link DataTransformer}, correlated via
 * {@link DataAtlasConstants#TRANSFORMATION_ID} = the transformation's id; the
 * transformer is unregistered when the configuration service goes away.
 *
 * <p>
 * The executable AST comes from exactly one of two places:
 * </p>
 * <ul>
 * <li>{@code transformation}: the compiled AST inside a CompiledUnit document.
 * It is copied at registration time, so the transformer never dangles into a
 * configuration the bootstrap has replaced.</li>
 * <li>{@code source}: the URI of a {@code .qvto} source, parsed here when the
 * configuration is applied (data.atlas#29). A relative URI resolves against
 * the configuration document ({@link ConfigurationUris}).</li>
 * </ul>
 * <p>
 * A transformation with both or neither set, an unresolvable AST, a source
 * that does not parse, or one that does not declare exactly one supported and
 * one result EClass (the transformers registered here serve the 1:1 bridge) is
 * refused loudly and no transformer appears, keeping every dependent endpoint
 * down (fail-early gating).
 * </p>
 */
@Component
public class TransformationConfigurator {

	private static final Logger LOG = System.getLogger(TransformationConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ComponentServiceObjects<QvtoEngine> engines;

	private record Registered(QvtoEngine engine, ServiceRegistration<?> registration) {
	}

	private final Map<QvtoDataTransformation, Registered> registrations = new IdentityHashMap<>();

	@Activate
	public TransformationConfigurator(BundleContext bundleContext,
			@Reference ComponentServiceObjects<QvtoEngine> engines) {
		this.bundleContext = bundleContext;
		this.engines = engines;
	}

	@Deactivate
	void deactivate() {
		synchronized (registrations) {
			registrations.values().forEach(this::unregister);
			registrations.clear();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addDataTransformation(QvtoDataTransformation transformation, Map<String, Object> serviceProps) {
		String id = transformation.getId();
		if (id == null || id.isBlank()) {
			LOG.log(Level.WARNING, () -> "Ignoring QvtoDataTransformation without id: " + transformation);
			return;
		}
		OperationalTransformation ast = transformation.getTransformation();
		String source = transformation.getSource();
		boolean hasSource = source != null && !source.isBlank();
		if ((ast != null) == hasSource) {
			LOG.log(Level.ERROR, () -> "QvtoDataTransformation '" + id
					+ "' must set exactly one of 'transformation' (compiled AST) and 'source' (.qvto URI), found "
					+ (hasSource ? "both" : "neither") + " - no transformer is registered");
			return;
		}
		if (ast != null && ast.eIsProxy()) {
			LOG.log(Level.ERROR, () -> "QvtoDataTransformation '" + id
					+ "' references no resolvable QVT-O transformation - no transformer is registered");
			return;
		}
		if (transformation.getSupportedEClasses().size() != 1 || transformation.getResultEClasses().size() != 1) {
			LOG.log(Level.ERROR, () -> "QvtoDataTransformation '" + id
					+ "' must declare exactly one supported and one result EClass (the bridge runs transformations 1:1), found "
					+ transformation.getSupportedEClasses().size() + "/" + transformation.getResultEClasses().size()
					+ " - no transformer is registered");
			return;
		}
		EClass inputType = transformation.getSupportedEClasses().get(0);
		EClass outputType = transformation.getResultEClasses().get(0);
		// one PROTOTYPE engine per transformer, with its own caches. Since
		// emf.m2x#245 the DS engine binds the emf.osgi ResourceSet itself, so
		// it resolves model types against the dynamically registered packages
		// - the registrar publishes them before the configuration objects, so
		// they are present when the engine binds here (also for parsing).
		QvtoEngine engine = engines.getService();
		OperationalTransformation executable;
		if (hasSource) {
			String sourceUri = ConfigurationUris.resolve(source, serviceProps);
			try {
				executable = engine.parse(URI.createURI(sourceUri));
			} catch (QvtoParseException | RuntimeException e) {
				engines.ungetService(engine);
				LOG.log(Level.ERROR, () -> "QvtoDataTransformation '" + id + "': the QVT-O source " + sourceUri
						+ " cannot be compiled - no transformer is registered", e);
				return;
			}
		} else {
			// own copy: the AST must not dangle into a replaced configuration.
			// The AST lives inside a CompiledUnit document whose satellites
			// (variables, type instances - the parser's leftovers a bare AST
			// dangles on) sit OUTSIDE the AST subtree, so the whole root
			// container is copied and the AST is picked out of the copy -
			// copying only the AST would leave its satellite references
			// pointing into the replaced configuration.
			executable = copyWithDocument(ast);
		}
		QvtoDataTransformer transformer = new QvtoDataTransformer(id, inputType, outputType, executable, engine);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(DataAtlasConstants.TRANSFORMATION_ID, id);
		Object atlasName = serviceProps.get(DataAtlasConstants.ATLAS_NAME);
		if (atlasName != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, atlasName);
		}
		ServiceRegistration<?> registration = bundleContext.registerService(DataTransformer.class.getName(),
				transformer, props);
		synchronized (registrations) {
			registrations.put(transformation, new Registered(engine, registration));
		}
		LOG.log(Level.INFO, () -> "Registered QVT-O DataTransformer for QvtoDataTransformation '" + id + "' ("
				+ typeName(inputType) + " -> " + typeName(outputType) + (hasSource ? ", compiled from " + source : "")
				+ ")");
	}

	void removeDataTransformation(QvtoDataTransformation transformation, Map<String, Object> serviceProps) {
		Registered registered;
		synchronized (registrations) {
			registered = registrations.remove(transformation);
		}
		if (registered != null) {
			unregister(registered);
		}
	}

	private void unregister(Registered registered) {
		registered.registration().unregister();
		engines.ungetService(registered.engine());
	}

	/**
	 * Copies the AST together with its containing document (the CompiledUnit
	 * with the satellites) and returns the copied AST.
	 */
	private static OperationalTransformation copyWithDocument(OperationalTransformation ast) {
		EObject root = EcoreUtil.getRootContainer(ast);
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		copier.copy(root);
		copier.copyReferences();
		return (OperationalTransformation) copier.getOrDefault(ast, ast);
	}

	private String typeName(EClass eClass) {
		return eClass.getEPackage() != null ? eClass.getEPackage().getNsURI() + "#" + eClass.getName()
				: eClass.getName();
	}
}
