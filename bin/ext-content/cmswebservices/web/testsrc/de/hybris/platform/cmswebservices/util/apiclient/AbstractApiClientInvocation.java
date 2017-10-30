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
 */
package de.hybris.platform.cmswebservices.util.apiclient;

import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;


/**
 * There are two reasons why this abstract class exists. One is to provide the necessary attributes for creating the new
 * HTTP request, and the second reason is to provide an interface for cloning the object extending this class. Cloning
 * subclasses will be important for concurrent access to the same implementation class, and it will be used as a
 * Prototype strategy by {@link ApiClient}.
 */
public abstract class AbstractApiClientInvocation implements ApiClientInvocation
{
	private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
	private MultivaluedMap<String, Object> parameters = new MultivaluedHashMap<>();
	private String endpoint;
	private String accept;
	private Locale[] acceptLanguages;


	public MultivaluedMap<String, Object> getHeaders()
	{
		return headers;
	}

	public String getAccept()
	{
		return accept;
	}

	public String getEndpoint()
	{
		return endpoint;
	}

	public void setAccept(final String accept)
	{
		this.accept = accept;
	}

	public void setEndpoint(final String endpoint)
	{
		this.endpoint = endpoint;
	}

	public Locale[] getAcceptLanguages()
	{
		return acceptLanguages;
	}

	public void setAcceptLanguages(final Locale[] acceptLanguages)
	{
		this.acceptLanguages = acceptLanguages;
	}

	public void setHeaders(final MultivaluedMap<String, Object> headers)
	{
		this.headers = headers;
	}

	public MultivaluedMap<String, Object> getParameters()
	{
		return parameters;
	}

	public void setParameters(final MultivaluedMap<String, Object> parameters)
	{
		this.parameters = parameters;
	}

	public <T> Response<T> response(final T body, final int status, final Map<String, String> headers, final T errorResponse)
	{
		return new Response<T>()
		{
			@Override
			public int getStatus()
			{
				return status;
			}

			@Override
			public T getBody()
			{
				return body;
			}

			@Override
			public Map getHeaders()
			{
				return headers;
			}

			@Override
			public T getErrorResponse()
			{
				return errorResponse;
			}
		};
	}

	/**
	 * Sub classes must implement a clone strategy in order to prevent cross references between different requests. The
	 * level of the clone strategy (deep/shallow) will depend on the case, but it is extremely necessary to clone, or
	 * have a new reference, to the attributes defined in the {@link AbstractApiClientInvocation}.
	 *
	 * @return a cloned instance of the implementation class.
	 */
	public abstract AbstractApiClientInvocation cloneClientInvocation();
}
