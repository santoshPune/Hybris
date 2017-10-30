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
package de.hybris.platform.personalizationfacades.segment.impl;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.AbstractFacadeIntegrationTest;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.personalizationfacades.segment.SegmentFacade;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultSegmentFacadeIntegrationTest extends AbstractFacadeIntegrationTest
{
	private static final String NEW_SEGMENT_ID = "newSegment";

	@Resource(name = "defaultCxSegmentFacade")
	private SegmentFacade segmentFacade;

	@Test
	public void getSegmentTest()
	{
		//when
		final SegmentData segment = segmentFacade.getSegment(SEGMENT_ID);

		//then
		assertNotNull(segment);
		assertTrue(SEGMENT_ID.equals(segment.getCode()));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getNotExistingSegmentTest()
	{
		//when
		segmentFacade.getSegment(NOTEXISTING_SEGMENT_ID);
	}

	@Test
	public void getSegmentsTest()
	{

		//given
		final Set<String> expectedSegmentsCodes = Sets.newHashSet(SEGMENT_ID, SEGMENT_ID_1);

		//when
		final SearchResult<SegmentData> result = segmentFacade.getSegments(Collections.emptyMap(), 0, 10);

		//then
		Assert.assertNotNull(result);
		final List<SegmentData> resultList = result.getResult();
		Assert.assertNotNull(resultList);
		Assert.assertEquals(2, resultList.size());
		Assert.assertEquals(expectedSegmentsCodes,
				resultList.stream().map(SegmentData::getCode).collect(Collectors.toSet()));

		final SegmentData data = resultList.get(0);
		Assert.assertEquals(SEGMENT_ID, data.getCode());
	}

	@Test
	public void getSegmentsWhenThereIsNoSegmentsTest()
	{
		//given
		segmentFacade.deleteSegment(SEGMENT_ID);
		segmentFacade.deleteSegment(SEGMENT_ID_1);

		//when
		final SearchResult<SegmentData> resultList = segmentFacade.getSegments(Collections.emptyMap(), 0, 10);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertTrue(resultList.getResult().isEmpty());
	}

	@Test
	public void createSegmentTest()
	{
		//given
		final SegmentData segmentData = new SegmentData();
		segmentData.setCode(NEW_SEGMENT_ID);

		//when
		final SegmentData result = segmentFacade.createSegment(segmentData);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(NEW_SEGMENT_ID, result.getCode());
	}

	@Test(expected = AlreadyExistsException.class)
	public void createAltreadyExistedSegmentTest()
	{
		//given
		final SegmentData segmentData = new SegmentData();
		segmentData.setCode(SEGMENT_ID);

		//when
		segmentFacade.createSegment(segmentData);
	}

	@Test
	public void updateSegmentTest()
	{
		//given
		final SegmentData segmentData = new SegmentData();
		segmentData.setCode(SEGMENT_ID);

		//when
		final SegmentData result = segmentFacade.updateSegment(SEGMENT_ID, segmentData);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getCode(), segmentData.getCode());
	}


	@Test(expected = UnknownIdentifierException.class)
	public void updateNotExistingSegmentTest()
	{
		//given
		final SegmentData segmentData = new SegmentData();
		segmentData.setCode(NOTEXISTING_SEGMENT_ID);

		//when
		segmentFacade.updateSegment(NOTEXISTING_SEGMENT_ID, segmentData);
	}

	@Test
	public void deleteSegmentTest()
	{
		//given
		boolean segmentRemoved = false;

		//when
		segmentFacade.deleteSegment(SEGMENT_ID);

		//then
		try
		{
			segmentFacade.getSegment(SEGMENT_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			segmentRemoved = true;
		}
		assertTrue(segmentRemoved);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteNotExistingSegmentTest()
	{
		//when
		segmentFacade.deleteSegment(NOTEXISTING_SEGMENT_ID);
	}

}
