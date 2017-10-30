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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fractussyncservices.adapter.FractusOrderSiteAdapter;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class FractusOrderSiteAdapterTest
{
	@InjectMocks
	private FractusOrderSiteAdapter adapter;
	@Mock
	private ModelService modelService;
	@Mock
	private YaasConfigurationService yaasConfigurationService;
	@Mock
	private Order order;
	@Mock
	private OrderModel orderModel;
	@Mock
	private YaasApplicationModel yaasApplicationModel;
	@Mock
	private YaasProjectModel yaasProjectModel;
	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private BaseStoreModel baseStoreModel;

	@Before
	public void setup()
	{
		adapter = new FractusOrderSiteAdapter();
		adapter.setBaseStoreUID("fractusStore");

		MockitoAnnotations.initMocks(this);

		Mockito.when(modelService.get(order)).thenReturn(orderModel);
		Mockito.when(yaasConfigurationService.getYaasApplicationForId("appId")).thenReturn(yaasApplicationModel);
		Mockito.when(yaasApplicationModel.getYaasProject()).thenReturn(yaasProjectModel);
		Mockito.when(yaasProjectModel.getBaseSite()).thenReturn(baseSiteModel);
		Mockito.when(baseSiteModel.getStores()).thenReturn(Lists.newArrayList(baseStoreModel));
		Mockito.when(baseStoreModel.getUid()).thenReturn("fractusStore");

	}

	@Test
	public void shouldPerformImport()
	{
		final String appId = "appId";
		adapter.performImport(appId, order);

		Mockito.verify(modelService, Mockito.times(1)).save(orderModel);
		Mockito.verify(orderModel).setSite(baseSiteModel);
		Mockito.verify(orderModel).setStore(baseStoreModel);
	}
}
