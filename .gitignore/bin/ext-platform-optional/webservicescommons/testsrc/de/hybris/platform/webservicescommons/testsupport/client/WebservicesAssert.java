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
package de.hybris.platform.webservicescommons.testsupport.client;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;


public class WebservicesAssert
{
	/**
	 * Standard headers that need to be present across hybris webservices.
	 */
	public static final Map<String, String> SECURED_HEADERS;
	static
	{
		final Map<String, String> localSecuredHeaders = new HashMap<>();
		localSecuredHeaders.put("X-FRAME-Options", "SAMEORIGIN");
		localSecuredHeaders.put("X-XSS-Protection", "1; mode=block");
		localSecuredHeaders.put("X-Content-Type-Options", "nosniff");
		localSecuredHeaders.put("Strict-Transport-Security", "max-age=16070400 ; includeSubDomains");
		SECURED_HEADERS = Collections.unmodifiableMap(localSecuredHeaders);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertOk(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.OK, response, expectEmptyBody);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertCreated(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.CREATED, response, expectEmptyBody);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertForbidden(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.FORBIDDEN, response, expectEmptyBody);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertBadRequest(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.BAD_REQUEST, response, expectEmptyBody);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertUnauthorized(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.UNAUTHORIZED, response, expectEmptyBody);
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertResponseStatus(final Status expectedStatus, final Response response, final boolean expectEmptyBody)
	{
		assertEquals("Wrong HTTP status at response: " + response, expectedStatus.getStatusCode(), response.getStatus());
		if (expectEmptyBody)
		{
			Assert.assertTrue("Body should be empty at response: " + response, !response.hasEntity());
		}
	}

	/**
	 * @deprecated use assertResponse instead
	 */
	@Deprecated
	public static void assertResponseStatus(final Status expectedStatus, final Response response)
	{
		assertResponseStatus(expectedStatus, response, false);
	}



	/**
	 * Assert response status and verify that no body is there if expectEmptyBody is true. This will also check the
	 * response against the expected headers passed in parameters.
	 *
	 * @param expectedStatus
	 *           expected status
	 * @param response
	 *           response to test
	 *
	 * @param expectedHeaders
	 *           headers that should be present in the response header
	 */
	public static void assertResponse(final Status expectedStatus, final Optional<Map<String, String>> expectedHeaders,
			final Response response)
	{
		assertEquals("Wrong HTTP status at response: " + response, expectedStatus.getStatusCode(), response.getStatus());
		if (expectedHeaders.isPresent())
		{
			for (final Entry<String, String> header : expectedHeaders.get().entrySet())
			{
				assertEquals(header.getValue(), response.getHeaderString(header.getKey()));
			}
		}
	}

	/**
	 * Assert response status and verify that no body is there if expectEmptyBody is true. This will also check the
	 * response against basic security headers.
	 *
	 * @param expectedStatus
	 *           expected status
	 * @param response
	 *           response to test
	 *
	 */
	public static void assertResponse(final Status expectedStatus, final Response response)
	{
		assertResponse(expectedStatus, Optional.of(SECURED_HEADERS), response);
	}


}
