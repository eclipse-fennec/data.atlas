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
import org.eclipse.fennec.data.atlas.configuration.ODataDataService;
import org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration;
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
import vcard.Organization;
import vcard.VcardFactory;

/**
 * Translates one published Fennec {@code DataService} — DataService-first, the
 * unit that owns the endpoint — into the DCAT entities the portal expects:
 * the service as {@code dcat:DataService}, each of its DataSets as
 * {@code dcat:Dataset} with one {@code dcat:Distribution} per resolved
 * {@code DistributionExport} (REST) or one per entity set (OData — the
 * textbook {@code dcat:DataService}: the service root is the
 * {@code endpointURL}, its {@code $metadata} document the
 * {@code endpointDescription}, each entity set a JSON distribution).
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
	/** The collection annotation of emf.ogc.features (its {@code id} detail names the collection). */
	private static final String OGC_FEATURES_SOURCE = "https://eclipse.org/fennec/ogc/features";

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
	 * @param service         the provider, its publication resolved and non-null
	 * @param publicBaseUrl   the deployment-supplied public base the Data Atlas is
	 *                        reachable under (DA-DCAT-13): the host, without a
	 *                        mount prefix
	 * @param restContextPath the context path the REST whiteboard mounts
	 *                        {@code RestDataService}s under (the Data Atlas
	 *                        runtime: {@code rest}); OData and OGC API Features
	 *                        roots mount directly on the HTTP runtime and get no
	 *                        prefix (data.atlas#19)
	 * @throws PublicationConfigException when mandatory metadata is missing or
	 *                                    the provider kind is not publishable
	 */
	static ProviderPlan plan(DataService service, String publicBaseUrl, String restContextPath)
			throws PublicationConfigException {
		DcatPublication publication = service.getPublication();
		List<String> problems = new ArrayList<>();
		if (publicBaseUrl == null || publicBaseUrl.isBlank()) {
			problems.add("no public base URL is configured (set " + DcatPublicationConfigurator.PID
					+ " / public.base.url, e.g. via DATA_ATLAS_PUBLIC_BASE_URL)");
		}
		if (!(service instanceof RestDataService) && !(service instanceof ODataDataService)
				&& !(service instanceof OgcFeaturesDataService)) {
			throw new PublicationConfigException("DataService '" + service.getId() + "': publication of a "
					+ service.eClass().getName() + " is not supported (only RestDataService, ODataDataService and "
					+ "OgcFeaturesDataService in this version)");
		}
		if (publication.getCatalog() == null || publication.getCatalog().isBlank()) {
			problems.add("publication '" + publication.getId() + "' names no target catalog");
		}

		String endpointUrl = problems.isEmpty()
				? join(publicBaseUrl, mountPrefix(service, restContextPath) + basePath(service))
				: null;
		dcat.DataService dcatService = DcatFactory.eINSTANCE.createDataService();
		if (endpointUrl != null) {
			dcatService.getEndpointURL().add(endpointUrl);
			if (service instanceof ODataDataService) {
				// the CSDL document describes the service (DCAT-AP: endpointDescription)
				dcatService.getEndpointDescription().add(endpointUrl + "/$metadata");
			} else if (service instanceof OgcFeaturesDataService) {
				// the OpenAPI document of the API root describes the service
				dcatService.getEndpointDescription().add(endpointUrl + "/api");
			}
		}
		applyResourceMetadata(dcatService, publication, service.getName(), service.getDescription(), null,
				"DataService '" + service.getId() + "'", problems);

		List<DatasetPlan> datasets = new ArrayList<>();
		if (service instanceof RestDataService rest) {
			Map<String, RestDataServiceConfiguration> byDataSet = new LinkedHashMap<>();
			for (RestDataServiceConfiguration configuration : rest.getConfiguration()) {
				if (configuration.getDataSet() != null) {
					byDataSet.putIfAbsent(configuration.getDataSet().getId(), configuration);
				}
			}
			for (RestDataServiceConfiguration configuration : byDataSet.values()) {
				DataSet dataSet = configuration.getDataSet();
				String path = configuration.getPath() != null ? configuration.getPath() : dataSet.getName();
				datasets.add(planDataSet(service, dataSet, path, Shape.REST, publication, endpointUrl, problems));
			}
		} else if (service instanceof OgcFeaturesDataService ogc) {
			Map<String, OgcFeaturesDataServiceConfiguration> byDataSet = new LinkedHashMap<>();
			for (OgcFeaturesDataServiceConfiguration configuration : ogc.getConfiguration()) {
				if (configuration.getDataSet() != null) {
					byDataSet.putIfAbsent(configuration.getDataSet().getId(), configuration);
				}
			}
			for (OgcFeaturesDataServiceConfiguration configuration : byDataSet.values()) {
				DataSet dataSet = configuration.getDataSet();
				// the collection id mirrors the ogc bundle's derivation
				String collectionId = collectionId(configuration, dataSet.getOutputType());
				datasets.add(planDataSet(service, dataSet, collectionId == null ? null
						: "collections/" + collectionId + "/items", Shape.OGC_FEATURES, publication, endpointUrl,
						problems));
			}
		} else {
			Map<String, ODataDataServiceConfiguration> byDataSet = new LinkedHashMap<>();
			for (ODataDataServiceConfiguration configuration : ((ODataDataService) service).getConfiguration()) {
				if (configuration.getDataSet() != null) {
					byDataSet.putIfAbsent(configuration.getDataSet().getId(), configuration);
				}
			}
			for (ODataDataServiceConfiguration configuration : byDataSet.values()) {
				DataSet dataSet = configuration.getDataSet();
				// the entity set name mirrors the odata bundle's derivation
				String entitySet = configuration.getEntitySetName() != null
						&& !configuration.getEntitySetName().isBlank() ? configuration.getEntitySetName().trim()
								: dataSet.getOutputType() != null ? dataSet.getOutputType().getName() : null;
				datasets.add(planDataSet(service, dataSet, entitySet, Shape.ODATA, publication, endpointUrl,
						problems));
			}
		}

		if (!problems.isEmpty()) {
			throw new PublicationConfigException("DataService '" + service.getId()
					+ "' cannot be published as configured: " + String.join("; ", problems));
		}
		String serviceId = identifier(publication, service.getId());
		return new ProviderPlan(publication.getPortal(), publication.getCatalog(), serviceId, dcatService, datasets);
	}

	/** The distribution shape of a service kind. */
	private enum Shape {
		REST, ODATA, OGC_FEATURES
	}

	/**
	 * The collection id of an OGC API Features configuration: the configured
	 * {@code collectionId}, else the {@code id} of the type's
	 * {@code https://eclipse.org/fennec/ogc/features} annotation, else the type
	 * name — the same derivation the ogc bundle serves by.
	 */
	private static String collectionId(OgcFeaturesDataServiceConfiguration configuration, EClass type) {
		if (configuration.getCollectionId() != null && !configuration.getCollectionId().isBlank()) {
			return configuration.getCollectionId().trim();
		}
		if (type == null) {
			return null;
		}
		EAnnotation annotation = type.getEAnnotation(OGC_FEATURES_SOURCE);
		String annotated = annotation == null ? null : annotation.getDetails().get("id");
		return annotated != null && !annotated.isBlank() ? annotated.trim() : type.getName();
	}

	/**
	 * Plans one served DataSet: {@code path} is the segment under the service
	 * endpoint it is reachable at (REST path, OData entity set name or the OGC
	 * collection's items path); {@code shape} selects the distribution shape.
	 */
	private static DatasetPlan planDataSet(DataService service, DataSet dataSet, String path, Shape shape,
			DcatPublication servicePublication, String endpointUrl, List<String> problems) {
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

		String dataSetUrl = endpointUrl == null || path == null ? null : endpointUrl + "/" + path;
		if (path == null) {
			problems.add(where + ": neither a configured " + switch (shape) {
			case ODATA -> "entity set name";
			case OGC_FEATURES -> "collection id";
			default -> "path";
			} + " nor a name to derive one from");
		}

		List<DistributionPlan> distributions = new ArrayList<>();
		if (shape == Shape.ODATA) {
			// one entity set = one distribution: OData JSON is the protocol's format
			distributions.add(planDistribution("odata", "application/json", dataSetUrl, publication, where,
					problems));
		} else if (shape == Shape.OGC_FEATURES) {
			// one collection = one distribution: its items as GeoJSON
			distributions.add(planDistribution("ogc-features", "application/geo+json", dataSetUrl, publication,
					where, problems));
		} else {
			for (Map.Entry<String, String> entry : mediaTypesOf(dataSet, service, where, problems).entrySet()) {
				distributions.add(planDistribution(entry.getKey(), entry.getValue(), dataSetUrl, publication,
						where, problems));
			}
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
	 * the provider's model type. The optional rights holder and contact point
	 * come from the publication alone; unset, the entry states none.
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
		String rightsHolderName = blankToNull(publication.getRightsHolderName());
		if (rightsHolderName != null) {
			Agent rightsHolder = FoafFactory.eINSTANCE.createAgent();
			rightsHolder.getName().add(literal(rightsHolderName, language));
			String rightsHolderUri = blankToNull(publication.getRightsHolderUri());
			if (rightsHolderUri != null) {
				rightsHolder.setAbout(rightsHolderUri);
			}
			resource.setRightsHolder(rightsHolder);
		}
		Organization contact = contactPoint(publication);
		if (contact != null) {
			resource.getContactPoint().add(contact);
		}
	}

	/**
	 * The dcat:contactPoint of a publication as a vcard:Organization, or
	 * {@code null} when it declares none of name, e-mail and URL. A plain e-mail
	 * address becomes a {@code mailto:} IRI.
	 */
	static Organization contactPoint(DcatPublication publication) {
		String name = blankToNull(publication.getContactName());
		String email = blankToNull(publication.getContactEmail());
		String url = blankToNull(publication.getContactUrl());
		if (name == null && email == null && url == null) {
			return null;
		}
		Organization contact = VcardFactory.eINSTANCE.createOrganization();
		contact.setFn(name);
		if (email != null) {
			contact.getHasEmail().add(email.regionMatches(true, 0, "mailto:", 0, 7) ? email : "mailto:" + email);
		}
		if (url != null) {
			contact.getHasURL().add(url);
		}
		return contact;
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.trim();
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

	/**
	 * Where a service kind is mounted relative to the public base: REST services
	 * below the whiteboard's context path, OData and OGC API Features roots
	 * directly on the HTTP runtime (data.atlas#19).
	 */
	static String mountPrefix(DataService service, String restContextPath) {
		if (!(service instanceof RestDataService) || restContextPath == null || restContextPath.isBlank()) {
			return "";
		}
		String prefix = restContextPath.trim();
		while (prefix.endsWith("/")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		return prefix.isEmpty() ? "" : prefix.startsWith("/") ? prefix : "/" + prefix;
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
