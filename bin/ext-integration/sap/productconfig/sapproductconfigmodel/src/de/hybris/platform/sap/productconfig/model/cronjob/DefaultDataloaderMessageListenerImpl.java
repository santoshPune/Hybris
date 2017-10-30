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
package de.hybris.platform.sap.productconfig.model.cronjob;

import org.apache.log4j.Logger;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderMessageListener;
import com.sap.sxe.loader.download.Message;


/**
 * Default implementation for listening to data loader messages
 */
public class DefaultDataloaderMessageListenerImpl implements DataloaderMessageListener
{

	private static final Logger LOG = Logger.getLogger(DefaultDataloaderMessageListenerImpl.class);


	@Override
	public void messageReported(final Message message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(message.getMessage());
		}
	}



}
