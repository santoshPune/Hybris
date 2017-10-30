package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.populator.ConfigurationOverviewPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ConfigurationOverviewFacadeImplTest
{
	public ConfigurationOverviewFacadeImpl classUnderTest;

	private static final String CONFIG_ID = "config_id";

	@Mock
	private ConfigurationOverviewPopulator configurationOverviewPopulator;

	@Mock
	private ProductConfigurationService productConfigurationService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest = new ConfigurationOverviewFacadeImpl();
		classUnderTest.setConfigurationOverviewPopulator(configurationOverviewPopulator);
		classUnderTest.setConfigurationService(productConfigurationService);
		Mockito.when(productConfigurationService.retrieveConfigurationModel(CONFIG_ID)).thenReturn(new ConfigModelImpl());
	}

	@Test
	public void testGetOverviewForConfigurationNull()
	{
		ConfigurationOverviewData configOverviewData = null;
		configOverviewData = classUnderTest.getOverviewForConfiguration(CONFIG_ID, configOverviewData);
		assertNotNull(configOverviewData);
	}
}
