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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.util.CSSClassResolver;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;



public class CSSClassResolverImplTestBase
{

	protected CSSClassResolver classUnderTest;

	@Before
	public void setUp()
	{
		classUnderTest = new CSSClassResolverImpl();
	}

	protected UiGroupData createUiGroupWithSubGroup()
	{
		final UiGroupData subGroup = new UiGroupData();
		final UiGroupData group = new UiGroupData();
		final List<UiGroupData> subGroups = new ArrayList<>();
		subGroups.add(subGroup);
		group.setSubGroups(subGroups);
		group.setConfigurable(true);
		group.setGroupType(GroupType.INSTANCE);
		group.setGroupStatus(GroupStatusType.DEFAULT);
		subGroup.setConfigurable(true);
		subGroup.setGroupType(GroupType.CSTIC_GROUP);
		subGroup.setGroupStatus(GroupStatusType.DEFAULT);
		return group;
	}

	protected UiGroupData createUiGroupWithNoSubGroup()
	{
		final UiGroupData group = new UiGroupData();
		group.setConfigurable(true);
		group.setGroupType(GroupType.CSTIC_GROUP);
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setSubGroups(new ArrayList<UiGroupData>());
		return group;
	}

	protected boolean containsStyleClass(final String styleClassString, final String styleClass)
	{
		final String[] styles = styleClassString.split(CSSClassResolverImpl.STYLE_CLASS_SEPERATOR);
		boolean containsStyle = false;
		for (int ii = 0; ii < styles.length; ii++)
		{
			containsStyle = styleClass.equals(styles[ii]);
			if (containsStyle)
			{
				break;
			}
		}
		return containsStyle;
	}

	protected int getNumberOfStyleClasses(final String styleClassString)
	{
		int length;
		if (styleClassString.isEmpty())
		{
			length = 0;
		}
		else
		{
			length = styleClassString.split(CSSClassResolverImpl.STYLE_CLASS_SEPERATOR).length;
		}
		return length;
	}

	protected void assertContainsStyleClass(final String styleClassString, final String... expectedStyles)
	{
		int expectedLength = expectedStyles.length;
		if (expectedStyles.length == 1 && expectedStyles[0].isEmpty())
		{
			expectedLength = 0;
		}
		assertEquals("Wrong Number of style classes in list '" + styleClassString + "'.", expectedLength,
				getNumberOfStyleClasses(styleClassString));
		for (int ii = 0; ii < expectedLength; ii++)
		{
			final boolean containsStyle = containsStyleClass(styleClassString, expectedStyles[ii]);
			assertTrue("Style class '" + expectedStyles[ii] + "' missing in style class list '" + styleClassString + "'.",
					containsStyle);
		}
	}

}
