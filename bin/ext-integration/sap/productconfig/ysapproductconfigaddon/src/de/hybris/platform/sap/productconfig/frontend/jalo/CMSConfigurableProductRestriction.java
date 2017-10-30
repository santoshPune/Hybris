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

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;

import org.apache.log4j.Logger;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CMSConfigurableProductRestriction extends GeneratedCMSConfigurableProductRestriction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CMSConfigurableProductRestriction.class.getName());

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.cms2.jalo.restrictions.AbstractRestriction#getDescription(de.hybris.platform.jalo.
	 * SessionContext)
	 */
	@Override
	public String getDescription(final SessionContext ctx)
	{
		String result = Localization.getLocalizedString("type.CMSConfigurableRestriction.description.text");
		if (result == null)
		{
			result = "Page/Component only applies for configurable products";
		}
		return result;
	}
}
