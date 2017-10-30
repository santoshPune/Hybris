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
package de.hybris.platform.warehousingwebservices.controllers.stocklevel;


import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.warehousing.enums.StockLevelAdjustmentReason;
import de.hybris.platform.warehousingfacade.product.data.StockLevelData;
import de.hybris.platform.warehousingfacade.stocklevel.WarehousingStockLevelFacade;
import de.hybris.platform.warehousingfacade.stocklevel.data.StockLevelAdjustmentData;
import de.hybris.platform.warehousingfacade.stocklevel.data.StockLevelAdjustmentReasonDataList;
import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelSearchPageWsDto;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelWsDto;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentReasonsWsDTO;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentWsDTO;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentsWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * WebResource exposing {@link WarehousingStockLevelFacade}
 * http://host:port/warehousingwebservices/stocklevels
 */
@Controller
@RequestMapping(value = "/stocklevels")
public class WarehousingStockLevelsController extends WarehousingBaseController
{
	@Resource
	private WarehousingStockLevelFacade warehousingStockLevelFacade;
	@Resource(name = "warehousingStockLevelValidator")
	private Validator warehousingStockLevelValidator;
	@Resource(name = "stockLevelAdjustmentValidator")
	private Validator stockLevelAdjustmentValidator;

	/**
	 * Request to get a {@link de.hybris.platform.ordersplitting.model.StockLevelModel} for its {@value de.hybris.platform.ordersplitting.model.WarehouseModel#CODE}
	 *
	 * @param code
	 * 		the code of the requested {@link de.hybris.platform.ordersplitting.model.WarehouseModel}
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return the list of stocklevels
	 */
	@RequestMapping(value = "/warehouses/{code}", method = RequestMethod.GET)
	@ResponseBody
	public StockLevelSearchPageWsDto getStockLevelsForWarehouseCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<StockLevelData> stockLevelSearchPageData = warehousingStockLevelFacade.getStockLevelsForWarehouseCode(code,pageableData);
		return dataMapper.map(stockLevelSearchPageData, StockLevelSearchPageWsDto.class, fields);
	}

	/**
	 * Request to create a {@link de.hybris.platform.ordersplitting.model.StockLevelModel} in the system
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param stockLevelWsDto
	 * 		object representing {@link StockLevelWsDto}
	 * @return created stockLevel
	 */
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public StockLevelWsDto createStockLevel(@RequestBody final StockLevelWsDto stockLevelWsDto,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException
	{
		validate(stockLevelWsDto, "stockLevelWsDto", warehousingStockLevelValidator);
		final StockLevelData stockLevelData = dataMapper.map(stockLevelWsDto,StockLevelData.class);
		final StockLevelData createdStockLevelData = warehousingStockLevelFacade.createStockLevel(stockLevelData);

		return dataMapper.map(createdStockLevelData,StockLevelWsDto.class,fields);
	}

	/**
	 * Request to get return stock level adjustment reasons
	 *
	 * @return list of stock level adjustment reason
	 */
	@RequestMapping(value = "/adjustment-reasons", method = RequestMethod.GET)
	@ResponseBody
	public StockLevelAdjustmentReasonsWsDTO getStockLevelAdjustmentReasons()
	{
		final List<StockLevelAdjustmentReason> stockLevelAdjustmentReasons = warehousingStockLevelFacade.getStockLevelAdjustmentReasons();
		final StockLevelAdjustmentReasonDataList stockLevelAdjustmentReasonDataList = new StockLevelAdjustmentReasonDataList();
		stockLevelAdjustmentReasonDataList.setReasons(stockLevelAdjustmentReasons);
		return dataMapper.map(stockLevelAdjustmentReasonDataList, StockLevelAdjustmentReasonsWsDTO.class);
	}

	/**
	 * Request to create a {@link de.hybris.platform.warehousing.model.InventoryEventModel} in the system to adjust a specific {@link de.hybris.platform.ordersplitting.model.StockLevelModel}
	 *
	 * @param productCode
	 * 		the product code for which an adjustment is required
	 * @param warehouseCode
	 * 		the warehouse code for which an adjustment is required
	 * @param binCode
	 * 		the bin code of the stock level to adjust (optional)
	 * @param releaseDate
	 * 		the release date of the stock level to adjust (optional)
	 * @param stockLevelAdjustmentsWsDTO
	 * 		list of stock level adjustment to be created
	 * @return created stockLevel
	 */
	@RequestMapping(value = "/product/{productCode}/warehouse/{warehouseCode}/adjustment", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public StockLevelAdjustmentsWsDTO createStockLevelAdjustment(
			@PathVariable final String productCode,
			@PathVariable final String warehouseCode,
			@RequestParam(required = false) final String binCode,
			@RequestParam(required = false) final String releaseDate,
			@RequestBody final StockLevelAdjustmentsWsDTO stockLevelAdjustmentsWsDTO)
			throws WebserviceValidationException
	{
		List<StockLevelAdjustmentData> stockLevelAdjustmentDatas = new ArrayList<>();
		stockLevelAdjustmentsWsDTO.getStockLevelAdjustmentWsDTO().stream().forEach(stockLevelAdjustmentWsDto -> {
			validate(stockLevelAdjustmentWsDto, "stockLevelAdjustmentWsDto", stockLevelAdjustmentValidator);
			stockLevelAdjustmentDatas.add(dataMapper.map(stockLevelAdjustmentWsDto, StockLevelAdjustmentData.class));
		});

		final List<StockLevelAdjustmentData> createdStockLevelAdjustmentsData = warehousingStockLevelFacade.createStockLevelAdjustements(productCode, warehouseCode, binCode, releaseDate, stockLevelAdjustmentDatas);

		final List<StockLevelAdjustmentWsDTO> stockLevelAdjustmentWsDTOs = new ArrayList<>();
		createdStockLevelAdjustmentsData.stream().forEach(stockLevelAdjustmentData -> stockLevelAdjustmentWsDTOs.add(dataMapper.map(stockLevelAdjustmentData,StockLevelAdjustmentWsDTO.class)));

		final StockLevelAdjustmentsWsDTO returnedStockLevelAdjustmentsWsDTO = new StockLevelAdjustmentsWsDTO();
		returnedStockLevelAdjustmentsWsDTO.setStockLevelAdjustmentWsDTO(stockLevelAdjustmentWsDTOs);
		return returnedStockLevelAdjustmentsWsDTO;
	}
}
