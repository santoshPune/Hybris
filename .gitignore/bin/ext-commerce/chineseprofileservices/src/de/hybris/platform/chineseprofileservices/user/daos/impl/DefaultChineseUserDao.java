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
package de.hybris.platform.chineseprofileservices.user.daos.impl;


import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.user.daos.UserDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultChineseUserDao extends DefaultGenericDao<UserModel> implements UserDao
{

	private static final String UID = "uid";
	private static final String MOB_NUM = "mobileNumber";
	private static final String FIND_USER_BY_UID = "SELECT {" + UserModel.PK + "} FROM {" + UserModel._TYPECODE + "} WHERE {"
			+ UserModel.UID + "}=?" + UID;
	private static final String FIND_USER_BY_MOBILE = "SELECT {" + CustomerModel.PK + "} FROM {" + CustomerModel._TYPECODE
			+ "} WHERE {" + CustomerModel.MOBILENUMBER + "}=?" + MOB_NUM;

	public DefaultChineseUserDao()
	{
		super(UserModel._TYPECODE);
	}

	@Override
	public UserModel findUserByUID(String loginName) throws ModelNotFoundException
	{
		final FlexibleSearchQuery query;
		if (loginName.contains("@"))
		{
			query = new FlexibleSearchQuery(FIND_USER_BY_UID);
			query.addQueryParameter(UID, loginName);
		}
		else
		{
			query = new FlexibleSearchQuery(FIND_USER_BY_MOBILE);
			query.addQueryParameter(MOB_NUM, loginName);
		}

		return (UserModel) getFlexibleSearchService().searchUnique(query);
	}

}
