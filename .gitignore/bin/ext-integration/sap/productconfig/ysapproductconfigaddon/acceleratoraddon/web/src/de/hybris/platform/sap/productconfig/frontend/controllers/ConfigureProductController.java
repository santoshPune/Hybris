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


import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.breadcrumb.ProductConfigureBreadcrumbBuilder;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import de.hybris.platform.sap.productconfig.frontend.util.impl.UiStatusSync;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping()
public class ConfigureProductController extends AbstractProductConfigController
{
	private static final Logger LOGGER = Logger.getLogger(ConfigureProductController.class);

	@Resource(name = "sapProductConfigBreadcrumbBuilder")
	private ProductConfigureBreadcrumbBuilder productConfigurationBreadcrumbBuilder;

	@RequestMapping(value = "/**/{productCode:.*}/config", method = RequestMethod.GET)
	public String configureProduct(@PathVariable("productCode")
	final String productCode, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Config GET received for '" + productCode + "' - Current Session: '"
					+ getSessionService().getCurrentSession().getSessionId() + "'");
		}

		final ProductModel productmodel = getProductService().getProductForCode(productCode);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productConfigurationBreadcrumbBuilder.getBreadcrumbs(productmodel));

		populateConfigurationModel(request, model, productCode);


		return "addon:/" + SapproductconfigaddonConstants.EXTENSIONNAME + "/pages/configuration/configurationPage";
	}

	protected void populateConfigurationModel(final HttpServletRequest request, final Model model, final String productCode)
			throws CMSItemNotFoundException
	{
		final ProductModel productModel = populateProductModel(productCode, model, request);
		if (model.containsAttribute(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE))
		{
			return;
		}

		final KBKeyData kbKey = createKBKeyForProduct(productModel);
		final ConfigurationData configData;

		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		if (uiStatus != null)
		{
			final String configId = uiStatus.getConfigId();
			configData = reloadConfiguration(kbKey, configId, uiStatus);
		}
		else
		{
			configData = loadNewConfiguration(kbKey, null);
		}

		final UiStatusSync uiStatusSync = new UiStatusSync();

		uiStatusSync.compileGroupForDisplay(configData, uiStatus);

		setCartItemPk(configData);

		model.addAttribute(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE, configData);

		final BindingResult errors = getBindingResultForConfig(configData, uiStatus);
		model.addAttribute(BindingResult.MODEL_KEY_PREFIX + SapproductconfigaddonConstants.CONFIG_ATTRIBUTE, errors);
		countNumberOfUiErrorsPerGroup(configData.getGroups());

		handleConflictSolverMessage(uiStatus, configData, model);

		logModelmetaData(configData);
	}
}
