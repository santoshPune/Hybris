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
package com.sap.hybris.reco.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sap.hybris.reco.test.bo.ProductRecommendationManagerBOTest;
import com.sap.hybris.reco.test.bo.SAPRecommendationItemDSReaderTest;
import com.sap.hybris.reco.test.bo.SAPRecommendationScenarioReaderTest;
import com.sap.hybris.reco.test.util.TestUserIdProvider;

@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses({	TestUserIdProvider.class, 
				ProductRecommendationManagerBOTest.class,
				SAPRecommendationScenarioReaderTest.class,
				SAPRecommendationItemDSReaderTest.class})
public class UnitTestSuite
{

}
