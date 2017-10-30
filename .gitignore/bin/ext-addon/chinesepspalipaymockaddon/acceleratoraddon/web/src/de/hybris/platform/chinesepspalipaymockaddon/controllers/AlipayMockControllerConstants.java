/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspalipaymockaddon.controllers;

/**
 *
 */
public interface AlipayMockControllerConstants
{

	// implement here controller constants used by this extension
	final String ADDON_PREFIX = "addon:/chinesepspalipaymockaddon/";

	interface Pages
	{
		static final String AlipayMockPage = ADDON_PREFIX + "pages/alipay/mockWeb";
		static final String AlipayRefundTestPage = ADDON_PREFIX + "pages/alipay/refundTestPage";
		static final String AlipayRefundPage = ADDON_PREFIX + "pages/alipay/refundWeb";
	}

}
