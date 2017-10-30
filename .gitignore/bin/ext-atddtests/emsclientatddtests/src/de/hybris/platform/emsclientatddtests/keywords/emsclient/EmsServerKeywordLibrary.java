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
package de.hybris.platform.emsclientatddtests.keywords.emsclient;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.emsclientatddtests.server.EmsJetty;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class EmsServerKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = Logger.getLogger(EmsServerKeywordLibrary.class);

	@Autowired
	private EmsJetty server;

	public void startServer() throws Exception
	{
		server.start();
	}

	public void stopServer() throws Exception
	{
		server.stop();
	}
}
