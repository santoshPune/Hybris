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

import com.hybris.backoffice.solrsearch.daos.SolrFacetSearchConfigDAO;
import com.hybris.backoffice.solrsearch.events.DirectSolrIndexSynchronizationStrategy;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import com.hybris.backoffice.solrsearch.utils.SolrPlatformUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DirectSolrIndexSynchronizationStrategyTest
{
	public static final String PRODUCT_TYPECODE = "Product";
	public static final long PK_ = 1L;
	public static final String CONFIG_NAME = "backoffice_product";

	@Mock
	private ComposedTypeModel typeModel;

	@Mock
	private BackofficeIndexedTypeToSolrFacetSearchConfigModel searchConfig;

	@Mock
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;

	@Mock
	private BeanFactory beanFactory;

	@Mock
	private FacetSearchConfigService facetSearchConfigService;

	@Mock
	private IndexerService indexerService;

	@Mock
	private ModelService modelService;

	@Mock
	private SolrFacetSearchConfigDAO configDAO;

	@Mock
	private TypeService typeService;

	@Mock
	private FacetSearchConfig facetSearchConfig;


	@Mock
	private Exporter solrExporter;

	private IndexedType indexedType = new IndexedType();

	@InjectMocks
	private DirectSolrIndexSynchronizationStrategy strategy = new DirectSolrIndexSynchronizationStrategy();

	private String exporterBeanName;

	@Before
	public void init() throws FacetConfigServiceException
	{
		MockitoAnnotations.initMocks(this);

		final SolrConfig config = new SolrConfig();
		config.setMode(SolrServerMode.EMBEDDED);

		exporterBeanName = SolrPlatformUtils.SOLR_EXPORTER_BEAN_NAME_PREFIX + config.getMode().toString().toLowerCase();

		indexedType.setCode(PRODUCT_TYPECODE);

		final Map<String,IndexedType> indexedTypes = new HashMap<>();
		indexedTypes.put("bo_product", indexedType);

		IndexConfig indexConfig = new IndexConfig();
		indexConfig.setIndexedTypes(indexedTypes);

		Mockito.when(searchConfig.getSolrFacetSearchConfig()).thenReturn(solrFacetSearchConfigModel);
		Mockito.when(searchConfig.getSolrFacetSearchConfig().getName()).thenReturn(CONFIG_NAME);
		Mockito.when(typeService.getTypeForCode(PRODUCT_TYPECODE)).thenReturn(typeModel);
		Mockito.when(configDAO.findSearchConfigurationsForTypes(Arrays.asList(typeModel))).thenReturn(Arrays.asList(searchConfig));
		Mockito.when(facetSearchConfigService.getConfiguration(CONFIG_NAME)).thenReturn(facetSearchConfig);
		Mockito.when(facetSearchConfig.getSolrConfig()).thenReturn(config);
		Mockito.when(beanFactory.getBean(exporterBeanName, Exporter.class)).thenReturn(solrExporter);
		Mockito.when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);


	}

	@Test
	public void testRemoveItem() throws FacetConfigServiceException, ExporterException
	{
		strategy.removeItem(PRODUCT_TYPECODE, PK_);

		Mockito.verify(facetSearchConfigService).getConfiguration(CONFIG_NAME);
		Mockito.verify(beanFactory).getBean(exporterBeanName, Exporter.class);
		Mockito.verify(solrExporter).exportToDeleteFromIndex(
				Matchers.eq(Arrays.asList(Long.toString(PK_))),
				Matchers.eq(facetSearchConfig),
				Matchers.eq(indexedType));
	}


	@Test
	public void testUpdateItem() throws FacetConfigServiceException, IndexerException
	{
		strategy.updateItem(PRODUCT_TYPECODE, PK_);

		Mockito.verify(facetSearchConfigService).getConfiguration(CONFIG_NAME);
		Mockito.verify(indexerService).updateTypeIndex(
				Matchers.eq(facetSearchConfig),
				Matchers.eq(indexedType),
				Matchers.eq(Arrays.asList(PK.fromLong(PK_)))
		);
	}

}
