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
package de.hybris.platform.warehousingfacade.stocklevel.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;
import de.hybris.platform.warehousing.enums.StockLevelAdjustmentReason;
import de.hybris.platform.warehousing.model.InventoryEventModel;
import de.hybris.platform.warehousing.stock.services.WarehouseStockService;
import de.hybris.platform.warehousing.stock.strategies.StockLevelSelectionStrategy;
import de.hybris.platform.warehousingfacade.product.data.StockLevelData;
import de.hybris.platform.warehousingfacade.stocklevel.WarehousingStockLevelFacade;
import de.hybris.platform.warehousingfacade.stocklevel.data.StockLevelAdjustmentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


/**
 * Default implementation of {@link WarehousingStockLevelFacade}
 */
public class DefaultWarehousingStockLevelFacade extends OmsBaseFacade implements WarehousingStockLevelFacade
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehousingStockLevelFacade.class);

	protected static final String ADJUSTMENT_COMMENT_SUBJECT = "Stock level adjustment";

	private PagedGenericDao<StockLevelModel> stockLevelPagedGenericDao;
	private Converter<StockLevelModel, StockLevelData> stockLevelConverter;
	private WarehouseService warehouseService;
	private WarehouseStockService warehouseStockService;
	private EnumerationService enumerationService;
	private WarehousingCommentService<StockLevelModel> stockLevelCommentService;
	private StockLevelSelectionStrategy stockLevelSelectionStrategy;
	private GuidKeyGenerator guidKeyGenerator;

	private Map<StockLevelAdjustmentReason, Class> reasonMapper;

	@Override
	public SearchPageData<StockLevelData> getStockLevelsForWarehouseCode(final String code,
			final PageableData pageableData)
	{
		final Map<String, WarehouseModel> warehouseParams = new HashMap<>();
		warehouseParams.put(StockLevelModel.WAREHOUSE, getWarehouseService().getWarehouseForCode(code));

		SearchPageData<StockLevelModel> stockLevelModelSearchPageData = getStockLevelPagedGenericDao()
				.find(warehouseParams, pageableData);
		return convertSearchPageData(stockLevelModelSearchPageData, getStockLevelConverter());
	}

	@Override
	public StockLevelData createStockLevel(final StockLevelData stockLevelData)
	{
		validateStockLevelData(stockLevelData);
		final String warehouseCode = stockLevelData.getWarehouse().getCode();
		final WarehouseModel warehouse = getWarehouseService().getWarehouseForCode(warehouseCode);
		ServicesUtil.validateParameterNotNull(warehouse, String.format("No Warehouse found for the code: [%s]", warehouseCode));

		final StockLevelModel stockLevel = getWarehouseStockService()
				.createStockLevel(stockLevelData.getProductCode(), warehouse, stockLevelData.getInitialQuantityOnHand(),
						stockLevelData.getInStockStatus(),
						stockLevelData.getReleaseDate(), stockLevelData.getBin());
		return getStockLevelConverter().convert(stockLevel);
	}

	@Override
	public List<StockLevelAdjustmentReason> getStockLevelAdjustmentReasons()
	{
		return getEnumerationService().getEnumerationValues(StockLevelAdjustmentReason._TYPECODE);
	}

	@Override
	public List<StockLevelAdjustmentData> createStockLevelAdjustements(final String productCode, final String warehouseCode,
			final String binCode, final String releaseDate,
			final List<StockLevelAdjustmentData> stockLevelAdjustmentDatas)
	{
		validateStockLevelAdjustments(productCode, stockLevelAdjustmentDatas);

		final StockLevelModel stockLevelToAdjust = getStockLevelToAdjust(productCode, warehouseCode, binCode, releaseDate);

		stockLevelAdjustmentDatas.stream().forEach(stockLevelAdjustmentData -> {
			createStockLevelAdjustment(stockLevelToAdjust, stockLevelAdjustmentData);
		});

		return stockLevelAdjustmentDatas;
	}

	/**
	 * Validates the release date if provided using the format dd/MM/yyyy and English locale and retrieves a unique {@link StockLevelModel} to adjust. If the result is not unique, then ask for more precise details.
	 *
	 * @param productCode
	 * 		the product code of the product for which adjustments are required
	 * @param warehouseCode
	 * 		the warehouse code for which adjustments are required
	 * @param binCode
	 * 		the bin code of the stock level for which adjustments are required
	 * @param releaseDate
	 * 		the release date for which adjustments are required
	 * @return {@link StockLevelModel} to adjust
	 */
	protected StockLevelModel getStockLevelToAdjust(final String productCode, final String warehouseCode, final String binCode,
			final String releaseDate)
	{
		Date formattedDate = null;
		try
		{
			if (releaseDate != null)
			{
				formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(releaseDate);
			}
		}
		catch (ParseException e)
		{
			Assert.isTrue(false, "Provided release date cannot be interpreted. It must be using the format dd/MM/yyyy");
		}

		return getWarehouseStockService()
				.getUniqueStockLevel(productCode, warehouseCode, binCode, formattedDate);
	}

	/**
	 * Validates the stock levels adjustments to be created.
	 *
	 * @param productCode
	 * 		the product code of the product for which adjustments are required
	 * @param stockLevelAdjustmentDatas
	 * 		the list of stock level adjustements to be created
	 */
	protected void validateStockLevelAdjustments(final String productCode,
			final List<StockLevelAdjustmentData> stockLevelAdjustmentDatas)
	{
		Assert.notNull(productCode, "Product code cannot be null");
		Assert.notNull(stockLevelAdjustmentDatas, "Stock level adjustment list cannot be null");
		Assert.notEmpty(stockLevelAdjustmentDatas, "Stock level adjustment list cannot be empty");

		final Set<StockLevelAdjustmentReason> reasonsToCreate = new HashSet<>();
		stockLevelAdjustmentDatas.stream().forEach(stockLevelAdjustmentData -> {
			validateStockLevelAdjustmentData(stockLevelAdjustmentData);
			Assert.isTrue(!reasonsToCreate.contains(stockLevelAdjustmentData.getReason()),
					"Only one of each reasons can be created at once");
			reasonsToCreate.add(stockLevelAdjustmentData.getReason());
		});
	}

	/**
	 * Creates a specific {@link StockLevelAdjustmentData}
	 *
	 * @param stockLevelToAdjust
	 * 		{@link StockLevelModel} to adjust
	 * @param stockLevelAdjustmentData
	 * 		The {@link StockLevelAdjustmentData} to create
	 */
	public void createStockLevelAdjustment(final StockLevelModel stockLevelToAdjust,
			final StockLevelAdjustmentData stockLevelAdjustmentData)
	{
		try
		{
			final InventoryEventModel adjustment = (InventoryEventModel) reasonMapper.get(stockLevelAdjustmentData.getReason())
					.newInstance();
			adjustment.setStockLevel(stockLevelToAdjust);
			adjustment.setQuantity(stockLevelAdjustmentData.getQuantity());
			if (stockLevelAdjustmentData.getComment() != null)
			{
				final WarehousingCommentContext commentContext = new WarehousingCommentContext();
				commentContext.setCommentType(WarehousingCommentEventType.INVENTORY_ADJUSTMENT_COMMENT);
				commentContext.setItem(stockLevelToAdjust);
				commentContext.setSubject(ADJUSTMENT_COMMENT_SUBJECT);
				commentContext.setText(stockLevelAdjustmentData.getComment());

				final String code = "adjustment_" + getGuidKeyGenerator().generate().toString();
				CommentModel comment = getStockLevelCommentService().createAndSaveComment(commentContext, code);
				adjustment.setComments(Lists.newArrayList(comment));
			}
			getModelService().save(adjustment);
		}
		catch (InstantiationException | IllegalAccessException e) //NOSONAR
		{
			LOGGER.error("Cannot find an inventory event which matches the provided class.");
		}
	}

	/**
	 * Validates for null check and mandatory fields in {@link StockLevelAdjustmentData}
	 *
	 * @param stockLevelAdjustmentData
	 * 		{@link StockLevelAdjustmentData} to be validated
	 */
	protected void validateStockLevelAdjustmentData(final StockLevelAdjustmentData stockLevelAdjustmentData)
	{
		Assert.notNull(stockLevelAdjustmentData.getReason(), "Stock level adjustment reason cannot be null");
		Assert.notNull(stockLevelAdjustmentData.getQuantity(), "Stock level adjustment quantity cannot be null");
	}

	/**
	 * Validates for null check and mandatory fields in {@link StockLevelData}
	 *
	 * @param stockLevelData
	 * 		{@link StockLevelData} to be validated
	 */
	protected void validateStockLevelData(final StockLevelData stockLevelData)
	{
		Assert.notNull(stockLevelData.getProductCode(), "ProductCode cannot be null for StockLevel");
		Assert.notNull(stockLevelData.getWarehouse(), "Warehouse cannot be null for StockLevel");
		Assert.isTrue(stockLevelData.getInitialQuantityOnHand() != null && stockLevelData.getInitialQuantityOnHand() > 0,
				"initialQuantityOnHand cannot be null or negative for StockLevel");
	}

	protected PagedGenericDao<StockLevelModel> getStockLevelPagedGenericDao()
	{
		return stockLevelPagedGenericDao;
	}

	@Required
	public void setStockLevelPagedGenericDao(final PagedGenericDao<StockLevelModel> stockLevelPagedGenericDao)
	{
		this.stockLevelPagedGenericDao = stockLevelPagedGenericDao;
	}

	protected Converter<StockLevelModel, StockLevelData> getStockLevelConverter()
	{
		return stockLevelConverter;
	}

	@Required
	public void setStockLevelConverter(final Converter<StockLevelModel, StockLevelData> stockLevelConverter)
	{
		this.stockLevelConverter = stockLevelConverter;
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

	protected WarehouseStockService getWarehouseStockService()
	{
		return warehouseStockService;
	}

	@Required
	public void setWarehouseStockService(final WarehouseStockService warehouseStockService)
	{
		this.warehouseStockService = warehouseStockService;
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

	protected StockLevelSelectionStrategy getStockLevelSelectionStrategy()
	{
		return stockLevelSelectionStrategy;
	}

	@Required
	public void setStockLevelSelectionStrategy(StockLevelSelectionStrategy stockLevelSelectionStrategy)
	{
		this.stockLevelSelectionStrategy = stockLevelSelectionStrategy;
	}

	protected Map<StockLevelAdjustmentReason, Class> getReasonMapper()
	{
		return reasonMapper;
	}

	@Required
	public void setReasonMapper(Map<StockLevelAdjustmentReason, Class> reasonMapper)
	{
		this.reasonMapper = reasonMapper;
	}

	protected GuidKeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	@Required
	public void setGuidKeyGenerator(GuidKeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}

	protected WarehousingCommentService<StockLevelModel> getStockLevelCommentService()
	{
		return stockLevelCommentService;
	}

	@Required
	public void setStockLevelCommentService(WarehousingCommentService<StockLevelModel> stockLevelCommentService)
	{
		this.stockLevelCommentService = stockLevelCommentService;
	}
}
