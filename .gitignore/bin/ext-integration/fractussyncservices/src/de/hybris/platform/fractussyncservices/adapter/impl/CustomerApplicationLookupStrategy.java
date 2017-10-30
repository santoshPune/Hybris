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
package de.hybris.platform.fractussyncservices.adapter.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class CustomerApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	private YaasConfigurationService yaasConfigurationService;

	/**
	 * @return the yaasConfigurationService
	 */
	public YaasConfigurationService getYaasConfigurationService()
	{
		return yaasConfigurationService;
	}


	/**
	 * @param yaasConfigurationService the yaasConfigurationService to set
	 */
	@Required
	public void setYaasConfigurationService(YaasConfigurationService yaasConfigurationService)
	{
		this.yaasConfigurationService = yaasConfigurationService;
	}


	@Override
	public String getTypeCode()
	{
		return CustomerModel._TYPECODE;
	}


	@Override
	public String lookup(Item item)
	{
		StringBuilder yaasApplicationIds = new StringBuilder();
		List<YaasApplicationModel> allYaasApplications = yaasConfigurationService.getYaaSApplications();
		allYaasApplications.forEach(yaasApplication->yaasApplicationIds.append(yaasApplication.getIdentifier()+","));
		return yaasApplicationIds.toString();
	}

}
