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

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmswebservices.util.builder.CMSPageTypeModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.CMSPageTypeDao;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import org.springframework.beans.factory.annotation.Required;


public class CMSPageTypeModelMother extends AbstractModelMother<CMSPageTypeModel>
{
	protected static final Class<?> SUPER_TYPE = CMSItemModel.class;
	public static final String CODE_CONTENT_PAGE = "ContentPage";
	protected static final String CODE_CATALOG_PAGE = "CatalogPage";

	private CMSPageTypeDao cmsPageTypeDao;
	private TypeService typeService;

	public CMSPageTypeModel ContentPage()
	{
		return getOrSaveAndReturn( //
				() -> cmsPageTypeDao.getCMSPageTypeByCode(CODE_CONTENT_PAGE), //
				() -> CMSPageTypeModelBuilder.fromModel(defaultPage()) //
						.withCode(CODE_CONTENT_PAGE) //
						.build());
	}

	public CMSPageTypeModel CatalogPage()
	{
		return getOrSaveAndReturn( //
				() -> cmsPageTypeDao.getCMSPageTypeByCode(CODE_CATALOG_PAGE), //
				() -> CMSPageTypeModelBuilder.fromModel(defaultPage()) //
						.withCode(CODE_CATALOG_PAGE) //
						.build());
	}

	protected CMSPageTypeModel defaultPage()
	{
		return CMSPageTypeModelBuilder.aModel() //
				.withCatalogItemType(Boolean.TRUE) //
				.withGenerate(Boolean.FALSE) //
				.withSingleton(Boolean.TRUE) //
				.withSuperType(getComposedTypeModel()) //
				.build();
	}

	protected ComposedTypeModel getComposedTypeModel()
	{
		return typeService.getComposedTypeForClass(SUPER_TYPE);
	}

	protected CMSPageTypeDao getCmsPageTypeDao()
	{
		return cmsPageTypeDao;
	}

	@Required
	public void setCmsPageTypeDao(CMSPageTypeDao cmsPageTypeDao)
	{
		this.cmsPageTypeDao = cmsPageTypeDao;
	}

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}


}
