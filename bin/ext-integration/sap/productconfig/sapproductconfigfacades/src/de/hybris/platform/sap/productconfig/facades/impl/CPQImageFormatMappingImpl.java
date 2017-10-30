package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.CPQImageFormatMapping;
import de.hybris.platform.sap.productconfig.facades.CPQImageType;

import java.util.Map;


public class CPQImageFormatMappingImpl implements CPQImageFormatMapping
{
	private Map<String, CPQImageType> mapping;

	public void setMapping(final Map<String, CPQImageType> mapping)
	{
		this.mapping = mapping;
	}

	@Override
	public Map<String, CPQImageType> getCPQMediaFormatQualifiers()
	{
		return mapping;
	}

}
