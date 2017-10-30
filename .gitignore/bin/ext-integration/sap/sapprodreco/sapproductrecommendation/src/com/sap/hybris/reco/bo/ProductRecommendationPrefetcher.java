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
package com.sap.hybris.reco.bo;

import de.hybris.platform.sap.core.jco.connection.JCoConnection;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;


/**
 * Multithreaded prefetching of Product Recommendation For RFC
 */
public class ProductRecommendationPrefetcher implements Callable<JCoParameterList>
{
	private final static Logger LOG = Logger.getLogger(ProductRecommendationPrefetcher.class.getName());
	protected JCoConnection jCoConnection;
	protected JCoFunction function;

	/**
	 * Product Recommendation Prefetcher
	 *
	 * @param jCoConnection
	 * @param function
	 */
	public ProductRecommendationPrefetcher(final JCoConnection jCoConnection, final JCoFunction function)
	{
		this.jCoConnection = jCoConnection;
		this.function = function;
	}

	/**
	 * Backend call for PRI using RFC to get the product recommendations
	 *
	 * @return JCoParameterList
	 */
	@Override
	public JCoParameterList call() throws Exception
	{
		LOG.debug("\n\n RFC Call \n\n");
		jCoConnection.execute(function);
		return function.getExportParameterList();
	}
}
