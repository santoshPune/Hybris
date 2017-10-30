/*
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
package de.hybris.platform.financialservices.jalo;

import de.hybris.platform.financialservices.constants.FinancialservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class FinancialservicesManager extends GeneratedFinancialservicesManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(FinancialservicesManager.class.getName());

	public static final FinancialservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (FinancialservicesManager) em.getExtension(FinancialservicesConstants.EXTENSIONNAME);
	}

}
