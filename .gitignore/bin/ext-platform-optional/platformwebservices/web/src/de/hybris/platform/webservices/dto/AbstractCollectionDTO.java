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
package de.hybris.platform.webservices.dto;

import javax.xml.bind.annotation.XmlAttribute;


/**
 * Abstract base class for collection DTOs.
 */
public abstract class AbstractCollectionDTO implements UriDTO
{
	private String uri;

	@XmlAttribute
	@Override
	public String getUri()
	{
		return uri;
	}

	@Override
	public void setUri(final String uri)
	{
		this.uri = uri;
	}
}
