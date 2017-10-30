package de.hybris.platform.cmswebservices.items.facade.populator.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.common.facade.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmswebservices.data.CMSLinkComponentData;
import de.hybris.platform.cmswebservices.items.facade.populator.data.LinkComponentDataPopulator;
import de.hybris.platform.cmswebservices.languages.facade.LanguageFacade;
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

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentModelPopulatorTest
{
	private static final String LINKNAME_EN = "linkName-EN";
	private static final String LINKNAME_FR = "linkName-FR";
	private static final String URL_LINK = "test-url-link";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private LanguageFacade languageFacade;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private LinkComponentDataPopulator populator;

	private CMSLinkComponentData linkNameDto;
	private CMSLinkComponentModel linkNameModel;

	@Before
	public void setUp()
	{
		linkNameDto = new CMSLinkComponentData();
		linkNameModel = new CMSLinkComponentModel();

		linkNameDto.setExternal(Boolean.TRUE);
		linkNameDto.setUrl(URL_LINK);

		final Map<String, String> linkNames = new HashMap<>();
		linkNames.put(EN, LINKNAME_EN);
		linkNames.put(FR, LINKNAME_FR);
		linkNameDto.setLinkName(linkNames);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);

		when(cmsAdminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));

		populator.setLocalizedPopulator(localizedPopulator);

	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(linkNameDto, linkNameModel);

		assertThat(linkNameModel.isExternal(), is(Boolean.TRUE));
		assertThat(linkNameModel.getUrl(), is(URL_LINK));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages() {
		populator.populate(linkNameDto, linkNameModel);

		assertThat(linkNameModel.getLinkName(Locale.ENGLISH), is(LINKNAME_EN));
		assertThat(linkNameModel.getLinkName(Locale.FRENCH), is(LINKNAME_FR));

	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		linkNameDto.getLinkName().remove(FR);

		populator.populate(linkNameDto, linkNameModel);

		assertThat(linkNameModel.getLinkName(Locale.ENGLISH), is(LINKNAME_EN));
		assertThat(linkNameModel.getLinkName(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldNotPopulateLocalizedAttributes_NullMaps()
	{
		linkNameDto.setLinkName(null);

		populator.populate(linkNameDto, linkNameModel);

		assertThat(linkNameModel.getLinkName(Locale.ENGLISH), nullValue());
		assertThat(linkNameModel.getLinkName(Locale.FRENCH), nullValue());
	}

	@Test
	public void shouldDefaultExternalValue()
	{
		linkNameDto.setExternal(null);

		populator.populate(linkNameDto, linkNameModel);

		assertThat(linkNameModel.isExternal(), is(Boolean.FALSE));
	}


}
