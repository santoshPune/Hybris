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
package de.hybris.platform.persistence.links.jdbc.dml.context;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;

import java.util.Date;


@IntegrationTest
public class NewTransactionContextIntegrationTest extends AbstractRelationModifictionContextIntegrationTest
{
	@Override
	protected RelationModifictionContext instantiateContext(final Date now)
	{
		return new NewTransactionContext(RELATION_CODE, writePersistenceGateway, cacheInvalidator, true, now);
	}
}
