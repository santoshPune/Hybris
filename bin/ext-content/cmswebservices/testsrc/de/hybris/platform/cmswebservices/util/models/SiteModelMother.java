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

import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.cmswebservices.util.builder.SiteModelBuilder;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class SiteModelMother extends AbstractModelMother<CMSSiteModel>
{
	public static final String ELECTRONICS = "electronics";
	public static final String APPAREL = "test-apparel";
	public static final String POWERTOOL = "test-powertools";

	public static final String PREVIEW_URL = "/yacceleratorstorefront/?site=electronics";
	public static final String REDIRECT_URL = "/dummy/redirect/path";

	private CMSSiteDao cmsSiteDao;
	private LanguageModelMother languageModelMother;
	private ContentCatalogModelMother catalogModelMother;
	private BaseStoreModelMother baseStoreModelMother;
	private CatalogVersionModelMother catalogVersionModelMother;

	protected CMSSiteModel defaultSite()
	{
		return SiteModelBuilder.aModel().withActive(Boolean.TRUE).build();
	}

	public CMSSiteModel createElectronicsWithAppleCatalog()
	{
		return getFromCollectionOrSaveAndReturn(
				() -> getCmsSiteDao().findCMSSitesById(ELECTRONICS),
				() -> SiteModelBuilder.fromModel(defaultSite()) //
				.withUid(ELECTRONICS) //
				.withPreviewRUL(PREVIEW_URL) //
				.withRedirectUrl(REDIRECT_URL) //
				.withName(ELECTRONICS, Locale.ENGLISH) //
				.withLanguage(getLanguageModelMother().createEnglish()) //
				.withDefaultCatalog(getCatalogModelMother().createAppleContentCatalogModel()) //
				.build());
	}

	public CMSSiteModel createElectronicsWithAppleStagedAndOnlineCatalog()
	{
		final ContentCatalogModel appleCatalog = getCatalogModelMother().createAppleStagedAndOnlineContentCatalogModel();
		return getFromCollectionOrSaveAndReturn(() -> getCmsSiteDao().findCMSSitesById(ELECTRONICS),
				() -> SiteModelBuilder.fromModel(defaultSite()) //
				.withUid(ELECTRONICS) //
				.withPreviewRUL(PREVIEW_URL) //
				.withRedirectUrl(REDIRECT_URL) //
				.withName(ELECTRONICS, Locale.ENGLISH) //
				.withLanguage(getLanguageModelMother().createEnglish()) //
				.withDefaultCatalog(appleCatalog) //
				.withContentCatalogs(Arrays.asList(appleCatalog)) //
				.build());
	}

	public CMSSiteModel createNorthAmericaElectronicsWithAppleStagedCatalog()
	{
		return getFromCollectionOrSaveAndReturn(() -> getCmsSiteDao().findCMSSitesById(ELECTRONICS),
				() -> SiteModelBuilder.fromModel(defaultSite()) //
				.withUid(ELECTRONICS) //
				.withPreviewRUL(PREVIEW_URL) //
				.withRedirectUrl(REDIRECT_URL) //
				.withName(ELECTRONICS, Locale.ENGLISH) //
				.withLanguage(getLanguageModelMother().createEnglish()) //
				.withDefaultCatalog(getCatalogModelMother().createAppleContentCatalogModel()) //
				.withStores(Arrays.asList(getBaseStoreModelMother()
						.createNorthAmerica(getCatalogVersionModelMother().createAppleStagedCatalogVersionModel())))
				.build());
	}

	protected LanguageModelMother getLanguageModelMother()
	{
		return languageModelMother;
	}

	@Required
	public void setLanguageModelMother(final LanguageModelMother languageModelMother)
	{
		this.languageModelMother = languageModelMother;
	}

	protected ContentCatalogModelMother getCatalogModelMother()
	{
		return catalogModelMother;
	}

	@Required
	public void setCatalogModelMother(final ContentCatalogModelMother catalogModelMother)
	{
		this.catalogModelMother = catalogModelMother;
	}

	protected CMSSiteDao getCmsSiteDao()
	{
		return cmsSiteDao;
	}

	@Required
	public void setCmsSiteDao(final CMSSiteDao cmsSiteDao)
	{
		this.cmsSiteDao = cmsSiteDao;
	}

	protected BaseStoreModelMother getBaseStoreModelMother()
	{
		return baseStoreModelMother;
	}

	@Required
	public void setBaseStoreModelMother(final BaseStoreModelMother baseStoreModelMother)
	{
		this.baseStoreModelMother = baseStoreModelMother;
	}

	protected CatalogVersionModelMother getCatalogVersionModelMother()
	{
		return catalogVersionModelMother;
	}

	@Required
	public void setCatalogVersionModelMother(final CatalogVersionModelMother catalogVersionModelMother)
	{
		this.catalogVersionModelMother = catalogVersionModelMother;
	}
}
