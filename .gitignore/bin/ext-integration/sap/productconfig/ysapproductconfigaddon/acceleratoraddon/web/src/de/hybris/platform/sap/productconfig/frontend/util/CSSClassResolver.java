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
package de.hybris.platform.sap.productconfig.frontend.util;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;


/**

 *
 */
public interface CSSClassResolver
{

	public String getGroupStyleClass(UiGroupData group);

	public String getLabelStyleClass(CsticData cstic);

	public String getValueStyleClass(CsticData cstic);

	public String getMenuNodeStyleClass(UiGroupData group, Integer level);

	public String getGroupStyleClass(UiGroupData group, boolean hideExpandCollapse);

	public String getMenuConflictStyleClass(UiGroupData conflict);

	public String getGroupStatusTooltipKey(UiGroupData group);
}
