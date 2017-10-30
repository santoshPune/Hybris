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
 *
 *
 */
package de.hybris.platform.commercesearch.searchandizing.facet.reconfiguration.impl;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import de.hybris.platform.commercesearch.facet.config.FacetSearchStateData;
import de.hybris.platform.commercesearch.searchandizing.facet.reconfiguration.SearchStateApplicableFacetAdminService;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DefaultFacetAdminServiceTest
{

	private DefaultFacetAdminService facetSearchStateAwareService = new DefaultFacetAdminService();
	private static final String FACET_CODE = "facetCode";

	@Mock
	SearchStateApplicableFacetAdminService appropriateFacetAdminService;
	@Mock
	SearchStateApplicableFacetAdminService secondAppropriateFacetAdminService;
	@Mock
	SearchStateApplicableFacetAdminService inappropriateFacetAdminService;
	@Mock
	SearchStateApplicableFacetAdminService fallbackFacetAdminService;

	@Mock
	FacetSearchStateData facetSearchState;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		facetSearchState = new FacetSearchStateData();
		given(inappropriateFacetAdminService.isApplicable(facetSearchState)).willReturn(false);
		given(appropriateFacetAdminService.isApplicable(facetSearchState)).willReturn(true);
		given(appropriateFacetAdminService.isApplicable(facetSearchState)).willReturn(true);
	}


	@Test
	public void shouldUseAppropriateReconfiguration()
	{
		//given
		facetSearchStateAwareService.setSearchStateApplicableFacetAdminServices(Arrays.asList(inappropriateFacetAdminService,
				appropriateFacetAdminService));
		facetSearchStateAwareService.setFallbackFacetAdminService(fallbackFacetAdminService);

		//when
		facetSearchStateAwareService.toggleFacetVisibility(FACET_CODE, facetSearchState);

		//then
		verify(inappropriateFacetAdminService, never()).toggleFacetVisibility(FACET_CODE, facetSearchState);
		verify(appropriateFacetAdminService).toggleFacetVisibility(FACET_CODE, facetSearchState);
	}

	@Test
	public void shouldUseFirstAppropriateReconfiguration()
	{
		//given
		facetSearchStateAwareService.setSearchStateApplicableFacetAdminServices(Arrays.asList(appropriateFacetAdminService,
				secondAppropriateFacetAdminService));
		facetSearchStateAwareService.setFallbackFacetAdminService(fallbackFacetAdminService);

		//when
		facetSearchStateAwareService.toggleFacetVisibility(FACET_CODE, facetSearchState);

		//then
		verify(secondAppropriateFacetAdminService, never()).toggleFacetVisibility(FACET_CODE, facetSearchState);
		verify(appropriateFacetAdminService).toggleFacetVisibility(FACET_CODE, facetSearchState);
	}

	@Test
	public void shouldFallbackToDefaultReconfigurationService()
	{
		//given
		facetSearchStateAwareService
				.setSearchStateApplicableFacetAdminServices(Arrays.asList(inappropriateFacetAdminService));
		facetSearchStateAwareService.setFallbackFacetAdminService(fallbackFacetAdminService);

		//when
		facetSearchStateAwareService.toggleFacetVisibility(FACET_CODE, facetSearchState);

		//then
		verify(inappropriateFacetAdminService, never()).toggleFacetVisibility(FACET_CODE, facetSearchState);
		verify(fallbackFacetAdminService).toggleFacetVisibility(FACET_CODE, facetSearchState);
	}
}
