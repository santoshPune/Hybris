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
package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.model.ItemContextBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;


public class CartModelBuilder
{
	private CartModelBuilder()
	{
		// no public constructors
	}

	public static CartModelDraft newCart(final String code)
	{
		return new CartModelDraft().setCode(code);
	}

	public static class CartModelDraft
	{
		private final CartModel cartModel = new CartModel();
		{
			cartModel.setEntries(new ArrayList<AbstractOrderEntryModel>());
			cartModel.setDiscounts(new ArrayList<DiscountModel>());
		}

		public CartModelDraft setCode(final String code)
		{
			cartModel.setCode(code);
			return this;
		}

		public CartModelDraft setCurrency(final String currencyCode)
		{
			final CurrencyModel currencyModel = new CurrencyModel();
			currencyModel.setIsocode(currencyCode);
			cartModel.setCurrency(currencyModel);
			return this;
		}

		public CartModelDraft setTotalPrice(final double totalPrice)
		{
			cartModel.setTotalPrice(Double.valueOf(totalPrice));
			return this;
		}

		public CartModelDraft setDelivery(final String deliveryModeCode, final double deliveryCost)
		{
			final DeliveryModeModel deliveryModeModel = new DeliveryModeModel();
			deliveryModeModel.setCode(deliveryModeCode);
			cartModel.setDeliveryMode(deliveryModeModel);
			cartModel.setDeliveryCost(Double.valueOf(deliveryCost));
			return this;
		}

		public CartModelDraft addProduct(final String productCode, final int quantity, final double basePrice,
				final int entryNumber, final String... categories)
		{
			final ItemContextBuilder orderEntryBuilder = ItemContextBuilder.createDefaultBuilder(CartEntryModel.class);
			orderEntryBuilder.setLocaleProvider(new StubLocaleProvider(Locale.ENGLISH));
			final ItemContextBuilder productBuilder = ItemContextBuilder.createDefaultBuilder(ProductModel.class);
			productBuilder.setLocaleProvider(new StubLocaleProvider(Locale.ENGLISH));
			final ItemContextBuilder catalogVersionModelBuilder = ItemContextBuilder.createDefaultBuilder(CatalogVersionModel.class);
			catalogVersionModelBuilder.setLocaleProvider(new StubLocaleProvider(Locale.ENGLISH));
			final ItemContextBuilder catalogModelBuilder = ItemContextBuilder.createDefaultBuilder(CatalogModel.class);
			catalogModelBuilder.setLocaleProvider(new StubLocaleProvider(Locale.ENGLISH));
			final ItemContextBuilder unitModelBuilder = ItemContextBuilder.createDefaultBuilder(UnitModel.class);
			unitModelBuilder.setLocaleProvider(new StubLocaleProvider(Locale.ENGLISH));

			final AbstractOrderEntryModel entryModel = new CartEntryModel(orderEntryBuilder.build());
			final ProductModel product = new ProductModel(productBuilder.build());
			product.setCode(productCode);
			final CatalogVersionModel catalogVersionModel = new CatalogVersionModel(catalogVersionModelBuilder.build());
			catalogVersionModel.setVersion("Online");
			final CatalogModel catalogModel = new CatalogModel(catalogModelBuilder.build());
			catalogModel.setId("testCatalog");
			catalogVersionModel.setCatalog(catalogModel);
			product.setCatalogVersion(catalogVersionModel);

			product.setSupercategories(new ArrayList<CategoryModel>());
			Arrays.asList(categories).stream().forEach((catCode) -> {
				final CategoryModel catModel = new CategoryModel();
				catModel.setCode(catCode);
				product.getSupercategories().add(catModel);
			});

			entryModel.setProduct(product);
			entryModel.setQuantity(Long.valueOf(quantity));
			entryModel.setBasePrice(Double.valueOf(basePrice));
			entryModel.setOrder(cartModel);
			entryModel.setEntryNumber(Integer.valueOf(entryNumber));
			final UnitModel unitModel = new UnitModel(unitModelBuilder.build());
			unitModel.setCode("pieces");
			unitModel.setUnitType("pieces");
			entryModel.setUnit(unitModel);
			cartModel.getEntries().add(entryModel);
			return this;
		}

		public CartModelDraft addDiscount(final String discountCurrencyCode, final double discountValue)
		{
			final DiscountModel discountModel = new DiscountModel();
			final CurrencyModel currencyModel = new CurrencyModel();
			currencyModel.setIsocode(discountCurrencyCode);
			discountModel.setCurrency(currencyModel);
			discountModel.setValue(Double.valueOf(discountValue));
			cartModel.getDiscounts().add(discountModel);
			return this;
		}

		public CartModelDraft setUser(final String userId, final String... userGroupIds)
		{
			final CustomerModel customer = new CustomerModel();
			customer.setUid(userId);
			customer.setGroups(new LinkedHashSet<PrincipalGroupModel>());

			Arrays.asList(userGroupIds).stream().forEach((customerGroupId) -> {
				final UserGroupModel userGroupModel = new UserGroupModel();
				userGroupModel.setUid(customerGroupId);
				customer.getGroups().add(userGroupModel);
			});

			cartModel.setUser(customer);
			return this;
		}

		public CartModel getModel()
		{
			return cartModel;
		}
	}
}
