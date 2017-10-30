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
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import org.apache.log4j.Logger;


public class SSCTimer
{
	private static final Logger LOG = Logger.getLogger(SSCTimer.class);


	private long startTime;
	private long endTime;


	private String step;

	public SSCTimer()
	{
		super();
		startTime = System.nanoTime();
	}

	public void stop()
	{
		endTime = System.nanoTime();
		logTime();
	}

	protected void logTime()
	{
		if (!LOG.isDebugEnabled())
		{
			return;
		}
		final long timeInNanos = endTime - startTime;
		final long timeInMs = timeInNanos / (1000 * 1000);

		LOG.debug("Call to SSC (" + step + ") took " + timeInMs + "ms");
	}

	public void start(final String step)
	{
		this.step = step;
		startTime = System.nanoTime();
	}

}
