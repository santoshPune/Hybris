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

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSourceParameters;
import de.hybris.platform.sap.productconfig.model.intf.DataLoaderConfigurationHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderConfiguration;


/**
 * Default implementation of {@link DataLoaderConfigurationHelper}
 */
public class DataLoaderConfigurationHelperImpl implements DataLoaderConfigurationHelper
{
	private static final Logger LOG = Logger.getLogger(DataLoaderConfigurationHelperImpl.class);


	@Override
	public DataloaderSourceParameters getDataloaderSourceParam(final SAPConfigurationModel configuration)
	{

		final SAPRFCDestinationModel sapServer = configuration.getSapproductconfig_sapServer();
		final String rfcDestination = configuration.getSapproductconfig_sapRFCDestination();

		if (sapServer == null)
		{
			throw new IllegalArgumentException("An RFC destination is needed to connect to the backend system");
		}

		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		params.setClient(sapServer.getClient());
		params.setClientRfcDestination(rfcDestination);
		params.setServerRfcDestination(sapServer.getRfcDestinationName());
		return params;
	}

	@Override
	public Map<String, String> createConfigMap(final DataloaderSourceParameters params)
	{
		final Map<String, String> dataloaderConfigMap = new HashMap<>();

		final String eccClient = params.getClient();
		final String outboundDestination = params.getServerRfcDestination();
		final String eccDestination = params.getClientRfcDestination();

		final String targetFromProperties = Boolean.toString(true);

		if (LOG.isDebugEnabled())
		{
			final StringBuilder debugOutput = new StringBuilder("\n Dataloader configuration attributes:");
			debugOutput.append("\n ECC client               : ").append(eccClient);
			debugOutput.append("\n RFC outbound dest        : ").append(outboundDestination);
			debugOutput.append("\n RFC ECC dest             : ").append(eccDestination);
			debugOutput.append("\n Target DB from properties: ").append(targetFromProperties);

			LOG.debug(debugOutput.toString());
		}
		dataloaderConfigMap.put(DataloaderConfiguration.ECC_CLIENT, eccClient);
		dataloaderConfigMap.put(DataloaderConfiguration.OUTBOUND_DESTINATION_NAME, outboundDestination);
		dataloaderConfigMap.put(DataloaderConfiguration.ECC_RFC_DESTINATION, eccDestination);

		// SSC DB
		dataloaderConfigMap.put(DataloaderConfiguration.TARGET_FROM_PROPERTIES, targetFromProperties);

		return dataloaderConfigMap;
	}






	@Override
	public void prepareFilterFiles(final Map<String, String> dataloaderConfigMap, final SAPConfigurationModel sapConfiguration)
	{
		MediaModel filterFile = sapConfiguration.getSapproductconfig_filterKnowledgeBase();

		final String kbFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = sapConfiguration.getSapproductconfig_filterMaterial();

		final String materialsFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = sapConfiguration.getSapproductconfig_filterCondition();

		final String conditionsFilterFile = getAbsolutFilePathForMedia(filterFile);


		dataloaderConfigMap.put(DataloaderConfiguration.KB_FILTER_FILE_PATH, kbFilterFile);
		dataloaderConfigMap.put(DataloaderConfiguration.MATERIALS_FILTER_FILE_PATH, materialsFilterFile);
		dataloaderConfigMap.put(DataloaderConfiguration.CONDITIONS_FILTER_FILE_PATH, conditionsFilterFile);

		return;
	}

	@Override
	public String getAbsolutFilePathForMedia(final MediaModel filterFile)
	{

		String filterFileAbsolutPath = null;

		if (filterFile != null)
		{

			final boolean isAlive = !filterFile.getItemModelContext().isRemoved() && filterFile.getItemModelContext().isUpToDate();

			if (filterFile.getSize() != 0 && isAlive)
			{
				final File file = MediaManager.getInstance().getMediaAsFile(filterFile.getFolder().getQualifier(),
						filterFile.getLocation());

				filterFileAbsolutPath = file.getAbsolutePath();

			}
		}

		return filterFileAbsolutPath;
	}

}
