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
package de.hybris.platform.chineseprofile.controllers.pages.imported;

import de.hybris.platform.acceleratorfacades.ordergridform.OrderGridFormFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EmailValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.PasswordValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.verification.AddressVerificationResultHandler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for home page
 */
//@Scope("tenant")
//@RequestMapping("/my-account/update-email")
public class AccountPageController extends AbstractSearchPageController
{
	private static final String BREADCRUMBS_ATTR = "breadcrumbs";
	private static final String IS_DEFAULT_ADDRESS_ATTR = "isDefaultAddress";
	private static final String COUNTRY_DATA_ATTR = "countryData";
	private static final String ADDRESS_BOOK_EMPTY_ATTR = "addressBookEmpty";
	private static final String TITLE_DATA_ATTR = "titleData";
	private static final String FORM_GLOBAL_ERROR = "form.global.error";
	private static final String PROFILE_CURRENT_PASSWORD_INVALID = "profile.currentPassword.invalid";
	private static final String TEXT_ACCOUNT_PROFILE = "text.account.profile";
	private static final String COUNTRY_ATTR = "country";
	private static final String REGIONS_ATTR = "regions";
	// Internal Redirects
	private static final String REDIRECT_TO_UPDATE_EMAIL_PAGE = REDIRECT_PREFIX + "/my-account/update-email";


	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */


	// CMS Pages
	private static final String UPDATE_EMAIL_CMS_PAGE = "update-email";

	private static final Logger LOG = Logger.getLogger(AccountPageController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "passwordValidator")
	private PasswordValidator passwordValidator;

	@Resource(name = "addressValidator")
	private AddressValidator addressValidator;

	@Resource(name = "profileValidator")
	private ProfileValidator profileValidator;

	@Resource(name = "emailValidator")
	private EmailValidator emailValidator;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;

	@Resource(name = "addressVerificationResultHandler")
	private AddressVerificationResultHandler addressVerificationResultHandler;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "orderGridFormFacade")
	private OrderGridFormFacade orderGridFormFacade;

	protected PasswordValidator getPasswordValidator()
	{
		return passwordValidator;
	}

	protected AddressValidator getAddressValidator()
	{
		return addressValidator;
	}

	protected ProfileValidator getProfileValidator()
	{
		return profileValidator;
	}

	protected EmailValidator getEmailValidator()
	{
		return emailValidator;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected AddressVerificationFacade getAddressVerificationFacade()
	{
		return addressVerificationFacade;
	}

	protected AddressVerificationResultHandler getAddressVerificationResultHandler()
	{
		return addressVerificationResultHandler;
	}

	@ModelAttribute("countries")
	public Collection<CountryData> getCountries()
	{
		return checkoutFacade.getDeliveryCountries();
	}

	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}

	@ModelAttribute("countryDataMap")
	public Map<String, CountryData> getCountryDataMap()
	{
		final Map<String, CountryData> countryDataMap = new HashMap<>();
		for (final CountryData countryData : getCountries())
		{
			countryDataMap.put(countryData.getIsocode(), countryData);
		}
		return countryDataMap;
	}

	protected TitleData findTitleForCode(final List<TitleData> titles, final String code)
	{
		if (code != null && !code.isEmpty() && titles != null && !titles.isEmpty())
		{
			for (final TitleData title : titles)
			{
				if (code.equals(title.getCode()))
				{
					return title;
				}
			}
		}
		return null;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateEmail(final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getEmailValidator().validate(updateEmailForm, bindingResult);
		String returnAction = REDIRECT_TO_UPDATE_EMAIL_PAGE;

		if (!bindingResult.hasErrors() && !updateEmailForm.getEmail().equals(updateEmailForm.getChkEmail()))
		{
			bindingResult.rejectValue("chkEmail", "validation.checkEmail.equals", new Object[] {}, "validation.checkEmail.equals");
		}

		if (bindingResult.hasErrors())
		{
			returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
		}
		else
		{
			try
			{
				customerFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						"text.account.profile.confirmationUpdated", null);

				// Replace the spring security authentication with the new UID
				final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "profile.email.unique");
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
			}
			catch (final PasswordMismatchException passwordMismatchException)
			{
				bindingResult.rejectValue("password", PROFILE_CURRENT_PASSWORD_INVALID);
				returnAction = setErrorMessagesAndCMSPage(model, UPDATE_EMAIL_CMS_PAGE);
			}
		}

		return returnAction;
	}

	protected String setErrorMessagesAndCMSPage(final Model model, final String cmsPageLabelOrId) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(cmsPageLabelOrId));
		model.addAttribute(BREADCRUMBS_ATTR, accountBreadcrumbBuilder.getBreadcrumbs(TEXT_ACCOUNT_PROFILE));
		return getViewForPage(model);
	}



	protected AddressForm getPreparedAddressForm()
	{
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final AddressForm addressForm = new AddressForm();
		addressForm.setFirstName(currentCustomerData.getFirstName());
		addressForm.setLastName(currentCustomerData.getLastName());
		addressForm.setTitleCode(currentCustomerData.getTitleCode());
		return addressForm;
	}


	protected void setUpAddressFormAfterError(final AddressForm addressForm, final Model model)
	{
		model.addAttribute(COUNTRY_DATA_ATTR, checkoutFacade.getDeliveryCountries());
		model.addAttribute(TITLE_DATA_ATTR, userFacade.getTitles());
		model.addAttribute(ADDRESS_BOOK_EMPTY_ATTR, Boolean.valueOf(userFacade.isAddressBookEmpty()));
		model.addAttribute(IS_DEFAULT_ADDRESS_ATTR, Boolean.valueOf(isDefaultAddress(addressForm.getAddressId())));
		if (addressForm.getCountryIso() != null)
		{
			model.addAttribute(REGIONS_ATTR, getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute(COUNTRY_ATTR, addressForm.getCountryIso());
		}
	}



	/**
	 * Method checks if address is set as default
	 *
	 * @param addressId
	 *           - identifier for address to check
	 * @return true if address is default, false if address is not default
	 */
	protected boolean isDefaultAddress(final String addressId)
	{
		final AddressData defaultAddress = userFacade.getDefaultAddress();
		return defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressId);
	}

}
