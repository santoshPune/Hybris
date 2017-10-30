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

/**
 * Defines the synchronous HTTP methods for CmsWebServices WS Tests.
 * 
 * @see {@link ApiClient}
 */
public interface ApiClientInvocation
{

	/**
	 * Retrieve a representation by doing a GET
	 * @param clazz the class type of the object to be returned.
	 * @param <R> the object type
	 * @return the resulting object
	 * @throws Exception
	 */
	<R> Response<R> get(Class<R> clazz) throws Exception;

	/**
	 * Retrieve a resource by GETing the given object, and returns the representation found in the
	 * response.<br/>
	 * This should <b><em>only</em></b> be used for GET <b>failure</b> test cases.
	 *
	 * @param clazz
	 *           the class type of the response's representation.
	 * @param <T>
	 *           the object type expected by GET
	 * @param <R>
	 *           the object type to be returned.
	 * @return the representation found on the given resource.
	 * @throws Exception
	 */
	<T, R> Response<R> get(Class<T> clazz, Class<R> errorClazz) throws Exception;

	/**
	 * Create or update a resource by PUTting the given object to the URI.
	 * @param entity the object to PUT
	 * @param <T> The object type
	 * @throws Exception
	 */
	<T> Response<Void> put(T entity) throws Exception;

	/**
	 * Update a resource by PUTing the given object to the URI, and returns the representation found
	 * in the response.
	 *
	 * @param entity
	 *           the object to be created
	 * @param clazz
	 *           the class type of the response's representation.
	 * @param <T>
	 *           the object type to be created
	 * @param <R>
	 *           the object type to be returned.
	 * @return the representation found on the given resource.
	 * @throws Exception
	 */
	<T, R> Response<R> put(T entity, Class<R> clazz) throws Exception;

	/**
	 * Create a new resource by POSTing the given object to the URI template, and returns the
	 * representation found in the response.
	 *
	 * @param entity
	 *           the object to be created
	 * @param clazz
	 *           the class type of the response's representation.
	 * @param <T>
	 *           the object type to be created
	 * @param <R>
	 *           the object type to be returned.
	 * @return the representation found on the given resource.
	 * @throws Exception
	 */
	<T, R> Response<R> post(T entity, Class<R> clazz) throws Exception;

	/**
	 * Create a new resource by POSTing the given object to the URI template, and returns the representation found in the response.
	 * @param entity the object to be created
	 * @param <T> the object type to be created
	 * @return the representation found on the given resource.
	 * @throws Exception
	 */
	<T> Response<Void> post(T entity) throws Exception;

	/**
	 * Delete a resource by sending a DELETE HTTP request
	 *
	 * @return the representation found on the given resource.
	 * @throws Exception
	 */
	Response<Void> delete() throws Exception;

    /**
     * Delete a resource and return any response body response.
     * <br/>
     * This should <b><em>only</em></b> be used for DELETE <b>failure</b> test cases.
     *
     * @param errorClazz
     *           the class type of the response's representation.
     * @param <T>
     *           the error type
     * @return the representation found on the given resource.
     * @throws Exception
     */
    <T> Response<T> delete(Class<T> errorClazz) throws Exception;

}
