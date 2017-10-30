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
package de.hybris.platform.ruleengine.versioning.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RuleEngineRuleModelHistoricalContentCreatorUnitTest
{

	private RuleEngineRuleModelHistoricalContentCreator historicalContentCreator;

	@Before
	public void setUp() throws Exception
	{
		historicalContentCreator = new RuleEngineRuleModelHistoricalContentCreator();
		historicalContentCreator.afterPropertiesSet();
	}

	@Test
	public void testGetCleanedContent() throws Exception
	{
		final String rule1Content = readFromResource("ruleengine/test/versioning/rule1.drl");
		final String rule2Content = readFromResource("ruleengine/test/versioning/rule2.drl");

		final String rule1CleanedContent = historicalContentCreator.getCleanedContent(rule1Content);
		final String rule2CleanedContent = historicalContentCreator.getCleanedContent(rule2Content);

		assertThat(rule1CleanedContent).isNotEmpty();
		assertThat(rule2CleanedContent).isNotEmpty();
		assertThat(rule1CleanedContent).isEqualTo(rule2CleanedContent);
	}

	private String readFromResource(final String resourceName) throws IOException
	{
		final URL url = Resources.getResource(resourceName);
		return Resources.toString(url, Charsets.UTF_8);
	}

}
