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
package de.hybris.platform.chineseprofile.setup;

import de.hybris.platform.chineseprofile.constants.ChineseprofileaddonConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


/**
 *
 */
@SystemSetup(extension = ChineseprofileaddonConstants.EXTENSIONNAME)
public class ChineseProfileAddonInitialDataSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ChineseProfileAddonInitialDataSetup.class);

	public static final String ELECTRONICS = "electronics";
	public static final String APPAREL = "apparel";
	public static final String POWERTOOLS = "powertools";
	private static final String IMPORT_CMS_DATA = "import Neccessary CMS Content";

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CMS_DATA, "Import Neccessary CMS Data", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData electronicsImportData = new ImportData();
		electronicsImportData.setProductCatalogName(ELECTRONICS);
		electronicsImportData.setContentCatalogNames(Arrays.asList(ELECTRONICS));
		electronicsImportData.setStoreNames(Arrays.asList(ELECTRONICS));
		importData.add(electronicsImportData);
		importSampleContentCatalog(context, importData);
	}

	private void importSampleContentCatalog(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(String.format("/%s/import/contentCatalogs/%sContentCatalog/cms-content.impex",
						extensionName, contentCatalogName), false);
			}
		}
		synchronizeContentCatalog(this, context, importData);
	}

	private void synchronizeContentCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final List<ImportData> importData)
	{
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				systemSetup.logInfo(context, String.format("Begin synchronizing Content Catalog [%s]", contentCatalogName));
				getSetupSyncJobService().createContentCatalogSyncJob(String.format("%sContentCatalog", contentCatalogName));
				final PerformResult syncCronJobResult = getSetupSyncJobService()
						.executeCatalogSyncJob(String.format("%sContentCatalog", contentCatalogName));
				if (isSyncRerunNeeded(syncCronJobResult))
				{
					systemSetup.logInfo(context, String.format("Content Catalog [%s] sync has issues.", contentCatalogName));
				}
			}
		}
	}
}
