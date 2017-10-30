/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.integration.util;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * this class is mainly for order sourcing
 */
@Component
public class SourcingUtil extends ProcessUtil
{

	private static final Logger LOG = Logger.getLogger(SourcingUtil.class);

	public void assertSourcingResultContents(final SourcingResults results, final WarehouseModel expectedWarehouse,
			final ProductModel product, final Long expectedAllocation)
	{
		final Optional<SourcingResult> sourcingResult = results.getResults().stream()
				.filter(result -> result.getWarehouse().getCode().equals(expectedWarehouse.getCode())).findFirst();

		assertTrue("No sourcing result with warehouse " + expectedWarehouse.getCode(), sourcingResult.isPresent());
		assertEquals(expectedWarehouse.getCode(), sourcingResult.get().getWarehouse().getCode());
		assertEquals(expectedAllocation, getAllocationForProduct(sourcingResult.get(), product));
	}

	public Long getAllocationForProduct(final SourcingResult result, final ProductModel product)
	{
		return result.getAllocation().get(result.getAllocation().keySet().stream() //
				.filter(entry -> entry.getProduct().getCode().equals(product.getCode())) //
				.findFirst().get());
	}

	/**
	 * wait for order status
	 *
	 * @param orderProcess
	 * @param order
	 * @param orderStatus
	 * @param timeOut
	 * @throws InterruptedException
	 */
	public void waitForOrderStatus(final OrderProcessModel orderProcess, final OrderModel order, final OrderStatus orderStatus,
			final int timeOut) throws InterruptedException
	{
		int timeCount = 0;
		do
		{
			Thread.sleep(1000);
			getModelService().refresh(order);
		}
		while (!orderStatus.equals(orderProcess.getOrder().getStatus()) && timeCount++ < timeOut);
		getModelService().refresh(order);
	}

	public OrderProcessModel runOrderProcessForOrderBasedPriority(final OrderModel orderModel, final OrderStatus status)
			throws InterruptedException
	{
		LOG.info("Sourcing from allocation sourcing factor only");
		setSourcingFactors(orderModel.getStore(), 0, 0, 100);
		warehouses.Montreal().setPriority(Integer.valueOf(1));
		warehouses.Boston().setPriority(Integer.valueOf(50));
		warehouses.Toronto().setPriority(Integer.valueOf(60));
		return runDefaultOrderProcessForOrder(orderModel, status);
	}

	/**
	 * run default order process
	 *
	 * @param orderModel
	 * @param status
	 * @return
	 * @throws InterruptedException
	 */
	public OrderProcessModel runDefaultOrderProcessForOrder(final OrderModel orderModel, final OrderStatus status)
			throws InterruptedException
	{
		setCalculatedStatus(orderModel);
		modelService.saveAll();

		OrderProcessModel orderProcessModel = getBusinessProcessService().getProcess(
				ORDER_PROCESS_DEFINITION_NAME + "-" + orderModel.getCode());
		if (orderProcessModel != null)
		{
			getModelService().remove(orderProcessModel);
		}
		orderProcessModel = getBusinessProcessService().<OrderProcessModel>createProcess(
				ORDER_PROCESS_DEFINITION_NAME + "-" + orderModel.getCode(), ORDER_PROCESS_DEFINITION_NAME);
		orderProcessModel.setOrder(orderModel);

		getModelService().save(orderProcessModel);
		assertProcessState(orderProcessModel, ProcessState.CREATED);

		businessProcessService.startProcess(orderProcessModel);
		LOG.info("Order process" + ORDER_TEST_PROCESS + " started");
		waitForOrderStatus(orderProcessModel, orderModel, status, timeOut);
		assertEquals(status, orderProcessModel.getOrder().getStatus());
		LOG.info("Order process is in state " + orderProcessModel.getOrder().getStatus());
		return orderProcessModel;
	}

	/**
	 * confirm default consignment
	 *
	 * @param orderProcessModel
	 * @param consignment
	 */
	public void confirmDefaultConsignment(final OrderProcessModel orderProcessModel, final ConsignmentModel consignment)
	{
		try
		{
			waitUntilConsignmentProcessIsNotRunning(orderProcessModel, consignment, timeOut);
			getConsignmentBusinessProcessService().triggerChoiceEvent(consignment, CONSIGNMENT_ACTION_EVENT_NAME,
					SHIP_CONSIGNMENT_CHOICE);
			waitUntilConsignmentProcessIsNotRunning(orderProcessModel, consignment, timeOut);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
	{
		return consignmentBusinessProcessService;
	}
}


