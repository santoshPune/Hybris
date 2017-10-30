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
package de.hybris.platform.acceleratorfacades.flow;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.commercefacades.order.CheckoutFacade;


/**
 * CheckoutFlowFacade interface extends the {@link CheckoutFacade}. The CheckoutFlowFacade supports resolving the
 * {@link CheckoutFlowEnum} for the current request.
 *
 * @since 4.6
 * @spring.bean checkoutFacade
 */
public interface CheckoutFlowFacade extends AcceleratorCheckoutFacade
{
	/**
	 * @deprecated Since 5.6. There’s only one checkout flow in the accelerator
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	CheckoutFlowEnum getCheckoutFlow();

	CheckoutPciOptionEnum getSubscriptionPciOption();
}
