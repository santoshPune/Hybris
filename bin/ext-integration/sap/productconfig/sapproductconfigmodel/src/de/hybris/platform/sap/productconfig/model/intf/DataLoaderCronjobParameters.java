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

/**
 * Spring managed parameter container for dataload jobs
 */
public interface DataLoaderCronjobParameters
{

	/**
	 * @return Data Loader Start Job Bead Id
	 */
	public String getDataloadStartJobBeanId();

	/**
	 * @param dataloadStartJobBeanId
	 *           Data Loader Start Job Bead Id
	 */
	public void setDataloadStartJobBeanId(String dataloadStartJobBeanId);

	/**
	 * @return Data Loader Stop Job Bead Id
	 */
	public String getDataloadStopJobBeanId();

	/**
	 * @param dataloadStopJobBeanId
	 *           Data Loader Stop Job Bead Id
	 */
	public void setDataloadStopJobBeanId(String dataloadStopJobBeanId);

	/**
	 * @return Node Id for Start Job
	 */
	public Integer retrieveNodeIdForStartJob();

	/**
	 * @return Node Id for Stop Job
	 */
	public Integer retrieveNodeIdForStopJob();

}
