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

/**
 * Transfer object for correct yaas format.
 */
public class ShipmentInfo {

    private Address address;
    private String carrier;
    private String trackingRef;
    private String status;

    public ShipmentInfo(Address address, String status) {
        this.address = address;
        this.status = status;
    }

    public ShipmentInfo(Address address, String carrier, String trackingRef, String status) {
        this.address = address;
        this.carrier = carrier;
        this.trackingRef = trackingRef;
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTrackingRef() {
        return trackingRef;
    }

    public void setTrackingRef(String trackingRef) {
        this.trackingRef = trackingRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShipmentInfo{");
        sb.append("address=").append(address);
        sb.append(", carrier='").append(carrier).append('\'');
        sb.append(", trackingRef='").append(trackingRef).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
