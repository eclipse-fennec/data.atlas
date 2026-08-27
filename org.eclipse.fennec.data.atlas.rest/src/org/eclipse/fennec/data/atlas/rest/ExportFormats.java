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
package org.eclipse.fennec.data.atlas.rest;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.fennec.codec.csv.CodecCsvOptions;
import org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Variant;

/**
 * The effective serialization formats of one DataSet: the media types it may be
 * served as, plus the fennec codec save options belonging to each of them.
 *
 * <p>
 * Resolved from the {@code DistributionExport} templates of the configuration
 * model with the usual override-else-default rule — a DataSet's own exports
 * fully replace the enclosing service's, an empty set falls back to the
 * service's. Two outcomes:
 * </p>
 * <ul>
 * <li><b>no export resolved</b> — {@link #defaults()}: the runtime serves
 * {@code application/json} and {@code application/xml}, which is what every
 * configuration without an {@code exports} registry did before exports became
 * executable.</li>
 * <li><b>at least one export resolved</b> — exactly those media types are
 * served; the endpoint answers anything else with {@code 406}.</li>
 * </ul>
 *
 * <p>
 * The media type of an export is its {@code mediaType} attribute, or the
 * kind-specific default when that is unset ({@code CSVDistributionExport} →
 * {@code text/csv}, or {@code application/x-csv-zip} when {@code compressed}
 * is set). A plain {@code DistributionExport} without {@code mediaType} names
 * no format at all and is therefore a configuration error.
 * </p>
 */
public final class ExportFormats {

	private static final Logger LOG = System.getLogger(ExportFormats.class.getName());

	/** {@code text/csv} — the CSV codec's content type. */
	public static final String TEXT_CSV = "text/csv";
	/** {@code application/x-csv-zip} — the CSV codec's zipped multi-table content type. */
	public static final String APPLICATION_CSV_ZIP = "application/x-csv-zip";

	static final MediaType TEXT_CSV_TYPE = MediaType.valueOf(TEXT_CSV);
	static final MediaType APPLICATION_CSV_ZIP_TYPE = MediaType.valueOf(APPLICATION_CSV_ZIP);

	private static final ExportFormats DEFAULTS = new ExportFormats(
			List.of(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE), Map.of(), true);

	private final List<MediaType> mediaTypes;
	private final List<Variant> variants;
	private final Map<String, Map<String, Object>> optionsByType;
	private final boolean runtimeDefaults;

	private ExportFormats(List<MediaType> mediaTypes, Map<String, Map<String, Object>> optionsByType,
			boolean runtimeDefaults) {
		this.mediaTypes = List.copyOf(mediaTypes);
		this.variants = Variant.mediaTypes(this.mediaTypes.toArray(MediaType[]::new)).build();
		this.optionsByType = Map.copyOf(optionsByType);
		this.runtimeDefaults = runtimeDefaults;
	}

	/** The formats served when a DataSet resolves to no {@code DistributionExport}. */
	public static ExportFormats defaults() {
		return DEFAULTS;
	}

	/**
	 * Resolves the formats of {@code dataSet} within {@code service}.
	 *
	 * @param dataSet the DataSet to resolve, never {@code null}
	 * @param service the enclosing DataService, providing the fallback exports
	 * @return the resolved formats, {@link #defaults()} if no export applies,
	 *         or {@code null} if exports are declared but none of them names a
	 *         media type — a configuration error the caller must not serve
	 */
	public static ExportFormats resolve(DataSet dataSet, DataService service) {
		List<DistributionExport> exports = dataSet.getDistributionExport().isEmpty()
				? service.getDistributionExport()
				: dataSet.getDistributionExport();
		if (exports.isEmpty()) {
			return defaults();
		}
		List<MediaType> types = new ArrayList<>();
		Map<String, Map<String, Object>> options = new LinkedHashMap<>();
		for (DistributionExport export : exports) {
			MediaType mediaType = mediaTypeOf(export, dataSet);
			if (mediaType == null) {
				continue;
			}
			String key = key(mediaType);
			if (options.containsKey(key)) {
				LOG.log(Level.WARNING, () -> "DataSet '" + dataSet.getId() + "': export '" + export.getId()
						+ "' repeats media type '" + key + "', keeping the first declaration");
				continue;
			}
			types.add(mediaType);
			options.put(key, optionsOf(export));
		}
		if (types.isEmpty()) {
			LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId()
					+ "': none of its DistributionExports names a media type");
			return null;
		}
		return new ExportFormats(types, options, false);
	}

	/**
	 * The media type an export produces: its {@code mediaType}, else the
	 * kind-specific default, else {@code null} for an export that names none.
	 */
	private static MediaType mediaTypeOf(DistributionExport export, DataSet dataSet) {
		String declared = export.getMediaType();
		if (declared != null && !declared.isBlank()) {
			try {
				return MediaType.valueOf(declared.trim());
			} catch (IllegalArgumentException e) {
				LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId() + "': export '" + export.getId()
						+ "' declares an unparseable mediaType '" + declared + "', ignoring the export");
				return null;
			}
		}
		if (export instanceof CSVDistributionExport csv) {
			return csv.isCompressed() ? APPLICATION_CSV_ZIP_TYPE : TEXT_CSV_TYPE;
		}
		LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId() + "': export '" + export.getId()
				+ "' has no mediaType and no kind-specific default, ignoring the export");
		return null;
	}

	/**
	 * The codec save options of an export. CSV maps onto the codec's option
	 * keys rather than reimplementing them; {@code includeTypeHeader} is always
	 * passed on because the codec's own default for the SQL-type row is
	 * {@code true} while the model's default is {@code false}.
	 */
	private static Map<String, Object> optionsOf(DistributionExport export) {
		if (!(export instanceof CSVDistributionExport csv)) {
			return Map.of();
		}
		Map<String, Object> options = new LinkedHashMap<>();
		String separator = csv.getSeparator();
		if (separator != null && !separator.isEmpty()) {
			options.put(CodecCsvOptions.OPTION_DELIMITER, Character.valueOf(separator.charAt(0)));
		}
		options.put(CodecCsvOptions.OPTION_DATA_TYPE_IN_SECOND_ROW, Boolean.valueOf(csv.isIncludeTypeHeader()));
		return Map.copyOf(options);
	}

	private static String key(MediaType mediaType) {
		return (mediaType.getType() + "/" + mediaType.getSubtype()).toLowerCase();
	}

	/** The variants to negotiate against, in declaration order. */
	public List<Variant> variants() {
		return variants;
	}

	/** The declared media types, in declaration order. */
	public List<MediaType> mediaTypes() {
		return mediaTypes;
	}

	/** {@code true} for {@link #defaults()} — no export was configured. */
	public boolean isRuntimeDefaults() {
		return runtimeDefaults;
	}

	/**
	 * The configured codec save options for a negotiated media type; empty when
	 * the export carries none.
	 */
	public Map<String, Object> optionsFor(MediaType mediaType) {
		return optionsByType.getOrDefault(key(mediaType), Map.of());
	}

	@Override
	public String toString() {
		return mediaTypes.toString();
	}
}
