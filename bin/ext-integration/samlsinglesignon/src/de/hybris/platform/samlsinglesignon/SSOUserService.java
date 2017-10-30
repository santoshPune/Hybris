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
package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


/**
 * SSO service interface for getting/creating user
 */
public interface SSOUserService
{
	/**
	 * @throws IllegalArgumentException
	 *            in case the user cannot be mapped due to roles being unknown or disallowed
	 * @param id
	 *           the user id
	 * @param name
	 * 			the user name
	 * @param roles
	 *           user roles
	 * @return existing or newly created user model
	 */
	UserModel getOrCreateSSOUser(String id, String name, Collection<String> roles);
}
