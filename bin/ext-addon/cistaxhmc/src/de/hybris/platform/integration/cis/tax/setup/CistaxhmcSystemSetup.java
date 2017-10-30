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
 */
package de.hybris.platform.integration.cis.tax.setup;

import static de.hybris.platform.integration.cis.tax.constants.CistaxhmcConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import de.hybris.platform.integration.cis.tax.constants.CistaxhmcConstants;
import de.hybris.platform.integration.cis.tax.service.CistaxhmcService;


@SystemSetup(extension = CistaxhmcConstants.EXTENSIONNAME)
public class CistaxhmcSystemSetup
{
	private final CistaxhmcService cistaxhmcService;

	public CistaxhmcSystemSetup(final CistaxhmcService cistaxhmcService)
	{
		this.cistaxhmcService = cistaxhmcService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		cistaxhmcService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return CistaxhmcSystemSetup.class.getResourceAsStream("/cistaxhmc/sap-hybris-platform.png");
	}
}
