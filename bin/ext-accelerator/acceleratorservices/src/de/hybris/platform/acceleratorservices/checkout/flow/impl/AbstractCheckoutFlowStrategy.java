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

import org.springframework.beans.factory.annotation.Required;


/**
 * Base {@link CheckoutFlowStrategy} implementation, gives {@link #defaultStrategy} fallback functionality.
 *
 * @deprecated Since 5.6.
 */
@SuppressWarnings("deprecation")
@Deprecated
public abstract class AbstractCheckoutFlowStrategy implements CheckoutFlowStrategy
{
	private CheckoutFlowStrategy defaultStrategy;

	protected CheckoutFlowStrategy getDefaultStrategy()
	{
		return defaultStrategy;
	}

	@Required
	public void setDefaultStrategy(final CheckoutFlowStrategy defaultStrategy)
	{
		this.defaultStrategy = defaultStrategy;
	}
}
