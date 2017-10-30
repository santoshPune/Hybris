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
import de.hybris.platform.jalo.user.PhoneContactInfo;
import java.util.Collection;


/**
 * 
 * Translator to get phone number for customer
 *
 */
public class FractusCustomerContactTranslator extends AbstractValueTranslator
{

	private static final String DEFAULT_EMPTY_EXPR = "<empty>";

	@Override
	public String exportValue(Object value) throws JaloInvalidParameterException
	{
		if (value instanceof Collection)
		{
			for (Object o : (Collection) value)
			{
				if (o instanceof PhoneContactInfo)
				{
					return ((PhoneContactInfo) o).getPhoneNumber();
				}
			}
		}
		return DEFAULT_EMPTY_EXPR;
	}


	@Override
	public Object importValue(String arg0, Item arg1) throws JaloInvalidParameterException
	{
		throw new UnsupportedOperationException();
	}

}
