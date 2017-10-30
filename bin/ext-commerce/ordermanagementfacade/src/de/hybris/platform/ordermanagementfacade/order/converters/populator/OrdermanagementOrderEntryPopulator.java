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
 *
 */
package de.hybris.platform.ordermanagementfacade.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * ordermanagementfacade populator for converting orderEntry's dynamic attributes added by oms
 */

public class OrdermanagementOrderEntryPopulator extends OrderEntryPopulator
{
	@Override
	public void populate( final AbstractOrderEntryModel source, final OrderEntryData target ) throws ConversionException {
		org.springframework.util.Assert.notNull( source, "Parameter source cannot be null." );
		org.springframework.util.Assert.notNull( target, "Parameter target cannot be null." );

		if(source instanceof OrderEntryModel)
		{
			OrderEntryModel orderEntry = (OrderEntryModel) source;
			target.setQuantityAllocated(orderEntry.getQuantityAllocated());
			target.setQuantityUnallocated(orderEntry.getQuantityUnallocated());
			target.setQuantityCancelled(orderEntry.getQuantityCancelled());
			target.setQuantityPending(orderEntry.getQuantityPending());
			target.setQuantityShipped(orderEntry.getQuantityShipped());
			target.setQuantityReturned(orderEntry.getQuantityReturned());
		}

		super.populate(source,target);
	}
}
