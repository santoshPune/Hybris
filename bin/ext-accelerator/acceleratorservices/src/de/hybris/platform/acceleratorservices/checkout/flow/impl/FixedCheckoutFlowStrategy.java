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

import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 * Uses fixed {@link CheckoutFlowEnum} as result. Used most likely on the end of checkout flow strategy chain.
 *
 * @since 4.6
 *
 * @deprecated Since 5.6.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class FixedCheckoutFlowStrategy implements CheckoutFlowStrategy
{
	private CheckoutFlowEnum checkoutFlow;

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return checkoutFlow;
	}

	@Required
	public void setCheckoutFlow(final CheckoutFlowEnum checkoutFlow)
	{
		this.checkoutFlow = checkoutFlow;
	}
}
