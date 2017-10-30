/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesetaxinvoicefacades.facades.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoicefacades.facades.TaxInvoiceCheckoutFacade;
import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;
import de.hybris.platform.chinesetaxinvoiceservices.services.TaxInvoiceService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;

import org.springframework.beans.factory.annotation.Required;


public class ChineseTaxInvoiceCheckoutFacade extends DefaultAcceleratorCheckoutFacade implements TaxInvoiceCheckoutFacade
{

	private TaxInvoiceService taxInvoiceService;

	private Populator<CartModel, CartData> cartTaxInvoicePopulator;

	private Populator<TaxInvoiceData, TaxInvoiceModel> taxInvoiceReversePopulator;

	@Override
	public boolean setTaxInvoice(final TaxInvoiceData data)
	{

		final CartModel cartModel = getCart();

		if (cartModel != null)
		{

			final TaxInvoiceModel taxInvoiceModel = taxInvoiceService.createTaxInvoice(data);
			taxInvoiceReversePopulator.populate(data, taxInvoiceModel);
			getModelService().save(taxInvoiceModel);
			getModelService().refresh(taxInvoiceModel);

			cartModel.setTaxInvoice(taxInvoiceModel);
			getModelService().save(cartModel);
			getModelService().refresh(cartModel);

			return true;
		}

		return false;
	}

	@Override
	public boolean removeTaxInvoice(final String code)
	{

		final TaxInvoiceModel invoiceModel = taxInvoiceService.getTaxInvoiceForCode(code);

		if (invoiceModel != null)
		{

			getModelService().remove(invoiceModel);

			final CartModel cartModel = getCart();
			cartModel.setTaxInvoice(null);
			getModelService().save(cartModel);
			getModelService().refresh(cartModel);

			return true;
		}

		return false;
	}

	@Override
	public CartData getCheckoutCart()
	{

		final CartModel cartModel = getCart();
		final CartData cartData = super.getCheckoutCart();
		cartTaxInvoicePopulator.populate(cartModel, cartData);

		return cartData;
	}

	@Override
	public boolean hasTaxInvoice()
	{

		return getCart().getTaxInvoice() != null;
	}

	protected TaxInvoiceService getTaxInvoiceService()
	{
		return taxInvoiceService;
	}

	@Required
	public void setTaxInvoiceService(TaxInvoiceService taxInvoiceService)
	{
		this.taxInvoiceService = taxInvoiceService;
	}

	protected Populator<CartModel, CartData> getCartTaxInvoicePopulator()
	{
		return cartTaxInvoicePopulator;
	}

	@Required
	public void setCartTaxInvoicePopulator(Populator<CartModel, CartData> cartTaxInvoicePopulator)
	{
		this.cartTaxInvoicePopulator = cartTaxInvoicePopulator;
	}

	protected Populator<TaxInvoiceData, TaxInvoiceModel> getTaxInvoiceReversePopulator()
	{
		return taxInvoiceReversePopulator;
	}

	@Required
	public void setTaxInvoiceReversePopulator(Populator<TaxInvoiceData, TaxInvoiceModel> taxInvoiceReversePopulator)
	{
		this.taxInvoiceReversePopulator = taxInvoiceReversePopulator;
	}





}
