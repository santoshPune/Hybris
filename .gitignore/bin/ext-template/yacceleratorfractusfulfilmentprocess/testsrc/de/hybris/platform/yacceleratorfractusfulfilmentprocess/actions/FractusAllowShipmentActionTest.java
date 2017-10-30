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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehouse.Process2WarehouseAdapter;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



/**
 * Unit test for the FractusAllowShipmentAction class.
 */
@UnitTest
public class FractusAllowShipmentActionTest
{

	@InjectMocks
	FractusAllowShipmentAction action;
	@Mock
	Process2WarehouseAdapter p2wAdapter;
	@Mock
	FractusConsignmentProcessModel model1;
	@Mock
	private ConsignmentModel consignment1;
	@Mock
	private FractusConsignmentProcessModel model2;
	@Mock
	private FractusConsignmentProcessModel model3;
	@Mock
	private ConsignmentModel consignment2;

	@Before
	public void setUp()
	{
		action = new FractusAllowShipmentAction();
		MockitoAnnotations.initMocks(this);
		//p2wAdapter doesn't like this particular consignment!
		Mockito.doThrow(new RuntimeException()).when(p2wAdapter).shipConsignment(consignment2);
		setUp(model1, consignment1);
		setUp(model2, null);
		setUp(model3, consignment2);
	}

	/**
	 * @param model
	 * @param consignment
	 */
	private void setUp(final FractusConsignmentProcessModel model, final ConsignmentModel consignment)
	{
		Mockito.when(model.getConsignment()).thenReturn(consignment);
	}

	@Test
	public void testForValidConsignment()
	{
		final String execute = action.execute(model1);
		Assert.assertEquals("DELIVERY", execute);
	}

	@Test
	public void testForNullConsignment()
	{
		final String execute = action.execute(model2);
		Assert.assertEquals("ERROR", execute);
	}

	@Test
	public void testForExceptionalConsignment()
	{
		final String execute = action.execute(model3);
		Assert.assertEquals("ERROR", execute);
	}

}
