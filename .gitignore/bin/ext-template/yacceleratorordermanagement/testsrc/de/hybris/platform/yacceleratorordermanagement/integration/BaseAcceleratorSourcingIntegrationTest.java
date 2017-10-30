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

import com.google.common.collect.Lists;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.warehousing.stock.services.WarehouseStockService;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.SourcingConfigs;
import de.hybris.platform.warehousing.util.models.RestockConfigs;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;
import de.hybris.platform.yacceleratorordermanagement.integration.util.CancellationUtil;
import de.hybris.platform.yacceleratorordermanagement.integration.util.DeclineUtil;
import de.hybris.platform.yacceleratorordermanagement.integration.util.ReturnUtil;
import de.hybris.platform.yacceleratorordermanagement.integration.util.SourcingUtil;
import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import javax.annotation.Resource;


@Ignore
public class BaseAcceleratorSourcingIntegrationTest extends BaseAcceleratorIntegrationTest
{
	protected static final String CODE_MEMORY_CARD = "memorycard";
	protected static final String CODE_MONTREAL = "montreal";
	protected static final String CODE_TORONTO = "toronto";
	protected static final String CODE_BOSTON = "boston";
	protected static final Long CAMERA_QTY = Long.valueOf(3L);
	protected static final Long MEMORY_CARD_QTY = Long.valueOf(2L);
	protected static final Long LENS_QTY = Long.valueOf(4L);
	protected static final String CAMERA_CODE = "camera";
	protected static final String MEMORY_CARD_CODE = "memorycard";
	protected static final String LENS_CODE = "lens";
	protected static final String DECLINE_ENTRIES = "declineEntries";
	protected final static int timeOut = 15; //seconds

	protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
	protected static final String REALLOCATE_CONSIGNMENT_CHOICE = "reallocateConsignment";
	protected static final String ORDER_ACTION_EVENT_NAME = "OrderActionEvent";
	protected static final String RE_SOURCE_CHOICE = "reSource";

	@Resource
	protected SourcingUtil sourcingUtil;
	@Resource
	protected ReturnUtil returnUtil;
	@Resource
	protected CancellationUtil cancellationUtil;
	@Resource
	protected DeclineUtil declineUtil;

	protected PointsOfService pointsOfService;
	protected ModelService modelService;
	protected DeliveryModeModel deliveryMode;
	protected OrderModel order;
	protected StockLevels stockLevels;
	protected Products products;
	protected ProductService productService;
	protected Warehouses warehouses;
	protected StockService stockService;
	protected CommerceStockService commerceStockService;
	protected Orders orders;
	protected BaseStores baseStores;
	protected Users users;
	protected SourcingConfigs sourcingConfigs;
	protected RestockConfigs restockConfigs;
	protected WarehouseStockService warehouseStockService;
	protected BusinessProcessService orderBusinessProcessService;

	@Before
	public void setup()
	{
		users = sourcingUtil.getUsers();
		baseStores = sourcingUtil.getBaseStores();
		restockConfigs = returnUtil.getRestockConfigs();
		pointsOfService = sourcingUtil.getPointsOfService();
		order = sourcingUtil.getOrder();
		stockLevels = sourcingUtil.getStockLevels();
		products = sourcingUtil.getProducts();
		productService = sourcingUtil.getProductService();
		warehouses = sourcingUtil.getWarehouses();
		stockService = sourcingUtil.getStockService();
		commerceStockService = sourcingUtil.getCommerceStockService();
		orders = sourcingUtil.getOrders();
		modelService = sourcingUtil.getModelService();
		deliveryMode = sourcingUtil.getDeliveryModes().standardShipment();
		sourcingConfigs = sourcingUtil.getSourcingConfigs();
		warehouseStockService = sourcingUtil.getWarehouseStockService();
		orderBusinessProcessService = sourcingUtil.getOrderBusinessProcessService();

		restockConfigs.RestockAfterReturn();
		users.Nancy();
		baseStores.NorthAmerica()
				.setPointsOfService(Lists.newArrayList(pointsOfService.Boston(), pointsOfService.Montreal_Downtown()));
		baseStores.NorthAmerica().setSourcingConfig(sourcingConfigs.HybrisConfig());
		saveAll();
	}

	@After
	public void resetFactors()
	{
		modelService.remove(baseStores.NorthAmerica().getSourcingConfig());
	}

	protected void saveAll()
	{
		modelService.saveAll();
	}

	protected void cleanUpData()
	{
		cleanUpModel("Order");
		cleanUpModel("Consignment");
		cleanUpModel("BusinessProcess");
		cleanUpModel("InventoryEvent");
		cleanUpModel("ConsignmentEntryEvent");
		cleanUpModel("SourcingBan");
		cleanUpModel("PickUpDeliveryMode");
		cleanUpModel("TaskCondition");
		cleanUpModel("Task");
		cleanUpModel("StockLevel");
		cleanUpModel("OrderCancelConfig");
		cleanUpModel("RestockConfig");
		cleanUpModel("BaseStore");
	}

	protected void cleanUpModel(String modelName)
	{
		try
		{
			SearchResult<T> result = flexibleSearchService.search("SELECT {pk} FROM {" + modelName + "}");
			if (result.getCount() != 0)
				modelService.removeAll(result.getResult());
		}
		catch (NullPointerException e)
		{
			//do nothing
		}
	}
}
