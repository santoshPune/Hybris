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
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cmswebservices.util.builder.ABTestCMSComponentContainerModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.ABTestCMSComponentContainerDao;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


public class ABTestCMSComponentContainerModelMother extends AbstractModelMother<ABTestCMSComponentContainerModel>
{

	public static final String UID_HEADER = "uid-abtest-header";

	private ParagraphComponentModelMother paragraphComponentModelMother;
	private ABTestCMSComponentContainerDao abTestCMSComponentContainerDao;

	public ABTestCMSComponentContainerModel createHeaderParagraphsABTestContainerModel(CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> abTestCMSComponentContainerDao.getByUidAndCatalogVersion(UID_HEADER, catalogVersion), //
				() -> ABTestCMSComponentContainerModelBuilder.aModel() //
						.withUid(UID_HEADER) //
						.withCatalogVersion(catalogVersion) //
						.withSimpleCMSComponent(Lists.newArrayList( //
								paragraphComponentModelMother.createHeaderParagraphComponentModel(catalogVersion),
								paragraphComponentModelMother.createFooterParagraphComponentModel(catalogVersion))) //
						.build());
	}

	public ParagraphComponentModelMother getParagraphComponentModelMother()
	{
		return paragraphComponentModelMother;
	}

	@Required
	public void setParagraphComponentModelMother(ParagraphComponentModelMother paragraphComponentModelMother)
	{
		this.paragraphComponentModelMother = paragraphComponentModelMother;
	}

	public ABTestCMSComponentContainerDao getAbTestCMSComponentContainerDao()
	{
		return abTestCMSComponentContainerDao;
	}

	@Required
	public void setAbTestCMSComponentContainerDao(ABTestCMSComponentContainerDao abTestCMSComponentContainerDao)
	{
		this.abTestCMSComponentContainerDao = abTestCMSComponentContainerDao;
	}




}
