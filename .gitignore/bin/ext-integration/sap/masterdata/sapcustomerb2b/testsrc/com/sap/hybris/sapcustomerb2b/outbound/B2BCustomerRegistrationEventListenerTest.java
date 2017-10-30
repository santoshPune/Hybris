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
package com.sap.hybris.sapcustomerb2b.outbound;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;




//import de.hybris.platform.b2b.model.B2BCustomerModel;
//import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
//import de.hybris.platform.commerceservices.event.RegisterEvent;
//import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
//import de.hybris.platform.core.Registry;
//import de.hybris.platform.core.model.user.AddressModel;
//import de.hybris.platform.core.model.user.CustomerModel;
//import de.hybris.platform.processengine.BusinessProcessService;
//import de.hybris.platform.sap.core.configuration.global.impl.SAPGlobalConfigurationServiceImpl;
//import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
//import de.hybris.platform.servicelayer.model.ModelService;
//import de.hybris.platform.store.BaseStoreModel;
//import de.hybris.platform.store.services.BaseStoreService;
//import de.hybris.platform.store.services.impl.DefaultBaseStoreService;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import org.junit.Test;
//




import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
public class B2BCustomerRegistrationEventListenerTest {
	
	/*
	public static final String REPLICATEREGISTEREDB2BUSER = "replicateregisteredb2buser";
	
	
	@Test
	public void checkNoReplicationSAPGobalConfigurationExists()
	{


		// given
		final B2BCustomerRegistrationEventListener b2BCustomerRegistrationEventListener = new B2BCustomerRegistrationEventListener();
		final B2BCustomerRegistrationEventListener spyB2BCustomerRegistrationEventListener = spy(b2BCustomerRegistrationEventListener);

		final DefaultBaseStoreService defaultBaseStoreService = mock(DefaultBaseStoreService.class);
		final SAPGlobalConfigurationServiceImpl sapCoreSAPGlobalConfigurationService = mock(SAPGlobalConfigurationServiceImpl.class);
		
		given(sapCoreSAPGlobalConfigurationService.getProperty(REPLICATEREGISTEREDB2BUSER)).willReturn(false);

		final BusinessProcessService businessProcessService = mock(BusinessProcessService.class);
		doReturn(businessProcessService).when(spyB2BCustomerRegistrationEventListener).getBusinessProcessService();

		final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = mock(StoreFrontCustomerProcessModel.class);
		doReturn(storeFrontCustomerProcessModel).when(spyB2BCustomerRegistrationEventListener).createProcess();

		final BaseStoreService baseStoreService = mock(BaseStoreService.class);
		spyB2BCustomerRegistrationEventListener.setBaseStoreService(baseStoreService);

		final BaseStoreModel currentBaseStore = mock(BaseStoreModel.class);
		given(baseStoreService.getCurrentBaseStore()).willReturn(currentBaseStore);

		final ModelService modelService = mock(ModelService.class);
		spyB2BCustomerRegistrationEventListener.setModelService(modelService);

		doNothing().when(modelService).save(storeFrontCustomerProcessModel);
		doNothing().when(businessProcessService).startProcess(storeFrontCustomerProcessModel);

		final RegisterEvent registerEvent = mock(RegisterEvent.class);
		final BaseSiteModel site = new BaseSiteModel();
		given(registerEvent.getSite()).willReturn(site);

		final CustomerModel customerModel = mock(CustomerModel.class);
		given(registerEvent.getCustomer()).willReturn(customerModel);


		//when
		try
		{
			spyB2BCustomerRegistrationEventListener.onEvent(registerEvent);
		}
		catch (final NullPointerException nullPointerException)
		{
			// in case of null pointer exception the "replicateregistereduser" property was in correctly evaluated
			assertNull("BusinessProcess was triggered");
		}

		// then 
		verify(sapCoreSAPGlobalConfigurationService, never()).getProperty(REPLICATEREGISTEREDB2BUSER);
		verify(spyB2BCustomerRegistrationEventListener, never()).createProcess();
		verify(storeFrontCustomerProcessModel, never()).setSite(site);
		verify(storeFrontCustomerProcessModel, never()).setCustomer(customerModel);
		//verify(baseStoreService, never()).getCurrentBaseStore();
		verify(storeFrontCustomerProcessModel, never()).setStore(currentBaseStore);
		verify(modelService, never()).save(storeFrontCustomerProcessModel);
		verify(businessProcessService, never()).startProcess(storeFrontCustomerProcessModel);
	}
	
	
	@Test
	public void checkNoReplicationIfReplicateRegisteredUserIsNotActive()
	{

		// given
				final B2BCustomerRegistrationEventListener b2BCustomerRegistrationEventListener = new B2BCustomerRegistrationEventListener();
				final B2BCustomerRegistrationEventListener spyB2BCustomerRegistrationEventListener = spy(b2BCustomerRegistrationEventListener);

		final SAPGlobalConfigurationServiceImpl sapCoreSAPGlobalConfigurationService = mock(SAPGlobalConfigurationServiceImpl.class);
		spyB2BCustomerRegistrationEventListener.setSapCoreSAPGlobalConfigurationService(sapCoreSAPGlobalConfigurationService);
		given(sapCoreSAPGlobalConfigurationService.sapGlobalConfigurationExists()).willReturn(true);
		
		
		given(sapCoreSAPGlobalConfigurationService.getProperty(REPLICATEREGISTEREDB2BUSER)).willReturn(false);

		final BusinessProcessService businessProcessService = mock(BusinessProcessService.class);
		doReturn(businessProcessService).when(spyB2BCustomerRegistrationEventListener).getBusinessProcessService();

		final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = mock(StoreFrontCustomerProcessModel.class);
		doReturn(storeFrontCustomerProcessModel).when(spyB2BCustomerRegistrationEventListener).createProcess();

		final BaseStoreService baseStoreService = mock(BaseStoreService.class);
		spyB2BCustomerRegistrationEventListener.setBaseStoreService(baseStoreService);

		final BaseStoreModel currentBaseStore = mock(BaseStoreModel.class);
		given(baseStoreService.getCurrentBaseStore()).willReturn(currentBaseStore);

		final ModelService modelService = mock(ModelService.class);
		spyB2BCustomerRegistrationEventListener.setModelService(modelService);

		doNothing().when(modelService).save(storeFrontCustomerProcessModel);
		doNothing().when(businessProcessService).startProcess(storeFrontCustomerProcessModel);

		final RegisterEvent registerEvent = mock(RegisterEvent.class);
		final BaseSiteModel site = new BaseSiteModel();
		given(registerEvent.getSite()).willReturn(site);

		final CustomerModel customerModel = mock(CustomerModel.class);
		given(registerEvent.getCustomer()).willReturn(customerModel);


		//when
		try
		{
			spyB2BCustomerRegistrationEventListener.onEvent(registerEvent);
		}
		catch (final NullPointerException nullPointerException)
		{
			// in case of null pointer exception the "replicateregistereduser" property was in correctly evaluated
			assertNull("BusinessProcess was triggered");
		}

		// then 
		verify(sapCoreSAPGlobalConfigurationService, times(0)).getProperty(REPLICATEREGISTEREDB2BUSER);
		verify(spyB2BCustomerRegistrationEventListener, never()).createProcess();
		verify(storeFrontCustomerProcessModel, never()).setSite(site);
		verify(storeFrontCustomerProcessModel, never()).setCustomer(customerModel);		
		verify(storeFrontCustomerProcessModel, never()).setStore(currentBaseStore);
		verify(modelService, never()).save(storeFrontCustomerProcessModel);
		verify(businessProcessService, never()).startProcess(storeFrontCustomerProcessModel);
	}
	
	@Test
	public void checkReplicationIfReplicateRegisteredUserIsActive()
	{

		// given
		final B2BCustomerRegistrationEventListener b2BCustomerRegistrationEventListener = new B2BCustomerRegistrationEventListener();
		final B2BCustomerRegistrationEventListener spyB2BCustomerRegistrationEventListener = spy(b2BCustomerRegistrationEventListener);

		final SAPGlobalConfigurationServiceImpl sapCoreSAPGlobalConfigurationService = mock(SAPGlobalConfigurationServiceImpl.class);
		spyB2BCustomerRegistrationEventListener.setSapCoreSAPGlobalConfigurationService(sapCoreSAPGlobalConfigurationService);
		given(sapCoreSAPGlobalConfigurationService.sapGlobalConfigurationExists()).willReturn(true);
		
		given(sapCoreSAPGlobalConfigurationService.getProperty(REPLICATEREGISTEREDB2BUSER)).willReturn(true);

		final BusinessProcessService businessProcessService = mock(BusinessProcessService.class);
		doReturn(businessProcessService).when(spyB2BCustomerRegistrationEventListener).getBusinessProcessService();

		final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = mock(StoreFrontCustomerProcessModel.class);
		doReturn(storeFrontCustomerProcessModel).when(spyB2BCustomerRegistrationEventListener).createProcess();

		final BaseStoreService baseStoreService = mock(BaseStoreService.class);
		spyB2BCustomerRegistrationEventListener.setBaseStoreService(baseStoreService);

		final BaseStoreModel currentBaseStore = mock(BaseStoreModel.class);
		given(baseStoreService.getCurrentBaseStore()).willReturn(currentBaseStore);

		final ModelService modelService = mock(ModelService.class);
		spyB2BCustomerRegistrationEventListener.setModelService(modelService);

		doNothing().when(modelService).save(storeFrontCustomerProcessModel);
		doNothing().when(businessProcessService).startProcess(storeFrontCustomerProcessModel);

		final RegisterEvent registerEvent = mock(RegisterEvent.class);
		final BaseSiteModel site = new BaseSiteModel();
		given(registerEvent.getSite()).willReturn(site);

		final CustomerModel customerModel = mock(CustomerModel.class);
		given(registerEvent.getCustomer()).willReturn(customerModel);

		//when
		try
		{
			spyB2BCustomerRegistrationEventListener.onEvent(registerEvent);
		}
		catch (final NullPointerException nullPointerException)
		{
			assertEquals("BusinessProcess was NOT triggered", false, false);
		}

		// then 
		verify(sapCoreSAPGlobalConfigurationService, times(0)).getProperty(REPLICATEREGISTEREDB2BUSER);		
		verify(baseStoreService, times(2)).getCurrentBaseStore();
	}
	*/
}


