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
package de.hybris.platform.cmswebservices.util.apiclient.impl;

import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.util.apiclient.AbstractApiClientInvocation;
import de.hybris.platform.cmswebservices.util.apiclient.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Basic ApiClient implementation using Spring technology.
 */
public class SpringApiClientInvocation extends AbstractApiClientInvocation
{

	private List<HttpMessageConverter<?>> messageConverters;
	private String targetUrl;

	@Override
	public <R> Response<R> get(final Class<R> clazz) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();
		final HttpHeaders httpHeaders = getHttpHeaders();
		try
		{
			final ResponseEntity<R> responseEntity = restTemplate.exchange(targetUrl + getEndpoint(), HttpMethod.GET,
					new HttpEntity<String>(httpHeaders), clazz, getParameters());

			return response(responseEntity.getBody(), responseEntity.getStatusCode().value(),
					responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, createErrorResponse(clazz, e));
		}
	}

	@Override
	public <T, R> Response<R> get(final Class<T> clazz, final Class<R> errorClazz) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();
		final HttpHeaders httpHeaders = getHttpHeaders();
		try
		{
			final ResponseEntity<T> responseEntity = restTemplate.exchange(targetUrl + getEndpoint(), HttpMethod.GET,
					new HttpEntity<String>(httpHeaders), clazz, getParameters());

			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, createErrorResponse(errorClazz, e));
		}
	}

	@Override
	public <T> Response<Void> put(final T entity) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();
		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(entity, getHttpHeaders());
			final ResponseEntity<Void> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestEntity,
					Void.class, getParameters());

			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, null);
		}
	}

	@Override
	public <T, R> Response<R> put(final T entity, final Class<R> clazz) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();
		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(entity, getHttpHeaders());
			final ResponseEntity<Void> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestEntity,
					Void.class, getParameters());

			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, createErrorResponse(clazz, e));
		}
	}

	@Override
	public <T, R> Response<R> post(final T entity, final Class<R> clazz) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();

		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(entity, getHttpHeaders());
			final ResponseEntity<R> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.POST, requestEntity, clazz,
					getParameters());


			return response(responseEntity.getBody(), responseEntity.getStatusCode().value(),
					responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, createErrorResponse(clazz, e));
		}
	}

	private <R> R createErrorResponse(final Class<R> clazz, final HttpStatusCodeException e)
			throws IOException, JsonParseException, JsonMappingException
	{
		final ObjectMapper mapper = new ObjectMapper();
		final R errorResponse = mapper.readValue(e.getResponseBodyAsString(), clazz);
		return errorResponse;
	}

	@Override
	public <T> Response<Void> post(final T entity) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();
		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(entity, getHttpHeaders());
			final ResponseEntity<Void> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.POST, requestEntity,
					Void.class, getParameters());


			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, null);
		}
	}

	@Override
	public Response<Void> delete() throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();

		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(getHttpHeaders());
			final ResponseEntity<Void> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestEntity,
					Void.class, getParameters());
			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, null);
		}
	}

	@Override
	public <T> Response<T> delete(Class<T> errorClazz) throws Exception
	{
		final RestTemplate restTemplate = getRestTemplate();

		try
		{
			final String resourceUrl = targetUrl + getEndpoint();
			final HttpEntity<T> requestEntity = new HttpEntity<T>(getHttpHeaders());
			final ResponseEntity<Void> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, requestEntity,
					Void.class, getParameters());
			return response(null, responseEntity.getStatusCode().value(), responseEntity.getHeaders().toSingleValueMap(), null);
		}
		catch (HttpClientErrorException | HttpServerErrorException e)
		{
			return response(null, e.getStatusCode().value(), null, createErrorResponse(errorClazz, e));
		}
	}

	private RestTemplate getRestTemplate()
	{
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(this.messageConverters);
		return restTemplate;
	}

	private HttpHeaders getHttpHeaders()
	{
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Arrays.asList(MediaType.valueOf(getAccept())));
		if (!ArrayUtils.isEmpty(getAcceptLanguages()))
		{
			httpHeaders.put(HttpHeaders.ACCEPT_LANGUAGE, Arrays.asList(Arrays.stream(getAcceptLanguages()).map(
							locale -> locale.getLanguage()).collect(Collectors.joining(","))));
		}
		getHeaders().entrySet().stream().forEach(e -> {
			httpHeaders.put(e.getKey(), (List) e.getValue());
		});
		return httpHeaders;
	}


	public void setMessageConverters(final List<HttpMessageConverter<?>> messageConverters)
	{
		this.messageConverters = messageConverters;
	}

	public void setTargetUrl(final String targetUrl)
	{
		this.targetUrl = targetUrl;
	}

	@Override
	public AbstractApiClientInvocation cloneClientInvocation()
	{
		final SpringApiClientInvocation springApiClient = new SpringApiClientInvocation();
		springApiClient.messageConverters = this.messageConverters;
		springApiClient.targetUrl = this.targetUrl;
		return springApiClient;
	}
}
