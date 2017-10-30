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

import com.google.common.collect.Lists;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.returns.service.RestockConfigService;
import de.hybris.platform.warehousing.model.SourcingConfigModel;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.AtpFormulas;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import javax.annotation.Resource;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@Ignore("Just a base class for sourcing integration tests.")
public class BaseSourcingIntegrationTest extends BaseWarehousingIntegrationTest
{
	@Resource
	protected SourcingService sourcingService;
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
	@Resource
	protected AtpFormulas atpFormulas;
	@Resource
	protected RestockConfigService restockConfigService;

    public BaseSourcingIntegrationTest() {
    }


    @Before
	public void setupShopper()
	{
		users.Nancy();
	}

	@Before
	public void setupBaseStore()
	{
		baseStores.NorthAmerica().setPointsOfService(Lists.newArrayList( //
				pointsOfService.Boston(), //
				pointsOfService.Montreal_Downtown() //
		));
		saveAll();
	}

	@After
	public void resetFactors()
	{
		modelService.remove(baseStores.NorthAmerica().getSourcingConfig());
	}

	/**
	 * Assert that the sourcing result selected the correct warehouse and sourced the correct quantity.
	 *
	 * @param results
	 * @param expectedWarehouse
	 * @param expectedAllocation
	 */
	protected void assertSourcingResultContents(final SourcingResults results, final WarehouseModel expectedWarehouse,
			final Map<ProductModel, Long> expectedAllocation)
	{
		final Optional<SourcingResult> sourcingResult = results.getResults().stream()
				.filter(result -> result.getWarehouse().getCode().equals(expectedWarehouse.getCode())).findFirst();

		assertTrue("No sourcing result with warehouse " + expectedWarehouse.getCode(), sourcingResult.isPresent());
		assertEquals(expectedWarehouse.getCode(), sourcingResult.get().getWarehouse().getCode());
		assertTrue(sourcingResult.get().getAllocation().entrySet().stream().allMatch(
				result -> expectedAllocation.get(result.getKey().getProduct()).equals(result.getValue())
		));
	}


	/**
	 * Sets the sourcing factors to use.
	 * @param baseStore
	 * @param allocation
	 * @param distance
	 * @param priority
	 */
	protected void setSourcingFactors(final BaseStoreModel baseStore, final int allocation, final int distance, final int priority)
	{
		final SourcingConfigModel sourcingConfig = baseStore.getSourcingConfig();
		sourcingConfig.setDistanceWeightFactor(distance);
		sourcingConfig.setAllocationWeightFactor(allocation);
		sourcingConfig.setPriorityWeightFactor(priority);
		modelService.save(sourcingConfig);
	}
	/**
	 * Get the quantity allocated for the order entry with the given product.
	 * 
	 * @param result
	 * @param product
	 * @return quantity allocated
	 */
	protected Long getAllocationForProduct(final SourcingResult result, final ProductModel product)
	{
		return result.getAllocation().get(result.getAllocation().keySet().stream() //
				.filter(entry -> entry.getProduct().getCode().equals(product.getCode())) //
				.findFirst().get());
	}

	public Boolean verifyConsignment_Camera(OrderModel order, String location, Long quantityDeclined,
			Long quantityAllocated, Long quantityPending)
	{
		return order.getConsignments().stream().anyMatch(result ->
		{
			Boolean warehouseResult = result.getWarehouse().getCode().equals(location);
			Boolean DeclineQuantityResult = result.getConsignmentEntries().stream().allMatch(
					e -> e.getQuantityDeclined().equals(quantityDeclined));
			Boolean QuantityAllocatedResult = result.getConsignmentEntries().stream()
					.allMatch(e -> e.getQuantity().equals(quantityAllocated));
			Boolean QuantityPendingResult = result.getConsignmentEntries().stream()
					.allMatch(e -> e.getQuantityPending().equals(quantityPending));
			return warehouseResult && DeclineQuantityResult && QuantityAllocatedResult && QuantityPendingResult;
		});
	}

	public Boolean verifyConsignment_Camera_MemoryCard(OrderModel order, String location, Long quantityDeclined_Camera,
			Long quantityAllocated_Camera, Long quantityDeclined_MemoryCard, Long quantityAllocated_MemoryCard)
	{
		return order.getConsignments().stream().anyMatch(result ->
		{
			Boolean warehouseResult = result.getWarehouse().getCode().equals(location);
			Boolean DeclineQuantityResult_Camera = result.getConsignmentEntries().stream().anyMatch(
					e -> e.getQuantityDeclined().equals(quantityDeclined_Camera) && e.getOrderEntry().getProduct().getCode().equals(Products.CODE_CAMERA));
			Boolean QuantityResult_Camera = result.getConsignmentEntries().stream().anyMatch(
					e -> e.getQuantity().equals(quantityAllocated_Camera) && e.getOrderEntry().getProduct().getCode().equals(Products.CODE_CAMERA));
			Boolean DeclineQuantityResult_MemoryCard = result.getConsignmentEntries().stream().anyMatch(
					e -> e.getQuantityDeclined().equals(quantityDeclined_MemoryCard) && e.getOrderEntry().getProduct().getCode().equals(Products.CODE_MEMORY_CARD));
			Boolean QuantityResult_MemoryCard = result.getConsignmentEntries().stream().anyMatch(
					e -> e.getQuantity().equals(quantityAllocated_MemoryCard) && e.getOrderEntry().getProduct().getCode().equals(Products.CODE_MEMORY_CARD));
			return warehouseResult && DeclineQuantityResult_Camera && QuantityResult_Camera &&  DeclineQuantityResult_MemoryCard && QuantityResult_MemoryCard;
		});
	}

	/**
	 * refresh order.
	 * @param order
	 * @return
	 */
	protected OrderModel refreshOrder(OrderModel order)
	{
		final AbstractOrderEntryModel orderEntry = order.getEntries().get(0);
		//Refresh consignment entries to update quantityShipped
		orderEntry.getConsignmentEntries().stream().forEach(entry -> modelService.refresh(entry));
		return order;
	}
}
