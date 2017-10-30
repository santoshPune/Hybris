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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmswebservices.util.builder.ContentSlotForPageModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.ContentSlotForPageDao;

import org.springframework.beans.factory.annotation.Required;


public class ContentSlotForPageModelMother extends AbstractModelMother<ContentSlotForPageModel>
{
	public static final String UID_LOGO_HOMEPAGE = "uid-logo-homepage";
	public static final String UID_HEADER_HOMEPAGE = "uid-header-homepage";
	public static final String UID_FOOTER_HOMEPAGE = "uid-footer-homepage";
	public static final String UID_FOOTER_EMAILPAGE = "uid-footer-emailpage";

	private ContentSlotForPageDao contentSlotForPageDao;
	private ContentSlotModelMother contentSlotModelMother;
	private ContentPageModelMother contentPageModelMother;

	public ContentSlotForPageModel LogoHomepage(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_LOGO_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_LOGO_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().Logo_Slot(catalogVersion)) //
						.withPage(getContentPageModelMother().HomePage(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_LOGO) //
				.build());
	}

	public ContentSlotForPageModel HeaderHomepage_ParagraphOnly(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_HEADER_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_HEADER_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().createHeaderSlotWithParagraph(catalogVersion)) //
						.withPage(getContentPageModelMother().HomePage(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_HEADER) //
				.build());
	}

	public ContentSlotForPageModel HeaderHomepage_FlashComponentOnly(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_HEADER_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_HEADER_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().createHeaderSlotWithFlashComponent(catalogVersion)) //
						.withPage(getContentPageModelMother().HomePage(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_HEADER) //
				.build());
	}

	public ContentSlotForPageModel HeaderHomepage_ParagraphAndBanner(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_HEADER_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_HEADER_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().createHeaderSlotWithParagraphAndBanner(catalogVersion)) //
						.withPage(getContentPageModelMother().HomePage(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_HEADER) //
				.build());
	}

	public ContentSlotForPageModel FooterHomepage_Empty(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_FOOTER_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_FOOTER_HOMEPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().createFooterEmptySlot(catalogVersion)) //
						.withPage(getContentPageModelMother().HomePage(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_FOOTER) //
				.build());
	}

	public ContentSlotForPageModel FooterEmailPage_Empty(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentSlotForPageDao().getByUidAndCatalogVersion(UID_FOOTER_EMAILPAGE, catalogVersion), //
				() -> ContentSlotForPageModelBuilder.aModel() //
				.withUid(UID_FOOTER_EMAILPAGE) //
				.withCatalogVersion(catalogVersion) //
						.withContentSlot(getContentSlotModelMother().createFooterEmptySlot(catalogVersion)) //
						.withPage(getContentPageModelMother().EmailPageFromHomePageTemplate(catalogVersion)) //
				.withPosition(ContentSlotNameModelMother.NAME_FOOTER) //
				.build());
	}

	public ContentSlotForPageDao getContentSlotForPageDao()
	{
		return contentSlotForPageDao;
	}

	@Required
	public void setContentSlotForPageDao(final ContentSlotForPageDao contentSlotForPageDao)
	{
		this.contentSlotForPageDao = contentSlotForPageDao;
	}

	public ContentSlotModelMother getContentSlotModelMother()
	{
		return contentSlotModelMother;
	}

	@Required
	public void setContentSlotModelMother(final ContentSlotModelMother contentSlotModelMother)
	{
		this.contentSlotModelMother = contentSlotModelMother;
	}

	public ContentPageModelMother getContentPageModelMother()
	{
		return contentPageModelMother;
	}

	@Required
	public void setContentPageModelMother(final ContentPageModelMother contentPageModelMother)
	{
		this.contentPageModelMother = contentPageModelMother;
	}

}
