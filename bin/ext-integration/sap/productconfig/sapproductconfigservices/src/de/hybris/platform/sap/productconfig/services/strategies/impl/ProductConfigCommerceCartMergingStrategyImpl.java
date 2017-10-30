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
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartMergingStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class ProductConfigCommerceCartMergingStrategyImpl extends DefaultCommerceCartMergingStrategy
{

	private static final Logger LOG = Logger.getLogger(ProductConfigCommerceCartMergingStrategyImpl.class);

	private String configurableSource;


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
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("ProductConfig before mergeCarts, fromCart=" + fromCart.getGuid() + "; toCart=" + toCart.getGuid());
		}

		final Map<String, List<String>> collectedConfigs = collectConfigsBeforeMerge(fromCart);
		super.mergeCarts(fromCart, toCart, modifications);
		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = findEntiesWithMissingConfig(toCart);
		final int changeCounter = reApplyConfigsAfterMerge(missingConfigs, collectedConfigs);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Restored " + changeCounter + " configurations in cart " + toCart.getGuid()
					+ (changeCounter > 0 ? "; saving cart" : "; not saving cart"));
		}

		if (changeCounter > 0)
		{
			getModelService().save(toCart);
		}

		LOG.debug("ProductConfig after mergeCarts");
	}

	protected Map<String, List<String>> collectConfigsBeforeMerge(final CartModel fromCart)
	{
		final Map<String, List<String>> collectedConfigs = new HashMap(fromCart.getEntries().size());
		for (final AbstractOrderEntryModel entry : fromCart.getEntries())
		{
			final ProductModel product = entry.getProduct();
			final Boolean isConfigurable = (Boolean) getProductAttribute(product, configurableSource);
			if (isConfigurable != null && isConfigurable.booleanValue())
			{
				final String productCode = product.getCode();
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Storing external Configuration for product=" + productCode);
				}
				List<String> configList;
				if (collectedConfigs.containsKey(productCode))
				{
					configList = addToConfigListForMultipleOccurences(collectedConfigs, entry.getExternalConfiguration(),
							productCode);
				}
				else
				{
					// 99% case, a product is only once in cart, hence creating SingeltonList
					configList = Collections.singletonList(entry.getExternalConfiguration());
				}
				collectedConfigs.put(productCode, configList);
			}
		}
		if (LOG.isDebugEnabled())
		{
			final int numberCollectedConfigs = countCollectedConfigs(collectedConfigs);
			LOG.debug("Saved " + numberCollectedConfigs + " externalConfigs from cart " + fromCart.getGuid());
		}
		return collectedConfigs;
	}



	protected int reApplyConfigsAfterMerge(final Map<String, List<AbstractOrderEntryModel>> missingConfigs,
			final Map<String, List<String>> collectedConfigs) throws CommerceCartMergingException
	{
		int changeCounter = 0;
		final int numberCollectedConfigs = countCollectedConfigs(collectedConfigs);
		final int numberMissingConfigs = countMissingConfigs(missingConfigs);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Restoring " + numberCollectedConfigs + " collected configs to " + numberMissingConfigs + " missing configs");
		}
		if (numberCollectedConfigs != numberMissingConfigs)
		{
			throw new CommerceCartMergingException("Merge created inconsitentc cart, configSaved=" + numberCollectedConfigs
					+ "; but missingConfigs=" + numberMissingConfigs);
		}

		for (final Entry<String, List<String>> collectedConfigEntry : collectedConfigs.entrySet())
		{
			final List<AbstractOrderEntryModel> missingConfigEntry = missingConfigs.get(collectedConfigEntry.getKey());
			int ii = 0;
			for (final String extnernalConfig : collectedConfigEntry.getValue())
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Restoring external config for cartItemPK=" + missingConfigEntry.get(ii).getPk() + "; which is item #"
							+ ii + " with productCode=" + collectedConfigEntry.getKey());
				}

				missingConfigEntry.get(ii).setExternalConfiguration(extnernalConfig);
				ii++;
				changeCounter++;
			}
		}
		return changeCounter;
	}




	protected Map<String, List<AbstractOrderEntryModel>> findEntiesWithMissingConfig(final CartModel toCart)
	{
		final Map<String, List<AbstractOrderEntryModel>> missingConfigs = new HashMap(toCart.getEntries().size());

		for (final AbstractOrderEntryModel entry : toCart.getEntries())
		{
			final String externalConfig = entry.getExternalConfiguration();
			final ProductModel product = entry.getProduct();
			final Boolean isConfigurable = (Boolean) getProductAttribute(product, configurableSource);
			if (isConfigurable != null && isConfigurable.booleanValue() && (externalConfig == null || externalConfig.isEmpty()))
			{
				final String productCode = product.getCode();
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Missing external Configuration for cartEntry=" + entry.getPk() + "; product=" + productCode);
				}
				List<AbstractOrderEntryModel> entryList;
				if (missingConfigs.containsKey(productCode))
				{
					entryList = addToConfigListForMultipleOccurences(missingConfigs, entry, productCode);
				}
				else
				{
					// 99% case, a product is only once in cart, hence creating SingeltonList
					entryList = Collections.singletonList(entry);
				}
				missingConfigs.put(productCode, entryList);
			}
		}

		if (LOG.isDebugEnabled())
		{
			final int numberMissingConfigs = countMissingConfigs(missingConfigs);
			LOG.debug(numberMissingConfigs + " externalConfigs missing in cart " + toCart.getGuid());
		}
		return missingConfigs;

	}

	protected <T> List<T> addToConfigListForMultipleOccurences(final Map<String, List<T>> missingConfigs, final T entry,
			final String productCode)
	{
		List<T> entryList;
		// 1% case - wrap the singletonList into a fully arraylist, if required
		entryList = missingConfigs.get(productCode);
		if (entryList.size() == 1)
		{
			final List<T> newConfigList = new ArrayList(entryList.size() + 1);
			newConfigList.addAll(entryList);
			entryList = newConfigList;
		}
		entryList.add(entry);
		return entryList;
	}



	protected int countCollectedConfigs(final Map<String, List<String>> collectedConfigs)
	{
		int counter = 0;
		for (final List<String> list : collectedConfigs.values())
		{
			counter += list.size();
		}
		return counter;
	}



	protected int countMissingConfigs(final Map<String, List<AbstractOrderEntryModel>> missingConfigs)
	{
		int counter = 0;
		for (final List<AbstractOrderEntryModel> list : missingConfigs.values())
		{
			counter += list.size();
		}
		return counter;
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
		final Object value = getModelService().getAttributeValue(productModel, attribute);
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
