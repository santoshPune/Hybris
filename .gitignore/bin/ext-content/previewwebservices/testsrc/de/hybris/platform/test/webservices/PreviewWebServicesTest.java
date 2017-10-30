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
package de.hybris.platform.test.webservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.previewwebservices.constants.PreviewwebservicesConstants;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;
import de.hybris.platform.webservicescommons.jaxb.adapters.DateAdapter;
import de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions =
{ PreviewwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
public class PreviewWebServicesTest extends ServicelayerTest
{
	public static final String OAUTH_CLIENT_ID = "mobile_android";
	public static final String OAUTH_CLIENT_PASS = "secret";
	public static final String FIELDS_QUERYPARAM = "fields";

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

		importCsv("/previewwebservices/test/democustomer-data.impex", "utf-8");
	}

	@Test
	public void testPostEmptyEntityForValidationErrors() throws IOException
	{
		// given empty ticket
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();

		// when ticket is posted to the endpoint
		final Response response = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build().post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		// then following errors are returned
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(3, errors.getErrors().size());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("missing", error1.getReason());
		assertEquals("catalog", error1.getSubject());
		assertEquals("parameter", error1.getSubjectType());
		assertEquals("ValidationError", error1.getType());

		final ErrorWsDTO error2 = errors.getErrors().get(1);
		assertEquals("missing", error2.getReason());
		assertEquals("catalogVersion", error2.getSubject());
		assertEquals("parameter", error2.getSubjectType());
		assertEquals("ValidationError", error2.getType());

		final ErrorWsDTO error3 = errors.getErrors().get(2);
		assertEquals("missing", error3.getReason());
		assertEquals("resourcePath", error3.getSubject());
		assertEquals("parameter", error3.getSubjectType());
		assertEquals("ValidationError", error3.getType());

	}

	@Test
	public void testPostForTicketMinimum() throws IOException
	{
		// given ticket with required values only
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");

		// when such a ticket is posted
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		// then ticket is created and contains sent values
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);
		assertNotNull(resultDTO);
		assertTrue(StringUtils.isNotEmpty(resultDTO.getTicketId()));
		assertEquals("testCatalog", resultDTO.getCatalog());
		assertEquals("Online", resultDTO.getCatalogVersion());
		assertEquals("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite", resultDTO.getResourcePath());
		assertNull(resultDTO.getLanguage());
		assertNull(resultDTO.getUser());
		assertNull(resultDTO.getUserGroup());
		assertNull(resultDTO.getTime());
		assertNull(resultDTO.getPageId());
	}

	@Test
	public void testPostWrongCatalog() throws IOException
	{
		// given ticket with wrong catalog
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testwrongcatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");

		// when such a ticket is posted
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		// then ticket is created and contains sent values
		WebservicesAssert.assertResponse(Status.BAD_REQUEST, result);

	}

	@Test
	public void testPostForTicketWithPage() throws IOException
	{
		// given ticket with required values only
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		previewWsDTO.setPageId("homepage");

		// when such a ticket is posted
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		// then ticket is created and contains sent values
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);
		assertNotNull(resultDTO);
		assertTrue(StringUtils.isNotEmpty(resultDTO.getTicketId()));
		assertEquals("testCatalog", resultDTO.getCatalog());
		assertEquals("Online", resultDTO.getCatalogVersion());
		assertEquals("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite", resultDTO.getResourcePath());
		assertEquals("homepage", resultDTO.getPageId());
	}

	@Test
	public void testPostForTicketWithEverything() throws IOException, JAXBException
	{
		// given ticket with all values
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		previewWsDTO.setLanguage("en");
		previewWsDTO.setUser("testoauthcustomer");
		previewWsDTO.setUserGroup("regulargroup");
		previewWsDTO.setPageId("homepage");

		final Date time = DateTimeFormat.forPattern(DateAdapter.DATE_FORMAT).parseDateTime("2013-02-14T13:15:03-0800").toDate();
		previewWsDTO.setTime(time);

		// when such a ticket is posted
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(marshallDto(previewWsDTO, PreviewTicketWsDTO.class), MediaType.APPLICATION_JSON));

		// then ticket is created and contains sent values
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);
		assertNotNull(resultDTO);
		assertTrue(StringUtils.isNotEmpty(resultDTO.getTicketId()));
		assertEquals("testCatalog", resultDTO.getCatalog());
		assertEquals("Online", resultDTO.getCatalogVersion());
		assertEquals("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite", resultDTO.getResourcePath());
		assertEquals("en", resultDTO.getLanguage());
		assertEquals("testoauthcustomer", resultDTO.getUser());
		assertEquals("regulargroup", resultDTO.getUserGroup());
		assertEquals(time.getTime(), resultDTO.getTime().getTime());
		assertEquals("homepage", resultDTO.getPageId());

	}

	@Test
	public void shouldReturn201ForAdmin() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");

		//when posting with admin
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("admin", "nimda")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		//then we receive a 201
		WebservicesAssert.assertResponse(Status.CREATED, result);
	}

	@Test
	public void shouldReturn400ForRandomUser() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");

		//when posting with a user that doesn't belong to cmsmanager group
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("randomuser", "1234")//
				.grantClientCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

		//then we receive a 401
		Assert.assertEquals(401, result.getStatus());
	}

	@Test
	public void shouldReturn201WithSearchRestrictionsEnabled() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, JAXBException
	{
		//yeah, we have to do that because it is a private method...
		final Method method = CatalogManager.class.getDeclaredMethod("createSearchRestrictions", JspContext.class);
		method.setAccessible(true);
		method.invoke(CatalogManager.getInstance(), (JspContext) null);

		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		previewWsDTO.setLanguage("en");
		previewWsDTO.setUser("testoauthcustomer");
		previewWsDTO.setUserGroup("regulargroup");
		final Date time = DateTimeFormat.forPattern(DateAdapter.DATE_FORMAT).parseDateTime("2013-02-14T13:15:03-0800").toDate();
		previewWsDTO.setTime(time);

		// when such a ticket is posted
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(marshallDto(previewWsDTO, PreviewTicketWsDTO.class), MediaType.APPLICATION_JSON));

		// then ticket is created and contains sent values
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);
		assertNotNull(resultDTO);
		assertTrue(StringUtils.isNotEmpty(resultDTO.getTicketId()));
		assertEquals("testCatalog", resultDTO.getCatalog());
		assertEquals("Online", resultDTO.getCatalogVersion());
		assertEquals("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite", resultDTO.getResourcePath());
		assertEquals("en", resultDTO.getLanguage());
		assertEquals("testoauthcustomer", resultDTO.getUser());
		assertEquals("regulargroup", resultDTO.getUserGroup());
		assertEquals(time.getTime(), resultDTO.getTime().getTime());

	}

	@Test
	public void testGetForForPostTicketMinimum() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);

		//when
		final Response response = wsSecuredRequestBuilder.path(resultDTO.getTicketId())//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.get();
		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		final PreviewTicketWsDTO previewTicketWsDTO = response.readEntity(PreviewTicketWsDTO.class);
		assertEquals(resultDTO.getCatalog(), previewTicketWsDTO.getCatalog());
		assertEquals(resultDTO.getCatalogVersion(), previewTicketWsDTO.getCatalogVersion());
		assertEquals(resultDTO.getResourcePath(), previewTicketWsDTO.getResourcePath());
		assertNull(previewTicketWsDTO.getLanguage());
		assertNull(previewTicketWsDTO.getUser());
		assertNull(previewTicketWsDTO.getUserGroup());
		assertNull(previewTicketWsDTO.getTime());
	}

	@Test
	public void testGetForCatalogOnlyForPostTicketMinimum() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);

		//when
		final Response response = wsSecuredRequestBuilder.path(resultDTO.getTicketId())//
				.queryParam(FIELDS_QUERYPARAM, "catalog")//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.get();
		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		final PreviewTicketWsDTO previewTicketWsDTO = response.readEntity(PreviewTicketWsDTO.class);
		assertEquals(resultDTO.getCatalog(), previewTicketWsDTO.getCatalog());
		assertNull(previewTicketWsDTO.getCatalogVersion());
		assertNull(previewTicketWsDTO.getResourcePath());
		assertNull(previewTicketWsDTO.getLanguage());
		assertNull(previewTicketWsDTO.getUser());
		assertNull(previewTicketWsDTO.getUserGroup());
		assertNull(previewTicketWsDTO.getTime());
	}

	@Test
	public void testGetForNotExistingTicket() throws IOException
	{
		//when
		final Response response = wsSecuredRequestBuilder.path("1234")//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
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
	public void testPutForNotExistingTicket() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setTicketId("1234");
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		//when
		final Response response = wsSecuredRequestBuilder.path("1234")//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

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
	public void testPutForCodeConflictExistingTicket() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setTicketId("1234");
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		//when
		final Response response = wsSecuredRequestBuilder.path("12345")//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));

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
	public void testPutBadResourcePathForPreview() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());

		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);
		resultDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite123");

		//when
		final Response response = wsSecuredRequestBuilder.path(resultDTO.getTicketId())//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(resultDTO, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.BAD_REQUEST, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		final ErrorWsDTO error1 = errors.getErrors().get(0);
		assertEquals("ConversionError", error1.getType());
	}


	@Test
	public void testPutPreviewWithNullMandatoryFields() throws IOException
	{
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(previewWsDTO, MediaType.APPLICATION_JSON));
		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());

		final PreviewTicketWsDTO resultDTO = result.readEntity(PreviewTicketWsDTO.class);

		final PreviewTicketWsDTO emptyTicketToPut = new PreviewTicketWsDTO();
		emptyTicketToPut.setTicketId(resultDTO.getTicketId());

		//when
		final Response response = wsSecuredRequestBuilder.path(emptyTicketToPut.getTicketId())//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(emptyTicketToPut, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.BAD_REQUEST, response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(3, errors.getErrors().size());
		errors.getErrors().stream().forEach(e -> {
			assertEquals("ValidationError", e.getType());
			assertEquals("missing", e.getReason());
		});
	}

	@Test
	public void testPutPreviewWithNullNotMandatoryFields() throws IOException, JAXBException
	{
		// given ticket with all values
		final PreviewTicketWsDTO previewWsDTO = new PreviewTicketWsDTO();
		previewWsDTO.setCatalog("testCatalog");
		previewWsDTO.setCatalogVersion("Online");
		previewWsDTO.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite");
		previewWsDTO.setLanguage("en");
		previewWsDTO.setUser("testoauthcustomer");
		previewWsDTO.setUserGroup("regulargroup");
		final Date time = DateTimeFormat.forPattern(DateAdapter.DATE_FORMAT).parseDateTime("2013-02-14T13:15:03-0800").toDate();
		previewWsDTO.setTime(time);

		final Response result = wsSecuredRequestBuilder//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.post(Entity.entity(marshallDto(previewWsDTO, PreviewTicketWsDTO.class), MediaType.APPLICATION_JSON));

		WebservicesAssert.assertResponse(Status.CREATED, result);
		assertTrue(result.hasEntity());
		final PreviewTicketWsDTO createdPreviewTicket = result.readEntity(PreviewTicketWsDTO.class);

		final PreviewTicketWsDTO previewTicketForUpdate = new PreviewTicketWsDTO();
		previewTicketForUpdate.setTicketId(createdPreviewTicket.getTicketId());
		previewTicketForUpdate.setCatalog(createdPreviewTicket.getCatalog());
		previewTicketForUpdate.setCatalogVersion(createdPreviewTicket.getCatalogVersion());
		previewTicketForUpdate.setResourcePath(createdPreviewTicket.getResourcePath());

		//when
		final Response response = wsSecuredRequestBuilder.path(previewTicketForUpdate.getTicketId())//
				.resourceOwner("cmsmanager", "1234")//
				.grantResourceOwnerPasswordCredentials()//
				.build()//
				.put(Entity.entity(previewTicketForUpdate, MediaType.APPLICATION_JSON));

		//then
		WebservicesAssert.assertResponse(Status.OK, response);
		final PreviewTicketWsDTO updatedPreviewTicket = response.readEntity(PreviewTicketWsDTO.class);
		assertEquals("testCatalog", updatedPreviewTicket.getCatalog());
		assertEquals("Online", updatedPreviewTicket.getCatalogVersion());
		assertEquals("https://127.0.0.1:9002/yacceleratorstorefront?site=testCmsSite", updatedPreviewTicket.getResourcePath());
		assertNull(updatedPreviewTicket.getLanguage());
		assertNull(updatedPreviewTicket.getUser());
		assertNull(updatedPreviewTicket.getUserGroup());
	}

	protected String marshallDto(final Object input, final Class c) throws JAXBException
	{
		final Marshaller marshaller = jsonHttpMessageConverter.createMarshaller(c);
		final StringWriter writer = new StringWriter();
		marshaller.marshal(input, writer);
		return writer.toString();
	}
}
