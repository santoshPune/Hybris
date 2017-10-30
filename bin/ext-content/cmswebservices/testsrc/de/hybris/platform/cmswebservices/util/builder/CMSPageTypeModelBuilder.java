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
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Arrays;

import com.google.common.collect.Sets;


public class CMSPageTypeModelBuilder
{

	private final CMSPageTypeModel model;

	private CMSPageTypeModelBuilder()
	{
		model = new CMSPageTypeModel();
	}

	private CMSPageTypeModelBuilder(CMSPageTypeModel model)
	{
		this.model = model;
	}

	private CMSPageTypeModel getModel()
	{
		return this.model;
	}

	public static CMSPageTypeModelBuilder aModel()
	{
		return new CMSPageTypeModelBuilder();
	}

	public static CMSPageTypeModelBuilder fromModel(CMSPageTypeModel model)
	{
		return new CMSPageTypeModelBuilder(model);
	}

	public CMSPageTypeModelBuilder withCode(String code)
	{
		getModel().setCode(code);
		return this;
	}

	public CMSPageTypeModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public CMSPageTypeModelBuilder withPreviewDisabled(Boolean previewDisabled)
	{
		getModel().setPreviewDisabled(previewDisabled);
		return this;
	}

	public CMSPageTypeModelBuilder withCatalogItemType(Boolean catalogItemType)
	{
		getModel().setCatalogItemType(catalogItemType);
		return this;
	}

	public CMSPageTypeModelBuilder withGenerate(Boolean generate)
	{
		getModel().setGenerate(generate);
		return this;
	}

	public CMSPageTypeModelBuilder withSingleton(Boolean singleton)
	{
		getModel().setSingleton(singleton);
		return this;
	}

	public CMSPageTypeModelBuilder withSuperType(ComposedTypeModel composedTypeModel)
	{
		getModel().setSuperType(composedTypeModel);
		return this;
	}

	public CMSPageTypeModelBuilder withRestrictionTypeModel(RestrictionTypeModel... restrictionTypes)
	{
		getModel().setRestrictionTypes(Arrays.asList(restrictionTypes));
		return this;
	}

	public CMSPageTypeModelBuilder withTemplates(PageTemplateModel... templates)
	{
		getModel().setTemplates(Sets.newHashSet(Arrays.asList(templates)));
		return this;
	}

	public CMSPageTypeModel build()
	{
		return this.getModel();
	}
}
