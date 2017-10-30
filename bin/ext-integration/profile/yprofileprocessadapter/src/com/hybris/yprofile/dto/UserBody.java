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
import de.hybris.platform.core.model.user.UserModel;

/**
 * Transfer object for correct yaas format.
 */
public class UserBody {

    private String date;
    private Consumer identity;
    private String type;
    private UserMasterData masterData;

    public UserBody(UserModel userModel) {
        this.type = "YaaS account";
        this.date = Utils.formatDate(userModel.getCreationtime());
        this.identity = new Consumer(userModel.getUid(), "email");
        this.masterData = new UserMasterData(userModel);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Consumer getIdentity() {
        return identity;
    }

    public void setIdentity(Consumer identity) {
        this.identity = identity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserMasterData getMasterData() {
        return masterData;
    }

    public void setMasterData(UserMasterData masterData) {
        this.masterData = masterData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserBody{");
        sb.append("date='").append(date).append('\'');
        sb.append(", identity=").append(identity);
        sb.append(", type='").append(type).append('\'');
        sb.append(", masterData=").append(masterData);
        sb.append('}');
        return sb.toString();
    }
}
