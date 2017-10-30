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

import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.util.builder.SimpleResponsiveBannerComponentModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.SimpleResponsiveBannerComponentDao;

import java.util.Locale;


public class SimpleResponsiveBannerComponentModelMother extends AbstractModelMother<SimpleResponsiveBannerComponentModel>
{

	public static final String UID_HEADER_LOGO = "uid-responsive-banner-header-logo";
	public static final String NAME_HEADER_LOGO = "name-responsive-banner-header-logo";
	public static final String URL_HEADER_LOGO = "url-responsive-banner-header-logo";

	private SimpleResponsiveBannerComponentDao simpleResponsiveBannerComponentDao;
	private MediaContainerModelMother mediaContainerModelMother;

	public SimpleResponsiveBannerComponentModel createSimpleResponsiveBannerComponentModel(final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn(
				() -> getSimpleResponsiveBannerComponentDao().getByUidAndCatalogVersion(UID_HEADER_LOGO, catalogVersion), //
				() -> SimpleResponsiveBannerComponentModelBuilder.aModel()
				.withUid(UID_HEADER_LOGO)
				.withCatalogVersion(catalogVersion)
				.withName(NAME_HEADER_LOGO)
				.withUrlLink(URL_HEADER_LOGO)
				.withMedia(mediaContainerModelMother.createEmptyMediaContainerModel(catalogVersion), Locale.ENGLISH)
				.build());
	}

	public SimpleResponsiveBannerComponentModel createSimpleResponsiveBannerComponentModelWithLogoMedia(
			final CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn(
				() -> getSimpleResponsiveBannerComponentDao().getByUidAndCatalogVersion(UID_HEADER_LOGO, catalogVersion), //
				() -> SimpleResponsiveBannerComponentModelBuilder.aModel().withUid(UID_HEADER_LOGO).withCatalogVersion(catalogVersion)
				.withName(NAME_HEADER_LOGO).withUrlLink(URL_HEADER_LOGO)
				.withMedia(mediaContainerModelMother.createMediaContainerModelWithLogos(catalogVersion), Locale.ENGLISH)
				.build());
	}

	public SimpleResponsiveBannerComponentDao getSimpleResponsiveBannerComponentDao()
	{
		return simpleResponsiveBannerComponentDao;
	}

	public void setSimpleResponsiveBannerComponentDao(final SimpleResponsiveBannerComponentDao
			simpleResponsiveBannerComponentDao)
	{
		this.simpleResponsiveBannerComponentDao = simpleResponsiveBannerComponentDao;
	}

	public MediaContainerModelMother getMediaContainerModelMother()
	{
		return mediaContainerModelMother;
	}

	public void setMediaContainerModelMother(final MediaContainerModelMother mediaContainerModelMother)
	{
		this.mediaContainerModelMother = mediaContainerModelMother;
	}
}
