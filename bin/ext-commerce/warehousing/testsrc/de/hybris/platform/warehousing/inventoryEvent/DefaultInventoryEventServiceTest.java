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

package de.hybris.platform.warehousing.inventoryEvent;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.warehousing.stock.strategies.StockLevelSelectionStrategy;
import de.hybris.platform.warehousing.inventoryevent.service.impl.DefaultInventoryEventService;
import de.hybris.platform.warehousing.model.IncreaseEventModel;
import de.hybris.platform.warehousing.model.ShrinkageEventModel;
import de.hybris.platform.warehousing.model.WastageEventModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultInventoryEventServiceTest
{
	@Mock
	private IncreaseEventModel increaseEventModel;
	@Mock
	private ShrinkageEventModel shrinkageEventModel;
	@Mock
	private WastageEventModel wastageEventModel;
	@Mock
	private ConsignmentEntryModel consignmentEntryModel;
	@Mock
	private OrderEntryModel orderEntryModel;
	@Mock
	private ProductModel productModel;
	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private WarehouseModel warehouseModel;
	@Mock
	private Collection<StockLevelModel> stockLevelModels;
	@Mock
	private StockLevelModel stockLevelModel;
	@InjectMocks
	private DefaultInventoryEventService inventoryEventService = new DefaultInventoryEventService();
	@Mock
	private ModelService modelService;
	@Mock
	private TimeService timeService;
	@Mock
	private StockService stockService;
	@Mock
	private StockLevelSelectionStrategy stockLevelSelectionStrategy;

	private long quantity = 5;
	private String productCode = "product1";

	@Before
	public void setUp()
	{
		inventoryEventService.setModelService(modelService);

		when(shrinkageEventModel.getConsignmentEntry()).thenReturn(consignmentEntryModel);
		when(shrinkageEventModel.getQuantity()).thenReturn(quantity);

		when(wastageEventModel.getConsignmentEntry()).thenReturn(consignmentEntryModel);
		when(wastageEventModel.getQuantity()).thenReturn(quantity);

		when(consignmentEntryModel.getOrderEntry()).thenReturn(orderEntryModel);
		when(orderEntryModel.getProduct()).thenReturn(productModel);
		when(productModel.getCode()).thenReturn(productCode);
		when(consignmentEntryModel.getConsignment()).thenReturn(consignmentModel);
		when(consignmentModel.getWarehouse()).thenReturn(warehouseModel);

		when(stockService.getStockLevels(any(), any())).thenReturn(stockLevelModels);

		when(stockLevelSelectionStrategy.getStockLevelForAllocation(stockLevelModels)).thenReturn(stockLevelModel);
	}

	@Test
	public void shouldCreateIncreaseEvent()
	{
		when(modelService.create(IncreaseEventModel.class)).thenReturn(increaseEventModel);
		when(timeService.getCurrentTime()).thenReturn(new Date());
		inventoryEventService.createIncreaseEvent(increaseEventModel);
		verify(modelService, times(1)).save(increaseEventModel);
	}

	@Test
	public void shouldCreateShrinkageEvent()
	{
		when(modelService.create(ShrinkageEventModel.class)).thenReturn(shrinkageEventModel);
		when(timeService.getCurrentTime()).thenReturn(new Date());
		ShrinkageEventModel resultEvent = inventoryEventService.createShrinkageEvent(shrinkageEventModel);
		verify(modelService, times(1)).save(shrinkageEventModel);
		assertEquals(quantity, resultEvent.getQuantity());
	}

	@Test
	public void shouldCreateWastageEvent()
	{
		when(modelService.create(WastageEventModel.class)).thenReturn(wastageEventModel);
		when(timeService.getCurrentTime()).thenReturn(new Date());
		WastageEventModel resultEvent = inventoryEventService.createWastageEvent(wastageEventModel);
		verify(modelService, times(1)).save(wastageEventModel);
		assertEquals(quantity, resultEvent.getQuantity());
	}
}
