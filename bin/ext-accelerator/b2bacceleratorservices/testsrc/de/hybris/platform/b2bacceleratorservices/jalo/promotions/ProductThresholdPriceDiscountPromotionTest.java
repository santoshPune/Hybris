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
 */
package de.hybris.platform.b2bacceleratorservices.jalo.promotions;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.AbstractPromotionTest;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ProductThresholdPriceDiscountPromotionTest extends AbstractPromotionTest
{
	private static final double DELTA = 0.01;
	private ProductModel product1;
	private ProductModel product2;
	private CartModel cart;
	private PromotionGroupModel promotionGroup;

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private CartService cartService;
	@Resource
	private CalculationService calculationService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ModelService modelService;
	@Resource
	private PromotionsService defaultPromotionsService;


	/**
	 * HW1210-3411: 253 Euro, HW2310-1001: 29.90 Euro
	 */
	@Before
	public void setup() throws Exception
	{
		super.setUp();

		final CatalogVersionModel version = catalogVersionService.getCatalogVersion("hwcatalog", "Online");
		catalogVersionService.addSessionCatalogVersion(version);
		product1 = productService.getProductForCode(version, "HW1210-3411");
		product2 = productService.getProductForCode(version, "HW2310-1001");
		userService.setCurrentUser(userService.getUserForUID("demo"));
		commonI18NService.setCurrentCurrency(commonI18NService.getCurrency("EUR"));

		importCsv("/b2bacceleratorservices/test/testProductThresholdPriceDiscountPromotion.csv", "UTF-8");
	}

	/**
	 * Entry total 253.0d is greater than threshold 100. Promotion should be applied
	 */
	@Test
	public void shouldApplyPromotion() throws CalculationException
	{
		final double cartTotal = 253.0d;
		final double cartTotalAfterPromotion = 203.0d;

		cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product1, 1, product1.getUnit());
		modelService.save(cart);
		calculationService.calculate(cart);
		assertEquals("cart total before updatePromotions(ProductThresholdPriceDiscountPromotion)", cartTotal,
				cart.getTotalPrice().doubleValue(), DELTA);

		promotionGroup = defaultPromotionsService.getPromotionGroup("pgProductThresholdPriceDiscountPromotionTest1");
		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
		promotionGroups.add(promotionGroup);
		defaultPromotionsService.updatePromotions(promotionGroups, cart, false, AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL,
				new Date());
		modelService.refresh(cart);
		assertEquals("cart total after updatePromotions(ProductFixedPricePromotion)", cartTotalAfterPromotion,
				cart.getTotalPrice().doubleValue(), DELTA);
	}

	/**
	 * Entry total 29.90d is less than threshold 100. Promotion should be not be applied
	 */
	@Test
	public void shouldNotApplyPromotion() throws CalculationException
	{
		final double cartTotal = 29.9d;

		cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product2, 1, product2.getUnit());
		modelService.save(cart);
		calculationService.calculate(cart);
		assertEquals("cart total before updatePromotions(ProductThresholdPriceDiscountPromotion)", cartTotal,
				cart.getTotalPrice().doubleValue(), DELTA);

		promotionGroup = defaultPromotionsService.getPromotionGroup("pgProductThresholdPriceDiscountPromotionTest1");
		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
		promotionGroups.add(promotionGroup);
		final PromotionOrderResults results = defaultPromotionsService.updatePromotions(promotionGroups, cart, false,
				AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL, new Date());
		modelService.refresh(cart);
		assertEquals("cart total after updatePromotions(ProductFixedPricePromotion)", cartTotal, cart.getTotalPrice().doubleValue(),
				DELTA);
		assertEquals("Size of PotentialProductPromotions should be 1", 1, results.getPotentialProductPromotions().size());
		assertEquals("cart total after updatePromotions(ProductFixedPricePromotion)", 0.299f,
				results.getPotentialProductPromotions().get(0).getCertainty().floatValue(), DELTA);
	}

}
