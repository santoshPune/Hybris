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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;

import org.junit.Test;


/**

 *
 */
@UnitTest
public class CSSClassResolverImplTest extends CSSClassResolverImplTestBase
{


	@Test
	public void testGetInputStyle_noError()
	{
		final CsticData cstic = new CsticData();
		cstic.setCsticStatus(CsticStatusType.DEFAULT);
		final String inputStyle = classUnderTest.getValueStyleClass(cstic);

		assertContainsStyleClass(inputStyle, CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE);

	}

	@Test
	public void testGetInputStyle_error()
	{
		final CsticData cstic = new CsticData();
		cstic.setCsticStatus(CsticStatusType.ERROR);

		final String inputStyle = classUnderTest.getValueStyleClass(cstic);

		assertContainsStyleClass(inputStyle, CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR,
				CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE);
	}

	@Test
	public void testGetLabelStyle_StatusDefault()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.DEFAULT);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL);

	}

	@Test
	public void testGetLabelStyle_StatusSucces()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.FINISHED);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_SUCCESS);
	}

	@Test
	public void testGetLabelStyle_StatusWarning()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.WARNING);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_WARNING);
	}

	@Test
	public void testGetLabelStyle_notRequiredError()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.ERROR);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);
		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_ERROR);
	}

	@Test
	public void testGetGroupStyle_Conflict()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.CONFLICT);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_CONFLICT);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 2, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResolverImpl.STYLE_CLASS_MENU_LEAF);
	}

	@Test
	public void test_MenuNodeStyleClass_Node()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED);
	}

	@Test
	public void test_MenuNodeStyleClass_NonConfLeaf()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setConfigurable(false);
		group.setGroupType(GroupType.INSTANCE);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 2, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NON_CONF_LEAF);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Error()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.ERROR);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_ERROR);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Warning()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_WARNING);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Ok()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.FINISHED);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COMPLETED);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setCollapsedInSpecificationTree(true);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COLLAPSED);
	}

	@Test
	public void test_MenuNodeStyleClass_NonConfNode_Collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setCollapsedInSpecificationTree(true);
		group.setConfigurable(false);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COLLAPSED);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Error()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.ERROR);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_ERROR);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Warning()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_WARNING);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Ok()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.FINISHED);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COMPLETED);
	}

	@Test
	public void testGetGroupStyle_ConflicGroup()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.CONFLICT);
		group.setCollapsed(true);
		group.setGroupType(GroupType.CONFLICT);
		final String groupStyle = classUnderTest.getGroupStyleClass(group, true);
		assertEquals(2, getNumberOfStyleClasses(groupStyle));
		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP,
				CSSClassResolverImpl.STYLE_CLASS_CONFLICTGROUP);

	}

	@Test
	public void testGetGroupStyle_Error()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.ERROR);
		group.setCollapsed(true);

		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_CLOSE,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_ERROR);
	}

	@Test
	public void testGetGroupStyle_Warning()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.WARNING);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_WARNING);
	}

	@Test
	public void testGetGroupStyle_Default()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsed(true);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_CLOSE);

	}

	@Test
	public void testGetGroupStyle_hideExpandCollapse()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsed(true);
		final String groupStyle = classUnderTest.getGroupStyleClass(group, true);
		assertEquals(1, getNumberOfStyleClasses(groupStyle));
		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP);

	}

	@Test
	public void testGetGroupStyle_Finished()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.FINISHED);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_FINISHED);
	}

	@Test
	public void testGetGroupStatusTooltipKey()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.ERROR);
		String tooltip = classUnderTest.getGroupStatusTooltipKey(group);
		assertEquals(CSSClassResolverImpl.RESOURCE_KEY_GROUP_ERROR_TOOLTIP, tooltip);
		group.setGroupStatus(GroupStatusType.FINISHED);
		tooltip = classUnderTest.getGroupStatusTooltipKey(group);
		assertEquals(CSSClassResolverImpl.RESOURCE_KEY_GROUP_FINISHED_TOOLTIP, tooltip);
		group.setGroupStatus(GroupStatusType.WARNING);
		tooltip = classUnderTest.getGroupStatusTooltipKey(group);
		assertEquals(CSSClassResolverImpl.RESOURCE_KEY_GROUP_ERROR_TOOLTIP, tooltip);
		group.setGroupStatus(GroupStatusType.DEFAULT);
		tooltip = classUnderTest.getGroupStatusTooltipKey(group);
		assertEquals("", tooltip);

	}


}
