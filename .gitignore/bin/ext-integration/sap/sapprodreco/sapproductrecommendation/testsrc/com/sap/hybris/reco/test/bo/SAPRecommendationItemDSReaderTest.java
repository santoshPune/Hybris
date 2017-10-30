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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.ep.feed.ODataDeltaFeedImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Matchers.anyString;

import com.sap.hybris.reco.be.impl.RecommendationEntityManagerCEI;
import com.sap.hybris.reco.bo.SAPRecommendationItemDSTypeReader;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.SAPRecommendationItemDataSourceType;

import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class SAPRecommendationItemDSReaderTest {
	
	@Mock
	private RecommendationEntityManagerCEI accessBE;
	private SAPRecommendationItemDSTypeReader recoBO;
	private ODataFeed data;
	private HMCConfigurationReader hmcConfig;
			
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		buildDataList();
		hmcConfig = new HMCConfigurationReader();

		try {			
			doReturn(hmcConfig).when(accessBE).getConfiguration();
			doReturn("SCENARIO").when(accessBE).getUsage();
			doReturn(data).when(accessBE).getTypes(anyString(), anyString(), anyString(), anyString(), anyString());
		} catch (ODataException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void buildDataList() {
		List<ODataEntry> list = new ArrayList<ODataEntry>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ItemSourceObjectType", "ItemDSType1");
		map.put("ItemSourceTypeDescription", "ItemDSType1_DESC");
		ODataEntry datum = new ODataEntryImpl(map,null,null,null,false);
		list.add(datum);
		
		map = new HashMap<String, Object>();
		map.put("ItemSourceObjectType", "ItemDSType2");
		map.put("ItemSourceTypeDescription", "ItemDSType2_DESC");
		datum = new ODataEntryImpl(map,null,null,null,false);
		list.add(datum);
		
		data = new ODataDeltaFeedImpl(list, null);
	}

	@Test
	public void testGetItemDSTypeScenario() {
		
		hmcConfig.setUsage(SapproductrecommendationConstants.SCENARIO);
		recoBO = new SAPRecommendationItemDSTypeReader();
		recoBO.setAccessBE(accessBE);

		try {
			List<SAPRecommendationItemDataSourceType> typesList = recoBO.getAllItemDSTypes();
			assertNotNull(typesList);
			assertEquals(typesList.size(),2);
		} catch (ODataException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}				
	}
	
	@Test
	public void testGetItemDSTypeModelType() {

		hmcConfig.setUsage(SapproductrecommendationConstants.MODELTYPE);
		recoBO = new SAPRecommendationItemDSTypeReader();
		recoBO.setAccessBE(accessBE);

		try {
			List<SAPRecommendationItemDataSourceType> typesList = recoBO.getAllItemDSTypes();
			assertNotNull(typesList);
			assertEquals(typesList.size(),2);
		} catch (ODataException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}				
	}
}
