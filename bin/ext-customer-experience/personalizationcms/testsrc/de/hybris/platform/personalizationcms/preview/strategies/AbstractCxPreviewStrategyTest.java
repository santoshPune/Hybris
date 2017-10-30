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
 *
 *
 */
package de.hybris.platform.personalizationcms.preview.strategies;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;


@Ignore
public abstract class AbstractCxPreviewStrategyTest extends ServicelayerTransactionalTest
{
	protected static final String CX_CONTEXT_USER_UID = "cxcontextcustomer@hybris.com";
	protected static final String COMPONENT_ID = "cxcomponent1";

	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private PreviewDataModel previewContext;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importCsv("/personalizationcms/test/testdata_personalizationcms.impex", "utf-8");
		createPreviewContext();
	}

	protected void createPreviewContext()
	{
		previewContext = modelService.create(PreviewDataModel.class);
		previewContext.setLiveEdit(Boolean.TRUE);
		initPreviewContext();
		modelService.save(previewContext);
	}

	protected abstract void initPreviewContext();

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public PreviewDataModel getPreviewContext()
	{
		return previewContext;
	}

	public void setPreviewContext(final PreviewDataModel previewContext)
	{
		this.previewContext = previewContext;
	}
}
