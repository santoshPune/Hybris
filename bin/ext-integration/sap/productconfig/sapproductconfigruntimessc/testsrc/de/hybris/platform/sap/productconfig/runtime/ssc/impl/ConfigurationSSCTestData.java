/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;

import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticData;
import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticValueData;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.CsticData;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.CsticHeader;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.CsticValueData;


/**
 * Helper class for tests
 *
 */
public class ConfigurationSSCTestData
{

	public static ICsticValueData createCsticValueData()
	{
		final CsticValueData data = new CsticValueData();
		return data;
	}

	public static ICsticData createCsticData()
	{
		final ICsticData data = new CsticData();
		final CsticHeader csticHeader = new CsticHeader();
		csticHeader.setCsticValueType(Integer.valueOf(CsticModel.TYPE_STRING));
		data.setCsticHeader(csticHeader);
		return data;
	}

}
