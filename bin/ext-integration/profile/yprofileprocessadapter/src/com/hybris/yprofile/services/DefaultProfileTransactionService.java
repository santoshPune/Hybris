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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hybris.yprofile.clients.ProfileClient;
import com.hybris.yprofile.dto.*;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link ProfileTransactionService}. Communication service to send transactions to Profile
 */
public class DefaultProfileTransactionService implements ProfileTransactionService {
    private static final Logger LOG = Logger.getLogger(DefaultProfileTransactionService.class);
    private static final String NULL = "null";
    private static final String ACCOUNT_REGISTRATION_EVENT_TYPE = "account registration";
    private static final String LOGIN_EVENT_TYPE = "login";
    private static final String NEW_ORDER_EVENT_TYPE = "order";
    private static final String ORDER_SHIPPED_EVENT_TYPE ="order shipment";
    private static final String COMPLETE_RETURN_ORDER_EVENT_TYPE ="return";
    private static final String PARTIAL_RETURN_ORDER_EVENT_TYPE ="partial return";
    private static final String ORDER_STATUS_COMPLETED = "completed";
    private static final String ORDER_STATUS_PARTIAL_DELIVERED = "partial delivered";
    private static final String RETURN_ORDER_ENTRY_STATUS = "returned";

    private ProfileCharonFactory charonFactory;

    private ProfileConfigurationService profileConfigurationService;

    /**
     * Send order to yprofile.
     * @param orderModel
     * @return
     */
    @Override
    public void sendSubmitOrderEvent(final OrderModel orderModel) {
        final Order order = new Order(orderModel, orderModel.getStore().getUid(), NEW_ORDER_EVENT_TYPE);

        sendOrder(orderModel.getConsentReference(), order);
    }

    /**
     * Send consignment to yprofile.
     * @param consignmentModel
     * @return
     */
    @Override
    public void sendConsignmentEvent(final ConsignmentModel consignmentModel) {

        final OrderModel orderModel = (OrderModel) consignmentModel.getOrder();
        final Order order = new Order(orderModel.getStore().getUid(), ORDER_SHIPPED_EVENT_TYPE);
        final ShipmentInfo shipmentInfo = new ShipmentInfo(new Address(consignmentModel.getShippingAddress()),
                consignmentModel.getCarrier(), consignmentModel.getTrackingID(), consignmentModel.getStatusDisplay());

        List<OrderLineItem> lineItems = new ArrayList<>();
        consignmentModel.getConsignmentEntries().stream().forEach(consignmentEntry -> {
                    OrderLineItem lineItem = new OrderLineItem(consignmentEntry.getOrderEntry());
                    lineItem.setStatus(consignmentModel.getStatusDisplay());
            lineItem.setQuantity(consignmentEntry.getQuantity());
                    lineItems.add(lineItem);
                }
        );

        final OrderBody orderBody = new OrderBody(lineItems,
                getOrderStatus(orderModel),
                consignmentModel.getCreationtime(),
                orderModel.getCode(),
                shipmentInfo);

        order.setBody(orderBody);

        sendOrder(orderModel.getConsentReference(), order);
    }

    private String getOrderStatus(OrderModel orderModel) {
        for (ConsignmentModel consignment : orderModel.getConsignments()) {
            if (!consignment.getStatus().equals(ConsignmentStatus.SHIPPED) && !consignment.getStatus().equals(ConsignmentStatus.PICKUP_COMPLETE)) {
                return ORDER_STATUS_PARTIAL_DELIVERED;
            }

        }
        return ORDER_STATUS_COMPLETED;
    }

    /**
     * Send return to yprofile.
     * @param returnRequestModel
     * @return
     */
    @Override
    public void sendReturnOrderEvent(final ReturnRequestModel returnRequestModel) {

        OrderModel orderModel = returnRequestModel.getOrder();

        List<OrderLineItem> lineItems = new ArrayList<>();
        returnRequestModel.getReturnEntries().stream().forEach( returnEntry -> {
            OrderLineItem lineItem = new OrderLineItem(returnEntry.getOrderEntry());
            lineItem.setStatus(RETURN_ORDER_ENTRY_STATUS);
            lineItem.setQuantity(returnEntry.getExpectedQuantity());
            lineItems.add(lineItem);
        });

        final Order order = new Order(orderModel.getStore().getUid(), getReturnEventType(orderModel.getEntries(), returnRequestModel.getReturnEntries()));
        final OrderBody orderBody = new OrderBody(lineItems,
                orderModel.getStatusDisplay(),
                returnRequestModel.getCreationtime(),
                orderModel.getCode(),
                null);

        order.setBody(orderBody);

        sendOrder(orderModel.getConsentReference(), order);
    }

    private String getReturnEventType(List<AbstractOrderEntryModel> orderEntries, List<ReturnEntryModel> returnEntries) {
        if (orderEntries.size() == returnEntries.size()){
            for (ReturnEntryModel entry : returnEntries) {
                if (entry.getOrderEntry().getQuantity() != entry.getReceivedQuantity()){
                    return PARTIAL_RETURN_ORDER_EVENT_TYPE;
                }
            }
            return COMPLETE_RETURN_ORDER_EVENT_TYPE;
        }

        return PARTIAL_RETURN_ORDER_EVENT_TYPE;
    }



    private void sendOrder(String consentReference, Order order) {
        if (getProfileConfigurationService().isYaaSConfigurationPresent() && isValidConsentReference(consentReference)) {
            getClient().sendOrderEvent(getProfileConfigurationService().getYaaSTenant(), consentReference, order)
                    .subscribe(response -> logSuccess(order),
                            error -> logError(error, order.toString()),
                            () -> logSuccess(order));
        }
    }

    /**
     * Send user registration event to yprofile.
     * @param userModel
     * @param storeName
     * @return
     */
    @Override
    public void sendUserRegistrationEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName) {
        sendUserEvent(userModel, consentReferenceId, sessionId, storeName, ACCOUNT_REGISTRATION_EVENT_TYPE);
    }

    /**
     * Send user login event to yprofile.
     * @param userModel
     * @param storeName
     * @return
     */
    @Override
    public void sendLoginEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName) {
        sendUserEvent(userModel, consentReferenceId, sessionId, storeName, LOGIN_EVENT_TYPE);
    }

    private void sendUserEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName, final String eventType) {
        final User user = new User(userModel, eventType, sessionId, storeName);

        if (getProfileConfigurationService().isYaaSConfigurationPresent() && isValidConsentReference(consentReferenceId)) {
            getClient().sendUserEvent(getProfileConfigurationService().getYaaSTenant(), eventType, consentReferenceId, user)
                    .subscribe(response -> logSuccess(user),
                            error -> logError(error, user.toString()),
                            () -> logSuccess(user));
        }
    }

    private boolean isValidConsentReference(String consentReferenceId) {
        return StringUtils.isNotBlank(consentReferenceId) && !NULL.equals(consentReferenceId);
    }

    private void logSuccess(final Object obj) {
        if (LOG.isDebugEnabled()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            String event = obj.toString();
            try {
                event = mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                /*ignore*/
            }
            LOG.debug(event + " sent to yprofile ");
        }
    }

    private void logError(final Throwable error, final String obj) {
        LOG.error(obj.toString() + " sending to yprofile failed", error);
    }

    private ProfileClient getClient(){
        return getCharonFactory().client(getProfileConfigurationService().getApplicationId(), ProfileClient.class);
    }

    public ProfileCharonFactory getCharonFactory() {
        return charonFactory;
    }

    @Required
    public void setCharonFactory(ProfileCharonFactory charonFactory) {
        this.charonFactory = charonFactory;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
