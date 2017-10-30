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
package de.hybris.platform.chineseprofileservices.mobile;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofileservices.security.ChineseUserDetailsService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.spring.security.CoreUserDetails;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@UnitTest
public class ChineseUserDetailsServiceTest
{
	private ChineseUserDetailsService chineseUserDetailsService;
	private CustomerModel user;

	@Mock
	private UserDao userDao;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private UserGroupModel userGroup;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		chineseUserDetailsService = new ChineseUserDetailsService();
		user = new CustomerModel();
		user.setLoginDisabled(false);
		user.setUid("test@gmail.com");
		user.setMobileNumber("12345678910");
		user.setEncodedPassword("123456123456");

		Mockito.when(userGroup.getAllGroups()).thenReturn(new HashSet<PrincipalGroupModel>());
		Mockito.when(userGroup.getUid()).thenReturn("customergroup");
		Set<PrincipalGroupModel> groups = new HashSet<>();
		groups.add(userGroup);
		user.setGroups(groups);

		LanguageModel language = new LanguageModel();
		language.setIsocode("en");

		chineseUserDetailsService.setCommonI18NService(commonI18NService);
		chineseUserDetailsService.setUserDao(userDao);
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(language);

	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_Load_Exists_User()
	{
		Mockito.when(userDao.findUserByUID(Mockito.any())).thenReturn(user);
		CoreUserDetails userDetails = chineseUserDetailsService.loadUserByUsername("12345678910");

		assertEquals("123456123456", userDetails.getPassword());
		assertEquals("test@gmail.com", userDetails.getUsername());
		assertEquals("en", userDetails.getLanguageISO());
		assertEquals(new SimpleGrantedAuthority("ROLE_CUSTOMERGROUP"), userDetails.getAuthorities().toArray()[0]);

	}

	@Test
	public void test_Load_Unkown_User()
	{
		Mockito.when(userDao.findUserByUID(Mockito.any())).thenThrow(new ModelNotFoundException("Didn't find match user"));
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("User not found!");

		chineseUserDetailsService.loadUserByUsername("111111111");

	}
}
