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
package com.sap.hybris.reco.util;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.recentvieweditemsservices.RecentViewedItemsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.be.ProductRecommendationsValueProvider;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;


/**
 * Utility Class For Product Recommendation
 *
 */
public class ProductRecommendationManagerUtil
{

	protected HMCConfigurationReader configuration;
	private SessionService sessionService;
	private CartService cartService;
	private ProductService productService;
	private RecentViewedItemsService recentViewedItemsService;

	private boolean prefetchEnabled;
	private ProductRecommendationsValueProvider defaultResultProvider;
	private int maxThreads;
	private int retrieveWaitTimeoutMs;
	private ExecutorService threadPool;

	/**
	 * For initializing thread pool
	 */
	@PostConstruct
	public void init()
	{
		this.threadPool = Executors.newFixedThreadPool(getMaxThreads(), new TenantAwareThreadFactory(Registry.getCurrentTenant()));
	}

	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getPrefetchedResult(final RecommendationContext context)
	{
		List<ProductRecommendationData> result = Lists.newArrayList();

		final List<String> recommendationIds = getAndRemoveFutureFromSession(
				getComponentSessionKey(context.getComponentModel().getUid()));
		if (recommendationIds == null)
		{
			result = getDefaultResultProvider().getDefaultResult();
		}
		else
		{
			for (final String recommendationId : recommendationIds)
			{
				result.add(createProductRecommedation(recommendationId));
			}
		}


		return result;
	}

	/**
	 * @param context
	 * @return list of recommendations
	 */
	@SuppressWarnings("unchecked")
	private List<String> getAndRemoveFutureFromSession(final String uid)
	{
		List<String> recommendationIds = null;
		if (getSessionService().getAttribute(uid) != null)
		{
			recommendationIds = (List<String>) getSessionService().getAttribute(uid);
			removeFromSession(uid);
		}
		return recommendationIds;
	}

	/**
	 * @param key
	 *           to remove recommendation from session
	 */
	private void removeFromSession(final String key)
	{
		getSessionService().removeAttribute(key);
	}

	/**
	 * @param productId
	 * @return Product Recommendation Data
	 */
	public ProductRecommendationData createProductRecommedation(final String productId)
	{
		try
		{
			final ProductRecommendationData productRecommendationData = new ProductRecommendationData();
			productRecommendationData.setProductCode(productId);
			return productRecommendationData;
		}
		catch (final UnknownIdentifierException exception)
		{
			return null;
		}
	}

	/**
	 * @param context
	 * @return list of leading item ids
	 */
	public List<String> getLeadingItemId(final RecommendationContext context)
	{
		final List<String> leadingItems = new ArrayList<String>();
		final String leadingItemId = context.getLeadingItemId();

		if (leadingItemId != null)
		{
			if (!leadingItemId.isEmpty())

			{
				if (context.getLeadingItemType().equals(SapproductrecommendationConstants.CATEGORY))
				{
					final ProductModel productModel = getProductService().getProductForCode(leadingItemId);
					final Collection<CategoryModel> catList = productModel.getSupercategories();
					for (final CategoryModel model : catList)
					{
						leadingItems.add(model.getCode());
					}

				}
				else if (context.getLeadingItemType().equals(SapproductrecommendationConstants.PRODUCT))
				{
					leadingItems.add(leadingItemId);
				}
			}
		}
		return leadingItems;
	}

	/**
	 * @return cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	/**
	 * @return productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
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

	/**
	 * @return sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the recentViewedItemsService
	 */
	public RecentViewedItemsService getRecentViewedItemsService()
	{
		return recentViewedItemsService;
	}

	/**
	 * @param recentViewedItemsService the recentViewedItemsService to set
	 */
	public void setRecentViewedItemsService(RecentViewedItemsService recentViewedItemsService)
	{
		this.recentViewedItemsService = recentViewedItemsService;
	}
	
	/**
	 * @return prefetchEnabled
	 */
	public boolean isPrefetchEnabled()
	{
		return prefetchEnabled;
	}

	/**
	 * @param prefetchEnabled
	 */
	public void setPrefetchEnabled(final boolean prefetchEnabled)
	{
		this.prefetchEnabled = prefetchEnabled;
	}

	/**
	 * @return defaultResultProvider
	 */
	public ProductRecommendationsValueProvider getDefaultResultProvider()
	{
		return defaultResultProvider;
	}

	/**
	 * @param defaultResultProvider
	 */
	public void setDefaultResultProvider(final ProductRecommendationsValueProvider defaultResultProvider)
	{
		this.defaultResultProvider = defaultResultProvider;
	}

	/**
	 * @param compUid
	 * @return Key to store recommendation results in session for component
	 */
	public String getComponentSessionKey(final String compUid)
	{
		return getSessionService().getCurrentSession().getSessionId() + SapproductrecommendationConstants.UNDERSCORE + compUid;
	}

	/**
	 * @return maxThreads
	 */
	public int getMaxThreads()
	{
		return maxThreads;
	}

	/**
	 * @param maxThreads
	 *           the maxThreads to set
	 */
	@Required
	public void setMaxThreads(final int maxThreads)
	{
		this.maxThreads = maxThreads;
	}

	/**
	 * @return retrieveWaitTimeoutMs
	 */
	public int getRetrieveWaitTimeoutMs()
	{
		return retrieveWaitTimeoutMs;
	}


	/**
	 * @param retrieveWaitTimeoutMs
	 */
	@Required
	public void setRetrieveWaitTimeoutMs(final int retrieveWaitTimeoutMs)
	{
		this.retrieveWaitTimeoutMs = retrieveWaitTimeoutMs;
	}

	/**
	 * @return threadPool
	 */
	public ExecutorService getThreadPool()
	{
		return threadPool;
	}

	/**
	 * @param threadPool
	 */
	public void setThreadPool(final ExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}


}
