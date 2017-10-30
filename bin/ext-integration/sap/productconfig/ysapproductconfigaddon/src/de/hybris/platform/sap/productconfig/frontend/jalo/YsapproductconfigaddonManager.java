/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.frontend.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;

import org.apache.log4j.Logger;


public class YsapproductconfigaddonManager extends GeneratedYsapproductconfigaddonManager
{
	private final static Logger LOG = Logger.getLogger(YsapproductconfigaddonManager.class.getName());

	public static final YsapproductconfigaddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YsapproductconfigaddonManager) em.getExtension(SapproductconfigaddonConstants.EXTENSIONNAME);
	}


	/**
	 * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
	 * like registering a jalo session listener. Each manager is created once for each tenant.
	 */
	public YsapproductconfigaddonManager()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("constructor of ysapproductconfigaddonManager called.");
		}
	}
}
