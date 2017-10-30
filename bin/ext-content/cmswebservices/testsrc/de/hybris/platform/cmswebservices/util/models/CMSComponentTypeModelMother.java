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
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmswebservices.util.builder.CMSComponentTypeModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.CMSComponentTypeDao;

import org.springframework.beans.factory.annotation.Required;


public class CMSComponentTypeModelMother extends AbstractModelMother<CMSComponentTypeModel>
{

	protected static final String CODE_CMS_PARAGRAPH_COMPONENT = CMSParagraphComponentModel._TYPECODE;
	protected static final String CODE_BANNER_COMPONENT = BannerComponentModel._TYPECODE;
	protected static final String CODE_SIMPLE_BANNER_COMPONENT = SimpleBannerComponentModel._TYPECODE;

	private CMSComponentTypeDao cmsComponentTypeDao;

	public CMSComponentTypeModel CMSParagraphComponent()
	{
		return getOrSaveAndReturn( //
				() -> cmsComponentTypeDao.getCMSComponentTypeByCode(CODE_CMS_PARAGRAPH_COMPONENT), //
				() -> CMSComponentTypeModelBuilder.aModel() //
						.withCode(CODE_CMS_PARAGRAPH_COMPONENT) //
						.build());
	}

	public CMSComponentTypeModel BannerComponent()
	{
		return getOrSaveAndReturn( //
				() -> cmsComponentTypeDao.getCMSComponentTypeByCode(CODE_BANNER_COMPONENT), //
				() -> CMSComponentTypeModelBuilder.aModel() //
						.withCode(CODE_BANNER_COMPONENT) //
						.build());
	}

	public CMSComponentTypeModel SimpleBannerComponent()
	{
		return getOrSaveAndReturn( //
				() -> cmsComponentTypeDao.getCMSComponentTypeByCode(CODE_SIMPLE_BANNER_COMPONENT), //
				() -> CMSComponentTypeModelBuilder.aModel() //
						.withCode(CODE_SIMPLE_BANNER_COMPONENT) //
						.build());
	}

	protected CMSComponentTypeDao getCmsComponentTypeDao()
	{
		return cmsComponentTypeDao;
	}

	@Required
	public void setCmsComponentTypeDao(CMSComponentTypeDao cmsComponentTypeDao)
	{
		this.cmsComponentTypeDao = cmsComponentTypeDao;
	}

}
