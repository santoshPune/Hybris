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
package de.hybris.platform.sap.sapordermgmtb2bfacades.cart.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2bacceleratorfacades.exception.DomainException;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCartFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.sapordermgmtb2bfacades.cart.CartRestorationFacade;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;
import de.hybris.platform.sap.sapordermgmtservices.prodconf.ProductConfigurationService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SapOrdermgmtB2BCartFacade extends DefaultB2BCartFacade
{
	private static final Logger LOG = Logger.getLogger(SapOrdermgmtB2BCartFacade.class);
	private CartService sapCartService;
	private CartRestorationFacade cartRestorationFacade;
	private BackendAvailabilityService backendAvailabilityService;
	private ProductConfigurationService productConfigurationService;
	private ConfigurationProviderFactory configurationProviderFactory;
	private BaseStoreService baseStoreService;
	private CartFacade sapCartFacade;

	private static final String CART_MODIFICATION_ERROR = "basket.error.occurred";

	protected boolean isSyncOrdermgmtEnabled()
	{
		return (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null)
				&& (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled());
	}

	@Override
	public CartModificationData addOrderEntry(final OrderEntryData cartEntry)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addOrderEntry(cartEntry);

		}

		CartModificationData cartModification = null;
		try
		{
			cartModification = getSapCartFacade().addToCart(cartEntry.getProduct().getCode(), cartEntry.getQuantity().longValue());
		}
		catch (final CommerceCartModificationException e)
		{
			throw new DomainException(getLocalizedString(CART_MODIFICATION_ERROR), e);
		}

		return cartModification;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#updateOrderEntry(de.hybris.platform.commercefacades
	 * .order.data.OrderEntryData)
	 */
	@Override
	public CartModificationData updateOrderEntry(final OrderEntryData cartEntry) throws EntityValidationException
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateOrderEntry(cartEntry);

		}

		final long entryNumber = cartEntry.getEntryNumber().longValue();
		final long quantity = cartEntry.getQuantity().longValue();
		if (isBackendDown())
		{
			try
			{
				final String itemKey = getItemKey(entryNumber);
				checkForConfigurationRelease(quantity, itemKey);
				return getSapCartFacade().updateCartEntry(entryNumber, quantity);
			}
			catch (final CommerceCartModificationException e)
			{
				throw new EntityValidationException("UpdateOrderEntry failed", e);
			}
		}
		else
		{

			final CartModificationData cartModificationData = getSapCartService().updateCartEntry(entryNumber, quantity);
			if (getCartRestorationFacade() != null)
			{
				try
				{
					getCartRestorationFacade().setSavedCart(getSapCartFacade().getSessionCart());
				}
				catch (final CommerceCartModificationException e)
				{
					throw new EntityValidationException("UpdateOrderEntry failed", e);
				}
			}
			return cartModificationData;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#addOrderEntryList(java.util.List)
	 */
	@Override
	public List<CartModificationData> addOrderEntryList(final List<OrderEntryData> cartEntries)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.addOrderEntryList(cartEntries);

		}

		throw new ApplicationBaseRuntimeException("addOrderEntryList not supported");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade#updateOrderEntryList(java.util.List)
	 */
	@Override
	public List<CartModificationData> updateOrderEntryList(final List<OrderEntryData> cartEntries)
	{

		if (!isSyncOrdermgmtEnabled())
		{
			return super.updateOrderEntryList(cartEntries);

		}

		throw new ApplicationBaseRuntimeException("updateOrderEntryList not supported");
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

	/**
	 * @return Is Backend down?
	 */
	public boolean isBackendDown()
	{
		return backendAvailabilityService.isBackendDown();
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


	public void checkForConfigurationRelease(final long quantity, final String itemKey)
	{
		if (quantity == 0)
		{
			final String configId = getProductConfigurationService().getGetConfigId(itemKey);
			if (configId != null)
			{
				getConfigurationProvider().releaseSession(configId);
			}
		}
	}



	ConfigurationProvider getConfigurationProvider()
	{
		return getConfigurationProviderFactory().getProvider();
	}


	String getItemKey(final long entryNumber)
	{
		final List<OrderEntryData> entries = getSapCartFacade().getSessionCart().getEntries();
		for (final OrderEntryData entry : entries)
		{
			if (entry.getEntryNumber().longValue() == entryNumber)
			{
				return entry.getItemPK();
			}
		}
		return null;
	}


	/**
	 * @return the configurationProviderFactory
	 */
	public ConfigurationProviderFactory getConfigurationProviderFactory()
	{
		return configurationProviderFactory;
	}


	/**
	 * @param configurationProviderFactory
	 *           the configurationProviderFactory to set
	 */
	public void setConfigurationProviderFactory(final ConfigurationProviderFactory configurationProviderFactory)
	{
		this.configurationProviderFactory = configurationProviderFactory;
	}

	/**
	 * Updates the information in the cart based on the content of the cartData
	 *
	 * @param cartData
	 *           the cart to modify and it's modifications.
	 * @return the updated cart.
	 */
	@Override
	public CartData update(final CartData cartData)
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return super.update(cartData);

		}

		throw new ApplicationBaseRuntimeException("not supported");

	}


	/**
	 * This gets the current cart.
	 *
	 * @return the current cart.
	 */
	@Override
	public CartData getCurrentCart()
	{
		if (!isSyncOrdermgmtEnabled())
		{
			return getSapCartFacade().getSessionCart();

		}
		return getSapCartFacade().getSessionCart();

	}


	/**
	 * @return the sapCartService
	 */
	public CartService getSapCartService()
	{
		return sapCartService;
	}


	/**
	 * @param sapCartService
	 *           the sapCartService to set
	 */
	@Required
	public void setSapCartService(final CartService sapCartService)
	{
		this.sapCartService = sapCartService;
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

	/**
	 * @return the sapCartFacade
	 */
	public CartFacade getSapCartFacade()
	{
		return sapCartFacade;
	}

	/**
	 * @param sapCartFacade
	 *           the sapCartFacade to set
	 */
	@Required
	public void setSapCartFacade(final CartFacade sapCartFacade)
	{
		this.sapCartFacade = sapCartFacade;
	}




}