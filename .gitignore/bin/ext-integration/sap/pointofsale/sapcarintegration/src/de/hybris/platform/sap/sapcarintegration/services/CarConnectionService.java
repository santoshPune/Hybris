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
package de.hybris.platform.sap.sapcarintegration.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public interface CarConnectionService {

	public abstract HttpURLConnection createConnection(String absoluteUri, String contentType,
			String httpMethod) throws IOException, MalformedURLException;

}