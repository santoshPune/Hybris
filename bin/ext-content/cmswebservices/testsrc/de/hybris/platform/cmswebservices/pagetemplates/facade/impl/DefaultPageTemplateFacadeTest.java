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
package de.hybris.platform.cmswebservices.pagetemplates.facade.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmswebservices.data.PageTemplateDTO;
import de.hybris.platform.cmswebservices.data.PageTemplateData;
import de.hybris.platform.cmswebservices.util.dao.CMSPageTypeDao;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageTemplateFacadeTest
{
	@Spy
	@InjectMocks
	private DefaultPageTemplateFacade pageTemplateFacade;

	@Mock
	private CMSPageTypeDao cmsPageTypeDao;
	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private Converter<PageTemplateModel, PageTemplateData> pageTemplateModelConverter;

	@Mock
	private CMSPageTypeModel pageType;

	@Mock
	private PageTemplateModel model1;
	@Mock
	private PageTemplateModel model2;
	@Mock
	private PageTemplateData data1;
	@Mock
	private PageTemplateData data2;
	@Mock
	private MediaModel iconMedia;

	@Before
	public void setup()
	{
		when(iconMedia.getURL()).thenReturn("previewIconRUL");
		when(model1.getUid()).thenReturn("theUid");
		when(model1.getFrontendTemplateName()).thenReturn("theName");
		when(model1.getPreviewIcon()).thenReturn(iconMedia);
	}


	@Test
	public void willRetrieveActivetemplatesForTheGivenPageType()
	{
		String pageTypeCode = "pageTypeCode";
		Boolean active = true;

		PageTemplateDTO pageTemplateDto = new PageTemplateDTO();
		pageTemplateDto.setPageTypeCode(pageTypeCode);
		pageTemplateDto.setActive(active);

		when(cmsPageTypeDao.getCMSPageTypeByCode(pageTypeCode)).thenReturn(pageType);
		when(cmsAdminPageService.getAllRestrictedPageTemplates(active, pageType)).thenReturn(asList(model1));
		when(pageTemplateModelConverter.convert(model1)).thenReturn(data1);

		List<PageTemplateData> result = pageTemplateFacade.findPageTemplates(pageTemplateDto);

		assertThat(result.size(), is(equalTo(1)));
		assertThat(result, contains(data1));

		verify(cmsAdminPageService, never()).getAllRestrictedPageTemplates(false, pageType);

	}

	@Test
	public void willRetrieveNonActivetemplatesForTheGivenPageType()
	{
		String pageTypeCode = "pageTypeCode";
		Boolean active = false;

		PageTemplateDTO pageTemplateDto = new PageTemplateDTO();
		pageTemplateDto.setPageTypeCode(pageTypeCode);
		pageTemplateDto.setActive(active);

		when(cmsPageTypeDao.getCMSPageTypeByCode(pageTypeCode)).thenReturn(pageType);
		when(cmsAdminPageService.getAllRestrictedPageTemplates(active, pageType)).thenReturn(asList(model1));
		when(pageTemplateModelConverter.convert(model1)).thenReturn(data1);

		List<PageTemplateData> result = pageTemplateFacade.findPageTemplates(pageTemplateDto);

		assertThat(result.size(), is(equalTo(1)));
		assertThat(result, contains(data1));

		verify(cmsAdminPageService, never()).getAllRestrictedPageTemplates(true, pageType);

	}

	@Test
	public void willRetrieveAlltemplatesForTheGivenPageType()
	{
		String pageTypeCode = "pageTypeCode";

		PageTemplateDTO pageTemplateDto = new PageTemplateDTO();
		pageTemplateDto.setPageTypeCode(pageTypeCode);

		when(cmsPageTypeDao.getCMSPageTypeByCode(pageTypeCode)).thenReturn(pageType);
		when(cmsAdminPageService.getAllRestrictedPageTemplates(true, pageType)).thenReturn(asList(model1));
		when(cmsAdminPageService.getAllRestrictedPageTemplates(false, pageType)).thenReturn(asList(model2));
		when(pageTemplateModelConverter.convert(model1)).thenReturn(data1);
		when(pageTemplateModelConverter.convert(model2)).thenReturn(data2);

		List<PageTemplateData> result = pageTemplateFacade.findPageTemplates(pageTemplateDto);

		assertThat(result.size(), is(equalTo(2)));
		assertThat(result, contains(data1, data2));

	}

}

