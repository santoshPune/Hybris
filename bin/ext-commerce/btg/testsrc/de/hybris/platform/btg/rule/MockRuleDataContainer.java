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
package de.hybris.platform.btg.rule;

import de.hybris.platform.btg.rule.impl.AbstractRuleDataContainer;
import de.hybris.platform.btg.rule.impl.StorageElementsContainer;

import java.io.Serializable;


/**
 * 
 */
public class MockRuleDataContainer<T extends Serializable> extends AbstractRuleDataContainer<T>
{
	private final StorageElementsContainer<T> container = new StorageElementsContainer<T>();

	@Override
	protected StorageElementsContainer<T> getStorageElementsContainer()
	{
		return container;
	}
}
