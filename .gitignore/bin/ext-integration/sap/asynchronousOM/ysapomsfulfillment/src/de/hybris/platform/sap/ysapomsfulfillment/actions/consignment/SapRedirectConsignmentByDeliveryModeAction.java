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
package de.hybris.platform.sap.ysapomsfulfillment.actions.consignment;

import org.apache.log4j.Logger;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.sap.ysapomsfulfillment.model.SapConsignmentProcessModel;
import de.hybris.platform.task.RetryLaterException;


/**
 * Redirects to the proper wait node depending on whether a consignment is for ship or pickup.
 */
public class SapRedirectConsignmentByDeliveryModeAction extends AbstractProceduralAction<SapConsignmentProcessModel>
{
	
	private static final Logger LOG = Logger.getLogger(SapRedirectConsignmentByDeliveryModeAction.class);
	@Override
	public void executeAction(SapConsignmentProcessModel process) throws RetryLaterException, Exception {
		LOG.info(String.format("Proccess: %s in step %s",process.getCode(), getClass().getSimpleName()));
			
	}
	
}
