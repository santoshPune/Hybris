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
package de.hybris.platform.sap.productconfig.facades.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.data.CartEntryConfigurationAttributes;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Takes care of populating product configuration relevant attributes
 */
public class CartConfigurationPopulator implements Populator<CartModel, CartData>
{

	private static final Logger LOG = Logger.getLogger(CartConfigurationPopulator.class);
	private SessionAccessService sessionAccessService;
	private ProductConfigurationService productConfigurationService;
	private String configurableSource;
	private ModelService modelService;

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

	public ProductConfigurationService getProductConfigurationService()
	{
		return productConfigurationService;
	}

	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}

	/**
	 * @return sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return this.sessionAccessService;
	}

	public void setConfigurableSource(final String configurableSource)
	{
		this.configurableSource = configurableSource;
	}

	@Override
	public void populate(final CartModel source, final CartData target) throws ConversionException
	{
		long startTime = 0;
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
		}

		for (final AbstractOrderEntryModel entry : source.getEntries())
		{
			populateCartEntry(target, entry);
		}

		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("CPQ Populating for cart took " + duration + " ms");
		}
	}

	/**
	 * Transfers configuration related attributes from order entry into its DTO representation
	 *
	 * @param target
	 *           Cart DTO, used to get the cart entry DTO via searching for key
	 * @param entry
	 *           Cart entry model
	 */
	protected void populateCartEntry(final CartData target, final AbstractOrderEntryModel entry)
	{
		final Boolean isConfigurable = (Boolean) getProductAttribute(entry.getProduct(), configurableSource);
		if (isConfigurable != null && isConfigurable.booleanValue())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("CartItem with PK " + entry.getPk() + " is Configurable ==> populating DTO.");
			}


			final CartEntryConfigurationAttributes configurationAttributes = productConfigurationService
					.calculateCartEntryConfigurationAttributes(entry);

			checkForExternalConfiguration(entry);

			writeToTargetEntry(target, entry, configurationAttributes);

		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("CartItem with PK " + entry.getPk() + " is NOT Configurable ==> skipping population of DTO.");
			}
		}
	}

	/**
	 * Writes external configuration to cart entry if it is not present yet
	 *
	 * @param entry
	 *           Cart entry
	 */
	protected void checkForExternalConfiguration(final AbstractOrderEntryModel entry)
	{
		final String xml = entry.getExternalConfiguration();
		if (xml == null || xml.isEmpty())
		{
			final String configId = getSessionAccessService().getConfigIdForCartEntry(entry.getPk().toString());
			entry.setExternalConfiguration(productConfigurationService.retrieveExternalConfiguration(configId));
		}
	}

	/**
	 * Writes result to target entry DTO
	 *
	 * @param target
	 *           Cart DTO, used to get the cart entry DTO via searching for key
	 * @param entry
	 *           Cart entry model
	 * @param configurationAttributes
	 *           Configuration relevant attributes
	 */
	protected void writeToTargetEntry(final CartData target, final AbstractOrderEntryModel entry,
			final CartEntryConfigurationAttributes configurationAttributes)
	{
		final OrderEntryData targetEntry = findTargetEntry(target, entry.getEntryNumber());
		if (targetEntry == null)
		{
			throw new IllegalArgumentException("Target items do not match source items");
		}
		targetEntry.setItemPK(entry.getPk().toString());
		targetEntry.setConfigurationAttached(true);
		targetEntry.setConfigurationConsistent(configurationAttributes.getConfigurationConsistent().booleanValue());
		targetEntry.setConfigurationErrorCount(configurationAttributes.getNumberOfErrors().intValue());
	}


	/**
	 * Finds an entry part of the cart
	 *
	 * @param target
	 *           Cart DTO representation
	 * @param entryNumber
	 *           Number of item we search for
	 * @return Target order entry DTO
	 */
	protected OrderEntryData findTargetEntry(final CartData target, final Integer entryNumber)
	{
		for (final OrderEntryData targetEntry : target.getEntries())
		{
			if (targetEntry.getEntryNumber().equals(entryNumber))
			{
				return targetEntry;
			}
		}
		return null;
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
