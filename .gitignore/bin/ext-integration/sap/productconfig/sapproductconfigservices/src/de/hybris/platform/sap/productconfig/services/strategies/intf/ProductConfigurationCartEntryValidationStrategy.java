/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.services.strategies.intf;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.sap.productconfig.services.strategies.impl.ProductConfigurationCartValidationStrategyImpl;


public interface ProductConfigurationCartEntryValidationStrategy
{
	/**
	 * Validates a cart entry model with regards to product configuration
	 *
	 * @param cartEntryModel
	 *           Model representation of cart entry
	 * @return Null if no issue occurred. A modification in status
	 *         {@link ProductConfigurationCartValidationStrategyImpl#REVIEW_CONFIGURATION} in case a validation error
	 *         occurred.
	 */
	public CommerceCartModification validateConfiguration(final CartEntryModel cartEntryModel);
}
