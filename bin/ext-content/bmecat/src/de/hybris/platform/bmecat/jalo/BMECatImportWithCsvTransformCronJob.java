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
 *  
 */
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


public class BMECatImportWithCsvTransformCronJob extends GeneratedBMECatImportWithCsvTransformCronJob
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(BMECatImportWithCsvTransformCronJob.class.getName());

	private boolean parserError = false;

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	public boolean isParserError()
	{
		return parserError;
	}

	public void setParserError(final boolean parserError)
	{
		this.parserError = parserError;
	}

}
