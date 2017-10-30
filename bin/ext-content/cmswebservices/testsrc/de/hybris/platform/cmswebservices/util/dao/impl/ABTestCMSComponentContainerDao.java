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
 */
package de.hybris.platform.cmswebservices.util.dao.impl;

import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;


public class ABTestCMSComponentContainerDao extends AbstractCmsWebServicesDao<ABTestCMSComponentContainerModel>
{

	@Override
	protected String getQuery()
	{
		return "SELECT {pk} FROM {ABTestCMSComponentContainer} WHERE {uid}=?uid AND {catalogVersion}=?catalogVersion";
	}

}
