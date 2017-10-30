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

/**
 *
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v1
import static org.junit.Assert.fail

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config;
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.TestNamePrinter
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.TestUtil
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.config.TestConfigFactory
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.docu.BaseWSTestWatcher

import org.junit.Before;
import org.junit.Rule

/**
 * Base class for all groovy webservice tests
 */
@ManualTest
class BaseWSTest {
	protected final TestUtil testUtil;
	protected final ConfigObject config;

	@Before
	public void ignoreIf(){
		org.junit.Assume.assumeTrue(Config.getBoolean("ycommercewebservicestest.enableV1", false))
	}
	
	@Rule
	public TestNamePrinter tnp = new TestNamePrinter(System.out);

	protected BaseWSTest() {
		config = TestConfigFactory.createConfig("v1", "/ycommercewebservicestest/groovytests-property-file.groovy");
		testUtil = new TestUtil(config);
	}

	/**
	 * Ancillary method to mark that some webservice resources is not tested
	 * @param resource
	 */
	protected void missingTest(String resource) {
		fail("Missing test for resource :" + resource);
	}

	@Rule
	public BaseWSTestWatcher testWatcher = new BaseWSTestWatcher();
}