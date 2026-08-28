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
package org.eclipse.fennec.data.atlas.tests;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.fennec.dcat.atlas.client.api.DcatAtlasClient;
import org.eclipse.fennec.dcat.atlas.client.api.DcatCollection;
import org.eclipse.fennec.dcat.atlas.client.api.DeleteMode;
import org.eclipse.fennec.dcat.atlas.client.api.Registration;

import dcat.Catalog;
import dcat.DataService;
import dcat.Dataset;
import dcat.DatasetSeries;
import dcat.Distribution;

/**
 * A portal double for the publication tests: records every registration, link
 * and deletion, applies everything, refuses nothing. What the real portal
 * would refuse is covered by the docker-gated {@code DcatPortalIntegrationTest}.
 */
class RecordingDcatAtlasClient implements DcatAtlasClient {

	final Map<String, DataService> dataServices = new ConcurrentHashMap<>();
	final Map<String, Dataset> datasets = new ConcurrentHashMap<>();
	/** key: datasetId + "/" + distributionId */
	final Map<String, Distribution> distributions = new ConcurrentHashMap<>();
	final List<String> links = new CopyOnWriteArrayList<>();
	final List<String> deletions = new CopyOnWriteArrayList<>();

	@Override
	public Registration<Catalog> registerCatalog(String id, Catalog catalog, String ifMatch) {
		return Registration.applied(catalog, "mock");
	}

	@Override
	public Registration<Dataset> registerDataset(String id, Dataset dataset, String ifMatch) {
		datasets.put(id, dataset);
		return Registration.applied(dataset, "mock");
	}

	@Override
	public Registration<DatasetSeries> registerDatasetSeries(String id, DatasetSeries series, String ifMatch) {
		return Registration.applied(series, "mock");
	}

	@Override
	public Registration<DataService> registerDataService(String id, DataService service, String ifMatch) {
		dataServices.put(id, service);
		return Registration.applied(service, "mock");
	}

	@Override
	public Registration<Distribution> registerDistribution(String datasetId, String id, Distribution distribution,
			String ifMatch) {
		distributions.put(datasetId + "/" + id, distribution);
		return Registration.applied(distribution, "mock");
	}

	@Override
	public void linkDatasetToCatalog(String catalogId, String datasetId) {
		links.add("catalog:" + catalogId + "<-dataset:" + datasetId);
	}

	@Override
	public void linkDataServiceToCatalog(String catalogId, String serviceId) {
		links.add("catalog:" + catalogId + "<-service:" + serviceId);
	}

	@Override
	public void linkSubCatalog(String catalogId, String subCatalogId) {
		links.add("catalog:" + catalogId + "<-catalog:" + subCatalogId);
	}

	@Override
	public void linkDatasetToSeries(String seriesId, String datasetId) {
		links.add("series:" + seriesId + "<-dataset:" + datasetId);
	}

	@Override
	public void linkDatasetToDataService(String serviceId, String datasetId) {
		links.add("service:" + serviceId + "<-dataset:" + datasetId);
	}

	@Override
	public void linkAccessService(String datasetId, String distributionId, String serviceId) {
		links.add("distribution:" + datasetId + "/" + distributionId + "<-service:" + serviceId);
	}

	@Override
	public void unlinkDatasetFromCatalog(String catalogId, String datasetId) {
	}

	@Override
	public void unlinkDataServiceFromCatalog(String catalogId, String serviceId) {
	}

	@Override
	public void unlinkSubCatalog(String catalogId, String subCatalogId) {
	}

	@Override
	public void unlinkDatasetFromSeries(String seriesId, String datasetId) {
	}

	@Override
	public void unlinkDatasetFromDataService(String serviceId, String datasetId) {
	}

	@Override
	public void unlinkAccessService(String datasetId, String distributionId, String serviceId) {
	}

	@Override
	public Optional<Catalog> catalog(String id) {
		return Optional.empty();
	}

	@Override
	public Optional<Dataset> dataset(String id) {
		return Optional.ofNullable(datasets.get(id));
	}

	@Override
	public Optional<DatasetSeries> datasetSeries(String id) {
		return Optional.empty();
	}

	@Override
	public Optional<DataService> dataService(String id) {
		return Optional.ofNullable(dataServices.get(id));
	}

	@Override
	public Optional<Distribution> distribution(String datasetId, String id) {
		return Optional.ofNullable(distributions.get(datasetId + "/" + id));
	}

	@Override
	public Optional<String> etagOf(DcatCollection collection, String id) {
		return Optional.empty();
	}

	@Override
	public Optional<String> etagOfDistribution(String datasetId, String id) {
		return Optional.empty();
	}

	@Override
	public List<String> delete(DcatCollection collection, String id, DeleteMode mode) {
		deletions.add(collection.segment() + ":" + id);
		if (collection == DcatCollection.DATASETS) {
			datasets.remove(id);
		} else if (collection == DcatCollection.DATA_SERVICES) {
			dataServices.remove(id);
		}
		return List.of();
	}

	@Override
	public void deleteDistribution(String datasetId, String id) {
		deletions.add("distribution:" + datasetId + "/" + id);
		distributions.remove(datasetId + "/" + id);
	}

	@Override
	public boolean ready() {
		return true;
	}

	@Override
	public URI aboutFor(DcatCollection collection, String id) {
		return URI.create("mock://" + collection.segment() + "/" + id);
	}

	@Override
	public void close() {
	}
}
