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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fractussyncservices.exception.YaasBusinessException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.dao.YaasOrderStatusDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.impl.DefaultYaasOrderStatusService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.model.YaasOrderStatusMapModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class YaasOrderStatusServiceTest
{
	@Mock
	YaasOrderStatusMapModel goodModel;

	@Mock
	YaasOrderStatusMapModel unknownYaasStatusModel;

	@Mock
	YaasOrderStatusMapModel unknownEcpStatusModel;

	@Mock
	YaasOrderStatusDao yaasOrderStatusDao;

	@InjectMocks
	DefaultYaasOrderStatusService yaasOrderStatusService = new DefaultYaasOrderStatusService();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(yaasOrderStatusDao.findYaasOrderStatus("CREATED")).thenReturn(goodModel);
		Mockito.when(yaasOrderStatusDao.findYaasOrderStatus("PAYMENT_AUTHORIZED")).thenReturn(unknownYaasStatusModel);
		Mockito.when(yaasOrderStatusDao.findYaasOrderStatus("")).thenReturn(unknownEcpStatusModel);
		Mockito.when(yaasOrderStatusDao.findYaasOrderStatus(null)).thenReturn(null);

		Mockito.when(goodModel.getYaasOrderStatus()).thenReturn("CREATED");
		Mockito.when(unknownYaasStatusModel.getYaasOrderStatus()).thenReturn("");
		Mockito.when(unknownEcpStatusModel.getYaasOrderStatus()).thenReturn(null);
	}

	@Test
	public void testSimpleMatching()
	{
		final OrderModel model = new OrderModel();
		model.setStatus(OrderStatus.CREATED);

		boolean errorThrown = false;
		String determineYaasStatus = null;

		try
		{
			determineYaasStatus = yaasOrderStatusService.determineYaasStatus(model);
			Mockito.verify(yaasOrderStatusDao).findYaasOrderStatus("CREATED");
		}
		catch (final YaasBusinessException e)
		{
			errorThrown = true;
		}

		Assert.assertEquals(determineYaasStatus, "CREATED");
		Assert.assertFalse(errorThrown);
	}

	@Test
	public void testUnknownYaasMatching()
	{
		final OrderModel model = new OrderModel();
		model.setStatus(OrderStatus.PAYMENT_AUTHORIZED);

		boolean unknownErrorThrown = false;
		boolean matchingErrorThrown = false;
		String determineYaasStatus = null;

		try
		{
			determineYaasStatus = yaasOrderStatusService.determineYaasStatus(model);
			Mockito.verify(yaasOrderStatusDao).findYaasOrderStatus("PAYMENT_AUTHORIZED");
		}
		catch (final UnknownEcpStatusException e)
		{
			unknownErrorThrown = true;
		}
		catch (final NoMatchingYaasStatusException e)
		{
			matchingErrorThrown = true;
		}
		catch (final YaasBusinessException e)
		{
			//other yaas exception
		}

		Assert.assertEquals(determineYaasStatus, null);
		Assert.assertTrue(matchingErrorThrown);
		Assert.assertFalse(unknownErrorThrown);
	}

	@Test
	public void testUnknownEcpMatching()
	{
		final OrderModel model = new OrderModel();

		boolean unknownErrorThrown = false;
		boolean matchingErrorThrown = false;
		String determineYaasStatus = null;

		try
		{
			determineYaasStatus = yaasOrderStatusService.determineYaasStatus(model);
			Mockito.verifyZeroInteractions(yaasOrderStatusDao);
		}
		catch (final UnknownEcpStatusException e)
		{
			unknownErrorThrown = true;
		}
		catch (final NoMatchingYaasStatusException e)
		{
			matchingErrorThrown = true;
		}
		catch (final YaasBusinessException e)
		{
			//other yaas exception
		}

		Assert.assertEquals(determineYaasStatus, null);
		Assert.assertFalse(matchingErrorThrown);
		Assert.assertTrue(unknownErrorThrown);
	}
}
