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

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;

/**
 * Transfer object for correct yaas format.
 */
public class PaymentInfo {

    private String paymentType;
    private String status;
    private Address address;

    public PaymentInfo(OrderModel orderModel) {

        PaymentInfoModel paymentInfo = orderModel.getPaymentInfo();

        this.paymentType = paymentInfo != null ? paymentInfo.getItemtype() : "";

        if (paymentInfo instanceof CreditCardPaymentInfoModel){
            this.paymentType = ((CreditCardPaymentInfoModel) paymentInfo).getType().toString();
        }

        this.status = orderModel.getPaymentStatus() != null ? orderModel.getPaymentStatus().toString() : "";
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentInfo{");
        sb.append("paymentType='").append(paymentType).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
