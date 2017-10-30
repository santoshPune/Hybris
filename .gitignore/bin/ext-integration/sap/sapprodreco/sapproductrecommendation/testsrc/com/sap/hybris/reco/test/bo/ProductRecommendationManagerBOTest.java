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
package com.sap.hybris.reco.test.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.hybris.reco.be.impl.OdataProductRecommendationManagerCEI;
import com.sap.hybris.reco.be.impl.ProductRecommendationManagerCEI;
import com.sap.hybris.reco.bo.ProductRecommendationManagerBO;
import com.sap.hybris.reco.bo.impl.ProductRecommendationManagerBOImpl;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.util.ProductRecommendationManagerUtil;


/**
 *
 */
@UnitTest
public class ProductRecommendationManagerBOTest {

	@Mock
	private ProductRecommendationManagerCEI backendObj;
	@Mock
	private OdataProductRecommendationManagerCEI oDataBackendObj;
	@Mock
	private ProductRecommendationManagerUtil util;

	private ProductRecommendationManagerBO recoBO;
	private RecommendationContext context;
	private List<RecommendationContext> contexts;
	private List<ProductRecommendationData> data;
	private boolean isPrefetchEnabled;
	private BackendException be;

	/**
	 *
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);

		context = new RecommendationContext();
		contexts = new ArrayList<RecommendationContext>();
		data = new ArrayList<ProductRecommendationData>();
		be = new BackendException(null);

		buildDataList();
		doReturn(data).when(backendObj).getProductRecommendation(context);
		doReturn(data).when(oDataBackendObj).getProductRecommendation(context);


	}

	private void buildDataList() {
		final ProductRecommendationData datum = new ProductRecommendationData();
		datum.setProductCode("M-01");
		data.add(datum);
		datum.setProductCode("M-09");
		data.add(datum);
		datum.setProductCode("M-13");
		data.add(datum);
	}


	/**
	 *
	 */
	@Test
	public void testRFCIsPrefetchEnabled(){
		recoBO = new ProductRecommendationManagerBOImpl();
      util.setPrefetchEnabled(true);
      Mockito.when(backendObj.isPrefetchEnabled()).thenReturn(true);
      recoBO.setBackendObject(backendObj);

      isPrefetchEnabled = recoBO.isPrefetchEnabled();
      assertEquals(true, isPrefetchEnabled);

//      Mockito.when(backendObj.isPrefetchEnabled()).thenThrow(be);
//      recoBO.isPrefetchEnabled();


	}

	/**
	 *
	 */
	@Test
	public void testRFCGetRecommendations() {
		recoBO = new ProductRecommendationManagerBOImpl();
		recoBO.setBackendObject(backendObj);
		final List<ProductRecommendationData> recos = recoBO.getProductRecommendation(context);
		assertNotNull(recos);
		assertEquals(recos, data);

//		Mockito.when(backendObj.getProductRecommendation(context)).thenThrow(new BackendException(null));
//		recoBO.getProductRecommendation(context);


	}

	/**
	 *
	 */
	@Test
	public void testRFCPrefetchRecommendations(){
		recoBO = new ProductRecommendationManagerBOImpl();
		recoBO.setBackendObject(backendObj);
		Mockito.when(backendObj.isPrefetchEnabled()).thenReturn(true);
		recoBO.prefetchRecommendations(contexts);
		Mockito.verify(backendObj).prefetchRecommendations(contexts);

//		doThrow(new BackendException(null)).when(recoBO).prefetchRecommendations(contexts);
//		recoBO.prefetchRecommendations(contexts);

	}

	/**
	 *
	 */
	@Test
	public void testODataIsPrefetchEnabled(){
		recoBO = new ProductRecommendationManagerBOImpl();
      util.setPrefetchEnabled(true);
      Mockito.when(oDataBackendObj.isPrefetchEnabled()).thenReturn(true);
      recoBO.setBackendObject(oDataBackendObj);

      isPrefetchEnabled = recoBO.isPrefetchEnabled();
      assertEquals(true, isPrefetchEnabled);

//      Mockito.when(oDataBackendObj.isPrefetchEnabled()).thenThrow(be);
//      isPrefetchEnabled = recoBO.isPrefetchEnabled();


	}


	/**
	 *
	 */
	@Test
	public void testODataGetRecommendations() {
		recoBO = new ProductRecommendationManagerBOImpl();
		recoBO.setBackendObject(oDataBackendObj);
		final List<ProductRecommendationData> recos = recoBO.getProductRecommendation(context);
		assertNotNull(recos);
		assertEquals(recos, data);

//		Mockito.when(backendObj.getProductRecommendation(context)).thenThrow(new Exception(""));
//		recoBO.getProductRecommendation(context);


	}

	/**
	 *
	 */
	@Test
	public void testODataPrefetchRecommendations(){
		recoBO = new ProductRecommendationManagerBOImpl();
		recoBO.setBackendObject(oDataBackendObj);

		Mockito.when(oDataBackendObj.isPrefetchEnabled()).thenReturn(true);
		recoBO.prefetchRecommendations(contexts);
		Mockito.verify(oDataBackendObj).prefetchRecommendations(contexts);

//		doThrow(new BackendException("")).when(oDataBackendObj).prefetchRecommendations(contexts);
//		recoBO.prefetchRecommendations(contexts);


	}


}
