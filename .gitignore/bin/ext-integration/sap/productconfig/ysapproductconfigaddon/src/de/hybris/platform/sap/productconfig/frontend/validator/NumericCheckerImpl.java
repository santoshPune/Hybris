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
package de.hybris.platform.sap.productconfig.frontend.validator;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;


public class NumericCheckerImpl implements NumericChecker
{

	private static final String TOO_MANY_DIGITS = "Too many digits";
	private static final String TOO_MANY_FRACTIONS = "Too many fractions";
	private static final String SOURCE_FIELD_VALUE = "formattedValue";
	private static final String SOURCE_FIELD_ADDITIONAL_VALUE = "additionalValue";
	private static final String NOT_A_NUMBER = "Not a number";
	private static final char PATTERN_CHAR = '#';
	private static final char DEFAULT_DECIMAL_SEPARATOR = '.';
	private static final String MSG_KEY_NOT_NUMERIC_ERROR = "sapproductconfig.value.not.numeric";
	private static final String MSG_KEY_TOO_LONG_FRACTION = "sapproductconfig.value.too.long.fraction";
	private static final String MSG_KEY_TOO_MANY_DIGITS = "sapproductconfig.value.too.many.digits";
	private static final String MSG_KEY_NO_NEGATIVE_NUMBER = "sapproductconfig.value.no.negative";

	private I18NService i18NService;

	@Override
	public void validate(final CsticData cstic, final Errors errorObj)
	{
		final String value = cstic.getFormattedValue();
		validateValue(cstic, errorObj, value);
	}

	@Override
	public void validateAdditionalValue(final CsticData cstic, final Errors errorObj)
	{
		final String value = cstic.getAdditionalValue();
		validateValue(cstic, errorObj, value);
	}

	protected void validateValue(final CsticData cstic, final Errors errorObj, final String value)
	{
		final Locale locale = i18NService.getCurrentLocale();
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

		if (!validateValue(value, symbols))
		{
			createFieldError(cstic, value, errorObj, new Object[] {}, NOT_A_NUMBER, MSG_KEY_NOT_NUMERIC_ERROR);
			return;
		}

		final DecimalFormat numberFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
		numberFormatter.setParseBigDecimal(true);


		BigDecimal number;
		try
		{
			number = (BigDecimal) numberFormatter.parse(value);
		}
		catch (final ParseException e)
		{
			createFieldError(cstic, value, errorObj, new Object[]
			{ value }, NOT_A_NUMBER, MSG_KEY_NOT_NUMERIC_ERROR);
			return;
		}

		final boolean isNegativeNumber = BigDecimal.ZERO.compareTo(number) > 0;
		if (isNegativeNumber && !isNegativeNumberAllowed(cstic.getEntryFieldMask()))
		{
			createFieldError(cstic, value, errorObj, new Object[] {}, NOT_A_NUMBER, MSG_KEY_NO_NEGATIVE_NUMBER);
			return;
		}

		checkLength(number, value, cstic, errorObj, locale);
	}

	public boolean validateValue(final String value, final DecimalFormatSymbols symbols)
	{
		final String groupingSeperator = "\\" + symbols.getGroupingSeparator();
		final String decimalSeperator = "\\" + symbols.getDecimalSeparator();

		final String firstRegex = "^[+-]?[0-9]{1,3}(" + groupingSeperator + "[0-9]{3})*(" + decimalSeperator + "[0-9]+)?$";
		final String secondRegex = "^[+-]?[0-9]+(" + decimalSeperator + "[0-9]+)?$";

		final Pattern firstPattern = Pattern.compile(firstRegex);
		final Matcher firstMatcher = firstPattern.matcher(value);
		final boolean firstValidation = firstMatcher.matches();

		final Pattern secondPattern = Pattern.compile(secondRegex);
		final Matcher secondMatcher = secondPattern.matcher(value);
		final boolean secondValidation = secondMatcher.matches();

		return firstValidation || secondValidation;
	}

	protected boolean isNegativeNumberAllowed(final String entryFieldMask)
	{
		if (entryFieldMask == null || entryFieldMask.isEmpty())
		{
			return true;
		}
		return entryFieldMask.charAt(0) == '-';
	}

	protected void checkLength(final BigDecimal numberToCheck, final String value, final CsticData cstic, final Errors errorObj,
			final Locale locale)
	{
		final int maxFractions = cstic.getNumberScale();
		final int maxDecimals = cstic.getTypeLength() - maxFractions;
		final String expectedFormat = createExpectedFormatAsString(maxDecimals, maxFractions, locale);


		final int actualFraction = numberToCheck.scale();
		if (actualFraction > maxFractions)
		{
			final Object[] args =
			{ value, Integer.valueOf(actualFraction), Integer.valueOf(maxFractions), expectedFormat };
			createFieldError(cstic, value, errorObj, args, TOO_MANY_FRACTIONS, MSG_KEY_TOO_LONG_FRACTION);
		}

		final int numberOfDecimals = countDecimals(numberToCheck);
		if (numberOfDecimals > maxDecimals)
		{
			final Object[] args =
			{ value, Integer.valueOf(numberOfDecimals), Integer.valueOf(maxDecimals), expectedFormat };
			createFieldError(cstic, value, errorObj, args, TOO_MANY_DIGITS, MSG_KEY_TOO_MANY_DIGITS);
		}

	}

	protected void createFieldError(final CsticData cstic, final String errorValue, final Errors errorObj, final Object[] args,
			final String defaultMsg, final String msgKey)
	{
		final String source;
		if (errorValue.equals(cstic.getFormattedValue()))
		{
			source = SOURCE_FIELD_VALUE;
		}
		else
		{
			source = SOURCE_FIELD_ADDITIONAL_VALUE;
		}
		errorObj.rejectValue(source, msgKey, args, defaultMsg);
		cstic.setCsticStatus(CsticStatusType.ERROR);
	}

	protected String createExpectedFormatAsString(final int maxIntegerPlaces, final int maxDecimalPlaces, final Locale locale)
	{
		final DecimalFormat numberFormatter = (DecimalFormat) NumberFormat.getInstance(locale);
		final DecimalFormatSymbols symbols = numberFormatter.getDecimalFormatSymbols();

		final char groupingSeparator = symbols.getGroupingSeparator();
		final char decimalSeparator = symbols.getDecimalSeparator();

		final StringBuilder builder = createTemplateForDecimals(maxIntegerPlaces, groupingSeparator);

		final String decimalTemplate = createTemplateForFractions(maxDecimalPlaces, decimalSeparator);
		builder.append(decimalTemplate);

		return builder.toString();
	}

	protected String createTemplateForFractions(final int maxDecimalPlaces, final char decimalSeparator)
	{
		final StringBuilder builder = new StringBuilder();
		if (maxDecimalPlaces > 0)
		{
			builder.append(decimalSeparator);
		}
		for (int ii = 0; ii < maxDecimalPlaces; ++ii)
		{
			builder.append(PATTERN_CHAR);
		}

		return builder.toString();
	}

	protected StringBuilder createTemplateForDecimals(final int maxIntegerPlaces, final char groupingSeparator)
	{
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < maxIntegerPlaces; i++)
		{
			if ((i % 3) == 0 && i > 0)
			{
				builder.append(groupingSeparator);
			}

			builder.append(PATTERN_CHAR);
		}

		builder = builder.reverse();
		return builder;
	}

	protected int countDecimals(final BigDecimal numberToCheck)
	{
		String plainString = numberToCheck.toPlainString();
		plainString = plainString.replaceFirst("-", "");


		int pos = plainString.indexOf(DEFAULT_DECIMAL_SEPARATOR);
		final boolean separatorNotFound = pos == -1;
		if (separatorNotFound)
		{
			pos = plainString.length();
		}

		return pos;
	}

	/**
	 * @return the i18NService
	 */
	public I18NService getI18NService()
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
