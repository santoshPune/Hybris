/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.strategies.impl.ProductConfigCommerceCartMergingStrategyImpl;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



@UnitTest
public class ProductConfigCommerceCartMergingStrategyImplTest
{
	private static final String SAP_CONFGURABLE = "SAP_CONFIGURABLE";
	private ProductConfigCommerceCartMergingStrategyImpl classUnderTest;


	@Mock
	private ModelService mockedModelService;

	@Before
	public void setUp()
	{
		classUnderTest = new ProductConfigCommerceCartMergingStrategyImpl();
		MockitoAnnotations.initMocks(this);
		classUnderTest.setModelService(mockedModelService);
		classUnderTest.setConfigurableSource(SAP_CONFGURABLE);

	}

	@Test
	public void test_collectConfigsBeforeMerge_emptyCart()
	{
		final CartModel mockedCart = mockCartWithNumberEntries(Collections.EMPTY_LIST);
		final Map<String, List<String>> collectedConfigs = classUnderTest.collectConfigsBeforeMerge(mockedCart);
		assertNotNull(collectedConfigs);
	}

	@Test
	public void test_collectConfigsBeforeMerge_configurable()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1</xml>"));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<String>> collectedConfigs = classUnderTest.collectConfigsBeforeMerge(mockedCart);

		assertEquals(1, collectedConfigs.size());
		assertEquals("<xml>confProduct_1</xml>", collectedConfigs.get("confProduct_1").get(0));
	}

	@Test
	public void test_collectConfigsBeforeMerge_nonConfigurable()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<String>> collectedConfigs = classUnderTest.collectConfigsBeforeMerge(mockedCart);

		assertEquals(0, collectedConfigs.size());
	}

	@Test
	public void test_collectConfigsBeforeMerge_twoConfigurableWithSameproductCode()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1_1</xml>"));
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1_2</xml>"));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<String>> collectedConfigs = classUnderTest.collectConfigsBeforeMerge(mockedCart);

		assertEquals(1, collectedConfigs.size());
		assertEquals(2, collectedConfigs.get("confProduct_1").size());
		assertEquals("<xml>confProduct_1_1</xml>", collectedConfigs.get("confProduct_1").get(0));
		assertEquals("<xml>confProduct_1_2</xml>", collectedConfigs.get("confProduct_1").get(1));
	}

	@Test
	public void test_collectConfigsBeforeMerge_multiEntries()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1_1</xml>"));
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1_2</xml>"));
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		dataToMock.add(createMockData("confProduct_1", true, "<xml>confProduct_1_3</xml>"));
		dataToMock.add(createMockData("nonConfProduct_2", false, null));
		dataToMock.add(createMockData("confProduct_2", true, "<xml>confProduct_2</xml>"));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<String>> collectedConfigs = classUnderTest.collectConfigsBeforeMerge(mockedCart);

		assertEquals(2, collectedConfigs.size());
		assertEquals(3, collectedConfigs.get("confProduct_1").size());
		assertEquals(1, collectedConfigs.get("confProduct_2").size());
		assertEquals("<xml>confProduct_1_1</xml>", collectedConfigs.get("confProduct_1").get(0));
		assertEquals("<xml>confProduct_1_2</xml>", collectedConfigs.get("confProduct_1").get(1));
		assertEquals("<xml>confProduct_1_3</xml>", collectedConfigs.get("confProduct_1").get(2));
		assertEquals("<xml>confProduct_2</xml>", collectedConfigs.get("confProduct_2").get(0));

	}


	@Test
	public void test_findEntiesWithMissingConfig_emptyCart()
	{
		final CartModel mockedCart = mockCartWithNumberEntries(Collections.EMPTY_LIST);
		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = classUnderTest.findEntiesWithMissingConfig(mockedCart);
		assertNotNull(missingConfigs);
	}

	@Test
	public void test_findEntiesWithMissingConfig_oneConfigMissing()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("confProduct_1", true, null));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = classUnderTest.findEntiesWithMissingConfig(mockedCart);
		assertEquals(1, missingConfigs.size());
		assertNotNull(missingConfigs.get("confProduct_1").get(0));
	}

	@Test
	public void test_findEntiesWithMissingConfig_nonConfig()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = classUnderTest.findEntiesWithMissingConfig(mockedCart);
		assertEquals(0, missingConfigs.size());
	}

	@Test
	public void test_findEntiesWithMissingConfig_configNotMissing()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("nonConfProduct_1", true, "<xml>confProduct_1</xml>"));
		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = classUnderTest.findEntiesWithMissingConfig(mockedCart);
		assertEquals(0, missingConfigs.size());
	}

	@Test
	public void test_findEntiesWithMissingConfig_multiConfigMissing()
	{
		final List<MockData> dataToMock = new ArrayList();
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		dataToMock.add(createMockData("confProduct_1", true, null));
		dataToMock.add(createMockData("confProduct_1", true, ""));
		dataToMock.add(createMockData("nonConfProduct_1", false, null));
		dataToMock.add(createMockData("confProduct_1", true, null));
		dataToMock.add(createMockData("nonConfProduct_2", false, null));
		dataToMock.add(createMockData("confProduct_2", true, "<xml>confProduct_2</xml>"));
		dataToMock.add(createMockData("confProduct_3", true, ""));

		final CartModel mockedCart = mockCartWithNumberEntries(dataToMock);

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = classUnderTest.findEntiesWithMissingConfig(mockedCart);
		assertEquals(2, missingConfigs.size());
		assertEquals(3, missingConfigs.get("confProduct_1").size());
		assertNotNull(missingConfigs.get("confProduct_1").get(0));
		assertNotNull(missingConfigs.get("confProduct_1").get(1));
		assertNotNull(missingConfigs.get("confProduct_1").get(2));
		assertEquals(1, missingConfigs.get("confProduct_3").size());
		assertNotNull(missingConfigs.get("confProduct_3").get(0));
	}


	@Test
	public void test_reApplyConfigsAfterMerge_noConfigurable() throws CommerceCartMergingException
	{
		final Map<String, List<String>> collectedConfigs = Collections.EMPTY_MAP;
		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = Collections.EMPTY_MAP;

		classUnderTest.reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);

	}

	@Test
	public void test_reApplyConfigsAfterMerge_oneConfig() throws CommerceCartMergingException
	{

		final Map<String, List<String>> collectedConfigs = new HashMap();
		collectedConfigs.put("confProduct_1", Collections.singletonList("<xml>confProduct_1</xml>"));


		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = new HashMap();
		final AbstractOrderEntryModel cartEntry1 = new DummyCartEntry();
		missingConfigs.put("confProduct_1", Collections.singletonList(cartEntry1));


		classUnderTest.reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);
		assertEquals("<xml>confProduct_1</xml>", cartEntry1.getExternalConfiguration());
	}

	@Test(expected = CommerceCartMergingException.class)
	public void test_reApplyConfigsAfterMerge_exceptionMapsNotSameSize() throws CommerceCartMergingException
	{

		final Map<String, List<String>> collectedConfigs = new HashMap();
		collectedConfigs.put("confProduct_1", Collections.singletonList("<xml>confProduct_1</xml>"));

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = Collections.EMPTY_MAP;


		classUnderTest.reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);

	}

	@Test(expected = CommerceCartMergingException.class)
	public void test_reApplyConfigsAfterMerge_exceptionListNotSameSize() throws CommerceCartMergingException
	{

		final Map<String, List<String>> collectedConfigs = new HashMap<>();
		final List<String> configList = new ArrayList();
		configList.add("<xml>confProduct_1_1</xml>");
		configList.add("<xml>confProduct_1_2</xml>");
		collectedConfigs.put("confProduct_1", configList);

		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = new HashMap();
		final AbstractOrderEntryModel cartEntry1 = new DummyCartEntry();
		missingConfigs.put("confProduct_1", Collections.singletonList(cartEntry1));

		classUnderTest.reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);

	}


	@Test
	public void test_reApplyConfigsAfterMerge_multiConfig() throws CommerceCartMergingException
	{

		final Map<String, List<String>> collectedConfigs = new HashMap();
		final List<String> configList = new ArrayList();
		configList.add("<xml>confProduct_1_1</xml>");
		configList.add("<xml>confProduct_1_2</xml>");
		configList.add("<xml>confProduct_1_3</xml>");
		collectedConfigs.put("confProduct_1", configList);
		collectedConfigs.put("confProduct_2", Collections.singletonList("<xml>confProduct_2</xml>"));


		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = new HashMap();
		final AbstractOrderEntryModel cartEntry2 = new DummyCartEntry();
		missingConfigs.put("confProduct_2", Collections.singletonList(cartEntry2));
		final List<AbstractOrderEntryModel> entryList = new ArrayList<>();
		final AbstractOrderEntryModel cartEntry11 = new DummyCartEntry();
		entryList.add(cartEntry11);
		final AbstractOrderEntryModel cartEntry12 = new DummyCartEntry();
		entryList.add(cartEntry12);
		final AbstractOrderEntryModel cartEntry13 = new DummyCartEntry();
		entryList.add(cartEntry13);
		missingConfigs.put("confProduct_1", entryList);


		classUnderTest.reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);
		assertEquals("<xml>confProduct_2</xml>", cartEntry2.getExternalConfiguration());
		assertEquals("<xml>confProduct_1_1</xml>", cartEntry11.getExternalConfiguration());
		assertEquals("<xml>confProduct_1_2</xml>", cartEntry12.getExternalConfiguration());
		assertEquals("<xml>confProduct_1_3</xml>", cartEntry13.getExternalConfiguration());
	}

	private MockData createMockData(final String productCode, final boolean isConfigurable, final String externalConfig)
	{
		final MockData mockData = new MockData(productCode, isConfigurable, externalConfig);
		return mockData;
	}

	private CartModel mockCartWithNumberEntries(final List<MockData> mockDataList)
	{

		final CartModel mockedCart = Mockito.mock(CartModel.class);
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		for (final MockData mockData : mockDataList)
		{
			final AbstractOrderEntryModel entryDummy = new DummyCartEntry();
			entries.add(entryDummy);
			final ProductModel productMock = Mockito.mock(ProductModel.class);
			entryDummy.setProduct(productMock);

			entryDummy.setExternalConfiguration(mockData.getExternalConfig());
			given(productMock.getCode()).willReturn(mockData.getProductCode());
			given(mockedModelService.getAttributeValue(productMock, SAP_CONFGURABLE))
					.willReturn(Boolean.valueOf(mockData.isConfigurable()));

		}
		given(mockedCart.getEntries()).willReturn(entries);
		return mockedCart;

	}

	private static class MockData
	{
		private final String productCode;
		private final boolean configurable;
		private final String externalConfig;

		public MockData(final String productCode, final boolean configurable, final String externalConfig)
		{
			super();
			this.productCode = productCode;
			this.configurable = configurable;
			this.externalConfig = externalConfig;
		}

		public String getProductCode()
		{
			return productCode;
		}

		public boolean isConfigurable()
		{
			return configurable;
		}

		public String getExternalConfig()
		{
			return externalConfig;
		}
	}

	private static class DummyCartEntry extends AbstractOrderEntryModel
	{

		private String externalConfig;
		private ProductModel product;

		@Override
		public ProductModel getProduct()
		{
			return product;
		}

		@Override
		public void setProduct(final ProductModel product)
		{
			this.product = product;
		}

		@Override
		public String getExternalConfiguration()
		{
			return externalConfig;
		}

		@Override
		public void setExternalConfiguration(final String value)
		{
			externalConfig = value;
		}
	}
}
