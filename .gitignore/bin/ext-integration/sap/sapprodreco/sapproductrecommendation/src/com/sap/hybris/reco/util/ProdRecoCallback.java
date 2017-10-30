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

import de.hybris.platform.sap.core.odata.util.MyCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.callback.WriteCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.hybris.reco.constants.SapproductrecommendationConstants;


/**
 * Callback class for Product Recommendation
 */
public class ProdRecoCallback extends MyCallback
{
	private static final Logger LOG = LoggerFactory.getLogger(ProdRecoCallback.class);

	/**
	 * ProdRecoCallback constructor
	 */
	public ProdRecoCallback()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent#retrieveFeedResult(org.apache.olingo.odata2.api.ep.
	 * callback.WriteFeedCallbackContext)
	 */
	/**
	 * for retrieving the data for deep entities
	 */
	@Override
	public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException
	{
		final WriteFeedCallbackResult result = new WriteFeedCallbackResult();

		try
		{
			if (isNavigationFromTo(context, SapproductrecommendationConstants.RECOMMENDATION_SCENARIOS,
					SapproductrecommendationConstants.SCENARIOS))
			{

				final List<String> navigationPropertyNames = new ArrayList<String>();
				navigationPropertyNames.add(SapproductrecommendationConstants.BASKET_OBJECTS);
				navigationPropertyNames.add(SapproductrecommendationConstants.LEADING_OBJECTS);

				final Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
				final ProdRecoCallback prodRecoCallBack = new ProdRecoCallback();
				prodRecoCallBack.setDataStore(this.getDataStore());
				prodRecoCallBack.setServiceRoot(this.getServiceRoot());
				callbacks.put(SapproductrecommendationConstants.BASKET_OBJECTS, prodRecoCallBack);
				callbacks.put(SapproductrecommendationConstants.LEADING_OBJECTS, prodRecoCallBack);

				final EdmEntitySet entitySet = context.getSourceEntitySet().getEntityContainer()
						.getEntitySet(SapproductrecommendationConstants.SCENARIOS);

				final ExpandSelectTreeNode expandSelectTreeNode = ExpandSelectTreeNode.entitySet(entitySet)
						.expandedLinks(navigationPropertyNames).selectedProperties(Collections.<String> emptyList()).build();

				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
						.omitJsonWrapper(true).responsePayload(false).expandSelectTree(expandSelectTreeNode).callbacks(callbacks)
						.build();

				final List<Map<String, Object>> scenarios = dataStore.getEntities(SapproductrecommendationConstants.SCENARIOS);//getScenarios();
				result.setFeedData(scenarios);
				result.setInlineProperties(inlineProperties);
			}
			else if (isNavigationFromTo(context, SapproductrecommendationConstants.RECOMMENDATION_SCENARIOS,
					SapproductrecommendationConstants.CONTEXT_PARAMS))
			{
				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
						.omitJsonWrapper(true).responsePayload(false).expandSelectTree(context.getCurrentExpandSelectTreeNode())
						.selfLink(context.getSelfLink()).build();

				final List<Map<String, Object>> contextParams = new ArrayList<Map<String, Object>>();
				result.setFeedData(contextParams);
				result.setInlineProperties(inlineProperties);

			}
			else if (isNavigationFromTo(context, SapproductrecommendationConstants.RECOMMENDATION_SCENARIOS,
					SapproductrecommendationConstants.RESULT_OBJECTS))
			{
				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
						.omitJsonWrapper(true).responsePayload(false).expandSelectTree(context.getCurrentExpandSelectTreeNode())
						.selfLink(context.getSelfLink()).build();

				final List<Map<String, Object>> resultObjects = new ArrayList<Map<String, Object>>();
				result.setFeedData(resultObjects);
				result.setInlineProperties(inlineProperties);
			}
			else if (isNavigationFromTo(context, SapproductrecommendationConstants.SCENARIOS,
					SapproductrecommendationConstants.BASKET_OBJECTS))
			{
				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
						.omitJsonWrapper(true).responsePayload(false).expandSelectTree(context.getCurrentExpandSelectTreeNode())
						.selfLink(context.getSelfLink()).build();
				final String scenarioId = context.getEntryData().get(SapproductrecommendationConstants.SCENARIO_ID).toString();
				final List<Map<String, Object>> basketObjects = dataStore.getSubEntityList(
						SapproductrecommendationConstants.SCENARIOS, SapproductrecommendationConstants.SCENARIO_ID, scenarioId,
						SapproductrecommendationConstants.BASKET_OBJECTS);//getBasketObjects(scenarioId);
				result.setFeedData(basketObjects);
				result.setInlineProperties(inlineProperties);
			}
			else if (isNavigationFromTo(context, SapproductrecommendationConstants.SCENARIOS,
					SapproductrecommendationConstants.LEADING_OBJECTS))
			{
				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
						.omitJsonWrapper(true).responsePayload(false).expandSelectTree(context.getCurrentExpandSelectTreeNode())
						.selfLink(context.getSelfLink()).build();
				final String scenarioId = context.getEntryData().get(SapproductrecommendationConstants.SCENARIO_ID).toString();
				final List<Map<String, Object>> leadingObjects = dataStore.getSubEntityList(
						SapproductrecommendationConstants.SCENARIOS, SapproductrecommendationConstants.SCENARIO_ID, scenarioId,
						SapproductrecommendationConstants.LEADING_OBJECTS);//getLeadingObjects(scenarioId);
				result.setFeedData(leadingObjects);
				result.setInlineProperties(inlineProperties);
			}
		}
		catch (final EdmException e)
		{
			LOG.error(SapproductrecommendationConstants.ERR_EXPAND);
		}
		return result;
	}

	/**
	 * Method to check the navigation from for an entitySet
	 */
	@Override
	public boolean isNavigationFromTo(final WriteCallbackContext context, final String entitySetName,
			final String navigationPropertyName) throws EdmException
	{
		if (entitySetName == null || navigationPropertyName == null)
		{
			return false;
		}
		final EdmEntitySet sourceEntitySet = context.getSourceEntitySet();
		final EdmNavigationProperty navigationProperty = context.getNavigationProperty();
		return entitySetName.equals(sourceEntitySet.getName()) && navigationPropertyName.equals(navigationProperty.getName());
	}
}
