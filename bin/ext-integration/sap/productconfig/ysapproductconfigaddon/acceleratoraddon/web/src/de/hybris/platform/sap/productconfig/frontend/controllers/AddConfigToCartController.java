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
package de.hybris.platform.sap.productconfig.frontend.controllers;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigfrontendWebConstants;

import javax.annotation.MatchesPattern;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class AddConfigToCartController extends AbstractProductConfigController
{
	private static final Logger LOGGER = Logger.getLogger(AddConfigToCartController.class.getName());

	@RequestMapping(value = "/**/{productCode:.*}/addToCart", method = RequestMethod.POST)
	public String addConfigToCart(@PathVariable("productCode")
	final String productCode, @ModelAttribute(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE)
	@Valid
	@MatchesPattern(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE)
	final ConfigurationData configData, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("AddConfigToCart POST for '" + productCode + "'");
		}

		final String redirectURL = "redirect:/" + productCode + SapproductconfigfrontendWebConstants.CONFIG_OVERVIEW_URL;

		removeNullCstics(configData.getGroups());
		getConfigFacade().updateConfiguration(configData);

		final boolean validationErrors = bindingResult.hasErrors();
		if (validationErrors)
		{
			return redirectURL;
		}


		final ConfigurationData latestConfiguration = getConfigFacade().getConfiguration(configData);
		logModelmetaData(latestConfiguration);
		Boolean addedToCart = Boolean
				.valueOf(latestConfiguration.getCartItemPK() == null || latestConfiguration.getCartItemPK().isEmpty());

		//check whether we have this configuration in cart already (multiple tab e.g.)
		if (addedToCart.booleanValue())
		{
			//this means the system assumes addToCart didn't take place->check for parallel addToCarts!
			final String existingCartEntry = getSessionAccessFacade().getCartEntryForConfigId(latestConfiguration.getConfigId());
			if (existingCartEntry != null)
			{
				latestConfiguration.setCartItemPK(existingCartEntry);
				addedToCart = Boolean.FALSE;
			}
		}

		final String cartItemKey;
		try
		{
			cartItemKey = getConfigCartFacade().addConfigurationToCart(latestConfiguration);
			getSessionAccessFacade().setCartEntryForProduct(productCode, cartItemKey);
			final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
			getSessionAccessFacade().setUiStatusForCartEntry(cartItemKey, uiStatus);

			redirectAttributes.addFlashAttribute("addedToCart", addedToCart);
		}
		catch (final CommerceCartModificationException ex)
		{
			// In our case log level error is fine, as we don't foresee exceptions in our
			// standard process (in case no stock available, one would not be allowed to configure
			// at all).
			// Extensions and inproper setup can cause these extensions, in this case error handling
			// needs to be reconsidered here
			GlobalMessages.addErrorMessage(model, "sapproductconfig.addtocart.product.error");
			LOGGER.error("Add-To-Cart failed", ex);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Redirect to: '" + redirectURL + "'");
		}

		return redirectURL;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/**/{productCode:.*}/reset")
	public String resetConfiguration(@PathVariable("productCode")
	final String productCode, final String url) throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Reset POST for '" + productCode + "'");
		}

		getSessionAccessFacade().removeUiStatusForProduct(productCode);
		getSessionAccessFacade().removeCartEntryForProduct(productCode);

		//We keep the SSC session belonging to the configuration for later
		//configure from cart


		final String redirectUrl = "redirect:" + url;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Redirect to: '" + redirectUrl + "'");
		}

		return redirectUrl;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/**/{productCode:.*}/copy")
	public String copyConfiguration(@PathVariable("productCode")
	final String productCode, final String url) throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Copy POST for " + productCode);
		}

		getSessionAccessFacade().removeCartEntryForProduct(productCode);

		final UiStatus uiStatus = getUiStatus(productCode);
		final String oldConfigId = uiStatus.getConfigId();
		final String newConfigId = getConfigCartFacade().copyConfiguration(oldConfigId);
		checkUiStatus(productCode, uiStatus, oldConfigId, newConfigId);

		final String redirectUrl = "redirect:" + url;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Redirect to: '" + redirectUrl + "'");
		}

		return redirectUrl;
	}

	protected UiStatus getUiStatus(final String productCode)
	{
		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		if (uiStatus == null)
		{
			throw new IllegalStateException("Could not get uiStatus for: " + productCode);
		}
		return uiStatus;
	}

	/**
	 * Updates the UI Status if needed (in case a configuration has been copied)
	 *
	 * @param productCode
	 * @param uiStatus
	 *           existing UI status
	 * @param oldConfigId
	 *           ID of existing CFG
	 * @param newConfigId
	 *           ID of new CFG (might be the same as the old one)
	 */
	protected void checkUiStatus(final String productCode, final UiStatus uiStatus, final String oldConfigId,
			final String newConfigId)
	{
		if (!newConfigId.equals(oldConfigId))
		{
			final UiStatus newUiStatus = new UiStatus();
			newUiStatus.setConfigId(newConfigId);
			newUiStatus.setGroups(uiStatus.getGroups());
			newUiStatus.setPriceSummaryCollapsed(uiStatus.isPriceSummaryCollapsed());
			newUiStatus.setSpecificationTreeCollapsed(uiStatus.isSpecificationTreeCollapsed());
			newUiStatus.setHideImageGallery(uiStatus.isHideImageGallery());
			getSessionAccessFacade().setUiStatusForProduct(productCode, newUiStatus);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Old and new configId: " + oldConfigId + ", " + newConfigId);
		}
	}
}
