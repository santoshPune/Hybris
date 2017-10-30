/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.jiathis.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.jiathis.constants.ChinesesnsjiathisaddonConstants;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = ChinesesnsjiathisaddonConstants.EXTENSIONNAME)
public class JiaThisSystemSetup extends AbstractSystemSetup
{

	private static final String IMPORT_SYNC_CATALOGS = "contentCatalogs";
	private static final String ELECTRONICS = "electronics";
	private static final String CMS_CONTENT_FORMAT = "/{0}/import/{1}/{2}/cms-content.impex";

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_SYNC_CATALOGS, "Import JiaThis Content Catalogs Data", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{

		if (getBooleanSystemSetupParameter(context, IMPORT_SYNC_CATALOGS))
		{
			importContentCatalog(context, ELECTRONICS, Collections.singletonList(ELECTRONICS));

			final ImportData electronicsImportData = new ImportData();
			electronicsImportData.setProductCatalogName(ELECTRONICS);
			electronicsImportData.setContentCatalogNames(Arrays.asList(ELECTRONICS));
			electronicsImportData.setStoreNames(Arrays.asList(ELECTRONICS));
			// Send an event to notify any AddOns that the initial data import is complete
			getEventService().publishEvent(new SampleDataImportedEvent(context, Arrays.asList(electronicsImportData)));
		}
	}

	/**
	 * import content catalog
	 *
	 * @param context
	 * @param catalogName
	 * @param contentCatalogs
	 */
	private void importContentCatalog(final SystemSetupContext context, final String catalogName,
			final List<String> contentCatalogs)
	{
		logInfo(context, "Begin importing catalog [" + catalogName + "]");

		importImpexFile(context, MessageFormat.format(CMS_CONTENT_FORMAT, ChinesesnsjiathisaddonConstants.EXTENSIONNAME,
				IMPORT_SYNC_CATALOGS, catalogName + IMPORT_SYNC_CATALOGS));
		logInfo(context, "Done importing catalog [" + catalogName + "]");

		// perform content sync jobs
		for (final String contentCatalog : contentCatalogs)
		{
			synchronizeContentCatalog(context, contentCatalog, true);
		}
	}

	/**
	 * sync content catalog from staged to online
	 *
	 * @param context
	 * @param catalogName
	 * @param sync
	 */
	private void synchronizeContentCatalog(final SystemSetupContext context, final String catalogName, final boolean sync)
	{
		logInfo(context,
				"Begin synchronizing Content Catalog [" + catalogName + "] - " + (sync ? "synchronizing" : "initializing job"));

		createContentCatalogSyncJob(context, catalogName + "ContentCatalog");

		if (sync)
		{
			final PerformResult syncCronJobResult = executeCatalogSyncJob(context, catalogName + "ContentCatalog");
			if (isSyncRerunNeeded(syncCronJobResult))
			{
				logInfo(context, "Catalog catalog [" + catalogName + "] sync has issues.");
			}
		}

		logInfo(context, "Done " + (sync ? "synchronizing" : "initializing job") + " Content Catalog [" + catalogName + "]");
	}

}
