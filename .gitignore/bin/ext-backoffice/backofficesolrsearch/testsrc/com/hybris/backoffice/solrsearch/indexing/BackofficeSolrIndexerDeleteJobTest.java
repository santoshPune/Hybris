/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.backoffice.solrsearch.indexing;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import com.hybris.backoffice.solrsearch.daos.SolrModifiedItemDAO;
import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.indexer.cron.BackofficeSolrIndexerDeleteJob;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.utils.SolrPlatformUtils;


public class BackofficeSolrIndexerDeleteJobTest
{

	public static final String MODIFIED_TYPE_CODE = "Product";
	public static final Long DELETED_PK = Long.valueOf(1L);
	public static final Long UPDATED_PK = Long.valueOf(2L);
	public static final String SEARCH_CONFIG_NAME = "Product Index";
	public static final SolrServerMode SOLR_SERVER_MODE = SolrServerMode.EMBEDDED;

	@Mock
	private ModelService modelService;

	@Mock
	private BeanFactory beanFactory;

	@Mock
	private SolrModifiedItemDAO solrModifiedItemDAO;

	@Mock
	private FacetSearchConfigService facetSearchConfigService;

	@Mock
	private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;

	@Mock
	private Exporter exporterService;

	@InjectMocks
	private BackofficeSolrIndexerDeleteJob indexerDeleteJob;

	private SolrModifiedItemModel deletedItem;
	private SolrFacetSearchConfigModel searchConfig;
	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;


	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		deletedItem = new SolrModifiedItemModel();
		deletedItem.setModifiedTypeCode(MODIFIED_TYPE_CODE);
		deletedItem.setModifiedPk(DELETED_PK);
		deletedItem.setModificationType(SolrItemModificationType.DELETE);

		final SolrServerConfigModel serverConfig = new SolrServerConfigModel();

		searchConfig = new SolrFacetSearchConfigModel();
		searchConfig.setName(SEARCH_CONFIG_NAME);
		searchConfig.setSolrServerConfig(serverConfig);


		final Map<String, IndexedType> indexedTypes = new HashMap<>();
		indexedType = new IndexedType();
		indexedType.setCode(MODIFIED_TYPE_CODE);
		indexedTypes.put(MODIFIED_TYPE_CODE, indexedType);

		final IndexConfig indexConfig = new IndexConfig();
		indexConfig.setIndexedTypes(indexedTypes);


		final SolrConfig solrConfig = new SolrConfig();
		solrConfig.setMode(SolrServerMode.EMBEDDED);

		facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setIndexConfig(indexConfig);
		facetSearchConfig.setSolrConfig(solrConfig);


		Mockito.when(beanFactory.getBean(SolrPlatformUtils.createSolrExporterBeanName(SOLR_SERVER_MODE), Exporter.class))
				.thenReturn(exporterService);
	}

	@Test
	public void testDeleteSingleItem() throws FacetConfigServiceException, ExporterException
	{
		final List<SolrModifiedItemModel> modifiedItems = Arrays.asList(deletedItem);

		Mockito.when(solrModifiedItemDAO.findByModificationType(SolrItemModificationType.DELETE)).thenReturn(modifiedItems);
		Mockito.when(backofficeFacetSearchConfigService.getSolrFacetSearchConfigModel(MODIFIED_TYPE_CODE)).thenReturn(searchConfig);
		Mockito.when(facetSearchConfigService.getConfiguration(SEARCH_CONFIG_NAME)).thenReturn(facetSearchConfig);


		indexerDeleteJob.performIndexingJob(new CronJobModel());

		final List<String> pks = Arrays.asList(deletedItem).stream().map(i -> i.getModifiedPk().toString())
				.collect(Collectors.toList());

		Mockito.verify(exporterService).exportToDeleteFromIndex(pks, facetSearchConfig, indexedType);
		Mockito.verify(modelService).removeAll(modifiedItems);
	}
}
