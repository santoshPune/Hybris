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
package de.hybris.platform.acceleratorservices.checkout.flow.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FixedCheckoutFlowStrategyTest
{
	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testReturnsMultiStepFlow()
	{
		final FixedCheckoutFlowStrategy strategy = new FixedCheckoutFlowStrategy();
		strategy.setCheckoutFlow(CheckoutFlowEnum.MULTISTEP);

		Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, strategy.getCheckoutFlow());
	}
}
