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
package de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmswebservices.common.facade.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmswebservices.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;
import de.hybris.platform.cmswebservices.languages.facade.LanguageFacade;
import de.hybris.platform.cmswebservices.resolvers.sites.SiteThumbnailResolver;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionModelPopulatorTest
{
	private static final String SITE_UID = "test-site-id";
	private static final String CATALOG_ID = "test-catalog-id";
	private static final String CATALOG_VERSION = "test-version";
	private static final String SITE_NAME_EN = "site-name-EN";
	private static final String SITE_NAME_FR = "site-name-FR";
	private static final String CATALOG_NAME_EN = "catalog-name-EN";
	private static final String CATALOG_NAME_FR = "catalog-name-FR";
	private static final String REDIRECT_URL = "some-test-url";
	private static final String THUMBNAIL_URL = "some-thumbnail-url";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	@Mock
	private CMSSiteModel siteModel;
	@Mock
	private ContentCatalogModel catalogModel;
	@Mock
	private CatalogVersionModel versionModel;
	@Mock
	private SiteThumbnailResolver siteThumbnailResolver;
	@Mock
	private Comparator<CatalogVersionDetailData> catalogVersionDetailDataComparator;
	@Mock
	private LanguageFacade languageFacade;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private CatalogVersionModelPopulator populator;

	private CatalogVersionData versionDto;

	@Before
	public void setup() {
		versionDto = new CatalogVersionData();

		when(siteModel.getContentCatalogs()).thenReturn(Arrays.asList(catalogModel));
		final Set<CatalogVersionModel> versions = new HashSet<>();
		versions.add(versionModel);
		when(catalogModel.getCatalogVersions()).thenReturn(versions);

		when(siteModel.getUid()).thenReturn(SITE_UID);
		when(siteModel.getName(Locale.ENGLISH)).thenReturn(SITE_NAME_EN);
		when(siteModel.getName(Locale.FRENCH)).thenReturn(SITE_NAME_FR);
		when(siteModel.getRedirectURL()).thenReturn(REDIRECT_URL);
		when(catalogModel.getName(Locale.ENGLISH)).thenReturn(CATALOG_NAME_EN);
		when(catalogModel.getName(Locale.FRENCH)).thenReturn(CATALOG_NAME_FR);
		when(catalogModel.getId()).thenReturn(CATALOG_ID);
		when(versionModel.getVersion()).thenReturn(CATALOG_VERSION);
		when(siteThumbnailResolver.resolveHomepageThumbnailUrl(any(CMSSiteModel.class))).thenReturn(Optional.of(THUMBNAIL_URL));

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));

		populator.setCatalogVersionDetailDataComparator(catalogVersionDetailDataComparator);
		populator.setLocalizedPopulator(localizedPopulator);
	}

	@Test
	public void shouldPopulateNonLocalizedAttributes() throws Exception
	{
		populator.populate(siteModel, versionDto);

		assertThat(versionDto.getUid(), equalTo(SITE_UID));
		assertThat(versionDto.getCatalogVersionDetails().size(), is(1));
		final CatalogVersionDetailData detailDto = versionDto.getCatalogVersionDetails().stream().findFirst().get();
		assertThat(detailDto.getRedirectUrl(), equalTo(REDIRECT_URL));
		assertThat(detailDto.getThumbnailUrl(), equalTo(THUMBNAIL_URL));
		assertThat(detailDto.getCatalogId(), equalTo(CATALOG_ID));
		assertThat(detailDto.getVersion(), equalTo(CATALOG_VERSION));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(siteModel, versionDto);

		assertThat(versionDto.getName().get(EN), equalTo(SITE_NAME_EN));
		assertThat(versionDto.getName().get(FR), equalTo(SITE_NAME_FR));

		final CatalogVersionDetailData detailDto = versionDto.getCatalogVersionDetails().stream().findFirst().get();
		assertThat(detailDto.getName().get(EN), equalTo(CATALOG_NAME_EN));
		assertThat(detailDto.getName().get(FR), equalTo(CATALOG_NAME_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_NullMaps()
	{
		versionDto.setName(null);
		versionDto.setCatalogVersionDetails(null);

		populator.populate(siteModel, versionDto);

		assertThat(versionDto.getName().get(EN), equalTo(SITE_NAME_EN));
		assertThat(versionDto.getName().get(FR), equalTo(SITE_NAME_FR));

		final CatalogVersionDetailData detailDto = versionDto.getCatalogVersionDetails().stream().findFirst().get();
		assertThat(detailDto.getName().get(EN), equalTo(CATALOG_NAME_EN));
		assertThat(detailDto.getName().get(FR), equalTo(CATALOG_NAME_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		when(siteModel.getName(Locale.FRENCH)).thenReturn(null);
		when(catalogModel.getName(Locale.FRENCH)).thenReturn(null);

		populator.populate(siteModel, versionDto);

		assertThat(versionDto.getName().get(EN), equalTo(SITE_NAME_EN));
		assertThat(versionDto.getName().get(FR), nullValue());

		final CatalogVersionDetailData detailDto = versionDto.getCatalogVersionDetails().stream().findFirst().get();
		assertThat(detailDto.getName().get(EN), equalTo(CATALOG_NAME_EN));
		assertThat(detailDto.getName().get(FR), nullValue());
	}
}
