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
package com.sap.wec.adtreco.targetgroups.tests;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.odata.util.ODataClientService;
import junit.framework.TestCase;

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

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import com.sap.wec.adtreco.be.intf.ADTInitiativesBE;
import com.sap.wec.adtreco.bo.impl.SAPInitiative;
import com.sap.wec.adtreco.bo.impl.SAPInitiativeReaderImpl;
import com.sap.wec.adtreco.bo.intf.SAPInitiativeReader;


@UnitTest
public class TestInitiativeSearch extends TestCase
{
	private SAPInitiativeReader reader;
	@Mock
	private ADTInitiativesBE mockBE;
	@Mock
	private HMCConfigurationReader mockConfig;
	@Mock
	private ODataClientService mockODataService;
	
	@Mock
	private SAPGlobalConfigurationService globalConfigurationService;
		
	List<SAPInitiative> campaignList = null;
	SAPInitiative campaign = null;
	private ODataFeed data;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		buildDataList();

		try
		{
			doReturn("EMAIL").when(mockConfig).getIdOrigin();
			doReturn("COOKIE_ID").when(mockConfig).getAnonIdOrigin();
			doReturn("").when(mockConfig).getFilterCategory();
			doReturn(data.getEntries().get(0)).when(mockBE).getInitiative(anyString(), anyString(), anyString());
			doReturn(data).when(mockBE).getInitiatives(anyString(), anyString(), anyString(), anyString(), anyString());
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void buildDataList() {
		List<ODataEntry> list = new ArrayList<ODataEntry>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Name", "CampaignTest1");
		map.put("InitiativeIdExt", "1234");
		map.put("Description", "Campaign Test 1");
		ODataEntry datum = new ODataEntryImpl(map,null,null,null,false);
		list.add(datum);
		
		map = new HashMap<String, Object>();
		map.put("Name", "CampaignTest2");
		map.put("InitiativeIdExt", "1235");
		map.put("Description", "Campaign Test 2");
		datum = new ODataEntryImpl(map,null,null,null,false);
		list.add(datum);
		
		data = new ODataDeltaFeedImpl(list, null);

	}

	@Test
	public void testSearchInitiatives()
	{		
		reader = new SAPInitiativeReaderImpl();
		reader.setConfiguration(mockConfig);
		reader.setAccessBE(mockBE);
		
		try
		{
			campaignList = reader.searchInitiatives("abc");
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
		assertEquals(campaignList.get(0).getId(), "1234");
		assertEquals(campaignList.size(), 2);
	}

	@Test
	public void testSearchInitiativesForUser()
	{		
		reader = new SAPInitiativeReaderImpl();
		reader.setConfiguration(mockConfig);
		reader.setAccessBE(mockBE);
		try
		{
			campaignList = reader.searchInitiativesForBP("hpa.demo@sap.com", false);
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
		assertEquals(campaignList.get(0).getId(), "1234");
	}

	@Test
	public void testSearchInitiativesForMultiUsers()
	{		
		reader = new SAPInitiativeReaderImpl();
		reader.setConfiguration(mockConfig);
		reader.setAccessBE(mockBE);
		
		String[] bps = {"hpa.demo@sap.com", "tester@hybris.com"};
		
		try
		{
			campaignList = reader.searchInitiativesForMultiBP(bps);
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
		assertNotNull(campaignList);
	}
	
	@Test
	public void testGetSelectedCampaign(){
		reader = new SAPInitiativeReaderImpl();
		reader.setConfiguration(mockConfig);
		reader.setAccessBE(mockBE);
		try
		{
			campaign = reader.getSelectedInitiative("1234");
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
		assertEquals(campaign.getDescription(), "Campaign Test 1");
	}
	
}
