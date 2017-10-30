/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 * 
 *  
 */
package de.hybris.platform.classificationsystems.hmc;

import de.hybris.platform.classificationsystems.constants.ClassificationsystemsConstants;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;



public class Unspsc50TreeLeaf extends ClassificationImportTreeLeaf
{
	public Unspsc50TreeLeaf(final DisplayState displayState, final Chip parent)
	{
		super(displayState, parent, ClassificationsystemsConstants.UNSPSC_5_DE_EN, "startunspsc50");
	}
}
