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
 *
 *
 */
package de.hybris.platform.ruleengineatddtests.keywords;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class RuleEngineServicesKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(RuleEngineServicesKeywordLibrary.class);

	public String logTextWithLevel(final String logText, final String logLevel)
	{
		final Level level = Level.toLevel(logLevel, Level.INFO);
		LOG.log(level, logText);
		return null;
	}

}
