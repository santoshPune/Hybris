package de.hybris.platform.sap.productconfig.facades.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiTypeFinder;
import de.hybris.platform.sap.productconfig.facades.impl.ClassificationSystemCPQAttributesProviderImpl;
import de.hybris.platform.sap.productconfig.facades.impl.ConfigPricingImpl;
import de.hybris.platform.sap.productconfig.facades.impl.UiTypeFinderImpl;
import de.hybris.platform.sap.productconfig.facades.impl.ValueFormatTranslatorImpl;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit tests
 */
@UnitTest
public class ClassificationSystemCPQAttributesProviderTest
{

	public ConfigurationOverviewValuePopulator classUnderTest;

	@Mock
	public UiTypeFinder uiTypeFinder;
	@Mock
	public ConfigPricingImpl configPricing;
	@Mock
	public PriceModel valuePrice;

	public CsticValueModel source;
	public CharacteristicValue target;
	public PriceData price;
	public CsticModel cstic;
	public Collection<Map> options;

	public final static String CSTIC_NAME = "SOFTWARE_CENTER";
	public final static String CSTIC_LAN_DEPENDENT_NAME = "Software center";
	public final static String VALUE_NAME = "SOFTWARE";
	public final static String VALUE_LAN_DEPENDENT_NAME = "Soffware Engineering";

	public final static String HYBRIS_CSTIC_LAN_DEPENDENT_NAME = "hybris Software center";
	public final static String HYBRIS_VALUE_LAN_DEPENDENT_NAME = "hybris Soffware Engineering";

	public final static String PRICE = "$25.99";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		classUnderTest = new ConfigurationOverviewValuePopulator();
		classUnderTest.setConfigPricing(configPricing);
		final ClassificationSystemCPQAttributesProviderImpl nameProvider = new ClassificationSystemCPQAttributesProviderImpl();
		nameProvider.setUiTypeFinder(new UiTypeFinderImpl());
		nameProvider.setValueFormatTranslator(new ValueFormatTranslatorImpl());
		classUnderTest.setNameProvider(nameProvider);

		target = new CharacteristicValue();
		source = new CsticValueModelImpl();

		source.setLanguageDependentName(VALUE_LAN_DEPENDENT_NAME);
		source.setName(VALUE_NAME);

		source.setValuePrice(valuePrice);

		price = new PriceData();
		price.setFormattedValue(PRICE);

		cstic = new CsticModelImpl();
		cstic.setName(CSTIC_NAME);
		cstic.setLanguageDependentName(CSTIC_LAN_DEPENDENT_NAME);

		options = new ArrayList<Map>();
		final HashMap<String, Object> optionsMap = new HashMap<String, Object>();
		optionsMap.put(ConfigurationOverviewValuePopulator.CSTIC_MODEL, cstic);
		final ClassificationSystemCPQAttributesContainer hybrisNames = ClassificationSystemCPQAttributesContainer.NULL_OBJ;
		optionsMap.put(ConfigurationOverviewValuePopulator.HYBRIS_NAMES, hybrisNames);
		options.add(optionsMap);
	}


	@Test
	public void testLangDependentNameNotNullAndNameNull()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(price);
		source.setName(null);

		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name", VALUE_LAN_DEPENDENT_NAME, target.getValue());
	}

	@Test
	public void testLangDependentNameNullAndNameNull()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(price);
		source.setName(null);
		source.setLanguageDependentName(null);
		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertNull("We expect value equals null", target.getValue());
	}

	@Test
	public void testLangDependentNameNullAndNameNotNull()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(price);
		source.setLanguageDependentName(null);
		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name", VALUE_NAME, target.getValue());
	}

	@Test
	public void testLangDependentNameNotNullAndNameNotNull()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(price);
		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name: ", VALUE_LAN_DEPENDENT_NAME, target.getValue());
	}

	@Test
	public void testNoConfigPrice()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(ConfigPricing.NO_PRICE);

		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name", VALUE_LAN_DEPENDENT_NAME, target.getValue());
		assertNull(target.getPriceDescription());
	}

	@Test
	public void testValuePrice()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(price);

		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name", VALUE_LAN_DEPENDENT_NAME, target.getValue());
		assertEquals("Wrong price", PRICE, target.getPriceDescription());
	}

	@Test
	public void testHybrisNames()
	{
		Mockito.when(configPricing.getPriceData(valuePrice)).thenReturn(ConfigPricing.NO_PRICE);

		final HashMap optionsMap = (HashMap) options.iterator().next();
		final Map<String, String> hybris_value_names = new HashMap<String, String>();
		hybris_value_names.put(CSTIC_NAME + "_" + VALUE_NAME, HYBRIS_VALUE_LAN_DEPENDENT_NAME);

		final ClassificationSystemCPQAttributesContainer hybrisNames = new ClassificationSystemCPQAttributesContainer(CSTIC_NAME,
				HYBRIS_CSTIC_LAN_DEPENDENT_NAME, null, hybris_value_names, Collections.emptyMap(), Collections.emptyMap());
		optionsMap.put(ConfigurationOverviewValuePopulator.HYBRIS_NAMES, hybrisNames);

		classUnderTest.populate(source, target, options);

		assertNotNull(target);
		assertEquals("Wrong characteristic name", HYBRIS_CSTIC_LAN_DEPENDENT_NAME, target.getCharacteristic());
		assertEquals("Wrong value name: ", HYBRIS_VALUE_LAN_DEPENDENT_NAME, target.getValue());
	}

	@Test
	public void testOverviewValueNameSupportedType()
	{
		final ClassificationSystemCPQAttributesProviderImpl classSystemProvider = new ClassificationSystemCPQAttributesProviderImpl();
		classSystemProvider.setUiTypeFinder(uiTypeFinder);
		final ClassificationSystemCPQAttributesContainer hybrisNames = ClassificationSystemCPQAttributesContainer.NULL_OBJ;

		Mockito.when(uiTypeFinder.findUiTypeForCstic(cstic)).thenReturn(UiType.STRING);

		final String displayName = classSystemProvider.getOverviewValueName(source, cstic, hybrisNames, false);

		assertNotNull(displayName);
		assertEquals(VALUE_LAN_DEPENDENT_NAME, displayName);
	}

	@Test
	public void testOverviewValueNameNotYetSupportedType()
	{
		final ClassificationSystemCPQAttributesProviderImpl classSystemProvider = new ClassificationSystemCPQAttributesProviderImpl();
		classSystemProvider.setUiTypeFinder(uiTypeFinder);
		final ClassificationSystemCPQAttributesContainer hybrisNames = ClassificationSystemCPQAttributesContainer.NULL_OBJ;

		Mockito.when(uiTypeFinder.findUiTypeForCstic(cstic)).thenReturn(UiType.NOT_IMPLEMENTED);

		final String displayName = classSystemProvider.getOverviewValueName(source, cstic, hybrisNames, false);

		assertNotNull(displayName);
		assertEquals("NOT_IMPLEMENTED", displayName);
	}
}
