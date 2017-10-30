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

package de.hybris.platform.ordermanagementwebservices.returns;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordermanagementwebservices.dto.returns.CancelReturnRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnEntryWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.util.BaseOrderManagementWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



@IntegrationTest
public class ReturnsControllerIntegrationTest extends BaseOrderManagementWebservicesIntegrationTest
{
	private static final String RETURNS = "returns";

	@Before
	public void setup()
	{
		try
		{
			importCsv("/test/OrderTestData.csv", "UTF-8");
		}
		catch (ImpExException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void getAllReturnCancellationReasons()
	{
		//When
		final Response result = getReturnCancellationReasons();
		//then
		assertEquals("6", getNodeByXpath(result, "count(//cancelReasonList/reasons)"));
		final List<CancelReason> cancelReasonList = getEnumerationService().getEnumerationValues(CancelReason._TYPECODE);

		assertTrue(cancelReasonList.stream().anyMatch(r -> r.getCode().equals(getNodeByXpath(result, "//cancelReasonList/reasons"))));
	}

	@Test
	public void testPostReturnRequest_ValidQtyReturn_Success()
	{
		//Given
		final ReturnEntryWsDTO returnEntryWsDTO1 = createReturnEntryWsDTO(1L,"HOLD",null,"DamagedInTransit",0);
		final ReturnEntryWsDTO returnEntryWsDTO2 = createReturnEntryWsDTO(1L,"HOLD",null,"DamagedInTransit",1);
		final List<ReturnEntryWsDTO> returnEntriesWsDTO = Arrays.asList(returnEntryWsDTO1,returnEntryWsDTO2);
		final ReturnRequestWsDTO returnRequestWsDTO = createReturnRequestWsDTO(returnEntriesWsDTO,"O-K2010-C0000-001",Boolean.FALSE);

		//When
		final ReturnRequestWsDTO createdReturnRequest = postDefaultRestCall(RETURNS, DEFAULT_FIELDS, returnRequestWsDTO, ReturnRequestWsDTO.class);

		//then
		assertNotNull(createdReturnRequest.getRma());
		assertEquals(ReturnStatus.APPROVAL_PENDING.toString(),createdReturnRequest.getStatus());
	}

	@Test
	public void testApproveReturnRequest_Success()
	{
		//Given
		final ReturnEntryWsDTO returnEntryWsDTO1 = createReturnEntryWsDTO(1L,"HOLD",null,"DamagedInTransit",0);
		final ReturnEntryWsDTO returnEntryWsDTO2 = createReturnEntryWsDTO(1L,"HOLD",null,"DamagedInTransit",1);
		final List<ReturnEntryWsDTO> returnEntriesWsDTO = Arrays.asList(returnEntryWsDTO1,returnEntryWsDTO2);
		final ReturnRequestWsDTO returnRequestWsDTO = createReturnRequestWsDTO(returnEntriesWsDTO,"O-K2010-C0000-001",Boolean.FALSE);
		final ReturnRequestWsDTO createdReturnRequest = postDefaultRestCall(RETURNS, DEFAULT_FIELDS, returnRequestWsDTO, ReturnRequestWsDTO.class);

		//When
		final Response response = postEmptyBodyRestCall(RETURNS+"/"+createdReturnRequest.getRma()+"/approve");
		response.bufferEntity();

		//then
		assertOk(response, true);

	}

	@Test
	public void testCancelReturnRequest_Success()
	{
		//Given
		final ReturnEntryWsDTO returnEntryWsDTO1 = createReturnEntryWsDTO(1L, "HOLD", null, "DamagedInTransit", 0);
		final ReturnEntryWsDTO returnEntryWsDTO2 = createReturnEntryWsDTO(1L, "HOLD", null, "DamagedInTransit", 1);
		final List<ReturnEntryWsDTO> returnEntriesWsDTO = Arrays.asList(returnEntryWsDTO1, returnEntryWsDTO2);
		final ReturnRequestWsDTO returnRequestWsDTO = createReturnRequestWsDTO(returnEntriesWsDTO, "O-K2010-C0000-001",
				Boolean.FALSE);
		final ReturnRequestWsDTO createdReturnRequest = postDefaultRestCall(RETURNS, DEFAULT_FIELDS, returnRequestWsDTO,
				ReturnRequestWsDTO.class);
		//When
		final Response response = postCancelReturnRequestByDefault(
				createCancelReturnRequestWsDTO(createdReturnRequest.getRma(), "Other", "This is a test"));
		//Then
		assertOk(response, true);
	}

	@Test
	public void testCancelReturnRequest_InvalidReason_Failure()
	{
		//Given
		final ReturnEntryWsDTO returnEntryWsDTO1 = createReturnEntryWsDTO(1L, "HOLD", null, "DamagedInTransit", 0);
		final ReturnEntryWsDTO returnEntryWsDTO2 = createReturnEntryWsDTO(1L, "HOLD", null, "DamagedInTransit", 1);
		final List<ReturnEntryWsDTO> returnEntriesWsDTO = Arrays.asList(returnEntryWsDTO1, returnEntryWsDTO2);
		final ReturnRequestWsDTO returnRequestWsDTO = createReturnRequestWsDTO(returnEntriesWsDTO, "O-K2010-C0000-001",
				Boolean.FALSE);
		final ReturnRequestWsDTO createdReturnRequest = postDefaultRestCall(RETURNS, DEFAULT_FIELDS, returnRequestWsDTO,
				ReturnRequestWsDTO.class);
		//When
		final Response response = postCancelReturnRequestByDefault(
				createCancelReturnRequestWsDTO(createdReturnRequest.getRma(), "invalidReason", "This is a test"));
		assertEquals(500, response.getStatus());
	}

	/**
	 * Prepares requestbody from the given params for the POST call to create return
	 *
	 * @param returnEntriesWsDTO
	 * @param orderCode
	 * @param refundDeliveryCost
	 * @return returnRequestWsDTO populated from the given params
	 */
	protected ReturnRequestWsDTO createReturnRequestWsDTO(final List<ReturnEntryWsDTO> returnEntriesWsDTO, final String orderCode, final Boolean refundDeliveryCost)
	{
		final ReturnRequestWsDTO returnRequestWsDTO = new ReturnRequestWsDTO();

		final OrderWsDTO orderWsDTO = new OrderWsDTO();
		orderWsDTO.setCode(orderCode);

		returnRequestWsDTO.setOrder(orderWsDTO);
		returnRequestWsDTO.setEntries(returnEntriesWsDTO);
		returnRequestWsDTO.setRefundDeliveryCost(refundDeliveryCost);

		return returnRequestWsDTO;
	}

	/**
	 * Prepares a request body dto from the given params for the POST call to cancel a return request.
	 *
	 * @param code
	 * 			the RMA code
	 * @param reason
	 * 			the reason for cancellation
	 * @param notes
	 * 			the notes for the cancellaiton
	 * @return {@link de.hybris.platform.ordermanagementwebservices.dto.returns.CancelReturnRequestWsDTO} populated from the params
	 */
	protected CancelReturnRequestWsDTO createCancelReturnRequestWsDTO(final String code, final String reason, final String notes)
	{
		final CancelReturnRequestWsDTO cancelReturnRequestWsDTO = new CancelReturnRequestWsDTO();
		cancelReturnRequestWsDTO.setCode(code);
		cancelReturnRequestWsDTO.setCancelReason(reason);
		cancelReturnRequestWsDTO.setNotes(notes);

		return cancelReturnRequestWsDTO;
	}

	/**
	 * Prepares ReturnEntryWsDTO from the given params
	 *
	 * @param expectedQuantity
	 * @param action
	 * @param notes
	 * @param refundReason
	 * @param entryNumber
	 * @return returnEntryWsDTO
	 */
	protected ReturnEntryWsDTO createReturnEntryWsDTO(final Long expectedQuantity,  final String action, final String notes, final String refundReason, final Integer entryNumber)
	{
		final ReturnEntryWsDTO returnEntryWsDTO = new ReturnEntryWsDTO();
		returnEntryWsDTO.setExpectedQuantity(expectedQuantity);
		returnEntryWsDTO.setAction(action);
		returnEntryWsDTO.setNotes(notes);
		returnEntryWsDTO.setRefundReason(refundReason);

		final OrderEntryWsDTO orderEntryWsDTO = new OrderEntryWsDTO();
		orderEntryWsDTO.setEntryNumber(entryNumber);
		returnEntryWsDTO.setOrderEntry(orderEntryWsDTO);

		return returnEntryWsDTO;
	}
}
