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
package de.hybris.platform.chineseprofileservices.security;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.spring.security.CoreUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ChineseUserDetailsService implements UserDetailsService
{

	private String rolePrefix = "ROLE_";

	private UserDao userDao;

	private CommonI18NService commonI18NService;

	@Override
	public CoreUserDetails loadUserByUsername(String username)
	{
		if (username == null)
		{
			return null;
		}

		UserModel user;
		try
		{
			user = userDao.findUserByUID(username);
		}
		catch (ModelNotFoundException e)
		{
			throw new UsernameNotFoundException("User not found!");
		}

		boolean enabled = !user.isLoginDisabled();

		// TODO dummy values, will be delivered by the platform
		final boolean accountNonExpired = true;
		final boolean credentialsNonExpired = true;
		final boolean accountNonLocked = true;

		String password = user.getEncodedPassword();

		// a null value has to be transformed to empty string, otherwise the constructor
		// of org.springframework.security.userdetails.User will fail
		if (password == null)
		{
			password = "";
		}

		final CoreUserDetails userDetails = new CoreUserDetails(user.getUid(), password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, getAuthorities(user),
				getCommonI18NService().getCurrentLanguage().getIsocode());
		return userDetails;
	}

	private Collection<GrantedAuthority> getAuthorities(UserModel user)
	{
		final Set<PrincipalGroupModel> groups = user.getGroups();
		final Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.size());
		final Iterator<PrincipalGroupModel> itr = groups.iterator();
		while (itr.hasNext())
		{
			final PrincipalGroupModel group = itr.next();
			authorities.add(new SimpleGrantedAuthority(rolePrefix + group.getUid().toUpperCase()));
			for (final PrincipalGroupModel gr : group.getAllGroups())
			{
				authorities.add(new SimpleGrantedAuthority(rolePrefix + gr.getUid().toUpperCase()));
			}

		}
		return authorities;
	}

	@Required
	public void setUserDao(UserDao UserDao)
	{
		this.userDao = UserDao;
	}

	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public void setRolePrefix(String rolePrefix)
	{
		this.rolePrefix = rolePrefix;
	}

	public UserDao getUserDao()
	{
		return userDao;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

}
