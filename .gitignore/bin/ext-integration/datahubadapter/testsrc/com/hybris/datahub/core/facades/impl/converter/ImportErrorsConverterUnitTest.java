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
 */

package com.hybris.datahub.core.facades.impl.converter;

import static com.hybris.datahub.core.facades.ImportTestUtils.successfulImportResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.impex.ImpExHeaderError;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;

import com.hybris.datahub.core.facades.ItemImportResult;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests how converter builds the import result and handles the import errors. The error parsing of the error log and
 * the unresolved lines reported in the import result is simulated for more comprehensive case coverage.
 */
@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ImportErrorsConverterUnitTest
{
	private final int ERROR_LIMIT = 1;

	@InjectMocks
	private final ImportErrorsConverter converter = new ImportErrorsConverter();
	@Mock
	private ImportService importService;

	@Before
	public void setup()
	{
		converter.setErrorLimit(ERROR_LIMIT);
	}

	@Test
	public void testConvertsNullImportResultToNullItemsImportResult()
	{
		final ItemImportResult result = converter.convert(null);
		assertNull(result);
	}

	@Test
	public void testConvertsSuccessfulImportResult()
	{
		final ItemImportResult result = converter.convert(successfulImportResult());

		assertNotNull(result);
		assertTrue(result.getErrors().isEmpty());
	}

	@Test
	public void testDoesNotAttemptToCollectErrorsWhenResultIsSuccessful() throws IOException, ImpExException
	{
		converter.convert(successfulImportResult());

		verify(importService, never()).collectImportErrors(any(ImportResult.class));
	}

	@Test
	public void testConvertedResultIsErrorWhenImportServiceReturnsErrors()
	{
		final ItemImportResult result = converter.convert(importResultWithErrors());

		verify(importService).collectImportErrors(any(ImportResult.class));
		assertFalse(result.isSuccessful());
		assertEquals(result.getErrors().size(), 1);
	}

	private ImportResult importResultWithErrors()
	{
		final ImportResult importResult = mock(ImportResult.class);
		doReturn(true).when(importResult).isError();
		doReturn(Stream.of(mock(ImpExHeaderError.class))).when(importService).collectImportErrors(importResult);
		return importResult;
	}

	@Test(expected = ErrorLimitExceededException.class)
	public void testThrowsExceptionWhenErrorLimitIsExceeded()
	{
		converter.convert(importResultWithErrorsExceedingLimit());
	}

	private ImportResult importResultWithErrorsExceedingLimit()
	{
		final ImportResult importResult = mock(ImportResult.class);
		doReturn(true).when(importResult).isError();
		doReturn(Stream.of(mock(ImpExHeaderError.class), mock(ImpExHeaderError.class))).when(importService).collectImportErrors(importResult);
		return importResult;
	}
}
