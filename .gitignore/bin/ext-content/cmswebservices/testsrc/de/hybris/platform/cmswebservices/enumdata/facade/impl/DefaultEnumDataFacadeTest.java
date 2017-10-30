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
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.EnumData;
import de.hybris.platform.cmswebservices.exception.InvalidNamedQueryException;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultEnumDataFacadeTest {
	private static final String TEST_ENUM1_CODE = "TEST_ENUM1";
	private static final TestEnumModel TEST_ENUM1 = TestEnumModel.TEST_ENUM1;
	private static final EnumData TEST_ED_1 = new EnumData();

	private static final String TEST_ENUM2_CODE = "TEST_ENUM2";
	private static final TestEnumModel TEST_ENUM2 = TestEnumModel.TEST_ENUM2;
	private static final EnumData TEST_ED_2 = new EnumData();

	private static final Class<? extends HybrisEnumValue> CLASS_TO_RETRIEVE = TestEnumModel.class;

	private EnumerationService mockEnumService;
	private EnumDataConverter mockEnumDataConverter;

	private DefaultEnumDataFacade enumDataFacade;

	@Before
	public void setUp() throws InvalidNamedQueryException {
		enumDataFacade = new DefaultEnumDataFacade();

		mockEnumService = Mockito.mock(EnumerationService.class);
		List<TestEnumModel> enumValues = new ArrayList<TestEnumModel>();
		enumValues.add(TEST_ENUM1);
		enumValues.add(TEST_ENUM2);
		Mockito.<List<? extends HybrisEnumValue>>when(mockEnumService.getEnumerationValues(CLASS_TO_RETRIEVE)).thenReturn(enumValues);

		mockEnumDataConverter = Mockito.mock(EnumDataConverter.class);
		TEST_ED_1.setCode(TEST_ENUM1_CODE);
		TEST_ED_1.setLabel(TEST_ENUM1_CODE);
		when(mockEnumDataConverter.convert(TEST_ENUM1)).thenReturn(TEST_ED_1);

		TEST_ED_2.setCode(TEST_ENUM2_CODE);
		TEST_ED_2.setLabel(TEST_ENUM2_CODE);
		when(mockEnumDataConverter.convert(TEST_ENUM2)).thenReturn(TEST_ED_2);

		enumDataFacade.setEnumerationService(mockEnumService);
		enumDataFacade.setEnumDataConverter(mockEnumDataConverter);
	}

	@Test
	public void shouldGetEnum()
	{
		final List<EnumData> returnedEnums = enumDataFacade.getEnumValues(CLASS_TO_RETRIEVE.getCanonicalName());
		assertNotNull("Expected values to be returned for enum class but was null.", returnedEnums);
		assertNotNull("Expected wrapper class to have list of EnumData objects but was null", returnedEnums);
		assertEquals("Expected 2 returned, but was not 2.", 2, returnedEnums.size());

		final EnumData ed1 = returnedEnums.get(0);
		assertNotNull("Expected first element in list to be non-null", ed1);
		assertEquals("Expected code to be:" + TEST_ENUM1_CODE, TEST_ENUM1_CODE, ed1.getCode());
		assertEquals("Expected label to be:" + TEST_ENUM1_CODE, TEST_ENUM1_CODE, ed1.getLabel());

		final EnumData ed2 = returnedEnums.get(1);
		assertNotNull("Expected first element in list to be non-null", ed2);
		assertEquals("Expected code to be:" + TEST_ENUM2_CODE, TEST_ENUM2_CODE, ed2.getCode());
		assertEquals("Expected label to be:" + TEST_ENUM2_CODE, TEST_ENUM2_CODE, ed2.getLabel());
	}

	@Test (expected = IllegalArgumentException.class)
	public void shouldNotGetEnum() {
		@SuppressWarnings("unused")
		final List<EnumData> returnedEnums = enumDataFacade.getEnumValues("com.hybris.packagethatdoesnotexist.ClassThatDoesNotExist");
	}
}
