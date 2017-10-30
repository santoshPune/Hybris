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

import static de.hybris.platform.cmswebservices.util.models.MediaModelMother.MediaTemplate.THUMBNAIL;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmswebservices.util.builder.ContentPageModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.ContentPageDao;

import org.springframework.beans.factory.annotation.Required;


public class ContentPageModelMother extends AbstractModelMother<ContentPageModel>
{

	public static final String UID_HOMEPAGE = "uid-homepage";
	public static final String UID_EMAILPAGE_HOMETEMPLATE = "uid-emailpage";
	public static final String UID_EMAILPAGE_EMAILTEMPLATE = "uid-emailpage";
	public static final String NAME_SUFFIX = "_page";
	public static final String TITLE_SUFFIX = "_pagetitle";

	private ContentPageDao contentPageDao;
	private PageTemplateModelMother pageTemplateModelMother;
	private MediaModelMother mediaModelMother;

	public ContentPageModel HomePage(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_HOMEPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
				.withUid(UID_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
				.asHomepage()
				.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
				.withDefaultPage(Boolean.TRUE) //
				.withApprovalStatus(CmsApprovalStatus.APPROVED) //
				.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL))
				.build());
	}

	public ContentPageModel EmailPageFromHomePageTemplate(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_EMAILPAGE_HOMETEMPLATE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
				.withUid(UID_EMAILPAGE_HOMETEMPLATE) //
				.withCatalogVersion(catalogVersion) //
				.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
				.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL)).build());
	}

	public ContentPageModel EmailPageFromEmailPageTemplate(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_EMAILPAGE_EMAILTEMPLATE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
				.withUid(UID_EMAILPAGE_EMAILTEMPLATE) //
				.withCatalogVersion(catalogVersion) //
				.withMasterTemplate(pageTemplateModelMother.EmailPage_Template(catalogVersion)) //
				.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL))
				.build());
	}

	public ContentPageModel somePage(final CatalogVersionModel catalogVersion, final String uid, final String nameTitle)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(uid, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
				.withUid(uid) //
				.withCatalogVersion(catalogVersion) //
				.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
				.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL))
				.withName(nameTitle + NAME_SUFFIX)
				.withEnglishTitle(nameTitle + TITLE_SUFFIX)
				.build());
	}

	public ContentPageDao getContentPageDao()
	{
		return contentPageDao;
	}

	@Required
	public void setContentPageDao(final ContentPageDao contentPageDao)
	{
		this.contentPageDao = contentPageDao;
	}

	public PageTemplateModelMother getPageTemplateModelMother()
	{
		return pageTemplateModelMother;
	}

	@Required
	public void setPageTemplateModelMother(final PageTemplateModelMother pageTemplateModelMother)
	{
		this.pageTemplateModelMother = pageTemplateModelMother;
	}

	public MediaModelMother getMediaModelMother() {
		return mediaModelMother;
	}

	@Required
	public void setMediaModelMother(final MediaModelMother mediaModelMother) {
		this.mediaModelMother = mediaModelMother;
	}
}
