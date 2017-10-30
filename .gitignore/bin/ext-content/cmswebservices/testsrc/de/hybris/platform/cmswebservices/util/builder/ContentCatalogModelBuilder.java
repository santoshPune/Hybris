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
 */
package de.hybris.platform.cmswebservices.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;

import java.util.Locale;
import java.util.Set;

public class ContentCatalogModelBuilder {

	private final ContentCatalogModel model;

	private ContentCatalogModelBuilder()
	{
		model = new ContentCatalogModel();
	}

	private ContentCatalogModelBuilder(final ContentCatalogModel model)
	{
		this.model = model;
	}

	private ContentCatalogModel getModel()
	{
		return this.model;
	}

	public static ContentCatalogModelBuilder aModel()
	{
		return new ContentCatalogModelBuilder();
	}

	public static ContentCatalogModelBuilder fromModel(final ContentCatalogModel model)
	{
		return new ContentCatalogModelBuilder(model);
	}

	public ContentCatalogModel build()
	{
		return this.getModel();
	}

	public ContentCatalogModelBuilder withId(final String id)
	{
		getModel().setId(id);
		return this;
	}

	public ContentCatalogModelBuilder withName(final String name, final Locale loc)
	{
		getModel().setName(name, loc);
		return this;
	}

	public ContentCatalogModelBuilder withCatalogVersions(final Set<CatalogVersionModel> catalogVersions)
	{
		getModel().setCatalogVersions(catalogVersions);
		return this;
	}

	public ContentCatalogModelBuilder withDefault(final boolean isDefault)
	{
		getModel().setDefaultCatalog(isDefault);
		return this;
	}
}
