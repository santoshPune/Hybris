/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.secureportaladdon.cockpit.label;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;


public class CMSSiteLabelProvider extends AbstractModelLabelProvider<CMSSiteModel>
{
	@Override
	protected String getItemLabel(final CMSSiteModel cmsSite)
	{
		return StringUtils.trimToEmpty(cmsSite.getName());
	}

	@Override
	protected String getItemLabel(final CMSSiteModel cmsSite, final String languageIso)
	{
		return StringUtils.trimToEmpty(cmsSite.getName(LocaleUtils.toLocale(languageIso)));
	}

	@Override
	protected String getIconPath(final CMSSiteModel item)
	{
		return null;
	}

	@Override
	protected String getIconPath(final CMSSiteModel item, final String languageIso)
	{
		return null;
	}

	@Override
	protected String getItemDescription(final CMSSiteModel item)
	{
		return "";
	}

	@Override
	protected String getItemDescription(final CMSSiteModel item, final String languageIso)
	{
		return "";
	}

}
