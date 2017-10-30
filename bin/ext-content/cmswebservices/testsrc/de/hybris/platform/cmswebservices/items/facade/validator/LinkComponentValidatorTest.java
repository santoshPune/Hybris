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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.common.facade.validator.LocalizedTypeValidator;
import de.hybris.platform.cmswebservices.common.facade.validator.LocalizedValidator;
import de.hybris.platform.cmswebservices.data.CMSLinkComponentData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentValidatorTest
{

	@Mock
	private LocalizedValidator localizedValidator;
	@Mock
	private LocalizedTypeValidator localizedStringValidator;


	@InjectMocks
	private LinkComponentValidator validator;

	@Before
	public void setUp()
	{
		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");
	}


	@Test
	public void shouldValidateLink_ValidLocalized()
	{

		doNothing().when(localizedValidator).validateRequiredLanguages(any(), any(), any());
		doNothing().when(localizedStringValidator).validate(any(), any(), any(), any());

		final CMSLinkComponentData data = new CMSLinkComponentData();
		final Map<String, String> names = new HashMap();
		names.put("en", "Contact Us");
		names.put("fr", "Contactez Nous");
		data.setLinkName(names);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertTrue(errors.getAllErrors().isEmpty());
	}


	@Test
	public void shouldValidateLink_InvalidLocalizedNull()
	{

		final Map<String, String> names = new HashMap();

		final CMSLinkComponentData data = new CMSLinkComponentData();
		data.setLinkName(names);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		doAnswer((Answer<Void>) invocationOnMock -> {
			errors.rejectValue("linkName", "missing required field", new Object[] { Locale.ENGLISH}, null);
			return null;
		}).when(localizedValidator).validateRequiredLanguages(any(), any(), any());

		validator.validate(data, errors);

		// only english is required
		assertEquals(1, errors.getAllErrors().size());
	}
}
