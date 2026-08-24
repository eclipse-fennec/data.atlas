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

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherListener;
import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * File-mode configuration source of a Data Atlas instance.
 *
 * <p>
 * Loads the {@code DataAtlasConfiguration} from the configured URI and
 * publishes it via the {@link ConfigurationRegistrar}: the EPackages of the
 * referenced model types and the configuration objects (the
 * {@code DataService}s and {@code DataInput}s) become OSGi services that the
 * per-technology configurator components pick up.
 * </p>
 *
 * <p>
 * <b>Lifecycle:</b> changes to the configuration <em>file</em> are picked up
 * by a {@code io.fs.watcher} listener on the file's directory (debounced —
 * the MDO {@code EMFFileWatcher} pattern) and applied as a diff by the
 * registrar; a changed {@code config.uri} is handled via Config Admin
 * ({@code @Modified}). Every load uses a fresh {@link ResourceSet}. A broken
 * new configuration — or a deleted file — fails hard: the published
 * configuration is torn down and the error logged loudly; the watcher keeps
 * running, so a later corrected version recovers the instance.
 * </p>
 */
@Component(name = DataAtlasBootstrap.PID, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DataAtlasBootstrap.Config.class)
public class DataAtlasBootstrap {

	public static final String PID = "DataAtlasBootstrap";

	private static final Logger LOG = System.getLogger(DataAtlasBootstrap.class.getName());

	/** Debounce for bursts of file events (editors save in several steps). */
	private static final long DEBOUNCE_MS = 1000L;

	@ObjectClassDefinition(name = "Data Atlas Bootstrap Configuration (file mode)")
	public @interface Config {

		@AttributeDefinition(name = "Configuration URI",
				description = "URI (or plain file path) of the DataAtlasConfiguration XMI describing this instance. "
						+ "To honour an environment variable, supply e.g. "
						+ "\"$[env:DATA_ATLAS_CONFIG_URI;default=/opt/dataatlas/data/dataatlas.xmi]\" via a "
						+ "configuration picked up by the Felix configadmin interpolation plugin.")
		String config_uri();
	}

	private final BundleContext bundleContext;
	private final ResourceSetFactory resourceSetFactory;
	private final ConfigurationRegistrar registrar;
	private final Timer debounce = new Timer("dataatlas-config-watcher", true);

	// guarded by this
	private URI configUri;
	private ServiceRegistration<FileSystemWatcherListener> watcherRegistration;
	private TimerTask pendingReload;

	@Activate
	public DataAtlasBootstrap(BundleContext bundleContext, @Reference ResourceSetFactory resourceSetFactory,
			Config config) {
		this.bundleContext = bundleContext;
		this.resourceSetFactory = resourceSetFactory;
		this.registrar = new ConfigurationRegistrar(bundleContext);
		synchronized (this) {
			configure(config.config_uri());
		}
	}

	@Modified
	synchronized void modified(Config config) {
		LOG.log(Level.INFO, () -> "DataAtlasBootstrap: configuration changed, re-applying");
		unregisterWatcher();
		configure(config.config_uri());
	}

	@Deactivate
	void deactivate() {
		debounce.cancel();
		synchronized (this) {
			unregisterWatcher();
		}
		registrar.unregisterAll();
	}

	private void configure(String configUriValue) {
		if (configUriValue == null || configUriValue.isBlank() || configUriValue.contains("$[")) {
			// Blank or an un-interpolated configadmin template -> nothing to do.
			LOG.log(Level.INFO, "DataAtlasBootstrap: no configuration URI configured, skipping.");
			configUri = null;
			registrar.unregisterAll();
			return;
		}
		configUri = toUri(configUriValue);
		// initial load stays fail-fast: an invalid configuration at startup (or
		// on a config.uri switch) fails loudly
		load();
		registerWatcher();
	}

	/** Loads the configuration into a fresh ResourceSet and applies it. */
	private void load() {
		LOG.log(Level.INFO, () -> "DataAtlasBootstrap: loading Data Atlas configuration from " + configUri);
		ResourceSet resourceSet = resourceSetFactory.createResourceSet();
		Resource resource = resourceSet.getResource(configUri, true);
		if (resource.getContents().isEmpty()
				|| !(resource.getContents().get(0) instanceof DataAtlasConfiguration configuration)) {
			throw new IllegalStateException(
					"DataAtlasBootstrap: " + configUri + " does not contain a DataAtlasConfiguration root");
		}
		EcoreUtil.resolveAll(resource);
		ConfigurationRegistrar.failOnUnresolvedProxies(configuration, configUri.toString());
		absolutizeFileInputUris(configuration, resource.getURI());
		registrar.apply(configuration);
	}

	/** A reload triggered by the file watcher: a broken file fails hard. */
	private synchronized void reload() {
		if (configUri == null) {
			return;
		}
		try {
			load();
		} catch (RuntimeException e) {
			LOG.log(Level.ERROR, () -> "DataAtlasBootstrap: the changed configuration at " + configUri
					+ " is broken - tearing the published configuration down", e);
			registrar.unregisterAll();
		}
	}

	/**
	 * Registers a {@code io.fs.watcher} whiteboard listener on the config
	 * file's directory, filtered to exactly this file.
	 */
	private void registerWatcher() {
		if (!configUri.isFile() || configUri.toFileString() == null) {
			LOG.log(Level.INFO, () -> "DataAtlasBootstrap: " + configUri + " is not a file, no change watching");
			return;
		}
		Path file = Path.of(configUri.toFileString()).toAbsolutePath();
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH, file.getParent().toString());
		// the watchservice matches the pattern against the FULL path string
		props.put(FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATTERN,
				".*" + Pattern.quote(java.io.File.separator + file.getFileName()));
		watcherRegistration = bundleContext.registerService(FileSystemWatcherListener.class,
				new ConfigFileListener(file), props);
		LOG.log(Level.INFO, () -> "DataAtlasBootstrap: watching " + file + " for changes");
	}

	private void unregisterWatcher() {
		if (watcherRegistration != null) {
			watcherRegistration.unregister();
			watcherRegistration = null;
		}
		if (pendingReload != null) {
			pendingReload.cancel();
			pendingReload = null;
		}
	}

	private synchronized void scheduleReload() {
		if (pendingReload != null) {
			pendingReload.cancel();
		}
		pendingReload = new TimerTask() {
			@Override
			public void run() {
				reload();
			}
		};
		debounce.schedule(pendingReload, DEBOUNCE_MS);
	}

	/** Debounced listener on the configuration file (MDO EMFFileWatcher pattern). */
	private final class ConfigFileListener implements FileSystemWatcherListener {

		private final Path file;

		ConfigFileListener(Path file) {
			this.file = file;
		}

		@Override
		public void handleBasePath(Path basePath) {
			// nothing to do: the initial state was loaded by the bootstrap
		}

		@Override
		public void handleInitialPaths(List<Path> paths) {
			// nothing to do: the initial state was loaded by the bootstrap
		}

		@Override
		public void handlePathEvent(Path path, Kind<Path> kind) {
			if (!file.equals(path.toAbsolutePath())) {
				return;
			}
			if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
				LOG.log(Level.ERROR, () -> "DataAtlasBootstrap: configuration file " + file
						+ " was deleted - tearing the published configuration down");
				synchronized (DataAtlasBootstrap.this) {
					registrar.unregisterAll();
				}
				return;
			}
			scheduleReload();
		}
	}

	private URI toUri(String value) {
		if (value.startsWith("file:")) {
			// normalize to EMF's canonical file URI form; java.net-style file URIs
			// with an empty authority (file:///C:/...) break URI.toFileString()
			return URI.createFileURI(Paths.get(java.net.URI.create(value)).toString());
		}
		// a "scheme" of length 1 is a Windows drive letter, not a URI scheme
		if (value.indexOf(":/") > 1) {
			return URI.createURI(value);
		}
		return URI.createFileURI(Paths.get(value).toAbsolutePath().toString());
	}

	/**
	 * Resolves relative FileDataInput URIs against the configuration resource,
	 * so consumers of the (detached) configuration object copies do not need
	 * the resource context.
	 */
	private void absolutizeFileInputUris(DataAtlasConfiguration configuration, URI base) {
		for (DataInput input : configuration.getDataInputs()) {
			if (input instanceof FileDataInput fileInput && fileInput.getUri() != null) {
				URI fileUri = URI.createURI(fileInput.getUri());
				if (fileUri.isRelative() && base != null) {
					fileInput.setUri(fileUri.resolve(base).toString());
				}
			}
		}
	}
}
