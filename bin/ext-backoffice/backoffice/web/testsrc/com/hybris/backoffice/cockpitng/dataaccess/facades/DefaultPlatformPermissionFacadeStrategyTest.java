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
package com.hybris.backoffice.cockpitng.dataaccess.facades;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.DefaultPlatformPermissionFacadeStrategy;
import com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom.InstancePermissionAdvisor;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;


public class DefaultPlatformPermissionFacadeStrategyTest
{
	private static final String ENGLISH_ISO_CODE = "en";
	private static final String GERMAN_ISO_CODE = "de";
	private final transient Set<Locale> readableLanguages = new HashSet<Locale>();
	@InjectMocks
	private final transient DefaultPlatformPermissionFacadeStrategy permissionFacade = new DefaultPlatformPermissionFacadeStrategy()
	{
		@Override
		public Set<Locale> getAllReadableLocalesForCurrentUser()
		{
			final Locale english = new Locale(ENGLISH_ISO_CODE);
			readableLanguages.add(english);
			return readableLanguages;
		}

		@Override
		public Set<Locale> getAllWritableLocalesForCurrentUser()
		{
			final Locale english = new Locale(ENGLISH_ISO_CODE);
			readableLanguages.add(english);
			return readableLanguages;
		}
	};
	@Mock
	private transient CatalogTypeService catalogTypeService;
	@Mock
	private transient CommonI18NService commonI18NService;
	@Mock
	private UserService userService;
	@Mock
	private UserModel user;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private TypeService typeService;
	@Mock
	private InstancePermissionAdvisor permissionAdvisor;

	private List<InstancePermissionAdvisor> permissionAdvisors;
	private final transient LanguageModel userProfileLanEnglish = new LanguageModel();
	private transient CatalogVersionModel catalog;
	private transient ProductModel product;
	private transient Locale englishLocale;
	private transient Locale germanLocale;

	private static final String TYPE = "TYPE";

	@Before
	public void setUp()
	{
		initMocks(this);

		permissionAdvisors = new ArrayList<>();
		permissionFacade.setPermissionAdvisors(permissionAdvisors);

		catalog = new CatalogVersionModel();

		final LanguageModel catalogVersionLanEnglish = new LanguageModel();

		catalogVersionLanEnglish.setIsocode(ENGLISH_ISO_CODE);

		final LanguageModel catalogVersionLanGerman = new LanguageModel();
		catalogVersionLanGerman.setIsocode(GERMAN_ISO_CODE);

		final Collection<LanguageModel> allLang = new ArrayList<LanguageModel>();
		allLang.add(catalogVersionLanEnglish);
		allLang.add(catalogVersionLanGerman);

		catalog.setLanguages(allLang);

		product = new ProductModel();
		product.setCatalogVersion(catalog);

		when(Boolean.valueOf(catalogTypeService.isCatalogVersionAwareModel(product))).thenReturn(Boolean.TRUE);
		when(catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(product)).thenReturn(catalog);
		when(userService.getCurrentUser()).thenReturn(user);
		when(Boolean.valueOf(userService.isAdmin(user))).thenReturn(Boolean.FALSE);

		englishLocale = new Locale(ENGLISH_ISO_CODE);
		germanLocale = new Locale(GERMAN_ISO_CODE);

		when(commonI18NService.getLocaleForLanguage(userProfileLanEnglish)).thenReturn(englishLocale);
		when(commonI18NService.getLocaleForLanguage(catalogVersionLanEnglish)).thenReturn(englishLocale);
		when(commonI18NService.getLocaleForLanguage(catalogVersionLanGerman)).thenReturn(germanLocale);
	}

	@Test
	public void testGetReadableLocalesForInstance()
	{
		final Set<Locale> expectedLocales = permissionFacade.getReadableLocalesForInstance(product);
		Assert.assertNotNull(expectedLocales);
		Assert.assertTrue(expectedLocales.contains(englishLocale));
	}

	@Test
	public void testGetWritableLocalesForInstance()
	{
		final Set<Locale> expectedLocales = permissionFacade.getWritableLocalesForInstance(product);
		Assert.assertNotNull(expectedLocales);
		Assert.assertTrue(expectedLocales.contains(englishLocale));
	}

	@Test
	public void testCatalogVersionAndReadableLanguageAreNull()
	{
		final DefaultPlatformPermissionFacadeStrategy permissionFacade = new DefaultPlatformPermissionFacadeStrategy()
		{
			@Override
			public Set<Locale> getAllReadableLocalesForCurrentUser()
			{
				return null;
			}

			@Override
			public Set<Locale> getAllWritableLocalesForCurrentUser()
			{
				return null;
			}
		};

		when(Boolean.valueOf(catalogTypeService.isCatalogVersionAwareModel(product))).thenReturn(Boolean.FALSE);
		permissionFacade.setCatalogTypeService(catalogTypeService);

		final Set<Locale> expectedReadableLocales = permissionFacade.getReadableLocalesForInstance(product);
		Assert.assertTrue(CollectionUtils.isEmpty(expectedReadableLocales));

		final Set<Locale> expectedWritableLocales = permissionFacade.getReadableLocalesForInstance(product);
		Assert.assertTrue(CollectionUtils.isEmpty(expectedWritableLocales));

	}

	@Test
	public void testCatalogVersionLanguagesNotNullAndAllLanguageIsNull()
	{
		final DefaultPlatformPermissionFacadeStrategy permissionFacade = new DefaultPlatformPermissionFacadeStrategy()
		{
			@Override
			public Set<Locale> getAllReadableLocalesForCurrentUser()
			{
				return null;
			}

			@Override
			public Set<Locale> getAllWritableLocalesForCurrentUser()
			{
				return null;
			}

			@Override
			protected Set<Locale> getLocalesForLanguage(final Collection<LanguageModel> languages)
			{
				final Set<Locale> localesForLanguage = new HashSet<Locale>();
				localesForLanguage.add(englishLocale);
				localesForLanguage.add(germanLocale);

				return localesForLanguage;
			}
		};

		permissionFacade.setCatalogTypeService(catalogTypeService);
		permissionFacade.setCommonI18NService(commonI18NService);

		final Set<Locale> expectedReadableLocales = permissionFacade.getReadableLocalesForInstance(product);
		Assert.assertNotNull(expectedReadableLocales);
		Assert.assertTrue(expectedReadableLocales.size() == 2);

		final Set<Locale> expectedWritableLocales = permissionFacade.getWritableLocalesForInstance(product);
		Assert.assertNotNull(expectedWritableLocales);
		Assert.assertTrue(expectedWritableLocales.size() == 2);
	}

	@Test
	public void testCatalogVersionLanguagesNullAndAllLanguageNotNull()
	{
		when(Boolean.valueOf(catalogTypeService.isCatalogVersionAwareModel(product))).thenReturn(Boolean.FALSE);

		final Set<Locale> expectedReadableLocales = permissionFacade.getReadableLocalesForInstance(product);
		Assert.assertNotNull(expectedReadableLocales);
		Assert.assertTrue(expectedReadableLocales.size() == 1);

		final Set<Locale> expectedWritableLocales = permissionFacade.getWritableLocalesForInstance(product);
		Assert.assertNotNull(expectedWritableLocales);
		Assert.assertTrue(expectedWritableLocales.size() == 1);
	}

	@Test
	public void testGetReadableLocalesForInstanceAsAdmin()
	{
		final Set<Locale> locales = new HashSet<Locale>();
		locales.add(Locale.ENGLISH);
		locales.add(Locale.GERMAN);
		locales.add(Locale.JAPAN);

		final DefaultPlatformPermissionFacadeStrategy permissionFacade = new DefaultPlatformPermissionFacadeStrategy()
		{
			@Override
			public Set<Locale> getAllReadableLocalesForCurrentUser()
			{
				return locales;
			}

			@Override
			public Set<Locale> getAllWritableLocalesForCurrentUser()
			{
				return locales;
			}
		};

		permissionFacade.setCatalogTypeService(catalogTypeService);
		permissionFacade.setCommonI18NService(commonI18NService);
		permissionFacade.setUserService(userService);

		when(Boolean.valueOf(userService.isAdmin(user))).thenReturn(Boolean.TRUE);

		final LanguageModel german = new LanguageModel();
		german.setIsocode(GERMAN_ISO_CODE);
		catalog.setLanguages(Collections.singletonList(german));

		final Set<Locale> readableLocalesForInstance = permissionFacade.getReadableLocalesForInstance(product);

		// Even though catalog is restricted for geramn language only - in case of admin, facade returns all readable
		// languages of the admin user - all defined. In this test - English
		Assert.assertTrue(readableLocalesForInstance.containsAll(locales));
	}

	@Test
	public void testCanRemoveInstanceWhenAdvisorListIsEmpty()
	{
		// given
		final Object objectToRemove = new Object();
		when(typeFacade.getType(objectToRemove)).thenReturn(TYPE);

		// when
		final boolean result = permissionFacade.canRemoveInstance(objectToRemove);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void testCanRemoveInstanceWhenAdvisorIsApplicableAndInstanceCanBeRemoved()
	{
		// given
		final Object objectToRemove = new Object();
		when(typeFacade.getType(objectToRemove)).thenReturn(TYPE);
		permissionAdvisors.add(permissionAdvisor);
		when(Boolean.valueOf(permissionAdvisor.isApplicableTo(objectToRemove))).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(permissionAdvisor.canDelete(objectToRemove))).thenReturn(Boolean.TRUE);

		// when
		final boolean result = permissionFacade.canRemoveInstance(objectToRemove);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void testCanRemoveInstanceWhenAdvisorIsNotApplicable()
	{
		// given
		final Object objectToRemove = new Object();
		when(typeFacade.getType(objectToRemove)).thenReturn(TYPE);
		permissionAdvisors.add(permissionAdvisor);
		when(Boolean.valueOf(permissionAdvisor.isApplicableTo(objectToRemove))).thenReturn(Boolean.FALSE);

		// when
		final boolean result = permissionFacade.canRemoveInstance(objectToRemove);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void testCanRemoveInstanceWhenAdvisorIsApplicableAndInstanceCannotBeRemoved()
	{
		// given
		final Object objectToRemove = new Object();
		when(typeFacade.getType(objectToRemove)).thenReturn(TYPE);
		permissionAdvisors.add(permissionAdvisor);
		when(Boolean.valueOf(permissionAdvisor.isApplicableTo(objectToRemove))).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(permissionAdvisor.canDelete(objectToRemove))).thenReturn(Boolean.FALSE);

		// when
		final boolean result = permissionFacade.canRemoveInstance(objectToRemove);

		// then
		assertThat(result).isFalse();
	}
}
