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
package de.hybris.platform.sap.productconfig.services.attributehandlers;

import de.hybris.platform.sap.productconfig.services.model.CMSCartConfigurationRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;


/**
 * Description of Restriction we use for enabling specific cart CMS components. Based in service extension because we
 * need to have dependent modules influence them also when our frontend is not deployed.
 */
public class CartRestrictionDynamicDescription implements DynamicAttributeHandler<String, CMSCartConfigurationRestrictionModel>
{

	@Override
	public String get(final CMSCartConfigurationRestrictionModel arg0)
	{
		return Localization.getLocalizedString("type.CMSCartConfigurationRestriction.description.text");

	}

	@Override
	public void set(final CMSCartConfigurationRestrictionModel arg0, final String arg1)
	{
		throw new UnsupportedOperationException();
	}



}
