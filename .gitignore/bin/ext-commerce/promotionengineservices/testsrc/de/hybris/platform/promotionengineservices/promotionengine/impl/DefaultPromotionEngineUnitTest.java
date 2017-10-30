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
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultPromotionEngineUnitTest
{
	private static final String RULE_BASED_PROMOTION_DESC = "RuleBasedPromotion description";
	private static final String NOT_RULE_BASED_PROMOTION_DESC = "Not RuleBasedPromotion description";

	@Mock
	private RuleBasedPromotionModel ruleBasedPromotion;

	@Mock
	private AbstractPromotionModel abstractPromotion;

	@InjectMocks
	private DefaultPromotionEngineService defaultPromotionEngineService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(ruleBasedPromotion.getPromotionDescription()).thenReturn(RULE_BASED_PROMOTION_DESC);
		when(abstractPromotion.getDescription()).thenReturn(NOT_RULE_BASED_PROMOTION_DESC);
	}

	@Test
	public void testGetPromotionDescriptionRuleBasedPromotion()
	{
		final String result = defaultPromotionEngineService.getPromotionDescription(ruleBasedPromotion);
		assertEquals(RULE_BASED_PROMOTION_DESC, result);
	}

	@Test
	public void testGetPromotionDescriptionNotRuleBasedPromotion()
	{
		final String result = defaultPromotionEngineService.getPromotionDescription(abstractPromotion);
		assertEquals(NOT_RULE_BASED_PROMOTION_DESC, result);
	}
}
