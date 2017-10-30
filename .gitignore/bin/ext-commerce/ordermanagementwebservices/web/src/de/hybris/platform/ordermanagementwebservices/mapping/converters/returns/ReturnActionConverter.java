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
package de.hybris.platform.ordermanagementwebservices.mapping.converters.returns;

import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;


/**
 * Bidirectional converter between {@link ReturnAction} and String
 */
@WsDTOMapping
public class ReturnActionConverter extends BidirectionalConverter<ReturnAction, String>
{
	@Override
	public ReturnAction convertFrom(final String source, final Type<ReturnAction> destinationType)
	{
		return ReturnAction.valueOf(source);
	}

	@Override
	public String convertTo(final ReturnAction source, final Type<String> destinationType)
	{
		return source.toString();
	}
}
