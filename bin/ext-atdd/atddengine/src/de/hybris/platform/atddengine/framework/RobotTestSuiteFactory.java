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
package de.hybris.platform.atddengine.framework;

import java.io.File;


public interface RobotTestSuiteFactory
{
	RobotTestSuite parseTestSuite(final File testSuiteFile);

	RobotTestSuite parseTestSuite(final File[] testSuiteFiles);

	RobotTestSuite parseTestSuite(final RobotSettings robotSettings, final File testSuiteFile);

	RobotTestSuite parseTestSuite(final RobotSettings robotSettings, final File[] testSuiteFiles);
}
