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
package com.sap.hybris.reco.addon.facade;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;

import java.util.List;

import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;

/**
 * to do
 */
public interface ProductRecommendationManagerFacade
{
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductReferenceData> getProductRecommendation(RecommendationContext context);

	/**
	 * @param pageModel
	 * @return recommendation components on the page
	 */
	public List<CMSSAPRecommendationComponentModel> getRecommendationComponentForPage(AbstractPageModel pageModel);

	/**
	 * @param context
	 */
	public void postInteraction(InteractionContext context);

	/**
	 * Get the logged in User ID
	 *
	 * @return String
	 */
	public String getSessionUserId();

	/**
	 * @param componentUID
	 * @return true if recommendation is already fetched
	 */
	public boolean isRecommendationFetched(String componentUID);

	/**
	 * @param userId
	 * @param anonymousUser
	 * @param context
	 * @param component
	 * @param productCode
	 */
	public void populateContext(String userId, boolean anonymousUser, RecommendationContext context,
			CMSSAPRecommendationComponentModel component, String productCode);

	/**
	 * @param userId
	 * @param anonymousUser
	 * @param productCode
	 * @param pageModel
	 */
	public void prefetchRecommendationsForAllComponents(String userId, boolean anonymousUser, AbstractPageModel pageModel,
			String productCode);

	/**
	 * @return OriginOfContactId for anonymous user
	 */
	public String getAnonOriginOfContactId();

	/**
	 * @param anonOriginOfContactId
	 */
	public void setAnonOriginOfContactId(String anonOriginOfContactId);

	/**
	 * @return true if prefetch is enabled
	 */
	public boolean isPrefetchEnabled();
}
