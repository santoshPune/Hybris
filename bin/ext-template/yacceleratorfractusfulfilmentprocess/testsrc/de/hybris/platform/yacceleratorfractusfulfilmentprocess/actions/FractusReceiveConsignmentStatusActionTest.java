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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * Unit test for the FractusReceiveConsignmentStatusAction class.
 */
@UnitTest
public class FractusReceiveConsignmentStatusActionTest
{

	FractusReceiveConsignmentStatusAction action;
	@Mock
	private FractusConsignmentProcessModel consignmentProcess;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		action = new FractusReceiveConsignmentStatusAction();
		initMocks(this);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions.FractusReceiveConsignmentStatusAction#execute(de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel)}
	 * .
	 */
	@Test
	public void testExecute()
	{
		final String execute = action.execute(consignmentProcess);
		assertThat(execute).isEqualTo("OK");
	}

	@Test
	public void testGetTransitions()
	{
		final Set<String> transitions = action.getTransitions();
		assertThat(transitions).containsOnly("OK", "ERROR", "CANCEL");
	}

}
