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
package de.hybris.platform.yacceleratorfulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratorfulfilmentprocess.constants.YAcceleratorFulfilmentProcessConstants;

@SuppressWarnings("PMD")
public class YAcceleratorFulfilmentProcessManager extends GeneratedYAcceleratorFulfilmentProcessManager
{
	public static final YAcceleratorFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YAcceleratorFulfilmentProcessManager) em.getExtension(YAcceleratorFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
