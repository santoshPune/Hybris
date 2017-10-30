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
 *
 */
package de.hybris.platform.warehousingfacade.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;

import java.util.List;
import java.util.Set;


/**
 * Warehousing Facade for the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel}, which provides the basic CRUD features for the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel}
 */
public interface WarehousingConsignmentFacade
{
	/**
	 * API to fetch all consignments in the system
	 *
	 * @param pageableData pageable object that contains info on the number or pages and how many items in each page
	 *                     in addition the sorting info
	 * @return list of consignments
	 */
	SearchPageData<ConsignmentData> getConsignments(PageableData pageableData);

	/**
	 * API to fetch all consignments in the system that have certain status(es)
	 *
	 * @param pageableData         pageable object that contains info on the number or pages and how many items in each page
	 *                             in addition the sorting info
	 * @param consignmentStatusSet set of {@link ConsignmentStatus}, in which we want to get list of consignments for
	 * @return list of consignments that complies with passed consignment status(es)
	 */
	SearchPageData<ConsignmentData> getConsignmentsByStatuses(PageableData pageableData, Set<ConsignmentStatus> consignmentStatusSet);

	/**
	 * API to get consignmentEntries for the given {@link de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE}
	 *
	 * @param code
	 * 		the consignment's code
	 * @param pageableData
	 *           pageable object that contains info on the number or pages and how many items in each page in addition
	 *           the sorting info
	 * @return SearchPageData that contains a list of the consignmentEntries for the given consignment
	 */
	SearchPageData<ConsignmentEntryData> getConsignmentEntriesForConsignmentCode(String code, PageableData pageableData);

	/**
	 * API to return details of a single consignment according to a consignment code.
	 *
	 * @param code consignment code.
	 * @return Consignment details for the given consignment code
	 */
	ConsignmentData getConsignmentForCode(String code);

	/**
	 * API to return all consignment statuses
	 *
	 * @return list of {@link ConsignmentStatus}
	 */
	List<ConsignmentStatus> getConsignmentStatuses();

	/**
	 * API to return all decline reasons.
	 *
	 * @return a list of {@link DeclineReason}
	 */
	List<DeclineReason> getDeclineReasons();

	/**
	 * API to return all warehouses fit for souring for the given consignment code.
	 *
	 * @param code
	 * 			the consignment code
	 * @param pageableData
	 * 			pageable object that contains info on the number or pages and how many items in each page
	 *          in addition the sorting info
	 * @return SearchPageData that contains a list of locations fit for sourcing
	 */
	SearchPageData<WarehouseData> getSourcingLocationsForConsignmentCode(String code, PageableData pageableData);

	/**
	 * Confirm Consignment's shipping
	 * @param code
	 * 		the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE} for the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel} to be shipped
	 */
	void confirmShipConsignment(String code);

	/**
	 * Confirm Consignment's pick up
	 *
	 * @param code
	 * 		the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE} for the {@link de.hybris.platform.ordersplitting.model.ConsignmentModel} to be picked up
	 */
	void confirmPickupConsignment(String code);

	/**
	 * Checks if the confirm ship/pickup is possible for the given {@link de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE}
	 * @param code
	 * 		the code for the consignment to be confirmed
	 * @return boolean to indicate if confirmation of the given consignment is possible
	 */
	boolean isConsignmentConfirmable(String code);
}
