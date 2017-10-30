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

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.util.DeclineEntryBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;


/**
 * this class is mainly to decline orders
 */
@Component
public class DeclineUtil extends ProcessUtil
{
	protected static final String DECLINE_ENTRIES = "declineEntries";
	protected static final String REALLOCATE_CONSIGNMENT_CHOICE = "reallocateConsignment";

	/**
	 * automatically decline consignment
	 *
	 * @param consignmentModels
	 * @param declineEntryInfo
	 * @param orderProcessModel
	 * @param declineReason
	 * @throws InterruptedException
	 */
	public void autoDeclineDefaultConsignment(final ConsignmentModel consignmentModels,
			final Map<ConsignmentEntryModel, Long> declineEntryInfo, final OrderProcessModel orderProcessModel,
			final DeclineReason declineReason) throws InterruptedException
	{
		//when decline the order
		ConsignmentModel cons = consignmentModels;

		String consignmentProcessCode = cons.getCode() + "_ordermanagement";
		ConsignmentProcessModel consignmentProcess = cons.getConsignmentProcesses().stream()
				.filter(cp -> cp.getCode().equals(consignmentProcessCode)).findFirst().get();

		waitUntilConsignmentProcessIsNotRunning(orderProcessModel, cons, timeOut);
		BusinessProcessParameterModel declineParam = new BusinessProcessParameterModel();
		declineParam.setName(DECLINE_ENTRIES);
		declineParam.setValue(DeclineEntryBuilder.aDecline().build_Auto(declineEntryInfo, declineReason));
		declineParam.setProcess(consignmentProcess);
		consignmentProcess.setContextParameters(Collections.singleton(declineParam));
		getModelService().save(consignmentProcess);

		//when decline the order
		getConsignmentBusinessProcessService().triggerChoiceEvent(cons, CONSIGNMENT_ACTION_EVENT_NAME,
				REALLOCATE_CONSIGNMENT_CHOICE);
		waitUntilConsignmentProcessIsNotRunning(orderProcessModel, cons, timeOut);
	}

	/**
	 * manually decline consignment
	 *
	 * @param consignmentModels
	 * @param declineEntryInfo
	 * @param orderProcessModel
	 * @param warehouseModel
	 * @param declineReason
	 * @throws InterruptedException
	 */
	public void manualDeclineDefaultConsignment(final ConsignmentModel consignmentModels,
			final Map<ConsignmentEntryModel, Long> declineEntryInfo, final OrderProcessModel orderProcessModel,
			final WarehouseModel warehouseModel, final DeclineReason declineReason) throws InterruptedException
	{
		//when decline the order
		ConsignmentModel cons = consignmentModels;

		String consignmentProcessCode = cons.getCode() + "_ordermanagement";
		ConsignmentProcessModel consignmentProcess = cons.getConsignmentProcesses().stream()
				.filter(cp -> cp.getCode().equals(consignmentProcessCode)).findFirst().get();

		waitUntilConsignmentProcessIsNotRunning(orderProcessModel, cons, timeOut);
		BusinessProcessParameterModel declineParam = new BusinessProcessParameterModel();
		declineParam.setName(DECLINE_ENTRIES);
		declineParam.setValue(DeclineEntryBuilder.aDecline().build_Manual(declineEntryInfo, warehouseModel, declineReason));
		declineParam.setProcess(consignmentProcess);
		consignmentProcess.setContextParameters(Collections.singleton(declineParam));
		getModelService().save(consignmentProcess);

		//when decline the order
		getConsignmentBusinessProcessService().triggerChoiceEvent(cons, CONSIGNMENT_ACTION_EVENT_NAME,
				REALLOCATE_CONSIGNMENT_CHOICE);
		waitUntilConsignmentProcessIsNotRunning(orderProcessModel, cons, timeOut);
	}

	protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
	{
		return consignmentBusinessProcessService;
	}

	public void setConsignmentBusinessProcessService(
			final WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
	{
		this.consignmentBusinessProcessService = consignmentBusinessProcessService;
	}
}
