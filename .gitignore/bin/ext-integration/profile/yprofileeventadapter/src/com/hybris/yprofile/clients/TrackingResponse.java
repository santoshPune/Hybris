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
package com.hybris.yprofile.clients;

import java.util.Objects;

public class TrackingResponse {

    private String response;

    public TrackingResponse(String response) {
        this.response = response;
    }

    public TrackingResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingResponse that = (TrackingResponse) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrackingResponse{");
        sb.append("response='").append(response).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
