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
import de.hybris.platform.core.model.order.OrderModel;

/**
 * Transfer object for correct yaas format.
 */
public class Order {

    private String channelRef;
    private String date;
    private String type;
    private Consumer consumer;

    private OrderBody body;

    public Order(final OrderModel orderModel, final String channelRef, final String type) {
        this.channelRef = channelRef;
        this.type = type;
        this.date = Utils.formatDate(orderModel.getCreationtime());
        this.consumer = new Consumer(orderModel.getUser().getUid(), "email");
        this.body = new OrderBody(orderModel);
    }

    public Order(final String channelRef, final String type) {
        this.channelRef = channelRef;
        this.type = type;
    }

    public String getChannelRef() {
        return channelRef;
    }

    public void setChannelRef(String channelRef) {
        this.channelRef = channelRef;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public OrderBody getBody() {
        return body;
    }

    public void setBody(OrderBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("channelRef='").append(channelRef).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", consumer=").append(consumer);
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
