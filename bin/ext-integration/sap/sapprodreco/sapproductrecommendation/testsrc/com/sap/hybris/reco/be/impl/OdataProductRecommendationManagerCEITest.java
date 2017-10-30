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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.odata.util.ODataClientService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.MediaMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.ep.feed.FeedMetadataImpl;
import org.apache.olingo.odata2.core.ep.feed.ODataDeltaFeedImpl;
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.hybris.reco.bo.ProductRecommendationODataPrefetcher;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;
import com.sap.hybris.reco.util.ProdRecoCallback;
import com.sap.hybris.reco.util.ProductRecommendationManagerUtil;


/**
 *
 */
@UnitTest
public class OdataProductRecommendationManagerCEITest
{
	@InjectMocks
	OdataProductRecommendationManagerCEI odataProductRecommendationManagerCEI;
	RecommendationContext context;
	List<RecommendationContext> contexts;
	Map<String, Object> headerValues;
	ODataEntry entry;
	ProductRecommendationData productRecommendationData;

	@Mock
	ProductRecommendationManagerUtil recommendationService;
	@Mock
	CartService cartService;
	@Mock
	CartModel cartModel;
	@Mock
	HMCConfigurationReader configuration;
	@Mock
	HTTPDestination httpDestination;
	@Mock
	SessionService sessionService;
	@Mock
	Session session;
	@Mock
	ODataClientService clientService;
	@Mock
	ProdRecoCallback prodRecoCallback;
	@Mock
	CMSSAPRecommendationComponentModel cMSSAPRecommendationComponentModel;
	@Mock
	Future<ODataEntry> future;
	@Mock
	ExecutorService executorService;
	@Mock
	GenericFactory genericFactory;
	@Mock
	Map<String, Object> properties;
	@Mock
	ODataEntry oDataEntry;
	@Mock
	List<ODataEntry> odataEntries;

	/**
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws EntityProviderException
	 * @throws EdmException
	 *
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws InterruptedException, ExecutionException, TimeoutException, EdmException, EntityProviderException,
			MalformedURLException, IOException, URISyntaxException
	{
		MockitoAnnotations.initMocks(this);

		odataProductRecommendationManagerCEI = new OdataProductRecommendationManagerCEI();
		odataProductRecommendationManagerCEI.setRecommendationService(recommendationService);
		odataProductRecommendationManagerCEI.setClientService(clientService);
		odataProductRecommendationManagerCEI.setGenericFactory(genericFactory);

		buildData();
		final List<String> leadingItems = new ArrayList<>();
		leadingItems.add("M-01");
		final AbstractOrderEntryModel abstractOrderEntryModel = new AbstractOrderEntryModel();
		final ProductModel productModel = new ProductModel();
		productModel.setCode("5555");
		abstractOrderEntryModel.setProduct(productModel);
		final List<AbstractOrderEntryModel> cartEntries = new ArrayList<>();
		cartEntries.add(abstractOrderEntryModel);

		when(recommendationService.isPrefetchEnabled()).thenReturn(true);
		when(recommendationService.getCartService()).thenReturn(cartService);
		when(recommendationService.getLeadingItemId(context)).thenReturn(leadingItems);
		when(cartService.getSessionCart()).thenReturn(cartModel);

		when(cartModel.getEntries()).thenReturn(cartEntries);
		when(recommendationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getHttpDestination()).thenReturn(httpDestination);
		when(httpDestination.getUserid()).thenReturn("reco_model");
		when(httpDestination.getPassword()).thenReturn("welcome");
		when(configuration.getHttpDestinationURL()).thenReturn("https://ldciant.wdf.sap.corp:44310");
		when(recommendationService.getSessionService()).thenReturn(sessionService);
		when(cMSSAPRecommendationComponentModel.getUid()).thenReturn("comp_000007PS");
		when(sessionService.getCurrentSession()).thenReturn(session);
		when(recommendationService.getThreadPool()).thenReturn(executorService);
		when(executorService.submit(any(ProductRecommendationODataPrefetcher.class))).thenReturn(future);
		when(genericFactory.getBean("PRICallback")).thenReturn(prodRecoCallback);
		when(future.get(any(Long.class), any(TimeUnit.class))).thenReturn(entry);

	}


	/**
	 * Sample Data required for junit test cases
	 */
	private void buildData()
	{
		context = new RecommendationContext();
		context.setRecotype("THEO_SCENARIO");
		context.setComponentModel(cMSSAPRecommendationComponentModel);
		context.setLeadingItemId("M-01");
		context.setLeadingItemDSType("SAP_ERP_MATNR");
		context.setIncludeCart(true);
		contexts = new ArrayList<>();
		contexts.add(context);
		headerValues = new HashMap<>();
		headerValues.put("csrf", new String("csrf"));
		headerValues.put("cookie", new String("cookie"));
		headerValues.put("edm", new String("edm"));

		productRecommendationData = new ProductRecommendationData();
		productRecommendationData.setProductCode("1234");

		final EntryMetadata emd = new EntryMetadataImpl();
		final MediaMetadata mmd = new MediaMetadataImpl();
		final ExpandSelectTreeNodeImpl node = new ExpandSelectTreeNodeImpl();
		node.setExpanded();
		node.setExplicitlySelected();
		final Map<String, Object> objectData = new HashMap<String, Object>();
		final List<ODataEntry> list = new ArrayList<ODataEntry>();
		final Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ScenarioId", "THEO_SCENARIO");
		map1.put("ResultObjectScore", 0.41132);
		map1.put("ResultObjectId", 1992693);
		map1.put("ResultObjectType", "SAP_HYBRIS_PRODUCT");
		final EntryMetadata emd1 = new EntryMetadataImpl();
		final MediaMetadata mmd1 = new MediaMetadataImpl();
		final ExpandSelectTreeNodeImpl node1 = new ExpandSelectTreeNodeImpl();
		final ODataEntry entry1 = new ODataEntryImpl(map1, mmd1, emd1, node1);
		list.add(entry1);
		final ODataDeltaFeedImpl feed = new ODataDeltaFeedImpl(list, new FeedMetadataImpl());
		objectData.put("ResultObjects", feed);
		entry = new ODataEntryImpl(objectData, mmd, emd, node, true);
	}


	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws EntityProviderException
	 * @throws EdmException
	 *
	 */
	@Test
	public void testGetRecommendationsPrefetchEnabledHeaderValuesNull()
			throws EdmException, EntityProviderException, MalformedURLException, IOException, URISyntaxException
	{
		when(clientService.getCSRFAndCookie(any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(headerValues);
		odataProductRecommendationManagerCEI.prefetchRecommendations(contexts);
		verify(recommendationService, times(1)).getLeadingItemId(any(RecommendationContext.class));
		//verify(recommendationService.getConfiguration(), times(1)).getHttpDestination(); //TODO
		verify(clientService, times(1)).getCSRFAndCookie(any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class));
	}

	/**
	 *
	 */
	@Test
	public void testGetRecommendationsPrefetchEnabledException()
	{
		when(clientService.getCSRFAndCookie(any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class))).thenReturn(headerValues);
		when(recommendationService.getSessionService()).thenReturn(null).thenReturn(sessionService);
		odataProductRecommendationManagerCEI.prefetchRecommendations(contexts);
		verify(recommendationService, times(1)).getLeadingItemId(any(RecommendationContext.class));
		//verify(recommendationService.getConfiguration(), times(2)).getHttpDestination(); //TODO
		verify(clientService, times(1)).getCSRFAndCookie(any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class));
	}

	/**
	 * @throws EdmException
	 * @throws EntityProviderException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetRecommendationsPrefetchEnabledHeaderValuesNotNull()
			throws EdmException, EntityProviderException, MalformedURLException, IOException, URISyntaxException
	{
		when(sessionService.getAttribute("csrfToken")).thenReturn("csrf");
		when(sessionService.getAttribute("cookie")).thenReturn("cookie");
		when(sessionService.getAttribute("edm")).thenReturn("edm");
		odataProductRecommendationManagerCEI.prefetchRecommendations(contexts);
		verify(recommendationService, times(1)).getLeadingItemId(any(RecommendationContext.class));
		//verify(recommendationService.getConfiguration(), times(1)).getHttpDestination(); //TODO
		verify(clientService, never()).getCSRFAndCookie(any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class));
	}

	/**
	 *
	 */
	@Test
	public void testGetRecommendationsPrefetchDiasabled()
	{
		when(this.recommendationService.isPrefetchEnabled()).thenReturn(false);
		odataProductRecommendationManagerCEI.prefetchRecommendations(contexts);
		verify(recommendationService.getConfiguration(), never()).getHttpDestination(); //TODO
	}

	/**
	 *
	 */
	@Test
	public void testGetProductRecommendationTestPrefetchEnabled()
	{
		final List<ProductRecommendationData> productRecommendations = new ArrayList<>();
		when(this.recommendationService.isPrefetchEnabled()).thenReturn(true);
		when(this.recommendationService.getPrefetchedResult(any(RecommendationContext.class))).thenReturn(productRecommendations);
		final List<ProductRecommendationData> result = odataProductRecommendationManagerCEI.getProductRecommendation(context);
		assertEquals(result.size(), 0);
	}

	/**
	 *
	 */
	@Test
	public void testGetProductRecommendationByScenarioTestPrefetchDisabled()
	{
		when(this.recommendationService.isPrefetchEnabled()).thenReturn(false);
		when(configuration.getUsage()).thenReturn(SapproductrecommendationConstants.SCENARIO);
		when(recommendationService.createProductRecommedation(any(String.class))).thenReturn(productRecommendationData);
		final List<ProductRecommendationData> result = odataProductRecommendationManagerCEI.getProductRecommendation(context);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getProductCode(), "1234");
	}

	/**
	 *
	 */
	@Test
	public void testGetProductRecommendationTestPrefetchDisabled()
	{
		when(this.recommendationService.isPrefetchEnabled()).thenReturn(false);
		when(configuration.getUsage()).thenReturn("garbage");
		final List<ProductRecommendationData> result = odataProductRecommendationManagerCEI.getProductRecommendation(context);
		assertEquals(result, null);
	}

	/**
	 *
	 */
	@Test
	public void testPostInteraction()
	{
		final InteractionContext interactionContext = new InteractionContext();
		odataProductRecommendationManagerCEI.postInteraction(interactionContext);
	}

}
