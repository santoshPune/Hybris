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
*/
package de.hybris.platform.chinesecommerceorgaddressaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.chinesecommerceorgaddressaddon.constants.WebConstants;
import de.hybris.platform.chinesecommerceorgaddressaddon.controllers.ChinesecommerceorgaddressaddonControllerConstants;
import de.hybris.platform.chinesecommerceorgaddressaddon.forms.ChineseUnitAddressForm;
import de.hybris.platform.chinesecommerceorgaddressaddon.handlers.ChineseUnitAddressHandler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceorgaddon.controllers.pages.BusinessUnitManagementPageController;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Process the request for adding and editing chinese address for units
 */
@Controller
@Scope("tenant")
@RequestMapping("/my-company/organization-management/manage-units")
public class ChineseBusinessUnitManagementPageController extends BusinessUnitManagementPageController
{
	private static final Logger LOG = Logger.getLogger(ChineseBusinessUnitManagementPageController.class);

	private static final String COUNTRY_ATTR = "country";
	private static final String REGIONS_ATTR = "regions";
	private static final String CITIES_ATTR = "cities";
	private static final String DISTRICTS_ATTR = "districts";
	private static final String ADDRESS_FORM_ATTR = "addressForm";

	private static final String COUNTRY_DATA_ATTR = "countryData";
	private static final String TITLE_DATA_ATTR = "titleData";
	private static final String ADDRESS_DATA_ATTR = "addressData";
	private static final String UNIT_NAME_ATTR = "unitName";

	@Resource(name = "chineseAddressFacade")
	protected AddressFacade chineseAddressFacade;

	@Resource(name = "chineseUnitAddressHandler")
	protected ChineseUnitAddressHandler chineseUnitAddressHandler;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	/**
	 * @return the i18NFacade
	 */
	public I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	@RequestMapping(value = "/addressform", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCountryAddressForm(@RequestParam(value = "addressCode", required = false) final String addressCode,
			@RequestParam("countryIsoCode") final String countryIsoCode, final Model model)
	{
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(COUNTRY_ATTR, countryIsoCode);
		model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(countryIsoCode));
		model.addAttribute(TITLE_DATA_ATTR, getUserFacade().getTitles());

		final ChineseUnitAddressForm chineseAddressForm = new ChineseUnitAddressForm();
		chineseAddressForm.setCountryIso(countryIsoCode);
		model.addAttribute(ADDRESS_FORM_ATTR, chineseAddressForm);

		return ChinesecommerceorgaddressaddonControllerConstants.Views.Fragments.Account.CountryAddressForm;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.POST, params = "countryIso=" + WebConstants.CHINA_ISOCODE)
	@RequireHardLogIn
	public String addAddress(@RequestParam("unit") final String unit,
			@ModelAttribute("addressForm") final ChineseUnitAddressForm addressForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		chineseUnitAddressHandler.validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			return prepareModelAndViewAfterError(model, addressForm, unit, null);
		}
		final AddressData newAddress = chineseUnitAddressHandler.prepareChineseUnitAddressData(addressForm);
		try
		{
			b2bUnitFacade.addAddressToUnit(newAddress, unit);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.added");
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "form.global.error");
		}
		return String.format(REDIRECT_TO_UNIT_DETAILS, urlEncode(unit));
	}

	@Override
	@RequestMapping(value = "/edit-address", method =
	{ RequestMethod.GET })
	@RequireHardLogIn
	public String editAddress(@RequestParam("unit") final String unit, @RequestParam("addressId") final String addressId,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		super.editAddress(unit, addressId, model, redirectModel);
		final AddressForm addressForm = (AddressForm) model.asMap().get(ADDRESS_FORM_ATTR);
		final AddressData addressData = (AddressData) model.asMap().get(ADDRESS_DATA_ATTR);
		if (addressData != null)
		{
			addressForm.setPhone(addressData.getPhone());
		}
		final ChineseUnitAddressForm chineseAddressForm = new ChineseUnitAddressForm();
		BeanUtils.copyProperties(addressForm, chineseAddressForm);

		model.addAttribute(COUNTRY_ATTR, chineseAddressForm.getCountryIso());
		model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(chineseAddressForm.getCountryIso()));
		model.addAttribute(ADDRESS_FORM_ATTR, chineseAddressForm);

		chineseUnitAddressHandler.prepareAddressForm(model, chineseAddressForm);
		return ChinesecommerceorgaddressaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUnitAddAddressPage;
	}

	@RequestMapping(value = "/edit-address", method = RequestMethod.POST, params = "countryIso=" + WebConstants.CHINA_ISOCODE)
	@RequireHardLogIn
	public String editAddress(@RequestParam("unit") final String unit, @RequestParam("addressId") final String addressId,
			@ModelAttribute("addressForm") final ChineseUnitAddressForm addressForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		chineseUnitAddressHandler.validate(addressForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			return prepareModelAndViewAfterError(model, addressForm, unit, addressId);
		}
		final AddressData newAddress = chineseUnitAddressHandler.prepareChineseUnitAddressData(addressForm);
		try
		{
			b2bUnitFacade.editAddressOfUnit(newAddress, unit);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
					"account.confirmation.address.updated");
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "form.global.error");
		}
		return String.format(REDIRECT_TO_UNIT_DETAILS, urlEncode(unit));
	}

	@Override
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@RequireHardLogIn
	public String unitDetails(@RequestParam("unit") final String unit, final Model model) throws CMSItemNotFoundException
	{
		super.unitDetails(unit, model);
		return ChinesecommerceorgaddressaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUnitDetailsPage;
	}

	protected String prepareModelAndViewAfterError(final Model model, final ChineseUnitAddressForm addressForm, final String unit,
			final String addressId) throws CMSItemNotFoundException
	{
		model.addAttribute(COUNTRY_ATTR, addressForm.getCountryIso());
		model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA_ATTR, getUserFacade().getTitles());
		model.addAttribute(ADDRESS_FORM_ATTR, addressForm);

		if (addressForm.getRegionIso() != null)
		{
			model.addAttribute(CITIES_ATTR, chineseAddressFacade.getCitiesForRegion(addressForm.getRegionIso()));
		}
		if (addressForm.getCityIso() != null)
		{
			model.addAttribute(DISTRICTS_ATTR, chineseAddressFacade.getDistrictsForCity(addressForm.getCityIso()));
		}

		final B2BUnitData unitData = b2bUnitFacade.getUnitForUid(unit);
		final List<AddressData> unitAddresses = unitData.getAddresses();
		if (unitData != null && unitAddresses != null)
		{
			for (final AddressData addressData : unitAddresses)
			{
				if (addressId != null && addressData.getId() != null && addressData.getId().equals(addressId))
				{
					model.addAttribute("addressData", addressData);
				}
			}
			model.addAttribute(UNIT_NAME_ATTR, unitData.getName());
		}

		final List<Breadcrumb> breadcrumbs = myCompanyBreadcrumbBuilder.createManageUnitsDetailsBreadcrumbs(unit);
		breadcrumbs.add(new Breadcrumb(
				String.format("/my-company/organization-management/manage-units/add-address/?unit=%s", urlEncode(unit)),
				getMessageSource().getMessage("text.company.manage.units.addAddress", new Object[]
		{ unit }, "Add Address to {0} Business Unit ", getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		GlobalMessages.addErrorMessage(model, "form.global.error");
		storeCmsPageInModel(model, getContentPageForLabelOrId(MANAGE_UNITS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MANAGE_UNITS_CMS_PAGE));
		model.addAttribute("uid", unit);
		return ChinesecommerceorgaddressaddonControllerConstants.Views.Pages.MyCompany.MyCompanyManageUnitAddAddressPage;
	}
}
