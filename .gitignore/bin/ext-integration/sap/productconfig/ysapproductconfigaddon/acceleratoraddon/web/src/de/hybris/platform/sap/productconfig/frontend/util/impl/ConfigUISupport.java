package de.hybris.platform.sap.productconfig.frontend.util.impl;

import de.hybris.platform.sap.productconfig.facades.CsticData;

import java.util.List;


@SuppressWarnings("squid:S1118")
public class ConfigUISupport
{
	public static boolean hasRequiredCstic(final List<CsticData> cstics)
	{
		if (cstics == null)
		{
			return false;
		}

		return cstics.parallelStream().anyMatch(cstic -> cstic.isRequired());
	}
}
