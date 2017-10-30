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
package de.hybris.platform.cmswebservices.items.facade.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;

import java.util.UUID;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BaseComponentValidatorTest
{

	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";
	private static final String UID = "uid";
	private static final String PARAGRAPH_COMPONENT = "ParagraphComponent";

	@InjectMocks
	private final BaseComponentValidator validator = new BaseComponentValidator();

	@Mock
	private Predicate<String> validStringLengthPredicate;

	@Before
	public void setUp()
	{
		when(validStringLengthPredicate.test(PARAGRAPH_COMPONENT)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldValidateComponent_All_Fields_are_valid()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateComponent_Invalid_Uid()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setUid(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(UID, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_Name()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setName(null);

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_Name_Length()
	{
		when(validStringLengthPredicate.test(PARAGRAPH_COMPONENT)).thenReturn(Boolean.FALSE);
		final AbstractCMSComponentData data = createComponentData();

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_TypeCode()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setTypeCode(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(TYPE_CODE, errors.getFieldError().getField());
	}

	private AbstractCMSComponentData createComponentData()
	{
		final CMSParagraphComponentData data = new CMSParagraphComponentData();
		data.setName(PARAGRAPH_COMPONENT);
		data.setTypeCode(SimpleBannerComponentModel._TYPECODE);
		data.setUid(UUID.randomUUID().toString());
		return data;
	}
}
