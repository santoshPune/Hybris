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
package de.hybris.platform.chinesepaymentservices.setup;

import de.hybris.platform.chinesepaymentservices.constants.ChinesepaymentservicesConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


@SystemSetup(extension = ChinesepaymentservicesConstants.EXTENSIONNAME)
public class ChinesePaymentInitialDataSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ChinesePaymentInitialDataSetup.class);
	private static final String ELECTRONICS = "electronics";
	private static final String IMPORT_CORE_DATA = "import Payment Data";

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Payment Data", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData electronicsImportData = new ImportData();
		electronicsImportData.setProductCatalogName(ELECTRONICS);
		electronicsImportData.setContentCatalogNames(Arrays.asList(ELECTRONICS));
		electronicsImportData.setStoreNames(Arrays.asList(ELECTRONICS));
		importData.add(electronicsImportData);
		importCommonData(context.getExtensionName());
	}

	protected void importCommonData(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/checkout.impex", extensionName), false);
	}

	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}
