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
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.util.CSSClassResolver;


public final class CSSClassResolverFactory
{
	private static CSSClassResolver resolver;

	private CSSClassResolverFactory()
	{
		//
	}

	/**
	 * setter to inject a resolver for testing
	 *
	 * @param resolver
	 */
	static void setResolver(final CSSClassResolver resolver)
	{
		CSSClassResolverFactory.resolver = resolver;
	}

	protected static CSSClassResolver getCSSClassResolver()
	{
		if (resolver == null)
		{
			resolver = (CSSClassResolver) Registry.getApplicationContext().getBean("sapProductConfigDefaultCssClassResolver");
		}
		return resolver;
	}

	public static String getStyleClassForGroup(final UiGroupData group, final Boolean hideExpandCollapse)
	{
		return getCSSClassResolver().getGroupStyleClass(group, hideExpandCollapse.booleanValue());
	}

	public static String getLabelStyleClassForCstic(final CsticData cstic)
	{
		return getCSSClassResolver().getLabelStyleClass(cstic);
	}

	public static String getValueStyleClassForCstic(final CsticData cstic)
	{
		return getCSSClassResolver().getValueStyleClass(cstic);
	}

	public static String getMenuNodeStyleClass(final UiGroupData group, final Integer level)
	{
		return getCSSClassResolver().getMenuNodeStyleClass(group, level);
	}

	public static String getMenuConflictStyleClass(final UiGroupData conflict)
	{
		return getCSSClassResolver().getMenuConflictStyleClass(conflict);
	}

	public static String getGroupStatusTooltipKey(final UiGroupData group)
	{
		return getCSSClassResolver().getGroupStatusTooltipKey(group);
	}
}
