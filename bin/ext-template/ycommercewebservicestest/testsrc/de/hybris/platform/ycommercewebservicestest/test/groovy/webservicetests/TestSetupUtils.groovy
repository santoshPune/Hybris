package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests

import de.hybris.platform.basecommerce.model.site.BaseSiteModel
import de.hybris.platform.catalog.jalo.CatalogManager
import de.hybris.platform.core.Initialization
import de.hybris.platform.core.Registry
import de.hybris.platform.core.initialization.SystemSetupContext
import de.hybris.platform.core.initialization.SystemSetup.Process
import de.hybris.platform.core.initialization.SystemSetup.Type
import de.hybris.platform.jalo.CoreBasicDataCreator
import de.hybris.platform.oauth2.constants.OAuth2Constants
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup
import de.hybris.platform.servicelayer.user.UserService
import de.hybris.platform.site.BaseSiteService
import de.hybris.platform.util.Config
import de.hybris.platform.webservicescommons.testsupport.server.EmbeddedServerController
import de.hybris.platform.ycommercewebservicestest.constants.YcommercewebservicestestConstants
import de.hybris.platform.ycommercewebservicestest.setup.YCommerceWebServicesTestSetup

import org.slf4j.LoggerFactory

class TestSetupUtils {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestSetupUtils.class)

	public static void loadData(){
		if(shouldLoadData()) {
			loginAdmin()
			setupCore()
			localizeTypes()
			setupCommerce()
		}
		else {
			LOG.info("Data are already loaded");
		}
	}

	private static boolean shouldLoadData() {
		BaseSiteService baseSiteService = Registry.getApplicationContext().getBean("baseSiteService");
		BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("wsTest");
		return baseSite == null;
	}
	private static void loginAdmin(){
		UserService userService = Registry.getApplicationContext().getBean("userService");
		userService.setCurrentUser(userService.getAdminUser());
	}

	private static void setupCore(){
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);

		ServiceLayerDataSetup service = Registry.getApplicationContext().getBean("serviceLayerDataSetup");
		service.setup();

		try{
			new CatalogManager().createEssentialData(Collections.singletonMap("initmethod", "init"), null);
		}catch(Exception e){
			//ignore
		}
	}

	private static void localizeTypes(){
		de.hybris.platform.util.localization.TypeLocalization.getInstance().localizeTypes()
	}

	private static void setupCommerce(){
		YCommerceWebServicesTestSetup setup = Registry.getApplicationContext().getBean("yCommerceWebServicesTestSetup");


		Map<String, String[]> params = ["init":["Go"].toArray(),
			"ycommercewebservicestest_sample":["true"].toArray(),
			"lucenesearch_rebuild.indexes":["true"].toArray(),
			"lucenesearch_update.index.configuration":["true"].toArray()]
		SystemSetupContext context = new SystemSetupContext(params, Type.ALL, Process.ALL, "ycommercewebservicestest")
		setup.createProjectData(context)
	}

	public static void cleanData(){
		LOG.info("Clean data created for test")
		Initialization.initializeTestSystem();
	}

	public static void startServer(){
		String[] ext = [
			YcommercewebservicestestConstants.EXTENSIONNAME - "test",
			OAuth2Constants.EXTENSIONNAME
		]
		startServer(ext);
	}

	public static void startServer(String[] ext){
		if(!Config.getBoolean("ycommercewebservicestest.embedded.server.enabled", true)){
			LOG.info("Ignoring embedded server")
			return
		}

		LOG.info("Starting embedded server")

		EmbeddedServerController controller = Registry.getApplicationContext().getBean("embeddedServerController")
		controller.start(ext);
		LOG.info("embedded server is running")
	}

	public static void stopServer(){
		if(!Config.getBoolean("ycommercewebservicestest.embedded.server.enabled", true)){
			LOG.info("Ignoring embedded server")
			return
		}

		LOG.info("Stopping embedded server")
		EmbeddedServerController controller = Registry.getApplicationContext().getBean("embeddedServerController")
		controller.stop()
		LOG.info("Stopped embedded server")
	}
}
