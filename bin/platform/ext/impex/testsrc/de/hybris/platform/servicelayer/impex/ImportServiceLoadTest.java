/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.servicelayer.impex;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;


@IntegrationTest
public class ImportServiceLoadTest extends ServicelayerBaseTest
{
	@Test
	public void multipleImportsShouldImportNotEmptyScript() throws InterruptedException
	{
		testMultipleImports("INSERT_UPDATE Title;code[unique=true]\n;T");
	}

	@Test
	public void multipleImportsShouldImportEmptyScript() throws InterruptedException
	{
		testMultipleImports("###################");
	}

	public void testMultipleImports(final String script) throws InterruptedException
	{
		final TestThread[] threads = new TestThread[64];


		final TestThread warmUpThread = new TestThread(null, Registry.getCurrentTenant().getTenantID(),
				script + UUID.randomUUID().toString(), 5);
		warmUpThread.run();
		assertThat(warmUpThread.result).isNotNull();
		assertThat(warmUpThread.result.isFinished()).isTrue();
		assertThat(warmUpThread.result.isSuccessful()).isTrue();

		final CountDownLatch startLatch = new CountDownLatch(1);

		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new TestThread(startLatch, Registry.getCurrentTenant().getTenantID(), script + UUID.randomUUID().toString(),
					7);
			threads[i].start();
		}

		startLatch.countDown();

		for (int i = 0; i < threads.length; i++)
		{
			final TestThread t = threads[i];
			t.join((threads.length - i) * 100 + 10000);
			assertThat(t.isAlive()).isFalse();
			assertThat(t.result).isNotNull();
			assertThat(t.result.isFinished()).isTrue();
			assertThat(t.result.isSuccessful()).isTrue();
		}

		final TestThread coolDownThread = new TestThread(null, Registry.getCurrentTenant().getTenantID(),
				script + UUID.randomUUID().toString(), 10);
		coolDownThread.run();
		assertThat(coolDownThread.result).isNotNull();
		assertThat(coolDownThread.result.isFinished()).isTrue();
		assertThat(coolDownThread.result.isSuccessful()).isTrue();

		System.out.println("Number of active threads: " + Registry.getCurrentTenant().getWorkersThreadPool().getNumActive());
	}

	private static class TestThread extends Thread
	{
		private final CountDownLatch startLatch;
		private final String tenantId;
		private final String script;
		private final int numberOfThreads;
		private ImportResult result;

		public TestThread(final CountDownLatch startLatch, final String tenantId, final String script, final int numberOfThreads)
		{
			this.startLatch = startLatch;
			this.tenantId = tenantId;
			this.script = script;
			this.numberOfThreads = numberOfThreads;
		}

		@Override
		public void run()
		{
			if (!waitForStartSignal())
			{
				return;
			}

			Registry.setCurrentTenantByID(tenantId);
			final ImportService importService = Registry.getApplicationContext().getBean(ImportService.class);
			final ImportConfig config = createConfig(script, numberOfThreads);
			result = importService.importData(config);
		}

		private boolean waitForStartSignal()
		{
			if (startLatch == null)
			{
				return true;
			}
			try
			{
				startLatch.await();
				return true;
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}

		private ImportConfig createConfig(final String script, final int numberOfThreads)
		{
			final ImportConfig config = new ImportConfig();
			config.setMaxThreads(numberOfThreads);
			config.setSynchronous(true);
			config.setLegacyMode(false);
			config.setEnableCodeExecution(false);
			config.setDistributedImpexEnabled(false);
			config.setValidationMode(ValidationMode.STRICT);

			config.setScript(script);
			return config;
		}
	}
}
