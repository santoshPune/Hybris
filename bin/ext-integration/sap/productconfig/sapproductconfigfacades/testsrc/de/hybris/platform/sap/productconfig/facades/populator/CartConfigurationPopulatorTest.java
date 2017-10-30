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
package de.hybris.platform.sap.productconfig.facades.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.data.CartEntryConfigurationAttributes;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CartConfigurationPopulatorTest
{

	private static final String SAP_CONFIGURABLE = "sapConfigurable";
	private CartConfigurationPopulator classUnderTest;
	private CartModel source;
	private CartData target;
	private List<OrderEntryData> targetEntryList;
	private OrderEntryData targetEntry;
	private final Integer entryNo = Integer.valueOf(1);
	private ConfigModel configModel;
	private ProductModel productModel;
	private InstanceModel rootInstance;

	@Mock
	private AbstractOrderEntryModel sourceEntry;
	@Mock
	private SessionAccessService sessionAccessService;
	@Mock
	private ProductConfigurationService productConfigurationService;
	@Mock
	private ModelService modelService;
	private final CartEntryConfigurationAttributes cartEntryAttributes = new CartEntryConfigurationAttributes();
	private static final int numberOfErrors = 4;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new CartModel();


		final List<AbstractOrderEntryModel> entryList = new ArrayList();
		entryList.add(sourceEntry);

		target = new CartData();
		targetEntryList = new ArrayList();
		target.setEntries(targetEntryList);
		targetEntry = new OrderEntryData();
		targetEntry.setEntryNumber(entryNo);
		targetEntry.setItemPK("123");

		source.setEntries(entryList);

		configModel = new ConfigModelImpl();
		productModel = new ProductModel();
		productModel.setCode("Product");


		classUnderTest = new CartConfigurationPopulator();
		classUnderTest.setSessionAccessService(sessionAccessService);
		classUnderTest.setProductConfigurationService(productConfigurationService);

		rootInstance = new InstanceModelImpl();
		configModel.setRootInstance(rootInstance);

		final CsticModel cstic01 = createCompleAndConsistentCstic("cstic01");
		final CsticModel cstic02 = createCompleAndConsistentCstic("cstic02");
		rootInstance.addCstic(cstic01);
		rootInstance.addCstic(cstic02);

		final InstanceModel subInstance = new InstanceModelImpl();
		rootInstance.setSubInstances(Collections.singletonList(subInstance));
		final CsticModel cstic11 = createCompleAndConsistentCstic("cstic11");
		final CsticModel cstic12 = createCompleAndConsistentCstic("cstic12");
		subInstance.addCstic(cstic11);
		subInstance.addCstic(cstic12);


		classUnderTest.setModelService(modelService);
		classUnderTest.setConfigurableSource(SAP_CONFIGURABLE);

		//productConfigurationService	.calculateCartEntryConfigurationAttributes(entry);

		Mockito.when(productConfigurationService.calculateCartEntryConfigurationAttributes(Mockito.any())).thenReturn(
				cartEntryAttributes);
		cartEntryAttributes.setConfigurationConsistent(Boolean.TRUE);
		cartEntryAttributes.setNumberOfErrors(Integer.valueOf(numberOfErrors));

	}


	protected CsticModelImpl createCompleAndConsistentCstic(final String name)
	{
		final CsticModelImpl cstic = new CsticModelImpl();
		cstic.setName(name);
		cstic.setComplete(true);
		cstic.setConsistent(true);
		cstic.setRequired(true);
		return cstic;
	}

	@Test(expected = RuntimeException.class)
	public void testPopulateNoTargetItems()
	{
		Mockito.when(sourceEntry.getProduct()).thenReturn(productModel);
		Mockito.when(modelService.getAttributeValue(productModel, SAP_CONFIGURABLE)).thenReturn(Boolean.TRUE);
		//entry numbers do not match->exception
		classUnderTest.populate(source, target);
	}

	@Test
	public void testPopulate()
	{
		final String configId = "123";
		final PK value = initializeSourceItem("Ext", configId);


		targetEntryList.add(targetEntry);

		classUnderTest.populate(source, target);
		assertTrue("ItemPK not set", targetEntry.getItemPK().equals(value.toString()));
		assertTrue("Configuration not marked as attached", targetEntry.isConfigurationAttached());
		assertTrue("Configuration should be consistent", targetEntry.isConfigurationConsistent());
	}


	@Test
	public void testPopulate_notConfigurable()
	{
		initializeSourceItem("Ext", null);

		configModel.setConsistent(false);
		configModel.setComplete(false);
		targetEntryList.add(targetEntry);

		classUnderTest.populate(source, target);
		assertFalse("Configuration marked as attached", targetEntry.isConfigurationAttached());
		assertFalse("Configuration should be inconsistent", targetEntry.isConfigurationConsistent());
		assertEquals("ErrorCount should be zero for non configurable products", 0, targetEntry.getConfigurationErrorCount());
	}



	@Test
	public void testPopulate_numberErrors_conflicts()
	{
		final String configId = "123";
		initializeSourceItem("Ext", configId);

		targetEntryList.add(targetEntry);

		classUnderTest.populate(source, target);
		assertEquals(numberOfErrors, targetEntry.getConfigurationErrorCount());
	}

	@Test
	public void testPopulateConfigCompleteAndConsistent()
	{
		final String configId = "123";

		configModel.setConsistent(true);
		configModel.setComplete(true);
		initializeSourceItem("Ext", configId);

		targetEntryList.add(targetEntry);

		classUnderTest.populate(source, target);

		assertTrue("Configuration should be consistent", targetEntry.isConfigurationConsistent());

	}



	@Test
	public void testPopulateConfigNoConfigModelInSession()
	{
		final String configId = "123";

		initializeSourceItem("Ext", configId);

		configModel.setConsistent(true);
		configModel.setComplete(true);

		targetEntryList.add(targetEntry);
		Mockito.when(productConfigurationService.retrieveConfigurationModel(configId)).thenReturn(null);
		Mockito.when(
				productConfigurationService.createConfigurationFromExternal((KBKey) Mockito.anyObject(), Mockito.matches("Ext")))
				.thenReturn(configModel);
		classUnderTest.populate(source, target);

		assertTrue("Configuration should be consistent", targetEntry.isConfigurationConsistent());

	}

	@Test
	public void testPopulateNoConfigurationAttached()
	{
		final String configId = "";
		initializeSourceItem("", configId);
		targetEntryList.add(targetEntry);
		classUnderTest.populate(source, target);
		assertTrue("Configuration must be marked as attached, as a default config is created",
				targetEntry.isConfigurationAttached());
	}

	private PK initializeSourceItem(final String extConfig, final String configId)
	{
		final PK value = PK.fromLong(123);

		Mockito.when(sourceEntry.getPk()).thenReturn(value);
		Mockito.when(sourceEntry.getExternalConfiguration()).thenReturn(extConfig);
		Mockito.when(sourceEntry.getProduct()).thenReturn(productModel);
		Mockito.when(sourceEntry.getEntryNumber()).thenReturn(entryNo);
		Mockito.when(sessionAccessService.getConfigIdForCartEntry(targetEntry.getItemPK())).thenReturn(configId);
		if (configId == null)
		{
			productModel.setSapConfigurable(Boolean.FALSE);
			Mockito.when(modelService.getAttributeValue(productModel, SAP_CONFIGURABLE)).thenReturn(Boolean.FALSE);
			Mockito.when(productConfigurationService.retrieveConfigurationModel(null)).thenThrow(new NullPointerException());
		}
		else
		{
			productModel.setSapConfigurable(Boolean.TRUE);
			Mockito.when(modelService.getAttributeValue(productModel, SAP_CONFIGURABLE)).thenReturn(Boolean.TRUE);
			Mockito.when(productConfigurationService.retrieveConfigurationModel(configId)).thenReturn(configModel);
		}
		return value;
	}
}
