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
package de.hybris.platform.yacceleratorordermanagement.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.util.DeclineEntryBuilder;
import de.hybris.platform.warehousing.util.VerifyOrderAndConsignment;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This integration test creates a process and tests the order decline after the consignment is created. Please make
 * sure that you have initialized and update junit tenant before running this test.
 */
@IntegrationTest
public class DeclineIntegrationTest extends BaseAcceleratorSourcingIntegrationTest
{
	private OrderModel order2;
	private Map<ConsignmentEntryModel, Long> declineEntryInfo;
	private Map<ConsignmentEntryModel, Long> declineEntryInfo_2;
	private Map<ConsignmentEntryModel, Long> declineEntryInfoManual;
	private VerifyOrderAndConsignment verifyOrderAndConsignment = new VerifyOrderAndConsignment();
	private static final Logger LOG = Logger.getLogger(DeclineIntegrationTest.class);

	@Before
	public void setUp() throws Exception
	{
		if (order != null)
		{
			modelService.remove(order);
		}
		declineEntryInfo = new HashMap<ConsignmentEntryModel, Long>();
		declineEntryInfo_2 = new HashMap<ConsignmentEntryModel, Long>();
		declineEntryInfoManual = new HashMap<ConsignmentEntryModel, Long>();
	}

	@After
	public void cleanUp()
	{
		cleanUpData();
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS with new location when to busy and ban first location<br>
	 * <p>
	 */

	@Test
	public void shouldAutoReallocate_SingleEntry_SuccessReSourced__ToBusy_BanLocation() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 3);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.autoDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the ATP
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(6),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify the new consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("CANCELLED")));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));

		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));

		//when place another order
		order2 = orders.MemoryCard_Shipped(MEMORY_CARD_QTY);
		order2.setDeliveryMode(deliveryMode);
		modelService.saveAll();

		//verify the location get banned
		sourcingUtil.runOrderProcessForOrderBasedPriority(order2, OrderStatus.SUSPENDED);
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS with new location when to busy and ban first location<br>
	 * <p>
	 */

	@Test
	public void shouldManualReallocate_SingleEntry() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 3);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.manualDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, warehouses.Boston(),
				DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the ATP
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(6),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify the new consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("CANCELLED")));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));

		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));

		//when place another order
		order2 = orders.MemoryCard_Shipped(MEMORY_CARD_QTY);
		order2.setDeliveryMode(deliveryMode);
		modelService.saveAll();

		//verify the location get banned
		sourcingUtil.runOrderProcessForOrderBasedPriority(order2, OrderStatus.SUSPENDED);
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should fail, and confirm the consignment<br>
	 * <p>
	 */

	@Test
	public void shouldAutoReallocate_SingleEntry_PartiallyFailReSourced() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), Long.valueOf(1L));
		declineUtil.autoDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the ATP
		assertEquals(Long.valueOf(4),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(4),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify the new consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 1);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(1L), Long.valueOf(3L), Long.valueOf(2L)));

		//confirm all the consignment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().equals(OrderStatus.SUSPENDED));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity unallocated: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated());
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated().equals(Long.valueOf(1L)));
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS with new location when to out of stock and reset inventory<br>
	 * <p>
	 */
	@Test
	public void shouldAutoReallocate_SingleEntry_FailReSourced__OutOfStock_ResetInventory() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 1);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.autoDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, DeclineReason.OUTOFSTOCK);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the new consignment
		modelService.refresh(order);
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated().equals(Long.valueOf(2L)));
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("CANCELLED")));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L)));
		sourcingUtil.waitForOrderStatus(orderProcessModel, order, OrderStatus.SUSPENDED, timeOut);

		//then verify the ATP
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));

		assertEquals(Long.valueOf(0), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));

	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS with new location when partially reallocated<br>
	 * <p>
	 */
	@Test
	public void shouldAutoReallocate_SingleEntry_PartiallyReSourcingSuccess() throws InterruptedException
	{
		//Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		Collection<ConsignmentModel> consignmentModels = order.getConsignments();
		LOG.info("Number of consignments: " + consignmentModels.size());
		assertEquals(1, consignmentModels.size());

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		LOG.info("Stock added in boston warehouse for the camera");
		stockLevels.Camera(warehouses.Boston(), CAMERA_QTY.intValue());
		modelService.saveAll();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), Long.valueOf(2L));
		declineUtil.autoDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the new consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment
						.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(2L), Long.valueOf(3L), Long.valueOf(1L)));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), Long.valueOf(2L), Long.valueOf(2L)));
		sourcingUtil.waitForOrderStatus(orderProcessModel, order, OrderStatus.READY, timeOut);

		//then verify the ATP
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(1),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(5),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));
	}

	/**
	 * Given an shipping order with 3 entries:<br>
	 * DeclineEntry 1 : auto<br>
	 * DeclineEntry 2 : manual<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS<br>
	 * <p>
	 */
	@Test
	public void shouldAutoReallocate_MixManualAuto_ReSourcingFailed() throws InterruptedException
	{
		//Given
		LOG.info("Stock added in Montreal warehouse for the camera, and Toronto Warehouse for memoryCard and Lens");
		stockLevels.Camera(warehouses.Toronto(), CAMERA_QTY.intValue());
		stockLevels.MemoryCard(warehouses.Toronto(), MEMORY_CARD_QTY.intValue());
		stockLevels.Lens(warehouses.Toronto(), LENS_QTY.intValue());

		order = orders.CameraAndMemoryCardAndLens_Shipped(CAMERA_QTY, MEMORY_CARD_QTY, LENS_QTY);
		order.setDeliveryMode(deliveryMode);
		modelService.saveAll();

		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		Collection<ConsignmentModel> consignmentModels = order.getConsignments();
		LOG.info("Number of consignments: " + consignmentModels.size());
		assertEquals(1, consignmentModels.size());

		Collection<ConsignmentEntryModel> consignmentEntries = consignmentModels.stream()
				.flatMap(cons -> cons.getConsignmentEntries().stream()).collect(Collectors.toList());
		LOG.info("Number of consignment entries: " + consignmentEntries.size());

		LOG.info("Stock added in boston warehouse for the camera, memoryCard and Lens");
		stockLevels.Camera(warehouses.Boston(), CAMERA_QTY.intValue());
		stockLevels.MemoryCard(warehouses.Boston(), MEMORY_CARD_QTY.intValue());
		stockLevels.Lens(warehouses.Boston(), LENS_QTY.intValue());
		modelService.saveAll();

		ConsignmentEntryModel cameraConsEntryModel = consignmentEntries.stream()
				.filter(consEntry -> consEntry.getOrderEntry().getProduct().getCode().equals(CAMERA_CODE)).findFirst().get();
		ConsignmentEntryModel memoryCardConsEntryModel = consignmentEntries.stream()
				.filter(consEntry -> consEntry.getOrderEntry().getProduct().getCode().equals(MEMORY_CARD_CODE)).findFirst().get();
		ConsignmentEntryModel lensConsEntryModel = consignmentEntries.stream()
				.filter(consEntry -> consEntry.getOrderEntry().getProduct().getCode().equals(LENS_CODE)).findFirst().get();

		declineEntryInfoManual.put(cameraConsEntryModel, CAMERA_QTY);
		declineEntryInfo.put(memoryCardConsEntryModel, MEMORY_CARD_QTY);
		declineEntryInfo.put(lensConsEntryModel, LENS_QTY);

		DeclineEntries manualEntries = DeclineEntryBuilder.aDecline().build_Manual(declineEntryInfoManual, warehouses.Boston());
		DeclineEntries autoEntries = DeclineEntryBuilder.aDecline().build_Auto(declineEntryInfo);

		DeclineEntries manualAndAuto = new DeclineEntries();
		manualAndAuto.setEntries(Stream.concat(manualEntries.getEntries().stream(), autoEntries.getEntries().stream()).collect
				(Collectors.toList()));

		ConsignmentModel cons = consignmentModels.iterator().next();

		ConsignmentProcessModel consignmentProcess = cons.getConsignmentProcesses().stream()
				.filter(process -> process.getConsignment().equals(cons)).findAny().get();
		BusinessProcessParameterModel declineParam = new BusinessProcessParameterModel();
		declineParam.setName(DECLINE_ENTRIES);
		declineParam.setValue(DeclineEntryBuilder.aDecline().build_Auto(declineEntryInfo));
		declineParam.setValue(manualAndAuto);
		declineParam.setProcess(consignmentProcess);
		consignmentProcess.setContextParameters(Collections.singleton(declineParam));
		modelService.save(consignmentProcess);

		//when decline the order
		sourcingUtil.getConsignmentBusinessProcessService().triggerChoiceEvent(cons, CONSIGNMENT_ACTION_EVENT_NAME,
				REALLOCATE_CONSIGNMENT_CHOICE);
		sourcingUtil.waitForOrderStatus(orderProcessModel, order, OrderStatus.READY, timeOut);

		//Then
		assertEquals(3, order.getConsignments().size());
		assertTrue(order.getStatus().equals(OrderStatus.READY));
		modelService.remove(order);
	}


	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS without sufficient stock twice<br>
	 * <p>
	 */
	@Test
	public void shouldAutoDecline_SuccessReSourcedTwice() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 3);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		Collection<ConsignmentModel> consignmentResult = order.getConsignments();
		ConsignmentModel oldCons = consignmentResult.iterator().next();

		//when decline the order twice
		declineEntryInfo.put(oldCons.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.autoDeclineDefaultConsignment(oldCons, declineEntryInfo, orderProcessModel, DeclineReason.OUTOFSTOCK);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));
		modelService.refresh(order);

		// When decline second time
		Collection<ConsignmentModel> consignmentResult2 = order.getConsignments();
		ConsignmentModel newCons = consignmentResult2.stream().filter(cons -> !cons.equals(oldCons)).findFirst().get();
		LOG.info("Stock added in Montreal warehouse for the camera");
		//this will only update the stock level table, use 11 to balance ATP to 5
		stockService.updateActualStockLevel(products.Camera(), warehouses.Montreal(), 11, "");
		declineEntryInfo_2.put(newCons.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.autoDeclineDefaultConsignment(newCons, declineEntryInfo_2, orderProcessModel, DeclineReason.OUTOFSTOCK);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the ATP
		assertEquals(Long.valueOf(2),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(2),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify the new consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 3);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("CANCELLED")));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_BOSTON, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * partially decline same consignment twice<br>
	 * <p>
	 */
	@Test
	public void shouldAutoDecline_PartiallyDeclineTwice() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		Collection<ConsignmentModel> consignmentResult = order.getConsignments();
		ConsignmentModel oldCons = consignmentResult.iterator().next();

		//when decline the order twice
		declineEntryInfo.put(oldCons.getConsignmentEntries().stream().findFirst().get(), Long.valueOf(1L));
		declineUtil.autoDeclineDefaultConsignment(oldCons, declineEntryInfo, orderProcessModel, DeclineReason.OUTOFSTOCK);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		// When decline second time
		Collection<ConsignmentModel> consignmentResult2 = order.getConsignments();
		ConsignmentModel newCons = consignmentResult2.stream().findFirst().get();
		LOG.info("Stock added in Montreal warehouse for the camera");
		//this will only update the stock level table, use 11 to balance ATP to 5
		stockService.updateActualStockLevel(products.Camera(), warehouses.Montreal(), 11, "");
		declineEntryInfo_2.put(newCons.getConsignmentEntries().stream().findFirst().get(), Long.valueOf(1L));

		declineUtil.autoDeclineDefaultConsignment(newCons, declineEntryInfo_2, orderProcessModel, DeclineReason.OUTOFSTOCK);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the ATP
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify the consignment
		modelService.refresh(order);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(2L), Long.valueOf(3L), Long.valueOf(1L)));

		//confirm all the consignment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().equals(OrderStatus.SUSPENDED));
		sourcingUtil.refreshOrder(order);
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated().equals(Long.valueOf(2L)));
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS<br>
	 * <p>
	 */
	@Test
	public void shouldAutoDecline2Entries_PartiallySuccessReSourced() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.Camera(warehouses.Boston(), 4);
		stockLevels.MemoryCard(warehouses.Montreal(), 5);
		stockLevels.MemoryCard(warehouses.Boston(), 4);

		// When create consignment
		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		Collection<ConsignmentModel> consignmentResult = order.getConsignments();
		ConsignmentModel cons = consignmentResult.iterator().next();

		//when decline the order
		declineEntryInfo.put(cons.getConsignmentEntries().stream().collect(Collectors.toList()).get(0), Long.valueOf(2L));
		declineEntryInfo.put(cons.getConsignmentEntries().stream().collect(Collectors.toList()).get(1), Long.valueOf(2L));

		declineUtil.autoDeclineDefaultConsignment(cons, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));
		modelService.refresh(order);

		//then verify the ATP
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(2),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(4),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(5),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(7),
				commerceStockService.getStockLevelForProductAndBaseStore(products.MemoryCard(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(2),
				commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Boston()));

		//then verify new consignment
		assertTrue(order.getConsignments().size() == 2);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(verifyOrderAndConsignment
				.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL, Long.valueOf(2L), CAMERA_QTY, Long.valueOf(1L),
						Long.valueOf(2L), MEMORY_CARD_QTY, Long.valueOf(0L)).booleanValue());
		assertTrue(verifyOrderAndConsignment
				.verifyConsignment_Camera_MemoryCard(order, CODE_BOSTON, Long.valueOf(0L), Long.valueOf(2L), Long.valueOf(2L),
						Long.valueOf(0L), Long.valueOf(2L), Long.valueOf(2L)).booleanValue());
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Decline should SUCCESS, and confirm order<br>
	 * <p>
	 */
	@Test
	public void shouldAutoDecline1Entry_MultiEntries_SuccessReSourced() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.Camera(warehouses.Boston(), 4);
		stockLevels.MemoryCard(warehouses.Montreal(), 5);
		stockLevels.MemoryCard(warehouses.Boston(), 4);

		// When create consignment
		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		ConsignmentModel consignmentResult_Montreal = order.getConsignments().stream()
				.filter(e -> e.getWarehouse().getCode().equals(CODE_MONTREAL)).findFirst().get();

		Collection<ConsignmentModel> consignmentResult = order.getConsignments();
		ConsignmentModel cons = consignmentResult.iterator().next();

		//when decline the order
		declineEntryInfo
				.put(cons.getConsignmentEntries().stream().filter(e -> e.getQuantity().equals(Long.valueOf(3L))).findFirst().get(),
						CAMERA_QTY);


		declineUtil.autoDeclineDefaultConsignment(cons, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		sourcingUtil.waitUntilConsignmentProcessIsNotRunning(orderProcessModel, consignmentResult_Montreal, timeOut);
		sourcingUtil.waitUntilProcessIsNotRunning(orderProcessModel, timeOut);
		modelService.refresh(order);
		ConsignmentModel consignmentResult_Boston = order.getConsignments().stream()
				.filter(e -> e.getWarehouse().getCode().equals(CODE_BOSTON)).findFirst().get();
		sourcingUtil.waitUntilConsignmentProcessIsNotRunning(orderProcessModel, consignmentResult_Boston, timeOut);

		//then verify the ATP
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(1),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
		assertEquals(Long.valueOf(5),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(3),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(7),
				commerceStockService.getStockLevelForProductAndBaseStore(products.MemoryCard(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(4),
				commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Boston()));

		//then verify new consignment
		assertTrue(order.getConsignments().size() == 2);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(verifyOrderAndConsignment
				.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL, Long.valueOf(3L), CAMERA_QTY, Long.valueOf(0L),
						Long.valueOf(0L), MEMORY_CARD_QTY, MEMORY_CARD_QTY).booleanValue());
		assertTrue(verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), Long.valueOf(3L), Long.valueOf(3L)).booleanValue());

		//confirm all the consignment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Decline should Fail<br>
	 * <p>
	 */
	@Test
	public void shouldAutoDecline1Entry_MultiEntries_FailReSourced() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.MemoryCard(warehouses.Montreal(), 5);

		// When create consignment
		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		Collection<ConsignmentModel> consignmentResult = order.getConsignments();
		ConsignmentModel cons = consignmentResult.iterator().next();

		//when decline the order
		declineEntryInfo
				.put(cons.getConsignmentEntries().stream().filter(e -> e.getQuantity().equals(Long.valueOf(3L))).findFirst().get(),
						CAMERA_QTY);

		declineUtil.autoDeclineDefaultConsignment(cons, declineEntryInfo, orderProcessModel, DeclineReason.TOOBUSY);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));
		modelService.refresh(order);

		//then verify the ATP
		assertEquals(Long.valueOf(5),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(5),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(3),
				commerceStockService
						.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.MemoryCard(), baseStores.NorthAmerica()));

		//then verify new consignment
		assertTrue(order.getConsignments().size() == 1);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(verifyOrderAndConsignment
				.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL, Long.valueOf(3L), CAMERA_QTY, Long.valueOf(0L),
						Long.valueOf(0L), MEMORY_CARD_QTY, MEMORY_CARD_QTY).booleanValue());
		assertTrue(order.getStatus().equals(OrderStatus.SUSPENDED));
		modelService.refresh(order.getConsignments().iterator().next().getConsignmentEntries().iterator().next());
		modelService
				.refresh(order.getEntries().stream().filter(e -> e.getProduct().getCode().equals(CAMERA_CODE)).findFirst().get());
		assertTrue(
				((OrderEntryModel) order.getEntries().stream().filter(e -> e.getProduct().getCode().equals(CAMERA_CODE)).findFirst()
						.get())
						.getQuantityUnallocated().equals(Long.valueOf(3L)));
	}
	@Test
	public void shouldAutoReallocate_SingleEntry_FailReSourced__Damaged_ResetInventory() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 1);
		order = sourcingUtil.createCameraShippedOrder();
		OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		// When create consignment
		ConsignmentModel consignmentResult = order.getConsignments().iterator().next();

		//when decline the order
		declineEntryInfo.put(consignmentResult.getConsignmentEntries().stream().findFirst().get(), CAMERA_QTY);
		declineUtil.autoDeclineDefaultConsignment(consignmentResult, declineEntryInfo, orderProcessModel, DeclineReason.DAMAGED);
		assertTrue(ProcessState.WAITING.equals(orderProcessModel.getProcessState()));

		//then verify the new consignment
		modelService.refresh(order);
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated().equals(Long.valueOf(2L)));
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("CANCELLED")));
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L)));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment
				.verifyConsignment_Camera(order, CODE_BOSTON, Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L)));
		sourcingUtil.waitForOrderStatus(orderProcessModel, order, OrderStatus.SUSPENDED, timeOut);

		//then verify the ATP
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));

		assertEquals(Long.valueOf(0), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));

	}
}
