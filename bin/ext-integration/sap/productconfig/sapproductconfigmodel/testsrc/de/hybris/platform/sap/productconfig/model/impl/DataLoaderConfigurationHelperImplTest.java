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
package de.hybris.platform.sap.productconfig.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.productconfig.model.cronjob.UnitTestPropertyAccess;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSourceParameters;
import de.hybris.platform.servicelayer.model.ItemModelContext;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderConfiguration;

@SuppressWarnings("javadoc")
@UnitTest
public class DataLoaderConfigurationHelperImplTest
{
	DataLoaderConfigurationHelperImpl classUnderTest = new DataLoaderConfigurationHelperImpl();
	
	@Mock
	private SAPRFCDestinationModel sapDestinationModel;
	
	@Mock
	private MediaModel mediaModel;
	
	@Mock
	private ItemModelContext itemModelContextKBFilterFile;
	
	@Mock
	private SAPConfigurationModel sapConfigurationModel;	
	
	private static String destination = "destination";
	
	private final String serverRfcDestination = "ZZZ000";	
	
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(sapDestinationModel.getRfcDestinationName()).thenReturn(destination);
		Mockito.when(mediaModel.getItemModelContext()).thenReturn(itemModelContextKBFilterFile);
		Mockito.when(itemModelContextKBFilterFile.isUpToDate()).thenReturn(Boolean.FALSE);
		Mockito.when(sapConfigurationModel.getSapproductconfig_filterKnowledgeBase()).thenReturn(mediaModel);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetDataLoaderSourceParamNoDestination()
	{
		classUnderTest.getDataloaderSourceParam(sapConfigurationModel);
	}

	@Test
	public void testGetDataLoaderSourceParam()
	{
		Mockito.when(sapConfigurationModel.getSapproductconfig_sapServer()).thenReturn(sapDestinationModel);
		final DataloaderSourceParameters dataloaderSourceParam = classUnderTest.getDataloaderSourceParam(sapConfigurationModel);
		assertEquals("We expect a destination name", destination, dataloaderSourceParam.getServerRfcDestination());
	}	
	
	@Test
	public void testCreateConfigMapFromConfig()
	{
		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		final Map<String, String> configMap = classUnderTest.createConfigMap(params);
		assertNotNull("We expect a map", configMap);
		final String targetFromProperties = configMap.get(DataloaderConfiguration.TARGET_FROM_PROPERTIES);
		assertEquals("TaargetFromProperties=true expected", Boolean.toString(true), targetFromProperties);
	}

	@Test
	public void testCreateConfigMapFromParams()
	{
		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		params.setServerRfcDestination(serverRfcDestination);
		final Map<String, String> configMap = classUnderTest.createConfigMap(params);
		assertNotNull("We expect a map", configMap);
		final String dest = configMap.get(DataloaderConfiguration.OUTBOUND_DESTINATION_NAME);
		assertEquals("Destination expected", serverRfcDestination, dest);
	}	
	


	@Test
	public void testPrepareFilterFiles()
	{
		final Map<String, String> dataloaderConfigMap = new HashMap<String, String>();
		classUnderTest.prepareFilterFiles(dataloaderConfigMap, sapConfigurationModel);
		final String kbFilterPath = dataloaderConfigMap.get(DataloaderConfiguration.KB_FILTER_FILE_PATH);
		assertNull("We expect no filter path since media model is not upToDate", kbFilterPath);
	}

	@Test
	public void testGetPathForMedia()
	{
		assertNull("No path since model is not upToDate", classUnderTest.getAbsolutFilePathForMedia(mediaModel));
	}	
}
