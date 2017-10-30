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
package de.hybris.platform.warehousingwebservices.controllers.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.ConsignmentWsDTO;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousingfacade.order.data.DeclineReasonDataList;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import de.hybris.platform.warehousingwebservices.dto.order.ConsignmentSearchPageWsDto;
import de.hybris.platform.warehousingwebservices.dto.order.ConsignmentEntrySearchPageWsDto;
import de.hybris.platform.warehousingwebservices.dto.order.ConsignmentStatusListWsDTO;
import de.hybris.platform.warehousingfacade.constants.WarehousingfacadeConstants;
import de.hybris.platform.warehousingfacade.order.WarehousingConsignmentFacade;
import de.hybris.platform.warehousingfacade.order.data.ConsignmentStatusDataList;
import de.hybris.platform.warehousingwebservices.dto.order.DeclineReasonListWsDTO;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseSearchPageWsDto;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * WebResource exposing {@link de.hybris.platform.warehousingfacade.order.WarehousingConsignmentFacade}
 * http://host:port/warehousingwebservices/consignments
 */
@Controller
@RequestMapping(value = "/consignments")
public class WarehousingConsignmentsController extends WarehousingBaseController
{
	@Resource
	private WarehousingConsignmentFacade warehousingConsignmentFacade;

	/**
	 * Request to get all consignments in the system
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of consignments
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ConsignmentSearchPageWsDto getConsignments(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ConsignmentData> consignmentSearchPageData = warehousingConsignmentFacade.getConsignments(pageableData);
		return dataMapper.map(consignmentSearchPageData, ConsignmentSearchPageWsDto.class, fields);
	}

	/**
	 * Request to get Consignment for the given code
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param code
	 * 		code to get the required consignment
	 * @return consignment details for the given code
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public ConsignmentWsDTO getConsignmentForCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ConsignmentData consignment = warehousingConsignmentFacade.getConsignmentForCode(code);
		return dataMapper.map(consignment, ConsignmentWsDTO.class, fields);
	}

	/**
	 * Request to get all sourcing locations for the given {@value de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE}
	 *
	 * @param code
	 * 		consignment's code for the requested sourcing location
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of locations compliant to the above conditions
	 */
	@RequestMapping(value = "/{code}/sourcing-locations", method = RequestMethod.GET)
	@ResponseBody
	public WarehouseSearchPageWsDto getSourcingLocationsForConsignmentCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<WarehouseData> warehouseSearchPageData = warehousingConsignmentFacade
				.getSourcingLocationsForConsignmentCode(code, pageableData);

		return dataMapper.map(warehouseSearchPageData, WarehouseSearchPageWsDto.class, fields);
	}

	/**
	 * Request to get all consignments with certain consignment status(es)
	 *
	 * @param consignmentStatuses
	 * 		a list of valid consignment statuses separated by ","
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of consignments that complies with conditions above
	 * @throws WebserviceValidationException
	 * 		in case of passing a wrong {@link ConsignmentStatus}
	 */
	@RequestMapping(value = "status/{consignmentStatuses}", method = RequestMethod.GET)
	@ResponseBody
	public ConsignmentSearchPageWsDto getConsignmentsByStatus(@PathVariable final String consignmentStatuses,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort) throws WebserviceValidationException
	{
		final Set<ConsignmentStatus> statusSet = extractConsignmentStatuses(consignmentStatuses);
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ConsignmentData> consignmentSearchPageData = warehousingConsignmentFacade.getConsignmentsByStatuses(pageableData, statusSet);
		return dataMapper.map(consignmentSearchPageData, ConsignmentSearchPageWsDto.class, fields);
	}

	/**
	 * Request to get all {@link ConsignmentStatus} in the system
	 *
	 * @return list of consignment statuses
	 */
	@RequestMapping(value = "/statuses", method = RequestMethod.GET)
	@ResponseBody
	public ConsignmentStatusListWsDTO getConsignmentStatuses()
	{
		final List<ConsignmentStatus> consignmentStatuses = warehousingConsignmentFacade.getConsignmentStatuses();
		final ConsignmentStatusDataList consignmentStatusList = new ConsignmentStatusDataList();
		consignmentStatusList.setStatuses(consignmentStatuses);
		return dataMapper.map(consignmentStatusList, ConsignmentStatusListWsDTO.class);
	}

	/**
	 * Request to get all decline reasons available in the system.
	 *
	 * @return list of {@link DeclineReason}
	 */
	@RequestMapping(value = "/decline-reasons", method = RequestMethod.GET)
	@ResponseBody
	public DeclineReasonListWsDTO getDeclineReasons()
	{
		final List<DeclineReason> declineReasons = warehousingConsignmentFacade.getDeclineReasons();
		final DeclineReasonDataList declineReasonList = new DeclineReasonDataList();
		declineReasonList.setReasons(declineReasons);
		return dataMapper.map(declineReasonList, DeclineReasonListWsDTO.class);
	}

	/**
	 * Request to get all consignment entries for the given {@value de.hybris.platform.ordersplitting.model.ConsignmentModel#CODE}
	 *
	 * @param code
	 * 		consignment's code for the requested consignment entries
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of consignmentEntries fulfilling the above conditions
	 */
	@RequestMapping(value = "/{code}/entries", method = RequestMethod.GET)
	@ResponseBody
	public ConsignmentEntrySearchPageWsDto getConsignmentEntriesForConsignmentCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ConsignmentEntryData> consignmentEntrySearchPageData = warehousingConsignmentFacade.getConsignmentEntriesForConsignmentCode(code,pageableData);
		return dataMapper.map(consignmentEntrySearchPageData, ConsignmentEntrySearchPageWsDto.class, fields);
	}

	/**
	 * Request to confirm Consignment's shipping
	 * @param code
	 * 		consignment's code for the requested consignment
	 */
	@RequestMapping(value = "{code}/confirm-shipping", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void confirmShipConsignment(@PathVariable final String code)
	{
		warehousingConsignmentFacade.confirmShipConsignment(code);
	}

	/**
	 * Request to confirm Consignment's pickup
	 * @param code
	 * 		consignment's code for the requested consignment
	 */
	@RequestMapping(value = "{code}/confirm-pickup", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void confirmPickupConsignment(@PathVariable final String code)
	{
		warehousingConsignmentFacade.confirmPickupConsignment(code);
	}

	/**
	 * Request to check if Consignment can be conifrmed
	 * @param code
	 * 		consignment's code for the requested consignment
	 */
	@RequestMapping(value = "{code}/is-confirmable", method = RequestMethod.GET)
	@ResponseBody
	public Boolean isConsignmentConfirmable(@PathVariable final String code)
	{
		return warehousingConsignmentFacade.isConsignmentConfirmable(code);
	}

	/**
	 * Extract the set of {@link ConsignmentStatus} from the request
	 *
	 * @param statuses
	 * 		"," separated {@link ConsignmentStatus}
	 * @return set of {@link ConsignmentStatus}
	 * @throws WebserviceValidationException
	 * 		in case of passing a wrong {@link ConsignmentStatus}
	 */
	protected Set<ConsignmentStatus> extractConsignmentStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(WarehousingfacadeConstants.OPTIONS_SEPARATOR);

		final Set<ConsignmentStatus> statusesEnum = new HashSet<>();
		try
		{
			for (final String status : statusesStrings)
			{
				statusesEnum.add(ConsignmentStatus.valueOf(status));
			}
		}
		catch (final IllegalArgumentException e)  //NOSONAR
		{
			throw new WebserviceValidationException(e.getMessage()); //NOSONAR
		}
		return statusesEnum;
	}

}
