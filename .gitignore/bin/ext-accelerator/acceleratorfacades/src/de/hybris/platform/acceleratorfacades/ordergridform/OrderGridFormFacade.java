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
package de.hybris.platform.acceleratorfacades.ordergridform;


import de.hybris.platform.acceleratorfacades.product.data.ReadOnlyOrderGridData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.Map;


/**
 * Interface that can be used/implemented to populate readonly order grid for multi-d products.
 */
public interface OrderGridFormFacade
{
	/**
	 * Populates the readonly order grid
	 * 
	 * @param orderEntryDataList
	 * @return map containing grouped ReadOnlyOrderGridDatas based on the category
	 */
	Map<String, ReadOnlyOrderGridData> getReadOnlyOrderGrid(final List<OrderEntryData> orderEntryDataList);
}
