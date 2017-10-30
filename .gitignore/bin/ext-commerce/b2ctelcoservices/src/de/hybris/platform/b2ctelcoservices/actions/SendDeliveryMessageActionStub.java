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
package de.hybris.platform.b2ctelcoservices.actions;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

/**
 * Class blocks default behavior of
 * de.hybris.platform.yacceleratorfulfilmentprocess.actions.consignment.SendDeliveryMessageAction class.
 */
public class SendDeliveryMessageActionStub extends AbstractProceduralAction<ConsignmentProcessModel>
{
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		// empty stub implementation
	}
}
