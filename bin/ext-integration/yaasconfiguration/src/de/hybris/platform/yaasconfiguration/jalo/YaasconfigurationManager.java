/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.yaasconfiguration.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yaasconfiguration.constants.YaasconfigurationConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class YaasconfigurationManager extends GeneratedYaasconfigurationManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( YaasconfigurationManager.class.getName() );
	
	public static final YaasconfigurationManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YaasconfigurationManager) em.getExtension(YaasconfigurationConstants.EXTENSIONNAME);
	}
	
}
