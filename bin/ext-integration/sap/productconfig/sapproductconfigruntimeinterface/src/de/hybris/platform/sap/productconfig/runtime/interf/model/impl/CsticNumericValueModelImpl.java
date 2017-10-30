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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;


import java.util.regex.Pattern;


public class CsticNumericValueModelImpl extends CsticValueModelImpl
{
	private static final String NUMERIC_FORMAT = "-?\\d+(\\.\\d+)?(E{1}\\d{1})?";

	private static final ThreadLocal<Pattern> numericFormatCache = new ThreadLocal()
	{
		@Override
		protected Pattern initialValue()
		{
			return Pattern.compile(NUMERIC_FORMAT);
		}
	};

	@Override
	protected boolean compareName(final CsticValueModelImpl other)
	{
		if (super.compareName(other))
		{
			return true;
		}

		final Pattern numericFormatPattern = numericFormatCache.get();
		if (!numericFormatPattern.matcher(getName()).matches() || !numericFormatPattern.matcher(other.getName()).matches())
		{
			return false;
		}

		if (0 != Double.compare(Double.parseDouble(getName()), Double.parseDouble(other.getName())))
		{
			return false;
		}

		return true;
	}
}
