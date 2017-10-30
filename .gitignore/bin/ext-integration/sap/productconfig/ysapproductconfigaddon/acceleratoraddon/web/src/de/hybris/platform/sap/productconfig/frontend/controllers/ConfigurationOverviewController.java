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
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.frontend.CPQOverviewActionType;
import de.hybris.platform.sap.productconfig.frontend.FilterData;
import de.hybris.platform.sap.productconfig.frontend.OverviewUiData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.breadcrumb.ProductConfigureBreadcrumbBuilder;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigfrontendWebConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 */
@Controller
@RequestMapping()
public class ConfigurationOverviewController extends AbstractProductConfigController
{
	private static final Logger LOGGER = Logger.getLogger(ConfigurationOverviewController.class.getName());

	@Resource(name = "sapProductConfigOverviewFacade")
	private ConfigurationOverviewFacade configurationOverviewFacade;

	@Resource(name = "sapProductConfigBreadcrumbBuilder")
	private ProductConfigureBreadcrumbBuilder productConfigurationBreadcrumbBuilder;

	@RequestMapping(value = "/**/{productCode:.*}"
			+ SapproductconfigfrontendWebConstants.CONFIG_OVERVIEW_URL, method = RequestMethod.GET)
	public String getConfiguationOverview(@PathVariable("productCode")
	final String productCode, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Config GET received for '" + productCode + "' - Current Session: '"
					+ getSessionService().getCurrentSession().getSessionId() + "'");
		}
		final String cartItemKey = getSessionAccessFacade().getCartEntryForProduct(productCode);
		if (StringUtils.isBlank(cartItemKey))
		{
			return "redirect:/" + productCode + SapproductconfigfrontendWebConstants.CONFIG_URL;
		}
		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		ConfigurationOverviewData configOverviewData = null;
		configOverviewData = populateConfigurationModel(uiStatus, configOverviewData);
		initializeFilterListsInUiStatus(configOverviewData, uiStatus);
		prepareUiModel(request, model, productCode, uiStatus, configOverviewData);

		return "addon:/" + SapproductconfigaddonConstants.EXTENSIONNAME + "/pages/configuration/configurationOverviewPage";
	}

	protected void initializeFilterListsInUiStatus(final ConfigurationOverviewData overview, final UiStatus uiStatus)
	{
		uiStatus.setCsticFilterList(generateCsticFilterDataList(null));
		uiStatus.setMaxGroupFilterList(initializeGroupFilterDataList(overview));
	}

	@RequestMapping(value = "/**/{productCode:.*}"
			+ SapproductconfigfrontendWebConstants.CONFIG_OVERVIEW_URL, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateConfiguationOverview(@PathVariable("productCode")
	final String productCode, final OverviewUiData overviewUIData, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Config POST received for '" + productCode + "' - Current Session: '"
					+ getSessionService().getCurrentSession().getSessionId() + "'");
		}
		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		handleCPQAction(productCode, overviewUIData, uiStatus);

		overviewUIData
				.setConfigurationOverviewData(populateConfigurationModel(uiStatus, overviewUIData.getConfigurationOverviewData()));
		prepareUiModel(request, model, productCode, uiStatus, overviewUIData.getConfigurationOverviewData());

		return new ModelAndView("addon:/" + SapproductconfigaddonConstants.EXTENSIONNAME
				+ "/pages/configuration/configurationOverviewPageForAJAXRequests");
	}

	protected void handleCPQAction(final String productCode, final OverviewUiData overviewUIData, final UiStatus uiStatus)
	{
		if (overviewUIData.getCpqAction() != null)
		{
			if (CPQOverviewActionType.TOGGLE_IMAGE_GALLERY.equals(overviewUIData.getCpqAction()))
			{
				uiStatus.setHideImageGallery(!uiStatus.isHideImageGallery());
			}
			if (CPQOverviewActionType.APPLY_FILTER.equals(overviewUIData.getCpqAction()))
			{
				updateCsticFilterList(overviewUIData, uiStatus);
				updateAppliedFilters(uiStatus, overviewUIData);

				updateGroupFilterList(overviewUIData, uiStatus);
				updateGroups(uiStatus, overviewUIData);
			}
		}
		getSessionAccessFacade().setUiStatusForProduct(productCode, uiStatus);
	}

	protected void updateGroups(final UiStatus uiStatus, final OverviewUiData overviewUIData)
	{
		final Set<String> filteredOutGroups = new HashSet<>();

		final List<FilterData> maxFilterDataList = uiStatus.getMaxGroupFilterList();

		for (final FilterData filterData : maxFilterDataList)
		{
			if (filterData.isSelected())
			{
				filteredOutGroups.add(filterData.getKey());
			}
		}
		if (overviewUIData.getConfigurationOverviewData() == null)
		{
			overviewUIData.setConfigurationOverviewData(new ConfigurationOverviewData());
		}
		overviewUIData.getConfigurationOverviewData().setAppliedGroupFilters(filteredOutGroups);
	}

	protected void updateGroupFilterList(final OverviewUiData overviewUIData, final UiStatus uiStatus)
	{
		final List<FilterData> uiFilterDataList = overviewUIData.getGroupFilterList();
		final List<FilterData> maxFilterDataList = uiStatus.getMaxGroupFilterList();

		if (uiFilterDataList != null)
		{
			final HashMap<String, FilterData> maxMap = new HashMap<>();
			for (final FilterData filterData : maxFilterDataList)
			{
				maxMap.put(filterData.getKey(), filterData);
			}
			for (final FilterData filterData : uiFilterDataList)
			{
				maxMap.get(filterData.getKey()).setSelected(filterData.isSelected());
			}
		}
	}

	protected ConfigurationOverviewData populateConfigurationModel(final UiStatus uiStatus,
			final ConfigurationOverviewData updatedOverview)
	{
		return configurationOverviewFacade.getOverviewForConfiguration(uiStatus.getConfigId(), updatedOverview);
	}

	protected void prepareUiModel(final HttpServletRequest request, final Model model, final String productCode,
			final UiStatus uiStatus, final ConfigurationOverviewData overview) throws CMSItemNotFoundException
	{
		final String configId = uiStatus.getConfigId();
		final ProductModel productModel = populateProductModel(productCode, model, request);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				productConfigurationBreadcrumbBuilder.getOverviewBreadcrumbs(productModel));

		final KBKeyData kbKey = createKBKeyForProduct(productModel);

		final ConfigurationData configData = reloadConfiguration(kbKey, configId, uiStatus);
		setCartItemPk(configData);

		model.addAttribute(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE, configData);
		logModelmetaData(configData);

		final BindingResult errors = getBindingResultForConfig(configData, uiStatus);
		model.addAttribute(BindingResult.MODEL_KEY_PREFIX + SapproductconfigaddonConstants.CONFIG_ATTRIBUTE, errors);

		final OverviewUiData overviewUIData = prepareOverviewUiData(uiStatus, overview, configId);

		final Integer errorCount = Integer.valueOf(getConfigFacade().getNumberOfErrors(configId));
		model.addAttribute("errorCount", errorCount);
		model.addAttribute("overviewUiData", overviewUIData);
		model.addAttribute("pageType", "productConfigOverviewPage");
	}

	protected OverviewUiData prepareOverviewUiData(final UiStatus uiStatus, final ConfigurationOverviewData overview,
			final String configId)
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		overviewUIData.setConfigId(configId);
		overviewUIData.setConfigurationOverviewData(overview);
		overviewUIData.setCsticFilterList(generateCsticFilterDataList(overview));
		overviewUIData.setGroupFilterList(computeUiGroupFilterList(uiStatus.getMaxGroupFilterList()));
		return overviewUIData;
	}

	protected List<FilterData> computeUiGroupFilterList(final List<FilterData> maxUiGroups)
	{
		return new ArrayList<>(maxUiGroups);
	}

	protected void updateCsticFilterList(final OverviewUiData overviewUIData, final UiStatus uiStatus)
	{
		if (overviewUIData != null)
		{
			final List<FilterData> csticFilterList = overviewUIData.getCsticFilterList();
			uiStatus.setCsticFilterList(csticFilterList);
		}
	}

	protected void updateAppliedFilters(final UiStatus uiStatus, final OverviewUiData overviewUIData)
	{
		if (overviewUIData == null)
		{
			return;
		}
		final List<FilterEnum> appliedFilters = new ArrayList<>();
		appliedFilters.add(FilterEnum.VISIBLE);

		final List<FilterData> filterDataList = uiStatus.getCsticFilterList();
		for (final FilterData filterdata : filterDataList)
		{
			if (filterdata.isSelected())
			{
				appliedFilters.add(FilterEnum.valueOf(filterdata.getKey()));
			}
		}
		if (overviewUIData.getConfigurationOverviewData() == null)
		{
			overviewUIData.setConfigurationOverviewData(new ConfigurationOverviewData());
		}
		overviewUIData.getConfigurationOverviewData().setAppliedCsticFilters(appliedFilters);
	}

	protected List<FilterData> initializeGroupFilterDataList(final ConfigurationOverviewData overview)
	{
		final List<CharacteristicGroup> groups = overview.getGroups();
		final List<FilterData> filterDataList = new ArrayList<>();
		for (final CharacteristicGroup group : groups)
		{
			final FilterData filterData = new FilterData();
			filterData.setKey(group.getId());
			filterData.setDescription(group.getGroupDescription());
			//CPQ design convention:
			//Although all groups are shown, none of the group filters are initially displayed as selected.
			filterData.setSelected(false);
			filterDataList.add(filterData);
		}
		return filterDataList;
	}

	protected List<FilterData> generateCsticFilterDataList(final ConfigurationOverviewData overview)
	{
		final List filterDataList = new ArrayList<>();
		if (overview != null)
		{
			final List<FilterEnum> allFilters = new ArrayList<>(Arrays.asList(FilterEnum.values()));

			for (final FilterEnum filter : allFilters)
			{
				if (FilterEnum.VISIBLE.equals(filter))
				{
					continue;
				}

				final FilterData filterData = new FilterData();
				filterData.setKey(filter.toString());
				final List appliedFilters = overview.getAppliedCsticFilters();
				if (appliedFilters != null)
				{
					filterData.setSelected(appliedFilters.contains(filter));
				}
				else
				{
					filterData.setSelected(false);
				}
				filterDataList.add(filterData);
			}
		}
		return filterDataList;
	}

	@Override
	protected AbstractPageModel getPageForProduct() throws CMSItemNotFoundException
	{
		return getCmsPageService().getPageForId("productConfigOverview");
	}
}
