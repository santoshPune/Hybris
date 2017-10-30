package de.hybris.platform.cmswebservices.items.facade.populator.data;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cmswebservices.common.facade.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmswebservices.data.CMSLinkComponentData;
import de.hybris.platform.cmswebservices.items.facade.populator.model.CmsLinkComponentModelPopulator;
import de.hybris.platform.cmswebservices.languages.facade.LanguageFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import jersey.repackaged.com.google.common.collect.Maps;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentDataPopulatorTest
{
	private static final String LINKNAME_EN = "linkName-EN";
	private static final String LINKNAME_FR = "linkName-FR";
	private static final String URL_LINK = "test-url-link";
	private static final String EN = Locale.ENGLISH.getLanguage();
	private static final String FR = Locale.FRENCH.getLanguage();

	@Mock
	private CMSLinkComponentModel linkNameModel;
	@Mock
	private LanguageFacade languageFacade;

	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	@InjectMocks
	private CmsLinkComponentModelPopulator populator;

	private CMSLinkComponentData linkNameDto;

	@Before
	public void setUp() {
		linkNameDto = new CMSLinkComponentData();
		linkNameDto.setLinkName(Maps.newHashMap());

		when(linkNameModel.getLinkName(Locale.ENGLISH)).thenReturn(LINKNAME_EN);
		when(linkNameModel.getLinkName(Locale.FRENCH)).thenReturn(LINKNAME_FR);
		when(linkNameModel.isExternal()).thenReturn(Boolean.TRUE);
		when(linkNameModel.getUrl()).thenReturn(URL_LINK);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));

		populator.setLocalizedPopulator(localizedPopulator);

	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		populator.populate(linkNameModel, linkNameDto);

		assertThat(linkNameModel.isExternal(), is(Boolean.TRUE));
		assertThat(linkNameModel.getUrl(), is(URL_LINK));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_NullMaps()
	{
		linkNameDto.setLinkName(null);

		populator.populate(linkNameModel, linkNameDto);

		assertThat(linkNameDto.getLinkName().get(EN), equalTo(LINKNAME_EN));
		assertThat(linkNameDto.getLinkName().get(FR), equalTo(LINKNAME_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_AllLanguages()
	{
		populator.populate(linkNameModel, linkNameDto);

		assertThat(linkNameDto.getLinkName().get(EN), equalTo(LINKNAME_EN));
		assertThat(linkNameDto.getLinkName().get(FR), equalTo(LINKNAME_FR));
	}

	@Test
	public void shouldPopulateLocalizedAttributes_SingleLanguages()
	{
		when(linkNameModel.getLinkName(Locale.FRENCH)).thenReturn(null);

		populator.populate(linkNameModel, linkNameDto);

		assertThat(linkNameDto.getLinkName().get(EN), equalTo(LINKNAME_EN));
		assertThat(linkNameDto.getLinkName().get(FR), nullValue());
	}


}
