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
package de.hybris.platform.acceleratorservices.order;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;


public interface AcceleratorCheckoutService
{
	List<PointOfServiceDistanceData> getConsolidatedPickupOptions(CartModel cartModel);

	/**
	 * 
	 * @param cartModel
	 * @param consolidatedPickupPointModel
	 * @return any unsuccessful modifications that made to the cart (i.e. due to very, very recent stock changes)
	 * @throws CommerceCartModificationException
	 */
	List<CommerceCartModification> consolidateCheckoutCart(CartModel cartModel, PointOfServiceModel consolidatedPickupPointModel)
			throws CommerceCartModificationException;
}
