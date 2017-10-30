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
package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.allocation.decline.action.DeclineActionStrategy;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Declines the consignment.
 */
public class ReAllocateConsignmentAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReAllocateConsignmentAction.class);

	protected static final String DECLINE_ENTRIES = "declineEntries";
	protected static final String RE_SOURCE_CHOICE = "reSource";
	protected static final String IS_CONSIGNMENT_AUTO_DECLINED = "isConsignmentAutoDecline";

	private AllocationService allocationService;
	private WarehousingBusinessProcessService<AbstractOrderModel> orderBusinessProcessService;
	private Map<DeclineReason, DeclineActionStrategy> declineActionsMap;

	@Override
	public void executeAction(final ConsignmentProcessModel consignmentProcessModel)
	{
		Preconditions.checkArgument(Objects.nonNull(consignmentProcessModel.getConsignment()),
				"No consignment selected to Decline");
		Preconditions.checkArgument(Objects.nonNull(consignmentProcessModel.getContextParameters()),"Nothing to Decline");

		Collection<BusinessProcessParameterModel> contextParams = new ArrayList<>();
		consignmentProcessModel.getContextParameters().forEach(param -> contextParams.add(param));

		final Optional<BusinessProcessParameterModel> declineEntriesParam = contextParams.stream().filter(param -> param.getName().equals(DECLINE_ENTRIES)).findFirst();

		Preconditions.checkArgument(declineEntriesParam.isPresent(),"Nothing to Decline");

		final DeclineEntries declinedEntries = (DeclineEntries) declineEntriesParam.get().getValue();
		Preconditions.checkArgument(Objects.nonNull(declinedEntries) || CollectionUtils.isNotEmpty(declinedEntries.getEntries()),"Nothing to Decline");


		final AbstractOrderModel order = declinedEntries.getEntries().stream().findFirst().get().getConsignmentEntry().getConsignment().getOrder();
		Boolean isAutoDecline = Boolean.FALSE;

		//Extracting manual decline entries and performing manual reallocation
		Collection<DeclineEntry> manualEntries = declinedEntries.getEntries().stream().filter(declineEntry -> declineEntry.getReallocationWarehouse() != null).collect(
				Collectors.toList());
		if(manualEntries.size() > 0)
		{
			LOGGER.debug("Performing Manual Reallocation for "+manualEntries.size()+" decline entries");
			DeclineEntries manualDeclineEntries = new DeclineEntries();
			manualDeclineEntries.setEntries(manualEntries);
			performManualDecline(manualDeclineEntries);
		}

		//Extracting auto decline entries and performing auto reallocation
		Collection<DeclineEntry> autoEntries = declinedEntries.getEntries().stream().filter(declineEntry -> declineEntry.getReallocationWarehouse() == null).collect(
				Collectors.toList());
		if(autoEntries.size() > 0)
		{
			LOGGER.debug("Performing Auto Reallocation for "+autoEntries.size()+" decline entries");
			DeclineEntries autoDeclineEntries = new DeclineEntries();
			autoDeclineEntries.setEntries(autoEntries);
			performAutoDecline(order, autoDeclineEntries);
			isAutoDecline = Boolean.TRUE;
		}
		//Calling corresponding declineActionStrategy to be executed, based on selected reasons for decline
		executeDeclineActions(declinedEntries);

		//Updating the consignment process context
		updateConsignmentContextParameters(consignmentProcessModel, contextParams, declineEntriesParam.get(), isAutoDecline);

	}

	/**
	 * Removes the {#link DeclineEntries} from the consignment process context and add flag to indicate if autoDecline was done for the consignment
	 * @param consignmentProcessModel - process for the consignment to be declined
	 * @param contextParams - context parameters for the consignment to be declined
	 * @param declineEntriesParam - declined entries param in the context parameters for the consignment to be declined
	 * @param isAutoDecline - flag to indicate if auto decline was performed on the consignment
	 */
	protected void updateConsignmentContextParameters(ConsignmentProcessModel consignmentProcessModel,
			Collection<BusinessProcessParameterModel> contextParams, BusinessProcessParameterModel declineEntriesParam,
			Boolean isAutoDecline)
	{
		LOGGER.debug("Cleaning up the declinedEntries param from context parameters of the consignmentProcess");
		contextParams.remove(declineEntriesParam);
		getModelService().remove(declineEntriesParam);

		BusinessProcessParameterModel autoDeclineParam = new BusinessProcessParameterModel();
		autoDeclineParam.setName(IS_CONSIGNMENT_AUTO_DECLINED);
		autoDeclineParam.setValue(isAutoDecline);
		autoDeclineParam.setProcess(consignmentProcessModel);
		contextParams.add(autoDeclineParam);
		consignmentProcessModel.setContextParameters(contextParams);
		getModelService().save(consignmentProcessModel);
	}

	/**
	 * Performs the auto decline for the given {@link DeclineEntries} and trigger the sourcing for the associated {@link AbstractOrderModel}
	 *
	 * @param autoDeclineEntries - entries to be declined
	 * @param order - associated {@link AbstractOrderModel} with the {@link DeclineEntries},for which sourcing needs to be triggered
	 */
	protected void performAutoDecline(AbstractOrderModel order, DeclineEntries autoDeclineEntries)
	{
		getAllocationService().autoReallocate(autoDeclineEntries);
		getOrderBusinessProcessService()
				.triggerChoiceEvent(order, YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME, RE_SOURCE_CHOICE);
	}

	/**
	 * Performs the manual reallocation for the given {@link DeclineEntries} and start the process for newly created consignments
	 *
	 * @param manualDeclineEntries - entries to be reallocated to the selected warehouse
	 */
	protected void performManualDecline(DeclineEntries manualDeclineEntries)
	{
		final Collection<ConsignmentModel> newConsignments = getAllocationService().manualReallocate(manualDeclineEntries);


		final OrderModel order = (OrderModel) newConsignments.iterator().next().getOrder();

		final String orderProcessCode = getOrderBusinessProcessService().getProcessCode(order);
		final OrderProcessModel orderProcess = getOrderBusinessProcessService().getProcess(
				orderProcessCode);
		newConsignments.stream().forEach(consignment -> {
			final ConsignmentProcessModel subProcess = getOrderBusinessProcessService().<ConsignmentProcessModel> createProcess(
					consignment.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.CONSIGNMENT_SUBPROCESS_NAME);
			subProcess.setParentProcess(orderProcess);
			subProcess.setConsignment(consignment);
			getModelService().save(subProcess);

			LOGGER.info("Start Consignment sub-process: '" + subProcess.getCode() + "'");
			getOrderBusinessProcessService().startProcess(subProcess);
		});
	}

	/**
	 * Executes a {@link de.hybris.platform.warehousing.allocation.decline.action.DeclineActionStrategy} for each declined entry according to the specified reason.
	 *
	 * @param declinedEntries
	 *           the declined entries
	 */
	protected void executeDeclineActions(final DeclineEntries declinedEntries)
	{
		declinedEntries.getEntries().stream().forEach(entry -> {
			getDeclineActionsMap().get(entry.getReason()).execute(entry);
		});
	}
	protected AllocationService getAllocationService()
	{
		return allocationService;
	}

	@Required
	public void setAllocationService(AllocationService allocationService)
	{
		this.allocationService = allocationService;
	}

	protected WarehousingBusinessProcessService<AbstractOrderModel> getOrderBusinessProcessService()
	{
		return orderBusinessProcessService;
	}

	@Required
	public void setOrderBusinessProcessService(WarehousingBusinessProcessService<AbstractOrderModel> orderBusinessProcessService)
	{
		this.orderBusinessProcessService = orderBusinessProcessService;
	}

	protected Map<DeclineReason, DeclineActionStrategy> getDeclineActionsMap()
	{
		return declineActionsMap;
	}

	@Required
	public void setDeclineActionsMap(final Map<DeclineReason, DeclineActionStrategy> declineActionsMap)
	{
		this.declineActionsMap = declineActionsMap;
	}
}
