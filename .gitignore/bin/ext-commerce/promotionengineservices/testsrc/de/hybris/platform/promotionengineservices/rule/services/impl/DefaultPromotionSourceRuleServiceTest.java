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
package de.hybris.platform.promotionengineservices.rule.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleTemplateModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultPromotionSourceRuleServiceTest
{
	@Mock
	private RuleDao ruleDao;
	@Mock
	private ModelService modelService;
	@Mock
	private KeyGenerator sourceRuleCodeGenerator;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private PromotionSourceRuleModel sourceRuleModel;
	@Mock
	private PromotionSourceRuleTemplateModel sourceRuleTemplateModel;
	@Mock
	private LanguageModel language1;
	@Mock
	private LanguageModel language2;

	@InjectMocks
	private final DefaultPromotionSourceRuleService promotionSourceRuleService = new DefaultPromotionSourceRuleService();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(modelService.create(PromotionSourceRuleModel.class)).thenReturn(sourceRuleModel);
		when(commonI18NService.getAllLanguages()).thenReturn(Lists.newArrayList(language1, language2));
		when(commonI18NService.getLocaleForLanguage(language1)).thenReturn(Locale.US);
		when(commonI18NService.getLocaleForLanguage(language2)).thenReturn(Locale.GERMANY);

	}

	@Test
	public void getAllPromotionsToPublishHappyTest()
	{
		//given
		final List<PromotionSourceRuleModel> promotionsToPublish = Lists.newArrayList(sourceRuleModel);
		when(ruleDao.findAllToBePublishedRulesByType(PromotionSourceRuleModel.class)).thenReturn(promotionsToPublish);

		//when
		final List<? extends AbstractRuleModel> promotionsTestResultList = promotionSourceRuleService.getAllToBePublishedRules();

		//then
		assertNotNull(promotionsTestResultList);
		assertTrue(promotionsTestResultList.size() == 1);
		assertTrue(promotionsTestResultList.get(0) instanceof PromotionSourceRuleModel);
		assertEquals(sourceRuleModel, promotionsTestResultList.get(0));
	}

	@Test
	public void createPromotionRuleFromTemplateNullTest()
	{
		//given

		//when
		final PromotionSourceRuleModel createdTestPromotion = promotionSourceRuleService.createPromotionRuleFromTemplate(null);

		//then
		assertNull(createdTestPromotion);
	}

	@Test
	public void createPromotionRuleFromTemplateTest()
	{
		//given
		final String generatedCode = "1234";
		final String testCode = "templateName";
		final Integer maxAllowedRuns = Integer.valueOf(5);
		final String conditions = "testConditions";
		final String actions = "testActions";
		final String nameLocale1 = "name1";
		final String nameLocale2 = "name2";
		final String descriptionLocale1 = "description1";
		final String descriptionLocale2 = "description2";
		final String firedMessageLocale1 = "firedMessage1";
		final String firedMessageLocale2 = "firedMessage2";

		when(sourceRuleCodeGenerator.generate()).thenReturn(generatedCode);
		when(sourceRuleTemplateModel.getCode()).thenReturn(testCode);
		when(sourceRuleTemplateModel.getMaxAllowedRuns()).thenReturn(maxAllowedRuns);
		when(sourceRuleTemplateModel.getConditions()).thenReturn(conditions);
		when(sourceRuleTemplateModel.getActions()).thenReturn(actions);
		when(sourceRuleTemplateModel.getName(Locale.US)).thenReturn(nameLocale1);
		when(sourceRuleTemplateModel.getName(Locale.GERMANY)).thenReturn(nameLocale2);
		when(sourceRuleTemplateModel.getDescription(Locale.US)).thenReturn(descriptionLocale1);
		when(sourceRuleTemplateModel.getDescription(Locale.GERMANY)).thenReturn(descriptionLocale2);
		when(sourceRuleTemplateModel.getMessageFired(Locale.US)).thenReturn(firedMessageLocale1);
		when(sourceRuleTemplateModel.getMessageFired(Locale.GERMANY)).thenReturn(firedMessageLocale2);

		//when
		promotionSourceRuleService.createPromotionRuleFromTemplate(sourceRuleTemplateModel);

		//then
		verify(sourceRuleModel, times(1)).setCode(testCode + "-" + generatedCode);
		verify(sourceRuleModel, times(1)).setMaxAllowedRuns(maxAllowedRuns);
		verify(sourceRuleModel, times(1)).setConditions(conditions);
		verify(sourceRuleModel, times(1)).setActions(actions);
		verify(sourceRuleModel, times(1)).setName(nameLocale1, Locale.US);
		verify(sourceRuleModel, times(1)).setName(nameLocale2, Locale.GERMANY);
		verify(sourceRuleModel, times(1)).setDescription(descriptionLocale1, Locale.US);
		verify(sourceRuleModel, times(1)).setDescription(descriptionLocale2, Locale.GERMANY);
		verify(sourceRuleModel, times(1)).setMessageFired(firedMessageLocale1, Locale.US);
		verify(sourceRuleModel, times(1)).setMessageFired(firedMessageLocale2, Locale.GERMANY);
	}
}
