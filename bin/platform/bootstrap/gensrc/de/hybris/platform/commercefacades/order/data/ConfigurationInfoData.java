/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;

public  class ConfigurationInfoData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ConfigurationInfoData.configuratorType</code> property defined at extension <code>commercefacades</code>. */
		
	private ConfiguratorType configuratorType;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.status</code> property defined at extension <code>commercefacades</code>. */
		
	private ProductInfoStatus status;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configurationLabel</code> property defined at extension <code>commercefacades</code>. */
		
	private String configurationLabel;

	/** <i>Generated property</i> for <code>ConfigurationInfoData.configurationValue</code> property defined at extension <code>commercefacades</code>. */
		
	private String configurationValue;
	
	public ConfigurationInfoData()
	{
		// default constructor
	}
	
	
	
	public void setConfiguratorType(final ConfiguratorType configuratorType)
	{
		this.configuratorType = configuratorType;
	}
	
	
	
	public ConfiguratorType getConfiguratorType() 
	{
		return configuratorType;
	}
	
	
	
	public void setStatus(final ProductInfoStatus status)
	{
		this.status = status;
	}
	
	
	
	public ProductInfoStatus getStatus() 
	{
		return status;
	}
	
	
	
	public void setConfigurationLabel(final String configurationLabel)
	{
		this.configurationLabel = configurationLabel;
	}
	
	
	
	public String getConfigurationLabel() 
	{
		return configurationLabel;
	}
	
	
	
	public void setConfigurationValue(final String configurationValue)
	{
		this.configurationValue = configurationValue;
	}
	
	
	
	public String getConfigurationValue() 
	{
		return configurationValue;
	}
	


}