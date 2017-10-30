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
package de.hybris.platform.sap.productconfig.runtime.interf.model;

import java.util.List;


/**
 * Represents a conflict accessible to the conflict solver
 */
public interface SolvableConflictModel
{

	/**
	 * Sets description
	 *
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * @return the description
	 */
	String getDescription();

	/**
	 * @param assumptions
	 */
	void setConflictingAssumptions(List<ConflictingAssumptionModel> assumptions);

	/**
	 * @return the conflictingAssumptions attached to this conflict
	 */
	List<ConflictingAssumptionModel> getConflictingAssumptions();

	/**
	 * @param id
	 *           ID of solvable conflict
	 */
	void setId(String id);

	/**
	 * @return ID of conflict. Is not stable during multiple roundtrips to SSC
	 */
	String getId();

}
