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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


/**
 * Tests
 */
@UnitTest
public class ConfigModelImplTest
{
	ConfigModelImpl classUnderTest = new ConfigModelImpl();

	@Test
	public void testToStringSolvableConflicts()
	{
		final List<SolvableConflictModel> solvableConflicts = new ArrayList<SolvableConflictModel>();
		final SolvableConflictModel solvableConflict = new SolvableConflictModelImpl();
		final String description = "This is a description";
		solvableConflict.setDescription(description);
		solvableConflicts.add(solvableConflict);
		classUnderTest.setSolvableConflicts(solvableConflicts);
		assertTrue("We expect the description of the conflict to appear in toString", classUnderTest.toString()
				.indexOf(description) > -1);
	}
}
