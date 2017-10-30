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
package de.hybris.platform.ruleengine.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.configuration.BaseConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieModuleModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@UnitTest
public class DefaultPlatformRuleEngineServiceArchiveRuleTest
{

	private AbstractRuleEngineRuleModel rule = null;
	private AbstractRulesModuleModel rulesModule = null;

	@Mock
	private ConfigurationService configurationService;
	@Mock
	private EventService eventService;
	@Mock
	private ModelService modelService;
	@Mock
	private KieServices kieServices;
	@InjectMocks
	private DefaultPlatformRuleEngineService service;
	@Mock
	private KieFileSystem kieFileSystem;
	@Mock
	private KieModuleModel kieModuleModel;
	@Mock
	private KieRepository kieRepository;
	@Mock
	private ReleaseId releaseId;
	@Mock
	private KieBuilder kieBuilder;
	@Mock
	private Results results;


	@Before
	public void setUp() throws Exception
	{
		service = new DefaultPlatformRuleEngineService();
		service.setEventService(eventService);
		initMocks(this);

		when(kieBuilder.getResults()).thenReturn(results);
		when(kieServices.newKieModuleModel()).thenReturn(kieModuleModel);
		when(kieServices.newKieFileSystem()).thenReturn(kieFileSystem);
		when(kieServices.getRepository()).thenReturn(kieRepository);
		when(kieServices.newReleaseId(anyString(), anyString(), anyString())).thenReturn(releaseId);
		when(kieServices.newKieBuilder(any(KieFileSystem.class))).thenReturn(kieBuilder);
		when(configurationService.getConfiguration()).thenReturn(new BaseConfiguration());

	}

	@Test
	public void testArchiveRuleAbstractRuleEngineRuleModelAllValidation()
	{
		AbstractRuleEngineRuleModel ruleEngineRule = null;
		final RuleEngineActionResult archiveRule = service.archiveRule(ruleEngineRule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));

		ruleEngineRule = mock(AbstractRuleEngineRuleModel.class);
		service.archiveRule(ruleEngineRule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));

		ruleEngineRule = mock(DroolsRuleModel.class);
		service.archiveRule(ruleEngineRule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testArchiveRuleAbstractRuleEngineRuleModelSuccessScenario()
	{
		final DroolsRuleModel rule = mock(DroolsRuleModel.class);

		final DroolsKIEModuleModel kieModule = mock(DroolsKIEModuleModel.class);
		when(kieModule.getPk()).thenReturn(PK.fromLong(2345l));
		final DroolsKIEBaseModel kieBase = mock(DroolsKIEBaseModel.class);
		when(kieBase.getKieModule()).thenReturn(kieModule);
		when(rule.getKieBase()).thenReturn(kieBase);
		when(rule.getActive()).thenReturn(Boolean.TRUE);

		service.archiveRule(rule);

		verify(rule).setActive(Boolean.FALSE);
		verify(modelService).save(rule);
	}

	@Test
	public void testArchiveRuleRuleAndModuleValidationNullRule()
	{

		final RuleEngineActionResult archiveRule = service.archiveRule(rule, rulesModule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testArchiveRuleRuleAndModuleValidationWrongTypeOfRule()
	{

		rule = mock(AbstractRuleEngineRuleModel.class);
		final RuleEngineActionResult archiveRule = service.archiveRule(rule, rulesModule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testArchiveRuleRuleAndModuleValidationNullRulesModule()
	{

		rule = mock(DroolsRuleModel.class);
		try
		{
			service.archiveRule(rule, rulesModule);
			fail("Expected NullPointerException when rulesModule is null");
		}
		catch (final NullPointerException e)
		{
			//"success" - continue.
		}
	}

	@Test
	public void testArchiveRuleRuleAndModuleValidationWrongTypeOfModule()
	{

		rule = mock(DroolsRuleModel.class);
		rulesModule = mock(AbstractRulesModuleModel.class);
		final RuleEngineActionResult archiveRule = service.archiveRule(rule, rulesModule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testArchiveRuleRuleAndModuleValidationRuleIsInactive()
	{

		rule = mock(DroolsRuleModel.class);
		final DroolsRuleModel droolsRule = (DroolsRuleModel) rule;
		when(droolsRule.getActive()).thenReturn(Boolean.FALSE);
		rulesModule = mock(DroolsKIEModuleModel.class);
		final RuleEngineActionResult archiveRule = service.archiveRule(rule, rulesModule);
		assertTrue(archiveRule.isActionFailed());
		assertThat(archiveRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

}
