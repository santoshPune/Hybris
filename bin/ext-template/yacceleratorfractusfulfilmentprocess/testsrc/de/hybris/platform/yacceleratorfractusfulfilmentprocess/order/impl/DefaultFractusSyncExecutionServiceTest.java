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
*
*/
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Service Tests.
 */
@UnitTest
public class DefaultFractusSyncExecutionServiceTest
{

	private DefaultFractusSyncExecutionService syncExecutionService;

	@Before
	public void setup()
	{
		syncExecutionService = new DefaultFractusSyncExecutionService();
	}

	@Test
	public void testAppendWhereClauseWithAndShouldNotChangeWhereClause()
	{
		final String originalWhereClause = StringUtils.EMPTY;
		final String newWhereClause = "new_wherecaluse";

		final String result = syncExecutionService.appendWhereClauseWithAnd(originalWhereClause, newWhereClause);

		Assert.assertEquals(newWhereClause, result);
	}

	@Test
	public void testAppendWhereClauseWithAndShouldAppenndWhereClause()
	{
		final String originalWhereClause = "original_whereclause";
		final String newWhereClause = "new_wherecaluse";

		final String result = syncExecutionService.appendWhereClauseWithAnd(originalWhereClause, newWhereClause);

		Assert.assertEquals(originalWhereClause + " AND " + newWhereClause, result);
	}
}
