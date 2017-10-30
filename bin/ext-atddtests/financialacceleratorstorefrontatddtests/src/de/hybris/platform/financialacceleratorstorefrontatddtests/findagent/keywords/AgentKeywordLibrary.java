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
package de.hybris.platform.financialacceleratorstorefrontatddtests.findagent.keywords;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class AgentKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(AgentKeywordLibrary.class);

	@Autowired
	public AgentFacade agentFacade;

	/**
	 * Load agent from facade and check it for nullability
	 *
	 * @param uid
	 */
	public void loadAgentByUid(final String uid)
	{
		final AgentData agentData = agentFacade.getAgentByUid(uid);

		assertNotNull(agentData);
	}

	/**
	 * Check inner parameter of agent - category.
	 *
	 * @param agentUid
	 * @param categoryCode
	 */
	public void loadAgentAndCheckCategory(final String agentUid, final String categoryCode)
	{
		final AgentData agentData = agentFacade.getAgentByUid(agentUid);

		assertNotNull(agentData);

		assertThat(getCategoriesCodeCollection(agentData.getCategories()), hasItem(categoryCode));
	}

	public void canLoadAgentsInCategory(final String categoryCode)
	{
		final List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);

		assertNotNull(agents);
	}

	public void categoryContains(final String categoryCode, final int number)
	{
		LOG.info("categorycode: " + categoryCode);
		LOG.info("number: " + number);
		final List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
		LOG.info("agents: " + agents);
		assertEquals(number, agents.size());
	}

	public void loadAgentsByCategoryAndVerify(final String category)
	{
		System.out.print(category);
		final List<AgentData> agents = agentFacade.getAgentsByCategory(category);

		assertNotNull(agents);
		assertThat(agents, not(is(Collections.<AgentData> emptyList())));
	}

	public void loadFromAnyCategoryIsSafely(final String categoryCode)
	{
		final List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
		assertNotNull(agents);
	}

	public void unknownCategoryIsEmpty(final String categoryCode)
	{
		final List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
		assertNotNull(agents);
		assertTrue(agents.isEmpty());
	}

	public void allAgentsContain(final String agentUid)
	{
		final List<AgentData> agents = agentFacade.getAgents();
		assertNotNull(agents);

		assertThat(getAgentUidCollection(agents), hasItem(agentUid));
	}

	public void allAgentsContainAgentWithCategory(final String categoryCode)
	{
		final List<AgentData> agents = agentFacade.getAgents();

		assertThat(getCategoriesCodeCollectionFromAgents(agents), hasItem(categoryCode));
	}


	protected List<String> getCategoriesCodeCollectionFromAgents(final List<AgentData> list)
	{
		final List<String> result = new ArrayList<>();

		for (final AgentData ad : list)
		{
			result.addAll(getCategoriesCodeCollection(ad.getCategories()));
		}

		return result;
	}

	protected List<String> getCategoriesCodeCollection(final List<CategoryData> list)
	{
		final List<String> result = new ArrayList<>();

		for (final CategoryData cd : list)
		{
			result.add(cd.getCode());
		}

		return result;
	}

	protected List<String> getAgentUidCollection(final List<AgentData> list)
	{
		final List<String> result = new ArrayList<>();

		for (final AgentData cd : list)
		{
			result.add(cd.getUid());
		}

		return result;
	}
}
