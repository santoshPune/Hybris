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
package de.hybris.platform.sap.productconfig.frontend.controllers;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;

import org.springframework.validation.BindingResult;


/**
 *
 */
public class UpdateDataHolder
{
	private ConfigurationData configData;
	private BindingResult bindingResult;
	private UiStatus uiStatus;
	private long startTime;

	public ConfigurationData getConfigData()
	{
		return configData;
	}

	public void setConfigData(final ConfigurationData configData)
	{
		this.configData = configData;
	}

	public BindingResult getBindingResult()
	{
		return bindingResult;
	}

	public void setBindingResult(final BindingResult bindingResult)
	{
		this.bindingResult = bindingResult;
	}

	public UiStatus getUiStatus()
	{
		return uiStatus;
	}

	public void setUiStatus(final UiStatus uiStatus)
	{
		this.uiStatus = uiStatus;
	}

	public String getProductCode()
	{
		return configData.getKbKey().getProductCode();
	}

	public long timeElapsed()
	{
		final long actualTime = System.currentTimeMillis();
		final long duration = actualTime - startTime;
		startTime = actualTime;
		return duration;
	}
}
