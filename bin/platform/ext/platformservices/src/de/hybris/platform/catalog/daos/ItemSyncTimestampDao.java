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
package de.hybris.platform.catalog.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;


/**
 * {@link ItemSyncTimestampModel} oriented Data Access Object.
 */
public interface ItemSyncTimestampDao
{
	/**
	 * Returns a limited list of sync timestamps for a specified
	 * <code>item<code>.<br>The <code>item<code> may be both: {@link ItemSyncTimestampModel#SOURCEITEM} or {@link ItemSyncTimestampModel#TARGETITEM}.
	 * 
	 * @param item
	 *           {@link ItemModel} for which we are searching sync timestamps
	 * @param limit
	 *           limit count of result (-1 : no limit)
	 */
	List<ItemSyncTimestampModel> findSyncTimestampsByItem(ItemModel item, int limit);

	/**
	 * Returns a limited list of sync timestamps for a specified
	 * <code>catalogVersion<code>.<br> The <code>catalogVersion<code> may be both {@link ItemSyncTimestampModel#SOURCEVERSION} or {@link ItemSyncTimestampModel#TARGETVERSION}.
	 * 
	 * @param catalogVersion
	 *           {@link CatalogVersionModel} for which we are searching sync timestamps
	 * @param limit
	 *           limit count of result (-1 : no limit)
	 */
	List<ItemSyncTimestampModel> findSyncTimestampsByCatalogVersion(CatalogVersionModel catalogVersion, int limit);

}
