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
package de.hybris.platform.personalizationwebservices;

import static de.hybris.platform.personalizationfacades.customization.CustomizationTestUtils.assertVariationsEquals;
import static de.hybris.platform.personalizationfacades.customization.CustomizationTestUtils.creteCustomizationData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationservices.enums.CxGroupingOperator;
import de.hybris.platform.personalizationwebservices.constants.PersonalizationwebservicesConstants;
import de.hybris.platform.personalizationwebservices.data.CustomerSegmentationListWsDTO;
import de.hybris.platform.personalizationwebservices.data.CustomizationListWsDTO;
import de.hybris.platform.personalizationwebservices.data.SegmentListWsDTO;
import de.hybris.platform.personalizationwebservices.data.TriggerListWsDTO;
import de.hybris.platform.personalizationwebservices.data.VariationListWsDTO;
import de.hybris.platform.webservicescommons.constants.WebservicescommonsConstants;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;


@IntegrationTest
@NeedsEmbeddedServer(webExtensions =
{ PersonalizationwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
public class PersonalizationWebServiceTest extends BaseWebServiceTest
{
	private static final String SEGMENT_ENDPOINT = VERSION + "/segments";
	private static final String CUSTOMIZATION_ENDPOINT = VERSION + "/catalogs/testCatalog/catalogVersions/Online/customizations";
	private static final String INVALID_CUSTOMIZATION_ENDPOINT = VERSION
			+ "/catalogs/missingCatalog/catalogVersions/Online/customizations";
	private static final String CUSTOMIZATIONPACKAGE_ENDPOINT = VERSION
			+ "/catalogs/testCatalog/catalogVersions/Online/customizationpackages";

	private static final String VARIATION_ENDPOINT = "variations";
	private static final String CUSTOMERSEGMENTATION_ENDPOINT = VERSION + "/customersegmentations";

	private static final String SEGMENT = "segment1";
	private static final String CUSTOMIZATION = "customization1";
	private static final String CUSTOMIZATION_NAME = "customization1";
	private static final String NEW_CUSTOMIZATION = "newCustomization";
	private static final String NEW_CUSTOMIZATION_NAME = "newCustomizationName";
	private static final String VARIATION = "variation1";
	private static final String NEW_VARIATION = "newVariation";
	private static final String NEW_VARIATION_NAME = "newVariationName";
	private static final String NEW_TRIGGER = "newTrigger";

	@Test
	public void getAllSegmentsForCmsManager() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//.
				.path(SEGMENT_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		SegmentListWsDTO segments = response.readEntity(SegmentListWsDTO.class);
		assertNotNull(segments.getSegments());
		assertEquals(4, segments.getSegments().size());

		SegmentData segment = segments.getSegments().get(0);
		assertNotNull(segment);
		assertNotNull(segment.getCode());
	}

	@Test
	public void getSegmentsByCode() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForAdmin()//
				.path(SEGMENT_ENDPOINT)//
				.queryParam("code", "1").build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		SegmentListWsDTO segments = response.readEntity(SegmentListWsDTO.class);
		assertNotNull(segments.getSegments());
		assertEquals(1, segments.getSegments().size());

		SegmentData segment = segments.getSegments().get(0);
		assertNotNull(segment);
		assertNotNull(segment.getCode());
	}

	@Test
	public void getAllSegmentsForAdmin() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForAdmin()//
				.path(SEGMENT_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		SegmentListWsDTO segments = response.readEntity(SegmentListWsDTO.class);
		assertNotNull(segments.getSegments());
		assertEquals(4, segments.getSegments().size());

		SegmentData segment = segments.getSegments().get(0);
		assertNotNull(segment);
		assertNotNull(segment.getCode());
	}


	@Test
	public void getAllSegmentsWithoutAuthorization() throws IOException
	{
		//when
		Response response = getWsRequestBuilder()//
				.path(SEGMENT_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.UNAUTHORIZED, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("UnauthorizedError", error1.getType());
	}

	@Test
	public void getAllSegmentsWithoutProperRights() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCustomer()//
				.path(SEGMENT_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.FORBIDDEN, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("ForbiddenError", error1.getType());
	}

	@Test
	public void getCustomerSegmentation() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMERSEGMENTATION_ENDPOINT)//
				.queryParam("segmentId", SEGMENT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		
		CustomerSegmentationListWsDTO segmentation = response.readEntity(CustomerSegmentationListWsDTO.class);
		assertNotNull(segmentation);
		assertNotNull(segmentation.getCustomerSegmentations());
		assertEquals(3, segmentation.getCustomerSegmentations().size());
	}

	@Test
	public void getCustomerSegmentationsWithPagination() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMERSEGMENTATION_ENDPOINT)//
				.queryParam("segmentId", SEGMENT)//
				.queryParam(WebservicescommonsConstants.PAGE_SIZE, 2)//
				.queryParam(WebservicescommonsConstants.CURRENT_PAGE, 1)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		CustomerSegmentationListWsDTO result = response.readEntity(CustomerSegmentationListWsDTO.class);
		assertNotNull(result);
		assertNotNull(result.getCustomerSegmentations());
		assertNotNull(result.getPagination());

		PaginationWsDTO pagination = result.getPagination();

		assertEquals(1, pagination.getCount());
		assertEquals(1, pagination.getPage());
		assertEquals(3, pagination.getTotalCount());
		assertEquals(2, pagination.getTotalPages());
	}

	@Test
	public void deleteSegment() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(SEGMENT_ENDPOINT)//
				.path(SEGMENT)//
				.build()//
				.delete();

		//then
		WebservicesAssert.assertResponse(Status.NO_CONTENT, response);
	}

	@Test
	public void getCustmizationById() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		CustomizationData customization = response.readEntity(CustomizationData.class);
		assertNotNull(customization);
		assertEquals(CUSTOMIZATION, customization.getCode());
		assertEquals(CUSTOMIZATION, customization.getName());
	}

	@Test
	public void getCustmizationWithPagination() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.queryParam("code", CUSTOMIZATION.substring(1, 5))//
				.queryParam(WebservicescommonsConstants.PAGE_SIZE, 2).queryParam(WebservicescommonsConstants.CURRENT_PAGE, 1).build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		CustomizationListWsDTO result = response.readEntity(CustomizationListWsDTO.class);
		assertNotNull(result);
		assertNotNull(result.getCustomizations());
		assertNotNull(result.getPagination());

		PaginationWsDTO pagination = result.getPagination();

		assertEquals(2, pagination.getCount());
		assertEquals(1, pagination.getPage());
		assertEquals(5, pagination.getTotalCount());
		assertEquals(3, pagination.getTotalPages());
	}

	@Test
	public void getCustmizationByNameWithPagination() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.queryParam("name", CUSTOMIZATION.substring(0, 5))//
				.queryParam(WebservicescommonsConstants.PAGE_SIZE, 2).queryParam(WebservicescommonsConstants.CURRENT_PAGE, 1).build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		CustomizationListWsDTO result = response.readEntity(CustomizationListWsDTO.class);
		assertNotNull(result);
		assertNotNull(result.getCustomizations());
		assertNotNull(result.getPagination());

		PaginationWsDTO pagination = result.getPagination();

		assertEquals(2, pagination.getCount());
		assertEquals(1, pagination.getPage());
		assertEquals(5, pagination.getTotalCount());
		assertEquals(3, pagination.getTotalPages());
	}

	@Test
	public void createCustomizationWithCode() throws IOException, JAXBException
	{
		//given
		Date data = new Date();
		CustomizationData input = new CustomizationData();
		input.setCode(NEW_CUSTOMIZATION);
		input.setName(NEW_CUSTOMIZATION_NAME);
		input.setDescription("desc");
		input.setRank(Integer.valueOf(3));
		input.setEnabledStartDate(data);

		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.build()//
				.post(Entity.json(marshallDto(input, CustomizationData.class)));// entity(input, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CREATED, response);

		String location = response.getHeaderString("Location");
		assertTrue(location.contains(input.getCode()));

		CustomizationData readEntity = unmarshallResult(response, CustomizationData.class);
		assertEquals(input.getCode(), readEntity.getCode());
		assertEquals(input.getName(), readEntity.getName());
		assertEquals(input.getRank(), readEntity.getRank());
		assertEquals(input.getDescription(), readEntity.getDescription());
		assertDateTimeAlmostEqual(input.getEnabledStartDate(), readEntity.getEnabledStartDate());



		Response resultResponse = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(readEntity.getCode())//
				.build()//
				.get();

		CustomizationData result = unmarshallResult(resultResponse, CustomizationData.class);

		assertEquals(input.getCode(), result.getCode());
		assertEquals(input.getName(), result.getName());
		assertEquals(input.getRank(), result.getRank());
		assertEquals(input.getDescription(), result.getDescription());
		assertDateTimeAlmostEqual(input.getEnabledStartDate(), result.getEnabledStartDate());
	}

	@Test
	public void createCustomization() throws IOException, JAXBException
	{
		//given
		Date data = new Date();
		CustomizationData input = new CustomizationData();
		input.setName(NEW_CUSTOMIZATION_NAME);
		input.setDescription("desc");
		input.setRank(Integer.valueOf(3));
		input.setEnabledStartDate(data);



		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.build()//
				.post(Entity.json(marshallDto(input, CustomizationData.class)));// entity(input, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CREATED, response);
		CustomizationData readEntity = unmarshallResult(response, CustomizationData.class);
		assertNotNull(readEntity.getCode());
		assertEquals(input.getName(), readEntity.getName());
		assertEquals(input.getRank(), readEntity.getRank());
		assertEquals(input.getDescription(), readEntity.getDescription());
		assertDateTimeAlmostEqual(input.getEnabledStartDate(), readEntity.getEnabledStartDate());


		String location = response.getHeaderString("Location");
		assertTrue(location.contains(readEntity.getCode()));

		Response resultResponse = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(readEntity.getCode())//
				.build()//
				.get();

		CustomizationData result = unmarshallResult(resultResponse, CustomizationData.class);

		assertEquals(readEntity.getCode(), result.getCode());
		assertEquals(input.getName(), result.getName());
		assertEquals(input.getRank(), result.getRank());
		assertEquals(input.getDescription(), result.getDescription());
		assertDateTimeAlmostEqual(input.getEnabledStartDate(), result.getEnabledStartDate());
	}

	@Test
	public void updateCustomizationEnabledDates() throws IOException, JAXBException
	{
		//given
		CustomizationData existing = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.build()//
				.get(CustomizationData.class);

		assertEquals(true, existing.isActive());

		Date data = new Date(0L);
		existing.setEnabledStartDate(data);
		existing.setEnabledEndDate(data);

		//when
		Response read = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION).build()//
				.put(Entity.json(marshallDto(existing, CustomizationData.class)));

		CustomizationData readEntity = unmarshallResult(read, CustomizationData.class);

		//then
		assertEquals(existing.getCode(), readEntity.getCode());
		assertEquals(existing.getName(), readEntity.getName());
		assertEquals(existing.getRank(), readEntity.getRank());
		assertEquals(existing.getDescription(), readEntity.getDescription());
		assertDateTimeAlmostEqual(existing.getEnabledStartDate(), readEntity.getEnabledStartDate());
		assertDateTimeAlmostEqual(existing.getEnabledEndDate(), readEntity.getEnabledEndDate());
		assertEquals(false, readEntity.isActive());

		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.build()//
				.get();

		CustomizationData result = unmarshallResult(response, CustomizationData.class);

		assertEquals(existing.getCode(), result.getCode());
		assertEquals(existing.getName(), result.getName());
		assertEquals(existing.getRank(), result.getRank());
		assertEquals(existing.getDescription(), result.getDescription());
		assertDateTimeAlmostEqual(existing.getEnabledStartDate(), result.getEnabledStartDate());
		assertDateTimeAlmostEqual(existing.getEnabledEndDate(), result.getEnabledEndDate());
		assertEquals(false, readEntity.isActive());
	}

	@Test
	public void getInvalidCatalog() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(INVALID_CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
	}



	@Test
	public void getVariations() throws IOException
	{
		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		VariationListWsDTO variations = response.readEntity(VariationListWsDTO.class);
		assertNotNull(variations);
		assertNotNull(variations.getVariations());
		assertEquals(10, variations.getVariations().size());
	}

	@Test
	public void updateVariation() throws IOException
	{
		//given
		VariationData input = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.build()//
				.get(VariationData.class);
		input.setRank(Integer.valueOf(4));;
		input.setName(NEW_VARIATION_NAME);

		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.build()//
				.put(Entity.entity(input, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		VariationData output = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.build()//
				.get(VariationData.class);

		assertEquals(input.getCode(), output.getCode());
		assertEquals(input.getName(), output.getName());
		assertEquals(input.getRank(), output.getRank());
	}

	@Test
	public void getTriggers() throws IOException
	{
		//when
		TriggerListWsDTO list = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(TRIGGER_ENDPOINT)///
				.build()//
				.get(TriggerListWsDTO.class);

		//then
		assertNotNull(list.getTriggers());
		assertEquals(1, list.getTriggers().size());
	}

	@Test
	public void updateTrigger() throws IOException
	{
		TestSegmentTriggerData input = new TestSegmentTriggerData();
		input.setGroupBy("AND");
		input.setSegments(new ArrayList<>());

		SegmentData s1 = new SegmentData();
		s1.setCode("segment1");
		input.getSegments().add(s1);

		SegmentData s2 = new SegmentData();
		s2.setCode("segment2");
		input.getSegments().add(s2);

		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(TRIGGER_ENDPOINT)//
				.path(TRIGGER)//
				.build()//
				.put(Entity.json(input));

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		TestSegmentTriggerData result = response.readEntity(TestSegmentTriggerData.class);

		//then
		assertNotNull(result);
		assertEquals(TRIGGER, result.getCode());
		assertEquals(input.getGroupBy(), result.getGroupBy());
		assertNotNull(result.getSegments());
		assertEquals(2, result.getSegments().size());

	}

	@Test
	public void createCustomizationPackage() throws IOException
	{
		//given
		final CustomizationData input = creteCustomizationData(NEW_CUSTOMIZATION, NEW_CUSTOMIZATION_NAME, NEW_VARIATION,
				NEW_VARIATION_NAME, () -> createSegmentTriggerData(NEW_TRIGGER, SEGMENT));
		input.setDescription("desc");
		input.setRank(Integer.valueOf(3));

		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATIONPACKAGE_ENDPOINT)//
				.build()//
				.post(Entity.entity(input, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CREATED, response);
		String location = response.getHeaderString("Location");
		assertTrue(location.contains(NEW_CUSTOMIZATION));

		CustomizationData result = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(NEW_CUSTOMIZATION)//
				.build()//
				.get(CustomizationData.class);

		assertEquals("Invalid customization code", input.getCode(), result.getCode());
		assertEquals("Invalid customization rank", input.getRank(), result.getRank());
		assertEquals("Invalid customization description", input.getDescription(), result.getDescription());

		VariationListWsDTO variationListDTO = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(NEW_CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.queryParam("fields", "FULL").build()//
				.get(VariationListWsDTO.class);

		Assert.assertNotNull("Variations should not be null", variationListDTO.getVariations());
		Assert.assertEquals("Variations size should be 1", 1, variationListDTO.getVariations().size());
		assertVariationsEquals(input.getVariations().get(0), variationListDTO.getVariations().get(0), null);
	}

	@Test
	public void updateCustomizationPackage() throws IOException
	{
		//given
		final CustomizationData input = creteCustomizationData(CUSTOMIZATION, CUSTOMIZATION_NAME, NEW_VARIATION,
				NEW_VARIATION_NAME, () -> createSegmentTriggerData(NEW_TRIGGER, SEGMENT));
		input.setDescription("newDescription");
		input.setRank(Integer.valueOf(1));

		//when
		Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATIONPACKAGE_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.build()//
				.put(Entity.entity(input, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		CustomizationData result = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.build()//
				.get(CustomizationData.class);

		assertEquals("Invalid customization code", input.getCode(), result.getCode());
		assertEquals("Invalid customization rank", input.getRank(), result.getRank());
		assertEquals("Invalid customization description", input.getDescription(), result.getDescription());

		VariationListWsDTO variationListDTO = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.queryParam("fields", "FULL").build()//
				.get(VariationListWsDTO.class);

		Assert.assertNotNull("Variations should not be null", variationListDTO.getVariations());
		Assert.assertEquals("Variations size should be 1", 1, variationListDTO.getVariations().size());
		assertVariationsEquals(input.getVariations().get(0), variationListDTO.getVariations().get(0), null);
	}

	protected TestSegmentTriggerData createSegmentTriggerData(String triggerCode, String segmentCode)
	{
		final TestSegmentTriggerData trigger = new TestSegmentTriggerData();
		trigger.setCode(triggerCode);
		trigger.setGroupBy(CxGroupingOperator.AND.getCode());
		if (segmentCode != null)
		{
			final SegmentData segment = new SegmentData();
			segment.setCode(segmentCode);
			trigger.setSegments(Collections.singletonList(segment));
		}
		return trigger;
	}

	protected static class TestSegmentTriggerData extends SegmentTriggerData
	{
		private static final long serialVersionUID = 1L;
		private String type = "segmentTriggerData";

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}
	}
}
