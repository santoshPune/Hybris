/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */
package de.hybris.platform.financialacceleratorstorefront.comparison;

import java.util.LinkedHashMap;
import java.util.Map;


public class ComparisonTable
{
	private Map<Object, ComparisonTableColumn> columns = new LinkedHashMap<Object, ComparisonTableColumn>();

	public void addColumn(final Object key, final ComparisonTableColumn column)
	{
		getColumns().put(key, column);
	}

	public Map<Object, ComparisonTableColumn> getColumns()
	{
		return columns;
	}

	public void setColumns(final Map<Object, ComparisonTableColumn> columns)
	{
		this.columns = columns;
	}
}
