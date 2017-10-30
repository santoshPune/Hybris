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

import de.hybris.platform.commercefacades.order.converters.populator.ConsignmentPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Ordermanagement Converter for converting Consignments
 */
public class OrdermanagementConsignmentPopulator extends ConsignmentPopulator
{
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;

	@Override
	public void populate(final ConsignmentModel source, final ConsignmentData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setOrderCode(source.getOrder().getCode());
		target.setShippingDate(source.getShippingDate());
		target.setWarehouseCode(source.getWarehouse().getCode());
		target.setDeliveryMode(
				source.getDeliveryMode() != null ? getDeliveryModeConverter().convert(source.getDeliveryMode()) : null);
		super.populate(source, target);

	}

	protected Converter<DeliveryModeModel, DeliveryModeData> getDeliveryModeConverter()
	{
		return deliveryModeConverter;
	}

	@Required
	public void setDeliveryModeConverter(final Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter)
	{
		this.deliveryModeConverter = deliveryModeConverter;
	}

}
