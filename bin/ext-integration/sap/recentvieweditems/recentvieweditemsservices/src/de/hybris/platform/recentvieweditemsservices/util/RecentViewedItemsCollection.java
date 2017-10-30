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
package de.hybris.platform.recentvieweditemsservices.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class RecentViewedItemsCollection
{

	private final List<String> productCodes;
	private final List<String> categoryCodes;
	private static final int DEFAULT_MAX_ENTRIES = 3;
	private int maxEntries = 0;

	public RecentViewedItemsCollection(final int maxEntries)
	{
		if (maxEntries < 1)
		{
			this.maxEntries = DEFAULT_MAX_ENTRIES;
			throw new IllegalArgumentException(
					"Maximum size not configured properly in backoffice." + " Using default value " + DEFAULT_MAX_ENTRIES);
		}
		this.maxEntries = maxEntries;
		productCodes = new ArrayList<String>(maxEntries + 1);
		categoryCodes = new ArrayList<String>(maxEntries + 1);
	}

	public synchronized void addProductCode(final String code)
	{
		if (!productCodes.contains(code))
		{
			productCodes.add(0, code);
			while (productCodes.size() > maxEntries)
			{
				productCodes.remove(maxEntries);
			}
		}
	}

	public synchronized void addCategoryCode(final String code)
	{
		if (!categoryCodes.contains(code))
		{
			categoryCodes.add(0, code);
			while (categoryCodes.size() > maxEntries)
			{
				categoryCodes.remove(maxEntries);
			}
		}
	}

	public List<String> getProductCodes()
	{
		return Collections.unmodifiableList(productCodes);
	}


	public List<String> getCategoryCodes()
	{
		return Collections.unmodifiableList(categoryCodes);
	}


}
