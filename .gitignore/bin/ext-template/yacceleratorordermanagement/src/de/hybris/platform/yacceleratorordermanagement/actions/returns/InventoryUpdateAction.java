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

package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.returns.RestockException;
import de.hybris.platform.warehousing.returns.service.RestockConfigService;
import de.hybris.platform.warehousing.returns.strategy.RestockWarehouseSelectionStrategy;
import de.hybris.platform.warehousing.stock.services.WarehouseStockService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;


/**
 * Update inventory and set the {@link ReturnRequestModel} status to COMPLETED.<br/>
 * A custom update inventory behavior must be implemented. This determines the steps to be executed after a successful
 * return.
 */
public class InventoryUpdateAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(InventoryUpdateAction.class);
	private RestockConfigService restockConfigService;
	private TimeService timeService;
	private RestockWarehouseSelectionStrategy restockWarehouseSelectionStrategy;
	private WarehouseStockService warehouseStockService;

	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();

		if (getRestockConfigService().getRestockConfig() != null)
		{
			final WarehouseModel warehouse = getRestockWarehouseSelectionStrategy().performStrategy(returnRequest);

			if (warehouse == null)
			{
				LOG.info("cannot find any warehouse accept returned item(s), please update returned stock(s) manually");
			}
			else if (Boolean.TRUE.equals(getRestockConfigService().getRestockConfig().getIsUpdateStockAfterReturn()))
			{
				Assert.notNull(getRestockConfigService().getRestockConfig().getReturnedBinCode(),
						"Code cannot be null for the returned bin code");
				if (CollectionUtils.isEmpty(returnRequest.getReturnEntries()))
				{
					throw new IllegalStateException("Return Entry cannot be null or empty");
				}
				returnRequest.getReturnEntries().stream().forEach(returnEntry ->
				{
					try
					{
						getWarehouseStockService().createStockLevel(returnEntry.getOrderEntry().getProduct().getCode(), warehouse,
								returnEntry.getReceivedQuantity().intValue(), null, getCurrentDateWithDelayDaysBeforeRestock(),
								getRestockConfigService().getRestockConfig().getReturnedBinCode());
					}
					catch (final RestockException e) //NOSONAR
					{
						LOG.error("More than one record found for restockConfig");
					}
				});
			}
		}
		returnRequest.setStatus(ReturnStatus.COMPLETED);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(ReturnStatus.COMPLETED);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}

	/**
	 * Calculates the current date -  # of delay days before restock according to a property.
	 */
	protected Date getCurrentDateWithDelayDaysBeforeRestock() throws RestockException
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(getTimeService().getCurrentTime());

		if (getRestockConfigService().getRestockConfig() != null)
		{
			final int delayDays = getRestockConfigService().getRestockConfig().getDelayDaysBeforeRestock();

			cal.add(Calendar.DATE, delayDays);
		}
		return cal.getTime();
	}

	@Required
	public void setRestockConfigService(final RestockConfigService restockConfigService)
	{
		this.restockConfigService = restockConfigService;
	}

	protected RestockConfigService getRestockConfigService()
	{
		return restockConfigService;
	}

	protected RestockWarehouseSelectionStrategy getRestockWarehouseSelectionStrategy()
	{
		return restockWarehouseSelectionStrategy;
	}

	@Required
	public void setRestockWarehouseSelectionStrategy(final RestockWarehouseSelectionStrategy restockWarehouseSelectionStrategy)
	{
		this.restockWarehouseSelectionStrategy = restockWarehouseSelectionStrategy;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	protected WarehouseStockService getWarehouseStockService()
	{
		return warehouseStockService;
	}

	@Required
	public void setWarehouseStockService(WarehouseStockService warehouseStockService)
	{
		this.warehouseStockService = warehouseStockService;
	}
}
