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
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;


public final class UiGroupHelperImpl
{
	private static final String GROUP_SEPERATOR = ".";
	private static final String INSTANCE_SEPERATOR = "-";
	private static final ThreadLocal<StringBuilder> keyBuilder = new ThreadLocal()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder(128);
		}
	};


	/**
	 * We don't want this class to be instantiated
	 */
	private UiGroupHelperImpl()
	{

	}

	public static String generateGroupIdForInstance(final InstanceModel instance)
	{
		return generateGroupIdForGroup(instance, null);
	}

	public static String generateGroupIdForGroup(final InstanceModel instance, final CsticGroup csticModelGroup)
	{
		final StringBuilder strBuilder = keyBuilder.get();
		strBuilder.setLength(0);
		if (strBuilder.capacity() > 1024)
		{
			strBuilder.trimToSize();
			strBuilder.ensureCapacity(1028);
		}
		strBuilder.append(instance.getId());
		strBuilder.append(INSTANCE_SEPERATOR);
		strBuilder.append(instance.getName());
		if (csticModelGroup != null)
		{
			strBuilder.append(GROUP_SEPERATOR);
			strBuilder.append(csticModelGroup.getName());
		}
		return strBuilder.toString();
	}


	public static String retrieveInstanceId(final String uiGroupId)
	{
		return uiGroupId.substring(0, uiGroupId.indexOf(INSTANCE_SEPERATOR));
	}

}
