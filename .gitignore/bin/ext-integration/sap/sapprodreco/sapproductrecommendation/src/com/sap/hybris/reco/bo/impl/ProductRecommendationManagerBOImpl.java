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
package com.sap.hybris.reco.bo.impl;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;
import de.hybris.platform.sap.core.bol.businessobject.BackendInterface;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;

import java.util.List;

import org.apache.log4j.Logger;

import com.sap.hybris.reco.be.ProductRecommendationManagerBackend;
import com.sap.hybris.reco.bo.ProductRecommendationManagerBO;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;


/**
 * Product Recommendation Manager BO Impl
 */
@BackendInterface(ProductRecommendationManagerBackend.class)
public class ProductRecommendationManagerBOImpl extends BusinessObjectBase implements ProductRecommendationManagerBO
{
	private static final Logger LOG = Logger.getLogger(ProductRecommendationManagerBOImpl.class);

	/**
	 * Get Product Recommendations from hybris Marketing based on current context
	 *
	 * @param context
	 * @return List<ProductRecommendation>
	 */
	@Override
	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{
		try
		{
			return ((ProductRecommendationManagerBackend) getBackendBusinessObject()).getProductRecommendation(context);
		}
		catch (final BackendException e)
		{
			LOG.error("Getting Recommendation failed due to BackendException");
		}
		return null;
	}

	/**
	 * Posts the user interactions to the hybris Marketing.
	 *
	 * @param context
	 */
	@Override
	public void postInteraction(final InteractionContext context)
	{
		try
		{
			((ProductRecommendationManagerBackend) getBackendBusinessObject()).postInteraction(context);
		}
		catch (final BackendException e)
		{
			LOG.error("Posting Interaction failed due to BackendException");
		}
	}

	/**
	 * Prefetch Product Recommendations from hybris Marketing based on contexts for the batch
	 *
	 * @param contexts
	 */
	@Override
	public void prefetchRecommendations(final List<RecommendationContext> contexts)
	{
		try
		{
			((ProductRecommendationManagerBackend) getBackendBusinessObject()).prefetchRecommendations(contexts);
		}
		catch (final BackendException e)
		{
			LOG.error("Prefetching Recommendations failed due to BackendException");
		}
	}

	/**
	 * @param backendObj
	 */
	@Override
	public void setBackendObject(final BackendBusinessObject backendObj)
	{
		super.setBackendObject(backendObj);
	}

	/**
	 * @return true if prefetch is enabled
	 */
	@Override
	public boolean isPrefetchEnabled()
	{
		boolean isPrefetchEnabled = false;
		try
		{
			isPrefetchEnabled = ((ProductRecommendationManagerBackend) getBackendBusinessObject()).isPrefetchEnabled();
		}
		catch (final BackendException e)
		{
			LOG.error("Error getting backend object");
		}
		return isPrefetchEnabled;
	}

}
