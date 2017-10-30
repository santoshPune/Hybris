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
package de.hybris.platform.personalizationfacades.customersegmentation.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.AbstractFacadeIntegrationTest;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCustomerSegmentationFacadeIntegrationTest extends AbstractFacadeIntegrationTest
{
	private static final String CUSTOMER_ID = "customer1@hybris.com";
	private static final String NOTEXISTING_CUSTOMER_ID = "nonExistCustomer";
	private static final String NOTRELATED_CUSTOMER_ID = "customer2@hybris.com";
	private static final String NOTRELATED_SEGMENT_ID = "segment1";

	private String SEGMENTATION_ID;
	private String NOTEXISTING_SEGMENTATION_ID;
	private String CREATED_SEGMENTATION_ID;
	private static final String INCORRECT_SEGMENTATION_ID = "incorrectId";

	@Resource(name = "defaultCxCustomerSegmentationFacade")
	private DefaultCustomerSegmentationFacade customerSegmentationFacade;

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		SEGMENTATION_ID = customerSegmentationFacade.getSegmentationHelper().getSegmentationCode(SEGMENT_ID, CUSTOMER_ID);
		NOTEXISTING_SEGMENTATION_ID = customerSegmentationFacade.getSegmentationHelper().getSegmentationCode(NOTRELATED_SEGMENT_ID,
				NOTRELATED_CUSTOMER_ID);
		CREATED_SEGMENTATION_ID = customerSegmentationFacade.getSegmentationHelper().getSegmentationCode(NOTRELATED_SEGMENT_ID,
				NOTRELATED_CUSTOMER_ID);

	}

	//Tests for getCustomerSegmentation
	@Test
	public void getCustomerSegmentationTest()
	{
		//when
		final CustomerSegmentationData result = customerSegmentationFacade.getCustomerSegmentation(SEGMENTATION_ID);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertEquals(CUSTOMER_ID, result.getCustomer().getUid());
		Assert.assertEquals(SEGMENT_ID, result.getSegment().getCode());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getNotExistingCustomerSegmentationTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentation(NOTEXISTING_SEGMENTATION_ID);
	}


	@Test(expected = UnknownIdentifierException.class)
	public void getCustomerSegmentationForNotExistingCustomerTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentation(customerSegmentationFacade.getSegmentationHelper().getSegmentationCode(
				SEGMENT_ID, NOTEXISTING_CUSTOMER_ID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getCustomerSegmentationForNotExistingSegmentTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentation(customerSegmentationFacade.getSegmentationHelper().getSegmentationCode(
				NOTEXISTING_SEGMENT_ID, CUSTOMER_ID));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCustomerSegmentationForIncorrectIdTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentation(INCORRECT_SEGMENTATION_ID);
	}

	//Tests for getCustomerSegmentations

	@Test
	public void getCustomerSegmentationsTest()
	{
		//when
		final SearchResult<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID,
				SEGMENT_ID, 0, 1);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertEquals(1, resultList.getResult().size());
		final CustomerSegmentationData result = resultList.getResult().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertEquals(CUSTOMER_ID, result.getCustomer().getUid());
		Assert.assertEquals(SEGMENT_ID, result.getSegment().getCode());
	}

	@Test
	public void getCustomerSegmentationsForCustomerTest()
	{
		//when
		final SearchResult<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID, null, 0, 1);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertEquals(1, resultList.getResult().size());
		final CustomerSegmentationData result = resultList.getResult().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertEquals(SEGMENT_ID, result.getSegment().getCode());
		Assert.assertNull(result.getCustomer());
	}

	@Test
	public void getCustomerSegmentationsForSegmentTest()
	{
		//when
		final SearchResult<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(null, SEGMENT_ID, 0, 1);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertEquals(1, resultList.getResult().size());
		final CustomerSegmentationData result = resultList.getResult().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertEquals(CUSTOMER_ID, result.getCustomer().getUid());
		Assert.assertNull(result.getSegment());
	}

	@Test
	public void getCustomerSegmentationsForNotExistingCustomerTest()
	{
		//when
		final SearchResult<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(
				NOTEXISTING_CUSTOMER_ID, SEGMENT_ID, 0, 1);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertTrue(resultList.getResult().isEmpty());
	}

	@Test
	public void getCustomerSegmentationsForNotExistingSegmentTest()
	{
		//when
		final SearchResult<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID,
				NOTEXISTING_SEGMENT_ID, 0, 1);

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResult());
		Assert.assertTrue(resultList.getResult().isEmpty());
	}

	//Tests for create method

	@Test
	public void createCustomerSegmentationsTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.getCustomer().setUid(NOTRELATED_CUSTOMER_ID);
		data.getSegment().setCode(NOTRELATED_SEGMENT_ID);


		//when
		final CustomerSegmentationData result = customerSegmentationFacade.createCustomerSegmentation(data);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(CREATED_SEGMENTATION_ID, result.getCode());
	}

	private CustomerSegmentationData createCustomerSegmentationData()
	{
		final CustomerSegmentationData customerSegmentationData = new CustomerSegmentationData();
		final CustomerData customerData = new CustomerData();
		final SegmentData segmentData = new SegmentData();
		customerSegmentationData.setCustomer(customerData);
		customerSegmentationData.setSegment(segmentData);
		customerSegmentationData.setAffinity(BigDecimal.ONE);

		return customerSegmentationData;
	}

	@Test(expected = AlreadyExistsException.class)
	public void createAltreadyExistedCustomerSegmentationTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.getCustomer().setUid(CUSTOMER_ID);
		data.getSegment().setCode(SEGMENT_ID);

		//when
		customerSegmentationFacade.createCustomerSegmentation(data);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createCustomerSegmentationWithNullSegmentTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.getCustomer().setUid(CUSTOMER_ID);
		data.setSegment(null);

		//when
		customerSegmentationFacade.createCustomerSegmentation(data);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createCustomerSegmentationWithNullCustomerTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.setCustomer(null);
		data.getSegment().setCode(SEGMENT_ID);

		//when
		customerSegmentationFacade.createCustomerSegmentation(data);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void createCustomerSegmentationForNotExistingCustomerTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.getCustomer().setUid(NOTEXISTING_CUSTOMER_ID);
		data.getSegment().setCode(SEGMENT_ID);

		//when
		customerSegmentationFacade.createCustomerSegmentation(data);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void createCustomerSegmentationsForNotExistingSegmentTest()
	{
		//given
		final CustomerSegmentationData data = createCustomerSegmentationData();
		data.getCustomer().setUid(CUSTOMER_ID);
		data.getSegment().setCode(NOTEXISTING_SEGMENT_ID);

		//when
		customerSegmentationFacade.createCustomerSegmentation(data);
	}

	//delete method tests

	@Test
	public void deleteCustomerSegmentationTest()
	{
		//given
		boolean customerSegmentationRemoved = false;

		//when
		customerSegmentationFacade.deleteCustomerSegmentation(SEGMENTATION_ID);

		//then
		try
		{
			customerSegmentationFacade.getCustomerSegmentation(SEGMENTATION_ID);
		}
		catch (final UnknownIdentifierException e)
		{
			customerSegmentationRemoved = true;
		}
		assertTrue(customerSegmentationRemoved);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteNotExistingCustomerSegmentationTest()
	{
		//when
		customerSegmentationFacade.deleteCustomerSegmentation(NOTEXISTING_SEGMENTATION_ID);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteCustomerSegmentationForNotExistingCustomerTest()
	{
		//when
		customerSegmentationFacade.deleteCustomerSegmentation(customerSegmentationFacade.getSegmentationHelper()
				.getSegmentationCode(SEGMENT_ID, NOTEXISTING_CUSTOMER_ID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteCustomerSegmentationForNotExistingSegmentTest()
	{
		//when
		customerSegmentationFacade.deleteCustomerSegmentation(customerSegmentationFacade.getSegmentationHelper()
				.getSegmentationCode(NOTEXISTING_SEGMENT_ID, CUSTOMER_ID));
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteCustomerSegmentationWithIncorrectIdTest()
	{
		//when
		customerSegmentationFacade.deleteCustomerSegmentation(INCORRECT_SEGMENTATION_ID);
	}
}
