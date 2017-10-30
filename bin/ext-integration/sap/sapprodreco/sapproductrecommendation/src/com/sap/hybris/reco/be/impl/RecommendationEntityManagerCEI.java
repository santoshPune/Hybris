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
package com.sap.hybris.reco.be.impl;

import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.odata.util.ODataClientService;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.hybris.reco.be.RecommendationEntityManager;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;


/**
 * Recommendation Entity Manager CEI
 */
public class RecommendationEntityManagerCEI implements RecommendationEntityManager
{
	private String baseURL;
	protected ODataClientService clientService;
	protected HMCConfigurationReader configuration;

	/**
	 * To load HTTP destination
	 */
	@Override
	public void initBackendObject() throws BackendException
	{

	}

	/**
	 * destroy Backend Object
	 */
	@Override
	public void destroyBackendObject()
	{

	}

	/**
	 * Retrieve the list of model types from a remote server
	 *
	 * @param entityName
	 * @param expand
	 * @param select
	 * @param filter
	 * @param orderby
	 *
	 * @return Returns an ODataFeed with the list of model types
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Override
	public ODataFeed getTypes(final String entityName, final String expand, final String select, final String filter,
			final String orderby) throws ODataException, URISyntaxException, IOException
	{
		final String serviceURL = configuration.getHttpDestinationURL() + baseURL;
		final String client = configuration.getHttpDestinationSAPClient();
		final String user = configuration.getHttpDestination().getUserid();
		final String password = configuration.getHttpDestination().getPassword();

		final ODataFeed feed = this.clientService.readFeed(serviceURL, SapproductrecommendationConstants.APPLICATION_XML, entityName, expand, select, filter,
				orderby, user, password, client);

		return feed;
	}

	/**
	 * @return clientService
	 */
	@Override
	public ODataClientService getClientService()
	{
		return clientService;
	}

	/**
	 * @param clientService
	 */
	@Override
	public void setClientService(final ODataClientService clientService)
	{
		this.clientService = clientService;
	}

	/**
	 * @return configuration
	 */
	@Override
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	/**
	 * @param configuration
	 */
	@Override
	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}

	/**
	 * @return usage
	 */
	public String getUsage()
	{
		return getConfiguration().getUsage();
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL()
	{
		return baseURL;
	}

	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(final String baseURL)
	{
		this.baseURL = baseURL;
	}

}
