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
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;


public class CMSCartConfigurationRestriction extends GeneratedCMSCartConfigurationRestriction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CMSCartConfigurationRestriction.class.getName());




	@Override
	public String getDescription(final SessionContext ctx)
	{
		return Localization.getLocalizedString("type.CMSCartConfigurationRestriction.description.text");
	}

}
