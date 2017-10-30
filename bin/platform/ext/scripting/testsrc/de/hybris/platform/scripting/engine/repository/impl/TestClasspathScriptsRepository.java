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

package de.hybris.platform.scripting.engine.repository.impl;

import javax.annotation.PostConstruct;


public class TestClasspathScriptsRepository extends ClasspathScriptsRepository
{
	@Override
    @PostConstruct
	public void init()
	{
		tenantId = "junit";
	}
}
