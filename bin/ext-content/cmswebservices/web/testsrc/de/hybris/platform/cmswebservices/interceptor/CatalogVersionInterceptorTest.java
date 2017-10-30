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
package de.hybris.platform.cmswebservices.interceptor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.HandlerMapping;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class CatalogVersionInterceptorTest
{
	private static final String VERSION = "staged";
	private static final String CATALOG_ID = "electronics";
	private static final String URI = "/catalogs/electronics/versions/staged/items";
	private static final String IMCOMPLETE_URI = "/catalogs/electronics/items";
	private static final String EMPTY_URI = "/";

	@InjectMocks
	private CatalogVersionInterceptor catalogVersionInterceptor;

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private Object handler;

	@Test
	public void shouldAddCatalogVersionToSession() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(CmswebservicesConstants.URI_CATALOG_ID, CATALOG_ID);
		pathVariables.put(CmswebservicesConstants.URI_VERSION_ID, VERSION);

		when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);

		catalogVersionInterceptor.preHandle(request, response, handler);

		verify(cmsAdminSiteService).setActiveCatalogVersion(CATALOG_ID, VERSION);
		verify(catalogVersionService).setSessionCatalogVersion(CATALOG_ID, VERSION);
	}

	@Test
	public void shouldNotAddCatalogVersionToSession_IncompleteURI() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(CmswebservicesConstants.URI_CATALOG_ID, CATALOG_ID);
		pathVariables.put(CmswebservicesConstants.URI_VERSION_ID, null);

		when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);

		catalogVersionInterceptor.preHandle(request, response, handler);

		verifyZeroInteractions(cmsAdminSiteService);
	}

	@Test
	public void shouldNotAddCatalogVersionToSession_EmptyURI() throws Exception
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(CmswebservicesConstants.URI_CATALOG_ID, null);
		pathVariables.put(CmswebservicesConstants.URI_VERSION_ID, null);

		when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);

		catalogVersionInterceptor.preHandle(request, response, handler);

		verifyZeroInteractions(cmsAdminSiteService);
	}
}