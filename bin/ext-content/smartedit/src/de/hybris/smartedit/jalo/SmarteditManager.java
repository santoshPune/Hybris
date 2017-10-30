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
package de.hybris.smartedit.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.smartedit.constants.SmarteditConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class SmarteditManager extends GeneratedSmarteditManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( SmarteditManager.class.getName() );
	
	public static final SmarteditManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SmarteditManager) em.getExtension(SmarteditConstants.EXTENSIONNAME);
	}
	
}
