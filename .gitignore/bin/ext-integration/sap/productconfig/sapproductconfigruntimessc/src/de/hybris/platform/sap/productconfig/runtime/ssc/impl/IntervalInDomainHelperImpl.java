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
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.ssc.IntervalInDomainHelper;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.log4j.Logger;


public class IntervalInDomainHelperImpl implements IntervalInDomainHelper
{
	private static final Logger LOG = Logger.getLogger(IntervalInDomainHelperImpl.class);
	private I18NService i18NService;

	private static final int HIGH_FRACTION_COUNT = 99;

	@Override
	public boolean validateIntervals(final String value, final CsticModel cstic)
	{
		boolean isValueInInteval = false;
		try
		{
			final DecimalFormat numberFormatter = createFormatterForService();
			final BigDecimal valueAsnumber = (BigDecimal) numberFormatter.parse(value);

			for (final CsticValueModel valueModel : cstic.getAssignableValues())
			{
				if (valueModel.isDomainValue())
				{
					isValueInInteval = isValueInInterval(numberFormatter, valueAsnumber, valueModel);
				}
				if (isValueInInteval)
				{
					break;
				}
			}
		}

		catch (final ParseException e)
		{
			throw new IllegalStateException("Could not format numeric value '" + value + "'", e);
		}

		return isValueInInteval;
	}

	protected boolean isValueInInterval(final DecimalFormat numberFormatter, final BigDecimal valueAsnumber,
			final CsticValueModel valueModel) throws ParseException
	{
		boolean isValueInInteval = false;
		final String interval = valueModel.getName();
		final String[] parsedInterval = interval.split("-");
		final BigDecimal intervalMinAsNumber = (BigDecimal) numberFormatter.parse(parsedInterval[0].trim());

		if (parsedInterval.length == 1)
		{
			isValueInInteval = isIntervalMin(valueAsnumber, intervalMinAsNumber);
		}

		else if (parsedInterval.length == 2)
		{
			isValueInInteval = isInInterval(numberFormatter, valueAsnumber, parsedInterval, intervalMinAsNumber);
		}
		return isValueInInteval;
	}

	protected boolean isInInterval(final DecimalFormat numberFormatter, final BigDecimal valueAsnumber,
			final String[] parsedInterval, final BigDecimal intervalMinAsNumber) throws ParseException
	{
		boolean isValueInInteval = false;
		final BigDecimal intervalMaxAsNumber = (BigDecimal) numberFormatter.parse(parsedInterval[1].trim());
		if (valueAsnumber.compareTo(intervalMinAsNumber) >= 0 && valueAsnumber.compareTo(intervalMaxAsNumber) <= 0)
		{
			isValueInInteval = true;
		}
		return isValueInInteval;
	}

	protected boolean isIntervalMin(final BigDecimal valueAsnumber, final BigDecimal intervalMinAsNumber)
	{
		boolean isValueInInteval = false;
		if (valueAsnumber.compareTo(intervalMinAsNumber) == 0)
		{
			isValueInInteval = true;
		}
		return isValueInInteval;
	}

	@Override
	public String retrieveIntervalMask(final CsticModel cstic)
	{
		final StringBuilder intervalBuffer = new StringBuilder();
		if (cstic.getAssignableValues() != null)
		{
			for (final CsticValueModel valueModel : cstic.getAssignableValues())
			{
				if (valueModel.isDomainValue())
				{
					appendToIntervalMask(intervalBuffer, valueModel);
				}
			}
		}
		return intervalBuffer.toString().trim();

	}

	protected void appendToIntervalMask(final StringBuilder intervalBuffer, final CsticValueModel valueModel)
	{
		if (intervalBuffer.length() > 0)
		{
			intervalBuffer.append(" ; ");
		}
		intervalBuffer.append(formatNumericInterval(valueModel.getName()));
	}

	@Override
	public String formatNumericInterval(final String interval)
	{
		String formattedInterval = "";

		final String[] parsedInterval = interval.split("-");

		if (parsedInterval.length == 1)
		{
			formattedInterval = formatNumericValue(parsedInterval[0]);
		}
		else if (parsedInterval.length == 2)
		{
			final String formattedValueMin = formatNumericValue(parsedInterval[0].trim());
			final String formattedValueMax = formatNumericValue(parsedInterval[1].trim());
			formattedInterval = formattedValueMin + " - " + formattedValueMax;
		}

		return formattedInterval;
	}

	@Override
	public String formatNumericValue(final String value)
	{
		String formattedValue = "";
		if (value != null)
		{
			try
			{
				DecimalFormat numberFormatter = createFormatterForService();
				final BigDecimal number = (BigDecimal) numberFormatter.parse(value);
				numberFormatter = createFormatterForUI(i18NService.getCurrentLocale());
				formattedValue = numberFormatter.format(number);
			}
			catch (final ParseException e)
			{
				LOG.debug("Could not format numeric value '" + value + "'");
				return value;
			}
		}

		return formattedValue;
	}

	@Override
	public String retrieveErrorMessage(final String value, final String interval)
	{
		return Localization.getLocalizedString("type.ProductConfiguration.IntervalValue.conflict", new Object[]
		{ value, interval });
	}

	protected DecimalFormat createFormatterForUI(final Locale locale)
	{
		DecimalFormat numberFormatter;
		numberFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
		numberFormatter.setGroupingUsed(true);
		numberFormatter.setParseBigDecimal(true);
		numberFormatter.setMaximumFractionDigits(HIGH_FRACTION_COUNT);
		return numberFormatter;
	}

	protected DecimalFormat createFormatterForService()
	{
		final DecimalFormat numberFormatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		numberFormatter.setParseBigDecimal(true);
		numberFormatter.setGroupingUsed(false);
		numberFormatter.setMaximumFractionDigits(HIGH_FRACTION_COUNT);
		return numberFormatter;
	}

	/**
	 * @return the i18NService
	 */
	protected I18NService getI18NService()
	{
		return i18NService;
	}

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

}
