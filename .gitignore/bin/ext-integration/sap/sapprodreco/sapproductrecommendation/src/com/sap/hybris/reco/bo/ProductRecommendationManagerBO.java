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

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObject;

import java.util.List;

import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;


/**
 * Product Recommendation Manager BO
 */
public interface ProductRecommendationManagerBO extends BusinessObject
{
	/**
	 * Get Product Recommendations from hybris Marketing based on current context
	 *
	 * @param context
	 * @return List<ProductRecommendation>
	 */
	public List<ProductRecommendationData> getProductRecommendation(RecommendationContext context);

	/**
	 * Posts the user interactions to the hybris Marketing.
	 *
	 * @param context
	 */
	public void postInteraction(InteractionContext context);

	/**
	 * Prefetch Product Recommendations from hybris Marketing based on contexts for the batch
	 *
	 * @param contexts
	 */
	void prefetchRecommendations(List<RecommendationContext> contexts);

	/**
	 * @return true if prefetch is enabled
	 */
	public boolean isPrefetchEnabled();


	/**
	 * @param backendObj
	 */
	public void setBackendObject(BackendBusinessObject backendObj);
}
