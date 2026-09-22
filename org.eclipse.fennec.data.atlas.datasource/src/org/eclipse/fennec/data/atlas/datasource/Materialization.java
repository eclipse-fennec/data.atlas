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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.ConnectionProperty;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataSource;
import org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource;
import org.eclipse.fennec.data.atlas.configuration.JdbcDataSource;
import org.eclipse.fennec.data.atlas.configuration.JdbcDriver;
import org.eclipse.fennec.data.atlas.configuration.MongoDataSource;

/**
 * The pure part of the datasource configurator: decides the mode of a
 * {@code DataSource} definition and translates a MATERIALIZE definition into
 * the backend's Config Admin factory configurations.
 *
 * <ul>
 * <li>{@code JdbcDataSource} → one
 * {@code daanse.jdbc.datasource.<driver>.DataSource~dataAtlas.<id>}; the daanse
 * components publish every non-dot key as a service property, so the marker
 * {@code data.atlas.datasource.id=<id>} is what the input configurators
 * target.</li>
 * <li>{@code MongoDataSource} → one {@code persistence.mongo.client} (the
 * connection string, liveness-gated) and one
 * {@code persistence.mongo.database} (alias {@code dataAtlas.<id>}) of the
 * fennec Mongo backend; the alias is what the input configurator targets.</li>
 * </ul>
 * Credentials never reach the configuration as values: {@code user} and
 * {@code password} must be {@code $[env:NAME]} / {@code $[secret:NAME]}
 * placeholders (see {@link Placeholders}), and their targets must exist in this
 * runtime - a missing environment variable or secret file is a deployment
 * error worth failing loudly for at registration instead of silently at the
 * first connection attempt.
 */
final class Materialization {

	/** Factory configuration name prefix, like the other Data Atlas configurators. */
	static final String CONFIG_NAME_PREFIX = "dataAtlas.";

	static final String PID_MONGO_CLIENT = "persistence.mongo.client";
	static final String PID_MONGO_DATABASE = "persistence.mongo.database";

	private static final Set<EStructuralFeature> IDENTITY_FEATURES = Set.of(DAConfigPackage.Literals.DATA_SOURCE__ID,
			DAConfigPackage.Literals.DATA_SOURCE__NAME, DAConfigPackage.Literals.DATA_SOURCE__DESCRIPTION,
			DAConfigPackage.Literals.DATA_SOURCE__FILTER);

	/** How a definition is realized. */
	enum Mode {
		/** {@code filter} set: the deployment provides the backend service. */
		BIND,
		/** Connection coordinates set: this runtime creates the backend service. */
		MATERIALIZE
	}

	/** One factory configuration to create. */
	record FactoryConfiguration(String factoryPid, String name, Dictionary<String, Object> properties) {
	}

	/** What the plan may ask the runtime about. */
	interface Environment {
		boolean environmentVariablePresent(String name);

		/** The secrets directory of the interpolation plugin, empty when not configured. */
		Optional<Path> secretsDirectory();

		/** Lower-cased literal hosts a definition may connect to; empty = no restriction. */
		Set<String> hostAllowList();
	}

	private Materialization() {
	}

	/** The mode of a definition, or an exception when both or neither are declared. */
	static Mode mode(DataSource dataSource) throws DataSourceDefinitionException {
		boolean filter = dataSource.getFilter() != null && !dataSource.getFilter().isBlank();
		boolean definition = definitionPresent(dataSource);
		if (filter && definition) {
			throw new DataSourceDefinitionException(describe(dataSource)
					+ " declares a filter AND connection coordinates - a data source is either bound (filter) or materialized (coordinates), not both");
		}
		if (!filter && !definition) {
			throw new DataSourceDefinitionException(describe(dataSource)
					+ " declares neither a filter nor connection coordinates");
		}
		return filter ? Mode.BIND : Mode.MATERIALIZE;
	}

	/** Whether any feature beyond identity and filter is set. */
	static boolean definitionPresent(DataSource dataSource) {
		for (EStructuralFeature feature : dataSource.eClass().getEAllStructuralFeatures()) {
			if (IDENTITY_FEATURES.contains(feature)) {
				continue;
			}
			if (feature.isMany() ? !((List<?>) dataSource.eGet(feature)).isEmpty() : dataSource.eIsSet(feature)) {
				return true;
			}
		}
		return false;
	}

	/** The factory configurations realizing a MATERIALIZE definition. */
	static List<FactoryConfiguration> plan(DataSource dataSource, Environment environment)
			throws DataSourceDefinitionException {
		if (dataSource instanceof JdbcDataSource jdbc) {
			return List.of(jdbc(jdbc, environment));
		}
		if (dataSource instanceof MongoDataSource mongo) {
			return mongo(mongo, environment);
		}
		throw new DataSourceDefinitionException(describe(dataSource) + " is of an unsupported kind "
				+ dataSource.eClass().getName() + " - only JdbcDataSource and MongoDataSource can be materialized");
	}

	static String configurationName(DataSource dataSource) {
		return CONFIG_NAME_PREFIX + dataSource.getId();
	}

	// --- JDBC ---------------------------------------------------------------

	private static FactoryConfiguration jdbc(JdbcDataSource source, Environment environment)
			throws DataSourceDefinitionException {
		JdbcDriver driver = source.getDriver() == null ? JdbcDriver.POSTGRESQL : source.getDriver();
		Dictionary<String, Object> props = new Hashtable<>();
		switch (driver) {
		case POSTGRESQL -> {
			require(source, "host", source.getHost());
			require(source, "database", source.getDatabase());
			checkHost(source, environment);
			props.put("host", source.getHost().trim());
			props.put("dbname", source.getDatabase().trim());
			if (source.getPort() != null) {
				props.put("port", source.getPort());
			}
			putCredential(props, "user", source.getUser(), source, environment);
			putCredential(props, ".password", source.getPassword(), source, environment);
			if (set(source.getSchema())) {
				props.put("currentSchema", source.getSchema().trim());
			}
		}
		case H2 -> {
			require(source, "database", source.getDatabase());
			if (set(source.getHost()) || source.getPort() != null || set(source.getSchema())) {
				throw new DataSourceDefinitionException(describe(source)
						+ ": host, port and schema do not apply to the H2 driver (database is the H2 identifier - a name or path)");
			}
			props.put("identifier", source.getDatabase().trim());
			putCredential(props, "username", source.getUser(), source, environment);
			putCredential(props, ".password", source.getPassword(), source, environment);
		}
		}
		props.put(DataAtlasConstants.DATASOURCE_ID, source.getId());
		putProperties(props, source);
		return new FactoryConfiguration("daanse.jdbc.datasource." + driver.getLiteral() + ".DataSource",
				configurationName(source), props);
	}

	// --- MongoDB -------------------------------------------------------------

	private static List<FactoryConfiguration> mongo(MongoDataSource source, Environment environment)
			throws DataSourceDefinitionException {
		require(source, "host", source.getHost());
		require(source, "database", source.getDatabase());
		checkHost(source, environment);
		if (set(source.getPassword()) && !set(source.getUser())) {
			throw new DataSourceDefinitionException(describe(source) + " declares a password without a user");
		}
		validateCredential(source, "user", source.getUser(), environment);
		validateCredential(source, "password", source.getPassword(), environment);

		// the placeholders stay verbatim in the connection string; the
		// interpolation plugin substitutes them when the configuration is
		// delivered to the client component
		StringBuilder connection = new StringBuilder("mongodb://");
		if (set(source.getUser())) {
			connection.append(source.getUser().trim());
			if (set(source.getPassword())) {
				connection.append(':').append(source.getPassword().trim());
			}
			connection.append('@');
		}
		connection.append(source.getHost().trim());
		if (source.getPort() != null) {
			connection.append(':').append(source.getPort());
		}
		connection.append('/');
		if (set(source.getAuthSource())) {
			connection.append("?authSource=").append(source.getAuthSource().trim());
		}

		String ident = configurationName(source);
		Dictionary<String, Object> client = new Hashtable<>();
		client.put("ident", ident);
		client.put("connectionString", connection.toString());
		if (set(source.getFlavor())) {
			client.put("flavor", source.getFlavor().trim());
		}
		client.put(DataAtlasConstants.DATASOURCE_ID, source.getId());
		putProperties(client, source);

		Dictionary<String, Object> database = new Hashtable<>();
		database.put("alias", ident);
		database.put("database", source.getDatabase().trim());
		database.put("client.target", "(mongo.client.ident=" + ident + ")");
		database.put(DataAtlasConstants.DATASOURCE_ID, source.getId());

		return List.of(new FactoryConfiguration(PID_MONGO_CLIENT, ident, client),
				new FactoryConfiguration(PID_MONGO_DATABASE, ident, database));
	}

	// --- shared rules -------------------------------------------------------

	private static void putCredential(Dictionary<String, Object> props, String key, String value,
			DataSource source, Environment environment) throws DataSourceDefinitionException {
		validateCredential(source, key.startsWith(".") ? key.substring(1) : key, value, environment);
		if (set(value)) {
			props.put(key, value.trim());
		}
	}

	/**
	 * The credential rule: a set {@code user}/{@code password} is exactly one
	 * {@code $[env:NAME]} or {@code $[secret:NAME]} placeholder whose target
	 * exists in this runtime.
	 */
	static void validateCredential(DataSource source, String attribute, String value, Environment environment)
			throws DataSourceDefinitionException {
		if (!set(value)) {
			return;
		}
		Placeholders.Reference reference = Placeholders.credential(value)
				.orElseThrow(() -> new DataSourceDefinitionException(describe(source) + ": " + attribute
						+ " must be a $[env:NAME] or $[secret:NAME] placeholder, never a literal value or a placeholder with a default"));
		switch (reference.type()) {
		case Placeholders.TYPE_ENV -> {
			if (!environment.environmentVariablePresent(reference.name())) {
				throw new DataSourceDefinitionException(describe(source) + ": " + attribute
						+ " references the environment variable " + reference.name() + ", which is not set");
			}
		}
		case Placeholders.TYPE_SECRET -> {
			Path directory = environment.secretsDirectory()
					.orElseThrow(() -> new DataSourceDefinitionException(describe(source) + ": " + attribute
							+ " references the secret " + reference.name() + ", but this runtime has no secrets directory ("
							+ Placeholders.SECRETS_DIR_PROPERTY + " is not set)"));
			if (!Files.isRegularFile(directory.resolve(reference.name()))) {
				throw new DataSourceDefinitionException(describe(source) + ": " + attribute
						+ " references the secret " + reference.name() + ", which does not exist in " + directory);
			}
		}
		default -> throw new IllegalStateException(reference.type());
		}
	}

	/**
	 * The allow-list rule: when the runtime restricts hosts, the host must be a
	 * literal (a placeholder cannot be checked) and on the list.
	 */
	private static void checkHost(DatabaseDataSource source, Environment environment)
			throws DataSourceDefinitionException {
		Set<String> allowed = environment.hostAllowList();
		if (allowed.isEmpty()) {
			return;
		}
		String host = source.getHost().trim();
		if (Placeholders.containsPlaceholder(host)) {
			throw new DataSourceDefinitionException(describe(source) + ": host '" + host
					+ "' is a placeholder, which cannot be checked against the host allow-list of this runtime");
		}
		if (!allowed.contains(host.toLowerCase(Locale.ROOT))) {
			throw new DataSourceDefinitionException(
					describe(source) + ": host '" + host + "' is not on the host allow-list of this runtime " + allowed);
		}
	}

	/** Driver-specific pass-through settings; derived keys win, so they cannot be overridden. */
	private static void putProperties(Dictionary<String, Object> props, DatabaseDataSource source)
			throws DataSourceDefinitionException {
		for (ConnectionProperty property : source.getProperties()) {
			String key = property.getKey() == null ? "" : property.getKey().trim();
			if (key.isEmpty() || property.getValue() == null) {
				throw new DataSourceDefinitionException(describe(source) + " has a connection property without key or value");
			}
			if (props.get(key) != null) {
				throw new DataSourceDefinitionException(describe(source) + ": connection property '" + key
						+ "' would override a key derived from the definition's attributes");
			}
			props.put(key, property.getValue());
		}
	}

	private static void require(DataSource source, String attribute, String value)
			throws DataSourceDefinitionException {
		if (!set(value)) {
			throw new DataSourceDefinitionException(describe(source) + " is materialized but declares no " + attribute);
		}
	}

	private static boolean set(String value) {
		return value != null && !value.isBlank();
	}

	static String describe(DataSource source) {
		return source.eClass().getName() + " '" + source.getId() + "'";
	}
}
