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
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;


public class FractusSyncNullValueTranslator extends AbstractValueTranslator
{

	private static final String DEFAULT_EMPTY_EXPR = "<empty>";

	@Override
	public Object importValue(final String valueExpr, final Item toItem) throws JaloInvalidParameterException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Null values are not processed by datahub.Exporting "<empty>" for null valued attributes.
	 */
	@Override
	public String exportValue(final Object value) throws JaloInvalidParameterException
	{
		if (value == null)
		{
			return DEFAULT_EMPTY_EXPR;
		}
		return (String) value;
	}

}
