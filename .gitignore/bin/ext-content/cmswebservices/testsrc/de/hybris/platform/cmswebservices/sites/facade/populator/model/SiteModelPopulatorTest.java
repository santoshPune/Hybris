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
package de.hybris.platform.cmswebservices.sites.facade.populator.model;

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmswebservices.common.facade.populator.LocalizedPopulator;
import de.hybris.platform.cmswebservices.data.SiteData;
import de.hybris.platform.cmswebservices.resolvers.sites.SiteThumbnailResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SiteModelPopulatorTest
{
	@Mock
	private ItemModelInternalContext mockContext;
	@Mock
	private CMSAdminPageService cmsAdminPageService;
	
	@Mock
	private SiteThumbnailResolver siteThumbnailResolver;

	@Mock
	private ContentPageModel homepage;
	@Mock
	private MediaModel thumbnail;
	@Mock
	private LocalizedPopulator localizedPopulator;

	@InjectMocks
	private SiteModelPopulator populator;

	private final CMSSiteModel sourceSite = new CMSSiteModel();

	@Before
	public void setup() {
		when(siteThumbnailResolver.resolveHomepageThumbnailUrl(any(CMSSiteModel.class))).thenReturn(of(APPAREL.getThumbnailUri()));
		when(cmsAdminPageService.getHomepage(sourceSite)).thenReturn(homepage);
		when(homepage.getPreviewImage()).thenReturn(thumbnail);
		when(thumbnail.getDownloadURL()).thenReturn(APPAREL.getThumbnailUri());
	}

	@Test
	public void populateWillPopulateADtoWhenGivenAModelIgnoreName() throws Exception {
		final SiteData targetSiteData = new ComparableSiteData();
		final Map<String, String> names = new HashMap<>();
		names.put(Locale.ENGLISH.toString(), "test-site-name");
		targetSiteData.setName(names);

		populator.populate(sourceSite, targetSiteData);

		verify(localizedPopulator).populate(any(), any());
	}

	@Test
	public void populateWillPopulateADtoAndLocalizedPopulatorHasBeenCalled() throws Exception {
		final SiteData targetSiteData = new ComparableSiteData();
		populator.populate(sourceSite, targetSiteData);
		verify(localizedPopulator).populate(Mockito.any(), Mockito.any());
	}

    @Test
    public void assertsThatGetSiteDataNameSetterPopulatesTheTarget()
    {
        final String expectedValue = "value";
        SiteData targetSiteData = new ComparableSiteData();
        targetSiteData.setName(new HashMap<>());
        final BiConsumer<Locale, String> siteDataNameSetter = populator.getSiteDataNameSetter(targetSiteData);
        siteDataNameSetter.accept(Locale.ENGLISH, expectedValue);
        assertThat(targetSiteData.getName().get(localizedPopulator.getLanguage(Locale.ENGLISH)), is(expectedValue));
    }

	@Test
	public void assertsThatGetSiteModelNameGetter()
	{
		final String expectedValue = "value";
		final CMSSiteModel model = Mockito.mock(CMSSiteModel.class);
		when(model.getName(Locale.ENGLISH)).thenReturn(expectedValue);
		final Function<Locale, String> siteModelNameGetter = populator.getSiteModelNameGetter(model);
		final String returnedValue = siteModelNameGetter.apply(Locale.ENGLISH);
		assertThat(returnedValue, is(expectedValue));
	}
}
