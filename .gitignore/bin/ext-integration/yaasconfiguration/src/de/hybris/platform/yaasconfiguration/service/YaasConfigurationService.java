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

package de.hybris.platform.yaasconfiguration.service;


import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.List;
import java.util.Optional;


/**
 * Focuses on methods to retrieve configuration items like yaas application
 */
public interface YaasConfigurationService
{

	/**
	 * Retrieve YaaSApplication by identifier
	 *
	 * @param applicationId
	 * @return
	 */
	YaasApplicationModel getYaasApplicationForId(final String applicationId);

	/**
	 * if exists it return the first record in YaasApplication table, otherwise it returns empty
	 *
	 * @return
	 */
	Optional<YaasApplicationModel> takeFirstModel();

	/**
	 * get all configured yaas application
	 *
	 * @return
	 */
	List<YaasApplicationModel> getYaaSApplications();
}
