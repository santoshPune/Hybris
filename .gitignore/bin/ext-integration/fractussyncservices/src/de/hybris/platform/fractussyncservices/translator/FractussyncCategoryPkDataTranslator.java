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
*/
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class FractussyncCategoryPkDataTranslator extends AbstractSpecialValueTranslator
{
	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(FractussyncCategoryPkDataTranslator.class);

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		if (item instanceof Category)
		{
			final CategoryModel categorytModel = getModelService().get(item);

			if (categorytModel != null)
			{
				return categorytModel.getPk().toString();
			}
			else
			{
				LOG.warn("No categorytModel found.");
			}
		}

		return StringUtils.EMPTY;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}

