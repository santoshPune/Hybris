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
package de.hybris.platform.sap.orderexchangeb2b.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hybris.platform.sap.orderexchangeb2b.outbound.impl.DefaultB2BOrderContributorTest;
import de.hybris.platform.sap.orderexchangeb2b.outbound.impl.DefaultB2BPartnerContributorTest;


@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses(
{ DefaultB2BPartnerContributorTest.class, DefaultB2BOrderContributorTest.class })
public class UnitTestSuite
{

}
