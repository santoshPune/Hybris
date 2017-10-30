/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineseprofilefacades.customer.impl;

import de.hybris.platform.chineseprofilefacades.customer.CustomerFacade;
import de.hybris.platform.chineseprofileservices.customer.CustomerAccountService;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


public class ChineseCustomerFacade extends DefaultCustomerFacade implements CustomerFacade
{

	private CustomerAccountService customerAccountService;

	private ConfigurationService configurationService;

	@Override
	public void saveEmailLanguageForCurrentUser(final String languageISO)
	{
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		if (currentUser != null && !getUserService().isAnonymousUser(currentUser))
		{
			currentUser.setEmailLanguage(languageISO);
		}
	}

	@Override
	public String generateVerificationCode()
	{
		return customerAccountService.generateVerificationCode();
	}

	@Override
	public void sendVerificationCode(final VerificationData data)
	{
		customerAccountService.sendVerificationCode(data);
	}

	@Override
	public void saveVerificationCodeInSession(final VerificationData data, final String name)
	{
		data.setTime(new Date());
		getSessionService().setAttribute(name, data);
	}

	@Override
	public void removeVerificationCodeFromSession(final String name)
	{
		getSessionService().removeAttribute(name);
	}

	@Override
	public void saveMobileNumber(VerificationData data)
	{
		final CustomerModel customer = getCurrentSessionCustomer();
		customer.setMobileNumber(data.getMobileNumber());
		customerAccountService.updateMobileNumber(customer);
	}

	@Override
	public int getVerificationCodeTimeout(final String key)
	{
		return configurationService.getConfiguration().getInt(key);
	}

	@Override
	public boolean isMobileNumberUnique(final String mobileNumber)
	{
		return !customerAccountService.getCustomerForMobileNumber(mobileNumber).isPresent();
	}

	@Override
	public void updateProfile(CustomerData customerData) throws DuplicateUidException
	{
		super.updateProfile(customerData);
		final CustomerModel customer = getCurrentSessionCustomer();
		customer.setMobileNumber(customerData.getMobileNumber());
		customerAccountService.updateMobileNumber(customer);
	}

	@Override
	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
