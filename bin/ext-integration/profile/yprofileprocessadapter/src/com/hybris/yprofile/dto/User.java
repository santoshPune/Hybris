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
public class User {

    private String channelRef;
    private String date;
    private String type;
    private String sessionId;
    private UserBody body;

    public User(final UserModel userModel, final String type, final String sessionId, final String storeName) {
        this.type = type;
        this.channelRef = storeName;
        this.date = Utils.formatDate(userModel.getCreationtime());
        this.sessionId = sessionId;
        this.body = new UserBody(userModel);
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserBody getBody() {
        return body;
    }

    public void setBody(UserBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("channelRef='").append(channelRef).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
