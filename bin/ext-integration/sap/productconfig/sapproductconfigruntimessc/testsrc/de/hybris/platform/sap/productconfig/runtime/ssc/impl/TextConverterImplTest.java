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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.ssc.constants.SapproductconfigruntimesscConstants;

import junit.framework.TestCase;

import org.junit.Test;

import com.sap.custdev.projects.fbs.slc.cfg.client.ITextDescription;




@UnitTest
public class TextConverterImplTest extends TestCase
{
	private static String textWoTags = "Huhu";
	private static String textAdditional = "Hello";
	private static String textWExplanation = textWoTags + SapproductconfigruntimesscConstants.EXPLANATION + textAdditional;
	private static String textWDocumentation = textWoTags + SapproductconfigruntimesscConstants.DOCUMENTATION + textAdditional;
	private final ITextDescription textDescription = new ITextDescription()
	{

		@Override
		public void setTextLineId(final Integer arg0)
		{//
		}

		@Override
		public void setTextLine(final String arg0)
		{
			//
		}

		@Override
		public void setTextFormat(final String arg0)
		{
			//
		}

		@Override
		public Integer getTextLineId()
		{
			return Integer.valueOf(7);
		}

		@Override
		public String getTextLine()
		{
			return textWExplanation;
		}

		@Override
		public String getTextFormat()
		{
			return "X";
		}
	};

	public TextConverterImpl classUnderTest = new TextConverterImpl();

	@Test
	public void testRemoveFormatting_unformatted()
	{

		final String input = "hello world. <> This string has no valid formats < >";
		final String output = classUnderTest.removeFormatting(input);
		assertEquals(input, output);
	}

	@Test
	public void testRemoveFormatting_formatted()
	{

		final String input = "<b>hello world. This >< string has formats</>";
		final String expected = "hello world. This >< string has formats";
		final String output = classUnderTest.removeFormatting(input);
		assertEquals(expected, output);
	}

	@Test
	public void testDeescapeString_unEscaped()
	{

		final String input = "<b>hello world. This >< string has formats</>";
		final String output = classUnderTest.deEscapeString(input);
		assertEquals(input, output);
	}

	@Test
	public void testRemoveFormatting_escaped()
	{

		final String input = "<b>hello world. This <(>&<)> string has formats</>";
		final String expected = "<b>hello world. This & string has formats</>";
		final String output = classUnderTest.deEscapeString(input);
		assertEquals(expected, output);
	}


	@Test
	public void testExtractSection_noSection()
	{

		final String input = "<b>DESCRIPTION This <(>&<)> string has formats</>";
		final String output = classUnderTest.extractSection(input);
		assertEquals(input, output);
	}

	@Test
	public void testExtractSection_section()
	{

		final String input = "&EXPLANATION&\text\n\n&DESCRIPTION&\ndescription section";
		final String expected = "\ndescription section";
		final String output = classUnderTest.extractSection(input);

		assertEquals(expected, output);
	}

	@Test
	public void testConvertLongText()
	{

		final String input = "<(>&<)>DESCRIPTION&\n" + "<H>Text in bold</>, <U>text underlined.</> Lorem ipsum dolor sit amet,"
				+ "consectetur adipiscing elit. Nullam euismod tristique orci sed faucibus."
				+ "Curabitur id felis et sem ultricies posuere. Maecenas laoreet quis nibh"
				+ "vitae iaculis. Pellentesque eros lorem, hendrerit ut aliquet quis, porta"
				+ "eget augue. In gravida non metus nec porta. Curabitur ac mattis dui."
				+ "Donec auctor, magna non laoreet tempus, ante purus ultricies libero, vel"
				+ "laoreet ex nibh eget dolor. Curabitur quis molestie neque. Praesent"
				+ "tempor dui orci, et vehicula est dignissim ut. Nulla risus dolor,"
				+ "molestie in nisl sit amet, porta sagittis neque. Aliquam erat volutpat.\"&>\"" + "\n" + "<(>&<)>EXPLANATION&"
				+ "\n" + "Donec massa quam, luctus eget ultrices ut, euismod eu sapien. Duis"
				+ "maximus, urna ut suscipit vestibulum, nunc lectus egestas metus, pretium"
				+ "auctor quam lacus a libero. Donec neque risus, tincidunt eget luctus"
				+ "sed, maximus sed magna. Nulla porttitor orci in facilisis pretium. Nunc"
				+ "malesuada, nulla sit amet hendrerit vehicula, dolor arcu elementum leo";

		final String expected = "\nText in bold, text underlined. Lorem ipsum dolor sit amet,"
				+ "consectetur adipiscing elit. Nullam euismod tristique orci sed faucibus."
				+ "Curabitur id felis et sem ultricies posuere. Maecenas laoreet quis nibh"
				+ "vitae iaculis. Pellentesque eros lorem, hendrerit ut aliquet quis, porta"
				+ "eget augue. In gravida non metus nec porta. Curabitur ac mattis dui."
				+ "Donec auctor, magna non laoreet tempus, ante purus ultricies libero, vel"
				+ "laoreet ex nibh eget dolor. Curabitur quis molestie neque. Praesent"
				+ "tempor dui orci, et vehicula est dignissim ut. Nulla risus dolor,"
				+ "molestie in nisl sit amet, porta sagittis neque. Aliquam erat volutpat.'&>'" + "\n";
		final String output = classUnderTest.convertLongText(input);

		assertEquals(expected, output);
	}

	@Test
	public void testConvertMarkup()
	{
		final String input = "ABC<U:WWW.HYBRIS.DE>abc<U:HTTPS://WWW.SAP.COM>ABC</U>";
		final String expected = "ABCabcABC";

		final String output = classUnderTest.convertLongText(input);

		assertEquals(expected, output);
	}

	@Test
	public void testConvertLongTextEmpty()
	{
		final String EMPTY_TEXT = "";
		final String output = classUnderTest.convertLongText(EMPTY_TEXT);

		assertSame(EMPTY_TEXT, output);
	}

	@Test
	public void testReplaceDoubleQuotes()
	{

		final String input = "String with some \" double quotes \"\"";
		final String expected = "String with some ' double quotes ''";
		final String output = classUnderTest.replaceDoubleQuotes(input);
		assertEquals(expected, output);
	}

	@Test
	public void testGetExplanationNoExplanationText()
	{
		final String explanation = classUnderTest.getExplanationForDependency(textWoTags);
		assertEquals(textWoTags, explanation);

	}

	@Test
	public void testGetExplanationExplanationText()
	{
		final String explanation = classUnderTest.getExplanationForDependency(textWExplanation);
		assertEquals(textAdditional, explanation);
	}

	@Test
	public void testGetExplanationDocumentationText()
	{
		final String explanation = classUnderTest.getExplanationForDependency(textWDocumentation);
		assertEquals(textWoTags, explanation);
	}

	@Test
	public void testGetExplanationNull()
	{
		final String explanation = classUnderTest.getExplanationForDependency(null);
		assertEquals("", explanation);
	}

	@Test
	public void testConvert()
	{
		final ITextDescription[] textDescriptionArray =
		{ textDescription };
		final String convertedText = classUnderTest.convertDependencyText(textDescriptionArray);
		assertEquals(textAdditional, convertedText);
	}



}
