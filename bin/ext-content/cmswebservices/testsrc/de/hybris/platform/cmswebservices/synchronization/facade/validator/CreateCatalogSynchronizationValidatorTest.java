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
package de.hybris.platform.cmswebservices.synchronization.facade.validator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.SyncJobRequestData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateCatalogSynchronizationValidatorTest
{

	private static final String SOURCE = "source";
	private static final String TARGET = "target";
	private static final String CATALOG = "catalog";

	@Mock
	private UserService userService;

	@Mock
	private CatalogVersionService catalogVersionService;

	@InjectMocks
	private CreateCatalogSynchronizationValidator createCatalogSynchronizationValidator;

	private SyncJobRequestData syncJobRequestData;
	private CatalogVersionModel sourceCatalog;
	private CatalogVersionModel targetCatalog;
	private UserModel userModel;

	@Before
	public void setup()
	{
		userModel = mock(UserModel.class);
		when(userService.getCurrentUser()).thenReturn(userModel);

		sourceCatalog = mock(CatalogVersionModel.class);
		targetCatalog = mock(CatalogVersionModel.class);

		when(catalogVersionService.getCatalogVersion(CATALOG, SOURCE)).thenReturn(sourceCatalog);
		when(catalogVersionService.getCatalogVersion(CATALOG, TARGET)).thenReturn(targetCatalog);

		syncJobRequestData = new SyncJobRequestData();
		syncJobRequestData.setCatalogId(CATALOG);
		syncJobRequestData.setSourceVersionId(SOURCE);
		syncJobRequestData.setTargetVersionId(TARGET);
	}

	@Test
	public void testValidateSuccess()
	{
		when(catalogVersionService.canRead(sourceCatalog, userModel)).thenReturn(true);
		when(catalogVersionService.canWrite(targetCatalog, userModel)).thenReturn(true);

		final Errors errors = createErrors();
		createCatalogSynchronizationValidator.validate(syncJobRequestData, errors);

		Assert.assertTrue("Class is not supported.", createCatalogSynchronizationValidator.supports(syncJobRequestData.getClass()));
		Assert.assertFalse("There should be no validation errors.", errors.hasErrors());
	}


	@Test
	public void testValidateCannotRead()
	{
		when(catalogVersionService.canRead(sourceCatalog, userModel)).thenReturn(false);
		when(catalogVersionService.canWrite(targetCatalog, userModel)).thenReturn(true);

		final Errors errors = createErrors();
		createCatalogSynchronizationValidator.validate(syncJobRequestData, errors);

		Assert.assertTrue("Class is not supported.", createCatalogSynchronizationValidator.supports(syncJobRequestData.getClass()));
		Assert.assertTrue("There should be validation errors.", errors.hasErrors());
		Assert.assertEquals("The user cannot read.", 1, errors.getFieldErrorCount());
		Assert.assertEquals("The user cannot read.", "sourceVersionId", errors.getFieldErrors().get(0).getField());
	}

	@Test
	public void testValidateCannotWrite()
	{
		when(catalogVersionService.canRead(sourceCatalog, userModel)).thenReturn(true);
		when(catalogVersionService.canWrite(targetCatalog, userModel)).thenReturn(false);

		final Errors errors = createErrors();
		createCatalogSynchronizationValidator.validate(syncJobRequestData, errors);

		Assert.assertTrue("Class is not supported.", createCatalogSynchronizationValidator.supports(syncJobRequestData.getClass()));
		Assert.assertTrue("There should be validation errors.", errors.hasErrors());
		Assert.assertEquals("The user cannot read.", 1, errors.getFieldErrorCount());
		Assert.assertEquals("The user cannot read.", "targetVersionId", errors.getFieldErrors().get(0).getField());
	}

	private Errors createErrors()
	{
		return new BeanPropertyBindingResult(syncJobRequestData, syncJobRequestData.getClass().getSimpleName());
	}
}
