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
package de.hybris.platform.personalizationcmsweb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.personalizationcmsweb.data.CxCmsActionData;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationwebservices.BaseWebServiceTest;
import de.hybris.platform.personalizationwebservices.constants.PersonalizationwebservicesConstants;
import de.hybris.platform.personalizationwebservices.data.ActionListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.io.IOException;
import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
@NeedsEmbeddedServer(webExtensions =
{ PersonalizationwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
public class CmsActionsWebServiceTest extends BaseWebServiceTest
{
	private static final String CUSTOMIZATION_ENDPOINT = VERSION + "/catalogs/testCatalog/catalogVersions/Online/customizations";

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		importCsv("/personalizationcmsweb/test/webcontext_testdata.impex", "utf-8");
	}

	@Test
	public void getCmsActionForCmsManager() throws IOException
	{
		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get();


		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		final ActionData action = response.readEntity(ActionData.class);
		assertNotNull(action);
		assertEquals(ACTION, action.getCode());
	}

	@Test
	public void getCmsActionForAdmin() throws IOException
	{
		//when
		final Response response = getWsSecuredRequestBuilderForAdmin()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);

		final ActionData action = response.readEntity(ActionData.class);
		assertNotNull(action);
		assertEquals(ACTION, action.getCode());
	}


	@Test
	public void createCmsAction() throws IOException
	{
		//given
		final String newActionCode = "newAction";
		final String newActionComponentId = "newComponent";

		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "cxCmsActionData");
		actionAttributes.put("code", newActionCode);
		actionAttributes.put("componentId", newActionComponentId);
		actionAttributes.put("containerId", CONTAINER);

		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.build()//
				.post(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CREATED, response);
		final String location = response.getHeaderString("Location");
		assertTrue(location.contains("newAction"));
		CxCmsActionData action = response.readEntity(CxCmsActionData.class);
		assertEquals(newActionCode, action.getCode());
		assertEquals(newActionComponentId, action.getComponentId());
		assertEquals(CONTAINER, action.getContainerId());


		action = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path("newAction")//
				.build()//
				.get(CxCmsActionData.class);
		assertEquals(newActionCode, action.getCode());
		assertEquals(newActionComponentId, action.getComponentId());
		assertEquals(CONTAINER, action.getContainerId());
	}

	@Test
	public void createAutomaticCmsAction() throws IOException
	{
		//given
		final String newActionComponentId = "newComponent";

		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "cxCmsActionData");
		actionAttributes.put("componentId", newActionComponentId);
		actionAttributes.put("containerId", CONTAINER);

		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.build()//
				.post(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CREATED, response);
		final CxCmsActionData action = response.readEntity(CxCmsActionData.class);
		assertEquals(newActionComponentId, action.getComponentId());
		assertEquals(CONTAINER, action.getContainerId());


		final CxCmsActionData getAction = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(action.getCode())//
				.build()//
				.get(CxCmsActionData.class);
		assertEquals(action.getCode(), getAction.getCode());
		assertEquals(newActionComponentId, getAction.getComponentId());
		assertEquals(CONTAINER, getAction.getContainerId());
	}


	@Test
	public void updateCmsActionTypeWithOtherType() throws IOException
	{
		//give
		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "testActionData");


		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.put(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CONFLICT, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("TypeConflictError", error1.getType());
	}

	@Test
	public void updateCmsActionTypeWithUnknownType() throws IOException
	{
		//give
		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "unknowntype");


		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.put(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.BAD_REQUEST, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("HttpMessageNotReadableError", error1.getType());
	}

	@Test
	public void updateCmsAction() throws IOException
	{
		//give
		final String newActionComponentId = "newComponent";

		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "cxCmsActionData");
		actionAttributes.put("componentId", newActionComponentId);
		actionAttributes.put("containerId", CONTAINER);


		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.put(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		final CxCmsActionData action = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get(CxCmsActionData.class);

		assertEquals(ACTION, action.getCode());
		assertEquals(newActionComponentId, action.getComponentId());
	}

	@Test
	public void deleteCmsAction() throws IOException
	{
		//given
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.delete();

		//then
		WebservicesAssert.assertResponse(Status.NO_CONTENT, response);

		final Response result = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get();
		WebservicesAssert.assertResponse(Status.NOT_FOUND, result);
		final ErrorListWsDTO errors = result.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());
	}

	@Test
	public void getAllActionsForVariationTest()
	{
		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		final ActionListWsDTO actions = response.readEntity(ActionListWsDTO.class);
		//controller.getActions(CATALOG, CATALOG_VERSION, CUSTOMIZATION, VARIATION);

		assertNotNull(actions);
		assertNotNull(actions.getActions());
		assertEquals(5, actions.getActions().size());
	}

	@Test
	public void getNonexistingActionByIdTest()
	{
		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(NON_EXISTINGACTION)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());
	}

	@Test
	public void getActionByIdFromInvalidVariationTest()
	{

		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(NONEXISTING_VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());

	}

	@Test
	public void getActionByIdFromInvalidCustomizationTest()
	{
		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(NONEXISTING_CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.get();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());
	}

	@Test
	public void createExistingCmsAction() throws IOException
	{
		//given
		final String newActionComponentId = "newComponent";

		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "cxCmsActionData");
		actionAttributes.put("code", ACTION);
		actionAttributes.put("componentId", newActionComponentId);
		actionAttributes.put("containerId", CONTAINER);

		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.build()//
				.post(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CONFLICT, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("AlreadyExistsError", error1.getType());
	}

	@Test
	public void updateCmsActionWithInconsidtentCode() throws IOException
	{
		//give
		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("type", "cxCmsActionData");
		actionAttributes.put("code", NON_EXISTINGACTION);


		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.put(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.CONFLICT, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("CodeConflictError", error1.getType());
	}


	@Test
	public void updateNonExistingCmsActionWithInconsidtentCode() throws IOException
	{
		//give
		final HashMap<String, String> actionAttributes = new HashMap<String, String>();
		actionAttributes.put("componentId", "thisisanewcomponent");


		//when
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(NON_EXISTINGACTION)//
				.build()//
				.put(Entity.entity(actionAttributes, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());

	}

	@Test
	public void deleteNonExistingCmsAction() throws IOException
	{
		//given
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(NON_EXISTINGACTION)//
				.build()//
				.delete();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("NotFoundError", error1.getType());
	}

	@Test
	public void deleteCmsActionFromUnknownVariation() throws IOException
	{
		//given
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(NONEXISTING_VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.delete();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
	}

	@Test
	public void deleteCmsActionFromUnknownCustomization() throws IOException
	{
		//given
		final Response response = getWsSecuredRequestBuilderForCmsManager()//
				.path(CUSTOMIZATION_ENDPOINT)//
				.path(NONEXISTING_CUSTOMIZATION)//
				.path(VARIATION_ENDPOINT)//
				.path(VARIATION)//
				.path(ACTION_ENDPOINT)//
				.path(ACTION)//
				.build()//
				.delete();

		//then
		WebservicesAssert.assertResponse(Status.NOT_FOUND, response);
	}


}
