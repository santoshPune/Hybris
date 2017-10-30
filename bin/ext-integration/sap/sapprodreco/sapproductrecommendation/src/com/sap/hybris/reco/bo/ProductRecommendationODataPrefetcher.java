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
package com.sap.hybris.reco.bo;

import de.hybris.platform.sap.core.odata.util.MyCallback;
import de.hybris.platform.sap.core.odata.util.ODataClientService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;


/**
 * Multithreaded prefetching of Product Recommendation For OData
 *
 */
public class ProductRecommendationODataPrefetcher implements Callable<ODataEntry>
{
	private final static Logger LOG = Logger.getLogger(ProductRecommendationODataPrefetcher.class.getName());
	protected String contentType;
	protected String httpMethod;
	protected String user;
	protected String password;
	protected String serviceUri;
	protected String entitySetName;
	protected ODataClientService clientService;
	protected String client;
	protected Map<String, Object> recommendationScenario;
	protected List<String> entities;
	protected Map<String, Object> headerValues;
	protected MyCallback myCallback;


	/**
	 * @param clientService
	 * @param serviceUri
	 * @param entitySetName
	 * @param contentType
	 * @param httpMethod
	 * @param user
	 * @param password
	 * @param client
	 * @param recommendationScenario
	 * @param entities
	 * @param headerValues
	 * @param myCallback
	 */
	public ProductRecommendationODataPrefetcher(final ODataClientService clientService, final String serviceUri,
			final String entitySetName, final Map<String, Object> recommendationScenario, final String contentType,
			final String httpMethod, final String user, final String password, final String client, final List<String> entities,
			final Map<String, Object> headerValues, final MyCallback myCallback)
	{
		this.clientService = clientService;
		this.entitySetName = entitySetName;
		this.serviceUri = serviceUri;
		this.contentType = contentType;
		this.httpMethod = httpMethod;
		this.user = user;
		this.password = password;
		this.client = client;
		this.recommendationScenario = recommendationScenario;
		this.entities = entities;
		this.headerValues = headerValues;
		this.myCallback = myCallback;
	}

	/**
	 * Backend call for PRI using ODATA to get the product recommendations
	 */
	@Override
	public ODataEntry call() throws Exception
	{
		LOG.debug("\n\n OData Call \n\n");
		return this.clientService.writeEntity(serviceUri, entitySetName, recommendationScenario, contentType, httpMethod, user,
				password, client, entities, headerValues, myCallback);
	}
}
