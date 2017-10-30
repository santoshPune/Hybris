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
package de.hybris.platform.recentvieweditemsservices.impl;

import de.hybris.platform.recentvieweditemsservices.RecentViewedItemsService;
import de.hybris.platform.recentvieweditemsservices.util.RecentViewedItemsCollection;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultRecentViewedItemsService implements RecentViewedItemsService
{

	static protected final String VISITED_PRODUCTS_KEY = "RECENT_VIEWED_ITEMS";

	private SessionService sessionService;
	private int maxRecentViewedItems;

	@Override
	public void productVisited(final String productCode, final String categoryCode)
	{
		final RecentViewedItemsCollection collection = getRecentViewedItemsCollection();
		collection.addProductCode(productCode);
		collection.addCategoryCode(categoryCode);
	}

	protected RecentViewedItemsCollection getRecentViewedItemsCollection()
	{
		return sessionService.getOrLoadAttribute(VISITED_PRODUCTS_KEY,
				() -> new RecentViewedItemsCollection(this.getMaxRecentViewedItems()));
	}

	@Override
	public List<String> getRecentViewedProducts()
	{
		return getRecentViewedItemsCollection().getProductCodes();
	}

	@Override
	public List<String> getRecentViewedCategories()
	{
		return getRecentViewedItemsCollection().getCategoryCodes();
	}


	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the maxRecentViewedItems
	 */
	public int getMaxRecentViewedItems()
	{
		return maxRecentViewedItems;
	}

	/**
	 * @param maxRecentViewedItems
	 *           the maxRecentViewedItems to set
	 */
	public void setMaxRecentViewedItems(final int maxRecentViewedItems)
	{
		this.maxRecentViewedItems = maxRecentViewedItems;
	}
}
