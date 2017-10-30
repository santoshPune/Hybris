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
package de.hybris.platform.cmswebservices.items.facade.populator.data;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.common.facade.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmswebservices.data.SimpleBannerComponentData;
import de.hybris.platform.cmswebservices.languages.facade.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleBannerComponentDataPopulatorTest
{
	private static final String MEDIA_CODE_EN = "mediaCodeEN";
	private static final String MEDIA_CODE_FR = "mediaCodeFR";
	private static final String URL_LINK = "http://something-to-test.com";

	@InjectMocks
	private final SimpleBannerComponentDataPopulator populator = new SimpleBannerComponentDataPopulator();

	@InjectMocks
	private final DefaultLocalizedPopulator localizedPopulator = new DefaultLocalizedPopulator();

	@Mock
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private LanguageFacade languageFacade;

	@Mock
	private MediaModel mediaModelEN;
	@Mock
	private MediaModel mediaModelFR;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private SimpleBannerComponentModel bannerModel;

	private SimpleBannerComponentData bannerDto;

	@Before
	public void setUp()
	{
		populator.setLocalizedPopulator(localizedPopulator);

		bannerDto = new SimpleBannerComponentData();
		bannerDto.setExternal(Boolean.TRUE);
		bannerDto.setUrlLink(URL_LINK);
		final Map<String, String> media = new HashMap<>();
		media.put("en", MEDIA_CODE_EN);
		media.put("fr", MEDIA_CODE_FR);
		bannerDto.setMedia(media);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("en");
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode("fr");

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_EN)).thenReturn(mediaModelEN);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_FR)).thenReturn(mediaModelFR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(bannerDto, bannerModel);

		verify(bannerModel).setExternal(Boolean.TRUE);
		verify(bannerModel).setUrlLink(URL_LINK);
		verify(bannerModel).setMedia(mediaModelEN, Locale.ENGLISH);
		verify(bannerModel).setMedia(mediaModelFR, Locale.FRENCH);
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(bannerDto, bannerModel);

		verify(bannerModel).setMedia(mediaModelEN, Locale.ENGLISH);
		verify(bannerModel).setMedia(mediaModelFR, Locale.FRENCH);
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		final Map<String, String> media = new HashMap<>();
		media.put("en", MEDIA_CODE_EN);
		bannerDto.setMedia(media);

		populator.populate(bannerDto, bannerModel);

		verify(bannerModel).setMedia(mediaModelEN, Locale.ENGLISH);
		verify(bannerModel, times(0)).setMedia(mediaModelFR, Locale.FRENCH);
	}

	@Test
	public void shouldNotPopulateMedia_MediaIsNull()
	{
		bannerDto.setMedia(null);
		populator.populate(bannerDto, bannerModel);

		verify(bannerModel, times(0)).setMedia(mediaModelEN, Locale.ENGLISH);
		verify(bannerModel, times(0)).setMedia(mediaModelFR, Locale.FRENCH);
	}

	@Test
	public void shouldDefaultExternalValue()
	{
		bannerDto.setExternal(null);
		populator.populate(bannerDto, bannerModel);
		verify(bannerModel).setExternal(Boolean.FALSE);
	}

	@Test(expected = ConversionException.class)
	public void shouldFailConversion_MediaNotFound()
	{
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE_EN)).thenThrow(new ConversionException("Test"));
		populator.populate(bannerDto, bannerModel);
		fail();
	}
}
