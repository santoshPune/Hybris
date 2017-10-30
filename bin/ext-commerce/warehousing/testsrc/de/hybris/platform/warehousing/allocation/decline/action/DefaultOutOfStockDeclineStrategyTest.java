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
package de.hybris.platform.warehousing.allocation.decline.action;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.allocation.decline.action.impl.DefaultOutOfStockDeclineStrategy;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import de.hybris.platform.warehousing.stock.services.impl.DefaultWarehouseStockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOutOfStockDeclineStrategyTest
{

	@Mock
	private ConsignmentEntryModel consignmentEntryModel;
	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private OrderEntryModel orderEntryModel;
	@Mock
	private ProductModel productModel;
	@Mock
	private WarehouseModel warehouseModel;
	@Mock
	private StockLevelModel stockLevelModel;
	@Mock
	private DeclineEntry declineEntry;
	@Mock
	private InventoryEventService inventoryEventService;
	@Mock
	private DefaultWarehouseStockService warehouseStockService;
	@Mock
	private AtpFormulaModel atpFormulaModel;
	@Mock
	private OrderModel orderModel;
	@Mock
	private BaseStoreModel baseStoreModel;


	@InjectMocks
	private DefaultOutOfStockDeclineStrategy defaultOutOfStockStrategy;


	@Test
	public void shouldExecute()
	{
		//given
		when(declineEntry.getConsignmentEntry()).thenReturn(consignmentEntryModel);
		when(consignmentEntryModel.getOrderEntry()).thenReturn(orderEntryModel);
		when(orderEntryModel.getProduct()).thenReturn(productModel);
		when(orderEntryModel.getOrder()).thenReturn(orderModel);
		when(consignmentEntryModel.getConsignment()).thenReturn(consignmentModel);
		when(consignmentModel.getWarehouse()).thenReturn(warehouseModel);
		when(warehouseStockService.getStockLevelForProductCodeAndWarehouse(productModel.getCode(), warehouseModel)).thenReturn(1L);
		when(orderModel.getStore()).thenReturn(baseStoreModel);
		when(baseStoreModel.getDefaultAtpFormula()).thenReturn(atpFormulaModel);

		// when
		defaultOutOfStockStrategy.execute(declineEntry);

		// Then
		verify(inventoryEventService).createShrinkageEvent(any());


	}
}
