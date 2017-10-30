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
package de.hybris.platform.commerceservices.address;

import de.hybris.platform.commerceservices.address.data.AddressVerificationResultData;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * Interface for verifying an Address with an external verification service. Each class that implements this interface
 * will work with a different service.
 */
public interface AddressVerificationService<DECISION, FIELD_ERROR>
{
	/**
	 * This method takes in an AddressModel, verifies it using the chosen implementation and returns an
	 * AddressVerificationResultData.
	 * 
	 * @param addressModel
	 *           the address to be verified.
	 * @return an AddressVerificationResultData that holds the verification data.
	 */
	AddressVerificationResultData<DECISION, FIELD_ERROR> verifyAddress(AddressModel addressModel);

	/**
	 * Used to determine if the customer is allowed to ignore the address suggestions of this AddressVerificationService
	 * and proceed with checkout.
	 * 
	 * @return true if the user can bypass address suggestions, false if not.
	 */
	boolean isCustomerAllowedToIgnoreSuggestions();
}
