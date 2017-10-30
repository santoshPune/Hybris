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

package com.hybris.datahub.core.facades;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.MediaUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mockito.Mockito;


/**
 * Utilities to help with testing.
 */
public class ImportTestUtils
{

	/**
	 * Simulates an import error with the specified message.
	 *
	 * @param msg an error message for the error.
	 * @return an import error
	 */
	public static ImportError error(final String msg)
	{
		final ImportError err = Mockito.mock(ImportError.class);
		Mockito.doReturn(msg).when(err).getMessage();
		return err;
	}

	/**
	 * Simulates an import error with the specified error code and the message.
	 *
	 * @param code an error code to use for the error.
	 * @param msg an error message for the error.
	 * @return an import error
	 */
	public static ImportError error(final ErrorCode code, final String msg)
	{
		final ImportError err = error(msg);
		Mockito.doReturn(code).when(err).getCode();
		return err;
	}

	/**
	 * Simulates an import error for each message submitted.
	 *
	 * @param messages an array of messages to convert to import errors
	 * @return a collection of import errors with the specified messages.
	 */
	public static Collection<ImportError> errors(final String... messages)
	{
		final List<ImportError> errors = new ArrayList<>(messages.length);
		for (final String msg : messages)
		{
			errors.add(error(msg));
		}
		return errors;
	}

	/**
	 * Mocks an import result, which is wired up with the CronJobModel and WorkMedia. The result is neither successful
	 * nor erroneous and should be further mocked for that purpose.
	 *
	 * @param impexFileLoc path to the source of the impex file being imported, which is relative to the hybris server data
	 * directory.
	 * @return the mocked result.
	 */
	public static ImportResult importResult(final String impexFileLoc)
	{
		final ImpExMediaModel media = Mockito.mock(ImpExMediaModel.class);
		Mockito.doReturn(impexFileLoc).when(media).getLocation();
		Mockito.doReturn(MediaUtil.URL_HAS_DATA).when(media).getInternalURL();

		final ItemModelContext itemModelContext = Mockito.mock(ItemModelContext.class);
		Mockito.doReturn(itemModelContext).when(media).getItemModelContext();

		final ImpExImportCronJobModel job = Mockito.mock(ImpExImportCronJobModel.class);
		Mockito.doReturn(media).when(job).getWorkMedia();

		final ImportResult res = Mockito.mock(ImportResult.class);
		Mockito.doReturn(job).when(res).getCronJob();
		Mockito.doReturn(media).when(res).getUnresolvedLines();

		final String tenantId = "master";
		Mockito.doReturn(tenantId).when(job).getTenantId();

		return res;
	}

	/**
	 * Mocks a successful import result.
	 *
	 * @return the mocked result.
	 */
	public static ImportResult successfulImportResult()
	{
		final ImportResult res = importResult(null);
		makeResultSuccessful(res);
		return res;
	}

	/**
	 * Takes mock of an <code>ImportResult</code> and finishes stubbing to simulate success import result.
	 *
	 * @param result a result mock to stub.
	 */
	public static void makeResultSuccessful(final ImportResult result)
	{
		Mockito.doReturn(true).when(result).isSuccessful();
		Mockito.doReturn(false).when(result).isError();
		Mockito.doReturn(false).when(result).hasUnresolvedLines();
	}

	/**
	 * Mocks an import result with errors present in the error log.
	 *
	 * @param impexFileLoc location of the impex source file being imported.
	 * @param errLogText content of the error log to mock in the result.
	 * @return the mocked result
	 */
	public static ImportResult importResultWithLogErrors(final String impexFileLoc, final String errLogText)
	{
		final ImportResult res = importResult(impexFileLoc);
		makeResultWithErrors(res, errLogText, null, null);
		return res;
	}

	/**
	 * Mocks an import result with errors present in the unresolved lines. Whenever reference violations are reported in the
	 * import result, the error log contains a generic error about reference violation. Therefore the error log content should
	 * passed too.
	 *
	 * @param impexFileLoc location of the impex source file being imported.
	 * @param errLogText content of the error log to mock in the result.
	 * @param mediaStream content of the unresolved lines to mock in the result.
	 * @return the mocked result
	 */
	public static ImportResult importResultWithUnresolvedLineErrors(final String impexFileLoc, final String errLogText,
			final InputStream mediaStream)
	{
		final ImportResult res = importResult(impexFileLoc);
		makeResultWithErrors(res, errLogText, null, mediaStream);
		return res;
	}

	/**
	 * Takes mock of an <code>ImportResult</code> and finishes stubbing to simulate errors present in the result.
	 *
	 * @param res a mock of the <code>ImportResult</code> to finish stubbing.
	 * @param errLogText content of the error log to mock in the import result.
	 */
	public static void makeResultWithErrors(final ImportResult res, final String errLogText)
	{
		makeResultWithErrors(res, errLogText, null, null);
	}

	/**
	 * Takes mock of an <code>ImportResult</code> and finishes stubbing to simulate errors present in the result.
	 *
	 * @param res a mock of the <code>ImportResult</code> to finish stubbing.
	 * @param errLogText content of the error log to mock in the import result.
	 * @param mediaService the mediaService
	 * @param mediaStream c
	 * should be simulated.
	 */
	public static void makeResultWithErrors(final ImportResult res, final String errLogText, final MediaService mediaService, final InputStream mediaStream)
	{
		Mockito.doReturn(false).when(res).isSuccessful();
		Mockito.doReturn(true).when(res).isError();
		Mockito.doReturn(mediaStream != null).when(res).hasUnresolvedLines();

		final ImpExImportCronJobModel job = res.getCronJob();
		Mockito.doReturn(errLogText).when(job).getLogText();

		final ImpExMediaModel media = job.getWorkMedia();
		Mockito.doReturn(media).when(res).getUnresolvedLines();

		if (mediaService != null)
		{
			Mockito.doReturn(mediaStream).when(mediaService).getStreamFromMedia(media);
		}
	}

	/**
	 * Takes mock of an <code>ImportResult</code> and throws an IOException when calling the mediaService
	 *
	 * @param res a mock of the <code>ImportResult</code> to finish stubbing.
	 * @param mediaService a mock of the <code>ImportResult</code> to finish stubbing.
	 */
	public static void makeResultWithMediaServiceException(final ImportResult res, final MediaService mediaService)
	{
		Mockito.doReturn(false).when(res).isSuccessful();
		Mockito.doReturn(true).when(res).isError();
		Mockito.doReturn(true).when(res).hasUnresolvedLines();

		final ImpExImportCronJobModel job = res.getCronJob();

		final ImpExMediaModel media = job.getWorkMedia();
		Mockito.doReturn(media).when(res).getUnresolvedLines();

		Mockito.doThrow(IOException.class).when(mediaService).getStreamFromMedia(media);
	}

	/**
	 * Converts separate strings into a multi-line text, where each string becomes a separate line.
	 *
	 * @param lines lines of text to merge together. Pass empty strings for empty (blank) lines in the resulting text.
	 * @return text consisting of the lines combined together.
	 */
	public static String toText(final String... lines)
	{
		return StringUtils.join(lines, System.lineSeparator());
	}

	public static InputStream toInputStream(final String... lines)
	{
		final String test = toText(lines);
		return IOUtils.toInputStream(test);
	}
}
