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
package de.hybris.platform.chinesestoreaddon.setup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.chinesestoreaddon.constants.ChinesestoreaddonConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;

/**
 * This class provides hooks into the system's initialization and update
 * processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = ChinesestoreaddonConstants.EXTENSIONNAME)
public class ChineseStoreAddonSystemSetup extends AbstractSystemSetup {

	public static final String ELECTRONICS = "electronics";
	public static final String POWERTOOLS = "powertools";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private BaseSiteService baseSiteService;

	/**
	 * This method will be used for initialization.
	 */
	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions() {
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *            the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context) {

		BaseSiteModel electronicsSite = getBaseSiteService().getBaseSiteForUID(ELECTRONICS);
		BaseSiteModel powertoolsSite = getBaseSiteService().getBaseSiteForUID(POWERTOOLS);

		final List<ImportData> importData = new ArrayList<ImportData>();

		if (electronicsSite != null) {

			addImportData(importData, ELECTRONICS);

		}
		if (powertoolsSite != null) {

			addImportData(importData, POWERTOOLS);

		}

		if (getBooleanSystemSetupParameter(context, IMPORT_CORE_DATA)) {
			importCoreData(context, importData);
		}
		if (getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA)) {
			importSampleData(context, importData);
		}

	}

	/**
	 * this method will import that impex files in coredata.
	 *
	 * @param context
	 *            the context provides the selected parameters and values
	 * @param importData
	 *            it provide parameters of
	 *            ProductCatalogName,ContentCatalogName,StoreName.
	 */
	protected void importCoreData(final SystemSetupContext context, final List<ImportData> importData) {
		final String extensionName = context.getExtensionName();

		for (final ImportData data : importData) {
			importCoreDataProductCatalog(extensionName);
			synchronizeProductCatalog(context, data);
			for (final String storeName : data.getStoreNames()) {
				logInfo(context, String.format("Begin importing store core data for [%s]", storeName));
				importCoreDataStore(context.getExtensionName(), storeName);

				if (getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS)) {
					logInfo(context, String.format("Activating solr index for [%s]", storeName));
					runSolrIndex(storeName);
				}

			}
			importCoreDataCommon(extensionName);
		}
	}

	/**
	 * this method will import that impex files in sampledata.
	 * 
	 * @param context
	 *            the context provides the selected parameters and values
	 * @param importData
	 *            it provide parameters of
	 *            ProductCatalogName,ContentCatalogName,StoreName.
	 */
	protected void importSampleData(final SystemSetupContext context, final List<ImportData> importData) {
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData) {
			importSampleDataProductCatalog(extensionName, data.getProductCatalogName());
			synchronizeProductCatalog(context, data);
			for (final String storeName : data.getStoreNames()) {
				logInfo(context, String.format("Begin importing store sample data for [%s]", storeName));
				importSampleDataStore(context.getExtensionName(), storeName);

				logInfo(context, String.format("Begin importing solr index sample data for [%s]", storeName));
				importSampleDataSolrIndex(context.getExtensionName(), storeName);

				if (getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS)) {
					logInfo(context, String.format("Activating solr index for [%s]", storeName));
					runSolrIndex(storeName);
				}
			}
		}
	}

	/**
	 * this method will import that impex files in common folder.
	 * 
	 * @param extensionName
	 *            which extension should be import
	 */
	protected void importCoreDataCommon(final String extensionName) {
		getSetupImpexService().importImpexFile(String.format("/%s/import/common/common-addon-extra.impex", extensionName), true);
		getSetupImpexService().importImpexFile(String.format("/%s/import/common/common-addon-extra_zh.impex", extensionName), true);
	}

	/**
	 * this method will import that impex files in productCatalogs folder.
	 * 
	 * @param extensionName
	 *            which extension should be import
	 */
	protected void importCoreDataProductCatalog(final String extensionName) {
		getSetupImpexService().importImpexFile(String.format("/%s/import/productCatalogs/template/catalog.impex", extensionName), false);
	}

	/**
	 * this method will import that impex files in stores folder.
	 * 
	 * @param extensionName
	 *            which extension should be import
	 * @param storeName
	 *            which store should be import
	 */
	protected void importCoreDataStore(final String extensionName, final String storeName) {
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/store.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/site.impex", extensionName, storeName), false);
	}

	/**
	 * this method will import that impex files in ProductCatalog folder.
	 * 
	 * @param extensionName
	 *            which extension should be import
	 */
	protected void importSampleDataProductCatalog(final String extensionName, final String productCatalogName) {
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products.impex", extensionName, productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-prices.impex", extensionName, productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-media.impex", extensionName, productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-stocklevels.impex", extensionName, productCatalogName), false);
		getSetupImpexService()
				.importImpexFile(
						String.format("/%s/import/productCatalogs/%sProductCatalog/products-pos-stocklevels.impex", extensionName, productCatalogName),
						false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-tax.impex", extensionName, productCatalogName), false);
	}

	/**
	 * this method will import that impex files in stores folder.
	 * 
	 * @param extensionName
	 *            which extension should be import
	 */
	protected void importSampleDataStore(final String extensionName, final String storeName) {
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/promotions.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/promotions_en.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/promotions_zh.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/points-of-service-media.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/points-of-service.impex", extensionName, storeName), false);
	}

	/**
	 * this method will import that impex files in solr folder.
	 * 
	 * @param extensionName
	 */
	protected void importSampleDataSolrIndex(final String extensionName, final String storeName) {

		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/solr.impex", extensionName, storeName), false);
		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

	/**
	 * this method will synchronizing productCatalog.
	 * 
	 * @param context
	 *            System setup context
	 * @param importData
	 *            it provide parameters of
	 *            ProductCatalogName,ContentCatalogName,StoreName.
	 */
	protected void synchronizeProductCatalog(final SystemSetupContext context, final ImportData importData) {

		final String catalogName = importData.getProductCatalogName();
		logInfo(context, String.format("Begin synchronizing Product Catalog [%s]", catalogName));

		getSetupSyncJobService().createProductCatalogSyncJob(String.format("%sProductCatalog", catalogName));
		final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(String.format("%sProductCatalog", catalogName));
		if (isSyncRerunNeeded(syncCronJobResult)) {
			logInfo(context, String.format("Product Catalog [%s] sync has issues.", catalogName));
		}
	}

	/**
	 * this method will synchronizing solr.
	 * 
	 * @param storeName
	 *            which store should be run solr.
	 */
	protected void runSolrIndex(final String storeName) {
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", storeName), true);
		getSetupSolrIndexerService().activateSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	/**
	 * add importData to importDatalist
	 * 
	 * @param importDataList
	 *            is used for setting parameter of
	 *            ProductCatalogName,ContentCatalogName,StoreName. That is used
	 *            for import data.
	 * @param site
	 *            is parameter for chose which site's data should be import.
	 */
	protected void addImportData(List<ImportData> importDataList, String site) {
		final ImportData importData = new ImportData();

		importData.setProductCatalogName(site);
		importData.setContentCatalogNames(Arrays.asList(site));
		importData.setStoreNames(Arrays.asList(site));

		importDataList.add(importData);
	}
}
