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
package de.hybris.platform.sap.sapcarcommercefacades.order;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.sap.sapcarintegration.data.CarMultichannelOrderHistoryData;
import de.hybris.platform.sap.sapcarintegration.data.CarOrderHistoryData;

import java.util.Date;


/**
 * 
 */
public interface CarOrderFacade
{

	/**
	 * Returns the detail of an Order corresponding to unique key (date, store, transaction index). use
	 * getOrderDetails(String transactionDate, String storeId, Integer transactionIndex)
	 * 
	 * @param transactionDate
	 * @param storeId
	 * @param transactionIndex
	 * @return the order details made in store
	 */
	@Deprecated
	CarOrderHistoryData getOrderDetails(Date transactionDate, String storeId, Integer transactionIndex);


	CarOrderHistoryData getOrderDetails(String transactionDate, String storeId, Integer transactionIndex);


	/**
	 * Returns the order history of the current user for given customer Id.
	 * 
	 * @param pageableData
	 * @return The order history made in store for current user.
	 */
	SearchPageData<CarOrderHistoryData> getPagedOrderHistoryForCustomer(PageableData pageableData);


	/**
	 * @param pageableData
	 * @return
	 */
	SearchPageData<CarMultichannelOrderHistoryData> getPagedMultichannelOrderHistoryForCustomer(PageableData pageableData);


	/**
	 * @param customerNumber
	 * @param TransactionNumber
	 * @return
	 */
	CarMultichannelOrderHistoryData getSalesDocumentDetails(String TransactionNumber);

}
