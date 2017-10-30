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
import de.hybris.platform.util.TaxValue;

import java.util.Collection;

/**
 * Transfer object for correct yaas format.
 */
public class OrderLineItem {

    private String ref;
    private String type;
    private String unit;
    private String price_list;
    private String price_effective;
    private String currency;
    private String taxAmount;
    private String status;

    private Integer pos;
    private Long quantity;

    public OrderLineItem(AbstractOrderEntryModel abstractOrderEntryModel) {
        this.pos = abstractOrderEntryModel.getEntryNumber();
        this.ref = abstractOrderEntryModel.getProduct().getCode();
        this.type = abstractOrderEntryModel.getProduct().getItemtype();
        this.unit = abstractOrderEntryModel.getUnit() != null ? abstractOrderEntryModel.getUnit().getCode() : "";
        this.price_list = Utils.formatDouble(abstractOrderEntryModel.getBasePrice());
        this.price_effective = Utils.formatDouble(abstractOrderEntryModel.getTotalPrice());
        this.currency = abstractOrderEntryModel.getOrder() != null ? (abstractOrderEntryModel.getOrder().getCurrency() != null ? abstractOrderEntryModel.getOrder().getCurrency().getIsocode() : "") : "";
        this.quantity = abstractOrderEntryModel.getQuantity();

        final Collection<TaxValue> taxValues = abstractOrderEntryModel.getTaxValues();
        if (taxValues != null) {
            for (TaxValue tv : taxValues) {
                this.taxAmount = Utils.formatDouble(tv.getValue());
                break;
            }
        }
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice_list() {
        return price_list;
    }

    public void setPrice_list(String price_list) {
        this.price_list = price_list;
    }

    public String getPrice_effective() {
        return price_effective;
    }

    public void setPrice_effective(String price_effective) {
        this.price_effective = price_effective;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderLineItem{");
        sb.append("ref='").append(ref).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", unit='").append(unit).append('\'');
        sb.append(", price_list='").append(price_list).append('\'');
        sb.append(", price_effective='").append(price_effective).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", taxAmount='").append(taxAmount).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", pos=").append(pos);
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}
