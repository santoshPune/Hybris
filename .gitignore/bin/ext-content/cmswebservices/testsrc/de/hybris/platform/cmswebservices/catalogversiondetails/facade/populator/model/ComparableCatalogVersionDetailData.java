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
package de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;

import java.util.Map;

public class ComparableCatalogVersionDetailData extends CatalogVersionDetailData
{

	public ComparableCatalogVersionDetailData(){
	}

	public ComparableCatalogVersionDetailData(final CatalogVersionDetailData details){
		try {
			copyProperties(this, details);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this, excludeFields());
	}

	@Override
	public boolean equals(final Object that) {
		return reflectionEquals(this, that, excludeFields())
				&& (
						(getName() == null)
						||
						(getName() instanceof Map<?, ?> && getName().equals(((CatalogVersionDetailData) that).getName()))
				);
	}

	@Override
	public String toString() {
		return reflectionToString(this, MULTI_LINE_STYLE);
	}

	public String[] excludeFields()  {
		return new String[] {"name"};
	}
}
