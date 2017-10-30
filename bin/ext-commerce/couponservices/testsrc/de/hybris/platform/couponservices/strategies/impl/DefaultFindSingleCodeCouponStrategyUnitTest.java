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
 *
 */
package de.hybris.platform.couponservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.couponservices.strategies.FindCouponStrategy;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultFindSingleCodeCouponStrategyUnitTest extends AbstractFindCouponStrategyUnitTest
{

	@InjectMocks
	private DefaultFindSingleCodeCouponStrategy strategy;

	@Override
	FindCouponStrategy getStrategy()
	{
		return strategy;
	}
}
