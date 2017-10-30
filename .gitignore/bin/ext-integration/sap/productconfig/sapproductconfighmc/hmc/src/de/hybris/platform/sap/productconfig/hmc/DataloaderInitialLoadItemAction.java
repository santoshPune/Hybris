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
package de.hybris.platform.sap.productconfig.hmc;

import de.hybris.platform.hmc.HMCHelper;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.util.action.ItemAction;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPRFCDestination;
import de.hybris.platform.sap.productconfig.hmc.constants.SapproductconfighmcConstants;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderConfiguration;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSource;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSourceParameters;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.InitialDownloadConfiguration;

import org.apache.log4j.Logger;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSource;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderFacadeImpl;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.Status;


/**
 * @deprecated(This action is deprecated, on the HMC UI it has been removed. In case you want to provide data loader
 *                  functions to HMC, use a new SSC UI. See
 *                  de.hybris.platform.sap.sapproductconfigbackoffice.actions.DataloaderInitialLoadItemAction as a
 *                  reference)
 */
@Deprecated
public class DataloaderInitialLoadItemAction extends ItemAction
{
	private static final Logger LOG = Logger.getLogger(DataloaderInitialLoadItemAction.class);

	@Override
	public ActionResult perform(final ActionEvent event) throws JaloBusinessException
	{
		final Item item = getItem(event);
		if (item == null)
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("text.sapproductconfig_configuration_not_set"),
					false);
		}

		if (!(item instanceof SAPConfiguration))
		{
			return new ActionResult(ActionResult.FAILED, false);
		}
		final SAPConfiguration configuration = (SAPConfiguration) item;

		final DataloaderConfiguration dataloaderConfiguration = new DataloaderConfiguration();
		final IDataloaderSource dataloaderSource = getSAPSource(configuration);
		dataloaderConfiguration.setSource(dataloaderSource);

		final InitialDownloadConfiguration initialDownloadConfiguration = getFilterFiles(configuration);
		dataloaderConfiguration.setInitialDownloadConfiguration(initialDownloadConfiguration);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Running initial data load [SAP_SYSTEM='" + dataloaderSource.getEccSetting().getMessageServer() + "';SAP_SID='"
					+ dataloaderSource.getEccSetting().getSid() + "';SAP_RFC_DESTINATION='" + dataloaderSource.getRfcDestination()
					+ "']");
		}

		return performDataload(dataloaderConfiguration);
	}


	protected ActionResult performDataload(final DataloaderConfiguration dataloaderConfiguration)
	{
		final DataloaderFacadeImpl facadeImpl = new DataloaderFacadeImpl();

		Status status = facadeImpl.createTables(null);

		if (!status.isOK())
		{
			return new ActionResult(Status.ERROR, " - [" + status.getMessage() + "]", false);
		}


		status = facadeImpl.initialDownload(dataloaderConfiguration,
				new com.sap.custdev.projects.fbs.slc.dataloader.standalone.IProgressListener()
				{

					@Override
					public void progressMessage(final String msg)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug(msg);
						}
					}
				});

		if (!status.isOK())
		{
			return new ActionResult(Status.ERROR, " - [" + status.getMessage() + "]", false);
		}

		return new ActionResult(Status.OK, HMCHelper.getLocalizedString("text.sapproductconfig_initial_load_successful"), false);
	}

	protected IDataloaderSource getSAPSource(final SAPConfiguration configuration)
	{
		final SAPRFCDestination sapServer = (SAPRFCDestination) configuration
				.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_SERVER);

		final String rfcDestination = (String) configuration
				.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_RFC_DEST);

		final IDataloaderSource dataloaderSource;
		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		params.setUser(sapServer.getUserid());
		params.setPassword(sapServer.getPassword());
		params.setClient(sapServer.getClient());
		params.setClientRfcDestination(rfcDestination);
		params.setServerRfcDestination(sapServer.getRfcDestinationName());
		if (sapServer.isConnectionTypeAsPrimitive())
		{
			// no load balance
			params.setUseLoadBalance(false);
			params.setInstanceno(sapServer.getInstance());
			params.setTargetHost(sapServer.getTargetHost());
		}
		else
		{
			// load balance
			params.setUseLoadBalance(true);
			params.setSysId(sapServer.getSid());
			params.setMsgServer(sapServer.getMessageServer());
			params.setLogonGroup(sapServer.getGroup());
		}
		dataloaderSource = new DataloaderSource(params);
		return dataloaderSource;
	}

	protected InitialDownloadConfiguration getFilterFiles(final SAPConfiguration configuration)
	{
		final String kbFilterFile = getAbsolutFilePathForMedia(configuration,
				SapproductconfighmcConstants.CONFIGURATION_DATALOADER_FILTER_KB);
		final String materialsFilterFile = getAbsolutFilePathForMedia(configuration,
				SapproductconfighmcConstants.CONFIGURATION_DATALOADER_FILTER_MATERIAL);
		final String conditionsFilterFile = getAbsolutFilePathForMedia(configuration,
				SapproductconfighmcConstants.CONFIGURATION_DATALOADER_FILTER_CONDITION);

		final InitialDownloadConfiguration initialDownloadConfiguration = new InitialDownloadConfiguration(kbFilterFile,
				materialsFilterFile, conditionsFilterFile);
		return initialDownloadConfiguration;
	}

	protected String getAbsolutFilePathForMedia(final SAPConfiguration configuration, final String property)
	{
		final Media filterFile = (Media) configuration.getProperty(property);

		String filterFileAbsolutPath = null;
		if (filterFile != null && filterFile.hasData() && filterFile.isAlive())
		{
			filterFileAbsolutPath = filterFile.getFile().getAbsoluteFile().toString();
		}

		return filterFileAbsolutPath;
	}
}
