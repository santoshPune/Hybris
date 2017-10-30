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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.IConfigSession;


/**
 *
 */
@UnitTest
public class ConfigurationSessionContainerImplTest
{
	ConfigurationSessionContainerImpl classUnderTest = new ConfigurationSessionContainerImpl();
	private static final String qualifiedId = "A";

	@Mock
	private IConfigSession configSession;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testStore()
	{
		classUnderTest.storeConfiguration(qualifiedId, configSession);
		assertEquals(configSession, classUnderTest.retrieveConfigSession(qualifiedId));
	}

	@Test(expected = IllegalStateException.class)
	public void testRelease()
	{
		classUnderTest.storeConfiguration(qualifiedId, configSession);
		classUnderTest.releaseSession(qualifiedId);
		classUnderTest.retrieveConfigSession(qualifiedId);
	}

	@Test(expected = IllegalStateException.class)
	public void testRetrieveEmptyMap()
	{
		classUnderTest.retrieveConfigSession(qualifiedId);
	}

	@Test
	public void testStackTrace()
	{
		final StringBuilder topLinesOfStacktrace = classUnderTest.getTopLinesOfStacktrace(5);
		assertNotNull(topLinesOfStacktrace);
		assertTrue(topLinesOfStacktrace.toString().length() > 0);
		assertFalse("We don't want to see the first two entries", topLinesOfStacktrace.toString().contains("java.lang.Thread"));
		System.out.println(topLinesOfStacktrace);

	}

}
