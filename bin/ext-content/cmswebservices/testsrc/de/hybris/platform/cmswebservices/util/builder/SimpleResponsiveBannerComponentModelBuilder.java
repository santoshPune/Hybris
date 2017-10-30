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

import java.util.List;
import java.util.Locale;

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

public class SimpleResponsiveBannerComponentModelBuilder {
	
	private SimpleResponsiveBannerComponentModel model;
	
	
	private SimpleResponsiveBannerComponentModelBuilder()
	{
		model = new SimpleResponsiveBannerComponentModel();
	}
	
	private SimpleResponsiveBannerComponentModelBuilder(SimpleResponsiveBannerComponentModel model)
	{
		this.model = model;
	}
	
	private SimpleResponsiveBannerComponentModel getModel()
	{
		return this.model;
	}

	public static SimpleResponsiveBannerComponentModelBuilder aModel()
	{
		return new SimpleResponsiveBannerComponentModelBuilder();
	}
	
	public static SimpleResponsiveBannerComponentModelBuilder fromModel(SimpleResponsiveBannerComponentModel model)
	{
		return new SimpleResponsiveBannerComponentModelBuilder(model);
	}
	
	public SimpleResponsiveBannerComponentModelBuilder withMediaContainer(MediaContainerModel container)
	{
		getModel().setMedia(container);
		return this;
	}
	
	public SimpleResponsiveBannerComponentModelBuilder withCatalogVersion(CatalogVersionModel cv)
	{
		getModel().setCatalogVersion(cv);
		return this;
	}
	
	public SimpleResponsiveBannerComponentModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}
	
	public SimpleResponsiveBannerComponentModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public SimpleResponsiveBannerComponentModelBuilder withContentSlots(List<ContentSlotModel> slots)
	{
		getModel().setSlots(slots);
		return this;
	}

	public SimpleResponsiveBannerComponentModelBuilder withUrlLink(String urlLink)
	{
		getModel().setUrlLink(urlLink);
		return this;
	}
	
	public SimpleResponsiveBannerComponentModelBuilder withMedia(MediaContainerModel media, Locale locale)
	{
		getModel().setMedia(media, locale);
		return this;
	}

	public SimpleResponsiveBannerComponentModel build()
	{
		return this.getModel();
	}
}
