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

import de.hybris.platform.core.model.user.UserModel;

/**
 * Transfer object for correct yaas format.
 */
public class UserMasterData {

    private Address address;

    public UserMasterData(UserModel userModel) {
        this.address = new Address(userModel.getDefaultPaymentAddress());
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserMasterData{");
        sb.append("address=").append(address);
        sb.append('}');
        return sb.toString();
    }
}
