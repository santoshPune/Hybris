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
*
*/
package de.hybris.platform.fractussyncservices.adapter.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.fractussyncservices.adapter.FractusValueOrderEntryProductAdapter;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class FractusOrderEntryProductAdapterTest
{

	@InjectMocks
	private FractusValueOrderEntryProductAdapter adapter;
	@Mock
	private ModelService modelService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private ProductService productService;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private OrderModel orderModel;
	@Mock
	private ProductModel productModel;
	@Mock
	private BaseStoreModel baseStoreModel;
	@Mock
	private CatalogModel catalogModel;
	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Before
	public void setup()
	{
		adapter = new FractusValueOrderEntryProductAdapter();

		MockitoAnnotations.initMocks(this);

		Mockito.when(flexibleSearchService.getModelByExample(Mockito.any(OrderModel.class))).thenReturn(orderModel);
		Mockito.when(orderModel.getStore()).thenReturn(baseStoreModel);
		Mockito.when(catalogModel.getVersion()).thenReturn(CatalogManager.ONLINE_VERSION);
		Mockito.when(catalogModel.getId()).thenReturn("id");
		Mockito.when(baseStoreModel.getCatalogs()).thenReturn(Lists.newArrayList(catalogModel));
		Mockito.when(catalogVersionService.getCatalogVersion("id", CatalogManager.ONLINE_VERSION)).thenReturn(catalogVersionModel);
		Mockito.when(productService.getProductForCode(catalogVersionModel, "productCode")).thenReturn(productModel);
	}

	@Test
	public void shouldPerformImport()
	{
		final String cellVal = "productCode|orderCode";
		final Object o = adapter.performImport(cellVal);

		Assert.assertEquals(productModel, o);
	}

}
