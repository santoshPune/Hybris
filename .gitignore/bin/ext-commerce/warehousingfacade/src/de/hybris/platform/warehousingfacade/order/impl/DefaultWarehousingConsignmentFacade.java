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
package de.hybris.platform.warehousingfacade.order.impl;


import com.google.common.collect.Sets;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.shipping.service.WarehousingShippingService;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import de.hybris.platform.warehousingfacade.order.WarehousingConsignmentFacade;
import de.hybris.platform.ordermanagementfacade.search.dao.impl.SearchByStatusPagedGenericDao;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;


/**
 * Default implementation of {@link WarehousingConsignmentFacade}.
 */
public class DefaultWarehousingConsignmentFacade extends OmsBaseFacade implements WarehousingConsignmentFacade
{
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	private Converter<WarehouseModel, WarehouseData> warehouseConverter;
	private PagedGenericDao<ConsignmentModel> consignmentPagedGenericDao;
	private GenericDao<ConsignmentModel> consignmentGenericDao;
	private PagedGenericDao<ConsignmentModel> consignmentEntryPagedDao;
	private SearchByStatusPagedGenericDao<ConsignmentModel> consignmentSearchByStatusPagedDao;
	private EnumerationService enumerationService;
	private WarehousingShippingService warehousingShippingService;
	private SourcingFilterProcessor sourcingFilterProcessor;

	@Override
	public SearchPageData<ConsignmentData> getConsignments(final PageableData pageableData)
	{
		return convertSearchPageData(getConsignmentPagedGenericDao().find(pageableData), getConsignmentConverter());
	}

	@Override
	public SearchPageData<ConsignmentData> getConsignmentsByStatuses(final PageableData pageableData,
			final Set<ConsignmentStatus> consignmentStatusSet)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(ConsignmentModel.STATUS, consignmentStatusSet);
		return convertSearchPageData(getConsignmentSearchByStatusPagedDao().find(params, pageableData), getConsignmentConverter());
	}

	@Override
	public SearchPageData<ConsignmentEntryData> getConsignmentEntriesForConsignmentCode(final String code,
			final PageableData pageableData)
	{
		final ConsignmentModel consignment = getConsignmentModelForCode(code);

		final Map<String, ConsignmentModel> consignmentEntryParams = new HashMap<>();
		consignmentEntryParams.put(ConsignmentEntryModel.CONSIGNMENT, consignment);
		return convertSearchPageData(getConsignmentEntryPagedDao().find(consignmentEntryParams, pageableData),
				getConsignmentEntryConverter());
	}

	@Override
	public ConsignmentData getConsignmentForCode(final String code)
	{
		return getConsignmentConverter().convert(getConsignmentModelForCode(code));
	}

	@Override
	public List<ConsignmentStatus> getConsignmentStatuses()
	{
		return getEnumerationService().getEnumerationValues(ConsignmentStatus._TYPECODE);
	}

	@Override
	public List<DeclineReason> getDeclineReasons()
	{
		return getEnumerationService().getEnumerationValues(DeclineReason._TYPECODE);
	}

	@Override
	public SearchPageData<WarehouseData> getSourcingLocationsForConsignmentCode(final String code, final PageableData pageableData)
	{
		Assert.notNull(code, "Code cannot be null for the consignment");

		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		final Set<WarehouseModel> locations = Sets.newHashSet();
		getSourcingFilterProcessor().filterLocations(consignmentModel.getOrder(), locations);
		List<WarehouseModel> locationsList = new ArrayList<>(locations);
		Collections.sort(locationsList,
				(warehouseModel1, warehouseModel2) -> warehouseModel1.getCode().compareTo(warehouseModel2.getCode()));

		SearchPageData<WarehouseModel> searchPageData = new SearchPageData<>();
		searchPageData.setPagination(createPaginationData(pageableData, locations.size()));
		searchPageData.setResults(getSublistOfSourcingLocations(pageableData, locationsList));

		return convertSearchPageData(searchPageData, getWarehouseConverter());
	}

	@Override
	public void confirmShipConsignment(final String code)
	{
		Assert.notNull(code, "Code cannot be null for the consignment");
		Assert.isTrue(isConsignmentConfirmable(code), String.format("Confirmation is not possible for Consignment with the code: [%s].", code));

		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		Assert.isTrue(!(consignmentModel.getDeliveryMode() instanceof PickUpDeliveryModeModel),
				String.format("Shipping is not allowed for Pick up Consignment with the code: [%s].", consignmentModel.getCode()));

		getWarehousingShippingService().confirmShipConsignment(consignmentModel);
	}

	@Override
	public void confirmPickupConsignment(final String code)
	{
		Assert.notNull(code, "Code cannot be null for the consignment");
		Assert.isTrue(isConsignmentConfirmable(code), String.format("Confirmation is not possible for Consignment with the code: [%s].", code));

		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		Assert.isTrue((consignmentModel.getDeliveryMode() instanceof PickUpDeliveryModeModel),
				String.format("Pick up is not allowed for Shipping Consignment with the code: [%s].", consignmentModel.getCode()));

		getWarehousingShippingService().confirmPickupConsignment(consignmentModel);
	}

	@Override
	public boolean isConsignmentConfirmable(final String code)
	{
		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		return getWarehousingShippingService().isConsignmentConfirmable(consignmentModel);
	}

	/**
	 * Finds {@link ConsignmentModel} for the given {@link ConsignmentModel#CODE}
	 *
	 * @param code
	 * 		the consignment's code
	 * @return the requested consignment for the given code
	 */
	protected ConsignmentModel getConsignmentModelForCode(final String code)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(ConsignmentModel.CODE, code);

		List<ConsignmentModel> consignments = getConsignmentGenericDao().find(params);
		validateIfSingleResult(consignments, String.format("Could not find Consignment with code: [%s].", code),
				String.format("Multiple results found for Consignment with code: [%s].", code));

		return consignments.get(0);
	}

	/**
	 * Creates a {@link de.hybris.platform.commerceservices.search.pagedata.PaginationData} based on the received PageableData
	 *
	 * @param pageableData
	 * 			contains pageable information
	 * @param totalResults
	 * 			the total number of results returned
	 * @return pagination data object
	 */
	protected PaginationData createPaginationData(final PageableData pageableData, final int totalResults)
	{
		PaginationData paginationData = new PaginationData();
		paginationData.setPageSize(pageableData.getPageSize());
		paginationData.setSort(pageableData.getSort());
		paginationData.setTotalNumberOfResults(totalResults);

		// Calculate the number of pages
		paginationData.setNumberOfPages((int) Math.ceil(((double) paginationData.getTotalNumberOfResults())
				/ paginationData.getPageSize()));

		// Work out the current page, fixing any invalid page values
		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		return paginationData;
	}

	/**
	 * Gets a sub list based on the pageable data object from the list containing all warehouses.
	 *
	 * @param pageableData
	 * 			the object which will filter the list with its page related information
	 * @param locations
	 * 			contains all available sourcing locations.
	 * @return the sub list which met the pageable data criteria
	 */
	protected List<WarehouseModel> getSublistOfSourcingLocations(final PageableData pageableData, final List<WarehouseModel> locations)
	{
		final int fromIndex = pageableData.getCurrentPage() == 0 ? 0 : pageableData.getCurrentPage() * pageableData.getPageSize();
		int toIndex = pageableData.getCurrentPage() == 0 ?
				pageableData.getPageSize() :
				(pageableData.getCurrentPage() + 1) * pageableData.getPageSize();
		toIndex = toIndex > locations.size() ? locations.size() : toIndex;

		return fromIndex > toIndex ? Collections.<WarehouseModel>emptyList() : locations.subList(fromIndex, toIndex);
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

	protected GenericDao<ConsignmentModel> getConsignmentGenericDao()
	{
		return consignmentGenericDao;
	}

	@Required
	public void setConsignmentGenericDao(final GenericDao<ConsignmentModel> consignmentGenericDao)
	{
		this.consignmentGenericDao = consignmentGenericDao;
	}

	@Required
	public void setConsignmentPagedGenericDao(final PagedGenericDao<ConsignmentModel> consignmentPagedGenericDao)
	{
		this.consignmentPagedGenericDao = consignmentPagedGenericDao;
	}

	protected PagedGenericDao<ConsignmentModel> getConsignmentPagedGenericDao()
	{
		return consignmentPagedGenericDao;
	}

	@Required
	public void setConsignmentConverter(final Converter<ConsignmentModel, ConsignmentData> consignmentConverter)
	{
		this.consignmentConverter = consignmentConverter;
	}

	protected Converter<ConsignmentModel, ConsignmentData> getConsignmentConverter()
	{
		return consignmentConverter;
	}

	@Required
	public void setWarehouseConverter(final Converter<WarehouseModel, WarehouseData> warehouseConverter)
	{
		this.warehouseConverter = warehouseConverter;
	}

	protected Converter<WarehouseModel, WarehouseData> getWarehouseConverter()
	{
		return warehouseConverter;
	}

	@Required
	public void setConsignmentSearchByStatusPagedDao(
			final SearchByStatusPagedGenericDao<ConsignmentModel> consignmentSearchByStatusPagedDao)
	{
		this.consignmentSearchByStatusPagedDao = consignmentSearchByStatusPagedDao;
	}

	protected SearchByStatusPagedGenericDao<ConsignmentModel> getConsignmentSearchByStatusPagedDao()
	{
		return consignmentSearchByStatusPagedDao;
	}

	@Required
	public void setConsignmentEntryPagedDao(final PagedGenericDao consignmentEntryPagedDao)
	{
		this.consignmentEntryPagedDao = consignmentEntryPagedDao;
	}

	protected PagedGenericDao getConsignmentEntryPagedDao()
	{
		return consignmentEntryPagedDao;
	}

	@Required
	public void setConsignmentEntryConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter)
	{
		this.consignmentEntryConverter = consignmentEntryConverter;
	}

	protected Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentEntryConverter()
	{
		return consignmentEntryConverter;
	}

	@Required
	public void setWarehousingShippingService( final WarehousingShippingService warehousingShippingService)
	{
		this.warehousingShippingService = warehousingShippingService;
	}

	protected WarehousingShippingService getWarehousingShippingService()
	{
		return warehousingShippingService;
	}

	@Required
	public void setSourcingFilterProcessor(SourcingFilterProcessor sourcingFilterProcessor)
	{
		this.sourcingFilterProcessor = sourcingFilterProcessor;
	}

	protected SourcingFilterProcessor getSourcingFilterProcessor()
	{
		return sourcingFilterProcessor;
	}
}
