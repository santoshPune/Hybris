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
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


/**
 *
 */
public class UiDataStats
{
	private int numCstics = 0;
	private int numUiGroups = 0;
	private int level = 0;
	private int maxlevel = 0;

	public void countCstics(final List<UiGroupData> groups)
	{
		if (groups != null)
		{
			level += 1;
			if (maxlevel < level)
			{
				maxlevel = level;
			}
			for (final UiGroupData group : groups)
			{
				numUiGroups += 1;
				final List<CsticData> cstics = group.getCstics();
				if (cstics != null)
				{
					numCstics += cstics.size();
				}
				countCstics(group.getSubGroups());
			}
			level -= 1;
		}
	}

	@Override
	public String toString()
	{
		final NumberFormat decFormat = DecimalFormat.getInstance(Locale.ENGLISH);
		return "UiModelStats: numberCstics=" + decFormat.format(numCstics) + "; numUiGroups=" + decFormat.format(numUiGroups)
				+ "; maxlevel=" + maxlevel;
	}
}
