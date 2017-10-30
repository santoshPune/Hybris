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
package de.hybris.platform.mediaconversion.os.process;

/**
 * @author pohl
 */
class Handle<T> 
{
	private T value;

	public T get() 
	{
		return this.value;
	}

	public void set(final T value) 
	{
		this.value = value;
	}
}
