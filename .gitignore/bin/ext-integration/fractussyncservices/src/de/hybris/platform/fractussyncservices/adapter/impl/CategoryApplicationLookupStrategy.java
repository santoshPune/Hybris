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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Sets;


public class CategoryApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	@Override
	public String getTypeCode()
	{
		return CategoryModel._TYPECODE;
	}

	@Override
	public String lookup(final Item item)
	{
		if (item instanceof Category)
		{
			final CategoryModel categoryModel = getModelService().get(item);

			final Set<YaasApplicationModel> applications = getApplications(categoryModel);
			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}

		return StringUtils.EMPTY;
	}

	protected Set<YaasApplicationModel> getApplications(final CategoryModel categoryModel)
	{
		final Set<YaasApplicationModel> apps = Sets.newHashSet();

		final Collection<BaseStoreModel> baseStores = categoryModel.getCatalogVersion().getCatalog().getBaseStores();

		for (final BaseStoreModel baseStore : baseStores)
		{
			for (final BaseSiteModel baseSiteModel : baseStore.getCmsSites())
			{
				apps.addAll(getApplications(baseSiteModel));
			}
		}

		return apps;
	}
}
