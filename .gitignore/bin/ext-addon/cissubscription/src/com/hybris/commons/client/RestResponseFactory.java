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
 */
package com.hybris.commons.client;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 *  Factory creates instance of RestResponse with stub values.
 */
public class RestResponseFactory {
	public static <T> RestResponse<T> newStubInstance() {
	
		final Response response = Response.status(200).build();
		
		return new RestResponse<T>(response) {
			@Override
			public T getResult() {
				return null;
			}
		};
	}
}
