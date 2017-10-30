/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;


public class ValueFormatTranslatorImpl implements ValueFormatTranslator
{

	private static final Logger LOG = Logger.getLogger(ValueFormatTranslatorImpl.class);
	private static final int HIGH_FRACTION_COUNT = 99;
	private I18NService i18NService;

	private static final ThreadLocal<Map<Locale, DecimalFormat>> decimalFormatCache = new ThreadLocal()
	{
		@Override
		protected Map<Locale, DecimalFormat> initialValue()
		{
			return new HashMap<>();
		}
	};

	private static final ThreadLocal<DecimalFormat> serviceFormatCache = new ThreadLocal()
	{
		@Override
		protected DecimalFormat initialValue()
		{
			final DecimalFormat numberFormatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
			numberFormatter.setParseBigDecimal(true);
			numberFormatter.setGroupingUsed(false);
			numberFormatter.setMaximumFractionDigits(HIGH_FRACTION_COUNT);
			return numberFormatter;

		}
	};

	@Override
	public String parse(final UiType uiType, final String value)
	{
		String parsedValue;

		if (UiType.NUMERIC == uiType)
		{
			parsedValue = parseNumeric(value);
		}
		else
		{
			parsedValue = value;
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Formatted value [INPUT_VALUE='" + value + "';PARSED_VALUE='" + parsedValue + "';UI_TYPE='" + uiType + "']");
		}

		return parsedValue;
	}

	protected String parseNumeric(final String value)
	{
		if (value == null || value.isEmpty())
		{
			return "";
		}

		String parsedValue;
		final Locale locale = getLocale();
		BigDecimal number;
		try
		{
			DecimalFormat numberFormatter = createFormatterForUI(locale);
			number = (BigDecimal) numberFormatter.parse(value);

			numberFormatter = createFormatterForService();
			parsedValue = numberFormatter.format(number);
		}
		catch (final ParseException e)
		{
			LOG.debug("Could not parse numeric value '" + value + "'");
			return "";
		}
		return parsedValue;
	}

	protected String formatNumeric(final String value)
	{
		if (null == value)
		{
			return "";
		}

		String formattedValue;
		final Locale locale = getLocale();
		BigDecimal number;
		try
		{
			DecimalFormat numberFormatter = createFormatterForService();
			number = (BigDecimal) numberFormatter.parse(value);

			numberFormatter = createFormatterForUI(locale);
			formattedValue = numberFormatter.format(number);
		}
		catch (final ParseException e)
		{
			LOG.debug("Could not format numeric value '" + value + "'");
			return value;
		}
		return formattedValue;
	}

	// have a guard for the tests
	protected Locale getLocale()
	{
		Locale locale;
		if (i18NService == null)
		{
			locale = Locale.ENGLISH;
		}
		else
		{
			locale = i18NService.getCurrentLocale();
		}
		return locale;
	}

	protected DecimalFormat createFormatterForUI(final Locale locale)
	{

		final Map<Locale, DecimalFormat> formatCache = decimalFormatCache.get();
		DecimalFormat numberFormatter = formatCache.get(locale);
		if (numberFormatter == null)
		{
			numberFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
			numberFormatter.setGroupingUsed(true);
			numberFormatter.setParseBigDecimal(true);
			numberFormatter.setMaximumFractionDigits(HIGH_FRACTION_COUNT);
			formatCache.put(locale, numberFormatter);
		}
		return numberFormatter;
	}

	protected DecimalFormat createFormatterForService()
	{
		return serviceFormatCache.get();
	}

	/**
	 * @deprecated Replaced by format(CsticModel cstic, String value)
	 */
	@Deprecated
	@Override
	public String format(final UiType uiType, final String value)
	{
		String formattedValue;

		if (UiType.NUMERIC == uiType)
		{
			formattedValue = formatNumeric(value);

		}
		else
		{
			formattedValue = value;
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(
					"Formatted value [INPUT_VALUE='" + value + "';FORMATTED_VALUE='" + formattedValue + "';UI_TYPE='" + uiType + "']");
		}

		return formattedValue;
	}

	@Override
	public String format(final CsticModel cstic, final String value)
	{
		String formattedValue;
		if (isNumericCsticType(cstic))
		{
			formattedValue = formatNumeric(value);
		}
		else
		{
			formattedValue = value;
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Formatted value [INPUT_VALUE='" + value + "';FORMATTED_VALUE='" + formattedValue + "';CSTIC_VALUE_TYPE='"
					+ cstic.getValueType() + "']");
		}
		return formattedValue;
	}

	@Override
	public boolean isNumericCsticType(final CsticModel model)
	{
		return CsticModel.TYPE_INTEGER == model.getValueType() || CsticModel.TYPE_FLOAT == model.getValueType();
	}

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	/**
	 * @return the i18NService
	 */
	public I18NService getI18NService()
	{
		return i18NService;
	}

}
