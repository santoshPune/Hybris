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
package de.hybris.platform.cmswebservices.util.models;

import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.util.builder.ContentSlotNameModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.ContentSlotNameDao;

import org.springframework.beans.factory.annotation.Required;


public class ContentSlotNameModelMother extends AbstractModelMother<ContentSlotNameModel>
{
	protected static final String NAME_HEADER = "name-header";
	protected static final String NAME_FOOTER = "name-footer";
	protected static final String NAME_LOGO = "name-logo";
	protected static final String NAME_TEXT_ONLY = "name-text-only";

	private ContentSlotNameDao contentSlotNameDao;
	private ComponentTypeGroupModelMother componentTypeGroupModelMother;

	public ContentSlotNameModel Header(final PageTemplateModel pageTemplate)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotNameDao.getContentSlotNameByName(NAME_HEADER), //
				() -> ContentSlotNameModelBuilder.aModel() //
						.withName(NAME_HEADER) //
						.withCompTypeGroup(componentTypeGroupModelMother.ParagraphsAndBanners()) //
						.withTemplate(pageTemplate) //
						.build());
	}

	public ContentSlotNameModel Header_without_restriction(final PageTemplateModel pageTemplate)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotNameDao.getContentSlotNameByName(NAME_HEADER), //
				() -> ContentSlotNameModelBuilder.aModel() //
						.withName(NAME_HEADER) //
						.withTemplate(pageTemplate) //
						.build());
	}

	public ContentSlotNameModel Footer(final PageTemplateModel pageTemplate)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotNameDao.getContentSlotNameByName(NAME_FOOTER), //
				() -> ContentSlotNameModelBuilder.aModel() //
						.withName(NAME_FOOTER) //
						.withCompTypeGroup(componentTypeGroupModelMother.ParagraphsAndBanners()) //
						.withTemplate(pageTemplate) //
						.build());
	}

	public ContentSlotNameModel TextOnly(final PageTemplateModel pageTemplate)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotNameDao.getContentSlotNameByName(NAME_TEXT_ONLY), //
				() -> ContentSlotNameModelBuilder.aModel() //
						.withName(NAME_TEXT_ONLY) //
						.withCompTypeGroup(componentTypeGroupModelMother.Paragraphs()) //
						.withTemplate(pageTemplate) //
						.build());
	}

	public ContentSlotNameModel Logo(final PageTemplateModel pageTemplate)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotNameDao.getContentSlotNameByName(NAME_LOGO), //
				() -> ContentSlotNameModelBuilder.aModel() //
						.withName(NAME_LOGO) //
						.withCompTypeGroup(componentTypeGroupModelMother.Banners()) //
						.withTemplate(pageTemplate) //
						.build());
	}

	protected ContentSlotNameDao getContentSlotNameDao()
	{
		return contentSlotNameDao;
	}

	@Required
	public void setContentSlotNameDao(final ContentSlotNameDao contentSlotNameDao)
	{
		this.contentSlotNameDao = contentSlotNameDao;
	}

	protected ComponentTypeGroupModelMother getComponentTypeGroupModelMother()
	{
		return componentTypeGroupModelMother;
	}

	@Required
	public void setComponentTypeGroupModelMother(final ComponentTypeGroupModelMother componentTypeGroupModelMother)
	{
		this.componentTypeGroupModelMother = componentTypeGroupModelMother;
	}

}
