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

import java.util.List;


public interface RobotTestSuite
{
	void close();

	String getName();

	RobotTest getRobotTest(final String testName);

	List<RobotTest> getRobotTests();

	boolean isClosed();

	boolean isStarted();

	RobotTestResult run(final RobotTest robotTest);

	void start();
}
