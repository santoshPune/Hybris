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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
public class PriceApplicationLookupStrategyTest
{
	@InjectMocks
	private PriceApplicationLookupStrategy priceApplicationLookupStrategy;
	@Mock
	private ModelService modelService;
	@Mock
	private PriceRow priceRow;
	@Mock
	private PriceRowModel priceRowModel;
	@Mock
	private ProductModel productModel;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private CatalogModel catalogModel;
	@Mock
	private BaseStoreModel baseStoreModel;
	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private YaasProjectModel yaasProjectModel;
	@Mock
	private YaasApplicationModel yaasApplicationModel;
	private final String applicationID = "id";
	@Mock
	private Item notPrice;
	@Mock
	private ProductService productService;
	@Mock
	private CatalogVersionService catalogVersionService;

	private static final String CATALOGNAME = "testCatalog";
	private static final String PRODUCTID = "testproductid";
	private static final String CATALOGVERSION = "Online";


	@Before
	public void setup()
	{
		priceApplicationLookupStrategy = new PriceApplicationLookupStrategy();
		priceApplicationLookupStrategy.setCatalogName(CATALOGNAME);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnLookupValue()
	{
		final String expectedVal = applicationID;

		Mockito.when(modelService.get(priceRow)).thenReturn(priceRowModel);
		Mockito.when(priceRowModel.getProductId()).thenReturn(PRODUCTID);
		Mockito.when(catalogVersionService.getCatalogVersion(CATALOGNAME, CATALOGVERSION)).thenReturn(catalogVersionModel);
		Mockito.when(productService.getProduct(catalogVersionModel, PRODUCTID)).thenReturn(productModel);
		Mockito.when(productModel.getCatalogVersion()).thenReturn(catalogVersionModel);
		Mockito.when(catalogVersionModel.getCatalog()).thenReturn(catalogModel);
		Mockito.when(catalogModel.getBaseStores()).thenReturn(Lists.newArrayList(baseStoreModel));
		Mockito.when(baseStoreModel.getCmsSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		Mockito.when(baseSiteModel.getYaasProjects()).thenReturn(Sets.newHashSet(yaasProjectModel));
		Mockito.when(yaasProjectModel.getYaasApplications()).thenReturn(Sets.newHashSet(yaasApplicationModel));
		Mockito.when(yaasApplicationModel.getIdentifier()).thenReturn(applicationID);

		final String lookupVal = priceApplicationLookupStrategy.lookup(priceRow);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnLookupEmptyIfNoYaasApplications()
	{
		final String expectedVal = StringUtils.EMPTY;

		Mockito.when(modelService.get(priceRow)).thenReturn(priceRowModel);
		Mockito.when(priceRowModel.getProductId()).thenReturn(PRODUCTID);
		Mockito.when(catalogVersionService.getCatalogVersion(CATALOGNAME, CATALOGVERSION)).thenReturn(catalogVersionModel);
		Mockito.when(productService.getProduct(catalogVersionModel, PRODUCTID)).thenReturn(productModel);
		Mockito.when(productModel.getCatalogVersion()).thenReturn(catalogVersionModel);
		Mockito.when(catalogVersionModel.getCatalog()).thenReturn(catalogModel);
		Mockito.when(catalogModel.getBaseStores()).thenReturn(Lists.newArrayList(baseStoreModel));
		Mockito.when(baseStoreModel.getCmsSites()).thenReturn(Lists.newArrayList(baseSiteModel));
		Mockito.when(baseSiteModel.getYaasProjects()).thenReturn(Sets.newHashSet(yaasProjectModel));
		Mockito.when(yaasProjectModel.getYaasApplications()).thenReturn(Sets.newHashSet());


		final String lookupVal = priceApplicationLookupStrategy.lookup(priceRow);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnLookupEmptyIfItemNotMatch()
	{
		final String expectedVal = StringUtils.EMPTY;

		final String lookupVal = priceApplicationLookupStrategy.lookup(notPrice);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnProductModelTypeCode()
	{
		Assert.assertEquals(PriceRowModel._TYPECODE, priceApplicationLookupStrategy.getTypeCode());
	}
}
