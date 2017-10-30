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
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link ConfigurationCartIntegrationFacade}
 */
public class ConfigurationCartIntegrationFacadeImpl extends ConfigurationBaseFacadeImpl
		implements ConfigurationCartIntegrationFacade
{

	private ProductConfigurationService configurationService;
	private CartService cartService;
	private ModelService modelService;
	private ProductService productService;
	private CommerceCartService commerceCartService;
	private String externalConfigurationDestination;

	private static final Logger LOG = Logger.getLogger(ConfigurationCartIntegrationFacadeImpl.class);

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ProductConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	@Override
	public String addConfigurationToCart(final ConfigurationData configContent) throws CommerceCartModificationException
	{

		final ProductModel product = productService.getProductForCode(configContent.getKbKey().getProductCode());

		final AbstractOrderEntryModel cartItem = getOrCreateCartItem(product, configContent);
		final PriceData currentTotal = configContent.getPricing().getCurrentTotal();
		if (currentTotal != ConfigPricing.NO_PRICE)
		{
			cartItem.setBasePrice(Double.valueOf(currentTotal.getValue().doubleValue()));
			modelService.save(cartItem);
			final CommerceCartParameter parameter = new CommerceCartParameter();
			fillCommerceCartParameterForCalculateCart(parameter, cartService.getSessionCart());
			commerceCartService.calculateCart(parameter);
		}

		final String configId = configContent.getConfigId();
		final String xml = configurationService.retrieveExternalConfiguration(configId);
		cartItem.setExternalConfiguration(xml);
		getSessionAccessService().setConfigIdForCartEntry(cartItem.getPk().toString(), configId);
		modelService.save(cartItem);

		final String key = cartItem.getPk().toString();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Added product '" + product.getCode() + "' with configId '" + configContent.getConfigId()
					+ "' to cart, referenced by cart entry PK '" + key + "'");
			LOG.debug("Configuration saved to database: " + xml);
		}
		return key;
	}

	/**
	 * @param parameter
	 * @param sessionCart
	 */
	protected void fillCommerceCartParameterForCalculateCart(final CommerceCartParameter parameter, final CartModel sessionCart)
	{
		parameter.setEnableHooks(true);
		parameter.setCart(sessionCart);

	}

	/**
	 * Creates a new entry in the session cart or returns the entry belonging to the current configuration. The link
	 * between cart entry and configuration is established via {@link ConfigurationData#getCartItemPK()}
	 *
	 * @param product
	 * @param configData
	 *           DTO representation of configuration runtime instance
	 * @return Corresponding cart entry model
	 * @throws CommerceCartModificationException
	 */
	protected AbstractOrderEntryModel getOrCreateCartItem(final ProductModel product, final ConfigurationData configData)
			throws CommerceCartModificationException
	{
		final String pkString = configData.getCartItemPK();
		final PK cartItemPk = convertStringToPK(pkString);
		AbstractOrderEntryModel cartItem = findItemInCartByPK(cartItemPk);
		if (cartItem == null)
		{
			final CartModel cart = cartService.getSessionCart();

			final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
			fillCommerceCartParameterForAddToCart(commerceCartParameter, cart, product, 1L, product.getUnit(), true);

			final CommerceCartModification commerceItem = commerceCartService.addToCart(commerceCartParameter);
			cartItem = commerceItem.getEntry();
		}
		return cartItem;
	}

	/* fills CommerceCartParameter Object for addToCart */
	protected void fillCommerceCartParameterForAddToCart(final CommerceCartParameter parameter, final CartModel cart,
			final ProductModel product, final long l, final UnitModel unit, final boolean forceNewEntry)
	{
		parameter.setEnableHooks(true);
		parameter.setCart(cart);
		parameter.setProduct(product);
		parameter.setQuantity(l);
		parameter.setUnit(unit);
		parameter.setCreateNewEntry(forceNewEntry);
	}

	/**
	 * Converts a string to the primary key wrapping it
	 *
	 * @param pkString
	 * @return Primary key
	 */
	protected PK convertStringToPK(final String pkString)
	{
		final PK cartItemPk;
		if (pkString != null && !pkString.isEmpty())
		{
			cartItemPk = PK.parse(pkString);
		}
		else
		{
			cartItemPk = PK.NULL_PK;
		}
		return cartItemPk;
	}

	/**
	 * Searches the session cart for an entry specified by a primary key. In case nothing is found, null is returned.
	 *
	 * @param cartItemPk
	 *           Entry key
	 * @return Corresponding order entry model
	 */
	public AbstractOrderEntryModel findItemInCartByPK(final PK cartItemPk)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Search for cartItem with PK '" + cartItemPk + "'");
		}

		if (cartItemPk == null || PK.NULL_PK.equals(cartItemPk))
		{
			return null;
		}

		final Optional<AbstractOrderEntryModel> cartEntry = cartService.getSessionCart().getEntries().parallelStream()
				.filter(entry -> entry.getPk().equals(cartItemPk) && !modelService.isRemoved(entry)).findFirst();
		if (cartEntry.isPresent())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("cartItem found for PK '" + cartItemPk + "'");
			}

			return cartEntry.get();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.facades.
	 * ConfigurationCartIntegrationFacade#isItemInCartByKey(java.lang.String )
	 */
	@Override
	public boolean isItemInCartByKey(final String key)
	{
		final PK cartItemPK = PK.parse(key);
		final AbstractOrderEntryModel item = findItemInCartByPK(cartItemPK);

		final boolean itemExistsInCart = item != null;

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Item with key '" + key + "' exists in cart: '" + itemExistsInCart + "'");
		}

		return itemExistsInCart;

	}

	@Override
	public String copyConfiguration(final String configId)
	{
		// We do a copy of the configuration, as we want the CFG session
		// to stay in the hybris session for later reconfiguration
		final String externalConfiguration = configurationService.retrieveExternalConfiguration(configId);
		final ConfigModel configModel = configurationService.retrieveConfigurationModel(configId);
		final KBKey kbKey = new KBKeyImpl(configModel.getRootInstance().getName());
		final ConfigModel newConfiguration = configurationService.createConfigurationFromExternal(kbKey, externalConfiguration);
		return newConfiguration.getId();
	}

	@Override
	public void resetConfiguration(final String configId)
	{
		configurationService.releaseSession(configId);
	}

	/**
	 * @return configurationService
	 */
	protected ProductConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @return cartService
	 */
	protected CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @return modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return productService
	 */
	protected ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @return commerceCartService
	 */
	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param externalConfigurationDestination
	 *           the externalConfigurationDestination to set
	 */
	public void setExternalConfigurationDestination(final String externalConfigurationDestination)
	{
		this.externalConfigurationDestination = externalConfigurationDestination;
	}

	/**
	 * @return externalConfigurationDestination
	 */
	protected String getExternalConfigurationDestination()
	{
		return externalConfigurationDestination;
	}

	@Override
	public ConfigurationData restoreConfiguration(final KBKeyData kbKey, final String cartEntryKey)
	{
		final PK cartItemPK = PK.parse(cartEntryKey);
		final AbstractOrderEntryModel item = findItemInCartByPK(cartItemPK);
		if (item == null)
		{
			LOG.warn("Probably multi-session issue: Item not found in cart for key: " + cartEntryKey);
			return null;
		}
		final String extConfig = item.getExternalConfiguration();
		ConfigModel configurationRuntimeModel;
		if (extConfig == null || extConfig.isEmpty())
		{
			configurationRuntimeModel = configurationService.createDefaultConfiguration(
					new KBKeyImpl(kbKey.getProductCode(), kbKey.getKbName(), kbKey.getKbLogsys(), kbKey.getKbVersion()));
		}
		else
		{
			configurationRuntimeModel = configurationService.createConfigurationFromExternal(
					new KBKeyImpl(kbKey.getProductCode(), kbKey.getKbName(), kbKey.getKbLogsys(), kbKey.getKbVersion()), extConfig);
		}

		return convert(kbKey, configurationRuntimeModel);
	}




}
