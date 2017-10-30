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
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.servicelayer.i18n.I18NService;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@ManualTest
public abstract class ConfigurationProviderSSCTestBase
{
	@Mock
	I18NService i18nService;



	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}



}
