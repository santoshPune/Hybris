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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationLookupStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;


public abstract class AbstractFractusApplicationLookupStrategy implements FractusApplicationLookupStrategy
{
	private ModelService modelService;

	protected Set<YaasApplicationModel> getApplications(final ProductModel productModel)
	{
		final Set<YaasApplicationModel> apps = Sets.newHashSet();

		final Collection<BaseStoreModel> baseStores = productModel.getCatalogVersion().getCatalog().getBaseStores();

		for (final BaseStoreModel baseStore : baseStores)
		{
			for (final BaseSiteModel baseSiteModel : baseStore.getCmsSites())
			{
				apps.addAll(getApplications(baseSiteModel));
			}
		}

		return apps;
	}

	protected Set<YaasApplicationModel> getApplications(final BaseSiteModel baseSiteModel)
	{
		final Set<YaasApplicationModel> apps = Sets.newHashSet();

		for (final YaasProjectModel yaasProjectModel : baseSiteModel.getYaasProjects())
		{
			apps.addAll(yaasProjectModel.getYaasApplications());
		}

		return apps;
	}


	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
