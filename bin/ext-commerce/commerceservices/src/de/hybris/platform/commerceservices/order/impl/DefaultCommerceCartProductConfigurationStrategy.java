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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartProductConfigurationStrategy;
import de.hybris.platform.commerceservices.order.ProductConfigurationHandler;
import de.hybris.platform.commerceservices.order.ProductConfigurationHandlerFactory;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.ProductConfigurationItem;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link CommerceCartProductConfigurationStrategy}.
 */
public class DefaultCommerceCartProductConfigurationStrategy implements CommerceCartProductConfigurationStrategy
{
	private ProductConfigurationHandlerFactory configurationHandlerFactory;
	private ModelService modelService;
	private CartService cartService;

	@Override
	public void configureCartEntry(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		validateParameters(parameters);
		Map<ConfiguratorType, List<ProductConfigurationItem>> byConfiguratorType
				= parameters.getProductConfiguration().stream()
				.collect(Collectors.groupingBy(ProductConfigurationItem::getConfiguratorType));
		final AbstractOrderEntryModel entry = getCartService()
				.getEntryForNumber(parameters.getCart(), (int) parameters.getEntryNumber());
		for (Map.Entry<ConfiguratorType, List<ProductConfigurationItem>> configuratorTypeListEntry : byConfiguratorType.entrySet())
		{
			ConfiguratorType configuratorType = configuratorTypeListEntry.getKey();
			if (configuratorType != null)
			{
				final List<AbstractOrderEntryProductInfoModel> configs = new ArrayList<>();
                if (entry.getProductInfos() != null)
				{
                    entry.getProductInfos().stream()
							.filter(item -> item.getConfiguratorType() != configuratorType)
							.forEach(configs::add);
                }
                final ProductConfigurationHandler handler
                        = getConfigurationHandlerFactory().handlerOf(configuratorType);
                if (handler == null)
                {
                    throw new CommerceCartModificationException(
                            "No handler for configuration type " + configuratorType.getCode());
                }
                final List<AbstractOrderEntryProductInfoModel> model 
                        = handler.convert(configuratorTypeListEntry.getValue(), entry);
				model.forEach(item -> item.setOrderEntry(entry));
				modelService.saveAll(model);
				configs.addAll(model);
				entry.setProductInfos(configs);
				modelService.save(entry);
			}
		}

	}

	protected void validateParameters(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		if (parameters.getCart() == null)
		{
			throw new CommerceCartModificationException("Null cart");
		}
		if (parameters.getCart().getEntries() == null)
		{
			throw new CommerceCartModificationException("Cart has no entries");
		}
	}

	protected ProductConfigurationHandlerFactory getConfigurationHandlerFactory()
	{
		return configurationHandlerFactory;
	}

	@Required
	public void setConfigurationHandlerFactory(final ProductConfigurationHandlerFactory configurationHandlerFactory)
	{
		this.configurationHandlerFactory = configurationHandlerFactory;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}
