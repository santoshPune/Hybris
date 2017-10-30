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
package de.hybris.platform.webservices;


import java.util.ArrayList;
import java.util.Collection;



public class HttpGetResponseBuilder<RESOURCE, RESPONSE> extends AbstractYResponseBuilder<RESOURCE, Object, RESPONSE>
{
	public HttpGetResponseBuilder()
	{
		super(Operation.READ);
	}


	public HttpGetResponseBuilder(final AbstractYResource resource)
	{
		super(resource, Operation.READ);
	}

	@Override
	public RESPONSE createResponseEntity(final RESOURCE resrcEntity, final Object reqEntity) throws Exception
	{
		RESPONSE result = null;
		if (resrcEntity instanceof Collection)
		{
			result = (RESPONSE) modelToDto(resrcEntity, new ArrayList(), 2);
		}
		else
		{
			result = (RESPONSE) modelToDto(resrcEntity, 1);
		}

		return result;
	}



}
