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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;

/**
 * Authoring tool for the example transformation: compiles
 * example/trafo/person-to-public.qvto with the fennec m2x QVT-O engine into a
 * self-contained CompiledUnit document (a bare parsed AST is not storable -
 * the parser's satellites live outside any resource) and serializes it twice:
 *
 * - person-to-public.xmi        file-relative metamodel hrefs (file mode)
 * - person-to-public-atlas.xmi  nsURI-based metamodel hrefs (Model Atlas mode)
 *
 * Usage: java -cp <m2x+emf jars> AstGen.java <path-to-example-dir>
 */
public class AstGen {

	public static void main(String[] args) throws Exception {
		Path example = Path.of(args[0]);

		// file-relative variant: package resources keep their file URIs
		write(example, false, example.resolve("trafo/person-to-public.xmi"));
		// atlas variant: package resources renamed to their nsURIs
		write(example, true, example.resolve("trafo/person-to-public-atlas.xmi"));
	}

	private static void write(Path example, boolean nsUriRefs, Path target) throws Exception {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		EPackage person = loadPackage(resourceSet, example.resolve("model/person.ecore"), nsUriRefs);
		EPackage publicPerson = loadPackage(resourceSet, example.resolve("model/person-public.ecore"), nsUriRefs);
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(person.getNsURI(), person);
		registry.put(publicPerson.getNsURI(), publicPerson);

		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoConfiguration configuration = QvtoConfiguration.builder(ocl).packageRegistry(registry).build();
		QvtoEngine engine = QvtoEngines.create(configuration);

		String source = Files.readString(example.resolve("trafo/person-to-public.qvto"));
		CompiledUnit compiled = engine.compile(source, "PersonToPublic");

		Resource resource = resourceSet.createResource(URI.createFileURI(target.toAbsolutePath().toString()));
		resource.getContents().add(compiled);
		resource.save(Map.of());
		System.out.println("wrote " + target.toAbsolutePath());
	}

	private static EPackage loadPackage(ResourceSet resourceSet, Path ecore, boolean nsUriRefs) {
		Resource resource = resourceSet.getResource(URI.createFileURI(ecore.toAbsolutePath().toString()), true);
		EPackage ePackage = (EPackage) resource.getContents().get(0);
		if (nsUriRefs) {
			// cross-references into the package serialize nsURI-based
			resource.setURI(URI.createURI(ePackage.getNsURI()));
		}
		return ePackage;
	}
}
