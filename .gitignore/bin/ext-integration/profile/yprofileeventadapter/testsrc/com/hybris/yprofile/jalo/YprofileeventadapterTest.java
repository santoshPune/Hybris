/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.yprofile.jalo;

import de.hybris.bootstrap.annotations.UnitTest;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * JUnit Tests for the Yprofileeventadapter extension
 */
@UnitTest
public class YprofileeventadapterTest
{
	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(YprofileeventadapterTest.class.getName());

	@Before
	public void setUp()
	{
		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void testYprofileeventadapter()
	{
		final boolean testTrue = true;
		assertTrue("true is not true", testTrue);
	}
}
