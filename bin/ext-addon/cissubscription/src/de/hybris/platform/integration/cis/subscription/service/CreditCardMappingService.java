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
 */
package de.hybris.platform.integration.cis.subscription.service;

import de.hybris.platform.commercefacades.order.data.CardTypeData;

import javax.annotation.Nonnull;
import java.util.Collection;


/**
 * This service should be implemented, if there is a difference between the credit card codes defined in hybris in
 * comparison to those of a billing provider.
 */
public interface CreditCardMappingService
{
	/**
	 * Converts the code of a credit card to the appropriate code of the provider
	 * 
	 * @param creditCards
	 *           the credit cards
	 * @param billingProvider
	 *           the name of the billing provider
	 */
	boolean convertCCToProviderSpecificName(@Nonnull Collection<CardTypeData> creditCards, String billingProvider);
}
