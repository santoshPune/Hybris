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
import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.odata.util.MyCallback;
import de.hybris.platform.sap.core.odata.util.ODataClientService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.feed.ODataDeltaFeedImpl;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.be.ProductRecommendationManagerBackend;
import com.sap.hybris.reco.bo.ProductRecommendationODataPrefetcher;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.util.ProductRecommendationManagerUtil;

import de.hybris.platform.recentvieweditemsservices.RecentViewedItemsService;

/**
 * Manager Layer For OData Calls regarding product recommendations
 */
@BackendType("CEI")
public class OdataProductRecommendationManagerCEI implements ProductRecommendationManagerBackend
{
	private final static Logger LOG = Logger.getLogger(OdataProductRecommendationManagerCEI.class.getName());

	private String serviceUri;
	private String entitySetName;
	private String baseURL;
	private ODataClientService clientService;
	private GenericFactory genericFactory;
	private ProductRecommendationManagerUtil recommendationService;

	private static final String APPLICATION_JSON = "application/json";
	private static final int NO_OF_RETRIES = 1;
	private static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_METHOD_POST = "POST";
	private static final String CSRF_TOKEN = "csrfToken";
	private static final String COOKIE = "cookie";
	private static final String EDM = "edm";
	private static final String USERID = "UserId";
	private static final String USERTYPE = "UserType";
	private static final String RESULT_OBJECT_ID = "ResultObjectId";
	private static final String LEADING_OBJECT_ID = "LeadingObjectId";
	private static final String LEADING_OBJECT_TYPE = "LeadingObjectType";
	private static final String BASKET_OBJECT_ID = "BasketObjectId";
	private static final String BASKET_OBJECT_TYPE = "BasketObjectType";
	private static final String INTERACTION_TYPE = "InteractionType";
	private static final String SOURCE_OBJECT_ID = "SourceObjectId";
	private static final String ITEM_TYPE = "ItemType";
	private static final String ITEM_ID = "ItemId";
	private static final String ITEM_NAV_URL = "ItemNavUrl";
	private static final String CLICK_THROUGH = "CLICK_THROUGH";
	private static final String PRI_CALLBACK = "PRICallback";
	private static final String IA_CALLBACK = "IACallback";
	private static final String TIMESTAMP = "TimeStamp";
	private static final String GMT = "GMT";


	/**
	 * Prefetch Product Recommendations from hybris Marketing based on contexts for the batch
	 *
	 * @param contexts
	 */
	@Override
	public void prefetchRecommendations(final List<RecommendationContext> contexts)
	{
		if (getRecommendationService().isPrefetchEnabled())
		{
			prefetchProductRecommendationsByScenario(contexts);
		}
	}

	/**
	 * Prefetch Product Recommendations from hybris Marketing based on scenario
	 *
	 * @param contexts
	 */
	private void prefetchProductRecommendationsByScenario(final List<RecommendationContext> contexts)
	{

		final Map<String, Object> recommendationScenario = createPayload(contexts);
		final Map<String, List<String>> recommendationMap = new HashMap<>();
		getRecommendations(recommendationScenario, recommendationMap, NO_OF_RETRIES);
		storeRecommendationsInSession(contexts, recommendationMap);
	}

	/**
	 * Creates payload for backend PRI call using OData
	 *
	 * @param contexts
	 * @return recommendendationScenario
	 */
	private Map<String, Object> createPayload(final List<RecommendationContext> contexts)
	{
		final Map<String, Object> recommendendationScenario = new HashMap<>();

		recommendendationScenario.put(USERID, contexts.get(0).getUserId());
		recommendendationScenario.put(USERTYPE, contexts.get(0).getUserType());
		recommendendationScenario.put(SapproductrecommendationConstants.CONTEXT_PARAMS, null);
		recommendendationScenario.put(SapproductrecommendationConstants.RESULT_OBJECTS, null);
		final List<Map<String, Object>> scenarios = new ArrayList<>();
		for (final RecommendationContext context : contexts)
		{
			final Map<String, Object> scenario = new HashMap<>();

			scenario.put(SapproductrecommendationConstants.SCENARIO_ID, context.getRecotype());
			final List<Map<String, String>> leadingObjects = new ArrayList<>();
			Map<String, String> leadingObject;
			final List<String> leadingItems = getRecommendationService().getLeadingItemId(context);
			if (leadingItems.size() > 0)
			{
				for (int i = 0; i < leadingItems.size(); i++)
				{
					leadingObject = new HashMap<>();
					leadingObject.put(LEADING_OBJECT_ID, context.getLeadingItemId());
					leadingObject.put(LEADING_OBJECT_TYPE, context.getLeadingItemDSType());
					leadingObjects.add(leadingObject);
				}
			}

			final List<Map<String, String>> basketObjects = new ArrayList<>();
			addCartItems(basketObjects, context);
			if (context.isIncludeCart())
			{
				addCartItemsAsLeadingItems(leadingObjects, context);
			}
			if (context.isIncludeRecent())
			{
				addRecentItemsAsLeadingItems(leadingObjects, context);
			}

			scenario.put(SapproductrecommendationConstants.LEADING_OBJECTS, leadingObjects);
			scenario.put(SapproductrecommendationConstants.BASKET_OBJECTS, basketObjects);
			scenarios.add(scenario);
		}
		recommendendationScenario.put(SapproductrecommendationConstants.SCENARIOS, scenarios);
		return recommendendationScenario;
	}

	/**
	 * Creates payload for backend Interaction call using OData
	 *
	 * @param contexts
	 * @return recommendendationScenario
	 */
	private Map<String, Object> createInteractionPayload(final InteractionContext context)
	{
		final Map<String, Object> interactionScenario = new HashMap<>();
		interactionScenario.put(SapproductrecommendationConstants.SCENARIO_ID, context.getScenarioId());
		interactionScenario.put(USERID, context.getUserId());
		interactionScenario.put(USERTYPE, context.getUserType());
		interactionScenario.put(INTERACTION_TYPE, new String(CLICK_THROUGH));
		interactionScenario.put(TIMESTAMP, getInteractionTimeStamp());
		interactionScenario.put(SOURCE_OBJECT_ID, context.getSourceObjectId());
		final List<Map<String, String>> interactionItems = new ArrayList<>();
		final Map<String, String> interactionItem = new HashMap<>();
		interactionItem.put(ITEM_TYPE, context.getProductType());
		interactionItem.put(ITEM_ID, context.getProductId());
		interactionItem.put(ITEM_NAV_URL, context.getProductNavURL());
		interactionItems.add(interactionItem);

		interactionScenario.put(SapproductrecommendationConstants.INTERACTION_ITEMS, interactionItems);// TODO [can be multiple]
		return interactionScenario;
	}

	private Long getInteractionTimeStamp()
	{
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(GMT));
		return cal.getTime().getTime();
	}


	/**
	 * Adds cart items to payload
	 *
	 * @param basketObjects
	 * @param context
	 */
	private void addCartItems(final List<Map<String, String>> basketObjects, final RecommendationContext context)
	{
		final List<String> cartItems = getCartItemsFromSession();
		for (final String cartItem : cartItems)
		{
			final Map<String, String> basketObject = new HashMap<>();
			basketObject.put(BASKET_OBJECT_ID, cartItem);
			basketObject.put(BASKET_OBJECT_TYPE, context.getCartItemDSType());
			basketObjects.add(basketObject);
		}
	}

	/**
	 * Adds cart items as leading objects to payload
	 *
	 * @param leadingObjects
	 * @param context
	 */
	private void addCartItemsAsLeadingItems(final List<Map<String, String>> leadingObjects, final RecommendationContext context)
	{
		final List<String> cartItems = getCartItemsFromSession();
		for (final String cartItem : cartItems)
		{
			final Map<String, String> leadingObject = new HashMap<>();
			leadingObject.put(LEADING_OBJECT_ID, cartItem);
			leadingObject.put(LEADING_OBJECT_TYPE, context.getCartItemDSType());
			leadingObjects.add(leadingObject);
		}
	}
	
	/**
	 * Adds recent items as leading objects to payload
	 *
	 * @param leadingObjects
	 * @param context
	 */
	private void addRecentItemsAsLeadingItems(final List<Map<String, String>> leadingObjects, final RecommendationContext context)
	{
		final List<String> recentItems = this.getRecentItemsFromSession(context.getLeadingItemType());
		for (final String recentItem : recentItems)
		{
			final Map<String, String> leadingObject = new HashMap<>();
			leadingObject.put(LEADING_OBJECT_ID, recentItem);
			leadingObject.put(LEADING_OBJECT_TYPE, context.getLeadingItemDSType());
			leadingObjects.add(leadingObject);
		}
	}

	/**
	 * Fetches the cart items from session
	 *
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
	 * Fetches the recent items from session
	 *
	 * @return cartItems
	 */
	private List<String> getRecentItemsFromSession(final String leadingItemType)
	{
		if(leadingItemType.equals(SapproductrecommendationConstants.PRODUCT)) 
		{
			return recommendationService.getRecentViewedItemsService().getRecentViewedProducts();
		}
		if (leadingItemType.equals(SapproductrecommendationConstants.CATEGORY))
		{
			return recommendationService.getRecentViewedItemsService().getRecentViewedCategories();
		}
		return null;
	}

	/**
	 * Fetch Product Recommendations from hybris Marketing based on scenario
	 *
	 * @param recommendationScenario
	 * @param recommedationsMap
	 * @param retriesLeft
	 * @return list of recommendations
	 */
	private List<String> getRecommendations(final Map<String, Object> recommendationScenario,
			final Map<String, List<String>> recommedationsMap, final int retriesLeft)
	{
		final String contentType = APPLICATION_JSON;
		final String httpMethod = HTTP_METHOD_POST;
		final String user = getRecommendationService().getConfiguration().getHttpDestination().getUserid();
		final String password = getRecommendationService().getConfiguration().getHttpDestination().getPassword();
		final String client = getRecommendationService().getConfiguration().getHttpDestinationSAPClient();
		serviceUri = getRecommendationService().getConfiguration().getHttpDestinationURL() + baseURL;
		entitySetName = SapproductrecommendationConstants.RECOMMENDATION_SCENARIOS;
		List<String> productRecommendations;
		List<String> recotypes = new ArrayList<>();
		try
		{

			final List<String> entities = Arrays.asList(SapproductrecommendationConstants.SCENARIOS,
					SapproductrecommendationConstants.CONTEXT_PARAMS, SapproductrecommendationConstants.RESULT_OBJECTS);

			Map<String, Object> headerValues = null;
			synchronized (getRecommendationService().getSessionService().getCurrentSession())
			{

				if (getRecommendationService().getSessionService().getAttribute(CSRF_TOKEN) != null
						&& getRecommendationService().getSessionService().getAttribute(COOKIE) != null
						&& getRecommendationService().getSessionService().getAttribute(EDM) != null)
				{
					headerValues = new HashMap<String, Object>();
					headerValues.put(CSRF_TOKEN, getRecommendationService().getSessionService().getAttribute(CSRF_TOKEN));
					headerValues.put(COOKIE, getRecommendationService().getSessionService().getAttribute(COOKIE));
					headerValues.put(EDM, getRecommendationService().getSessionService().getAttribute(EDM));
				}
				else
				{
					headerValues = this.getClientService().getCSRFAndCookie(serviceUri,
							SapproductrecommendationConstants.APPLICATION_XML, HTTP_METHOD_GET, user, password);


					if (headerValues.containsKey(CSRF_TOKEN) && headerValues.get(CSRF_TOKEN) != null)
					{
						getRecommendationService().getSessionService().setAttribute(CSRF_TOKEN, headerValues.get(CSRF_TOKEN));
					}
					if (headerValues.containsKey(COOKIE) && headerValues.get(COOKIE) != null)
					{
						getRecommendationService().getSessionService().setAttribute(COOKIE, headerValues.get(COOKIE));
					}
					if (headerValues.containsKey(EDM) && headerValues.get(EDM) != null)
					{
						getRecommendationService().getSessionService().setAttribute(EDM, headerValues.get(EDM));
					}
				}

			}
			final Future<ODataEntry> future = getRecommendationService().getThreadPool()
					.submit(new ProductRecommendationODataPrefetcher(this.getClientService(), serviceUri, entitySetName,
							recommendationScenario, contentType, httpMethod, user, password, client, entities, headerValues,
							getMyCallback(PRI_CALLBACK)));

			final Map<String, Object> properties = future
					.get(getRecommendationService().getRetrieveWaitTimeoutMs(), TimeUnit.MILLISECONDS).getProperties();
			if (properties != null && properties.size() > 0)
			{
				for (final Entry<String, Object> entrye : properties.entrySet())
				{
					if (entrye.getKey().contains(SapproductrecommendationConstants.RESULT_OBJECTS))
					{
						for (final ODataEntry e : ((ODataDeltaFeedImpl) entrye.getValue()).getEntries())
						{
							final Map<String, Object> properties2 = e.getProperties();
							if (properties2 != null && properties2.size() > 0)
							{
								String scenarioId = null;
								for (final Entry<String, Object> e2 : properties2.entrySet())
								{
									if (e2.getKey().contains(SapproductrecommendationConstants.SCENARIO_ID))
									{
										scenarioId = e2.getValue().toString();
										if (!recotypes.contains(scenarioId))
										{
											recotypes.add(scenarioId);
										}
									}
									if (e2.getKey().contains(RESULT_OBJECT_ID))
									{
										final String leadingItemId = e2.getValue().toString();
										productRecommendations = recommedationsMap.get(scenarioId);
										if (productRecommendations == null)
										{
											productRecommendations = new ArrayList<>();
										}
										productRecommendations.add(leadingItemId);
										recommedationsMap.put(scenarioId, productRecommendations);
									}

								}
							}
						}
					}
				}
			}
		}
		catch (InterruptedException | ExecutionException | TimeoutException e)
		{
			LOG.error("Error getting Recommendations from Backend System: " + e.getClass().getName() );
		}
		catch (final NullPointerException e)
		{

			LOG.error("NullPointerException while getting Recommendations from Backend System");
			synchronized (getRecommendationService().getSessionService().getCurrentSession())
			{
				if (retriesLeft > 0)
				{
					getRecommendationService().getSessionService().removeAttribute(CSRF_TOKEN);
					getRecommendationService().getSessionService().removeAttribute(COOKIE);
					getRecommendationService().getSessionService().removeAttribute(EDM);
					recotypes = this.getRecommendations(recommendationScenario, recommedationsMap, 0);
				}
			}
		}

		return recotypes;
	}

	/**
	 * For storing recommendations in session
	 *
	 * @param contexts
	 * @param results
	 */
	private void storeRecommendationsInSession(final List<RecommendationContext> contexts, final Map<String, List<String>> results)
	{
		for (final RecommendationContext context : contexts)
		{
			final List<String> recommendationIds = getRecommendationsForRecoType(results, context.getRecotype());
			getRecommendationService().getSessionService().setAttribute(
					getRecommendationService().getComponentSessionKey(context.getComponentModel().getUid()), recommendationIds);
		}
	}


	/**
	 * Fetches the product recommendations for a particular recommendation type
	 *
	 * @param results
	 * @param recotype
	 * @return Recommendations for a particular recotype
	 */
	private List<String> getRecommendationsForRecoType(final Map<String, List<String>> results, final String recotype)
	{
		final List<String> recommendations = new ArrayList<String>();
		final List<String> recoIds = results.get(recotype);
		if (recoIds != null && !recoIds.isEmpty())
		{
			final int len = recoIds.size();
			for (int i = 0; i < len; i++)
			{
				final String recommendationId = recoIds.get(i);
				recommendations.add(recommendationId);
			}
		}
		return recommendations;
	}

	/**
	 * Gets the recommendations from the session if prefetch is enabled else from hybris marketing
	 */
	@Override
	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{
		if (getRecommendationService().isPrefetchEnabled())
		{
			LOG.debug("\n\n PREFETCH ENABLED : ODATA \n\n");
			return getRecommendationService().getPrefetchedResult(context);
		}
		if (getRecommendationService().getConfiguration().getUsage().equals(SapproductrecommendationConstants.SCENARIO))
		{
			LOG.debug("\n\n PREFETCH DISABLED : ODATA \n\n");
			return getProductRecommendationByScenario(context);
		}
		return null;
	}

	/**
	 * Gets the recommendations from hybris marketing based on current context
	 *
	 * @param context
	 * @return List of recommendations
	 */
	private List<ProductRecommendationData> getProductRecommendationByScenario(final RecommendationContext context)
	{
		final List<RecommendationContext> contexts = Lists.newArrayList();
		contexts.add(context);
		final Map<String, Object> recommendationScenario = createPayload(contexts);
		final Map<String, List<String>> recommendationMap = new HashMap<>();
		final List<String> recoTypes = getRecommendations(recommendationScenario, recommendationMap, NO_OF_RETRIES);
		List<String> recoIds = Lists.newArrayList();
		if (recoTypes != null && !recoTypes.isEmpty())
		{
			if (recommendationMap.get(recoTypes.get(0)) != null)
			{
				recoIds = recommendationMap.get(recoTypes.get(0));
			}
		}
		final List<ProductRecommendationData> result = Lists.newArrayList();
		if (recoIds != null && !recoIds.isEmpty())
		{
			for (final String recommendationId : recoIds)
			{
				result.add(getRecommendationService().createProductRecommedation(recommendationId));
			}
		}

		return result;
	}


	/**
	 * Posts the user interactions to the hybris Marketing.
	 *
	 * @param context
	 */
	@Override
	public void postInteraction(final InteractionContext context)
	{
		LOG.debug("\n\n POST INTERACTION : ODATA \n\n");

		final String contentType = APPLICATION_JSON;
		final String httpMethod = HTTP_METHOD_POST;
		final String user = getRecommendationService().getConfiguration().getHttpDestination().getUserid();
		final String password = getRecommendationService().getConfiguration().getHttpDestination().getPassword();
		final String client = getRecommendationService().getConfiguration().getHttpDestinationSAPClient();
		serviceUri = getRecommendationService().getConfiguration().getHttpDestinationURL() + baseURL;
		entitySetName = SapproductrecommendationConstants.INTERACTIONS;


		try
		{
			Map<String, Object> headerValues = null;
			if (getRecommendationService().getSessionService().getAttribute(CSRF_TOKEN) != null
					&& getRecommendationService().getSessionService().getAttribute(COOKIE) != null
					&& getRecommendationService().getSessionService().getAttribute(EDM) != null)
			{
				headerValues = new HashMap<String, Object>();
				headerValues.put(CSRF_TOKEN, getRecommendationService().getSessionService().getAttribute(CSRF_TOKEN));
				headerValues.put(COOKIE, getRecommendationService().getSessionService().getAttribute(COOKIE));
				headerValues.put(EDM, getRecommendationService().getSessionService().getAttribute(EDM));
			}
			else
			{
				headerValues = this.getClientService().getCSRFAndCookie(serviceUri, SapproductrecommendationConstants.APPLICATION_XML,
						HTTP_METHOD_GET, user, password);
				if (headerValues.containsKey(CSRF_TOKEN) && headerValues.get(CSRF_TOKEN) != null)
				{
					getRecommendationService().getSessionService().setAttribute(CSRF_TOKEN, headerValues.get(CSRF_TOKEN));
				}
				if (headerValues.containsKey(COOKIE) && headerValues.get(COOKIE) != null)
				{
					getRecommendationService().getSessionService().setAttribute(COOKIE, headerValues.get(COOKIE));
				}
				if (headerValues.containsKey(EDM) && headerValues.get(EDM) != null)
				{
					getRecommendationService().getSessionService().setAttribute(EDM, headerValues.get(EDM));
				}
			}

			final List<String> entities = Arrays.asList(SapproductrecommendationConstants.INTERACTION_ITEMS);
			this.clientService.writeEntity(serviceUri, entitySetName, createInteractionPayload(context), contentType, httpMethod,
					user, password, client, entities, headerValues, getMyCallback(IA_CALLBACK));
			LOG.debug("\n ODATA : Interaction posted for product click \n");
		}
		catch (final NullPointerException | EdmException | EntityProviderException | IOException | URISyntaxException e)
		{
			LOG.error("Posting Interaction failed due to " + e.getClass().getName());
		}
	}

	/**
	 * @return clientService
	 */
	public ODataClientService getClientService()
	{
		return clientService;
	}

	/**
	 * @param clientService
	 */
	public void setClientService(final ODataClientService clientService)
	{
		this.clientService = clientService;
	}

	/**
	 * initialize Backend Object
	 */
	@Override
	public void initBackendObject() throws BackendException
	{

	}

	/**
	 * destroy Backend Object
	 */
	@Override
	public void destroyBackendObject()
	{
	}

	/**
	 * @return prefetchEnabled
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

	/**
	 * @return genericFactory
	 */
	public GenericFactory getGenericFactory()
	{
		return genericFactory;
	}

	/**
	 * @param genericFactory
	 */
	public void setGenericFactory(final GenericFactory genericFactory)
	{
		this.genericFactory = genericFactory;
	}

	/**
	 * @param callbackBean
	 * @return ProdRecoCallback
	 */
	public MyCallback getMyCallback(final String callbackBean)
	{
		return getGenericFactory().getBean(callbackBean);
	}

	/**
	 *
	 * @return baseURL
	 */
	public String getBaseURL()
	{
		return baseURL;
	}

	/**
	 *
	 * @param baseURL
	 */
	public void setBaseURL(final String baseURL)
	{
		this.baseURL = baseURL;
	}

}
