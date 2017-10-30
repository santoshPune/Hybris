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
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;


/**
 * Populates creation time and modified time of an {@link AbstractOrderData} target object from an {@link OrderModel}
 * source object.
 */
public class CisOrderTimePopulator implements Populator<OrderModel, AbstractOrderData>
{

	@Override
	public void populate(final OrderModel source, final AbstractOrderData target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setCreationTime(source.getCreationtime());
		target.setModifiedTime(source.getModifiedtime());
	}

}
