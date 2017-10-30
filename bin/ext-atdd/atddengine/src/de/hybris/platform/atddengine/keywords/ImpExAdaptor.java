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
package de.hybris.platform.atddengine.keywords;

import de.hybris.platform.impex.jalo.ImpExException;

import java.io.InputStream;


public interface ImpExAdaptor
{
	void importStream(final InputStream inputStream, final String encoding, final String resourceName) throws ImpExException;
}
