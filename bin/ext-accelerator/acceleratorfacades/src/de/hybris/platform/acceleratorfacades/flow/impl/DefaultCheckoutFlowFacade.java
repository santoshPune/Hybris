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
package de.hybris.platform.acceleratorfacades.flow.impl;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;
import de.hybris.platform.acceleratorservices.checkout.pci.CheckoutPciStrategy;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CheckoutFlowFacade}. Delegates resolving the checkout flow to an injected
 * {@link CheckoutFlowStrategy}.
 *
 * @since 4.6
 * @spring.bean checkoutFlowFacade
 */
@SuppressWarnings("deprecation")
public class DefaultCheckoutFlowFacade extends DefaultAcceleratorCheckoutFacade implements CheckoutFlowFacade
{
	/**
	 * @deprecated Since 5.6.
	 */
	@Deprecated
	private CheckoutFlowStrategy checkoutFlowStrategy;
	private CheckoutPciStrategy checkoutPciStrategy;

	/**
	 * @deprecated Since 5.6. There’s only one checkout flow in the accelerator
	 */
	@Deprecated
	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return getCheckoutFlowStrategy().getCheckoutFlow();
	}

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return getCheckoutPciStrategy().getSubscriptionPciOption();
	}

	/**
	 * @deprecated Since 5.6.
	 */
	@Deprecated
	protected CheckoutFlowStrategy getCheckoutFlowStrategy()
	{
		return checkoutFlowStrategy;
	}

	/**
	 * @deprecated Since 5.6.
	 */
	@Deprecated
	@Required
	public void setCheckoutFlowStrategy(final CheckoutFlowStrategy strategy)
	{
		this.checkoutFlowStrategy = strategy;
	}

	protected CheckoutPciStrategy getCheckoutPciStrategy()
	{
		return this.checkoutPciStrategy;
	}

	@Required
	public void setCheckoutPciStrategy(final CheckoutPciStrategy strategy)
	{
		this.checkoutPciStrategy = strategy;
	}
}
