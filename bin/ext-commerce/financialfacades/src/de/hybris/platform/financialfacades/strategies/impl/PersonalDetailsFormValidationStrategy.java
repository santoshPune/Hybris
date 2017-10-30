/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */

package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.financialfacades.facades.InsuranceCheckoutFacade;
import de.hybris.platform.financialfacades.strategies.StepValidationStrategy;

import org.springframework.beans.factory.annotation.Autowired;


public class PersonalDetailsFormValidationStrategy implements StepValidationStrategy
{

	@Autowired
	private InsuranceCheckoutFacade checkoutFacade;

	@Override
	public boolean isValid()
	{
		return checkoutFacade.orderEntryHasValidFormData();
	}
}
