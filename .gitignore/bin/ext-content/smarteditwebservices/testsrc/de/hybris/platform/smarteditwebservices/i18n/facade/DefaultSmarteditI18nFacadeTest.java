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
package de.hybris.platform.smarteditwebservices.i18n.facade;

import com.google.common.collect.Maps;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.impl.AbstractConverter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.smarteditwebservices.configuration.facade.SmarteditConfigurationFacade;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;
import de.hybris.platform.smarteditwebservices.i18n.facade.impl.DefaultSmarteditI18nFacade;
import de.hybris.platform.smarteditwebservices.i18n.facade.populator.SmarteditLanguagePopulator;
import jersey.repackaged.com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.Map.Entry;

import static de.hybris.platform.smarteditwebservices.configuration.facade.DefaultConfigurationKey.DEFAULT_LANGUAGE;
import static java.util.Collections.emptyEnumeration;
import static java.util.Locale.*;
import static java.util.Optional.of;
import static jersey.repackaged.com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DefaultSmarteditI18nFacadeTest
{
	private static final String L10N_KEY = "smarteditwebservices.key.dummy";
	private static final String L10N_VALUE = "value.dummy";

	private static final Entry<String, String> EXPECTED_ENTRY = Maps.immutableEntry(L10N_KEY, L10N_VALUE);

	private static final ConfigurationData ENGLISH_CONFIGURATION = new ConfigurationData();
	{
		ENGLISH_CONFIGURATION.setKey(DEFAULT_LANGUAGE.getKey());
		ENGLISH_CONFIGURATION.setValue(ENGLISH.getLanguage());
	}

	private static final SmarteditLanguagePopulator SMARTEDIT_LANGUAGE_POPULATOR = new SmarteditLanguagePopulator();
	private static final SmarteditLanguageData ENGLISH_DATA = new ComparableSmarteditLanguageData();
	private static final SmarteditLanguageData GERMAN_DATA = new ComparableSmarteditLanguageData();
	private static final SmarteditLanguageData FRENCH_DATA = new ComparableSmarteditLanguageData();
	static {
		SMARTEDIT_LANGUAGE_POPULATOR.populate(ENGLISH, ENGLISH_DATA);
		SMARTEDIT_LANGUAGE_POPULATOR.populate(GERMAN, GERMAN_DATA);
		SMARTEDIT_LANGUAGE_POPULATOR.populate(FRENCH, FRENCH_DATA);
	}


	@InjectMocks
	private DefaultSmarteditI18nFacade smarteditI18nFacade;

	@Mock
	private L10NService l10nService;
	@Mock
	private I18NService i18nService;
	@Mock
	private AbstractConverter<Locale, SmarteditLanguageData> smarteditLanguageConverter;
	@Mock
	private SmarteditConfigurationFacade smarteditConfigurationFacade;

	@Before
	public void setUp() throws Exception
	{

		when(i18nService.getAllLocales(ENGLISH)).thenReturn(new Locale[]
		{ ENGLISH });
		when(smarteditConfigurationFacade.tryAndFindByDefaultConfigurationKey(DEFAULT_LANGUAGE)).thenReturn(of(ENGLISH_CONFIGURATION));

		when(i18nService.getSupportedLocales()).thenReturn(newHashSet(ENGLISH));
		when(l10nService.getResourceBundle(i18nService.getAllLocales(ENGLISH))).thenReturn(
				createResourceBundle(L10N_KEY, L10N_VALUE));
	}

	@Test
	public void shouldGetTranslations_LocaleIsPresent()
	{
		when(i18nService.getSupportedLocales()).thenReturn(newHashSet(ENGLISH));
		when(l10nService.getResourceBundle(i18nService.getAllLocales(ENGLISH))).thenReturn(
				createResourceBundle(L10N_KEY, L10N_VALUE));
		final Map<String, String> translations = smarteditI18nFacade.getTranslationMap(ENGLISH);
		assertThat(L10N_VALUE, is(translations.get(L10N_KEY)));
	}

	@Test
	public void will_get_default_translation_when_locale_is_not_supported()
	{
		when(l10nService.getResourceBundle(i18nService.getAllLocales(FRENCH))).thenReturn(createResourceBundle(null, null));
		Map<String, String> translationMap = smarteditI18nFacade.getTranslationMap(FRENCH);
		assertThat(translationMap, hasEntry(L10N_KEY, L10N_VALUE));
	}

	@Test
	public void getSupportedLanguages_will_return_all_supported_languages()
	{
		setupMocksForGetSupportedLangguages();

		List<SmarteditLanguageData> supportedLanguages = smarteditI18nFacade.getSupportedLanguages();

		assertThat(supportedLanguages, hasItems(GERMAN_DATA, ENGLISH_DATA, FRENCH_DATA));
	}

	@Test
	public void getSupportedLanguages_will_return_all_supported_languages_starting_with_german()
	{
		setupMocksForGetSupportedLangguages();

		List<SmarteditLanguageData> supportedLanguages = smarteditI18nFacade.getSupportedLanguages();

		assertThat(supportedLanguages.get(0), is(GERMAN_DATA));
	}

	@Test
	public void getSupportedLanguages_will_return_all_supported_languages_starting_with_french_when_reversed()
	{
		setupMocksForGetSupportedLangguages();

		smarteditI18nFacade.setSmarteditLanguageDataComparator(smarteditI18nFacade.getSmarteditLanguageDataComparator().reversed());
		List<SmarteditLanguageData> supportedLanguages = smarteditI18nFacade.getSupportedLanguages();

		assertThat(supportedLanguages.get(0), is(FRENCH_DATA));
	}

	private void setupMocksForGetSupportedLangguages() {
		when(i18nService.getSupportedLocales()).thenReturn(newHashSet(ENGLISH, GERMAN, FRENCH));
		when(smarteditLanguageConverter.convert(ENGLISH)).thenReturn(ENGLISH_DATA);
		when(smarteditLanguageConverter.convert(GERMAN)).thenReturn(GERMAN_DATA);
		when(smarteditLanguageConverter.convert(FRENCH)).thenReturn(FRENCH_DATA);
	}

	@Test
	public void shouldNotGetSupportedLanguages()
	{
		when(i18nService.getSupportedLocales()).thenReturn(null);
		assertThat(smarteditI18nFacade.getSupportedLanguages().size(), is(0));
		verify(smarteditLanguageConverter, times(0)).convert(Mockito.any());
	}

	@Test
	public void getTranslationMap_will_return_default_keys_for_languages_without_keys() {
		when(i18nService.getSupportedLocales()).thenReturn(newHashSet(ENGLISH, GERMAN));
		when(l10nService.getResourceBundle(i18nService.getAllLocales(GERMAN))).thenReturn(
				new ResourceBundle() {
					@Override
					protected Object handleGetObject(String key) {
						return null;
					}

					@Override
					public Enumeration<String> getKeys() {
						return emptyEnumeration();
					}
				});


		Map<String, String> translationMap = smarteditI18nFacade.getTranslationMap(GERMAN);
		assertThat(translationMap.entrySet(), Matchers.contains(EXPECTED_ENTRY));
	}

	private ResourceBundle createResourceBundle(final String expectedKey, final String expectedValue)
	{
		final ResourceBundle rBundle = new ResourceBundle()
		{
			@Override
			protected Object handleGetObject(final String key)
			{
				return expectedValue;
			}

			@Override
			public Enumeration<String> getKeys()
			{
				return Collections.enumeration(Lists.newArrayList(expectedKey));
			}
		};
		return rBundle;
	}

	private static class ComparableSmarteditLanguageData extends SmarteditLanguageData {

		@Override
		public int hashCode() {
			return reflectionHashCode(this);
		}

		@Override
		public boolean equals(Object that) {
			return reflectionEquals(this, that);
		}

		@Override
		public String toString() {
			return reflectionToString(this, MULTI_LINE_STYLE);
		}
	}
}
