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
package de.hybris.platform.personalizationservices;

import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import org.junit.Before;
import org.junit.Ignore;


@Ignore
public abstract class AbstractCxServiceTest extends ServicelayerTransactionalTest
{

	protected final static String SEGMENT_CODE = "segment1";
	protected final static String CUSTOMIZATION_CODE = "customization1";
	protected final static String VARIATION_CODE = "variation1";

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importCsv("/personalizationservices/test/testdata_personalizationservices.impex", "utf-8");
	}
}
