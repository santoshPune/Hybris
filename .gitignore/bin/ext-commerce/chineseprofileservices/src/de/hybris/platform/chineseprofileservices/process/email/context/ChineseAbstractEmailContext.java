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
package de.hybris.platform.chineseprofileservices.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Locale;


public abstract class ChineseAbstractEmailContext<T extends BusinessProcessModel> extends AbstractEmailContext<T>
{

	@Override
	public void init(final T businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		localize(businessProcessModel);
	}

	@Override
	protected LanguageModel getEmailLanguage(final T businessProcessModel)
	{
		final LanguageModel langModel = new LanguageModel();
		final String language = getCustomer(businessProcessModel).getEmailLanguage();
		if (language == null || language.isEmpty())
		{
			langModel.setIsocode(Locale.SIMPLIFIED_CHINESE.getLanguage());
		}
		else
		{
			langModel.setIsocode(language);
		}
		return langModel;
	}

	protected void localize(final T businessProcessModel)
	{
		final CustomerModel customerModel = getCustomer(businessProcessModel);
		if (customerModel != null)
		{
			final Locale locale = new Locale(getEmailLanguage(businessProcessModel).getIsocode());
			put(TITLE, (customerModel.getTitle() != null && customerModel.getTitle().getName() != null)
					? customerModel.getTitle().getName(locale) : "");
		}
	}
}
