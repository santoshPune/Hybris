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

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.util.builder.SimpleBannerComponentModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.SimpleBannerComponentDao;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class SimpleBannerComponentModelMother extends AbstractModelMother<SimpleBannerComponentModel>
{

	public static final String UID_HEADER_LOGO = "uid-banner-header-logo";
	public static final String NAME_HEADER_LOGO = "name-banner-header-logo";
	public static final String URL_HEADER_LOGO = "url-banner-header-logo";

	private SimpleBannerComponentDao simpleBannerComponentDao;
	private MediaModelMother mediaModelMother;

	public SimpleBannerComponentModel createHeaderLogoBannerComponentModel(CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn(
				() -> simpleBannerComponentDao.getByUidAndCatalogVersion(UID_HEADER_LOGO, catalogVersion), //
				() -> SimpleBannerComponentModelBuilder.aModel()
						.withUid(UID_HEADER_LOGO)
						.withCatalogVersion(catalogVersion)
						.withName(NAME_HEADER_LOGO)
						.withExternal(Boolean.FALSE)
						.withUrlLink(URL_HEADER_LOGO)
						.withMedia(mediaModelMother.createLogoMediaModel(catalogVersion), Locale.ENGLISH)
						.build());
	}

	public SimpleBannerComponentDao getSimpleBannerComponentDao()
	{
		return simpleBannerComponentDao;
	}

	@Required
	public void setSimpleBannerComponentDao(SimpleBannerComponentDao simpleBannerComponentDao)
	{
		this.simpleBannerComponentDao = simpleBannerComponentDao;
	}

	public MediaModelMother getMediaModelMother()
	{
		return mediaModelMother;
	}

	@Required
	public void setMediaModelMother(MediaModelMother mediaModelMother)
	{
		this.mediaModelMother = mediaModelMother;
	}

}
