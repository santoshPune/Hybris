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
package de.hybris.platform.acceleratorservices.checkout.flow;

import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;


/**
 * Abstraction for strategy determining flow for checkout logic.
 *
 * @since 4.6
 */
@SuppressWarnings("deprecation")
@Deprecated
public interface CheckoutFlowStrategy
{
	/**
	 * Returns one of the possible {@link CheckoutFlowEnum} values - to select the checkout flow
	 *
	 * @deprecated Since 5.6.
	 */
	@Deprecated
	CheckoutFlowEnum getCheckoutFlow();
}
