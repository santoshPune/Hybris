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
package de.hybris.platform.personalizationwebservices.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.personalizationfacades.segment.SegmentFacade;
import de.hybris.platform.personalizationwebservices.data.SegmentListWsDTO;
import de.hybris.platform.personalizationwebservices.validator.SegmentDataValidator;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.pagination.impl.DefaultWebPaginationUtils;


@IntegrationTest
public class SegmentControllerTest extends BaseControllerTest
{
	private static final String SEGMENT = "segment1";
	private static final String NONEXISTING_SEGMENT = "segment10000";

	private SegmentController segmentController;

	@Resource
	private SegmentFacade cxSegmentFacade;

	@Before
	public void setUp() throws Exception
	{
		segmentController = new SegmentController(cxSegmentFacade, new SegmentDataValidator());
		segmentController.setWebPaginationUtils(new DefaultWebPaginationUtils());
	}

	@Test
	public void getAllSegmentsTest()
	{
		//when
		final SegmentListWsDTO segments = segmentController.getSegments(Collections.emptyMap());

		//then
		assertNotNull(segments);
		assertNotNull(segments.getSegments());
		assertEquals(4, segments.getSegments().size());
	}

	@Test
	public void getSegmentByIdTest()
	{
		//when
		final SegmentData segment = segmentController.getSegment(SEGMENT);

		//then
		assertNotNull(segment);
		assertEquals(segment.getCode(), SEGMENT);
	}

	@Test(expected = NotFoundException.class)
	public void getNonexistingSegmentByIdTest()
	{
		//when
		segmentController.getSegment(NONEXISTING_SEGMENT);
	}

	@Test
	public void createSegmentTest()
	{
		//given
		final SegmentData dto = new SegmentData();
		dto.setCode(NONEXISTING_SEGMENT);

		//when
		final ResponseEntity<SegmentData> response = segmentController.createSegment(dto, getUriComponentsBuilder());

		SegmentData body = response.getBody();
		assertNotNull(body);
		assertEquals(dto.getCode(), body.getCode());

		//then
		final SegmentData segment = cxSegmentFacade.getSegment(NONEXISTING_SEGMENT);
		assertNotNull(segment);
		assertEquals(NONEXISTING_SEGMENT, segment.getCode());

		assertLocation(VERSION + "/segments/" + NONEXISTING_SEGMENT, response);
	}

	@Test(expected = AlreadyExistsException.class)
	public void createExistingSegmentTest()
	{
		//given
		final SegmentData dto = new SegmentData();
		dto.setCode(SEGMENT);

		//when
		segmentController.createSegment(dto, getUriComponentsBuilder());
	}

	@Test(expected = WebserviceValidationException.class)
	public void createIncompleteSegmentTest()
	{
		//given
		final SegmentData dto = new SegmentData();

		//when
		segmentController.createSegment(dto, getUriComponentsBuilder());
	}

	@Test
	public void updateSegmentTest()
	{
		//given
		final SegmentData dto = new SegmentData();
		dto.setCode(SEGMENT);

		//when
		SegmentData updateSegment = segmentController.updateSegment(SEGMENT, dto);

		//then
		assertNotNull(updateSegment);
		assertEquals(dto.getCode(), updateSegment.getCode());
	}

	@Test(expected = NotFoundException.class)
	public void updateNonexistingSegmentTest()
	{
		//given
		final SegmentData dto = new SegmentData();
		dto.setCode(NONEXISTING_SEGMENT);

		//when
		segmentController.updateSegment(NONEXISTING_SEGMENT, dto);
	}

	@Test
	public void deleteSegmentTest()
	{
		//when
		segmentController.deleteSegment(SEGMENT);

		//then
		try
		{
			segmentController.getSegment(SEGMENT);
			fail("Segment should be deleted");
		}
		catch (NotFoundException e)
		{
			//ok
		}
	}

	@Test(expected = NotFoundException.class)
	public void deleteNonexistingSegmentTest()
	{
		segmentController.deleteSegment(NONEXISTING_SEGMENT);
	}
}
