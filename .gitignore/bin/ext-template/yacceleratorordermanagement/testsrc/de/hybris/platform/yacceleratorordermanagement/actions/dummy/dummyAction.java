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
package de.hybris.platform.yacceleratorordermanagement.actions.dummy;

import org.apache.log4j.Logger;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorordermanagement.actions.order.fraudcheck.SendOrderPlacedNotificationAction;

public class dummyAction extends AbstractProceduralAction {

	private static final Logger LOG = Logger.getLogger(SendOrderPlacedNotificationAction.class);

	@Override
	public void executeAction(BusinessProcessModel arg0) throws RetryLaterException, Exception {

		LOG.info("The dummy action was done successfully!");
	}

}
