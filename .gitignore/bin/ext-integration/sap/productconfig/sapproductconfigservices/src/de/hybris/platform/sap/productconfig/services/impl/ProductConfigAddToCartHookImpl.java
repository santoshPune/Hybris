/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class ProductConfigAddToCartHookImpl implements CommerceAddToCartMethodHook
{
	private final static Logger LOG = Logger.getLogger(ProductConfigAddToCartHookImpl.class);

	private ModelService modelService;
	private String configurableSource;

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @param configurableSource
	 *           source for the configurable attribute if the sap model is active it will be sapconfigurable
	 */
	@Required
	public void setConfigurableSource(final String configurableSource)
	{
		this.configurableSource = configurableSource;
	}


	@Override
	public void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		LOG.debug("ProductConfig beforeAddToCart start");
		final CartModel toCart = parameters.getCart();
		final Boolean sapConfigurable = (Boolean) getProductAttribute(parameters.getProduct(), configurableSource);

		if (sapConfigurable != null && sapConfigurable.booleanValue())
		{
			if (!parameters.isCreateNewEntry() && LOG.isDebugEnabled())
			{
				LOG.debug("Changing 'createNewEntry' from 'false' to 'true' for CartGUID=" + toCart.getGuid() + "; product="
						+ parameters.getProduct().getCode() + "; sapConfigurable=" + sapConfigurable);
			}
			// configurable products should always fore a new cart item
			parameters.setCreateNewEntry(true);
		}
		LOG.debug("ProductConfig beforeAddToCart end");
	}



	@Override
	public void afterAddToCart(final CommerceCartParameter parameters, final CommerceCartModification result)
			throws CommerceCartModificationException
	{
		// not required
	}

	/**
	 * Get an attribute value from a product. If the attribute value is null and the product is a variant then the same
	 * attribute will be requested from the base product.
	 *
	 * @param productModel
	 *           the product
	 * @param attribute
	 *           the name of the attribute to lookup
	 * @return the value of the attribute
	 */
	protected Object getProductAttribute(final ProductModel productModel, final String attribute)
	{
		final Object value = modelService.getAttributeValue(productModel, attribute);
		if (value == null && productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (baseProduct != null)
			{
				return getProductAttribute(baseProduct, attribute);
			}
		}
		return value;
	}

}
