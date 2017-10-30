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
package de.hybris.platform.sap.productconfig.frontend.breadcrumb;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.localization.Localization;

import java.util.List;


public class ProductConfigureBreadcrumbBuilder extends ProductBreadcrumbBuilder
{

	private static final String LAST_LINK_CLASS = "active";

	@Override
	public List<Breadcrumb> getBreadcrumbs(final ProductModel productModel)
	{
		final List<Breadcrumb> breadcrumbs = super.getBreadcrumbs(productModel.getCode());

		breadcrumbs.stream().filter(t -> LAST_LINK_CLASS.equalsIgnoreCase(t.getLinkClass())).forEach(t -> t.setLinkClass(null));

		final Breadcrumb last = new Breadcrumb(getConfigurationUrl(productModel), getLinkText(), LAST_LINK_CLASS);
		breadcrumbs.add(last);

		return breadcrumbs;
	}

	public List<Breadcrumb> getOverviewBreadcrumbs(final ProductModel productModel)
	{
		final List<Breadcrumb> breadcrumbs = getBreadcrumbs(productModel);

		breadcrumbs.stream().filter(t -> LAST_LINK_CLASS.equalsIgnoreCase(t.getLinkClass())).forEach(t -> t.setLinkClass(null));

		final Breadcrumb last = new Breadcrumb(getConfigurationOverviewUrl(productModel), getOverviewLinkText(), LAST_LINK_CLASS);
		breadcrumbs.add(last);

		return breadcrumbs;
	}

	protected String getLinkText()
	{
		if (Registry.isStandaloneMode())
		{
			return "TEST-STANDALONE-MODE";
		}
		return Localization.getLocalizedString("sapproductconfig.config.breadcrumb");
	}

	protected String getOverviewLinkText()
	{
		if (Registry.isStandaloneMode())
		{
			return "TEST-STANDALONE-MODE";
		}
		return Localization.getLocalizedString("sapproductconfig.config.overview.breadcrumb");
	}

	protected String getConfigurationUrl(final ProductModel productModel)
	{
		final String productUrl = super.getProductModelUrlResolver().resolve(productModel);
		return productUrl + "/config";
	}

	protected String getConfigurationOverviewUrl(final ProductModel productModel)
	{
		final String productUrl = super.getProductModelUrlResolver().resolve(productModel);
		return productUrl + "/configOverview";
	}

}
