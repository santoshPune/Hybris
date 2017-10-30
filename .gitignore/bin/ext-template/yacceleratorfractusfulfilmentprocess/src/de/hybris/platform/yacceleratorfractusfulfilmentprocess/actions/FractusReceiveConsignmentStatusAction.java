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

import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


public class FractusReceiveConsignmentStatusAction extends AbstractAction<FractusConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(FractusReceiveConsignmentStatusAction.class);

	public enum Transition
	{
		OK, CANCEL, ERROR;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final FractusConsignmentProcessModel process)
	{
		LOG.info(" FractusReceiveConsignmentStatusAction triggered and the status set to " + Transition.OK);

		final Transition result = Transition.OK;

		return result.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
