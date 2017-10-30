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
package de.hybris.platform.workflow.services.internal.impl;

/**
 * Testing class for {@link DefaultAutomatedWorkflowTemplateRegistryTest} test case
 */
public final class AnotherActionTemplate
{
	/**
	 * Private constructor to force IllegalAccessException during reflective instantiation.
	 */
	private AnotherActionTemplate()
	{
		super();
	}

	public static AnotherActionTemplate getInstance()
	{
		return new AnotherActionTemplate();
	}
}
