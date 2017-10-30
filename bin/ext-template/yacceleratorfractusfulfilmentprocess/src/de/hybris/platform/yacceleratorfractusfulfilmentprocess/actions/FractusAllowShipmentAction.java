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

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.warehouse.Process2WarehouseAdapter;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class FractusAllowShipmentAction extends AbstractAction<FractusConsignmentProcessModel>
{
	private Process2WarehouseAdapter process2WarehouseAdapter;

	private static final Logger LOG = Logger.getLogger(FractusAllowShipmentAction.class);

	public enum Transition
	{
		DELIVERY, CANCEL, ERROR;

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
		final ConsignmentModel consignment = process.getConsignment();
		if (consignment != null)
		{
			try
			{
				getProcess2WarehouseAdapter().shipConsignment(process.getConsignment());

				return Transition.DELIVERY.toString();

			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage(), e);
				return Transition.ERROR.toString();
			}
		}
		return Transition.ERROR.toString();
	}

	protected Process2WarehouseAdapter getProcess2WarehouseAdapter()
	{
		return process2WarehouseAdapter;
	}

	@Required
	public void setProcess2WarehouseAdapter(final Process2WarehouseAdapter process2WarehouseAdapter)
	{
		this.process2WarehouseAdapter = process2WarehouseAdapter;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
