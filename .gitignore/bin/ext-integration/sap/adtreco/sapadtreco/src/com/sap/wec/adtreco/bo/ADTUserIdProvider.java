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
package com.sap.wec.adtreco.bo;

import de.hybris.platform.core.model.user.UserModel;


/**
 *
 */
public class ADTUserIdProvider
{

	/**
	 * Get session user ID based on UserModel
	 * @param user
	 * @return String
	 */
	public String getUserId(final UserModel user)
	{
		final String userId = user.getUid();
		if (userId != null && !userId.isEmpty() && !userId.equals("anonymous"))
		{
			//if user is logged in, return his log on ID
			return userId;
		}
		
		//if user is not logged id and no piwik ID is provided
		return null;
	}	
	
}
