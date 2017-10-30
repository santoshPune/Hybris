/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.model.cronjob;

import de.hybris.platform.sap.productconfig.model.constants.SapproductconfigmodelConstants;
import de.hybris.platform.util.Config;


/**
 * Accessing properties for SSC DB attributes
 */
public class DefaultPropertyAccessFacade implements PropertyAccessFacade
{

	@Override
	public boolean getStartDeltaloadAfterInitial()
	{
		return Config.getBoolean(SapproductconfigmodelConstants.START_DELTA_LOAD_AFTER_INITIAL, true);
	}

}
