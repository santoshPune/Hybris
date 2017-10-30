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
 *
 */
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionEngineService;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Path;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;


/**
 * Base class for promotionengineservices tests
 */
public class PromotionEngineServiceBaseTest extends ServicelayerTest
{

	@Resource
	private PromotionEngineService promotionEngineService;


	/**
	 * imports the given impex file and overwrites beans based on the given properties file using a
	 * {@link PropertyOverrideConfigurer}
	 */
	protected void setupImpexAndOverrideEngineContext(final String impexFileName)
	{
		try
		{
			importCsv(impexFileName, "utf-8");
		}
		catch (final ImpExException e)
		{
			throw new IllegalStateException("exception during test setup:", e);
		}
		configurePromotionEngineService();
	}

	public void configurePromotionEngineService()
	{
		final DefaultPromotionEngineService defaultPromotionEngineService = (DefaultPromotionEngineService) getApplicationContext()
				.getBean("promotionEngineService");
		defaultPromotionEngineService.setDefaultRuleEngineContextName("promotions-junit-context");
	}

	protected PromotionEngineService getPromotionEngineService()
	{
		return promotionEngineService;
	}

	protected String readRuleFile(final String fileName, final String path) throws IOException
	{
		final Path rulePath = get(getApplicationContext().getResource("classpath:" + path + fileName).getURI());
		final InputStream is = newInputStream(rulePath);
		final StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, forName("UTF-8"));
		return writer.toString();
	}

}
