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
package de.hybris.platform.ordermanagementfacade.returns;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.ordermanagementfacade.returns.data.CancelReturnRequestData;
import de.hybris.platform.ordermanagementfacade.returns.impl.DefaultOmsReturnFacade;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOmsReturnFacadeTest
{
	@InjectMocks
	private DefaultOmsReturnFacade omsReturnFacade;
	@Mock
	private CancelReturnRequestData cancelReturnRequestData;
	@Mock
	private GenericDao<ReturnRequestModel> returnGenericDao;
	@Mock
	private List<ReturnEntryModel> returnEntryModelList;
	@Mock
	private ReturnCallbackService returnCallbackService;
	@Mock
	private ReturnEntryModel returnEntryModel;
	@Mock
	private ReturnRequestModel returnRequestModel;
	@Mock
	private ReturnService returnService;

	@Before
	public void setUp()
	{
		omsReturnFacade.setReturnGenericDao(returnGenericDao);
	}

	@Test
	public void shouldReversePayment() throws OrderReturnException
	{
		//Given
		when(returnRequestModel.getStatus()).thenReturn(ReturnStatus.PAYMENT_REVERSAL_FAILED);
		when(returnGenericDao.find(anyMap())).thenReturn(Arrays.asList(returnRequestModel));

		//When
		omsReturnFacade.requestManualPaymentReversalForReturnRequest(anyString());

		//Then
		verify(returnService).requestManualPaymentReversalForReturnRequest(any(ReturnRequestModel.class));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotReversePaymentWrongStatus() throws OrderReturnException
	{
		//Given
		when(returnRequestModel.getStatus()).thenReturn(ReturnStatus.COMPLETED);
		when(returnGenericDao.find(anyMap())).thenReturn(Arrays.asList(returnRequestModel));

		//When
		omsReturnFacade.requestManualPaymentReversalForReturnRequest(anyString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReversePaymentNullCode() throws OrderReturnException
	{
		//When
		omsReturnFacade.requestManualPaymentReversalForReturnRequest(null);
	}

	@Test
	public void shouldReverseTax() throws OrderReturnException
	{
		//Given
		when(returnRequestModel.getStatus()).thenReturn(ReturnStatus.TAX_REVERSAL_FAILED);
		when(returnGenericDao.find(anyMap())).thenReturn(Arrays.asList(returnRequestModel));

		//When
		omsReturnFacade.requestManualTaxReversalForReturnRequest(anyString());

		//Then
		verify(returnService).requestManualTaxReversalForReturnRequest(any(ReturnRequestModel.class));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotReverseTaxWrongStatus() throws OrderReturnException
	{
		//Given
		when(returnRequestModel.getStatus()).thenReturn(ReturnStatus.COMPLETED);
		when(returnGenericDao.find(anyMap())).thenReturn(Arrays.asList(returnRequestModel));

		//When
		omsReturnFacade.requestManualTaxReversalForReturnRequest(anyString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReverseTaxNullCode() throws OrderReturnException
	{
		//When
		omsReturnFacade.requestManualTaxReversalForReturnRequest(null);
	}

	@Test
	public void shouldExecuteOnReturnCancelResponse() throws OrderReturnException
	{
		//Given
		omsReturnFacade.setReturnGenericDao(returnGenericDao);
		final List<ReturnRequestModel> returnResultSet = Arrays.asList(returnRequestModel);
		when(returnGenericDao.find(anyMap())).thenReturn(returnResultSet);
		returnEntryModelList = Arrays.asList(returnEntryModel);
		when(returnRequestModel.getStatus()).thenReturn(ReturnStatus.APPROVAL_PENDING);
		when(returnRequestModel.getReturnEntries()).thenReturn(returnEntryModelList);

		//When
		omsReturnFacade.cancelReturnRequest(cancelReturnRequestData);

		//Then
		verify(returnCallbackService).onReturnCancelResponse(any());
	}
}
