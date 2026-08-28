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
package org.eclipse.fennec.data.atlas.publication.dcat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;

import dcat.DcatFactory;
import dcat.DcatResource;
import foaf.Agent;
import foaf.FoafFactory;
import rdf.PlainLiteral;
import rdf.RdfFactory;
import terms.LicenseDocument;
import terms.TermsFactory;

/**
 * Translates one published Fennec {@code DataService} — DataService-first, the
 * unit that owns the endpoint — into the DCAT entities the portal expects:
 * the service as {@code dcat:DataService}, each of its DataSets as
 * {@code dcat:Dataset} with one {@code dcat:Distribution} per resolved
 * {@code DistributionExport}.
 *
 * <p>
 * Metadata is derived by default and overridden explicitly (DA-DCAT-8): an
 * explicit {@code DcatPublication} value wins, else the provider's own
 * name/description, else the GenModel documentation annotation of the
 * provider's model type. What can neither be derived nor is declared — the
 * publisher, the distributions' license, the public base URL — is a diagnosed
 * configuration error, not a portal-side rejection at runtime (DA-DCAT-9/13).
 * </p>
 *
 * <p>
 * The export → media type resolution mirrors the rest bundle's
 * {@code ExportFormats} (override-else-default, CSV kind defaults, JSON+XMI
 * runtime defaults when nothing is declared) so the portal entry describes
 * exactly what the endpoint serves; keep the two in sync.
 * </p>
 */
final class DcatMapper {

	private static final String GENMODEL_SOURCE = "http://www.eclipse.org/emf/2002/GenModel";
	private static final String IANA_MEDIA_TYPES = "http://www.iana.org/assignments/media-types/";

	/** Everything one sync run registers and links for one provider. */
	record ProviderPlan(String portal, String catalog, String serviceId, dcat.DataService dcatService,
			List<DatasetPlan> datasets) {
	}

	record DatasetPlan(String datasetId, String catalog, dcat.Dataset dcatDataset,
			List<DistributionPlan> distributions) {
	}

	record DistributionPlan(String distributionId, dcat.Distribution dcatDistribution) {
	}

	private DcatMapper() {
	}

	/**
	 * Whether the provider declares a publication at all — the opt-in gate
	 * (DA-DCAT-4): no declaration, nothing is published.
	 */
	static boolean isPublished(DataService service) {
		return service.getPublication() != null;
	}

	/**
	 * Builds the full registration plan of one published DataService.
	 *
	 * @param service       the provider, its publication resolved and non-null
	 * @param publicBaseUrl the deployment-supplied public base the endpoints are
	 *                      reachable under (DA-DCAT-13)
	 * @throws PublicationConfigException when mandatory metadata is missing or
	 *                                    the provider kind is not publishable
	 */
	static ProviderPlan plan(DataService service, String publicBaseUrl) throws PublicationConfigException {
		DcatPublication publication = service.getPublication();
		List<String> problems = new ArrayList<>();
		if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
			problems.add("no public base URL is configured (set " + DcatPublicationConfigurator.PID
					+ " / public.base.url, e.g. via DATA_ATLAS_PUBLIC_BASE_URL)");
		}
		if (!(service instanceof RestDataService rest)) {
			throw new PublicationConfigException("DataService '" + service.getId() + "': publication of a "
					+ service.eClass().getName() + " is not supported (only RestDataService in this version)");
		}
		if (publication.getCatalog() == null || publication.getCatalog().isBlank()) {
			problems.add("publication '" + publication.getId() + "' names no target catalog");
		}

		String endpointUrl = problems.isEmpty() ? join(publicBaseUrl, basePath(rest)) : null;
		dcat.DataService dcatService = DcatFactory.eINSTANCE.createDataService();
		if (endpointUrl != null) {
			dcatService.getEndpointURL().add(endpointUrl);
		}
		applyResourceMetadata(dcatService, publication, service.getName(), service.getDescription(), null,
				"DataService '" + service.getId() + "'", problems);

		List<DatasetPlan> datasets = new ArrayList<>();
		Map<String, RestDataServiceConfiguration> byDataSet = new LinkedHashMap<>();
		for (RestDataServiceConfiguration configuration : rest.getConfiguration()) {
			if (configuration.getDataSet() != null) {
				byDataSet.putIfAbsent(configuration.getDataSet().getId(), configuration);
			}
		}
		for (RestDataServiceConfiguration configuration : byDataSet.values()) {
			datasets.add(planDataSet(rest, configuration, publication, endpointUrl, problems));
		}

		if (!problems.isEmpty()) {
			throw new PublicationConfigException("DataService '" + service.getId()
					+ "' cannot be published as configured: " + String.join("; ", problems));
		}
		String serviceId = identifier(publication, service.getId());
		return new ProviderPlan(publication.getPortal(), publication.getCatalog(), serviceId, dcatService, datasets);
	}

	private static DatasetPlan planDataSet(RestDataService service, RestDataServiceConfiguration configuration,
			DcatPublication servicePublication, String endpointUrl, List<String> problems) {
		DataSet dataSet = configuration.getDataSet();
		// override-else-default (DA-DCAT-7): a DataSet's own declaration wins
		DcatPublication publication = dataSet.getPublication() != null ? dataSet.getPublication()
				: servicePublication;
		String where = "DataSet '" + dataSet.getId() + "'";
		if (dataSet.getPublication() != null && dataSet.getPublication().getPortal() != null
				&& !dataSet.getPublication().getPortal().equals(servicePublication.getPortal())) {
			problems.add(where + ": its publication names portal '" + dataSet.getPublication().getPortal()
					+ "' but its service publishes to '" + servicePublication.getPortal()
					+ "' — a dataset cannot leave its service's portal");
		}

		dcat.Dataset dcatDataset = DcatFactory.eINSTANCE.createDataset();
		applyResourceMetadata(dcatDataset, publication, dataSet.getName(), dataSet.getDescription(),
				dataSet.getOutputType(), where, problems);
		String language = language(publication);
		publication.getKeywords().forEach(keyword -> dcatDataset.getKeyword().add(literal(keyword, language)));
		publication.getThemes().forEach(theme -> dcatDataset.getTheme().add(theme));

		String path = configuration.getPath() != null ? configuration.getPath() : dataSet.getName();
		String dataSetUrl = endpointUrl == null || path == null ? null : endpointUrl + "/" + path;
		if (path == null) {
			problems.add(where + ": neither a configured path nor a name to derive one from");
		}

		List<DistributionPlan> distributions = new ArrayList<>();
		for (Map.Entry<String, String> entry : mediaTypesOf(dataSet, service, where, problems).entrySet()) {
			distributions.add(planDistribution(entry.getKey(), entry.getValue(), dataSetUrl, publication, where,
					problems));
		}
		String catalog = publication.getCatalog();
		return new DatasetPlan(identifier(publication == servicePublication ? null : publication, dataSet.getId()),
				catalog, dcatDataset, distributions);
	}

	private static DistributionPlan planDistribution(String distributionId, String mediaType, String dataSetUrl,
			DcatPublication publication, String where, List<String> problems) {
		dcat.Distribution distribution = DcatFactory.eINSTANCE.createDistribution();
		String language = language(publication);
		distribution.setTitle(literal(mediaType, language));
		distribution.setDescription(literal("Served as " + mediaType, language));
		if (dataSetUrl != null) {
			distribution.getAccessURL().add(dataSetUrl);
		}
		distribution.setMediaType(IANA_MEDIA_TYPES + mediaType);
		String licenseUri = publication.getLicenseUri();
		if (licenseUri == null || licenseUri.isBlank()) {
			problems.add(where + ": its distributions need a license, and publication '" + publication.getId()
					+ "' declares no licenseUri");
		} else {
			LicenseDocument license = TermsFactory.eINSTANCE.createLicenseDocument();
			license.setAbout(licenseUri);
			distribution.setLicense(license);
		}
		return new DistributionPlan(distributionId, distribution);
	}

	/**
	 * The effective media types of a DataSet, keyed by distribution id — the same
	 * resolution the rest bundle serves by: the DataSet's own exports fully
	 * replace the service's; none at all means the runtime defaults JSON and XMI.
	 */
	private static Map<String, String> mediaTypesOf(DataSet dataSet, DataService service, String where,
			List<String> problems) {
		List<DistributionExport> exports = dataSet.getDistributionExport().isEmpty()
				? service.getDistributionExport()
				: dataSet.getDistributionExport();
		Map<String, String> byId = new LinkedHashMap<>();
		if (exports.isEmpty()) {
			byId.put("json", "application/json");
			byId.put("xml", "application/xml");
			return byId;
		}
		for (DistributionExport export : exports) {
			String declared = export.getMediaType();
			if (declared != null && !declared.isBlank()) {
				byId.put(export.getId(), declared.trim());
			} else if (export instanceof CSVDistributionExport csv) {
				byId.put(export.getId(), csv.isCompressed() ? "application/x-csv-zip" : "text/csv");
			} else {
				problems.add(where + ": export '" + export.getId() + "' names no media type");
			}
		}
		return byId;
	}

	/**
	 * Title, description and publisher — the fields the portal's shapes require
	 * of every {@code DcatResource}: explicit publication value, else the
	 * provider's own, else (for the description) the GenModel documentation of
	 * the provider's model type.
	 */
	private static void applyResourceMetadata(DcatResource resource, DcatPublication publication, String name,
			String description, EClass modelType, String where, List<String> problems) {
		String language = language(publication);
		String title = firstNonBlank(publication.getTitle(), name);
		if (title == null) {
			problems.add(where + ": no title — the publication declares none and the provider has no name");
		} else {
			resource.getTitle().add(literal(title, language));
		}
		String effectiveDescription = firstNonBlank(publication.getDescription(), description,
				documentationOf(modelType));
		if (effectiveDescription == null) {
			problems.add(where + ": no description — the publication declares none, the provider has none and "
					+ "its model type carries no documentation annotation");
		} else {
			resource.getDescription().add(literal(effectiveDescription, language));
		}
		String publisherName = publication.getPublisherName();
		if (publisherName == null || publisherName.isBlank()) {
			problems.add(where + ": publication '" + publication.getId()
					+ "' declares no publisherName — the portal requires a publisher and it is not derivable");
		} else {
			Agent publisher = FoafFactory.eINSTANCE.createAgent();
			publisher.getName().add(literal(publisherName, language));
			if (publication.getPublisherUri() != null && !publication.getPublisherUri().isBlank()) {
				publisher.setAbout(publication.getPublisherUri());
			}
			resource.setPublisher(publisher);
		}
	}

	/** The GenModel documentation of an EClass — the annotation-derived default. */
	private static String documentationOf(EClass eClass) {
		if (eClass == null) {
			return null;
		}
		EAnnotation annotation = eClass.getEAnnotation(GENMODEL_SOURCE);
		return annotation == null ? null : annotation.getDetails().get("documentation");
	}

	private static String identifier(DcatPublication publication, String providerId) {
		if (publication != null && publication.getIdentifier() != null && !publication.getIdentifier().isBlank()) {
			return publication.getIdentifier();
		}
		return providerId;
	}

	private static String language(DcatPublication publication) {
		String language = publication.getLanguage();
		return language == null || language.isBlank() ? "en" : language;
	}

	private static String basePath(DataService service) {
		String base = service.getUrlContext() == null || service.getUrlContext().isBlank()
				? "/" + service.getId()
				: service.getUrlContext();
		return base.startsWith("/") ? base : "/" + base;
	}

	private static String join(String publicBaseUrl, String path) {
		String base = publicBaseUrl.endsWith("/") ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
				: publicBaseUrl;
		return base + path;
	}

	private static String firstNonBlank(String... values) {
		for (String value : values) {
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		return null;
	}

	private static PlainLiteral literal(String value, String language) {
		PlainLiteral literal = RdfFactory.eINSTANCE.createPlainLiteral();
		literal.setValue(value);
		literal.setLang(language);
		return literal;
	}
}
