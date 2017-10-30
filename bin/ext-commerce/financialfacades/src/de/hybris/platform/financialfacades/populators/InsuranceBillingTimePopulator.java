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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.subscriptionfacades.converters.populator.BillingTimePopulator;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingEventModel;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;


/**
 * InsuranceBillingTimePopulator
 */
public class InsuranceBillingTimePopulator<SOURCE extends BillingTimeModel, TARGET extends BillingTimeData>
		extends BillingTimePopulator<SOURCE, TARGET>
{
	public InsuranceBillingTimePopulator()
	{
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);
		if (source instanceof BillingEventModel)
		{
			target.setHelpContent(((BillingEventModel) source).getHelpContent());
		}
	}
}
