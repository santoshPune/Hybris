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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.List;


/**
 * Top Values are a list of facet values that are immediately shown on search and category pages for facets with many
 * values. Other values will be collapsed.
 */
public interface TopValuesProvider
{
	List<FacetValue> getTopValues(IndexedProperty indexedProperty, List<FacetValue> facets);
}
