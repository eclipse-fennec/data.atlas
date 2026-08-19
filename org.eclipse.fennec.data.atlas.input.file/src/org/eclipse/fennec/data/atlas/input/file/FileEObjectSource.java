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
package org.eclipse.fennec.data.atlas.input.file;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.data.atlas.api.EObjectSource;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;

/**
 * {@link EObjectSource} over EMF resource files: loads the file (or every
 * regular file of a directory) configured on the {@code FileDataInput} into a
 * fresh {@link ResourceSet} per call, so each caller owns its data.
 */
public class FileEObjectSource implements EObjectSource {

	private final FileDataInput input;
	private final ResourceSetFactory resourceSetFactory;

	public FileEObjectSource(FileDataInput input, ResourceSetFactory resourceSetFactory) {
		this.input = input;
		this.resourceSetFactory = resourceSetFactory;
	}

	@Override
	public Resource loadContents() {
		ResourceSet resourceSet = resourceSetFactory.createResourceSet();
		URI uri = URI.createURI(input.getUri());
		if (uri.isFile() && uri.toFileString() != null) {
			// java.io.File tolerates URI-mangled path strings that Path.of rejects
			File file = new File(uri.toFileString());
			if (file.isDirectory()) {
				return loadDirectory(resourceSet, file.toPath());
			}
		}
		return resourceSet.getResource(uri, true);
	}

	private Resource loadDirectory(ResourceSet resourceSet, Path directory) {
		Resource target = resourceSet
				.createResource(URI.createFileURI(directory.resolve("contents.xmi").toString()));
		List<Path> files;
		try (Stream<Path> stream = Files.list(directory)) {
			files = stream.filter(Files::isRegularFile).sorted().toList();
		} catch (IOException e) {
			throw new UncheckedIOException("Unable to list data input directory " + directory, e);
		}
		for (Path file : files) {
			Resource resource = resourceSet.getResource(URI.createFileURI(file.toString()), true);
			// moving the contents detaches them from their source resource
			target.getContents().addAll(new ArrayList<>(resource.getContents()));
		}
		return target;
	}
}
