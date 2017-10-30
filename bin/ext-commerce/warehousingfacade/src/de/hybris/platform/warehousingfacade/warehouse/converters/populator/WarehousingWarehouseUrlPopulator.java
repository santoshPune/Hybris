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
package de.hybris.platform.warehousingfacade.warehouse.converters.populator;

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Populates the URI for the given {@link WarehouseModel}
 */
public class WarehousingWarehouseUrlPopulator implements Populator<WarehouseModel, WarehouseData>
{
	private UrlResolver<WarehouseModel> warehouseModelUrlResolver;

	@Override
	public void populate(final WarehouseModel source, final WarehouseData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setUrl(getWarehouseModelUrlResolver().resolve(source));
	}

	protected UrlResolver<WarehouseModel> getWarehouseModelUrlResolver()
	{
		return warehouseModelUrlResolver;
	}

	@Required
	public void setWarehouseModelUrlResolver(final UrlResolver<WarehouseModel> warehouseModelUrlResolver)
	{
		this.warehouseModelUrlResolver = warehouseModelUrlResolver;
	}

}
