package com.hybris.datahub.core.services.impl;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.datahub.client.extension.ExtensionClient;
import com.hybris.datahub.client.extension.ExtensionSource;


@SuppressWarnings("all")
@RunWith(MockitoJUnitRunner.class)
public class DefaultDataHubExtensionUploadServiceUnitTest
{
	@Mock
	private ExtensionClient client;
	@Mock
	private ExtensionSource extension;

	private DefaultDataHubExtensionUploadService service;

	@Before
	public void setUp()
	{
		service = createService(client);
	}

	@Test(expected = IllegalStateException.class)
	public void testClientStateBeforeUrlSet()
	{
		new DefaultDataHubExtensionUploadService().client();
	}

	@Test
	public void testClientStateAfterPropertiesSet() throws Exception
	{
		final DefaultDataHubExtensionUploadService service = new DefaultDataHubExtensionUploadService();
		service.afterPropertiesSet();

		Assert.assertNotNull(service.client());
	}

	@Test
	public void testDelegatesExtensionUploadToTheClient() throws IOException
	{
		service.setDataHubUrl("");

		service.uploadExtension(extension);

		verify(client).deployExtension(extension);
	}

	@Test(expected = IOException.class)
	public void testPropagatesExceptionsFromTheClient() throws IOException
	{
		doThrow(new IOException()).when(client).deployExtension(extension);

		service.uploadExtension(extension);
	}

	private DefaultDataHubExtensionUploadService createService(final ExtensionClient c)
	{
		final DefaultDataHubExtensionUploadService s = new DefaultDataHubExtensionUploadService()
		{
			@Override
			protected ExtensionClient createClient(final String url)
			{
				return c;
			}
		};
		try
		{
			s.afterPropertiesSet(); // initializes client
		}
		catch (Exception e)
		{
			//DO nothing
		}
		return s;
	}

}
