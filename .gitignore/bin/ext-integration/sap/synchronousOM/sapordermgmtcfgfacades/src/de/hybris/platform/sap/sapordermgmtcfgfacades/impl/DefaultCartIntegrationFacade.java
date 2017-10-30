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
package de.hybris.platform.sap.sapordermgmtcfgfacades.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.sapordermgmtb2bfacades.cart.CartRestorationFacade;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;



public class DefaultCartIntegrationFacade implements ConfigurationCartIntegrationFacade
{
	ProductConfigurationService productConfigurationService;
	CartService cartService;
	ProductService productService;
	BackendAvailabilityService backendAvailabilityService;
	CartRestorationFacade cartRestorationFacade;
	private SessionAccessService sessionAccessService;
	private BaseStoreService baseStoreService;

	private ConfigurationCartIntegrationFacade productConfigDefaultCartIntegrationFacade;

	@Resource(name = "sapProductConfigDefaultCartIntegrationFacade")
	protected ConfigurationCartIntegrationFacade sapProductConfigCartIntegrationFacade;

	/**
	 * @return the sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	@Required
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}


	@SuppressWarnings("deprecation")
	@Override
	public String addConfigurationToCart(final ConfigurationData configuration) throws CommerceCartModificationException
	{
		//isSapOrderMgmtEnabled
		if (!isSapOrderMgmtEnabled())
		{
			return sapProductConfigCartIntegrationFacade.addConfigurationToCart(configuration);
		}


		if (backendAvailabilityService.isBackendDown())
		{
			final String itemKey = getProductConfigDefaultCartIntegrationFacade().addConfigurationToCart(configuration);
			getSessionAccessService().setConfigIdForCartEntry(itemKey, configuration.getConfigId());

			return itemKey;
		}
		else
		{
			final ConfigModel configModel = productConfigurationService.retrieveConfigurationModel(configuration.getConfigId());
			String itemKey = configuration.getCartItemPK();

			final boolean isItemAvailable = isItemInCartByKey(itemKey);

			if (isItemAvailable)
			{
				cartService.updateConfigurationInCart(itemKey, configModel);
			}
			else
			{
				itemKey = cartService.addConfigurationToCart(configModel);
			}

			//this needs to be done before the call of cart restoration facade as the cart restoration relies on the
			//availability of the configuration reference in the session
			getSessionAccessService().setConfigIdForCartEntry(itemKey, configuration.getConfigId());

			//Persist the configuration for later cart restoration
			cartRestorationFacade.setSavedCart(cartService.getSessionCart());

			return itemKey;
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade#isItemInCartByKey(java.lang.String
	 * )
	 */
	@Override
	public boolean isItemInCartByKey(final String key)
	{
		if (!isSapOrderMgmtEnabled())
		{
			return sapProductConfigCartIntegrationFacade.isItemInCartByKey(key);
		}
		if (backendAvailabilityService.isBackendDown())
		{
			return productConfigDefaultCartIntegrationFacade.isItemInCartByKey(key);
		}
		else
		{
			return cartService.isItemAvailable(key);
		}
	}

	/**
	 * @return the productConfigurationService
	 */
	public ProductConfigurationService getProductConfigurationService()
	{
		return productConfigurationService;
	}

	/**
	 * @param productConfigurationService
	 *           the productConfigurationService to set
	 */
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	@Override
	public String copyConfiguration(final String configId)
	{
		if (!isSapOrderMgmtEnabled())
		{
			return sapProductConfigCartIntegrationFacade.copyConfiguration(configId);
		}

		//even if backend is down, we do a default copy of the configuration, as we want the CFG session
		//to stay in the hybris session for later reconfiguration
		final String externalConfiguration = productConfigurationService.retrieveExternalConfiguration(configId);
		final ConfigModel configModel = productConfigurationService.retrieveConfigurationModel(configId);
		final KBKey kbKey = getKBKey(configModel.getRootInstance().getName());
		final ConfigModel newConfiguration = productConfigurationService.createConfigurationFromExternal(kbKey,
				externalConfiguration);
		return newConfiguration.getId();
	}

	/**
	 * Creates a KB key for a given product ID, accessing the product model, and returns it.
	 *
	 * @param productId
	 * @return KBKey, containing KB data for the given product
	 */
	protected KBKey getKBKey(final String productId)
	{

		final KBKey kbKey = new KBKeyImpl(productId);

		return kbKey;

	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Override
	public void resetConfiguration(final String configId)
	{
		if (!isSapOrderMgmtEnabled())
		{
			sapProductConfigCartIntegrationFacade.resetConfiguration(configId);
		}

		//nothing needed for us as configuration must stay in session
		//even if backend is down, we want the CFG session to stay in the hybris session to offer a later
		//recovery
	}


	/**
	 * @return the productConfigDefaultCartIntegrationFacade
	 */
	public ConfigurationCartIntegrationFacade getProductConfigDefaultCartIntegrationFacade()
	{
		return productConfigDefaultCartIntegrationFacade;
	}

	/**
	 * @param productConfigDefaultCartIntegrationFacade
	 *           the productConfigDefaultCartIntegrationFacade to set
	 */
	public void setProductConfigDefaultCartIntegrationFacade(
			final ConfigurationCartIntegrationFacade productConfigDefaultCartIntegrationFacade)
	{
		this.productConfigDefaultCartIntegrationFacade = productConfigDefaultCartIntegrationFacade;
	}

	/**
	 * @return the backendAvailabilityService
	 */
	protected BackendAvailabilityService getBackendAvailabilityService()
	{
		return backendAvailabilityService;
	}

	/**
	 * @param backendAvailabilityService
	 *           the backendAvailabilityService to set
	 */
	public void setBackendAvailabilityService(final BackendAvailabilityService backendAvailabilityService)
	{
		this.backendAvailabilityService = backendAvailabilityService;
	}


	/**
	 * @return the cartRestorationFacade
	 */
	public CartRestorationFacade getCartRestorationFacade()
	{
		return cartRestorationFacade;
	}


	/**
	 * @param cartRestorationFacade
	 *           the cartRestorationFacade to set
	 */
	public void setCartRestorationFacade(final CartRestorationFacade cartRestorationFacade)
	{
		this.cartRestorationFacade = cartRestorationFacade;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade#restoreConfiguration(de.hybris
	 * .platform.sap.productconfig.facades.KBKeyData, java.lang.String)
	 */
	@Override
	public ConfigurationData restoreConfiguration(final KBKeyData kbKey, final String cartEntryKey)
	{
		if (!isSapOrderMgmtEnabled())
		{
			return sapProductConfigCartIntegrationFacade.restoreConfiguration(kbKey, cartEntryKey);
		}
		return null;
	}

	/**
	 * Check if synchronous order management SOM is active
	 *
	 * @return true is SOM is active
	 */
	protected boolean isSapOrderMgmtEnabled()
	{
		return getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null
				&& getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled();

	}

}
