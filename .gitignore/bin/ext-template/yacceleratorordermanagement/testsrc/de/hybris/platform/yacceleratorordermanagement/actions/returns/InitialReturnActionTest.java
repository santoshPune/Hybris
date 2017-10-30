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

package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class InitialReturnActionTest
{
	@InjectMocks
	private InitialReturnAction action;

	@Mock
	private ReturnProcessModel returnProcessModel;

	@Mock
	private ReturnRequestModel returnRequest;

	@Mock
	private ReturnEntryModel returnEntry;

	@Before
	public void setup()
	{
		List<ReturnEntryModel> returnEntries = new ArrayList<>();
		returnEntries.add(returnEntry);

		when(returnProcessModel.getReturnRequest()).thenReturn(returnRequest);
		when(returnRequest.getReturnEntries()).thenReturn(returnEntries);
	}

	@Test
	public void shouldRedirectToPayment() throws Exception
	{
		when(returnEntry.getAction()).thenReturn(ReturnAction.IMMEDIATE);
		String transition = action.execute(returnProcessModel);
		assertEquals(InitialReturnAction.Transition.INSTORE.toString(), transition);
	}

	@Test
	public void shouldRedirectToApproval() throws Exception
	{
		when(returnEntry.getAction()).thenReturn(ReturnAction.HOLD);
		String transition = action.execute(returnProcessModel);
		assertEquals(InitialReturnAction.Transition.ONLINE.toString(), transition);
	}
}
