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

import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.be.ProductRecommendationsValueProvider;
import com.sap.hybris.reco.dao.ProductRecommendationData;


/**
 * To provide default Value for Product Recommendations
 */
public class DefaultProductRecommendationsValueProvider implements ProductRecommendationsValueProvider
{

	private static final Logger LOG = Logger.getLogger(DefaultProductRecommendationsValueProvider.class);
	private final String LOG_MSG = "Providing default product recommendation result";

	/**
	 * @return List<ProductRecommendationData>
	 */
	@Override
	public List<ProductRecommendationData> getDefaultResult()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(LOG_MSG);
		}
		return Lists.newArrayList();
	}
}
