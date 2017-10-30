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
package de.hybris.platform.acceleratorservices.process.email.actions;

import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * A process action to remove emails that were sent successfully.
 */
public class RemoveSentEmailAction extends AbstractProceduralAction
{
	@Override
	public void executeAction(final BusinessProcessModel businessProcessModel)
	{
		for (final EmailMessageModel emailMessage : businessProcessModel.getEmails())
		{
			if (emailMessage.isSent())
			{
				getModelService().remove(emailMessage);
			}
		}
	}
}
