/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 *
 */
package de.hybris.platform.warehousingfacade.pointofservice.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseCodesDataList;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade}
 */
public class DefaultWarehousingPointOfServiceFacade extends OmsBaseFacade implements WarehousingPointOfServiceFacade
{
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private PagedGenericDao<WarehouseModel> warehousesByPointOfServicePagedDao;
	private GenericDao<PointOfServiceModel> pointOfServiceGenericDao;
	private Converter<WarehouseModel, WarehouseData> warehouseConverter;
	private Converter<AddressData, AddressModel> addressReverseConverter;
	private WarehouseService warehouseService;

	@Override
	public PointOfServiceData getPointOfServiceByName(final String posName)
	{
		return getPointOfServiceConverter().convert(getPointOfServiceModelByName(posName));
	}

	@Override
	public SearchPageData<WarehouseData> getWarehousesForPointOfService(final PageableData pageableData, final String posName)
	{
		final PointOfServiceModel pointOfService = getPointOfServiceModelByName(posName);

		final Map<String, PointOfServiceModel> params = new HashMap<>();
		params.put(WarehouseModel.POINTSOFSERVICE, pointOfService);

		final SearchPageData<WarehouseModel> warehouseSearchPage = getWarehousesByPointOfServicePagedDao()
				.find(params, pageableData);
		return convertSearchPageData(warehouseSearchPage, getWarehouseConverter());
	}

	@Override
	public PointOfServiceData updatePointOfServiceWithWarehouses(final String posName, final WarehouseCodesDataList warehouseCodes)
	{
		Assert.notNull(posName, "Point of Service name cannot be null when updating the list of warehouses.");
		Assert.isTrue(!posName.isEmpty(), "Point of Service name cannot be empty when updating the list of warehouses.");
		Assert.notNull(warehouseCodes,
				"warehouse codes cannot be null when updating PointOfService with list of Warehouses");

		final PointOfServiceModel pos = getPointOfServiceModelByName(posName);
		final List<WarehouseModel> warehouseModelsToUpdate = new ArrayList<>(pos.getWarehouses());
		if (!warehouseCodes.getCodes().isEmpty())
		{
			for (String code : warehouseCodes.getCodes())
			{
				ServicesUtil.validateParameterNotNull(getWarehouseService().getWarehouseForCode(code),
						String.format("No Warehouse found for the code: [%s]", code));
				warehouseModelsToUpdate.add(getWarehouseService().getWarehouseForCode(code));
			}

			pos.setWarehouses(warehouseModelsToUpdate);
			getModelService().save(pos);
		}

		return getPointOfServiceConverter().convert(pos);
	}

	@Override
	public PointOfServiceData deleteWarehouseFromPointOfService(final String posName, final String warehouseCode)
	{
		Assert.notNull(posName, "Point of Service name cannot be null when deleting the list of warehouses.");
		Assert.isTrue(!posName.isEmpty(), "Point of Service name cannot be empty when deleting the list of warehouses.");
		Assert.notNull(warehouseCode,
				"warehouse code cannot be null when deleting a warehouse from PointOfService");
		Assert.isTrue(!warehouseCode.isEmpty(), "Warehouse code cannot be empty when deleting the list of warehouses.");

		final PointOfServiceModel pos = getPointOfServiceModelByName(posName);

		if (!pos.getWarehouses().isEmpty())
		{
			final List<WarehouseModel> warehousesToRemove = pos.getWarehouses().stream()
					.filter(warehouse -> warehouseCode.equals(warehouse.getCode())).collect(
							Collectors.toList());

			final List<WarehouseModel> newWarehouses = new ArrayList<>(pos.getWarehouses());
			newWarehouses.removeAll(warehousesToRemove);
			pos.setWarehouses(newWarehouses);
			getModelService().save(pos);
		}

		return getPointOfServiceConverter().convert(pos);
	}

	@Override
	public PointOfServiceData updatePointOfServiceWithAddress(final String posName, final AddressData addressData)
	{
		Assert.notNull(addressData,
				"address cannot be null when updating PointOfService with a new address");
		// Create the new address model
		final PointOfServiceModel pos = getPointOfServiceModelByName(posName);
		final AddressModel newAddress = getModelService().create(AddressModel.class);
		newAddress.setOwner(pos);
		getAddressReverseConverter().convert(addressData, newAddress);
		pos.setAddress(newAddress);

		getModelService().save(pos);
		getModelService().save(newAddress);

		return getPointOfServiceConverter().convert(pos);
	}

	/**
	 * Finds {@link PointOfServiceModel} for the given {@link PointOfServiceModel#NAME}
	 *
	 * @param posName
	 * 		the pointOfServiceModel's posName
	 * @return the requested pointOfService for the given code
	 */
	protected PointOfServiceModel getPointOfServiceModelByName(final String posName)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(PointOfServiceModel.NAME, posName);

		final List<PointOfServiceModel> resultSet = getPointOfServiceGenericDao().find(params);

		if (resultSet.isEmpty())
		{
			throw new ModelNotFoundException(String.format("Could not find point of service with posName: [%s].", posName));
		}
		else if (resultSet.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					String.format("Multiple results found for point of service with posName: [%s].", posName));
		}
		return resultSet.get(0);
	}

	protected Converter<WarehouseModel, WarehouseData> getWarehouseConverter()
	{
		return warehouseConverter;
	}

	@Required
	public void setWarehouseConverter(final Converter<WarehouseModel, WarehouseData> warehouseConverter)
	{
		this.warehouseConverter = warehouseConverter;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Required
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	protected GenericDao<PointOfServiceModel> getPointOfServiceGenericDao()
	{
		return pointOfServiceGenericDao;
	}

	@Required
	public void setPointOfServiceGenericDao(final GenericDao<PointOfServiceModel> pointOfServiceGenericDao)
	{
		this.pointOfServiceGenericDao = pointOfServiceGenericDao;
	}

	protected PagedGenericDao<WarehouseModel> getWarehousesByPointOfServicePagedDao()
	{
		return warehousesByPointOfServicePagedDao;
	}

	@Required
	public void setWarehousesByPointOfServicePagedDao(
			final PagedGenericDao<WarehouseModel> warehousesByPointOfServicePagedDao)
	{
		this.warehousesByPointOfServicePagedDao = warehousesByPointOfServicePagedDao;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}


	protected Converter<AddressData, AddressModel> getAddressReverseConverter()
	{
		return addressReverseConverter;
	}

	@Required
	public void setAddressReverseConverter(final Converter<AddressData, AddressModel> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;

	}
}
