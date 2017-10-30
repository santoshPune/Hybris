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
package de.hybris.platform.cmswebservices.languages.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLanguageFacadeTest
{
	private static final String ENGLISH = "EN";
	private static final String GERMAN = "DE";

	@InjectMocks
	private DefaultLanguageFacade languageFacade;

	@Mock
	private StoreSessionFacade storeSessionFacade;

	private LanguageData languageEN;
	private LanguageData languageDE;
	private List<LanguageData> languages;

	@Before
	public void setUp()
	{
		languageEN = new LanguageData();
		languageEN.setIsocode("EN");
		languageDE = new LanguageData();
		languageDE.setIsocode("DE");
		languages = Arrays.asList(languageEN, languageDE);

		when(storeSessionFacade.getAllLanguages()).thenReturn(languages);
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);
	}

	@Test
	public void getLanguagesDefaultEnglish()
	{
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);
		final List<LanguageData> languagesFound = languageFacade.getLanguages();

		assertEquals(ENGLISH, languagesFound.get(0).getIsocode());
		assertTrue(languagesFound.get(0).isRequired());
		assertEquals(GERMAN, languagesFound.get(1).getIsocode());
		assertFalse(languagesFound.get(1).isRequired());
	}

	@Test
	public void getLanguagesDefaultGerman()
	{
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageDE);
		final List<LanguageData> languagesFound = languageFacade.getLanguages();

		assertEquals(GERMAN, languagesFound.get(0).getIsocode());
		assertTrue(languagesFound.get(0).isRequired());
		assertEquals(ENGLISH, languagesFound.get(1).getIsocode());
		assertFalse(languagesFound.get(1).isRequired());
	}
}
