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
package com.sap.hybris.reco.common.util;

import java.net.MalformedURLException;
import java.net.URL;

import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Read the SAP Core Global Configuration, Base Store 
 * Configuration, and other fields in the backoffice (hMC previously)
 *
 */
public class HMCConfigurationReader
{
	private final static Logger LOG = Logger.getLogger(HMCConfigurationReader.class.getName());
	
	@Resource(name = "sapCoreDefaultSAPGlobalConfigurationService")
	private SAPGlobalConfigurationService globalConfigurationService;
	
	@Resource(name = "sapCoreHTTPDestinationService")
	private HTTPDestinationServiceImpl httpDestinationService;
	private String httpDestinationId;
	
	@Resource(name = "sapPRIModuleConfigurationAccess")
	private ModuleConfigurationAccess baseStoreConfigurationService;
	
	private String rfcDestinationId;
	
	private String userType;
	private String usage;
	
	/**
	 * Get the RFC Destination details from the hMC SAP Integration HTTP Destination configuration
	 * 
	 */
	public void loadRFCConfiguration()
	{
		this.setRfcDestinationId(this.getGlobalConfigurationProperty("sapproductrecommendation_rfcdest"));		
	}
	
	/**
	 * Get the User Type from the PRI configuration in the Base Store configuration
	 * 
	 */
	public void loadUsageConfiguration()
	{		
		this.setUsage(this.getGlobalConfigurationProperty("sapproductrecommendation_usage"));
	}
	
	/**
	 * Get the User Type from the PRI configuration in the Base Store configuration
	 * 
	 */
	public void loadUserTypeConfiguration()
	{
		String userType = "";
		if (baseStoreConfigurationService!= null) {
			userType = (String) baseStoreConfigurationService.getProperty("sapproductrecommendation_usertype");
		}
		this.setUserType(userType);
	}
	
	/**
	 * Get Global Configuration Property
	 * 
	 */
	private String getGlobalConfigurationProperty(final String propertyName){
		String value = "";
		if (globalConfigurationService != null & globalConfigurationService.sapGlobalConfigurationExists())
		{
			if (globalConfigurationService.getProperty(propertyName) != null)
			{
				value = globalConfigurationService.getProperty(propertyName).toString();
			}
		}
		
		if (StringUtils.isNotEmpty(value))
		{
			return value.toString();
		}
		else
		{
			throw new ConfigurationException(propertyName);
		}
	}
	
	/**
	 * @return globalConfigurationService
	 */
	public SAPGlobalConfigurationService getGlobalConfigurationService()
	{
		return globalConfigurationService;
	}

	/**
	 * @param globalConfigurationService
	 */
	public void setGlobalConfigurationService(SAPGlobalConfigurationService globalConfigurationService)
	{
		this.globalConfigurationService = globalConfigurationService;
	}
	
	/**
	 * @return baseStoreConfigurationService
	 */
	public ModuleConfigurationAccess getBaseStoreConfigurationService()
	{
		return baseStoreConfigurationService;
	}


	/**
	 * @param baseStoreConfigurationService
	 */
	public void setBaseStoreConfigurationService(ModuleConfigurationAccess baseStoreConfigurationService)
	{
		this.baseStoreConfigurationService = baseStoreConfigurationService;
	}

	/**
	 * @return httpDestinationService
	 */
	public HTTPDestinationServiceImpl getHttpDestinationService()
	{
		return httpDestinationService;
	}

	/**
	 * @param httpDestinationService
	 */
	public void setHttpDestinationService(HTTPDestinationServiceImpl httpDestinationService)
	{
		this.httpDestinationService = httpDestinationService;
	}

	/**
	 * @return httpDestination
	 */
	public HTTPDestination getHttpDestination()
	{
		this.setHttpDestinationId(this.getGlobalConfigurationProperty("sapproductrecommendation_httpdest"));
		try
		{
			if (this.httpDestinationService != null)
			{
				 return this.httpDestinationService.getHTTPDestination(this.getHttpDestinationId());
			}
		}
		catch (UnknownIdentifierException e)
		{
			throw new ConfigurationException("sapproductrecommendation_httpdest");
		}
		return null;
	}

	/**
	 * @return httpDestinationId
	 */
	public String getHttpDestinationId()
	{
		return httpDestinationId;
	}

	/**
	 * @param httpDestinationId
	 */
	public void setHttpDestinationId(String httpDestinationId)
	{
		this.httpDestinationId = httpDestinationId;
	}
	

	/**
	 * @return rfcDestinationId
	 */
	public String getRfcDestinationId()
	{
		this.loadRFCConfiguration();
		return rfcDestinationId;
	}

	/**
	 * @param rfcDestinationId
	 */
	public void setRfcDestinationId(String rfcDestinationId)
	{
		this.rfcDestinationId = rfcDestinationId;
	}

	/**
	 * @return userType
	 */
	public String getUserType()
	{
		this.loadUserTypeConfiguration();
		return userType;
	}

	/**
	 * @param userType
	 */
	public void setUserType(String userType)
	{		
		this.userType = userType;
	}
	
	/**
	 * @param itemType
	 */
	public void setItemType(String itemType)
	{
	}

	/**
	 * @return usage
	 */
	public String getUsage()
	{
		this.loadUsageConfiguration();
		return usage;
	}
	
	/**
	 * @param usage
	 */
	public void setUsage(String usage)
	{
		this.usage = usage;
	}
	
	/**
	 * Returns the URL specified in HTTP Destination in backoffice
	 * in the format https://server:port
	 * 
	 * @return httpDestination
	 */
	public String getHttpDestinationURL()
	{
		URL url;
		String path = "";
		try
		{
			url = new URL(getHttpDestination().getTargetURL());
			path = url.getProtocol() + "://" + url.getAuthority();

		}
		catch (MalformedURLException e)
		{
			LOG.error("Configured HTTP Destination contains a malformed target URL");
		}
		return path;
	}
	
	/**
	 * Returns the SAP Client number if it is specified in the HTTP Destination URL in backoffice
	 * in the format https://server:port/?sap-client=XYZ
	 * 
	 * @return httpDestination
	 */
	public String getHttpDestinationSAPClient()
	{
		URL url;
		String client = "";
		try
		{
			url = new URL(getHttpDestination().getTargetURL());
			String query = url.getQuery();
			if (StringUtils.isNotEmpty(query))
			{
				client = query.substring(query.lastIndexOf("=") + 1);
			}
		}
		catch (MalformedURLException e)
		{
			LOG.warn("Configured URL is malformed, does not contain a client number");
		}
		return client;
	}
	
}
