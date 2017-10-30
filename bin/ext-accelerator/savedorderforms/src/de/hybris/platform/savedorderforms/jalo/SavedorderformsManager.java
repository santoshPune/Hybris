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
package de.hybris.platform.savedorderforms.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.savedorderforms.constants.SavedorderformsConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class SavedorderformsManager extends GeneratedSavedorderformsManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( SavedorderformsManager.class.getName() );
	
	public static final SavedorderformsManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SavedorderformsManager) em.getExtension(SavedorderformsConstants.EXTENSIONNAME);
	}
	
}
