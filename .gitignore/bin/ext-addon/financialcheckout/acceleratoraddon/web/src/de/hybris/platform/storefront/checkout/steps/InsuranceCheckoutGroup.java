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
package de.hybris.platform.storefront.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutGroup;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class InsuranceCheckoutGroup extends CheckoutGroup
{
	private Map<String, InsuranceCheckoutStep> insuranceCheckoutProgressBar;

	public Map<String, InsuranceCheckoutStep> getInsuranceCheckoutProgressBar()
	{
		return insuranceCheckoutProgressBar;
	}

	@Required
	public void setInsuranceCheckoutProgressBar(final Map<String, InsuranceCheckoutStep> insuranceCheckoutProgressBar)
	{
		this.insuranceCheckoutProgressBar = insuranceCheckoutProgressBar;
	}
}
