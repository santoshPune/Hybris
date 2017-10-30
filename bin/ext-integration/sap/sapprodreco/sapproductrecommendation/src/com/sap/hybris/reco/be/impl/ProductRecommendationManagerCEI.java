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

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.bol.backend.jco.BackendBusinessObjectBaseJCo;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.sap.hybris.reco.be.ProductRecommendationManagerBackend;
import com.sap.hybris.reco.bo.ProductRecommendationPrefetcher;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.util.ProductRecommendationManagerUtil;


/**
 * Product Recommendation Manager CEI
 *
 */
@BackendType("CEI")
public class ProductRecommendationManagerCEI extends BackendBusinessObjectBaseJCo implements ProductRecommendationManagerBackend
{
	private final static Logger LOG = Logger.getLogger(ProductRecommendationManagerCEI.class.getName());

	private ProductRecommendationManagerUtil recommendationService;

	private static final String JCO_STATELESS = "JCoStateless";
	private final static String ERROR = "E";
	private final static String INFO = "I";
	private final static String DEBUG = "D";
	private final static String PROD_RECO_POST_IA_FOR_SCENARIO = "PROD_RECO_POST_IA_FOR_SCENARIO";
	private final static String SPACE = " ";
	private static final String SCENARIO_ID = "SCENARIO_ID";
	private static final String USER_ID = "USER_ID";
	private static final String USER_TYPE = "USER_TYPE";
	private static final String IA_TYPE = "IA_TYPE";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String SOURCE_OBJECT_ID = "SOURCE_OBJECT_ID";
	private static final String OBJECT_TYPE = "OBJECT_TYPE";
	private static final String OBJECT_ID = "OBJECT_ID";
	private static final String PRODUCT_NAV_URL = "PRODUCT_NAV_URL";
	private static final String PRODUCT_IMAGE_URL = "PRODUCT_IMAGE_URL";
	private static final String IT_INTERACTIONS = "IT_INTERACTIONS";
	private static final String IPRODUCTS = "IPRODUCTS";
	private static final String ET_MESSAGES = "ET_MESSAGES";
	private static final String PROD_RECO_GET_RECOMMENDATIONS = "PROD_RECO_GET_RECOMMENDATIONS";
	private static final String MODEL_TYPE = "MODEL_TYPE";
	private static final String ET_RESULTS = "ET_RESULTS";
	private static final String PROD_RECO_FAIL = "Cannot retrieve product recommendations from yMarketing";
	private static final String ITEM_ID = "ITEM_ID";
	private static final String ITEM_TYPE = "ITEM_TYPE";
	private static final String PROD_RECO_GET_RECO_BY_SCENARIO = "PROD_RECO_GET_RECO_BY_SCENARIO";
	private static final String IT_RECOMMENDERS = "IT_RECOMMENDERS";
	private static final String LEADING_OBJECTS = "LEADING_OBJECTS";
	private static final String IV_USER_ID = "IV_USER_ID";
	private static final String IV_USER_TYPE = "IV_USER_TYPE";
	private static final String BASKET_OBJECTS = "BASKET_OBJECTS";
	private static final String RFC_HYPHEN = "RFC - ";
	private static final String BACKEND_NA = " backend is not available";
	private static final String ID = "ID";
	private static final String MESSAGE = "MESSAGE";


	/**
	 * @param context
	 *           For Post interaction of product recommendation
	 */
	@Override
	public void postInteraction(final InteractionContext context)
	{
		LOG.debug("\n\n POST INTERACTION : RFC \n\n");
		final JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction(PROD_RECO_POST_IA_FOR_SCENARIO);
			final JCoParameterList importParameterList = function.getImportParameterList();
			final JCoTable interactions = importParameterList.getTable(IT_INTERACTIONS);
			interactions.appendRow();
			interactions.setValue(SCENARIO_ID, context.getScenarioId());
			interactions.setValue(USER_ID, context.getUserId());
			interactions.setValue(USER_TYPE, context.getUserType());
			interactions.setValue(IA_TYPE, SapproductrecommendationConstants.CLICKTHROUGH);
			interactions.setValue(TIMESTAMP, context.getTimestamp());
			interactions.setValue(SOURCE_OBJECT_ID, context.getSourceObjectId());

			final JCoTable leadingObjects = interactions.getTable(IPRODUCTS);
			leadingObjects.appendRow();
			leadingObjects.setValue(OBJECT_TYPE, context.getProductType());
			leadingObjects.setValue(OBJECT_ID, context.getProductId());
			leadingObjects.setValue(PRODUCT_NAV_URL, context.getProductNavURL());
			leadingObjects.setValue(PRODUCT_IMAGE_URL, context.getProductImageURL());

			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable messages = exportParameterList.getTable(ET_MESSAGES);

			LOG.debug("\n RFC : Interaction posted for product click \n");

			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
				LOG.error("Posting Interaction failed due to a BackendException" );
		}

	}

	/**
	 * @param context
	 * @return list of recommended products
	 */
	@Override
	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{

		if (isPrefetchEnabled())
		{
			LOG.debug("\n\n PREFETCH ENABLED : RFC \n\n");
			return getRecommendationService().getPrefetchedResult(context);
		}

		if (getRecommendationService().getConfiguration().getUsage().equals(SapproductrecommendationConstants.SCENARIO))
		{
			LOG.debug("\n \n PREFETCH DISABLED : RFC : SCENARIO \n\n");
			return getProductRecommendationByScenario(context);
		}
		else if (getRecommendationService().getConfiguration().getUsage().equals(SapproductrecommendationConstants.MODELTYPE))
		{
			LOG.debug("\n\n PREFETCH DISABLED : RFC : MODEL TYPE \n\n");
			return getProductRecommendationByModelType(context);
		}
		return null;
	}


	/**
	 * @param contexts
	 *           For prefetching recomnendations
	 */
	@Override
	public void prefetchRecommendations(final List<RecommendationContext> contexts)
	{
		if (getRecommendationService().getConfiguration().getUsage().equals(SapproductrecommendationConstants.SCENARIO))
		{
			prefetchProductRecommendationsByScenario(contexts);
		}
		else if (getRecommendationService().getConfiguration().getUsage().equals(SapproductrecommendationConstants.MODELTYPE))
		{
			prefetchRecommendationsByModelType(contexts);
		}
	}

	/**
	 * @param contexts
	 */
	public void prefetchRecommendationsByModelType(final List<RecommendationContext> contexts)
	{
		//build JCoConneciton
		final JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction(PROD_RECO_GET_RECOMMENDATIONS);
			handleImportParameterList(function, contexts, MODEL_TYPE);
			prefetchRecommendations(jCoConnection, function, contexts);

		}
		catch (final BackendException e)
		{
			LOG.error("Prefetching Recommendations by Model Type failed due to a BackendException");
		}

	}

	/**
	 * @param jCoConnection
	 * @param function
	 * @param contexts
	 */
	private void prefetchRecommendations(final JCoConnection jCoConnection, final JCoFunction function,
			final List<RecommendationContext> contexts)
	{
		final Future<JCoParameterList> future = getRecommendationService().getThreadPool().submit(
				new ProductRecommendationPrefetcher(jCoConnection, function));
		storeFutureInSession(contexts, future);
	}

	/**
	 * @param contexts
	 * @return list of recommendations
	 */
	public List<String> getRecoTypes(final List<RecommendationContext> contexts)
	{
		final List<String> recotypes = new ArrayList<String>();
		for (final RecommendationContext context : contexts)
		{
			recotypes.add(context.getRecotype());
		}
		return recotypes;
	}

	/**
	 * For storing recommendations in session
	 *
	 * @param uid
	 * @param future
	 */
	private void storeFutureInSession(final List<RecommendationContext> contexts, final Future<JCoParameterList> future)
	{
		try
		{
			final JCoParameterList exportParameterList = future.get(getRecommendationService().getRetrieveWaitTimeoutMs(),
					TimeUnit.MILLISECONDS);
			final JCoTable results = exportParameterList.getTable(ET_RESULTS);
			final JCoTable messages = exportParameterList.getTable(ET_MESSAGES);

			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
			for (final RecommendationContext context : contexts)
			{
				final List<String> recommendationIds = getRecommendationsForRecoType(results, context.getRecotype());
				getRecommendationService().getSessionService().setAttribute(
						getRecommendationService().getComponentSessionKey(context.getComponentModel().getUid()), recommendationIds);
			}
		}
		catch (InterruptedException | ExecutionException | TimeoutException e)
		{
			LOG.error(PROD_RECO_FAIL + " " + e.getClass().getName());
		}
	}

	/**
	 * For getting recommendations of a particular recotype
	 *
	 * @param results
	 * @param result
	 */
	private List<String> getRecommendationsForRecoType(final JCoTable results, final String recotype)
	{
		final List<String> recommendations = new ArrayList<String>();
		if (!results.isEmpty())
		{
			final int len = results.getNumRows();
			for (int i = 0; i < len; i++)
			{
				results.setRow(i);
				final String recommendationId = results.getString(ITEM_ID);
				if (results.getString(SCENARIO_ID).equalsIgnoreCase(recotype))
				{
					recommendations.add(recommendationId);
				}
			}
		}
		return recommendations;
	}

	/**
	 * @param results
	 * @param result
	 */
	private void addRecommendations(final JCoTable results, final List<ProductRecommendationData> result)
	{
		if (!results.isEmpty())
		{
			final int len = results.getNumRows();
			for (int i = 0; i < len; i++)
			{
				results.setRow(i);
				final String recommendationId = results.getString(ITEM_ID);
				final ProductRecommendationData productRecommendation = getRecommendationService().createProductRecommedation(
						recommendationId);
				if (productRecommendation != null)
				{
					result.add(productRecommendation);
				}
			}
		}
	}

	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendationByModelType(final RecommendationContext context)
	{
		final List<ProductRecommendationData> result = new ArrayList<>();

		//build JCoConneciton
		final JCoConnection jCoConnection = getJcoConnection();
		try
		{
			final JCoFunction function = jCoConnection.getFunction(PROD_RECO_GET_RECOMMENDATIONS);
			handleImportParameterList(function, Arrays.asList(context), MODEL_TYPE);

			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable results = exportParameterList.getTable(ET_RESULTS);

			addRecommendations(results, result);

			final JCoTable messages = exportParameterList.getTable(ET_MESSAGES);

			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("Getting Recommendations by Model Type failed due to a BackendException");
		}

		return result;
	}


	/**
	 * @param contexts
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> prefetchProductRecommendationsByScenario(final List<RecommendationContext> contexts)
	{
		final List<ProductRecommendationData> result = new ArrayList<>();

		final JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction(PROD_RECO_GET_RECO_BY_SCENARIO);
			handleImportParameterList(function, contexts, SCENARIO_ID);
			prefetchRecommendations(jCoConnection, function, contexts);

		}
		catch (final BackendException e)
		{
			LOG.error("Prefecthing Recommendations by Scenario failed due to a BackendException");
		}

		return result;
	}


	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendationByScenario(final RecommendationContext context)
	{
		final List<ProductRecommendationData> result = new ArrayList<>();

		final JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction(PROD_RECO_GET_RECO_BY_SCENARIO);
			handleImportParameterList(function, Arrays.asList(context), SCENARIO_ID);

			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable results = exportParameterList.getTable(ET_RESULTS);

			if (!results.isEmpty())
			{
				final int len = results.getNumRows();
				for (int i = 0; i < len; i++)
				{
					results.setRow(i);
					final String recommendationId = results.getString(ITEM_ID);

					final ProductRecommendationData productRecommendation = getRecommendationService().createProductRecommedation(
							recommendationId);
					if (productRecommendation != null)
					{
						result.add(productRecommendation);
					}
				}
			}
			final JCoTable messages = exportParameterList.getTable(ET_MESSAGES);

			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("Getting Recommendations by Scenario failed due to a BackendException");
		}

		return result;
	}

	/**
	 * For creating request for product recommendations
	 *
	 * @param function
	 * @param context
	 * @param valueKey
	 */
	private void handleImportParameterList(final JCoFunction function, final List<RecommendationContext> contexts,
			final String valueKey)
	{

		final JCoParameterList importParameterList = function.getImportParameterList();
		final JCoTable recommenders = importParameterList.getTable(IT_RECOMMENDERS);
		for (final RecommendationContext context : contexts)
		{
			recommenders.appendRow();
			recommenders.setValue(valueKey, context.getRecotype());


			//Add leading items
			final List<String> leadingItems = getRecommendationService().getLeadingItemId(context);
			final JCoTable leadingObjects = recommenders.getTable(LEADING_OBJECTS);
			if (leadingItems.size() > 0)
			{
				for (int i = 0; i < leadingItems.size(); i++)
				{
					leadingObjects.appendRow();
					leadingObjects.setValue(ITEM_ID, leadingItems.get(i));
					leadingObjects.setValue(ITEM_TYPE, context.getLeadingItemDSType());
				}
			}

			addCartItems(recommenders, context);
			if (context.isIncludeCart())
			{
				addCartItemsAsLeadingItems(leadingObjects, context);
			}
			if (context.isIncludeRecent())
			{
				addRecentItemsAsLeadingItems(leadingObjects, context);
			}
			
		}

		importParameterList.setValue(IV_USER_ID, contexts.get(0).getUserId());
		importParameterList.setValue(IV_USER_TYPE, contexts.get(0).getUserType());
	}

	/**
	 * @param recommenders
	 * @param context
	 */
	private void addCartItems(final JCoTable recommenders, final RecommendationContext context)
	{
		final JCoTable cartEntries = recommenders.getTable(BASKET_OBJECTS);
		final List<String> cartItems = getCartItemsFromSession();
		for (final String cartItem : cartItems)
		{
			cartEntries.appendRow();
			cartEntries.setValue(ITEM_ID, cartItem);
			cartEntries.setValue(ITEM_TYPE, context.getCartItemDSType());
		}
	}

	/**
	 * @param leadingObjects
	 * @param context
	 */
	private void addCartItemsAsLeadingItems(final JCoTable leadingObjects, final RecommendationContext context)
	{
		final List<String> cartItems = getCartItemsFromSession();
		for (final String cartItem : cartItems)
		{
			leadingObjects.appendRow();
			leadingObjects.setValue(ITEM_ID, cartItem);
			leadingObjects.setValue(ITEM_TYPE, context.getCartItemDSType());
		}

	}
	
	/**
	 * Adds recent items as leading objects to payload
	 *
	 * @param leadingObjects
	 * @param context
	 */
	private void addRecentItemsAsLeadingItems(final JCoTable leadingObjects, final RecommendationContext context)
	{
		/*final List<String> cartItems = getCartItemsFromSession();
		for (final String cartItem : cartItems)
		{
			final Map<String, String> leadingObject = new HashMap<>();
			leadingObject.put(LEADING_OBJECT_ID, cartItem);
			leadingObject.put(LEADING_OBJECT_TYPE, context.getCartItemDSType());
			leadingObjects.add(leadingObject);
		}*/
	}

	/**
	 * @return cartItems
	 */
	private List<String> getCartItemsFromSession()
	{
		final CartModel cartModel = getRecommendationService().getCartService().getSessionCart();
		final List<String> cartItems = new ArrayList<String>();
		for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{
			cartItems.add(cartEntry.getProduct().getCode());
		}
		return cartItems;
	}

	/**
	 * @return jCoConnection
	 */
	private JCoConnection getJcoConnection()
	{
		JCoConnection jCoConnection;
		if (getRecommendationService().getConfiguration().getRfcDestinationId() == null)
		{
			jCoConnection = getDefaultJCoConnection();
		}
		else
		{
			jCoConnection = getJCoConnection(JCO_STATELESS, getRecommendationService().getConfiguration().getRfcDestinationId());
		}

		try
		{
			if (jCoConnection.isBackendAvailable() == false)
			{
				LOG.debug(RFC_HYPHEN + getRecommendationService().getConfiguration().getRfcDestinationId() + BACKEND_NA);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("JCo Connection Error due to BackendException");
		}
		return jCoConnection;
	}

	/**
	 * For logging Jco messages
	 *
	 * @param table
	 * @param level
	 */
	private void logJCoMessages(final JCoTable table, final String level)
	{
		final int len = table.getNumRows();
		for (int i = 0; i < len; i++)
		{
			table.setRow(i);
			final String msgId = table.getString(ID);
			final String msg = table.getString(MESSAGE);
			if (level.equals(DEBUG))
			{
				LOG.debug(msgId + SPACE + msg);
			}
			else if (level.equals(INFO))
			{
				LOG.info(msgId + SPACE + msg);
			}
			else if (level.equals(ERROR))
			{
				LOG.error(msgId + SPACE + msg);
			}
		}
	}



	/**
	 * checks whether prefetch is enabled
	 * @return boolean
	 */
	@Override
	public boolean isPrefetchEnabled()
	{
		return getRecommendationService().isPrefetchEnabled();
	}


	/**
	 * @return recommendationService
	 */
	public ProductRecommendationManagerUtil getRecommendationService()
	{
		return recommendationService;
	}

	/**
	 * @param recommendationService
	 */
	public void setRecommendationService(final ProductRecommendationManagerUtil recommendationService)
	{
		this.recommendationService = recommendationService;
	}


}
