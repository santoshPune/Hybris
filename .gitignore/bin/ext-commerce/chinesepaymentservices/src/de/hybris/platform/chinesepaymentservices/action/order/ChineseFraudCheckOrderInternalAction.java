/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepaymentservices.action.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.task.RetryLaterException;

import java.util.HashSet;
import java.util.Set;


/**
 * No need to check credit card information for Chinese customers, so hard code the result.
 */
public class ChineseFraudCheckOrderInternalAction extends AbstractAction<OrderProcessModel>
{
	public enum Transition
	{
		OK, POTENTIAL, FRAUD;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}


	public Transition executeAction(final OrderProcessModel process)
	{
		return Transition.OK;
	}

	@Override
	public String execute(OrderProcessModel process) throws RetryLaterException, Exception
	{
		return executeAction(process).toString();
	}


	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}




}
