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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.odata.util.ODataClientService;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import com.sap.wec.adtreco.be.intf.ADTInitiativesBE;
import com.sap.wec.adtreco.bo.impl.SAPInitiative;
import com.sap.wec.adtreco.bo.intf.SAPInitiativeReader;

@UnitTest
public class TestInitiativeConfig extends TestCase
{
	@Mock
	private SAPInitiativeReader mockReader;
	@Mock
	private ADTInitiativesBE mockBE;
	@Mock
	private HMCConfigurationReader mockConfig;
	@Mock
	private ODataClientService mockODataService;
		
	List<SAPInitiative> campaignList = null;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		SAPInitiative campaign = new SAPInitiative();
		campaign.setDescription("campaign_test");
		campaign.setId("1111");
		campaign.setMemberCount("105");
		campaignList = new ArrayList<>();
		campaignList.add(campaign);
	}
	
	@Test
	public void testId(){
		assertEquals(campaignList.get(0).getId(), "1111");
	}
	
	@Test
	public void testMemberCount(){
		assertEquals(campaignList.get(0).getMemberCount(), "105");
	}
	
}
