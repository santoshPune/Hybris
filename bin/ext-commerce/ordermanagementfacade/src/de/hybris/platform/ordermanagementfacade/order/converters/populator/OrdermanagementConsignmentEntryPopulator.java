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

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentEntryPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import org.springframework.util.Assert;


/**
 * Ordermanagement Converter for converting {@link ConsignmentEntryModel}
 */
public class OrdermanagementConsignmentEntryPopulator extends ConsignmentEntryPopulator
{
	@Override
	public void populate(final ConsignmentEntryModel source, final ConsignmentEntryData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setQuantityDeclined(source.getQuantityDeclined());
		target.setQuantityPending(source.getQuantityPending());
		target.setQuantityShipped(source.getQuantityShipped());

		super.populate(source,target);
	}


}
