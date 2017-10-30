/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.hybris.yprofile.services;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

/**
 * ProfileTransactionService Interface. Communication service to send transactions to Profile
 */
public interface ProfileTransactionService {

    /**
     * Sends order to Profile.
     * @param orderModel
     * @return
     */
    void sendSubmitOrderEvent(final OrderModel orderModel);

    /**
     * Sends consignment to Profile.
     * @param consignmentModel
     * @return
     */
    void sendConsignmentEvent(final ConsignmentModel consignmentModel);

    /**
     * Sends return order to Profile.
     * @param returnRequestModel
     * @return
     */
    void sendReturnOrderEvent(final ReturnRequestModel returnRequestModel);

     /**
     * Sends user registration event to Profile.
     * @param userModel
     * @param consentReferenceId
     * @param sessionId
     * @param storeName
     */
    void sendUserRegistrationEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName);

    /**
     * Sends user login event to Profile.
     * @param userModel
     * @param consentReferenceId
     * @param sessionId
     * @param storeName
     */
    void sendLoginEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName);
}
