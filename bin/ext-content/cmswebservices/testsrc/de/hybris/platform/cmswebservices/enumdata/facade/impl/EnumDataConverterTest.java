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
package de.hybris.platform.cmswebservices.enumdata.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.cmswebservices.data.EnumData;

import org.junit.Before;
import org.junit.Test;

/**
 * EnumDataConverterTest is a suite of test cases for testing
 * the mapping of a <code>HybrisEnumValue</code> type to a
 * <code>EnumData</code> serializable type used by cmswebservices.
 *
 */
public class EnumDataConverterTest {
	//Code and label are the same for time being.
	static final String ENUM_CODE = "TEST_ENUM1";
	static final String ENUM_LABEL = "TEST_ENUM1";

	EnumDataConverter converter;

	@Before
	public void setUp() {
		converter = new EnumDataConverter();
	}

	@Test
	public void testMapping() {
		TestEnumModel enum1 = TestEnumModel.TEST_ENUM1;
		EnumData ed = converter.convert(enum1);
		assertNotNull("Expected non-null converted enum data representation", ed);
		assertEquals("Expected code for EnumData to be " + ENUM_CODE + " but was" + ed.getCode(), ENUM_CODE, ed.getCode());
		assertEquals("Expected label for EnumData to be " + ENUM_CODE + " but was" + ed.getLabel(), ENUM_CODE, ed.getLabel());
	}
}
