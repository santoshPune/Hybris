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
package de.hybris.platform.sap.productconfig.model.intf;

import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.model.ModelService;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderManager;


/**
 * Responsible to call SSC for initial and delta load
 */
public interface DataLoader
{

	void performInitialLoad(SAPConfigurationModel sapConfiguration, DataloaderManager dataloaderManager,
			ModelService modelService);

	void performDeltaLoad(SAPConfigurationModel sapConfiguration, DataloaderManager dataloaderManager, ModelService modelService);



}
