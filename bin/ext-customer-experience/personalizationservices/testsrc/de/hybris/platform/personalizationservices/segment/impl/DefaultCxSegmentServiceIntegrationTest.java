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
package de.hybris.platform.personalizationservices.segment.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationservices.AbstractCxServiceTest;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultCxSegmentServiceIntegrationTest extends AbstractCxServiceTest
{
	@Resource
	private CxSegmentService cxSegmentService;

	@Test
	public void findSegmentByCodeTest()
	{
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT_CODE);

		assertTrue(segment.isPresent());
		assertTrue(SEGMENT_CODE.equals(segment.get().getCode()));
	}

	@Test
	public void findNoSegmentByCodeTest()
	{
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT_CODE + "...");

		assertFalse(segment.isPresent());
	}

	@Test
	public void findSegmetnsTest()
	{
		final SearchResult<CxSegmentModel> segments = cxSegmentService.getSegments(Collections.emptyMap(), 0, 10);

		assertNotNull(segments);
		assertNotNull(segments.getResult());
		assertTrue(!segments.getResult().isEmpty());
	}

	@Test
	public void findSegmetnsByCodeTest()
	{
		final SearchResult<CxSegmentModel> segments = cxSegmentService.getSegments(Collections.singletonMap("code", "1"), 0, 10);

		assertNotNull(segments);
		assertNotNull(segments.getResult());
		assertEquals(1, segments.getResult().size());
	}
}
