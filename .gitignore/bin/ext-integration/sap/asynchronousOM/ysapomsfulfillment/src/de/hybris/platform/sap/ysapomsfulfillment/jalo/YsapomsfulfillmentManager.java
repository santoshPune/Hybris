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
package de.hybris.platform.sap.ysapomsfulfillment.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.ysapomsfulfillment.constants.YsapomsfulfillmentConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class YsapomsfulfillmentManager extends GeneratedYsapomsfulfillmentManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( YsapomsfulfillmentManager.class.getName() );
	
	public static final YsapomsfulfillmentManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YsapomsfulfillmentManager) em.getExtension(YsapomsfulfillmentConstants.EXTENSIONNAME);
	}
	
}
