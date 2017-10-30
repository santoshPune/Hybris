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
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cmswebservices.util.builder.ContentSlotModelBuilder;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;


public class ContentSlotModelMother extends AbstractModelMother<ContentSlotModel>
{
	public static final String UID_LOGO = "uid-contentslot-logo";
	public static final String UID_HEADER = "uid-contentslot-header";
	public static final String UID_FOOTER = "uid-contentslot-footer";

	private CMSContentSlotDao cmsContentSlotDao;
	private ParagraphComponentModelMother paragraphComponentModelMother;
	private SimpleBannerComponentModelMother simpleBannerComponentModelMother;
	private FlashComponentModelMother flashComponentModelMother;
	private ABTestCMSComponentContainerModelMother abTestCMSComponentContainerModelMother;

	public ContentSlotModel Logo_Slot(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_LOGO,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_LOGO) //
				.withCmsComponents( //
						getSimpleBannerComponentModelMother().createHeaderLogoBannerComponentModel(catalogVersion)) //
				.build());
	}

	public ContentSlotModel createFooterEmptySlot(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_FOOTER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_FOOTER) //
				.build());
	}

	public ContentSlotModel createHeaderSlotWithParagraphAndBanner(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_HEADER) //
				.withCmsComponents( //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion), //
						getSimpleBannerComponentModelMother().createHeaderLogoBannerComponentModel(catalogVersion)) //
				.build());
	}

	public ContentSlotModel createPagesOfComponents(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_HEADER) //
				.withCmsComponents( //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion, ParagraphComponentModelMother.UID_HEADER+1, "Luke is good"), //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion, ParagraphComponentModelMother.UID_HEADER+2, "abc"), //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion, ParagraphComponentModelMother.UID_HEADER+3, "where is lUKe"), //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion, ParagraphComponentModelMother.UID_HEADER+4, "def"), //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion, ParagraphComponentModelMother.UID_HEADER+5, "ask LUKE how")) //
				.build());
	}

	
	public ContentSlotModel createHeaderSlotWithParagraph(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_HEADER) //
				.withCmsComponents( //
						getParagraphComponentModelMother().createHeaderParagraphComponentModel(catalogVersion)) //
				.build());
	}

	public ContentSlotModel createHeaderSlotWithFlashComponent(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
				.withUid(UID_HEADER) //
				.withCmsComponents( //
						getFlashComponentModelMother().createHeaderFlashComponentModel(catalogVersion)) //
				.build());
	}

	public ContentSlotModel createHeaderSlotWithABTestParagraphsContainer(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
						.withUid(UID_HEADER) //
						.withCmsComponents( //
								getAbTestCMSComponentContainerModelMother().createHeaderParagraphsABTestContainerModel(catalogVersion)) //
						.build());
	}

	protected ContentSlotModel defaultSlot(final CatalogVersionModel catalogVersion)
	{
		return ContentSlotModelBuilder.aModel() //
				.withCatalogVersion(catalogVersion) //
				.build();
	}

	public CMSContentSlotDao getCmsContentSlotDao()
	{
		return cmsContentSlotDao;
	}

	@Required
	public void setCmsContentSlotDao(final CMSContentSlotDao cmsContentSlotDao)
	{
		this.cmsContentSlotDao = cmsContentSlotDao;
	}

	public ParagraphComponentModelMother getParagraphComponentModelMother()
	{
		return paragraphComponentModelMother;
	}

	@Required
	public void setParagraphComponentModelMother(final ParagraphComponentModelMother paragraphComponentModelMother)
	{
		this.paragraphComponentModelMother = paragraphComponentModelMother;
	}

	public SimpleBannerComponentModelMother getSimpleBannerComponentModelMother()
	{
		return simpleBannerComponentModelMother;
	}

	@Required
	public void setSimpleBannerComponentModelMother(final SimpleBannerComponentModelMother simpleBannerComponentModelMother)
	{
		this.simpleBannerComponentModelMother = simpleBannerComponentModelMother;
	}

	protected FlashComponentModelMother getFlashComponentModelMother()
	{
		return flashComponentModelMother;
	}

	@Required
	public void setFlashComponentModelMother(final FlashComponentModelMother flashComponentModelMother)
	{
		this.flashComponentModelMother = flashComponentModelMother;
	}

	public ABTestCMSComponentContainerModelMother getAbTestCMSComponentContainerModelMother()
	{
		return abTestCMSComponentContainerModelMother;
	}

	@Required
	public void setAbTestCMSComponentContainerModelMother(
			ABTestCMSComponentContainerModelMother abTestCMSComponentContainerModelMother)
	{
		this.abTestCMSComponentContainerModelMother = abTestCMSComponentContainerModelMother;
	}

}
