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

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Locale;


public class SimpleBannerComponentModelBuilder
{

	private final SimpleBannerComponentModel model;

	private SimpleBannerComponentModelBuilder()
	{
		model = new SimpleBannerComponentModel();
	}

	private SimpleBannerComponentModelBuilder(SimpleBannerComponentModel model)
	{
		this.model = model;
	}

	private SimpleBannerComponentModel getModel()
	{
		return this.model;
	}

	public static SimpleBannerComponentModelBuilder aModel()
	{
		return new SimpleBannerComponentModelBuilder();
	}

	public static SimpleBannerComponentModelBuilder fromModel(SimpleBannerComponentModel model)
	{
		return new SimpleBannerComponentModelBuilder(model);
	}

	public SimpleBannerComponentModelBuilder withUid(String code)
	{
		getModel().setUid(code);
		return this;
	}

	public SimpleBannerComponentModelBuilder withCatalogVersion(CatalogVersionModel cv)
	{
		getModel().setCatalogVersion(cv);
		return this;
	}

	public SimpleBannerComponentModelBuilder withMedia(MediaModel media)
	{
		getModel().setMedia(media);
		return this;
	}

	public SimpleBannerComponentModelBuilder withMedia(MediaModel media, Locale locale)
	{
		getModel().setMedia(media, locale);
		return this;
	}

	public SimpleBannerComponentModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public SimpleBannerComponentModelBuilder withExternal(Boolean external)
	{
		getModel().setExternal(external);
		return this;
	}

	public SimpleBannerComponentModelBuilder withUrlLink(String urlLink)
	{
		getModel().setUrlLink(urlLink);
		return this;
	}

	public SimpleBannerComponentModel build()
	{
		return this.getModel();
	}

}
