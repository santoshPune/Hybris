/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.b2bservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.services.strategies.impl.ProductConfigurationCartValidationStrategyImplTest;


@SuppressWarnings("javadoc")
@UnitTest
public class ProductConfigurationB2BCartValidationStrategyImplTest extends ProductConfigurationCartValidationStrategyImplTest
{


	@Override
	protected void initTestClass()
	{
		classUnderTest = new ProductConfigurationB2BCartValidationStrategyImpl();
		((ProductConfigurationB2BCartValidationStrategyImpl) classUnderTest)
				.setProductConfigurationCartEntryValidationStrategy(productConfigurationCartEntryValidationStrategy);

	}
}
