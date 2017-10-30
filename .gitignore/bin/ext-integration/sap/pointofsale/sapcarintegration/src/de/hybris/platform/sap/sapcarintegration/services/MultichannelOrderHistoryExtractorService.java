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
package de.hybris.platform.sap.sapcarintegration.services;

import java.util.List;

import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.sap.sapcarintegration.data.CarMultichannelOrderHistoryData;


public interface MultichannelOrderHistoryExtractorService extends
		CarOrderHistoryExtractorService {

	abstract CarMultichannelOrderHistoryData extractSalesDocumentHeader(final ODataFeed feed);
	
	abstract void extractSalesDocumentEntries(final CarMultichannelOrderHistoryData order, final ODataFeed feed);

	abstract List<CarMultichannelOrderHistoryData> extractMultichannelOrders(
			PaginationData paginationData, ODataFeed feed);

}
