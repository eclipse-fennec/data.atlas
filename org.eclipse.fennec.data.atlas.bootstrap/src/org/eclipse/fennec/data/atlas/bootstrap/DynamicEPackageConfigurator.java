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
package org.eclipse.fennec.data.atlas.bootstrap;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;

/**
 * {@link EPackageConfigurator} for an EPackage loaded at runtime: puts the
 * package into the passed registry and, so URI-based lookups outside the
 * fennec registries (e.g. QVT) work as well, into the static
 * {@link EPackage.Registry#INSTANCE}.
 */
public class DynamicEPackageConfigurator implements EPackageConfigurator {

	private final EPackage ePackage;

	public DynamicEPackageConfigurator(EPackage ePackage) {
		this.ePackage = ePackage;
	}

	@Override
	public void configureEPackage(EPackage.Registry registry) {
		registry.put(ePackage.getNsURI(), ePackage);
		EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
	}

	@Override
	public void unconfigureEPackage(EPackage.Registry registry) {
		registry.remove(ePackage.getNsURI());
		EPackage.Registry.INSTANCE.remove(ePackage.getNsURI());
	}
}
