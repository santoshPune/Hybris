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
package de.hybris.platform.hac.facade.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.hac.facade.Log4JFacade;
import de.hybris.platform.hac.facade.LoggerConfigData;

import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;


@UnitTest
public class DefaultLog4JFacadeTest
{
	private Log4JFacade log4JFacade = new DefaultLog4JFacade();

	@Test
	public void shouldReturnAtLeastRootLoggerData()
	{
		final List<LoggerConfigData> loggers = log4JFacade.getLoggers();

		assertThat(loggers).isNotEmpty();

		final LoggerConfigData rootLoggerData = loggers.stream().filter(i -> i.getName().equals("root")).findFirst().get();
		assertThat(rootLoggerData.getEffectiveLevel()).isEqualTo(LogManager.getRootLogger().getLevel());
	}

	@Test
	public void testChangeLoggerOnRuntime()
	{
		final Level defaultLevel = LogManager.getRootLogger().getLevel();
		final Level wantedLevel = defaultLevel.equals(Level.WARN) ? Level.INFO : Level.WARN;

		try
		{
			log4JFacade.changeLogLevel("root", wantedLevel.name());
			assertThat(LogManager.getRootLogger().getLevel()).isEqualTo(wantedLevel);
		}
		finally
		{
			log4JFacade.changeLogLevel("root", wantedLevel.name());
		}
	}

}
