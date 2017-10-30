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
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class IndexedTypeSortPopulator implements Populator<SolrSortModel, IndexedTypeSort>
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SolrSortModel source, final IndexedTypeSort target) throws ConversionException
	{
		target.setSort(source);
		target.setCode(source.getCode());
		target.setName(source.getName());
	}

}
