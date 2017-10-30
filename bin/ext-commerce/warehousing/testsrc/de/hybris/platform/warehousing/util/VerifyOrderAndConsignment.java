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
package de.hybris.platform.warehousing.util;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;
import org.junit.Ignore;

import javax.annotation.Resource;


@Ignore("Just a base class for sourcing integration tests.")
public class VerifyOrderAndConsignment extends BaseWarehousingIntegrationTest
{
	@Resource
	protected SourcingService sourcingService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected Orders orders;
	@Resource
	protected BaseStores baseStores;
	@Resource
	protected Warehouses warehouses;
	@Resource
	protected Addresses addresses;
	@Resource
	protected StockLevels stockLevels;
	@Resource
	protected PointsOfService pointsOfService;
	@Resource
	protected Products products;
	@Resource
	protected Users users;

	private static final String CODE_CAMERA = "camera";

	public Boolean verifyConsignment_Camera(OrderModel order, String location, Long quantityDeclined,
			Long quantityAllocated, Long quantityPending)
	{
		return verifyConsignment(order, CODE_CAMERA, location, quantityDeclined,
				quantityAllocated, quantityPending);
	}

	public Boolean verifyConsignment(OrderModel order, String productCode, String location, Long quantityDeclined,
			Long quantityAllocated, Long quantityPending)
	{
		Boolean test = false;
		for(ConsignmentModel consignment:order.getConsignments()){
			if(getWarehouseCompareResult(consignment, location).booleanValue()){
				if(getDeclineQuantityResult(consignment, quantityDeclined,productCode)){
					if(getAllocatedQuantityResult(consignment, quantityAllocated, productCode)){
						if(getPendingQuantityResult(consignment, quantityPending, productCode)){
							test=true;
						}
					}
				}
			}
		}

		return order.getConsignments().stream().anyMatch(
				result ->getWarehouseCompareResult(result, location)
					&& getDeclineQuantityResult(result, quantityDeclined,productCode)
					&& getAllocatedQuantityResult(result, quantityAllocated, productCode)
					&& getPendingQuantityResult(result, quantityPending, productCode));
	}

	public Boolean verifyConsignment_Camera_MemoryCard(final OrderModel order, final String location,
			final Long quantityDeclined_Camera, final Long quantityAllocated_Camera, final Long quantityPending_Camera,
			final Long quantityDeclined_MemoryCard, final Long quantityAllocated_MemoryCard, final Long quantityPending_MemoryCard)
	{
		return order.getConsignments().stream().anyMatch(
				result -> getWarehouseCompareResult(result, location)
						&& getDeclineQuantityResult(result, quantityDeclined_Camera, Products.CODE_CAMERA)
						&& getAllocatedQuantityResult(result, quantityAllocated_Camera, Products.CODE_CAMERA)
						&& getDeclineQuantityResult(result, quantityDeclined_MemoryCard, Products.CODE_MEMORY_CARD)
						&& getAllocatedQuantityResult(result, quantityAllocated_MemoryCard, Products.CODE_MEMORY_CARD)
						&& getPendingQuantityResult(result, quantityPending_Camera, Products.CODE_CAMERA)
						&& getPendingQuantityResult(result, quantityPending_MemoryCard, Products.CODE_MEMORY_CARD));
	}

	private Boolean getAllocatedQuantityResult(final ConsignmentModel consignmentModel, final long quantityAllocated,
			final String productCode)
	{
		return consignmentModel.getConsignmentEntries().stream().anyMatch(
				e -> e.getQuantity().equals(quantityAllocated) && e.getOrderEntry().getProduct().getCode().equals(
						productCode));
	}

	private Boolean getDeclineQuantityResult(final ConsignmentModel consignmentModel, final long quantityDeclined,
			final String productCode)
	{
		return consignmentModel.getConsignmentEntries().stream().anyMatch(
				e -> e.getQuantityDeclined().equals(quantityDeclined) && e.getOrderEntry().getProduct().getCode().equals(
						productCode));
	}

	private Boolean getPendingQuantityResult(final ConsignmentModel consignmentModel, final long quantityPending,
			final String productCode)
	{
		return consignmentModel.getConsignmentEntries().stream().anyMatch(
				e -> e.getQuantityPending().equals(quantityPending) && e.getOrderEntry().getProduct().getCode().equals(
						productCode));
	}

	private Boolean getWarehouseCompareResult(final ConsignmentModel consignmentModel, final String location)
	{
		return consignmentModel.getWarehouse().getCode().equals(location);
	}


}
