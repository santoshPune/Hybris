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
package com.hybris.yprofile.dto;

import com.hybris.yprofile.common.Utils;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transfer object for correct yaas format.
 */
public class OrderBody {

    private List<OrderLineItem> lineItems = new ArrayList<>();
    private List<Promotion> promotionInfo = new ArrayList<>();
    private PaymentInfo paymentInfo;
    private String status;
    private String date;
    private String orderId;
    private String cartId;
    private ShipmentInfo shipmentInfo;
    private String orderValue;
    private String currency;

    public OrderBody(OrderModel orderModel) {
        this.orderId = orderModel.getCode();
        this.cartId = orderModel.getCartIdReference();
        this.date = Utils.formatDate(orderModel.getCreationtime());
        this.orderValue = Utils.formatDouble(orderModel.getTotalPrice());
        this.currency = orderModel.getCurrency().getIsocode();

        this.status = orderModel.getStatusDisplay() != null ? orderModel.getStatusDisplay().toString(): "new";

        orderModel.getAllPromotionResults().forEach(
                promotionResultModel ->
                        promotionInfo.add(new Promotion(promotionResultModel.getPromotion()))
        );

        this.paymentInfo = new PaymentInfo(orderModel);
        this.paymentInfo.setAddress(new Address(orderModel.getPaymentAddress()));

        String status = orderModel.getDeliveryStatus() != null ? orderModel.getDeliveryStatus().getCode() : "not delivered";
        this.shipmentInfo = new ShipmentInfo(new Address(orderModel.getDeliveryAddress()), status);

        orderModel.getEntries().stream().forEach(
                (AbstractOrderEntryModel abstractOrderEntryModel) -> lineItems.add(new OrderLineItem(abstractOrderEntryModel))
        );
    }

    public OrderBody(List<OrderLineItem> lineItems, String status, Date date, String orderId, ShipmentInfo shipmentInfo) {
        this.lineItems = lineItems;
        this.status = status;
        this.date = Utils.formatDate(date);
        this.orderId = orderId;
        this.shipmentInfo = shipmentInfo;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<Promotion> getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(List<Promotion> promotion) {
        this.promotionInfo = promotion;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ShipmentInfo getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(ShipmentInfo shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderBody{");
        sb.append("lineItems=").append(lineItems);
        sb.append(", promotionInfo=").append(promotionInfo);
        sb.append(", paymentInfo=").append(paymentInfo);
        sb.append(", status='").append(status).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", cartId='").append(cartId).append('\'');
        sb.append(", shipmentInfo=").append(shipmentInfo);
        sb.append(", orderValue='").append(orderValue).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
