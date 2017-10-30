/*
 *  
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
 */
package de.hybris.platform.warehousing.sourcing.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;

import java.util.HashMap;
import java.util.Objects;


public class SourcingResultBuilder
{
	private final SourcingResult result;

	private SourcingResultBuilder()
	{
		result = new SourcingResult();
	}

	private SourcingResult getResult()
	{
		return this.result;
	}

	public static SourcingResultBuilder aResult()
	{
		return new SourcingResultBuilder();
	}

	public SourcingResult build()
	{
		return getResult();
	}

	public SourcingResultBuilder withWarehouse(final WarehouseModel warehouse)
	{
		getResult().setWarehouse(warehouse);
		return this;
	}

	public SourcingResultBuilder withAllocation(final AbstractOrderEntryModel orderEntry, final Long quantity)
	{
		if (Objects.isNull(getResult().getAllocation()))
		{
			getResult().setAllocation(new HashMap<>());
		}
		getResult().getAllocation().put(orderEntry, quantity);
		return this;
	}
}
