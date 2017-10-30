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
package de.hybris.platform.integration.cis.payment.setup;

import static de.hybris.platform.integration.cis.payment.constants.CispaymenthmcConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import de.hybris.platform.integration.cis.payment.constants.CispaymenthmcConstants;
import de.hybris.platform.integration.cis.payment.service.CispaymenthmcService;


@SystemSetup(extension = CispaymenthmcConstants.EXTENSIONNAME)
public class CispaymenthmcSystemSetup
{
	private final CispaymenthmcService cispaymenthmcService;

	public CispaymenthmcSystemSetup(final CispaymenthmcService cispaymenthmcService)
	{
		this.cispaymenthmcService = cispaymenthmcService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		cispaymenthmcService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return CispaymenthmcSystemSetup.class.getResourceAsStream("/cispaymenthmc/sap-hybris-platform.png");
	}
}
