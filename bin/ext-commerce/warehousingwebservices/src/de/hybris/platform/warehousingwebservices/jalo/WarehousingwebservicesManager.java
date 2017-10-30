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
package de.hybris.platform.warehousingwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.warehousingwebservices.constants.WarehousingwebservicesConstants;

@SuppressWarnings("PMD")
public class WarehousingwebservicesManager extends GeneratedWarehousingwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( WarehousingwebservicesManager.class.getName() );
	
	public static final WarehousingwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WarehousingwebservicesManager) em.getExtension(WarehousingwebservicesConstants.EXTENSIONNAME);
	}
	
}
