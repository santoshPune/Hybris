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
package de.hybris.platform.cmswebservices.filter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class RestSessionDataInjectionFilterTest
{


	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private SessionService sessionService;

	@InjectMocks
	private RestSessionDataInjectionFilter filter;

	@Test
	public void testFilter() throws IOException, ServletException
	{
		final Collection<CatalogVersionModel> catalogVersions = mock(Collection.class);

		final FilterChain filterChain = mock(FilterChain.class);
		final ServletRequest req = mock(HttpServletRequest.class);
		final ServletResponse resp = mock(HttpServletResponse.class);
		when(sessionService.executeInLocalView(any())).thenReturn(catalogVersions);

		filter.doFilter(req, resp, filterChain);

		verify(sessionService).createNewSession();
		verify(sessionService).executeInLocalView(any());
		verify(sessionService).setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, catalogVersions);
		verify(filterChain).doFilter(req, resp);
	}

}
