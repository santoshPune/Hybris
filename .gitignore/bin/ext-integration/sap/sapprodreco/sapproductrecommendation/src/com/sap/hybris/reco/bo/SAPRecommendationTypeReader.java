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

import de.hybris.platform.core.Registry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.SAPRecommendationType;


/**
 * To fetch a list of model types from PRI using OData service
 */
public class SAPRecommendationTypeReader
{
	protected HMCConfigurationReader configuration;
	private final String SAP_RECOMMENDATION_SCENARIO_READER = "sapRecommendationScenarioReader";
	private final String SAP_RECOMMENDATION_MODEL_TYPE_READER = "sapRecommendationModelTypeReader";

	/**
	 * Gets all Recommendation Types
	 *
	 * @return a list of SAPRecommendationModelType
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public List<SAPRecommendationType> getAllRecommendationTypes() throws ODataException, URISyntaxException, IOException
	{
		final String usage = configuration.getUsage();
		if (usage.equals(SapproductrecommendationConstants.SCENARIO))
		{
			final SAPRecommendationScenarioReader reader = (SAPRecommendationScenarioReader) Registry.getApplicationContext()
					.getBean(SAP_RECOMMENDATION_SCENARIO_READER);
			return (reader.getAllRecommendationScenarios());
		}
		else if (usage.equals(SapproductrecommendationConstants.MODELTYPE))
		{
			final SAPRecommendationModelTypeReader reader = (SAPRecommendationModelTypeReader) Registry.getApplicationContext()
					.getBean(SAP_RECOMMENDATION_MODEL_TYPE_READER);
			return (reader.getAllRecommendationModelTypes());
		}
		return null;
	}

	/**
	 * @return configuration
	 */
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}

}
