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


import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class PriceApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	private ProductService productService;
	private CatalogVersionService catalogVersionService;
	private String catalogName;

	/**
	 * @return the productService
	 */
	protected ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the catalogVersionService
	 */
	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the catalogName
	 */
	public String getCatalogName()
	{
		return catalogName;
	}

	/**
	 * @param catalogName
	 *           the catalogName to set
	 */
	public void setCatalogName(final String catalogName)
	{
		this.catalogName = catalogName;
	}

	@Override
	public String getTypeCode()
	{
		return PriceRowModel._TYPECODE;
	}

	@Override
	public String lookup(final Item item)
	{
		if (item instanceof PriceRow)
		{
			final PriceRowModel priceRowModel = getModelService().get(item);
			final CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(getCatalogName(),
					CatalogManager.ONLINE_VERSION);
			final String productCode = priceRowModel.getProductId();
			final ProductModel product = getProductService().getProduct(catalogVersion, productCode);
			final Set<YaasApplicationModel> applications = getApplications(product);

			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}

		return StringUtils.EMPTY;
	}

}
