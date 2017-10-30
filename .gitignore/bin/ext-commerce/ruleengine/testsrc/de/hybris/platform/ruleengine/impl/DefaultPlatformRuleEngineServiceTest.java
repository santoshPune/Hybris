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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.constants.RuleEngineConstants;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.enums.DroolsEqualityBehavior;
import de.hybris.platform.ruleengine.enums.DroolsEventProcessingMode;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.impl.DefaultPlatformRuleEngineService.DroolsInitializationException;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengine.strategies.RuleModuleFinderStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.builder.model.KieSessionModel.KieSessionType;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@UnitTest
public class DefaultPlatformRuleEngineServiceTest
{
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;
	@Mock
	private EngineRuleDao engineRuleDao;
	@Mock
	private ModelService modelService;
	@Mock
	private RuleModuleFinderStrategy ruleModuleFinderStrategy;
	@Mock
	private KieServices kieServices;
	@InjectMocks
	private DefaultPlatformRuleEngineService service;
	private Configuration configurationMock;
	@Mock
	private RuleEngineActionResult result;
	@Mock
	private KieFileSystem kieFileSystem;
	@Mock
	private KieModuleModel kieModuleModel;
	@Mock
	private KieBaseModel baseKieSessionModel;
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
		initMocks(this);
		//set up base configuration:
		configurationMock = new BaseConfiguration();
		when(kieBuilder.getResults()).thenReturn(results);
		when(kieServices.newKieModuleModel()).thenReturn(kieModuleModel);
		when(kieServices.newKieFileSystem()).thenReturn(kieFileSystem);
		when(kieServices.getRepository()).thenReturn(kieRepository);
		when(kieServices.newReleaseId(anyString(), anyString(), anyString())).thenReturn(releaseId);
		when(kieServices.newKieBuilder(any(KieFileSystem.class))).thenReturn(kieBuilder);
		when(configurationService.getConfiguration()).thenReturn(configurationMock);
	}

	@Test
	public void testUpdateEngineRuleAbstractRuleEngineRuleModelWhenRuleIsNull()
	{
		final RuleEngineActionResult updateEngineRule = service.updateEngineRule(null);
		assertTrue(updateEngineRule.isActionFailed());
		assertThat(updateEngineRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testUpdateEngineRuleAbstractRuleEngineRuleModelWhenRuleoModuleIsNull()
	{
		final DroolsRuleModel ruleEngineRule = mock(DroolsRuleModel.class);
		when(ruleModuleFinderStrategy.getRuleModuleForRule(ruleEngineRule)).thenReturn(null);
		final RuleEngineActionResult updateEngineRule = service.updateEngineRule(ruleEngineRule);
		assertTrue(updateEngineRule.isActionFailed());
		assertThat(updateEngineRule.getMessagesAsString(MessageLevel.ERROR), is(not(nullValue())));
	}

	@Test
	public void testUpdateEngineRuleAbstractRuleEngineRuleModelForSuccessCondition()
	{
		final DroolsRuleModel ruleEngineRule = mock(DroolsRuleModel.class);
		final DroolsKIEModuleModel ruleModule = mock(DroolsKIEModuleModel.class);
		when(ruleModuleFinderStrategy.getRuleModuleForRule(ruleEngineRule)).thenReturn(ruleModule);
		final DroolsKIEBaseModel kieBase = mock(DroolsKIEBaseModel.class);
		when(droolsKIEBaseFinderStrategy.getKIEBaseForKIEModule(ruleModule)).thenReturn(kieBase);
		final RuleEngineActionResult actionResult = service.updateEngineRule(ruleEngineRule);
		verify(ruleEngineRule).setKieBase(kieBase);
		verify(modelService).save(ruleEngineRule);
		assertFalse(actionResult.isActionFailed());
		assertThat(actionResult.getMessagesAsString(MessageLevel.INFO), is(not(nullValue())));
	}

	@Test
	public void testUpdateEngineRuleAbstractRuleEngineRuleModelAbstractRulesModuleModel()
	{
		final RuleEngineActionResult result1 = service.updateEngineRule(null, null);
		assertTrue(result1.isActionFailed());
		assertThat(result1.getMessagesAsString(MessageLevel.ERROR),
				is("RulesModule:nullnull line 0 : Cannot update engine rule, given rule is null\n"));

		final AbstractRuleEngineRuleModel ruleModel = mock(AbstractRuleEngineRuleModel.class);
		final RuleEngineActionResult result2 = service.updateEngineRule(ruleModel, null);
		assertTrue(result2.isActionFailed());
		assertThat(
				result2.getMessagesAsString(MessageLevel.ERROR),
				is("RulesModule:nullnull line 0 : Cannot update engine rule, given rule with code: null is not DroolsRuleModel, but null.\n"));
	}

	@Test
	public void testGetRuleByCode()
	{
		service.getRuleForCode("random string code");
		verify(engineRuleDao).getRuleByCode(anyString());
	}

	@Test
	public void testCreateKieModuleWithNullModule() throws DroolsInitializationException
	{

		try
		{
			service.createKieModule(null, null);
			fail("Expected IllegalArgumentException when null model");
		}
		catch (final IllegalArgumentException e)
		{
			//success - continue
		}
	}


	@Test
	public void testCreateKieModuleWithSimpleModule() throws DroolsInitializationException
	{
		final DroolsKIEModuleModel module = mock(DroolsKIEModuleModel.class);
		final List<DroolsKIEBaseModel> kieBases = new ArrayList<>();
		final RuleEngineActionResult result = new RuleEngineActionResult();
		final DroolsKIEBaseModel base = mock(DroolsKIEBaseModel.class);
		when(base.getName()).thenReturn("Base Name");
		when(base.getEqualityBehavior()).thenReturn(DroolsEqualityBehavior.EQUALITY);
		when(base.getEventProcessingMode()).thenReturn(DroolsEventProcessingMode.STREAM);
		kieBases.add(base);
		when(module.getKieBases()).thenReturn(kieBases);
		final KieBaseModel kieBaseModel = mock(KieBaseModel.class);
		when(kieModuleModel.newKieBaseModel(anyString())).thenReturn(kieBaseModel);

		service.setKieServices(kieServices);
		when(kieServices.newKieModuleModel()).thenReturn(kieModuleModel);
		service.createKieModule(module, result);

		verify(kieModuleModel).newKieBaseModel("Base Name");
	}

	@Test
	public void testCreateKieModuleFullProcess() throws DroolsInitializationException
	{
		final DroolsKIEModuleModel module = mock(DroolsKIEModuleModel.class);

		service.createKieModule(module, result);
		verify(kieServices).newKieModuleModel();
		verify(kieServices).newKieFileSystem();
		verify(kieBuilder).buildAll();
	}

	@Test
	public void testWriteKModuleXML()
	{
		final KieModuleModel module = mock(KieModuleModel.class);
		final KieFileSystem kfs = mock(KieFileSystem.class);
		service.writeKModuleXML(module, kfs);
		verify(kfs).writeKModuleXML(anyString());
		verify(module).toXML();
	}

	@Test
	public void testWritePomXML()
	{
		final DroolsKIEModuleModel module = mock(DroolsKIEModuleModel.class);
		final KieFileSystem kfs = mock(KieFileSystem.class);

		service.writePomXML(module, kfs);

		verify(kfs).generateAndWritePomXML(any(ReleaseId.class));
	}

	@Test
	public void testAddKieBaseModel()
	{
		final DroolsKIEBaseModel base = mock(DroolsKIEBaseModel.class);
		when(base.getEqualityBehavior()).thenReturn(DroolsEqualityBehavior.EQUALITY);
		when(base.getName()).thenReturn("Mock Base");
		when(base.getItemtype()).thenReturn(DroolsKIEBaseModel._TYPECODE);
		when(base.getEventProcessingMode()).thenReturn(DroolsEventProcessingMode.STREAM);
		when(kieModuleModel.newKieBaseModel("Mock Base")).thenReturn(baseKieSessionModel);
		service.addKieBaseModel(kieModuleModel, kieFileSystem, base);

		verify(kieModuleModel).newKieBaseModel("Mock Base");
		verify(baseKieSessionModel).setEqualsBehavior(EqualityBehaviorOption.EQUALITY);
		verify(baseKieSessionModel).setEventProcessingMode(EventProcessingOption.STREAM);
		verify(base).getRules(); //test addRules separately.
		verify(base).getKieSessions();//test addKieSessionModel separately.
	}

	@Test
	public void testAddRulesEmptyRules()
	{
		final KieBaseModel kieBaseModel = mock(KieBaseModel.class);
		final KieFileSystem kfs = mock(KieFileSystem.class);
		final DroolsKIEBaseModel base = mock(DroolsKIEBaseModel.class);
		when(base.getRules()).thenReturn(Collections.EMPTY_SET);
		service.addRules(kieBaseModel, kfs, base);
		verifyZeroInteractions(kfs);
	}

	@Test
	public void testAddRulesRuleWithoutContent()
	{
		final KieBaseModel kieBaseModel = mock(KieBaseModel.class);
		final KieFileSystem kfs = mock(KieFileSystem.class);
		final DroolsKIEBaseModel base = mock(DroolsKIEBaseModel.class);
		final Set<DroolsRuleModel> rules = new HashSet();
		final DroolsRuleModel rule = mock(DroolsRuleModel.class);
		rules.add(rule);
		when(base.getRules()).thenReturn(rules);

		service.addRules(kieBaseModel, kfs, base);

		verifyZeroInteractions(kfs);

	}

	@Test
	public void testAddRulesRuleInactive()
	{
		final KieBaseModel kieBaseModel = mock(KieBaseModel.class);
		final KieFileSystem kfs = mock(KieFileSystem.class);
		final DroolsKIEBaseModel base = mock(DroolsKIEBaseModel.class);

		final Set<DroolsRuleModel> rules = new HashSet();
		final DroolsRuleModel rule = mock(DroolsRuleModel.class);
		when(rule.getRuleContent()).thenReturn("rule content");
		when(rule.getActive()).thenReturn(Boolean.FALSE);
		rules.add(rule);
		when(base.getRules()).thenReturn(rules);

		service.addRules(kieBaseModel, kieFileSystem, base);

		verifyZeroInteractions(kfs);
	}

	@Test
	public void testAddKieSessionModel()
	{
		final DroolsKIESessionModel session = mock(DroolsKIESessionModel.class);
		final KieBaseModel base = mock(KieBaseModel.class);
		final KieSessionModel kieSessionModel = mock(KieSessionModel.class);
		final DroolsKIEBaseModel droolsKIEBaseModel = mock(DroolsKIEBaseModel.class);

		when(session.getPk()).thenReturn(PK.fromLong(1235l));
		when(session.getSessionType()).thenReturn(DroolsSessionType.STATELESS);
		when(droolsKIEBaseModel.getDefaultKIESession()).thenReturn(session);
		when(base.newKieSessionModel(any(String.class))).thenReturn(kieSessionModel);
		when(session.getKieBase()).thenReturn(droolsKIEBaseModel);
		service.addKieSessionModel(base, session);

		verify(kieSessionModel).setDefault(true);
		verify(kieSessionModel).setType(KieSessionType.STATELESS);
	}

	@Test
	public void testGetReleaseId()
	{
		final String groupId = "groupId";
		final String artifactId = "artifactId";
		final String version = "version";
		final DroolsKIEModuleModel module = mock(DroolsKIEModuleModel.class);

		when(module.getMvnGroupId()).thenReturn(groupId);
		when(module.getMvnArtifactId()).thenReturn(artifactId);
		when(module.getMvnVersion()).thenReturn(version);

		service.getReleaseId(module);

		final ArgumentCaptor<String> argArtifactId = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> argGroupId = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<String> argVersion = ArgumentCaptor.forClass(String.class);
		verify(kieServices).newReleaseId(argGroupId.capture(), argArtifactId.capture(), argVersion.capture());
		assertThat(argArtifactId.getValue(), is(artifactId));
		assertThat(argGroupId.getValue(), is(groupId));
		assertThat(argVersion.getValue(), is(version));
	}

	@Test
	public void testCreateRuleEngineActionResult()
	{

		assertTestCreateRuleEngineActionResult(null, null, false, null);
		assertTestCreateRuleEngineActionResult("Message", null, true, MessageLevel.INFO);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", true, MessageLevel.INFO);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", true, MessageLevel.WARNING);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", true, MessageLevel.ERROR);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", false, MessageLevel.INFO);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", false, MessageLevel.WARNING);
		assertTestCreateRuleEngineActionResult("Message", "moduleName", false, MessageLevel.ERROR);
	}

	private void assertTestCreateRuleEngineActionResult(final String message, final String moduleName, final boolean success,
			final MessageLevel level)
	{
		final RuleEngineActionResult rear = service.createRuleEngineActionResult(message, moduleName, success, level);
		assertThat(rear.getModuleName(), is(moduleName));
		assertThat(Boolean.valueOf(rear.isActionFailed()), is(not(Boolean.valueOf(success))));

		if (MessageLevel.INFO.equals(level))
		{
			assertThat(rear.getMessagesAsString(level), containsString(message));
		}
		else
		{
			assertThat(rear.getMessagesAsString(MessageLevel.INFO), is(not(message)));
		}
		if (MessageLevel.WARNING.equals(level))
		{
			assertThat(rear.getMessagesAsString(level), containsString(message));
		}
		else
		{
			assertThat(rear.getMessagesAsString(MessageLevel.WARNING), is(not(message)));
		}
		if (MessageLevel.ERROR.equals(level))
		{
			assertThat(rear.getMessagesAsString(level), containsString(message));
		}
		else
		{
			assertThat(rear.getMessagesAsString(MessageLevel.ERROR), is(not(message)));
		}
	}

	@Test
	public void testSetup()
	{
		//second scenario
		configurationMock.setProperty(RuleEngineConstants.DROOLS_DATE_FORMAT_KEY, "dateFormat");
		service.setup();
		assertThat(System.getProperty(RuleEngineConstants.DROOLS_DATE_FORMAT_KEY), is("dateFormat"));
	}

	@Test
	public void testGetRuleModuleForRuleForSuccessCondition()
	{
		final DroolsRuleModel ruleEngineRule = mock(DroolsRuleModel.class);
		final DroolsKIEModuleModel ruleModule = mock(DroolsKIEModuleModel.class);
		when(ruleModuleFinderStrategy.getRuleModuleForRule(ruleEngineRule)).thenReturn(ruleModule);
		final AbstractRulesModuleModel rulesModuleModel = service.getRuleModuleForRule(ruleEngineRule);
		assertEquals(ruleModule, rulesModuleModel);
	}
}
