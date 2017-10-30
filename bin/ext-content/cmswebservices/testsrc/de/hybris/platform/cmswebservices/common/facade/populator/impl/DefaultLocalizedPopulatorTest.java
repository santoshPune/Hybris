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
 */
package de.hybris.platform.cmswebservices.common.facade.populator.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.languages.facade.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLocalizedPopulatorTest
{

	@Mock
	private LanguageFacade languageFacade;

	@InjectMocks
	private DefaultLocalizedPopulator defaultLocalizedPopulator;
	
	@Before
	public void setup() 
	{
		LanguageData english = new LanguageData();
		english.setIsocode(Locale.ENGLISH.getLanguage());
		when(languageFacade.getLanguages()).thenReturn(Arrays.asList(english));
	}
	
	
	@Test
	public void testPopulator() {
		
		final BiConsumer<Locale, String> setter = mock(BiConsumer.class);
		final Function<Locale, String> getter = mock(Function.class);

		final String expectedValue = "value";
		when(getter.apply(Locale.ENGLISH)).thenReturn(expectedValue);
		
		defaultLocalizedPopulator.populate(setter, getter);
		
		verify(languageFacade).getLanguages();
		verify(getter).apply(Locale.ENGLISH);
		verify(setter).accept(Locale.ENGLISH, expectedValue);
	}
	
	
}
