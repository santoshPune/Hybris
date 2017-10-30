package de.hybris.platform.sap.productconfig.facades.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.sap.productconfig.facades.ConfigOverviewFilter;
import de.hybris.platform.sap.productconfig.facades.ConfigOverviewGroupFilter;
import de.hybris.platform.sap.productconfig.facades.ConfigurationTestData;
import de.hybris.platform.sap.productconfig.facades.filters.DefaultOverviewGroupFilter;
import de.hybris.platform.sap.productconfig.facades.filters.OverviewFilterList;
import de.hybris.platform.sap.productconfig.facades.filters.VisibleValueFilter;
import de.hybris.platform.sap.productconfig.facades.impl.ConfigPricingImpl;
import de.hybris.platform.sap.productconfig.facades.impl.ClassificationSystemCPQAttributesProviderImpl;
import de.hybris.platform.sap.productconfig.facades.impl.UiTypeFinderImpl;
import de.hybris.platform.sap.productconfig.facades.impl.ValueFormatTranslatorImpl;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;
import de.hybris.platform.sap.productconfig.services.impl.SessionAccessServiceImpl;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit tests
 */
@UnitTest
public class ConfigurationOverviewPopulatorTest
{
	public ConfigurationOverviewPopulator classUnderTest;
	public ConfigurationOverviewInstancePopulator configurationOverviewInstancePopulator;
	public ConfigOverviewGroupFilter overviewGroupFilter;
	public ConfigurationOverviewValuePopulator configurationOverviewValuePopulator;
	private OverviewFilterList overviewFilterList;
	private ConfigOverviewFilter visibleValueFilter;
	public ConfigPricingImpl configPricing;

	public ConfigModel source;
	public ConfigurationOverviewData target;

	@Mock
	public ProductDao productDao;

	@Mock
	public SessionService sessionService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		classUnderTest = new ConfigurationOverviewPopulator();
		configurationOverviewInstancePopulator = new ConfigurationOverviewInstancePopulator();
		overviewGroupFilter = new DefaultOverviewGroupFilter();
		configurationOverviewInstancePopulator.setOverviewGroupFilter(overviewGroupFilter);
		visibleValueFilter = new VisibleValueFilter();
		configurationOverviewInstancePopulator.setVisibleValueFilter(visibleValueFilter);
		overviewFilterList = new OverviewFilterList();
		final List<ConfigOverviewFilter> filters = new ArrayList<>();
		overviewFilterList.setFilters(filters);
		classUnderTest.setOverviewFilterList(overviewFilterList);
		classUnderTest.setConfigurationOverviewInstancePopulator(configurationOverviewInstancePopulator);
		configurationOverviewValuePopulator = new ConfigurationOverviewValuePopulator();
		configPricing = new ConfigPricingImpl();
		configurationOverviewValuePopulator.setConfigPricing(configPricing);
		configurationOverviewInstancePopulator.setConfigurationOverviewValuePopulator(configurationOverviewValuePopulator);

		target = new ConfigurationOverviewData();
		source = ConfigurationTestData.createConfigModelWithGroupsAllVisible();

		final ClassificationSystemCPQAttributesProviderImpl nameProvider = Mockito.spy(new ClassificationSystemCPQAttributesProviderImpl());
		classUnderTest.setNameProvider(nameProvider);
		nameProvider.setUiTypeFinder(new UiTypeFinderImpl());
		nameProvider.setValueFormatTranslator(new ValueFormatTranslatorImpl());
		configurationOverviewInstancePopulator.setNameProvider(nameProvider);
		configurationOverviewValuePopulator.setNameProvider(nameProvider);
		configurationOverviewInstancePopulator.setProductDao(productDao);

		final SessionAccessServiceImpl sessionAccessService = new SessionAccessServiceImpl();
		sessionAccessService.setSessionService(sessionService);
		given(sessionService.getAttribute(ClassificationSystemCPQAttributesContainer.class.getName())).willReturn(null);
		classUnderTest.setSessionAccessService(sessionAccessService);

		Mockito.doReturn(ClassificationSystemCPQAttributesContainer.NULL_OBJ).when(nameProvider).getCPQAttributes(Mockito.anyString(),
				Mockito.anyMap());

		Mockito.when(productDao.findProductsByCode(Mockito.anyString())).thenReturn(null);
	}

	@Test
	public void testConfigurationOverviewPopulator()
	{
		classUnderTest.populate(source, target);
		assertNotNull(target);
		assertEquals("We expect target Id: ", "1", target.getId());

		final List<CharacteristicValue> firstCsticValues = target.getGroups().get(0).getCharacteristicValues();
		assertEquals("We expect cstic description: ", ConfigurationTestData.CHBOX_LD_NAME,
				firstCsticValues.get(0).getCharacteristic());
		assertEquals("We expect value: ", "X", firstCsticValues.get(0).getValue());
		assertNull(firstCsticValues.get(0).getPriceDescription());

		final List<CharacteristicValue> secondCsticValues = target.getGroups().get(1).getCharacteristicValues();
		assertEquals("We expect cstic description: ", ConfigurationTestData.CHBOX_LIST_LD_NAME,
				secondCsticValues.get(0).getCharacteristic());
		assertEquals("We expect value: ", "VALUE 2", secondCsticValues.get(0).getValue());
		assertNull(secondCsticValues.get(0).getPriceDescription());

	}

	@Test
	public void testCopyCsticsToNextLevelGroupIfOnlyOneCsticGroupExists()
	{

		final ConfigModel config = ConfigurationTestData.createConfigModelWithGroupsAndSubInstancesAllVisible();
		final InstanceModel sourceInstance = config.getRootInstance().getSubInstances().get(0);
		// Delete one cstic group that only one remains
		sourceInstance.getCsticGroups().remove(1);

		classUnderTest.populate(config, target);
		assertNotNull(target);

		assertEquals("ConfigurationOverviewData should have 3 groups (2 cstic groups, 1 subinstance)", 3,
				target.getGroups().size());
		assertNotNull("Sub-Instance of root should have cstics AND sub-instance",
				target.getGroups().get(2).getCharacteristicValues());
		assertNotNull("Sub-Instance of root should have cstics AND sub-instance", target.getGroups().get(2).getSubGroups());
	}



}
