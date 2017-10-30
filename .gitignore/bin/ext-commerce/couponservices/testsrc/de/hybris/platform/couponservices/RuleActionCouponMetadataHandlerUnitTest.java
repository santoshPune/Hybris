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
package de.hybris.platform.couponservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAdjustTotalActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RuleActionCouponMetadataHandlerUnitTest
{
	@InjectMocks
	private RuleActionCouponMetadataHandler ruleActionCouponMetadataHandler;

	@Mock
	private PromotionResultModel promotionResultModel;

	@Mock
	private AbstractOrderModel orderModel;

	private static final String COUPON_CODE = "CPN1";

	@Test
	public void testHandle()
	{
		ruleActionCouponMetadataHandler = spy(ruleActionCouponMetadataHandler);
		final AbstractRuleBasedPromotionActionModel actionModel = new RuleBasedOrderAdjustTotalActionModel();
		actionModel.setPromotionResult(promotionResultModel);
		actionModel.setUsedCouponCodes(new ArrayList<String>());
		when(promotionResultModel.getOrder()).thenReturn(orderModel);
		when(orderModel.getAllPromotionResults()).thenReturn(Collections.singleton(promotionResultModel));
		when(orderModel.getAppliedCouponCodes()).thenReturn(Collections.singleton(COUPON_CODE));
		when(promotionResultModel.getActions()).thenReturn(Collections.singleton(actionModel));
		when(ruleActionCouponMetadataHandler.getMetadataId()).thenReturn("cpnHandler");
		ruleActionCouponMetadataHandler.handle(actionModel, "CPN1, CPN2");
		assertEquals(1, actionModel.getUsedCouponCodes().size());
		assertTrue(actionModel.getUsedCouponCodes().contains("CPN1"));
		assertEquals(1, actionModel.getMetadataHandlers().size());
		assertTrue(actionModel.getMetadataHandlers().contains("cpnHandler"));
	}

	@Test
	public void testUndoHandle()
	{
		ruleActionCouponMetadataHandler = spy(ruleActionCouponMetadataHandler);
		final AbstractRuleBasedPromotionActionModel actionModel = new RuleBasedOrderAdjustTotalActionModel();
		actionModel.setPromotionResult(promotionResultModel);
		actionModel.setUsedCouponCodes(new ArrayList<String>());
		actionModel.setMetadataHandlers(Collections.singletonList("anotherCpnHandler"));
		when(promotionResultModel.getOrder()).thenReturn(orderModel);
		when(orderModel.getAllPromotionResults()).thenReturn(Collections.singleton(promotionResultModel));
		when(orderModel.getAppliedCouponCodes()).thenReturn(Collections.singleton(COUPON_CODE));
		when(promotionResultModel.getActions()).thenReturn(Collections.singleton(actionModel));
		when(ruleActionCouponMetadataHandler.getMetadataId()).thenReturn("cpnHandler");
		ruleActionCouponMetadataHandler.handle(actionModel, "CPN1, CPN2");
		ruleActionCouponMetadataHandler.undoHandle(actionModel);
		assertEquals(1, actionModel.getMetadataHandlers().size());
		assertTrue(actionModel.getMetadataHandlers().contains("anotherCpnHandler"));
	}
}
