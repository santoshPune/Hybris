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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotions.impl.DefaultPromotionResultService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.util.impl.DefaultMessagePlaceholderReplacementStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultPromotionEngineResultServiceTest
{

	private static final String RULE_PARAMETER_TYPE1 = "TYPE1";
	private static final String RULE_PARAMETER_TYPE2 = "TYPE2";
	private static final String FREE_GIFT_CODE = "product1234";
	private static final String FREE_GIFT_NAME = "the free gift of laughter";
	@InjectMocks
	private DefaultPromotionEngineResultService defaultPromotionEngineResultService;

	@Mock
	private DefaultPromotionResultService defaultPromotionResultService;

	@Mock
	private RuleParametersService ruleParametersService;

	@Mock
	private PromotionResultModel promotionResult;

	@Mock
	private RuleBasedPromotionModel ruleBasedPromotion;

	@Mock
	private AbstractOrderModel order;

	@Mock
	private CurrencyModel currency;

	@Mock
	private AbstractPromotionModel legacyPromotion;

	@Mock
	private AbstractRuleEngineRuleModel engineRule;

	@Mock
	private ProductService productService;

	@Mock
	private ProductModel freeGift;

	// YTODO turn this into a mock once PromotionOrderResults is no longer final @Mock
	private final PromotionOrderResults promotionOrderResults = new PromotionOrderResults(null, null, null, 0d);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		final Map<String, PromotionMessageParameterResolutionStrategy> resolutionStrategies = new HashMap<>();
		final DefaultCurrencyAmountResolutionStrategy currencyResolutionStrategy = new DefaultCurrencyAmountResolutionStrategy();
		final DefaultProductResolutionStrategy productResolutionStrategy = new DefaultProductResolutionStrategy();
		productResolutionStrategy.setProductService(productService);
		when(productService.getProductForCode(FREE_GIFT_CODE)).thenReturn(freeGift);
		when(freeGift.getName()).thenReturn(FREE_GIFT_NAME);

		resolutionStrategies.put(RULE_PARAMETER_TYPE1, currencyResolutionStrategy);
		resolutionStrategies.put(RULE_PARAMETER_TYPE2, productResolutionStrategy);
		defaultPromotionEngineResultService.setResolutionStrategies(resolutionStrategies);
		defaultPromotionEngineResultService.setReplacementStrategy(new DefaultMessagePlaceholderReplacementStrategy());


		// by default return the legacy promotion
		when(promotionResult.getPromotion()).thenReturn(legacyPromotion);
	}

	@Test
	public void testLegacyIsApplied()
	{
		final boolean result = defaultPromotionEngineResultService.isApplied(promotionResult);
		verify(defaultPromotionResultService, times(1)).isApplied(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyIsAppliedToOrder()
	{
		final boolean result = defaultPromotionEngineResultService.isAppliedToOrder(promotionResult);
		verify(defaultPromotionResultService, times(1)).isAppliedToOrder(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyApply()
	{
		final boolean result = defaultPromotionEngineResultService.apply(promotionResult);
		verify(defaultPromotionResultService, times(1)).apply(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyUndo()
	{
		final boolean result = defaultPromotionEngineResultService.undo(promotionResult);
		verify(defaultPromotionResultService, times(1)).undo(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyGetFired()
	{
		final boolean result = defaultPromotionEngineResultService.getFired(promotionResult);
		verify(defaultPromotionResultService, times(1)).getFired(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyGetCouldFire()
	{
		final boolean result = defaultPromotionEngineResultService.getCouldFire(promotionResult);
		verify(defaultPromotionResultService, times(1)).getCouldFire(promotionResult);
		assertFalse(result);
	}

	@Test
	public void testLegacyGetConsumedCount()
	{
		final long result = defaultPromotionEngineResultService.getConsumedCount(promotionResult, false);
		verify(defaultPromotionResultService, times(1)).getConsumedCount(promotionResult, false);
		assertEquals(0L, result);
	}

	@Test
	public void testLegacyGetTotalDiscount()
	{
		final double result = defaultPromotionEngineResultService.getTotalDiscount(promotionResult);
		verify(defaultPromotionResultService, times(1)).getTotalDiscount(promotionResult);
		assertEquals(0d, result, 0d);
	}

	@Test
	public void testLegacyGetPotentialProductPromotions()
	{
		final List<PromotionResultModel> result = defaultPromotionEngineResultService.getPotentialProductPromotions(
				promotionOrderResults, legacyPromotion);
		verify(defaultPromotionResultService, times(1)).getPotentialProductPromotions(promotionOrderResults, legacyPromotion);
		assertNotNull(result);
	}

	@Test
	public void testLegacyGetPotentialOrderPromotions()
	{
		final List<PromotionResultModel> result = defaultPromotionEngineResultService.getPotentialOrderPromotions(
				promotionOrderResults, legacyPromotion);
		verify(defaultPromotionResultService, times(1)).getPotentialOrderPromotions(promotionOrderResults, legacyPromotion);
		assertNotNull(result);
	}

	@Test
	public void testLegacyGetFiredProductPromotions()
	{
		final List<PromotionResultModel> result = defaultPromotionEngineResultService.getFiredProductPromotions(
				promotionOrderResults, legacyPromotion);
		verify(defaultPromotionResultService, times(1)).getFiredProductPromotions(promotionOrderResults, legacyPromotion);
		assertNotNull(result);
	}

	@Test
	public void testLegacyGetFiredOrderPromotions()
	{
		final List<PromotionResultModel> result = defaultPromotionEngineResultService.getFiredOrderPromotions(
				promotionOrderResults, legacyPromotion);
		verify(defaultPromotionResultService, times(1)).getFiredOrderPromotions(promotionOrderResults, legacyPromotion);
		assertNotNull(result);
	}

	@Test
	public void testLegacyGetDescription()
	{
		final String RESULT_STRING = "LEGACY_GET_DESCRIPTION";
		when(defaultPromotionResultService.getDescription(promotionResult, Locale.US)).thenReturn(RESULT_STRING);
		final String result = defaultPromotionEngineResultService.getDescription(promotionResult, Locale.US);
		verify(defaultPromotionResultService, times(1)).getDescription(promotionResult, Locale.US);
		assertEquals(RESULT_STRING, result);
	}

	@Test
	public void testGetDescription() throws RuleConverterException
	{
		final String NOTHING_TO_REPLACE = "Nothing to replace";

		when(promotionResult.getPromotion()).thenReturn(ruleBasedPromotion);
		when(ruleBasedPromotion.getRule()).thenReturn(engineRule);
		when(promotionResult.getOrder()).thenReturn(order);
		when(order.getCurrency()).thenReturn(currency);
		when(currency.getIsocode()).thenReturn("USD");
		when(currency.getDigits()).thenReturn(Integer.valueOf(2));
		// message parameter setup
		final String UUID_1 = "1234-5678";
		final String UUID_2 = "5678-9012";


		final String PARAMETERS_STRING = "parametersString";
		final List<RuleParameterData> parameters = new ArrayList<>();
		when(engineRule.getRuleParameters()).thenReturn(PARAMETERS_STRING);
		when(ruleParametersService.convertParametersFromString(PARAMETERS_STRING)).thenReturn(parameters);

		final RuleParameterData data1 = new RuleParameterData();
		data1.setUuid(UUID_1);
		data1.setType(RULE_PARAMETER_TYPE1);
		final Map<String, BigDecimal> value1 = Collections.singletonMap("USD", new BigDecimal("20.00"));
		data1.setValue(value1);
		parameters.add(data1);

		final RuleParameterData data2 = new RuleParameterData();
		data2.setUuid(UUID_2);
		data2.setType(RULE_PARAMETER_TYPE2);
		final String value2 = FREE_GIFT_CODE;
		data2.setValue(value2);
		parameters.add(data2);

		// message fired without parameter references
		when(ruleBasedPromotion.getMessageFired()).thenReturn(NOTHING_TO_REPLACE);
		final String resultNoReplacement = defaultPromotionEngineResultService.getDescription(promotionResult, Locale.US);
		assertEquals(NOTHING_TO_REPLACE, resultNoReplacement);

		// message fired with two parameters resolved
		when(ruleBasedPromotion.getMessageFired()).thenReturn(
				"You spent {" + UUID_1 + "} and qualify for {" + UUID_2 + "}. Yay you!");
		final String resultWithReplacements = defaultPromotionEngineResultService.getDescription(promotionResult, Locale.US);
		assertEquals("You spent $20.00 and qualify for " + FREE_GIFT_NAME + ". Yay you!", resultWithReplacements);

	}
}
