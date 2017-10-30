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

package de.hybris.platform.warehousingwebservices.warehousingwebservices.util;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.commerceservices.event.CreateReturnEvent;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnActionResponse;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.returns.service.impl.WarehousingReturnService;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.BaseSites;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.CommentTypes;
import de.hybris.platform.warehousing.util.models.Components;
import de.hybris.platform.warehousing.util.models.DeliveryModes;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelWsDto;
import de.hybris.platform.warehousingwebservices.dto.stocklevel.StockLevelAdjustmentsWsDTO;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseCodesWsDto;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;


@IntegrationTest
public class BaseWarehousingWebservicesIntegrationTest extends BaseWebservicesIntegrationTest
{
	@Resource
	protected SourcingService sourcingService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected Orders orders;
	@Resource
	protected BaseStores baseStores;
	@Resource
	protected Warehouses warehouses;
	@Resource
	protected Addresses addresses;
	@Resource
	protected StockLevels stockLevels;
	@Resource
	protected PointsOfService pointsOfService;
	@Resource
	protected Products products;
	@Resource
	protected AllocationService allocationService;
	@Resource
	protected WarehousingReturnService warehousingReturnService;
	@Resource
	protected Users users;
	@Resource
	protected DeliveryModes deliveryModes;
	@Resource
	protected EventService eventService;
	@Resource
	protected ReturnCallbackService returnCallbackService;
	@Resource
	protected BaseSites baseSites;
	@Resource
	protected Components components;
	@Resource
	protected CommentTypes commentTypes;

	protected static final String DEFAULT_FIELDS = "DEFAULT";
	protected static final String DEFAULT_CURRENT_PAGE = "0";
	protected static final String DEFAULT_PAGE_SIZE = "100";
	protected static final String STOCKLEVELS = "stocklevels";
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseWarehousingWebservicesIntegrationTest.class);

	private int timeOut = 4;

	@Before
	public void setupShopper()
	{
		users.Nancy();
	}

	@Before
	public void setupBaseStore()
	{
		baseStores.NorthAmerica().setPointsOfService(Lists.newArrayList( //
				pointsOfService.Boston(), //
				pointsOfService.Montreal_Downtown() //
		));
		saveAll();
	}

	/**
	 * Saves any unsaved models.
	 */
	protected void saveAll()
	{
		modelService.saveAll();
	}

	@Before
	public void setup()
	{
	}

	protected Response getStockLevelsForWarehouseCodeByDefault(final String code)
	{
		return getDefaultRestCall("stocklevels/warehouses/" + code, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getAllWarehousesByDefault()
	{
		return getDefaultRestCall("basestores/north-america/warehouses", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getAllOrderByDefault()
	{
		return getDefaultRestCall("orders", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getAllConsignmentsByDefault()
	{
		return getDefaultRestCall("consignments", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getWarehouseByDefault(final String code)
	{
		return getDefaultRestCall("warehouses/" + code, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getConsignmentsForCodeByDefault(final String code)
	{
		return getDefaultRestCall("consignments/" + code, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getConsignmentStatusByDefault()
	{
		return getDefaultRestCall("consignments/statuses", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getDeclineReasonsByDefault()
	{
		return getDefaultRestCall("consignments/decline-reasons", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getConsignmentEntriesByDefault(final String code)
	{
		return getDefaultRestCall("consignments/" + code + "/entries", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getSourcingLocationsByDefault(final String code)
	{
		return getDefaultRestCall("consignments/" + code + "/sourcing-locations", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE,
				DEFAULT_PAGE_SIZE);
	}

	protected Response getPointOfServiceByDefault(final String name)
	{
		return getDefaultRestCall("/pointofservices/" + name, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getWarehouseForPointOfServiceByDefault(final String pointOfService)
	{
		return getDefaultRestCall("/pointofservices/" + pointOfService + "/warehouses", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE,
				DEFAULT_PAGE_SIZE);
	}

	protected PointOfServiceWsDTO postUpdatePointOfServiceWarehouses(final String name, final WarehouseCodesWsDto warehouseCodesWsDTO)
	{
		return postDefaultRestCall("/pointofservices/" + name + "/warehouses", DEFAULT_FIELDS,
				warehouseCodesWsDTO, PointOfServiceWsDTO.class);
	}

	protected Response deleteWarehousesFromPointOfService(final String name, final String warehouseCode)
	{
		return deleteDefaultRestCall("/pointofservices/" + name + "/warehouses/" + warehouseCode, DEFAULT_FIELDS);
	}

	protected PointOfServiceWsDTO putUpdatePointOfServiceAddress(final String pointOfService, final AddressWsDTO addressWsDTO)
	{
		return putDefaultRestCall("/pointofservices/" + pointOfService + "/address", DEFAULT_FIELDS, addressWsDTO,
				PointOfServiceWsDTO.class);
	}

	protected Response postAcceptGoodsByDefault(final String code)
	{
		return postDefaultRestCall("returns/" + code + "/acceptGoods", DEFAULT_FIELDS, null);
	}

	protected Response postStockLevelByDefault(StockLevelWsDto newStock)
	{
		return postDefaultRestCall(STOCKLEVELS, DEFAULT_FIELDS, newStock);
	}

	protected StockLevelAdjustmentsWsDTO postStockLevelAdjustmentByDefault(final String productCode, final String warehouseCode, final StockLevelAdjustmentsWsDTO stockLevelAdjustmentsWsDTO)
	{
		return postDefaultRestCall(STOCKLEVELS +"/product/" + productCode + "/warehouse/" + warehouseCode + "/adjustment", DEFAULT_FIELDS, stockLevelAdjustmentsWsDTO, StockLevelAdjustmentsWsDTO.class);
	}

	protected StockLevelWsDto postStockLevelByDefault_WithReturnType_StockLevelWsDto(StockLevelWsDto newStock)
	{
		return postDefaultRestCall(STOCKLEVELS, DEFAULT_FIELDS, newStock, StockLevelWsDto.class);
	}
	protected Response getStockLevelAdjustmentReasons()
	{
		return getDefaultRestCall(STOCKLEVELS + "/adjustment-reasons", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	/**
	 * Builds a GET rest call.
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param currentPage
	 * 			the current page of the request
	 * @param pageSize
	 * 			total page size
	 * @return the result of the call
	 */
	protected Response getDefaultRestCall(String path, String fields, String currentPage, String pageSize)
	{
		final Response result = webResource
				.path(path)
				.queryParam("fields", fields)
				.queryParam("currentPage", currentPage)
				.queryParam("pageSize", pageSize)
				.request().accept(MediaType.APPLICATION_XML).get();
		result.bufferEntity();
		return result;
	}

	/**
	 * Builds a POST rest call.
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param <T>
	 * @return the result of the call
	 */
	protected <T> Response postDefaultRestCall(String path, String fields, T requestBodyWsDTO)
	{
		final Response result = webResource
				.path(path)
				.queryParam("fields", fields)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		return result;
	}

	/**
	 * this method is to build the rest call post with the return type <T>
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param <T>
	 *    		the current dto which is to be updated
	 * @param responseType
	 * 			type of class to return
	 * @param <T>
	 * @return the request class to return after the execution of the call
	 */
	protected <S, T> T postDefaultRestCall(String path, String fields, S requestBodyWsDTO, Class<T> responseType)
	{
		return webResource
				.path(path)
				.queryParam("fields", fields)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON), responseType);
	}

	/**
	 * Builds a PUT rest call
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param requestBodyWsDTO
	 * 			the current dto which is to be updated
	 * @param responseType
	 * 			type of class to return
	 * @param <T>
	 * @return the request class to return after the execution of the call
	 */
	protected <S, T> T putDefaultRestCall(String path, String fields, S requestBodyWsDTO, Class<T> responseType)
	{
		return webResource
				.path(path)
				.queryParam("fields", fields)
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON), responseType);
	}

	/**
	 * Builds a DELETE rest call
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @return the request class to return after the execution of the call
	 */
	protected Response deleteDefaultRestCall(String path, String fields)
	{
		return webResource
				.path(path)
				.queryParam("fields", fields)
				.request()
				.delete();
	}

	/**
	 * Validates the response in xpath.
	 *
	 * @param result
	 * 			the result of a rest call
	 * @param xpath
	 * 			the path to validate the result
	 * @return the validated result
	 */
	protected String getNodeByXpath(Response result, String xpath)
	{
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document document = db.parse(new InputSource(new StringReader(result.readEntity(String.class))));
			final XPathFactory xpf = XPathFactory.newInstance();
			final XPath xp = xpf.newXPath();
			return xp.evaluate(xpath, document.getDocumentElement());
		}
		catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a default order and consignment.
	 *
	 * @return OrderModel the newly created order
	 */
	protected OrderModel createConsignmentAndOrder()
	{
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.Camera(warehouses.Boston(), 4);
		final OrderModel order = orders.Camera_Shipped(7L);
		final SourcingResults results = sourcingService.sourceOrder(order);
		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, "con", results);
		order.setStatus(OrderStatus.COMPLETED);
		consignmentResult.stream().forEach(result -> result.setStatus(ConsignmentStatus.SHIPPED));
				modelService.saveAll();
		return order;
	}

	/**
	 * Creates a default return request and approves it.
	 *
	 * @param order
	 * 			the order with which to create the return request
	 * @return RefundEntryModel the created refund entry
	 */
	protected RefundEntryModel createApprovedReturnRequest(final OrderModel order)
	{
		//when
		final ReturnRequestModel request = warehousingReturnService.createReturnRequest(order);
		final RefundEntryModel refundEntry = warehousingReturnService.createRefund(request, order.getEntries().get(0),"",1L, ReturnAction.HOLD, RefundReason.DAMAGEDINTRANSIT);
		final CreateReturnEvent createReturnEvent = new CreateReturnEvent();
		createReturnEvent.setReturnRequest(request);
		getEventService().publishEvent(createReturnEvent);
		try
		{
			waitForReturnProcessComplete(request.getReturnProcess());
			getReturnCallbackService().onReturnApprovalResponse(new ReturnActionResponse(request));
			waitForReturnProcessComplete(request.getReturnProcess());
		}
		catch (OrderReturnException e)
		{
			LOGGER.info("Error happened during approval for the return request [%s]" ,request.getRMA());
		}
		modelService.saveAll();
		return refundEntry;
	}

	/**
	 * Waits for the process to complete before the time out.
	 *
	 * @param returnProcessModels
	 * 			a collection of return processes
	 */
	protected void waitForReturnProcessComplete(final Collection<ReturnProcessModel> returnProcessModels)
	{
		int timeCount = 0;
		do
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				LOGGER.info("Error happened during Thread.sleep(1000)");
			}
			modelService.refresh(returnProcessModels.iterator().next());

		}
		while (ProcessState.RUNNING.equals(returnProcessModels.iterator().next().getProcessState()) && timeCount++ < timeOut);
	}

	/**
	 * Creates a US address.
	 *
	 * @return the newly created address
	 */
	protected AddressWsDTO createUsAddress()
	{
		final AddressWsDTO addressWsDTO = new AddressWsDTO();
		addressWsDTO.setTown("New York");
		addressWsDTO.setLine1("5th Avenue");
		addressWsDTO.setPostalCode("79777");
		final CountryWsDTO countryWsDTO = new CountryWsDTO();
		countryWsDTO.setIsocode("US");
		addressWsDTO.setCountry(countryWsDTO);
		return addressWsDTO;
	}

	/**
	 * Creates a return request in a {@link de.hybris.platform.basecommerce.enums.ReturnStatus.WAIT}.
	 *
	 * @return RefundEntryModel the created return request
	 */
	protected RefundEntryModel createReturnAndReadyToAcceptGoods()
	{
		final RefundEntryModel refundEntry = createApprovedReturnRequest(createConsignmentAndOrder());
		modelService.saveAll();
		return refundEntry;
	}

	public EventService getEventService()
	{
		return eventService;
	}

	public ReturnCallbackService getReturnCallbackService()
	{
		return returnCallbackService;
	}

	public void setReturnCallbackService(final ReturnCallbackService returnCallbackService)
	{
		this.returnCallbackService = returnCallbackService;
	}
}
