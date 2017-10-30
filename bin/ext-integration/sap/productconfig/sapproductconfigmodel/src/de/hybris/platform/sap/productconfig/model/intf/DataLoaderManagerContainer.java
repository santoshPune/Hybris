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

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderManager;


/**
 * Spring managed wrapper for {@link DataloaderManager}
 */
public interface DataLoaderManagerContainer
{

	/**
	 * @param manager
	 *           SSC Data Loader Manager
	 */
	void setDataLoaderManager(DataloaderManager manager);

	/**
	 * @return SSC Data Loader Manager
	 */
	DataloaderManager getDataLoaderManager();

	/**
	 * Resume was triggered
	 *
	 * @param b
	 */
	void setResumePerformed(boolean b);

	/**
	 * @return Resume was triggered
	 */
	boolean isResumePerformed();

}
