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
 */package de.hybris.platform.sap.productconfig.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.productconfig.model.model.CPQDataloadStatusModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.verification.VerificationMode;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderFailureException;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderManager;

@SuppressWarnings("javadoc")
@UnitTest
public class DataLoaderImplTest
{
	DataLoaderImpl classUnderTest = new DataLoaderImpl();
	@Mock
	private DataloaderManager dataloaderManager;
	
	@Mock
	private SAPConfigurationModel sapConfiguration;
	
	@Mock
	private ModelService modelService;
	
	
	private CPQDataloadStatusModel dataLoadStatus = new CPQDataloadStatusModel();
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(sapConfiguration.getSapproductconfig_cpqDataloadStatus()).thenReturn(dataLoadStatus);		
	}
	
	@Test
	public void testInitialLoad() throws DataloaderFailureException{		
		classUnderTest.performInitialLoadRaisingException(dataloaderManager, sapConfiguration, modelService);
		Mockito.verify(modelService, VerificationModeFactory.atLeastOnce()).save(dataLoadStatus);		 
	}
	
	@Test
	public void testDeltaLoad() throws DataloaderFailureException{		
		classUnderTest.performDeltaLoadRaisingException(dataloaderManager, sapConfiguration, modelService);
		Mockito.verify(modelService, VerificationModeFactory.atLeastOnce()).save(dataLoadStatus);		 
	}	
	
	@Test
	public void testResetStatistics(){
		dataLoadStatus.setCpqCurrentDeltaLoadTransferredVolume(BigDecimal.ONE);
		dataLoadStatus.setCpqCurrentInitialLoadTransferredVolume(BigDecimal.ONE);
		dataLoadStatus.setCpqNumberOfEntriesInDeltaLoadQueue(Integer.MAX_VALUE);
		classUnderTest.resetStatistics(dataLoadStatus);
		assertNull(dataLoadStatus.getCpqCurrentDeltaLoadTransferredVolume());
		assertNull(dataLoadStatus.getCpqCurrentInitialLoadTransferredVolume());
		assertNull(dataLoadStatus.getCpqNumberOfEntriesInDeltaLoadQueue());
	}
}
