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
package de.hybris.platform.cmswebservices.util.builder;

import de.hybris.platform.cms2.model.CMSPageTypeModel;
import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.RestrictionTypeModel;

import java.util.Arrays;


public class RestrictionTypeModelBuilder
{

	private final RestrictionTypeModel model;

	private RestrictionTypeModelBuilder()
	{
		model = new RestrictionTypeModel();
	}

	private RestrictionTypeModelBuilder(RestrictionTypeModel model)
	{
		this.model = model;
	}

	private RestrictionTypeModel getModel()
	{
		return this.model;
	}

	public static RestrictionTypeModelBuilder aModel()
	{
		return new RestrictionTypeModelBuilder();
	}

	public static RestrictionTypeModelBuilder fromModel(RestrictionTypeModel model)
	{
		return new RestrictionTypeModelBuilder(model);
	}

	public RestrictionTypeModelBuilder withCode(String code)
	{
		getModel().setCode(code);
		return this;
	}

	public RestrictionTypeModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public RestrictionTypeModelBuilder withPageTypes(CMSPageTypeModel... pageTypes)
	{
		getModel().setPageTypes(Arrays.asList(pageTypes));
		return this;
	}

	public RestrictionTypeModel build()
	{
		return this.getModel();
	}
}
