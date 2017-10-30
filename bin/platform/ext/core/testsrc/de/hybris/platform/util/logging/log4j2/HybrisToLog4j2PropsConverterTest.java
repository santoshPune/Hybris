/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.util.logging.log4j2;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Properties;

import org.junit.Test;


public class HybrisToLog4j2PropsConverterTest
{


	private static final String LOG4J2 = "log4j2";
	private static final String APPENDER_CONSOLE_LAYOUT_PATTERN = "appender.console.layout.pattern";
	private static final String LOG4J2_APPENDER_CONSOLE_LAYOUT_PATTERN = LOG4J2 + "." + APPENDER_CONSOLE_LAYOUT_PATTERN;

	private final HybrisToLog4j2PropsConverter converter = new HybrisToLog4j2PropsConverter()
	{
		@Override
		public boolean isANSIDisable(final Properties platformProperties)
		{
			return true;
		}
	};


	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesWholeText()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]}",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesBeginingOfText()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}}%X{Tenant}%X{CronJob}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWhenItCapturesMiddleOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %highlight{%X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWithStyleWhenItCapturesMiddleOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %highlight{%X{RemoteAddr}%X{Tenant}%X{CronJob}}{STYLE=Logback}[%c{1}]",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldRemoveHighlightSyntaxWithStyleWhenItCapturesEndOfText()
	{
		convertAndAssertIfEqualTo("%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}%highlight{[%c{1}]}{STYLE=Logback}",
				"%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}[%c{1}]");
	}

	@Test
	public void shouldDoNothingWhenErrorInSyntax()
	{
		convertAndAssertIfEqualTo("%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}{[%c{1}]",
				"%highlight{%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}{[%c{1}]");
	}

	@Test
	public void shouldDoNothingWhenErrorInSyntax2()
	{
		convertAndAssertIfEqualTo("%highlight%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]",
				"%highlight%-5p [%t] %X{RemoteAddr}%X{Tenant}%X{CronJob}}[%c{1}]");
	}




	private void convertAndAssertIfEqualTo(final String convertFrom, final String compareTo)
	{
		final Properties props = new Properties();

		props.put(LOG4J2_APPENDER_CONSOLE_LAYOUT_PATTERN, convertFrom);

		final Properties convertToLog4jProps = converter.convertToLog4jProps(props);

		assertThat(convertToLog4jProps.get(APPENDER_CONSOLE_LAYOUT_PATTERN)).isEqualTo(compareTo);



	}



}
