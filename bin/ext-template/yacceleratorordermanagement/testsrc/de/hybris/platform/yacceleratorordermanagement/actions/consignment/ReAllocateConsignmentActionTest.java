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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.allocation.decline.action.DeclineActionStrategy;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ReAllocateConsignmentActionTest
{

	private OrderModel orderModel;
	private ConsignmentProcessModel consignmentProcessModel;
	private OrderProcessModel orderProcessModel;
	private ConsignmentModel consignmentModel;
	private ConsignmentEntryModel consignmentEntryModel;
	private Map<DeclineReason, DeclineActionStrategy> declineActionsMap;
	private DeclineEntries declineEntries;

	private static final String ORDER_PROCESS_CODE="orderProcessCode";

	@InjectMocks
	ReAllocateConsignmentAction action = new ReAllocateConsignmentAction();

	@Mock
	private ModelService modelService;
	@Mock
	private AllocationService allocationService;
	@Mock
	private WarehousingBusinessProcessService<AbstractOrderModel> orderBusinessProcessService;
	@Mock
	private DeclineActionStrategy declineActionStrategy;

	@Before
	public void setup()
	{
		consignmentModel = new ConsignmentModel();
		orderModel = new OrderModel();
		consignmentModel.setOrder(orderModel);
		consignmentEntryModel = new ConsignmentEntryModel();
		consignmentEntryModel.setConsignment(consignmentModel);

		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setCode(ORDER_PROCESS_CODE);
		orderProcessModel.setOrder(orderModel);

		consignmentProcessModel = new ConsignmentProcessModel();
		consignmentProcessModel.setConsignment(consignmentModel);

		declineActionsMap = spy(new HashMap<>());
		action.setDeclineActionsMap(declineActionsMap);
		when(declineActionsMap.get(any(DeclineReason.class))).thenReturn(declineActionStrategy);

		declineEntries = new DeclineEntries();
		BusinessProcessParameterModel param = new BusinessProcessParameterModel();
		param.setName(action.DECLINE_ENTRIES);
		param.setProcess(consignmentProcessModel);
		param.setValue(declineEntries);

		Collection<BusinessProcessParameterModel> contextParameters= spy(new ArrayList<>());
		contextParameters.add(param);
		consignmentProcessModel.setContextParameters(contextParameters);

		action.setOrderBusinessProcessService(orderBusinessProcessService);
		action.setAllocationService(allocationService);

		when(orderBusinessProcessService.getProcess(ORDER_PROCESS_CODE)).thenReturn(orderProcessModel);
		doNothing().when(declineActionStrategy).execute(any());
		doNothing().when(modelService).save(any());
		doNothing().when(orderBusinessProcessService).triggerChoiceEvent(orderModel, YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME,action.RE_SOURCE_CHOICE);

	}

	@Test
	public void shouldPerformAutoReallocation() throws Exception
	{
		//Given
		DeclineEntry autoEntry = spy(new DeclineEntry());
		autoEntry.setConsignmentEntry(consignmentEntryModel);
		declineEntries.setEntries(Arrays.asList(autoEntry));

		//When
		action.executeAction(consignmentProcessModel);

		//Then
		verify(allocationService).autoReallocate(any(DeclineEntries.class));
		verify(allocationService,never()).manualReallocate(any(DeclineEntries.class));
		verify(orderBusinessProcessService).triggerChoiceEvent(orderModel,YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME,action.RE_SOURCE_CHOICE);
	}

	@Test
	public void shouldPerformManualReallocation() throws Exception
	{
		//Given
		WarehouseModel warehouse = new WarehouseModel();
		DeclineEntry manualEntry = spy(new DeclineEntry());
		manualEntry.setConsignmentEntry(consignmentEntryModel);
		manualEntry.setReallocationWarehouse(warehouse);
		declineEntries.setEntries(Arrays.asList(manualEntry));

		ConsignmentModel newConsignment = new ConsignmentModel();
		newConsignment.setOrder(orderModel);

		ConsignmentProcessModel newConsProcess = new ConsignmentProcessModel();
		newConsProcess.setConsignment(newConsignment);

		when(orderBusinessProcessService.createProcess(anyString(),eq(YAcceleratorOrderManagementConstants.CONSIGNMENT_SUBPROCESS_NAME))).thenReturn(newConsProcess);
		when(allocationService.manualReallocate(any(DeclineEntries.class))).thenReturn(Collections.singleton(newConsignment));

		//When
		action.executeAction(consignmentProcessModel);

		//Then
		verify(allocationService).manualReallocate(any(DeclineEntries.class));
		verify(allocationService,never()).autoReallocate(any(DeclineEntries.class));
		verify(orderBusinessProcessService,never()).triggerChoiceEvent(orderModel,YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME,action.RE_SOURCE_CHOICE);
	}

	@Test
	public void shouldPerformManualAndAutoReallocation() throws Exception
	{
		//Given
		WarehouseModel warehouse = new WarehouseModel();
		DeclineEntry manualEntry = spy(new DeclineEntry());
		manualEntry.setConsignmentEntry(consignmentEntryModel);
		manualEntry.setReallocationWarehouse(warehouse);

		DeclineEntry autoEntry = spy(new DeclineEntry());
		autoEntry.setConsignmentEntry(consignmentEntryModel);

		declineEntries.setEntries(Arrays.asList(manualEntry,autoEntry));

		ConsignmentModel newConsignment = new ConsignmentModel();
		newConsignment.setOrder(orderModel);
		ConsignmentProcessModel newConsProcess = new ConsignmentProcessModel();
		newConsProcess.setConsignment(newConsignment);

		when(orderBusinessProcessService.createProcess(anyString(),eq(YAcceleratorOrderManagementConstants.CONSIGNMENT_SUBPROCESS_NAME))).thenReturn(newConsProcess);
		when(allocationService.manualReallocate(any(DeclineEntries.class))).thenReturn(Collections.singleton(newConsignment));

		//When
		action.executeAction(consignmentProcessModel);

		//Then
		verify(allocationService).manualReallocate(any(DeclineEntries.class));
		verify(allocationService).autoReallocate(any(DeclineEntries.class));
		verify(orderBusinessProcessService).triggerChoiceEvent(orderModel,YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME,action.RE_SOURCE_CHOICE);
	}

}
