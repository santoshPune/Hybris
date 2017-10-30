package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons.v2.spock


import de.hybris.platform.oauth2.constants.OAuth2Constants
import de.hybris.platform.util.Config
import de.hybris.platform.ycommercewebservicestest.constants.YcommercewebservicestestConstants
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.TestSetupUtils

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([SopTest, ExtendedCartV2Tests])
class AllAccSpockTests {
	@BeforeClass
	public static void setUpClass() {
		if (Config.getBoolean("ycommercewebservicestest.enableAccTest", false)) {
			TestSetupUtils.loadData();
			String[] ext = [
				YcommercewebservicestestConstants.EXTENSIONNAME - "test",
				OAuth2Constants.EXTENSIONNAME,
				"acceleratorservices"
			]
			TestSetupUtils.startServer(ext);
		}
	}

	@AfterClass
	public static void tearDown(){
		if (Config.getBoolean("ycommercewebservicestest.enableAccTest", false)) {
			TestSetupUtils.stopServer();
			TestSetupUtils.cleanData();
		}
	}

	@Test
	public static void testing() {
		//dummy test class
	}
}
