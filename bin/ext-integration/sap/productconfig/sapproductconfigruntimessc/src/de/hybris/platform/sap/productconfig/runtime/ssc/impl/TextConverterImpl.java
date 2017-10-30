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

import de.hybris.platform.sap.productconfig.runtime.ssc.TextConverter;
import de.hybris.platform.sap.productconfig.runtime.ssc.constants.SapproductconfigruntimesscConstants;

import java.util.regex.Pattern;

import com.sap.custdev.projects.fbs.slc.cfg.client.ITextDescription;


public class TextConverterImpl implements TextConverter
{
	/**
	 *
	 */
	private static final char DOUBLE_QUOTE = '\"';

	/**
	 *
	 */
	private static final char SINGLE_QUOTE = '\'';

	private static final String EMPTY_STRING = "";

	public final static String SECTION_DESCRIPTION = "&DESCRIPTION&";
	public final static String SECTION_EXPLAINATION = "&EXPLANATION&";

	private final Pattern pFormat = Pattern.compile("\\<(?:\\/\\w*|\\w+)\\>");
	private final Pattern pMarkup = Pattern.compile("\\<.*?\\>");
	private final Pattern pEscapeSeq = Pattern.compile("\\<(:?\\)|\\()\\>");


	@Override
	public String convertLongText(final String formattedText)
	{
		String result = formattedText;
		if (!result.isEmpty())
		{
			result = deEscapeString(result);
			result = extractSection(result);
			result = removeFormatting(result);
			result = removeMarkup(result);
			result = replaceDoubleQuotes(result);
		}
		return result;
	}

	protected String removeFormatting(final String formattedText)
	{
		final String unformattedText = pFormat.matcher(formattedText).replaceAll(EMPTY_STRING);
		return unformattedText;
	}

	protected String removeMarkup(final String markup)
	{
		final String withoutmMarkup = pMarkup.matcher(markup).replaceAll(EMPTY_STRING);
		return withoutmMarkup;
	}

	protected String extractSection(final String textWithSections)
	{

		int startIdx = textWithSections.indexOf(SECTION_DESCRIPTION);
		String sectionText;
		if (startIdx != -1)
		{
			startIdx += SECTION_DESCRIPTION.length();
			int endIdx = textWithSections.indexOf(SECTION_EXPLAINATION, startIdx);
			if (endIdx == -1)
			{
				endIdx = textWithSections.length();
			}
			sectionText = textWithSections.substring(startIdx, endIdx);
		}
		else
		{
			sectionText = textWithSections;
		}

		return sectionText;
	}

	protected String deEscapeString(final String escapedString)
	{
		final String descapedString = pEscapeSeq.matcher(escapedString).replaceAll(EMPTY_STRING);
		return descapedString;
	}

	protected String replaceDoubleQuotes(final String singleQuotes)
	{
		final String doubleQuotes = singleQuotes.replace(DOUBLE_QUOTE, SINGLE_QUOTE);
		return doubleQuotes;
	}

	@Override
	public String convertDependencyText(final ITextDescription[] textDescriptionArray)
	{
		String text = null;
		final StringBuilder textBuffer = new StringBuilder();


		if (textDescriptionArray != null && textDescriptionArray.length > 0)
		{
			for (final ITextDescription textDescriptionLine : textDescriptionArray)
			{
				final String textLine = textDescriptionLine.getTextLine();
				textBuffer.append(textLine);
			}
		}


		if (textBuffer.length() > 0)
		{
			text = getExplanationForDependency(textBuffer.toString());
			if (!text.isEmpty())
			{
				text = deEscapeString(text);
				text = removeFormatting(text);
				text = removeMarkup(text);
				text = replaceDoubleQuotes(text);
			}
			return text;

		}
		return text;
	}

	protected String getExplanationForDependency(final String text)
	{

		String explanation = null;

		if (text == null || text.isEmpty())
		{
			explanation = "";
		}
		else
		{
			int start = 0;
			if (text.indexOf(SapproductconfigruntimesscConstants.EXPLANATION) > -1)
			{
				start = text.indexOf(SapproductconfigruntimesscConstants.EXPLANATION)
						+ SapproductconfigruntimesscConstants.EXPLANATION.length();
			}
			int end = text.length();
			if (text.indexOf(SapproductconfigruntimesscConstants.DOCUMENTATION) > -1)
			{
				end = text.indexOf(SapproductconfigruntimesscConstants.DOCUMENTATION);
			}
			explanation = text.substring(start, end);
		}

		return explanation;
	}

}
