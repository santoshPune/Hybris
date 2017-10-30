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
package de.hybris.platform.b2ctelcostore.jalo;

import de.hybris.platform.b2ctelcostore.constants.B2ctelcostoreConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class B2ctelcostoreManager extends GeneratedB2ctelcostoreManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( B2ctelcostoreManager.class.getName() );
	
	public static final B2ctelcostoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2ctelcostoreManager) em.getExtension(B2ctelcostoreConstants.EXTENSIONNAME);
	}
	
}
