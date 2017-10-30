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
package de.hybris.platform.yacceleratorordermanagement.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class YAcceleratorOrderManagementManager extends GeneratedYAcceleratorOrderManagementManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( YAcceleratorOrderManagementManager.class.getName() );
	
	public static final YAcceleratorOrderManagementManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YAcceleratorOrderManagementManager) em.getExtension(YAcceleratorOrderManagementConstants.EXTENSIONNAME);
	}
	
}
