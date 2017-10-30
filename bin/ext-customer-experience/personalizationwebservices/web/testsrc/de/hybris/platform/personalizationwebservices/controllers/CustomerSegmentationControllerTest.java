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

import java.math.BigDecimal;
import java.util.Collections;

import javax.annotation.Resource;

import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.customersegmentation.CustomerSegmentationFacade;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.personalizationfacades.segmentation.SegmentationHelper;
import de.hybris.platform.personalizationwebservices.data.CustomerSegmentationListWsDTO;
import de.hybris.platform.personalizationwebservices.validator.SegmentationIdValidator;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

@IntegrationTest
public class CustomerSegmentationControllerTest extends BaseControllerTest
{
	private static final String CUSTOMER = "customer1@hybris.com";
	private static final String NONEXISTING_CUSTOMER = "customer1000000@hybris.com";
	private static final String NOTRELATED_CUSTOMER = "customer4@hybris.com";
	private static final String SEGMENT = "segment1";
	private static final String NONEXISTING_SEGMENT = "segment10000";
	private static final String NOTRELATED_SEGMENT = "segment2";
	
	private String SEGMENTATION;
	private String NONEXISTING_SEGMENTATION;
	
	private CustomerSegmentationController controller;
	
	@Resource
	private CustomerSegmentationFacade cxCustomerSegmentationFacade;
	
	@Resource 
	private SegmentationHelper cxSegmentationHelper;


	@Resource
	WebPaginationUtils webPaginationUtils;
	
	@Before
	public void setup()
	{
		final SegmentationIdValidator idValidator = new SegmentationIdValidator(cxSegmentationHelper);

		controller = new CustomerSegmentationController(cxCustomerSegmentationFacade, idValidator);
		controller.setWebPaginationUtils(webPaginationUtils);

		SEGMENTATION = cxSegmentationHelper.getSegmentationCode(SEGMENT, CUSTOMER);
		NONEXISTING_SEGMENTATION = cxSegmentationHelper.getSegmentationCode(NOTRELATED_SEGMENT, NOTRELATED_CUSTOMER);
	}

	@Test(expected = WebserviceValidationException.class)
	public void getAllCustomerSegmentationNullParamsTest()
	{
		// when
		controller.getCustomerSegmentations(null, null, Collections.emptyMap());
	}

	@Test
	public void getCustomerSegmentationFromCustomerTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(CUSTOMER, null, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(4, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromNonexistingCustomerTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(NONEXISTING_CUSTOMER, null, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(0, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(null, SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(3, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromNonexistingSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(null, NONEXISTING_SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(0, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromExisitngCustomerNonexistingSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(CUSTOMER,
				NONEXISTING_SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(0, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromNonexisitngCustomerExistingSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(NONEXISTING_CUSTOMER,
				SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(0, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromNonexisitngCustomerNonExistingSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(NONEXISTING_CUSTOMER,
				NONEXISTING_SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(0, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationFromExistingCustomerAndExistingSegmentTest()
	{
		// when
		final CustomerSegmentationListWsDTO customerSegmentations = controller.getCustomerSegmentations(CUSTOMER, SEGMENT, Collections.emptyMap());

		// then
		assertNotNull(customerSegmentations);
		assertNotNull(customerSegmentations.getCustomerSegmentations());
		assertEquals(1, customerSegmentations.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationByIdTest()
	{
		// when
		final CustomerSegmentationData segmentation = controller.getCustomerSegmentation(SEGMENTATION);

		// then
		assertNotNull(segmentation);
		assertEquals(SEGMENTATION, segmentation.getCode());
		assertEquals(BigDecimal.ONE, segmentation.getAffinity());
		assertNotNull(segmentation.getSegment());
		assertEquals(SEGMENT, segmentation.getSegment().getCode());
		assertNotNull(segmentation.getCustomer());
		assertEquals(CUSTOMER, segmentation.getCustomer().getUid());
	}

	@Test(expected = NotFoundException.class)
	public void getCustomerSegmentationByNonexistingIdTest()
	{
		// when
		controller.getCustomerSegmentation(NONEXISTING_SEGMENTATION);
	}

	@Test
	public void createCustomerSegmentationWithAffinityTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(NOTRELATED_CUSTOMER, NOTRELATED_SEGMENT, "0.9");
		final String id = NONEXISTING_SEGMENTATION;

		// when
		final ResponseEntity<CustomerSegmentationData> response = controller.create(dto, getUriComponentsBuilder());

		// then
		assertLocation(VERSION + "/customersegmentations/" + id, response);
		final CustomerSegmentationData body = response.getBody();
		assertNotNull(body);
		assertEquals(id, body.getCode());
		assertEquals(dto.getAffinity(), body.getAffinity());

		final CustomerSegmentationData segmentation = controller.getCustomerSegmentation(id);
		assertNotNull(segmentation);
		assertEquals(id, segmentation.getCode());
		assertEquals(NOTRELATED_CUSTOMER, segmentation.getCustomer().getUid());
		assertEquals(NOTRELATED_SEGMENT, segmentation.getSegment().getCode());
	}

	public void createCustomerSegmentationNoAffinityTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(NOTRELATED_CUSTOMER, NOTRELATED_SEGMENT, null);
		final String id = NONEXISTING_SEGMENTATION;

		// when
		final ResponseEntity<CustomerSegmentationData> response = controller.create(dto, getUriComponentsBuilder());

		// then
		assertLocation(VERSION + "/customersegmentations/" + id, response);
		final CustomerSegmentationData body = response.getBody();
		assertNotNull(body);
		assertEquals(id, body.getCode());
		assertEquals(null, body.getAffinity());

		final CustomerSegmentationData segmentation = controller.getCustomerSegmentation(id);
		assertNotNull(segmentation);
		assertEquals(id, segmentation.getCode());
		assertEquals(NOTRELATED_CUSTOMER, segmentation.getCustomer().getUid());
		assertEquals(NOTRELATED_SEGMENT, segmentation.getSegment().getCode());
	}

	@Test(expected = WebserviceValidationException.class)
	public void createCustomerSegmentationForNonexistingCustomerTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(NONEXISTING_CUSTOMER, NOTRELATED_SEGMENT, null);

		// when
		controller.create(dto, getUriComponentsBuilder());
	}

	@Test(expected = WebserviceValidationException.class)
	public void createCustomerSegmentationForNonexistingSegmentTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(NOTRELATED_CUSTOMER, NONEXISTING_SEGMENT, null);

		// when
		controller.create(dto, getUriComponentsBuilder());
	}

	@Test(expected = AlreadyExistsException.class)
	public void createAlreadyExistingCustomerSegmentationTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(CUSTOMER, SEGMENT, "0.9");

		// when
		controller.create(dto, getUriComponentsBuilder());
	}

	@Test
	public void updateCustomerSegmentationTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(CUSTOMER, SEGMENT, "0.5");
		final String id = SEGMENTATION;

		// when
		final CustomerSegmentationData update = controller.update(id, dto);

		// then
		assertNotNull(update);
		assertEquals(id, update.getCode());
		assertEquals(new BigDecimal("0.5"), update.getAffinity());

		final CustomerSegmentationData segmentation = controller.getCustomerSegmentation(id);
		assertNotNull(segmentation);
		assertEquals(id, segmentation.getCode());
		assertEquals(new BigDecimal("0.5"), segmentation.getAffinity());
	}

	@Test(expected = NotFoundException.class)
	public void updateNonexistingCustomerSegmentationTest()
	{
		// given
		final CustomerSegmentationData dto = createSegmentationDTO(NOTRELATED_CUSTOMER, NOTRELATED_SEGMENT, "0.5");
		final String id = NONEXISTING_SEGMENTATION;

		// when
		controller.update(id, dto);
	}

	@Test
	public void deleteCustomerSegmentationTest()
	{
		// when
		controller.delete(SEGMENTATION);

		// then
		try
		{
			controller.getCustomerSegmentation(SEGMENTATION);
			fail();
		}
		catch (final NotFoundException e)
		{
			// OK
		}
	}

	@Test(expected = NotFoundException.class)
	public void deleteNonexistingCustomerSegmentationTest()
	{
		// when
		controller.delete(NONEXISTING_SEGMENTATION);
	}

	private CustomerSegmentationData createSegmentationDTO(final String customerCode, final String segmentCode, final String c)
	{
		final CustomerSegmentationData dto = new CustomerSegmentationData();
		dto.setCustomer(new CustomerData());
		dto.setSegment(new SegmentData());
		dto.getCustomer().setUid(customerCode);
		dto.getSegment().setCode(segmentCode);
		if (c != null)
		{
			dto.setAffinity(new BigDecimal(c));
		}
		return dto;
	}

}
