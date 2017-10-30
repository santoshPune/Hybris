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
package de.hybris.platform.previewpersonalizationweb;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertBadRequest;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertCreated;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertOk;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.previewwebservices.constants.PreviewwebservicesConstants;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@NeedsEmbeddedServer(webExtensions =
{ PreviewwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
public class PreviewPersonalizationWebServicesTest extends ServicelayerTest
{
	public static final String OAUTH_CLIENT_ID = "mobile_android";
	public static final String OAUTH_CLIENT_PASS = "secret";

	public static final String CATALOG = "testCatalog";
	public static final String CATALOG_VERSION = "Online";
	public static final String RESOURCE_PATH = "https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite";
	public static final String VARIATION1_CODE = "variation1";
	public static final String VARIATION2_CODE = "variation2";
	public static final String WRONG_VARIATION1_CODE = "wrongVariationCode1";
	public static final String WRONG_VARIATION2_CODE = "wrongVariationCode2";
	public static final String CUSTOMIZATION_CODE = "customization1";

	private static final String URI = "v1/preview";

	private WsSecuredRequestBuilder wsSecuredRequestBuilder;

	@Resource(name = "jsonHttpMessageConverter")
	private Jaxb2HttpMessageConverter jsonHttpMessageConverter;

	@Before
	public void setUp() throws Exception
	{
		wsSecuredRequestBuilder = new WsSecuredRequestBuilder()//
				.extensionName(PreviewwebservicesConstants.EXTENSIONNAME)//
				.path(URI)//
				.client(OAUTH_CLIENT_ID, OAUTH_CLIENT_PASS);

		createCoreData();
		createDefaultCatalog();

		importCsv("/previewpersonalizationweb/test/democustomer-data.impex", "utf-8");
	}

	Set<CxVariationKey> createKeys(final String... variations)
	{
		final Set<CxVariationKey> result = new HashSet<>();

		for (final String v : variations)
		{
			final CxVariationKey k = new CxVariationKey();
			k.setVariationCode(v);
			k.setCustomizationCode(CUSTOMIZATION_CODE);
			result.add(k);
		}

		return result;
	}

	@Test
	public void testPostPreviewWithVariationsFields() throws IOException
	{
		final Set<CxVariationKey> variationCodes = createKeys(VARIATION1_CODE, VARIATION2_CODE);

		//given a preview ticket DTO with variation codes list
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());
		previewTicket.setVariations(variationCodes);

		//when calling POST for creating new preview ticket
		final Response createdResponse = createPreviewTicket(previewTicket);

		//then
		assertCreated(createdResponse, false);
		assertTrue("POST response should contain entity", createdResponse.hasEntity());

		final PreviewTicketWsDTO createdPreviewTicket = createdResponse.readEntity(PreviewTicketWsDTO.class);
		assertPreviewTicketEquals(previewTicket, createdPreviewTicket);

		final Response createdResponseOnceAgain = getPreviewTicket(createdPreviewTicket.getTicketId());

		assertOk(createdResponseOnceAgain, false);
		assertTrue("GET response should contain entity", createdResponseOnceAgain.hasEntity());

		final PreviewTicketWsDTO newlyCreatedPreviewTicket = createdResponseOnceAgain.readEntity(PreviewTicketWsDTO.class);
		assertPreviewTicketEquals(createdPreviewTicket, newlyCreatedPreviewTicket);
	}

	@Test
	public void testPostPreviewWithWrongVariationsFields() throws IOException
	{
		final Set<CxVariationKey> wrongVariationCodes = createKeys(WRONG_VARIATION1_CODE, WRONG_VARIATION2_CODE);

		//given a preview ticket DTO with wrong variation codes list
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());
		previewTicket.setVariations(wrongVariationCodes);

		//when calling POST for creating new preview ticket
		final Response response = createPreviewTicket(previewTicket);

		//then
		assertBadRequest(response, false);
		assertTrue("POST response should contain entity", response.hasEntity());

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertConversionError(errors, wrongVariationCodes);
	}

	@Test
	public void testPutPreviewWithVariationsFields() throws IOException
	{
		final Set<CxVariationKey> variationCodes = createKeys(VARIATION1_CODE, VARIATION2_CODE);

		//given a preview ticket DTO with default fields
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());

		//	and created preview ticket after POST call
		final Response createResponse = createPreviewTicket(previewTicket);

		final PreviewTicketWsDTO newPreviewTicket = createResponse.readEntity(PreviewTicketWsDTO.class);

		// 	and created new preview ticket DTO based on POST response entity
		final PreviewTicketWsDTO previewTicketForUpdate = createPreviewTicketDTOFor(Optional.of(newPreviewTicket));
		//	with list of variation codes
		previewTicketForUpdate.setVariations(variationCodes);

		//when calling PUT for updating existing preview ticket
		final Response response = updatePreviewTicket(previewTicketForUpdate);

		//then
		assertOk(response, false);
		assertTrue("PUT response should contain entity", response.hasEntity());

		final PreviewTicketWsDTO updatedPreviewTicket = response.readEntity(PreviewTicketWsDTO.class);
		assertPreviewTicketEquals(previewTicketForUpdate, updatedPreviewTicket);
	}

	@Test
	public void testPutPreviewWithNotExistingVariationsFields() throws IOException
	{
		final Set<CxVariationKey> wrongVariationCodes = createKeys(WRONG_VARIATION1_CODE, WRONG_VARIATION2_CODE);

		//given a preview ticket DTO with default fields
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());

		//	and created preview ticket after POST call
		final Response createResponse = createPreviewTicket(previewTicket);

		final PreviewTicketWsDTO newPreviewTicket = createResponse.readEntity(PreviewTicketWsDTO.class);

		// 	and created new preview ticket DTO based on POST response entity
		final PreviewTicketWsDTO previewTicketForUpdate = createPreviewTicketDTOFor(Optional.of(newPreviewTicket));
		//	with list of wrong variation codes
		previewTicketForUpdate.setVariations(wrongVariationCodes);

		//when calling PUT for updating existing preview ticket
		final Response response = updatePreviewTicket(previewTicketForUpdate);

		//then
		assertBadRequest(response, false);
		assertTrue("PUT response should contain entity", response.hasEntity());

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertConversionError(errors, wrongVariationCodes);
	}

	@Test
	public void testPutPreviewWithMixedVariationsFields() throws IOException
	{
		final Set<CxVariationKey> wrongVariationCodes = createKeys(WRONG_VARIATION1_CODE, WRONG_VARIATION2_CODE);
		final Set<CxVariationKey> variationCodes = createKeys(VARIATION1_CODE, VARIATION2_CODE);

		final Set<CxVariationKey> mixedVariationCodesList = Sets.union(variationCodes, wrongVariationCodes);

		//given a preview ticket DTO with default fields
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());

		//	and created preview ticket after POST call
		final Response createResponse = createPreviewTicket(previewTicket);

		final PreviewTicketWsDTO newPreviewTicket = createResponse.readEntity(PreviewTicketWsDTO.class);

		// 	and created new preview ticket DTO based on POST response entity
		final PreviewTicketWsDTO previewTicketForUpdate = createPreviewTicketDTOFor(Optional.of(newPreviewTicket));
		//	with list of mixed variation codes (correct and wrong ones)
		previewTicketForUpdate.setVariations(mixedVariationCodesList);

		//when calling PUT for updating existing preview ticket
		final Response response = updatePreviewTicket(previewTicketForUpdate);

		//then
		assertBadRequest(response, false);
		assertTrue("PUT response should contain entity", response.hasEntity());

		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertConversionError(errors, wrongVariationCodes);
	}

	@Test
	public void testPutPreviewWithNoVariationsFields() throws IOException
	{
		//given a preview ticket with default fields
		final PreviewTicketWsDTO previewTicket = createPreviewTicketDTOFor(Optional.empty());

		//	and created preview ticket after POST call
		final Response createResponse = createPreviewTicket(previewTicket);

		final PreviewTicketWsDTO resultDTO = createResponse.readEntity(PreviewTicketWsDTO.class);

		// 	and created new preview ticket DTO based on POST response entity
		final PreviewTicketWsDTO previewTicketForUpdate = createPreviewTicketDTOFor(Optional.of(resultDTO));

		//when calling PUT for updating existing preview ticket
		final Response response = updatePreviewTicket(previewTicketForUpdate);

		//then
		assertOk(response, false);
		assertTrue("PUT response should contain entity", response.hasEntity());

		final PreviewTicketWsDTO updatedPreviewTicket = response.readEntity(PreviewTicketWsDTO.class);
		assertPreviewTicketEquals(resultDTO, updatedPreviewTicket);
	}

	private PreviewTicketWsDTO createPreviewTicketDTOFor(final Optional<PreviewTicketWsDTO> basePreviewTicket)
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();

		if (basePreviewTicket.isPresent())
		{
			previewWsDTO.setTicketId(basePreviewTicket.get().getTicketId());
			previewWsDTO.setCatalog(basePreviewTicket.get().getCatalog());
			previewWsDTO.setCatalogVersion(basePreviewTicket.get().getCatalogVersion());
			previewWsDTO.setResourcePath(basePreviewTicket.get().getResourcePath());
		}
		else
		{
			previewWsDTO.setCatalog(CATALOG);
			previewWsDTO.setCatalogVersion(CATALOG_VERSION);
			previewWsDTO.setResourcePath(RESOURCE_PATH);
		}
		return previewWsDTO;
	}

	private void assertPreviewTicketEquals(final PreviewTicketWsDTO expectedPreviewTicket,
			final PreviewTicketWsDTO actualPreviewTicket)
	{
		assertEquals("Wrong catalog for preview ticket", expectedPreviewTicket.getCatalog(), actualPreviewTicket.getCatalog());
		assertEquals("Wrong catalog version for preview ticket", expectedPreviewTicket.getCatalogVersion(),
				actualPreviewTicket.getCatalogVersion());
		assertEquals("Wrong resource path for preview ticket", expectedPreviewTicket.getResourcePath(),
				actualPreviewTicket.getResourcePath());
		assertEquals("Wrong variations collection for preview ticket", expectedPreviewTicket.getResourcePath(),
				actualPreviewTicket.getResourcePath());
	}

	private void assertConversionError(final ErrorListWsDTO errors, final Collection<CxVariationKey> wrongVariationCodes)
	{
		assertNotNull("Errors list should not be null", errors.getErrors());
		assertEquals("Errors list should contain only one element", 1, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("Error should be of type ConversionError", "ConversionError", error1.getType());
		wrongVariationCodes.stream().forEach(varCode -> {
			final String message = String.format("Error message should contain wrong variation code %s", createKeyMessage(varCode));
			assertTrue(message, error1.getMessage().contains(createKeyMessage(varCode)));
		});
	}

	private String createKeyMessage(final CxVariationKey key)
	{
		return "[" + key.getCustomizationCode() + " , " + key.getVariationCode() + "]";
	}

	private Response getPreviewTicket(final String previewTicketId)
	{

		return wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.path(previewTicketId)//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.get();
	}

	private Response createPreviewTicket(final PreviewTicketWsDTO previewWsDTO)
	{

		return wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));
	}

	private Response updatePreviewTicket(final PreviewTicketWsDTO ticketToPut)
	{

		return wsSecuredRequestBuilder.path(ticketToPut.getTicketId())//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(ticketToPut, MediaType.APPLICATION_JSON));
	}

}
