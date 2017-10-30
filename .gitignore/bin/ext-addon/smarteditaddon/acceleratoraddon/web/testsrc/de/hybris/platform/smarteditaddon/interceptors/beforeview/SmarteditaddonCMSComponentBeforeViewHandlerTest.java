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
package de.hybris.platform.smarteditaddon.interceptors.beforeview;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SmarteditaddonCMSComponentBeforeViewHandlerTest
{
	private static final String CMSPARAGRAPH_VIEW = "cms/cmsparagraphcomponent";
	private static final String LINK_VIEW = "cms/linkcomponent";
	private static final List<String> smarteditaddonComponents = Arrays.asList(
			"cmsparagraphcomponent", "bannercomponent");

	@Mock
	private ResponsiveUtils responsiveUtils;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ModelAndView modelAndView;
	@Mock
	private SmarteditaddonResponsiveStrategy smarteditaddonResponsiveStrategy;

	@InjectMocks
	private SmarteditaddonCMSComponentBeforeViewHandler beforeViewHandler;

	@Before
	public void setUp()
	{
		beforeViewHandler.setSmarteditaddonComponents(smarteditaddonComponents);
	}

	@Test
	public void shouldUpdateViewName_validComponent() throws Exception
	{
		when(modelAndView.getViewName()).thenReturn(CMSPARAGRAPH_VIEW);
		when(smarteditaddonResponsiveStrategy.isResponsive()).thenReturn(Boolean.TRUE);

		beforeViewHandler.beforeView(request, response, modelAndView);

		verify(modelAndView).setViewName(
				SmarteditaddonCMSComponentBeforeViewHandler.ADDON_PREFIX + CMSPARAGRAPH_VIEW);
	}

	@Test
	public void shouldNotUpdateViewName_invalidComponent() throws Exception
	{
		when(modelAndView.getViewName()).thenReturn(LINK_VIEW);
		when(smarteditaddonResponsiveStrategy.isResponsive()).thenReturn(Boolean.TRUE);

		beforeViewHandler.beforeView(request, response, modelAndView);

		verify(modelAndView, times(0)).setViewName(LINK_VIEW);
	}

	@Test
	public void shouldNotUpdateViewName_notResponsive() throws Exception
	{
		when(smarteditaddonResponsiveStrategy.isResponsive()).thenReturn(Boolean.FALSE);

		beforeViewHandler.beforeView(request, response, modelAndView);

		verifyZeroInteractions(modelAndView);
	}
}
