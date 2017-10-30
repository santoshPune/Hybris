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
 */
package de.hybris.platform.sap.saporderexchangeoms.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultSalesConditionsContributor;
import de.hybris.platform.sap.sapmodel.model.SAPPricingConditionModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * SAP OMS SalesConditions Contributor
 *
 */
public class SapOmsSalesConditionsContributor extends DefaultSalesConditionsContributor
{

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		return super.syncPricingInactive(entries) ? createRowsAsyncPricing(order) : createRowsSyncPricing(order);
	}

	/**
	 * @param order
	 * @return pricing conditions with SAP synchronous pricing
	 */
	protected List<Map<String, Object>> createRowsSyncPricing(final OrderModel order)
	{
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final ConsignmentModel consignment : order.getConsignments())
		{

			for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
			{

				final Iterator<SAPPricingConditionModel> it = consignmentEntry.getOrderEntry().getSapPricingConditions().iterator();
				while (it.hasNext())
				{
					final SAPPricingConditionModel condition = it.next();
					final Map<String, Object> row = new HashMap<>();

					row.put(OrderCsvColumns.ORDER_ID, order.getCode());
					row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER,
							Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber() - 1));

					row.put(SalesConditionCsvColumns.CONDITION_CODE, condition.getConditionType());
					row.put(SalesConditionCsvColumns.CONDITION_VALUE, condition.getConditionRate());
					row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, condition.getConditionUnit());
					row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, condition.getConditionPricingUnit());
					row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, condition.getCurrencyKey());
					row.put(SalesConditionCsvColumns.CONDITION_COUNTER, condition.getConditionCounter());
					result.add(row);
				}
			}
		}

		return result;

	}

	/**
	 * @param order
	 * @return pricing conditions with with SAP asynchronous pricing
	 */
	protected List<Map<String, Object>> createRowsAsyncPricing(final OrderModel order)
	{
		setConditionTypes(order);

		final List<Map<String, Object>> result = new ArrayList<>();

		for (final ConsignmentModel consignment : order.getConsignments())
		{
			for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
			{
				createGrossPriceRow(order, consignmentEntry, result);
				createTaxRows(order, consignmentEntry, result);
				createDiscountRows(order, consignmentEntry, result);
			}
		}

		createDeliveryCostRow(order, result);
		createPaymentCostRow(order, result);

		return result;

	}

	protected void createGrossPriceRow(final OrderModel order, final ConsignmentEntryModel consignmentEntry,
			final List<Map<String, Object>> result)
	{
		final Map<String, Object> row = new HashMap<>();
		final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();

		row.put(OrderCsvColumns.ORDER_ID, order.getCode());
		row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber() - 1));
		row.put(SalesConditionCsvColumns.CONDITION_CODE, getGrossPrice());
		row.put(SalesConditionCsvColumns.CONDITION_VALUE, entry.getBasePrice());
		row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
		row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
		row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
		row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
		row.put(SalesConditionCsvColumns.CONDITION_COUNTER, Integer.valueOf(getConditionCounterGrossPrice()));

		result.add(row);
	}


	protected void createDiscountRows(final OrderModel order, final ConsignmentEntryModel consignmentEntry,
			final List<Map<String, Object>> result)
	{
		final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
		final List<DiscountValue> discountList = consignmentEntry.getOrderEntry().getDiscountValues();
		int conditionCounter = getConditionCounterStartDiscount();

		for (final DiscountValue disVal : discountList)
		{
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER,
					Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber() - 1));
			if (disVal.isAbsolute())
			{
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
			}
			else
			{
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.FALSE);
			}
			row.put(SalesConditionCsvColumns.CONDITION_CODE, disVal.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_VALUE, Double.valueOf(disVal.getValue() * -1));
			row.put(SalesConditionCsvColumns.CONDITION_COUNTER, Integer.valueOf(conditionCounter++));
			result.add(row);
		}

	}


	protected void createTaxRows(final OrderModel order, final ConsignmentEntryModel consignmentEntry,
			final List<Map<String, Object>> result)
	{
		final AbstractOrderEntryModel entry = consignmentEntry.getOrderEntry();
		final Iterator<TaxValue> taxIterator = entry.getTaxValues().iterator();

		while (taxIterator.hasNext())
		{
			final TaxValue next = taxIterator.next();
			final Map<String, Object> row = new HashMap<>();
			row.put(OrderCsvColumns.ORDER_ID, order.getCode());
			row.put(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER,
					Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber() - 1));
			row.put(SalesConditionCsvColumns.CONDITION_CODE, getTax1());
			row.put(SalesConditionCsvColumns.CONDITION_VALUE, Double.valueOf(next.getValue()));
			row.put(SalesConditionCsvColumns.CONDITION_COUNTER, Integer.valueOf(getConditionCounterTax()));

			if (next.isAbsolute())
			{
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.TRUE);
				row.put(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, order.getCurrency().getIsocode());
				row.put(SalesConditionCsvColumns.CONDITION_UNIT_CODE, entry.getUnit().getCode());
				row.put(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, entry.getProduct().getPriceQuantity());
			}
			else
			{
				row.put(SalesConditionCsvColumns.ABSOLUTE, Boolean.FALSE);
			}

			result.add(row);
			break; // Currently only the first entry is used
		}

	}




}
