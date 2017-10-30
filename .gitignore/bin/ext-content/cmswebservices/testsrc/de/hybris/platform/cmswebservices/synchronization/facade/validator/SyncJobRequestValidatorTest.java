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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.SyncJobRequestData;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;



@UnitTest
@RunWith(Parameterized.class)
public class SyncJobRequestValidatorTest
{
	private static final String BAD_CATALOG = null;
	private static final String BAD_SOURCE_VERSION = null;
	private static final String BAD_TARGET_VERSION = null;

	private static final String GOOD_CATALOG = "electronic";
	private static final String GOOD_SOURCE_VERSION = "staged";
	private static final String GOOD_TARGET_VERSION = "online";
	private static final Boolean ACCEPTABLE = Boolean.FALSE;
	private static final Boolean NOTACCEPTABLE = Boolean.TRUE;

	private final String testCatalog;
	private final String testSourceVersion;
	private final String testTargetVersion;
	private final Boolean expected;

	@InjectMocks
	@Spy
	private SyncJobRequestValidator validator;

	private SyncJobRequestData target;
	private Errors errors;


	@Parameters(name = "{index}: validate(Catalog: {0}, SourceVersion: {1}, TargertVersion: {2}) => Expected:{3}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(new Object[][]
				{
				{ GOOD_CATALOG, GOOD_SOURCE_VERSION, BAD_TARGET_VERSION, NOTACCEPTABLE },
				{ GOOD_CATALOG, BAD_SOURCE_VERSION, GOOD_TARGET_VERSION, NOTACCEPTABLE },
				{ BAD_CATALOG, GOOD_SOURCE_VERSION, GOOD_TARGET_VERSION, NOTACCEPTABLE },
				{ GOOD_CATALOG, GOOD_SOURCE_VERSION, GOOD_TARGET_VERSION, ACCEPTABLE } });
	}



	public SyncJobRequestValidatorTest(final String testCatalog, final String testSourceVersion, final String testTargetVersion,
			final Boolean expected)
	{
		this.testCatalog = testCatalog;
		this.testSourceVersion = testSourceVersion;
		this.testTargetVersion = testTargetVersion;
		this.expected = expected;
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		target = new SyncJobRequestData();
		errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
		Mockito.doReturn(isFindable(testTargetVersion)).when(validator).isFindableCatalogByIdVersion(testCatalog, testTargetVersion);
		Mockito.doReturn(isFindable(testSourceVersion)).when(validator).isFindableCatalogByIdVersion(testCatalog, testSourceVersion);
		Mockito.doReturn(isFindable(testCatalog)).when(validator).isFindableCatalogById(testCatalog);
	}



	@Test
	public void shouldTestValidation()
	{

		target.setCatalogId(testCatalog);
		target.setSourceVersionId(testSourceVersion);
		target.setTargetVersionId(testTargetVersion);

		validator.validate(target, errors);
		assertEquals(errors.toString(), errors.hasErrors(), expected);

	}



	private Boolean isFindable(final String object)
	{
		if (object == null)
		{
			return Boolean.FALSE;
		}
		return (!object.isEmpty());
	}
}
