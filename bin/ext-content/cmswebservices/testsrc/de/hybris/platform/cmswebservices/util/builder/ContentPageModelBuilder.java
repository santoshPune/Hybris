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
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.media.MediaModel;
import static java.util.Locale.ENGLISH;

import static java.lang.Boolean.TRUE;


public class ContentPageModelBuilder
{

	private final ContentPageModel model;

	private ContentPageModelBuilder()
	{
		model = new ContentPageModel();
	}

	private ContentPageModelBuilder(ContentPageModel model)
	{
		this.model = model;
	}

	private ContentPageModel getModel()
	{
		return this.model;
	}

	public static ContentPageModelBuilder aModel()
	{
		return new ContentPageModelBuilder();
	}

	public static ContentPageModelBuilder fromModel(ContentPageModel model)
	{
		return new ContentPageModelBuilder(model);
	}

	public ContentPageModelBuilder withCatalogVersion(CatalogVersionModel model)
	{
		getModel().setCatalogVersion(model);
		return this;
	}

	public ContentPageModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public ContentPageModelBuilder withDefaultPage(Boolean defaultPage)
	{
		getModel().setDefaultPage(defaultPage);
		return this;
	}

	public ContentPageModelBuilder withMasterTemplate(PageTemplateModel template)
	{
		getModel().setMasterTemplate(template);
		return this;
	}

	public ContentPageModelBuilder withApprovalStatus(CmsApprovalStatus approvalStatus)
	{
		getModel().setApprovalStatus(approvalStatus);
		return this;
	}

	public ContentPageModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public ContentPageModelBuilder withLabel(String label)
	{
		getModel().setLabel(label);
		return this;
	}

	public ContentPageModelBuilder withThumbnail(final MediaModel thumbnail)
	{
		getModel().setPreviewImage(thumbnail);
		return this;
	}

	public ContentPageModelBuilder asHomepage() {
		getModel().setHomepage(TRUE);
		return this;
	}

	public ContentPageModelBuilder withEnglishTitle(String title)
	{
		getModel().setTitle(title, ENGLISH);
		return this;
	}

	public ContentPageModel build()
	{
		return this.getModel();
	}
}
