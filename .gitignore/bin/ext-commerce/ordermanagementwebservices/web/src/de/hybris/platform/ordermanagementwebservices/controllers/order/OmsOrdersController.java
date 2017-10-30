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
package de.hybris.platform.ordermanagementwebservices.controllers.order;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.ordermanagementwebservices.controllers.OmsBaseController;
import de.hybris.platform.ordermanagementwebservices.dto.fraud.FraudReportListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderEntrySearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderSearchPageWsDto;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderStatusListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.CancelReasonListWsDTO;
import de.hybris.platform.ordermanagementfacade.constants.OrdermanagementfacadeConstants;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudReportDataList;
import de.hybris.platform.ordermanagementfacade.order.data.OrderStatusDataList;
import de.hybris.platform.ordermanagementfacade.order.data.CancelReasonDataList;
import de.hybris.platform.ordermanagementfacade.order.OmsOrderFacade;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * WebResource exposing {@link OmsOrderFacade}
 * http://host:port/ordermanagementwebservices/orders
 */
@Controller
@RequestMapping(value = "/orders")
public class OmsOrdersController extends OmsBaseController
{
	@Resource
	private OmsOrderFacade omsOrderFacade;

	/**
	 * Request to get all orders in the system
	 *
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 *           number of the current page
	 * @param pageSize
	 *           number of items in a page
	 * @param sort
	 *           sorting the results ascending or descending
	 * @return the list of orders
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public OrderSearchPageWsDto getOrders(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = "0") final int currentPage,
			@RequestParam(required = false, defaultValue = "10") final int pageSize,
			@RequestParam(required = false, defaultValue = "asc") final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<OrderData> orderSearchPageData = omsOrderFacade.getOrders(pageableData);
		return dataMapper.map(orderSearchPageData, OrderSearchPageWsDto.class, fields);
	}

	/**
	 * Request to get an order by code
	 *
	 * @param code
	 *           the code of the requested order
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @return the order
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public OrderWsDTO getOrderForCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData order = omsOrderFacade.getOrderForCode(code);
		return dataMapper.map(order, OrderWsDTO.class, fields);
	}

	/**
	 * Request to get all orders with certain order status(es)
	 *
	 * @param orderStatuses
	 *           a list of valid order statuses separated by ","
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 *           number of the current page
	 * @param pageSize
	 *           number of items in a page
	 * @param sort
	 *           sorting the results ascending or descending
	 * @return the list of orders fulfilling the above conditions
	 * @throws WebserviceValidationException
	 *            in case of passing a wrong order status validation exception will be thrown
	 */
	@RequestMapping(value = "status/{orderStatuses}", method = RequestMethod.GET)
	@ResponseBody
	public OrderSearchPageWsDto getOrdersByStatus(@PathVariable final String orderStatuses,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort) throws WebserviceValidationException
	{
		final Set<OrderStatus> statusSet = extractOrderStatuses(orderStatuses);
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<OrderData> orderSearchPageData = omsOrderFacade.getOrdersByStatuses(pageableData, statusSet);
		return dataMapper.map(orderSearchPageData, OrderSearchPageWsDto.class, fields);
	}

	/**
	 * Request to get all {@link OrderStatus} in the system
	 *
	 * @return list of order statuses
	 */
	@RequestMapping(value = "/statuses", method = RequestMethod.GET)
	@ResponseBody
	public OrderStatusListWsDTO getOrderStatuses()
	{
		final List<OrderStatus> orderStatuses = omsOrderFacade.getOrderStatuses();
		final OrderStatusDataList orderStatusList = new OrderStatusDataList();
		orderStatusList.setStatuses(orderStatuses);
		return dataMapper.map(orderStatusList, OrderStatusListWsDTO.class);
	}

	/**
	 * Request to get orderEntries for the given {@link de.hybris.platform.core.model.order.OrderModel#CODE}
	 *
	 * @param code
	 *           order's code for the requested order entries
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 *           number of the current page
	 * @param pageSize
	 *           number of items in a page
	 * @param sort
	 *           sorting the results ascending or descending
	 * @return the list of orderEntries fulfilling the above conditions
	 */
	@RequestMapping(value = "/{code}/entries", method = RequestMethod.GET)
	@ResponseBody
	public OrderEntrySearchPageWsDTO getOrderEntriesForOrderCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<OrderEntryData> orderEntrySearchPageData = omsOrderFacade.getOrderEntriesForOrderCode(code,
				pageableData);
		return dataMapper.map(orderEntrySearchPageData, OrderEntrySearchPageWsDTO.class, fields);
	}

	/**
	 * Request to get orderEntry for the given {@link de.hybris.platform.core.model.order.OrderModel#CODE} and
	 * {@link de.hybris.platform.core.model.order.OrderEntryModel#ENTRYNUMBER}
	 *
	 * @param code
	 *           order's code for the requested order entries
	 * @param entryNumber
	 *           the entry number
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @return the list of orderEntries fulfilling the above conditions
	 */
	@RequestMapping(value = "/{code}/entries/{entryNumber}", method = RequestMethod.GET)
	@ResponseBody
	public OrderEntryWsDTO getOrderEntryForOrderCodeAndEntryNumber(@PathVariable final String code,
			@PathVariable final String entryNumber,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderEntryData orderEntryData = omsOrderFacade.getOrderEntryForOrderCodeAndEntryNumber(code,
				Integer.valueOf(entryNumber));
		return dataMapper.map(orderEntryData, OrderEntryWsDTO.class, fields);
	}

	/**
	 * Request to get all {@link CancelReason} in the system
	 *
	 * @return list of cancel reasons
	 */
	@RequestMapping(value = "/cancel-reasons", method = RequestMethod.GET)
	@ResponseBody
	public CancelReasonListWsDTO getCancelReason()
	{
		final List<CancelReason> cancelReasons = omsOrderFacade.getCancelReasons();
		final CancelReasonDataList cancelReasonList = new CancelReasonDataList();
		cancelReasonList.setReasons(cancelReasons);
		return dataMapper.map(cancelReasonList, CancelReasonListWsDTO.class);
	}

	/**
	 * Request to get fraud reports for a certain order
	 *
	 * @param code
	 *           order's code for which to get the fraud reports
	 * @param fields
	 *           defaulted to DEFAULT but can be FULL or BASIC
	 * @return list of the order's fraud reports
	 */
	@RequestMapping(value = "/{code}/fraudreports", method = RequestMethod.GET)
	@ResponseBody
	public FraudReportListWsDTO getOrderFraudReports(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final FraudReportDataList fraudReports = new FraudReportDataList();
		fraudReports.setReports(omsOrderFacade.getOrderFraudReports(code));
		return dataMapper.map(fraudReports, FraudReportListWsDTO.class, fields);
	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(OrdermanagementfacadeConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		try
		{
			for (final String status : statusesStrings)
			{
				statusesEnum.add(OrderStatus.valueOf(status));
			}
		}
		catch (final IllegalArgumentException e) //NOSONAR
		{
			throw new WebserviceValidationException(e.getMessage()); //NOSONAR
		}
		return statusesEnum;
	}
}
