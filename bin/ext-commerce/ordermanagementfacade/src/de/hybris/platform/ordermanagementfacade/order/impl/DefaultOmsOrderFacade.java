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
package de.hybris.platform.ordermanagementfacade.order.impl;


import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudReportData;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordermanagementfacade.order.OmsOrderFacade;
import de.hybris.platform.ordermanagementfacade.search.dao.impl.SearchByStatusPagedGenericDao;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Default implementation of {@link OmsOrderFacade}
 */
public class DefaultOmsOrderFacade extends OmsBaseFacade implements OmsOrderFacade
{
	private Converter<FraudReportModel, FraudReportData> fraudReportConverter;
	private GenericDao<FraudReportModel> fraudReportGenericDao;
	private Converter<OrderModel, OrderData> orderConverter;
	private Converter<OrderEntryModel, OrderEntryData> orderEntryConverter;
	private GenericDao<OrderModel> orderGenericDao;
	private PagedGenericDao<OrderModel> orderPagedGenericDao;
	private SearchByStatusPagedGenericDao<OrderModel> orderSearchByStatusPagedDao;
	private PagedGenericDao<OrderEntryModel> orderEntryPagedGenericDao;
	private EnumerationService enumerationService;
	private OrderService orderService;

	@Override
	public SearchPageData<OrderData> getOrders(final PageableData pageableData)
	{
		final SearchPageData<OrderModel> orderSearchPageData = getOrderPagedGenericDao().find(pageableData);
		return convertSearchPageData(orderSearchPageData, getOrderConverter());
	}

	@Override
	public OrderData getOrderForCode(final String code)
	{
		return getOrderConverter().convert(getOrderModelForCode(code));
	}

	@Override
	public SearchPageData<OrderData> getOrdersByStatuses(final PageableData pageableData, final Set<OrderStatus> orderStatusSet)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(OrderModel.STATUS, orderStatusSet);
		return convertSearchPageData(getOrderSearchByStatusPagedDao().find(params, pageableData), getOrderConverter());
	}

	@Override
	public List<OrderStatus> getOrderStatuses()
	{
		return getEnumerationService().getEnumerationValues(OrderStatus._TYPECODE);
	}

	@Override
	public SearchPageData<OrderEntryData> getOrderEntriesForOrderCode(final String code, final PageableData pageableData)
	{
		final OrderModel order = getOrderModelForCode(code);

		final Map<String, OrderModel> orderEntryParams = new HashMap<>();
		orderEntryParams.put(OrderEntryModel.ORDER, order);
		return convertSearchPageData(getOrderEntryPagedGenericDao().find(orderEntryParams, pageableData), getOrderEntryConverter());
	}

	@Override
	public OrderEntryData getOrderEntryForOrderCodeAndEntryNumber(final String orderCode, final Integer entryNumber)
	{
		final OrderModel order = getOrderModelForCode(orderCode);
		return getOrderEntryConverter().convert(getOrderService().getEntryForNumber(order, entryNumber));
	}

	@Override
	public List<FraudReportData> getOrderFraudReports(final String orderCode)
	{
		final Map<String, OrderModel> fraudParam = new HashMap<>();
		fraudParam.put(FraudReportModel.ORDER, getOrderModelForCode(orderCode));
		final List<FraudReportModel> fraudReports = getFraudReportGenericDao().find(fraudParam);
		return Converters.convertAll(fraudReports, getFraudReportConverter());
	}

	@Override
	public List<CancelReason> getCancelReasons()
	{
		return getEnumerationService().getEnumerationValues(CancelReason._TYPECODE);
	}

	/**
	 * Finds {@link OrderModel} for the given {@value OrderModel#CODE}
	 *
	 * @param code
	 *           the order's code
	 * @return the requested order for the given code
	 */
	protected OrderModel getOrderModelForCode(final String code)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(OrderModel.CODE, code);

		final List<OrderModel> resultSet = discardOrderSnapshot(getOrderGenericDao().find(params));

		if (resultSet.isEmpty())
		{
			throw new ModelNotFoundException(String.format("Could not find Order with code: [%s].", code));
		}

		if (resultSet.size() > 1)
		{
			throw new AmbiguousIdentifierException(String.format("Multiple results found for Order with code: [%s].", code));
		}
		return resultSet.get(0);
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setOrderPagedGenericDao(final PagedGenericDao<OrderModel> orderPagedGenericDao)
	{
		this.orderPagedGenericDao = orderPagedGenericDao;
	}

	protected PagedGenericDao<OrderModel> getOrderPagedGenericDao()
	{
		return orderPagedGenericDao;
	}

	@Required
	public void setOrderGenericDao(final GenericDao<OrderModel> orderGenericDao)
	{
		this.orderGenericDao = orderGenericDao;
	}

	protected GenericDao<OrderModel> getOrderGenericDao()
	{
		return orderGenericDao;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	protected Converter<OrderEntryModel, OrderEntryData> getOrderEntryConverter()
	{
		return orderEntryConverter;
	}

	@Required
	public void setOrderEntryConverter(final Converter<OrderEntryModel, OrderEntryData> orderEntryConverter)
	{
		this.orderEntryConverter = orderEntryConverter;
	}

	protected PagedGenericDao<OrderEntryModel> getOrderEntryPagedGenericDao()
	{
		return orderEntryPagedGenericDao;
	}

	@Required
	public void setOrderEntryPagedGenericDao(final PagedGenericDao<OrderEntryModel> orderEntryPagedGenericDao)
	{
		this.orderEntryPagedGenericDao = orderEntryPagedGenericDao;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setOrderSearchByStatusPagedDao(final SearchByStatusPagedGenericDao<OrderModel> orderSearchByStatusPagedDao)
	{
		this.orderSearchByStatusPagedDao = orderSearchByStatusPagedDao;
	}

	protected SearchByStatusPagedGenericDao<OrderModel> getOrderSearchByStatusPagedDao()
	{
		return orderSearchByStatusPagedDao;
	}

	@Required
	public void setFraudReportConverter(final Converter<FraudReportModel, FraudReportData> fraudReportConverter)
	{
		this.fraudReportConverter = fraudReportConverter;
	}

	protected Converter<FraudReportModel, FraudReportData> getFraudReportConverter()
	{
		return fraudReportConverter;
	}

	@Required
	public void setFraudReportGenericDao(final GenericDao<FraudReportModel> fraudReportGenericDao)
	{
		this.fraudReportGenericDao = fraudReportGenericDao;
	}

	protected GenericDao<FraudReportModel> getFraudReportGenericDao()
	{
		return fraudReportGenericDao;
	}

}
