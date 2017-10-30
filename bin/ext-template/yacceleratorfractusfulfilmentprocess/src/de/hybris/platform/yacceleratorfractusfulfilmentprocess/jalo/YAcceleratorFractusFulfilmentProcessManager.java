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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.constants.YAcceleratorfractusfulfilmentProcessConstants;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class YAcceleratorFractusFulfilmentProcessManager extends GeneratedYAcceleratorFractusFulfilmentProcessManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(YAcceleratorFractusFulfilmentProcessManager.class.getName());

	public static final YAcceleratorFractusFulfilmentProcessManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YAcceleratorFractusFulfilmentProcessManager) em
				.getExtension(YAcceleratorfractusfulfilmentProcessConstants.EXTENSIONNAME);
	}

}
