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
package de.hybris.platform.ruleengine.dao.interceptors;

import static java.lang.Boolean.valueOf;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.impl.RuleEngineRuleModelChecksumCalculator;
import de.hybris.platform.ruleengine.versioning.impl.RuleEngineRuleModelValidator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RuleEngineRuleValidateInterceptorUnitTest
{
	private final static String RULE_MODULE_NAME = "test_rule_module";

	private final static String RULE_CONTENT = "rule content";
	private final static String RULE_CHECKSUM = "RULE_CHECKSUM";
	private final static Long RULE_VERSION = 1l;
	private final static String RULE_CODE = "test_code";
	private final static String UUID_STRING = UUID.randomUUID().toString();

	@InjectMocks
	private RuleEngineRuleValidateInterceptor validateInterceptor;
	@InjectMocks
	private RuleEngineRuleModelValidator validator;
	private MockRuleModelChecksumCalculator ruleModelChecksumCalculator;
	@Mock
	private EngineRuleDao engineRuleDao;

	private DroolsRuleModel model;
	@Mock
	private InterceptorContext context;
	@Mock
	private DroolsKIEBaseModel kieBaseModel;
	@Mock
	private DroolsKIEModuleModel kieModuleModel;

	@Before
	public void setUp()
	{
		ruleModelChecksumCalculator = spy(new MockRuleModelChecksumCalculator());
		validator.setRuleModelChecksumCalculator(ruleModelChecksumCalculator);

		validateInterceptor.setValidator(validator);

		model = new DroolsRuleModel();
		model.setCode(RULE_CODE);
		model.setUuid(UUID_STRING);
		model.setVersion(RULE_VERSION);
		model.setRuleContent(RULE_CONTENT);
		model.setChecksum(RULE_CHECKSUM);
		model.setActive(Boolean.TRUE);
		model.setKieBase(kieBaseModel);

		when(kieBaseModel.getKieModule()).thenReturn(kieModuleModel);
		when(kieModuleModel.getName()).thenReturn(RULE_MODULE_NAME);

		defineRegularUpdateBehaviour();
	}

	@Test
	public void testOnValidateNew() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateNewActiveNotSet() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(null);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateNewMustBeActive() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testOnValidateNewNonActiveRuleWithContent() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateNewNonActiveRuleWithContent() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION - 1);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testOnValidateNewNonActiveRuleWithContent2() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION + 1);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testOnValidateNewNonActiveRuleContentNotSet() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION);
		model.setRuleContent(null);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateNewNonActiveRuleNoContent() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION + 1);
		model.setRuleContent(null);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateNewNonActiveRuleNoContent2() throws InterceptorException
	{
		when(engineRuleDao.getCurrentRulesSnapshotVersion(kieModuleModel)).thenReturn(RULE_VERSION - 1);
		model.setRuleContent(null);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testFailOnValidateModifiedNonActiveRuleVersionsEqual() throws InterceptorException
	{
		when(engineRuleDao.getRuleVersion(kieModuleModel, RULE_CODE)).thenReturn(RULE_VERSION);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateModifiedNonActiveRuleVersionsNotEqual() throws InterceptorException
	{
		when(engineRuleDao.getRuleVersion(kieModuleModel, RULE_CODE)).thenReturn(RULE_VERSION + 1);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.FALSE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateModifiedActiveRuleNoContent() throws InterceptorException
	{
		model.setRuleContent(null);

		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateModifiedActiveRuleWithContentWrongChecksum() throws InterceptorException
	{
		model.setChecksum("WRONG_CHECKSUM");

		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testOnValidateModifiedActiveRule() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test(expected = InterceptorException.class)
	public void testFailOnValidateRemovedActiveRule() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isRemoved(model))).thenReturn(Boolean.TRUE);

		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testValidateNoKieBase() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		when(valueOf(context.isRemoved(model))).thenReturn(Boolean.FALSE);

		model.setKieBase(null);

		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	@Test
	public void testValidateNoKieModule() throws InterceptorException
	{
		when(valueOf(context.isNew(model))).thenReturn(Boolean.FALSE);
		when(valueOf(context.isModified(model))).thenReturn(Boolean.TRUE);
		when(valueOf(context.isRemoved(model))).thenReturn(Boolean.FALSE);

		when(kieBaseModel.getKieModule()).thenReturn(null);

		model.setActive(Boolean.TRUE);
		validateInterceptor.onValidate(model, context);
	}

	private void defineRegularUpdateBehaviour()
	{
		when(ruleModelChecksumCalculator.calculateContentChecksum(RULE_CONTENT)).thenReturn(RULE_CHECKSUM);
	}

	public static class MockRuleModelChecksumCalculator extends RuleEngineRuleModelChecksumCalculator
	{
		@Override
		protected String calculateContentChecksum(final String ruleContent) //NOPMD
		{
			return super.calculateContentChecksum(ruleContent);
		}
	}

}
