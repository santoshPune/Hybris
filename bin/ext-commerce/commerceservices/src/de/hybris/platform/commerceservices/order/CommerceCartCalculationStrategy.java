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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Strategy to calculate cart.
 */
public interface CommerceCartCalculationStrategy
{
	@Deprecated
	/**
	 * @deprecated use {@link #calculateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)}
	 */
	boolean calculateCart(final CartModel cartModel);

	@Deprecated
	/**
	 * @deprecated use {@link #recalculateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)}
	 */
	boolean recalculateCart(final CartModel cartModel);

	boolean calculateCart(final CommerceCartParameter parameter);

	boolean recalculateCart(final CommerceCartParameter parameter);


}
