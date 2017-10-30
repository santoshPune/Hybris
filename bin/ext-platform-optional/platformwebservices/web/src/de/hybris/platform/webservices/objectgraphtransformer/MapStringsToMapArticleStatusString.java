/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.webservices.objectgraphtransformer;

import java.util.Map;

import de.hybris.platform.catalog.enums.ArticleStatus;
import de.hybris.platform.webservices.util.objectgraphtransformer.PropertyContext;
import de.hybris.platform.webservices.util.objectgraphtransformer.PropertyInterceptor;


//Probably will be removed, waiting for DEL-260
public class MapStringsToMapArticleStatusString implements PropertyInterceptor<Map<String, String>, Map<ArticleStatus, String>>
{

	@Override
	public Map<ArticleStatus, String> intercept(final PropertyContext ctx, final Map<String, String> source)
	{
		//TODO convert map to map
		return null;
	}

}
