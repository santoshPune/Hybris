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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.constants.RuleEngineConstants;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.event.RuleEngineInitializedEvent;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengine.strategies.RuleModuleFinderStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@UnitTest
public class DefaultPlatformRuleEngineServiceInitializeTest
{




	@Mock
	private ConfigurationService configurationService;
	@Mock
	private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;
	@Mock
	private EngineRuleDao engineRuleDao;
	@Mock
	private EventService eventService;
	@Mock
	private ModelService modelService;
	@Mock
	private RuleModuleFinderStrategy ruleModuleFinderStrategy;
	@Mock
	private RulesModuleDao rulesModuleDao;
	@Mock
	KieServices kieServices;
	@InjectMocks
	DefaultPlatformRuleEngineService service;

	private Configuration configurationMock;
	@Mock
	private AbstractRulesModuleModel abstractModule;
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
	ReleaseId releaseId;
	@Mock
	private KieBuilder kieBuilder;
	@Mock
	private Results results;
	private AbstractRulesModuleModel module;
	private final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializerPropagate = (module) -> {
		return service.initialize(module, true);
	};
	private final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializerDefault = (module) -> {
		return service.initialize(module);
	};
	private final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializerDontPropagate = (module) -> {
		return service.initialize(module, false);

	};
	@Mock
	private KieModule oldKieModule;
	@Mock
	private KieModule newKieModule;


	@Before
	public void setUp() throws Exception
	{
		service = new DefaultPlatformRuleEngineService();
		initMocks(this);
		//set up base configuration:
		configurationMock = new BaseConfiguration();
		when(configurationService.getConfiguration()).thenReturn(configurationMock);
		when(kieBuilder.getResults()).thenReturn(results);
		when(kieBuilder.getKieModule()).thenReturn(newKieModule);
		when(kieServices.newKieModuleModel()).thenReturn(kieModuleModel);
		when(kieServices.newKieFileSystem()).thenReturn(kieFileSystem);
		when(kieRepository.getKieModule(any(ReleaseId.class))).thenReturn(oldKieModule);
		when(kieServices.getRepository()).thenReturn(kieRepository);
		when(kieServices.newReleaseId(anyString(), anyString(), anyString())).thenReturn(releaseId);
		when(kieServices.newKieBuilder(any(KieFileSystem.class))).thenReturn(kieBuilder);
	}


	@Test
	public void testInitializeAbstractRulesModuleValidationExceptionsWithPropagation()
	{
		assertInitializeValidationExceptions(serviceInitializerPropagate);
	}

	@Test
	public void testInitializeAbstractRulesModuleValidationExceptionsWithoutPropagation()
	{
		assertInitializeValidationExceptions(serviceInitializerDontPropagate);
	}

	@Test
	public void testInitializeAbstractRulesModuleValidationExceptionsWithDefault()
	{
		assertInitializeValidationExceptions(serviceInitializerDefault);
	}

	private void assertInitializeValidationExceptions(
			final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializer)
	{
		module = null;
		try
		{
			serviceInitializer.apply(module);
			fail("IllegalArgumentException expected: module must not be null");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("module must not be null"));
		}
		module = mock(AbstractRulesModuleModel.class);
		when(module.getName()).thenReturn("Mock Module");
		when(module.getItemtype()).thenReturn(AbstractRulesModuleModel._TYPECODE);
		try
		{
			serviceInitializer.apply(module);
			fail("IllegalArgumentException expected: module must be DroolsKIEModule");
		}
		catch (final IllegalStateException e)
		{
			assertThat(e.getMessage(),
					is("module Mock Module is not a DroolsKIEModule. " + AbstractRulesModuleModel._TYPECODE + " is not supported."));
		}
	}

	@Test
	public void testInitializeAbstractRulesModuleModelWithPropagation()
	{

		assertInitializeSuccessTest(serviceInitializerPropagate);
		assertInitializeSuccessTest(serviceInitializerDefault);
		assertInitializeSuccessTest(serviceInitializerDontPropagate);
	}

	private void assertInitializeSuccessTest(final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializer)
	{
		setUpSuccessDroolsKIEModuleModel();
		final RuleEngineActionResult initialize = serviceInitializer.apply(module);
		assertFalse(initialize.isActionFailed());
		assertThat(initialize.getMessagesAsString(MessageLevel.INFO), is(not(nullValue())));

	}

	private DroolsKIEModuleModel setUpSuccessDroolsKIEModuleModel()
	{
		module = mock(DroolsKIEModuleModel.class);
		when(module.getName()).thenReturn("Mock Module");
		when(module.getItemtype()).thenReturn(DroolsKIEModuleModel._TYPECODE);
		return (DroolsKIEModuleModel) module;
	}

	@Test
	public void testInitializeAbstractRulesModuleModelWhenRuleEngineIsNotActiveWithoutPropagation()
	{
		assertInitializeWhenRuleEngineIsNotActive(serviceInitializerDontPropagate);

	}

	@Test
	public void testInitializeAbstractRulesModuleModelWhenRuleEngineIsNotActiveWithPropagation()
	{
		assertInitializeWhenRuleEngineIsNotActive(serviceInitializerPropagate);

	}

	@Test
	public void testInitializeAbstractRulesModuleModelWhenRuleEngineIsNotActiveDefaultPropagation()
	{
		assertInitializeWhenRuleEngineIsNotActive(serviceInitializerDefault);

	}

	private void assertInitializeWhenRuleEngineIsNotActive(
			final Function<AbstractRulesModuleModel, RuleEngineActionResult> serviceInitializer)
	{
		activateRuleEngine(false);
		final RuleEngineActionResult initialize = serviceInitializer.apply(null);
		assertTrue(initialize.isActionFailed());
	}

	private void activateRuleEngine(final boolean b)
	{
		configurationMock.setProperty(RuleEngineConstants.RULE_ENGINE_ACTIVE, Boolean.valueOf(b));
	}

	@Test
	public void testInitializeAllRulesModulesFailWithAWarningMessage()
	{
		activateRuleEngine(false);

		final List<RuleEngineActionResult> initializeAllRulesModules = service.initializeAllRulesModules();

		assertThat(initializeAllRulesModules, is(not(nullValue())));
		assertEquals(1, initializeAllRulesModules.size());
		final RuleEngineActionResult raor = initializeAllRulesModules.get(0);
		assertTrue(raor.isActionFailed());
		assertThat(raor.getMessagesAsString(MessageLevel.WARNING), is(not(nullValue())));
		assertThat(raor.getModuleName(), is(nullValue()));
	}

	@Test
	public void testInitializeAllRulesModulesSucceed()
	{

		activateRuleEngine(true);

		final List<AbstractRulesModuleModel> rulesModuleList = new ArrayList<>();
		final DroolsKIEModuleModel moduleModel = setUpSuccessDroolsKIEModuleModel();
		rulesModuleList.add(moduleModel);
		when(rulesModuleDao.findAll()).thenReturn(rulesModuleList);

		final List<RuleEngineActionResult> initializeAllRulesModules = service.initializeAllRulesModules();

		assertThat(initializeAllRulesModules, is(not(nullValue())));
		assertEquals(1, initializeAllRulesModules.size());
		final RuleEngineActionResult ruleEngineActionResult = initializeAllRulesModules.get(0);
		assertThat(ruleEngineActionResult, is(not(nullValue())));
		assertFalse(ruleEngineActionResult.isActionFailed());
		assertThat(ruleEngineActionResult.getMessagesAsString(MessageLevel.INFO), is(not(nullValue())));
		//each rule will now be activated in turn!
		verify(eventService).publishEvent(any(RuleEngineInitializedEvent.class));
		verify(kieRepository).getKieModule(releaseId);
		verify(kieRepository).addKieModule(newKieModule);
		verify(kieRepository).removeKieModule(releaseId);
		verify(kieServices).newKieContainer(releaseId);
		//once in init, once in createKieModule
		verify(kieServices, times(2)).newReleaseId(anyString(), anyString(), anyString());
	}

	@Test
	public void testInitializeAllRulesModulesWhenRuleEngineIsInactive()
	{
		activateRuleEngine(false);
		final List<RuleEngineActionResult> initializeAllRulesModules = service.initializeAllRulesModules();
		assertThat(initializeAllRulesModules, is(not(nullValue())));
		final RuleEngineActionResult ruleEngineActionResult = initializeAllRulesModules.get(0);
		assertThat(ruleEngineActionResult, is(not(nullValue())));
		assertTrue(ruleEngineActionResult.isActionFailed());
		assertThat(ruleEngineActionResult.getMessagesAsString(MessageLevel.WARNING), is(not(nullValue())));
	}

}
