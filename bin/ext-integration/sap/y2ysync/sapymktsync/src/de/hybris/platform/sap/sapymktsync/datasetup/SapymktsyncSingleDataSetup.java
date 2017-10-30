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
package de.hybris.platform.sap.sapymktsync.datasetup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.sap.sapymktsync.constants.SapymktsyncConstants;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 * Configure the y2ySync tool to capture all the changes in the hybris platform to sync them to the hybris marketing
 * datahub extension
 */
@SystemSetup(extension = SapymktsyncConstants.EXTENSIONNAME)
public class SapymktsyncSingleDataSetup extends AbstractSystemSetup
{

	private ModelService modelService;

	private String buildRemoveMarkerScript()
	{

		final StringBuilder sb = new StringBuilder();

		sb.append("import de.hybris.platform.servicelayer.search.FlexibleSearchQuery\n\n");

		sb.append("def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {ItemVersionMarker}')\n");
		sb.append("def result = flexibleSearchService.search(fQuery)\n\n");

		sb.append("result.getResult().forEach {\n");
		sb.append("modelService.remove(it)\n}");

		return sb.toString();
	}

	private String buildSyncScript()
	{

		final StringBuilder sb = new StringBuilder();

		sb.append("import de.hybris.platform.servicelayer.search.FlexibleSearchQuery\n");
		sb.append("import de.hybris.y2ysync.services.SyncExecutionService.ExecutionMode\n\n");

		sb.append("def syncToDatahubJob = findJob '%s' \n");

		sb.append("syncExecutionService.startSync(syncToDatahubJob, ExecutionMode.SYNC)\n\n");

		sb.append("def findJob(code) {\n\t");
		sb.append("def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {Y2YSyncJob} WHERE {code}=?code')\n\t");
		sb.append("fQuery.addQueryParameter('code', code)\n\t");
		sb.append("flexibleSearchService.searchUnique(fQuery)\n}");

		return sb.toString();
	}

	private void createScript(final String code, final String content)
	{
		final ScriptModel script = modelService.create(ScriptModel.class);
		script.setScriptType(ScriptType.GROOVY);
		script.setContent(content);
		script.setCode(code);
		modelService.save(script);
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		return Collections.<SystemSetupParameter> emptyList();
	}

	private String getSyncScriptContent(final String syncJob)
	{
		return String.format(buildSyncScript(), syncJob);
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
	public void setup(final SystemSetupContext context)
	{
		importImpexFile(context, "/sapymktsync/import/sapymktsync-single-data-streams.impex");

		createScript("syncYmktSingleToDataHub", getSyncScriptContent("ymktToDatahubJob"));
		createScript("syncYmktSingleToZip", getSyncScriptContent("ymktToZipJob"));
		createScript("removeYmktSingleVersionMarkers", buildRemoveMarkerScript());
	}


}
