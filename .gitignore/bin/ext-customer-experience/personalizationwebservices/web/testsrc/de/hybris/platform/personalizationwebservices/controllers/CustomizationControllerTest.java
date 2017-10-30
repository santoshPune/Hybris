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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.action.ActionFacade;
import de.hybris.platform.personalizationfacades.customization.CustomizationFacade;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.personalizationfacades.variation.VariationFacade;
import de.hybris.platform.personalizationwebservices.data.CustomizationListWsDTO;
import de.hybris.platform.personalizationwebservices.data.VariationListWsDTO;
import de.hybris.platform.personalizationwebservices.validator.ActionDataValidator;
import de.hybris.platform.personalizationwebservices.validator.CustomizationDataValidator;
import de.hybris.platform.personalizationwebservices.validator.VariationDataValidator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.errors.exceptions.CodeConflictException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;


@IntegrationTest
public class CustomizationControllerTest extends BaseControllerTest
{
	private static final String CUSTOMIZATION = "customization1";
	private static final String CUSTOMIZATION_NAME = "customization1";
	private static final String NONEXISTING_CUSTOMIZATION = "customization1000";
	private static final String NONEXISTING_CUSTOMIZATION_NAME = "customization1000";
	private static final String VARIATION = "variation1";
	private static final String VARIATION_NAME = "variation1";
	private static final String NONEXISTING_VARIATION = "variation1000";
	private static final String NONEXISTING_VARIATION_NAME = "variation1000";

	private static final Integer ONE = Integer.valueOf(1);
	private static final Integer TWO = Integer.valueOf(2);

	CustomizationController controller;

	@Resource(name = "defaultCxCustomizationFacade")
	CustomizationFacade cxCustomizationFacade;
	@Resource(name = "defaultCxVariationFacade")
	VariationFacade cxVariationFacade;
	@Resource(name = "defaultCxActionFacade")
	ActionFacade cxActionFacade;
	@Resource
	ModelService modelService;
	@Resource
	WebPaginationUtils webPaginationUtils;


	@Before
	public void setUp() throws Exception
	{
		controller = new CustomizationController(cxActionFacade, cxCustomizationFacade, cxVariationFacade,
				new CustomizationDataValidator(), new VariationDataValidator(), new ActionDataValidator());
		controller.setWebPaginationUtils(webPaginationUtils);
	}

	@Test
	public void getAllCustomizationsTest()
	{
		//when
		CustomizationListWsDTO customizations = controller.getCustomizations(CATALOG, CATALOG_VERSION, Collections.emptyMap());

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getCustomizations());
		assertEquals(5, customizations.getCustomizations().size());
	}

	@Test
	public void getCustomizationByIdTest()
	{
		//when
		CustomizationData customization = controller.getCustomization(CATALOG, CATALOG_VERSION, CUSTOMIZATION);

		//then
		assertNotNull("Customization shouldn't be empty", customization);
		assertEquals("Invalid customizaiton code", CUSTOMIZATION, customization.getCode());
		assertNotNull("Variations in customization shouldn't be empty", customization.getVariations());
		assertEquals("Invaid number of variations in customziation", 10, customization.getVariations().size());
	}

	@Test(expected = NotFoundException.class)
	public void getNonexistingCustomizationByIdTest()
	{
		controller.getCustomization(CATALOG, CATALOG_VERSION, NONEXISTING_CUSTOMIZATION);
	}

	@Test
	public void createCustomizationTest()
	{
		//given
		Date date = new Date();

		CustomizationData dto = new CustomizationData();
		dto.setName(NONEXISTING_CUSTOMIZATION_NAME);
		dto.setRank(ONE);
		dto.setEnabledEndDate(date);

		//when
		ResponseEntity<CustomizationData> response = controller.createCustomization(CATALOG, CATALOG_VERSION, dto,
				getUriComponentsBuilder());

		//then

		CustomizationData body = response.getBody();
		assertNotNull(body);
		assertNotNull(body.getCode());
		assertEquals(dto.getName(), body.getName());
		assertEquals(dto.getEnabledEndDate(), body.getEnabledEndDate());

		assertLocationWithCatalog("customizations/" + body.getCode(), response);

		CustomizationListWsDTO customizations = controller.getCustomizations(CATALOG, CATALOG_VERSION, Collections.emptyMap());
		Set<String> codeSet = customizations.getCustomizations().stream().map(c -> c.getCode()).collect(Collectors.toSet());
		assertTrue("Customization was not created properly.", codeSet.contains(body.getCode()));

		Set<Integer> rankSet = customizations.getCustomizations().stream().map(c -> c.getRank()).collect(Collectors.toSet());
		assertEquals("Customization rank was not updated properly.", customizations.getCustomizations().size(), rankSet.size());
	}

	@Test(expected = WebserviceValidationException.class)
	public void createIncompleteCustomizationTest()
	{
		CustomizationData dto = new CustomizationData();

		controller.createCustomization(CATALOG, CATALOG_VERSION, dto, getUriComponentsBuilder());
	}

	@Test(expected = AlreadyExistsException.class)
	public void createExistingCustomizationTest()
	{
		//given
		CustomizationData dto = new CustomizationData();
		dto.setCode(CUSTOMIZATION);
		dto.setName(CUSTOMIZATION_NAME);
		dto.setRank(ONE);

		//when
		controller.createCustomization(CATALOG, CATALOG_VERSION, dto, getUriComponentsBuilder());
	}

	@Test
	public void updateCustomizationTest()
	{
		//given
		CustomizationData dto = new CustomizationData();
		dto.setCode(CUSTOMIZATION);
		dto.setName(CUSTOMIZATION_NAME);
		dto.setRank(TWO);

		//when
		CustomizationData updateCustomization = controller.updateCustomization(CATALOG, CATALOG_VERSION, CUSTOMIZATION, dto);

		//then
		assertEquals(dto.getCode(), updateCustomization.getCode());
		assertEquals(dto.getRank(), updateCustomization.getRank());

		CustomizationData customization = controller.getCustomization(CATALOG, CATALOG_VERSION, CUSTOMIZATION);
		assertEquals(dto.getRank(), customization.getRank());
	}

	@Test(expected = CodeConflictException.class)
	public void updateCustomizationWithInconsistenCodeTest()
	{
		//given
		CustomizationData dto = new CustomizationData();
		dto.setCode(CUSTOMIZATION);
		dto.setName(CUSTOMIZATION_NAME);
		dto.setRank(TWO);

		//when
		controller.updateCustomization(CATALOG, CATALOG_VERSION, NONEXISTING_CUSTOMIZATION, dto);
	}

	@Test(expected = NotFoundException.class)
	public void updateNonexistingCustomizationTest()
	{
		//given
		CustomizationData dto = new CustomizationData();
		dto.setCode(NONEXISTING_CUSTOMIZATION);
		dto.setName(NONEXISTING_CUSTOMIZATION_NAME);
		dto.setRank(TWO);

		//when
		controller.updateCustomization(CATALOG, CATALOG_VERSION, NONEXISTING_CUSTOMIZATION, dto);
	}

	@Test
	public void deleteCustomizationTest()
	{
		//when
		controller.deleteCustomization(CATALOG, CATALOG_VERSION, CUSTOMIZATION);

		//then
		try
		{
			controller.getCustomization(CATALOG, CATALOG_VERSION, CUSTOMIZATION);
			fail("Customization should be deleted");
		}
		catch (NotFoundException e)
		{
			//OK
		}
	}

	@Test(expected = NotFoundException.class)
	public void deleteNonexistingCustomizationTest()
	{
		//when
		controller.deleteCustomization(CATALOG, CATALOG_VERSION, NONEXISTING_CUSTOMIZATION);
	}

	@Test
	public void getAllVariationsForCustomizationTest()
	{
		//when
		VariationListWsDTO variations = controller.getVariations(CATALOG, CATALOG_VERSION, CUSTOMIZATION);

		//then
		assertNotNull(variations);
		assertNotNull(variations.getVariations());
		assertEquals(10, variations.getVariations().size());
	}

	@Test
	public void getVariationByIdFroCustomizationTest()
	{
		//when
		VariationData variation = controller.getVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION);

		//then
		assertNotNull(variation);
		assertEquals(VARIATION, variation.getCode());
	}

	@Test(expected = NotFoundException.class)
	public void getNonexistingVariationByIdTest()
	{
		//when
		controller.getVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, NONEXISTING_VARIATION);
	}

	@Test(expected = NotFoundException.class)
	public void getVariationByIdFromInvalidCustomizationTest()
	{
		//when
		controller.getVariation(CATALOG, CATALOG_VERSION, NONEXISTING_CUSTOMIZATION, VARIATION);
	}

	@Test
	public void createVariationInCustomizationTest()
	{
		//given
		VariationData dto = new VariationData();
		dto.setName(NONEXISTING_VARIATION_NAME);
		dto.setRank(ONE);
		dto.setEnabled(Boolean.TRUE);

		//when
		ResponseEntity<VariationData> response = controller.createVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, dto,
				getUriComponentsBuilder());

		//then
		VariationData body = response.getBody();
		assertNotNull(body);
		assertNotNull(body.getCode());
		assertEquals(dto.getCode(), body.getCode());
		assertLocationWithCatalog("customizations/" + CUSTOMIZATION + "/variations/" + body.getCode(), response);

		VariationData variation = controller.getVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, body.getCode());
		assertNotNull(variation);
		assertEquals(body.getCode(), variation.getCode());
		assertEquals(Boolean.TRUE, variation.isEnabled());
		assertEquals(dto.getRank(), variation.getRank());

		VariationListWsDTO variations = controller.getVariations(CATALOG, CATALOG_VERSION, CUSTOMIZATION);
		Set<Integer> rankSet = variations.getVariations().stream().map(c -> c.getRank()).collect(Collectors.toSet());
		assertEquals("Variation rank was not updated properly.", variations.getVariations().size(), rankSet.size());
	}

	@Test(expected = AlreadyExistsException.class)
	public void createExisitngVariationInCustomizationTest()
	{
		//given
		VariationData dto = new VariationData();
		dto.setCode(VARIATION);
		dto.setName(VARIATION_NAME);
		dto.setRank(ONE);
		dto.setEnabled(Boolean.TRUE);

		//	when
		controller.createVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, dto, getUriComponentsBuilder());
	}

	@Test
	public void updateVariationTest()
	{
		//given
		VariationData dto = new VariationData();
		dto.setCode(VARIATION);
		dto.setName(VARIATION_NAME);
		dto.setRank(Integer.valueOf(5));
		dto.setEnabled(Boolean.FALSE);

		//when
		VariationData updateVariation = controller.updateVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION, dto);

		//then
		assertNotNull(updateVariation);
		assertEquals(dto.getRank(), updateVariation.getRank());
		assertEquals(dto.isEnabled(), updateVariation.isEnabled());

		VariationData result = controller.getVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION);
		assertNotNull(result);
		assertEquals(dto.getRank(), result.getRank());
		assertEquals(dto.isEnabled(), result.isEnabled());
	}

	@Test(expected = CodeConflictException.class)
	public void updateVariationWithInconsistentCodeTest()
	{
		//given
		VariationData dto = new VariationData();
		dto.setCode(VARIATION);
		dto.setName(VARIATION_NAME);
		dto.setRank(TWO);
		dto.setEnabled(Boolean.FALSE);

		//when
		controller.updateVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, NONEXISTING_VARIATION, dto);
	}

	@Test(expected = NotFoundException.class)
	public void updateNonexistingVariationTest()
	{
		//given
		VariationData dto = new VariationData();
		dto.setCode(NONEXISTING_VARIATION);
		dto.setName(NONEXISTING_VARIATION_NAME);
		dto.setRank(TWO);
		dto.setEnabled(Boolean.FALSE);

		//when
		controller.updateVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, NONEXISTING_VARIATION, dto);
	}

	@Test
	public void deleteVariationTest()
	{
		//when
		controller.deleteVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION);

		//then
		try
		{
			controller.getVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION);
			fail("Variation should be deleted");
		}
		catch (NotFoundException e)
		{
			//ok
		}
	}

	@Test(expected = NotFoundException.class)
	public void deleteNonexisingVariationTest()
	{
		controller.deleteVariation(CATALOG, CATALOG_VERSION, CUSTOMIZATION, NONEXISTING_VARIATION);
	}
}
