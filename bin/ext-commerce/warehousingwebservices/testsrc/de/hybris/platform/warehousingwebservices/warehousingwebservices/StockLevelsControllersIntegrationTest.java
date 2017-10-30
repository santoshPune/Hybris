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
package de.hybris.platform.warehousingwebservices.warehousingwebservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.enums.StockLevelAdjustmentReason;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.Warehouses;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelWsDto;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentWsDTO;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentsWsDTO;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseWsDto;
import de.hybris.platform.warehousingwebservices.warehousingwebservices.util.BaseWarehousingWebservicesIntegrationTest;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class StockLevelsControllersIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	private static final String BOSTON_CODE = "boston";
	private static final String INCREASE_REASON = "INCREASE";
	private static final String SHRINKAGE_REASON = "SHRINKAGE";
	private static final String WASTAGE_REASON = "WASTAGE";
	private static final String COMMENT_TEXT = "test comment";
	private StockLevelModel stockLevels_Montreal_Camera;

	@Before
	public void setup()
	{
		stockLevels.Lens(warehouses.Boston(), 5);
		stockLevels.Camera(warehouses.Boston(), 4);
		stockLevels_Montreal_Camera = stockLevels.Camera(warehouses.Montreal(), 5);
		components.warehousingComponent();
		commentTypes.adjustmentNote();
		users.Bob();
	}

	@Test
	public void testGetStockLevelsForWarehouseCode()
	{
		//When
		final Response result = getStockLevelsForWarehouseCodeByDefault(BOSTON_CODE);
		//then
		assertEquals("2",getNodeByXpath(result, "count(//stockLevelSearchPageWsDto/stockLevels)"));
	}

	@Test
	public void testPostStockLevel()
	{
		//When
		final StockLevelWsDto newStock = createStockLevelRequest(Products.CODE_CAMERA,BOSTON_CODE,10);

		final StockLevelWsDto newStockCreated = postStockLevelByDefault_WithReturnType_StockLevelWsDto(newStock);

		//then
		assertEquals(10L,newStockCreated.getInitialQuantityOnHand().longValue());
	}

	@Test
	public void testPostStockLevelEmptyProductCode()
	{
		//When
		final StockLevelWsDto newStock = createStockLevelRequest(null,BOSTON_CODE,10);

		final Response response = postStockLevelByDefault(newStock);

		//then
		assertBadRequest(response, false);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(errors.getErrors().size(), 1);
		final ErrorWsDTO error = errors.getErrors().get(0);
		assertEquals(error.getReason(), "missing");
		assertEquals(error.getSubject(), "productCode");
		assertEquals(error.getSubjectType(), "parameter");
	}

	@Test
	public void testPostStockLevelNegativeInitialOnHandQuantity()
	{
		//When
		final StockLevelWsDto newStock = createStockLevelRequest(Products.CODE_CAMERA,BOSTON_CODE,-5);

		final Response response = postStockLevelByDefault(newStock);

		//then
		assertBadRequest(response, false);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(errors.getErrors().size(), 1);
		final ErrorWsDTO error = errors.getErrors().get(0);
		assertEquals(error.getReason(), "invalid");
		assertEquals(error.getSubject(), "initialQuantityOnHand");
		assertEquals(error.getSubjectType(), "parameter");
	}

	@Test
	public void testPostStockLevelEmptyWarehouseCode()
	{
		//When
		final StockLevelWsDto newStock = createStockLevelRequest(Products.CODE_CAMERA,null,10);

		final Response response = postStockLevelByDefault(newStock);

		//then
		assertBadRequest(response, false);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(errors.getErrors().size(), 1);
		final ErrorWsDTO error = errors.getErrors().get(0);
		assertEquals(error.getReason(), "missing");
		assertEquals(error.getSubject(), "warehouse.code");
		assertEquals(error.getSubjectType(), "parameter");
	}

	@Test
	public void getAllStockLevelAdjustmentReasons()
	{
		//When
		final Response result = getStockLevelAdjustmentReasons();
		//then
		assertEquals("3", getNodeByXpath(result, "count(//stockLevelAdjustmentReasons/reasons)"));
		List<StockLevelAdjustmentReason> adjustmentReasonList = getEnumerationService().getEnumerationValues(StockLevelAdjustmentReason._TYPECODE);
		assertTrue(adjustmentReasonList.stream().anyMatch(r -> r.name().equals(getNodeByXpath(result, "//stockLevelAdjustmentReasons/reasons"))));
	}

	@Test
	public void postCreateStockLevelAdjustment()
	{
		//When
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = createStockLevelAdjustmentWsDTO(INCREASE_REASON, 3);

		final StockLevelAdjustmentsWsDTO response = postStockLevelAdjustmentByDefault(Products.CODE_CAMERA, Warehouses.CODE_MONTREAL, createStockLevelAdjustmentsWsDTO(stockLevelAdjustmentWsDTO));

		//then
		assertEquals(3L, response.getStockLevelAdjustmentWsDTO().iterator().next().getQuantity().longValue());
		assertEquals(INCREASE_REASON, response.getStockLevelAdjustmentWsDTO().iterator().next().getReason());
	}

	@Test
	public void postCreateMultiStockLevelAdjustment()
	{
		//When
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = createStockLevelAdjustmentWsDTO(INCREASE_REASON, 3);
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO2 = createStockLevelAdjustmentWsDTO(SHRINKAGE_REASON, 2);
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO3 = createStockLevelAdjustmentWsDTO(WASTAGE_REASON, 1);

		final StockLevelAdjustmentsWsDTO response = postStockLevelAdjustmentByDefault(Products.CODE_CAMERA, Warehouses.CODE_MONTREAL, createStockLevelAdjustmentsWsDTO(stockLevelAdjustmentWsDTO, stockLevelAdjustmentWsDTO2, stockLevelAdjustmentWsDTO3));

		//then
		response.getStockLevelAdjustmentWsDTO().stream().anyMatch(e -> 3L == e.getQuantity().longValue() && INCREASE_REASON.equals(e.getReason()));
		response.getStockLevelAdjustmentWsDTO().stream().anyMatch(e -> 1L == e.getQuantity().longValue() && WASTAGE_REASON.equals(e.getReason()));
	}

	@Test
	public void postCreateStockLevelAdjustment_WithComment()
	{
		//When
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = createStockLevelAdjustmentWsDTO(INCREASE_REASON, 3, COMMENT_TEXT);

		final StockLevelAdjustmentsWsDTO response = postStockLevelAdjustmentByDefault(Products.CODE_CAMERA, Warehouses.CODE_MONTREAL, createStockLevelAdjustmentsWsDTO(stockLevelAdjustmentWsDTO));

		//then
		assertEquals(3L, response.getStockLevelAdjustmentWsDTO().iterator().next().getQuantity().longValue());
		assertEquals(INCREASE_REASON, response.getStockLevelAdjustmentWsDTO().iterator().next().getReason());
		assertEquals(COMMENT_TEXT, response.getStockLevelAdjustmentWsDTO().iterator().next().getComment());
	}

	@Test
	public void postCreateStockLevelAdjustment_WithBin()
	{
		//When
		stockLevels_Montreal_Camera.setBin("4");
		modelService.save(stockLevels_Montreal_Camera);
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = createStockLevelAdjustmentWsDTO(INCREASE_REASON, 3, COMMENT_TEXT);
		final StockLevelAdjustmentsWsDTO stockLevelAdjustmentsWsDTO = createStockLevelAdjustmentsWsDTO(stockLevelAdjustmentWsDTO);

		final StockLevelAdjustmentsWsDTO response = webResource
				.path(STOCKLEVELS +"/product/" + Products.CODE_CAMERA + "/warehouse/" + Warehouses.CODE_MONTREAL + "/adjustment")
				.queryParam("binCode", 4)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(stockLevelAdjustmentsWsDTO, MediaType.APPLICATION_JSON), StockLevelAdjustmentsWsDTO.class);
		//then
		assertEquals(3L, response.getStockLevelAdjustmentWsDTO().iterator().next().getQuantity().longValue());
		assertEquals(INCREASE_REASON, response.getStockLevelAdjustmentWsDTO().iterator().next().getReason());
		assertEquals(COMMENT_TEXT, response.getStockLevelAdjustmentWsDTO().iterator().next().getComment());
	}

	@Test
	public void postCreateStockLevelAdjustment_WithReleaseDate()
	{
		//given
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		stockLevels_Montreal_Camera.setReleaseDate(date);
		modelService.save(stockLevels_Montreal_Camera);
		//when
		final StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = createStockLevelAdjustmentWsDTO(INCREASE_REASON, 3, COMMENT_TEXT);
		final StockLevelAdjustmentsWsDTO stockLevelAdjustmentsWsDTO = createStockLevelAdjustmentsWsDTO(stockLevelAdjustmentWsDTO);

		final StockLevelAdjustmentsWsDTO response = webResource
				.path(STOCKLEVELS +"/product/" + Products.CODE_CAMERA + "/warehouse/" + Warehouses.CODE_MONTREAL + "/adjustment")
				.queryParam("releaseDate", dateFormat.format(date))
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(stockLevelAdjustmentsWsDTO, MediaType.APPLICATION_JSON), StockLevelAdjustmentsWsDTO.class);
		//then
		assertEquals(3L, response.getStockLevelAdjustmentWsDTO().iterator().next().getQuantity().longValue());
		assertEquals(INCREASE_REASON, response.getStockLevelAdjustmentWsDTO().iterator().next().getReason());
		assertEquals(COMMENT_TEXT, response.getStockLevelAdjustmentWsDTO().iterator().next().getComment());
	}

	/**
	 * Populates a {@link StockLevelWsDto} for a POST call, to add a StockLevel in the system
	 *
	 * @param productCode
	 * @param warehouseCode
	 * @param initialQuantityOnHand
	 * @return
	 */
	protected StockLevelWsDto createStockLevelRequest(final String productCode,  final String warehouseCode, final Integer initialQuantityOnHand)
	{
		final WarehouseWsDto warehouseWsDto = new WarehouseWsDto();
		warehouseWsDto.setCode(warehouseCode);

		final StockLevelWsDto stockLevelWsDto = new StockLevelWsDto();
		stockLevelWsDto.setProductCode(productCode);
		stockLevelWsDto.setInitialQuantityOnHand(initialQuantityOnHand);
		stockLevelWsDto.setWarehouse(warehouseWsDto);

		return stockLevelWsDto;
	}

	/**
	 * Populates a {@link StockLevelAdjustmentWsDTO} for a POST call, to add a StockLevel adjustment in the system
	 * @param reason
	 * @param quantity
	 * @param comment
	 * @return StockLevelAdjustmentWsDTO
	 */
	protected StockLevelAdjustmentWsDTO createStockLevelAdjustmentWsDTO(final String reason,  final long quantity, final String comment)
	{

		StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = new StockLevelAdjustmentWsDTO();
		stockLevelAdjustmentWsDTO.setComment(comment);
		stockLevelAdjustmentWsDTO.setReason(reason);
		stockLevelAdjustmentWsDTO.setQuantity(quantity);
		return stockLevelAdjustmentWsDTO;
	}

	/**
	 * Populates a {@link StockLevelAdjustmentWsDTO} for a POST call, to add a StockLevel adjustment in the system
	 * @param reason
	 * @param quantity
	 * @return StockLevelAdjustmentWsDTO
	 */
	protected StockLevelAdjustmentWsDTO createStockLevelAdjustmentWsDTO(final String reason,  final long quantity)
	{

		StockLevelAdjustmentWsDTO stockLevelAdjustmentWsDTO = new StockLevelAdjustmentWsDTO();
		stockLevelAdjustmentWsDTO.setReason(reason);
		stockLevelAdjustmentWsDTO.setQuantity(quantity);
		return stockLevelAdjustmentWsDTO;
	}

	/**
	 * Populates a {@link StockLevelAdjustmentsWsDTO} for a POST call, to add a list of StockLevelAdjustmentWsDTO
	 * @param stockLevelAdjustmentWsDTO
	 * @return
	 */
	protected StockLevelAdjustmentsWsDTO createStockLevelAdjustmentsWsDTO(StockLevelAdjustmentWsDTO ... stockLevelAdjustmentWsDTO)
	{
		List<StockLevelAdjustmentWsDTO> stockLevelAdjustmentWsDTOs = new ArrayList<>();
		for (StockLevelAdjustmentWsDTO s :stockLevelAdjustmentWsDTO) stockLevelAdjustmentWsDTOs.add(s);
		StockLevelAdjustmentsWsDTO stockLevelAdjustmentsWsDTO = new StockLevelAdjustmentsWsDTO();
		stockLevelAdjustmentsWsDTO.setStockLevelAdjustmentWsDTO(stockLevelAdjustmentWsDTOs);
		return stockLevelAdjustmentsWsDTO;
	}
}
