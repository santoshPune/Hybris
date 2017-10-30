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


import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.util.CSSClassResolver;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**

 *
 */
public class CSSClassResolverImpl implements CSSClassResolver
{
	public static final String STYLE_CLASS_SEPERATOR = " ";
	public static final String STYLE_CLASS_CSTIC_LABEL = "cpq-csticlabel";
	public static final String STYLE_CLASS_CSTIC_LABEL_ERROR = "cpq-csticlabel-error";
	public static final String STYLE_CLASS_CSTIC_LABEL_SUCCESS = "cpq-csticlabel-success";
	public static final String STYLE_CLASS_CSTIC_LABEL_WARNING = "cpq-csticlabel-warning";
	public static final String STYLE_CLASS_CSTIC_LABEL_LONGTEXT = "cpq-csticlabel-longtext-icon";

	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE = "cpq-csticValue";
	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR = "cpq-csticValue-error";
	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_MULTI_LIST = "cpq-csticValue-multi";

	public static final String STYLE_CLASS_GROUP = "cpq-group-header";
	public static final String STYLE_CLASS_GROUP_FINISHED = "cpq-group-completed";
	public static final String STYLE_CLASS_GROUP_ERROR = "cpq-group-error";
	public static final String STYLE_CLASS_GROUP_WARNING = "cpq-group-warning";
	public static final String STYLE_CLASS_GROUP_CLOSE = "cpq-group-title-close";
	public static final String STYLE_CLASS_GROUP_OPEN = "cpq-group-title-open";

	public static final String STYLE_CLASS_MENU_NODE_EXPANDED = "cpq-menu-expanded";
	public static final String STYLE_CLASS_MENU_NODE_COLLAPSED = "cpq-menu-collapsed";
	public static final String STYLE_CLASS_MENU_NODE_ERROR = "cpq-menu-error";
	public static final String STYLE_CLASS_MENU_NODE_WARNING = "cpq-menu-warning";
	public static final String STYLE_CLASS_MENU_NODE_COMPLETED = "cpq-menu-completed";

	public static final String STYLE_CLASS_MENU_LEVEL = "cpq-menu-level-";
	public static final String STYLE_CLASS_MENU_NODE = "cpq-menu-node";
	public static final String STYLE_CLASS_MENU_LEAF = "cpq-menu-leaf";
	public static final String STYLE_CLASS_MENU_NON_CONF_LEAF = "cpq-menu-nonConfLeaf";

	public static final String STYLE_MENU_CONFLICT_HEADER = "cpq-menu-conflict-header";
	public static final String STYLE_MENU_CONFLICT_NODE = "cpq-menu-conflict-node";

	public static final String STYLE_CLASS_MENU_NODE_CONFLICT = "cpq-menu-conflict";
	public static final String STYLE_CLASS_CSTIC_LABEL_CONFLICT = "cpq-csticlabel-conflict";
	public static final String STYLE_CLASS_GROUP_CONFLICT = "cpq-group-conflict";
	public static final String STYLE_CLASS_CONFLICTGROUP = "cpq-conflictgroup";

	public static final String RESOURCE_KEY_GROUP_ERROR_TOOLTIP = "sapproductconfig.group.tooltip.error";
	public static final String RESOURCE_KEY_GROUP_FINISHED_TOOLTIP = "sapproductconfig.group.tooltip.finished";



	@Override
	public String getLabelStyleClass(final CsticData cstic)
	{
		String styleClassString = STYLE_CLASS_CSTIC_LABEL;
		switch (cstic.getCsticStatus())
		{
			case ERROR:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_ERROR);
				break;
			case WARNING:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_WARNING);
				break;
			case CONFLICT:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_CONFLICT);
				break;
			case FINISHED:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_SUCCESS);
				break;
			default:
				break;
		}

		return styleClassString;
	}


	@Override
	public String getValueStyleClass(final CsticData cstic)
	{
		String styleClassString = STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE;
		final CsticStatusType csticStatus = cstic.getCsticStatus();
		if (CsticStatusType.ERROR.equals(csticStatus) || CsticStatusType.WARNING.equals(csticStatus)
				|| CsticStatusType.CONFLICT.equals(csticStatus))
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR);
		}
		if (isMulitUiElementsType(cstic))
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_MULTI_LIST);
		}

		return styleClassString;
	}

	protected boolean isMulitUiElementsType(final CsticData cstic)
	{
		return cstic.getType() == UiType.CHECK_BOX_LIST || cstic.getType() == UiType.RADIO_BUTTON;
	}

	protected String appendStyleClass(final String styleClassString, final String styleClass)
	{
		if (styleClassString.isEmpty())
		{
			return styleClass;
		}

		return styleClassString + STYLE_CLASS_SEPERATOR + styleClass;
	}

	protected boolean isNullorEmptyList(final List<?> list)
	{
		return null == list || list.isEmpty();
	}


	protected boolean thereIsContentToExpand(final UiGroupData group)
	{
		// we only show first level of non-configurable components
		if (!group.isConfigurable() || CollectionUtils.isEmpty(group.getSubGroups()))
		{
			return false;
		}

		// if there is only exactly one configurable  sub instance, it is inlined into the parent instance
		// so there is not instance to show
		if (!group.isOneConfigurableSubGroup())
		{
			return true;
		}

		// however even if teis sub instance is inlined into the parent, there might be content on a depper level
		for (final UiGroupData subGroup : group.getSubGroups())
		{
			// check recursively
			if (!subGroup.isConfigurable() || thereIsContentToExpand(subGroup))
			{
				// if one branch has content, it is sufficient
				return true;
			}
		}

		return false;
	}

	@Override
	public String getGroupStyleClass(final UiGroupData group)
	{
		return getGroupStyleClass(group, false);
	}

	@Override
	public String getGroupStyleClass(final UiGroupData group, final boolean hideExpandCollapse)
	{
		String styleClassString = STYLE_CLASS_GROUP;
		if (GroupType.CONFLICT == group.getGroupType())
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CONFLICTGROUP);
		}
		else
		{
			switch (group.getGroupStatus())
			{
				case ERROR:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_ERROR);
					break;
				case WARNING:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_WARNING);
					break;
				case CONFLICT:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_CONFLICT);
					break;
				case FINISHED:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_FINISHED);
					break;
				default:
					break;
			}
		}
		if (!hideExpandCollapse)
		{
			if (group.isCollapsed())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_CLOSE);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_OPEN);
			}
		}

		return styleClassString;
	}

	@Override
	public String getMenuNodeStyleClass(final UiGroupData group, final Integer level)
	{
		String styleClassString = STYLE_CLASS_MENU_LEVEL + level.toString();
		final boolean showAsNode = !isNullorEmptyList(group.getSubGroups());
		final boolean showAsLeaf = GroupType.CSTIC_GROUP == group.getGroupType();

		if (showAsNode)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE);
			if (group.isCollapsedInSpecificationTree())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COLLAPSED);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_EXPANDED);
			}
		}
		else if (showAsLeaf)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_LEAF);
		}
		else
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NON_CONF_LEAF);
		}

		if (group.isConfigurable())
		{
			switch (group.getGroupStatus())
			{
				case ERROR:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_ERROR);
					break;
				case WARNING:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_WARNING);
					break;
				case CONFLICT:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_CONFLICT);
					break;
				case FINISHED:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COMPLETED);
					break;
				default:
					break;
			}
		}
		return styleClassString;
	}

	@Override
	public String getMenuConflictStyleClass(final UiGroupData conflict)
	{
		final boolean showAsConflictHeader = GroupType.CONFLICT_HEADER == conflict.getGroupType();
		String styleClassString = "";
		if (showAsConflictHeader)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_MENU_CONFLICT_HEADER);
			if (conflict.isCollapsedInSpecificationTree())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COLLAPSED);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_EXPANDED);
			}
		}
		else
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_MENU_CONFLICT_NODE);
		}
		return styleClassString;
	}

	@Override
	public String getGroupStatusTooltipKey(final UiGroupData group)
	{
		String groupStatusTooltipKey = "";
		switch (group.getGroupStatus())
		{
			case WARNING:
			case ERROR:
				groupStatusTooltipKey = RESOURCE_KEY_GROUP_ERROR_TOOLTIP;
				break;
			case FINISHED:
				groupStatusTooltipKey = RESOURCE_KEY_GROUP_FINISHED_TOOLTIP;
				break;
			default:
				break;
		}

		return groupStatusTooltipKey;
	}
}
