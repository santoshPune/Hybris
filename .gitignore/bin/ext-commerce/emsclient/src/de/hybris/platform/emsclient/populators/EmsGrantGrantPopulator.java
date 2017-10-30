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
package de.hybris.platform.emsclient.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementservices.data.EmsGrantData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.hybris.services.entitlements.api.GrantData;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter implementation for {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as source and
 * {@link com.hybris.services.entitlements.api.GrantData} as target type.
 */
public class EmsGrantGrantPopulator implements Populator<EmsGrantData, GrantData>
{

	private Collection<Converter<EmsGrantData, ConditionData>> conditionDataConverters;
	private DateTimeConverter dateTimeConverter;

	@Override
	public void populate(final EmsGrantData source, final GrantData target) throws ConversionException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		target.setEntitlementType(source.getEntitlementType());
		target.setGrantSource(source.getBaseStoreUid());
		target.setUserId(source.getUserId());
		String sourceId = source.getOrderCode();
		if (source.getOrderEntryNumber() != null)
		{
			if (sourceId == null)
			{
				sourceId = source.getOrderEntryNumber();
			}
			else
			{
				sourceId += '_';
				sourceId += source.getOrderEntryNumber();
			}
		}
		target.setGrantSourceId(sourceId);
		target.setGrantTime(getDateTimeConverter().convertDateToString(source.getCreatedAt()));

		for (final Converter<EmsGrantData, ConditionData> converter : getConditionDataConverters())
		{
			final ConditionData conditionData = converter.convert(source);
			if (conditionData.getType() != null)
			{
				target.addCondition(conditionData);
			}
		}
	}

	protected Collection<Converter<EmsGrantData, ConditionData>> getConditionDataConverters()
	{
		return conditionDataConverters;
	}

	@Required
	public void setConditionDataConverters(final Collection<Converter<EmsGrantData, ConditionData>> conditionDataConverters)
	{
		this.conditionDataConverters = conditionDataConverters;
	}

	protected DateTimeConverter getDateTimeConverter()
	{
		return dateTimeConverter;
	}

	@Required
	public void setDateTimeConverter(final DateTimeConverter dateTimeConverter)
	{
		this.dateTimeConverter = dateTimeConverter;
	}
}
