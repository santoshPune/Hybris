/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.catalog.interceptors;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * Interceptor sets this catalog version as {@link CatalogModel#ACTIVECATALOGVERSION} if the
 * {@link CatalogVersionModel#ACTIVE} flag was set to TRUE.
 */
public class CatalogVersionPrepareInterceptor implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CatalogVersionModel)
		{
			final CatalogVersionModel catalogVersion = (CatalogVersionModel) model;
			//if ACTIVE flag was changed to TRUE
			if (ctx.isModified(catalogVersion, CatalogVersionModel.ACTIVE))
			{
				final CatalogModel catalog = catalogVersion.getCatalog();
				if (catalog == null)
				{
					//expect InterceptorException from validator.
					return;
				}
				final CatalogVersionModel activeCatalogVersion = catalog.getActiveCatalogVersion();

				//if current active version is different than this one and ACTIVE flag is set to true
				if (Boolean.TRUE.equals(catalogVersion.getActive()) && !catalogVersion.equals(activeCatalogVersion))
				{
					catalog.setActiveCatalogVersion(catalogVersion);
					ctx.registerElement(catalog);

					resetOtherCatalogVersionsFlags(catalog, catalogVersion, ctx);
				}
				//if current active version is this one and ACTIVE flag is set to false
				else if (Boolean.FALSE.equals(catalogVersion.getActive()) && catalogVersion.equals(activeCatalogVersion))
				{
					catalog.setActiveCatalogVersion(null);
					ctx.registerElement(catalog);
				}
			}
		}
	}

	private void resetOtherCatalogVersionsFlags(final CatalogModel catalog, final CatalogVersionModel catalogVersion,
			final InterceptorContext ctx)
	{
		if (catalog.getCatalogVersions() != null)
		{
			for (final CatalogVersionModel currentCatalogVersion : catalog.getCatalogVersions())
			{
				if (!currentCatalogVersion.equals(catalogVersion))
				{
					currentCatalogVersion.setActive(Boolean.FALSE);
					ctx.registerElement(currentCatalogVersion);
				}
			}
		}
	}

}
