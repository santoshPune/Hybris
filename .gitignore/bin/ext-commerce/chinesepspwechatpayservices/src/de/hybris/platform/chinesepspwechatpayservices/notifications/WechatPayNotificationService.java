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
package de.hybris.platform.chinesepspwechatpayservices.notifications;

import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;


/**
 * Provide method to handle notification from Wechat
 */
public interface WechatPayNotificationService
{
	/**
	 * Handling the Asyn-response of the 3rd part payment service provider server
	 *
	 * @param request
	 *           The HttpServletRequest
	 * @param response
	 *           The HttpServletResponse
	 */
	void handleWechatPayPaymentResponse(final WechatRawDirectPayNotification wechatpayNotification);
}
