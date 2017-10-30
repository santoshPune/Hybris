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
package com.sap.hybris.reco.common.util;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 */
public class UserIdProvider
{
	/**
	 * Get the session user ID. When no user is logged on, the UID "anonymous" is returned.  
	 * @param user
	 * @return userId
	 */
	public String getUserId(final UserModel user)
	{
		String userId = "";
		if (user != null)
		{
			if (user instanceof CustomerModel)
			{
				userId = ((CustomerModel) user).getUid();
			}
			
			if (userId == null)
			{
				userId = user.getUid();
			}
		}

		if (!userId.isEmpty())
		{
			return userId;
		}
		return null;
	}
}
