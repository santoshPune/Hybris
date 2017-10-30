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

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;


/**
 * ordermanagementfacade populator for converting order
 */
public class OrdermanagementOrderPopulator extends AbstractOrderPopulator<OrderModel, OrderData> {
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;

	@Override
	public void populate( final OrderModel source, final OrderData target ) throws ConversionException {
		org.springframework.util.Assert.notNull( source, "Parameter source cannot be null." );
		org.springframework.util.Assert.notNull( target, "Parameter target cannot be null." );

		addCommon(source, target);
		addEntries(source, target);
		addTotals(source, target);
		addPromotions(source, target);
		addDeliveryAddress(source, target);
		addDeliveryMethod(source, target);
		addPaymentInformation(source, target);
		checkForGuestCustomer(source, target);
		addDeliveryStatus(source, target);
		addPrincipalInformation(source, target);
		addConsignments(source, target);
	}

	protected void addConsignments( final OrderModel source, final OrderData target ) {
		target.setConsignments( Converters.convertAll( source.getConsignments(), getConsignmentConverter() ) );
	}

	protected Converter<ConsignmentModel, ConsignmentData> getConsignmentConverter() {
		return consignmentConverter;
	}

	@Required
	public void setConsignmentConverter( final Converter<ConsignmentModel, ConsignmentData> consignmentConverter ) {
		this.consignmentConverter = consignmentConverter;
	}
}
