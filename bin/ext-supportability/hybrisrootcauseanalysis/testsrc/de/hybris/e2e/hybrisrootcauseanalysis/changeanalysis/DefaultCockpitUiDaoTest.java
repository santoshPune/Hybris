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
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertTrue;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.dao.CockpitUiDao;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.dao.impl.DefaultCockpitUiDao;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class DefaultCockpitUiDaoTest extends HybrisJUnit4TransactionalTest
{
	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultCockpitUiDaoTest.class.getName());

	private CockpitUiDao defaultCockpitUiDao;

	int size = 351;

	@Before
	public void setUp()
	{
		final DefaultCockpitUiDao bean = Registry.getApplicationContext().getBean(DefaultCockpitUiDao.class);
		setDefaultCockpitUiDao(bean);
	}

	/**
	 * @param defaultCockpitUiDao
	 *           the defaultCockpitUiDao to set
	 */
	public void setDefaultCockpitUiDao(final CockpitUiDao defaultCockpitUiDao)
	{
		this.defaultCockpitUiDao = defaultCockpitUiDao;
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Ignore
	@Test
	public void testFindAllCockpitConfigNotEmpty()
	{
		final List<CockpitUIComponentConfigurationModel> cockpitsConfiguration = defaultCockpitUiDao.findAllCockpitsConfiguration();
		assertTrue("list not empty", !cockpitsConfiguration.isEmpty());
	}

	@Test
	public void testFindAllCockpitConfigNotNull()
	{
		final List<CockpitUIComponentConfigurationModel> cockpitsConfiguration = defaultCockpitUiDao.findAllCockpitsConfiguration();
		assertTrue("list not null", cockpitsConfiguration != null);
	}

	@Ignore
	@Test(expected = NullPointerException.class)
	public void testFindAllCockpitConfigNullPointer()
	{
		//comment the code in setUp the test should pass
		defaultCockpitUiDao.findAllCockpitsConfiguration();
	}

	@Ignore
	@Test
	public void testFindAllCockpitConfigSize()
	{
		final List<CockpitUIComponentConfigurationModel> cockpitsConfiguration = defaultCockpitUiDao.findAllCockpitsConfiguration();
		assertTrue("size = 351 ", cockpitsConfiguration.size() == size);
	}
}
