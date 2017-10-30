/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
 * ----------------------------------------------------------------
 *
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
package de.hybris.platform.cmswebservices.namedquery;

import de.hybris.platform.cmswebservices.namedquery.SortDirection;

public class Sort {

	/** <i>Generated property</i> for <code>Sort.parameter</code> property defined at extension <code>cmswebservices</code>. */
	private String parameter;
	/** <i>Generated property</i> for <code>Sort.direction</code> property defined at extension <code>cmswebservices</code>. */
	private SortDirection direction;


		public void setParameter(final String parameter)
	{
		this.parameter = parameter;
	}

	public Sort withParameter(final String parameter)
	{
		this.parameter = parameter;
		return this;
	}

			
	public String getParameter() 
	{
		return parameter;
	}

	
		public void setDirection(final SortDirection direction)
	{
		this.direction = direction;
	}

	public Sort withDirection(final SortDirection direction)
	{
		this.direction = direction;
		return this;
	}

			
	public SortDirection getDirection() 
	{
		return direction;
	}

	}