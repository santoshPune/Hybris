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
package com.sap.wec.adtreco.be.impl;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObjectBase;
import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.odata.util.ODataClientService;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import com.sap.wec.adtreco.be.intf.ADTInitiativesBE;

/**
 * 
 */
@BackendType("CEI")
public class ADTInitiativesBeCEIImpl extends BackendBusinessObjectBase implements ADTInitiativesBE
{
	private String serviceURL;
	private static final String EXPAND = "TargetGroup";
	protected String path;
	protected ODataClientService clientService;
	protected HMCConfigurationReader configuration;
	protected String httpDestinationId;
	private static final String APPLICATION_XML = "application/xml";

	public ODataFeed getInitiatives(final String select, final String filter, final String entitySetName, final String expand, String orderBy)
			throws ODataException, URISyntaxException, IOException
	{
		ODataFeed feed = null;
		String serviceURL = configuration.getHttpDestinationURL() + this.serviceURL;
		String client = configuration.getHttpDestinationSAPClient();
		
		feed = this.clientService.readFeed(serviceURL, APPLICATION_XML, entitySetName, expand, 
				select, filter,	orderBy, configuration.getHttpDestination().getUserid(), configuration.getHttpDestination().getPassword(), client);
		return feed;
	}

	public ODataEntry getInitiative(final String select, final String keyValue, final String entitySetName) throws ODataException, IOException, URISyntaxException
	{
		ODataEntry entry = null;
		String serviceURL = configuration.getHttpDestinationURL() + this.serviceURL;
		String client = configuration.getHttpDestinationSAPClient();
		String contentType = APPLICATION_XML;
		String user = configuration.getHttpDestination().getUserid();
		String password = configuration.getHttpDestination().getPassword();
				
		entry = this.clientService.readEntry(serviceURL, contentType, entitySetName, select, null, EXPAND, keyValue, user, password, client);
		
		return entry;
	}
	
	public ODataClientService getClientService()
	{
		return clientService;
	}

	public void setClientService(final ODataClientService clientService)
	{
		this.clientService = clientService;
	}

	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}	

	public String getPath()
	{
		return this.path;
	}

	public void setPath(final String path)
	{
		this.path = path;
	}

	/**
	 * @return the serviceURL
	 */
	public String getServiceURL() {
		return serviceURL;
	}

	/**
	 * @param serviceURL the serviceURL to set
	 */
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

}
