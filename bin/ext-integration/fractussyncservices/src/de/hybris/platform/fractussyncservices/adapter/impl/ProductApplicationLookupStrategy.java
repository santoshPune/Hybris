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
package de.hybris.platform.fractussyncservices.adapter.impl;


import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class ProductApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	@Override
	public String getTypeCode()
	{
		return ProductModel._TYPECODE;
	}

	@Override
	public String lookup(final Item item)
	{
		if (item instanceof Product)
		{
			final ProductModel productModel = getModelService().get(item);

			final Set<YaasApplicationModel> applications = getApplications(productModel);
			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}

		return StringUtils.EMPTY;
	}
}
