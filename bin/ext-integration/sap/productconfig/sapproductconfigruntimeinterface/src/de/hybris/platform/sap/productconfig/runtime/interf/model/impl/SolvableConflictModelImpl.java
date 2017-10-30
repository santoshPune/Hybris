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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.ConflictingAssumptionModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;

import java.util.List;


/**
 * Default implementation of {@link SolvableConflictModel}
 */
public class SolvableConflictModelImpl implements SolvableConflictModel
{

	private String description;
	private List<ConflictingAssumptionModel> conflictingAssumptions;
	private String id;

	@Override
	public void setDescription(final String description)
	{
		this.description = description;

	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public void setConflictingAssumptions(final List<ConflictingAssumptionModel> assumptions)
	{
		this.conflictingAssumptions = assumptions;

	}

	@Override
	public List<ConflictingAssumptionModel> getConflictingAssumptions()
	{
		return conflictingAssumptions;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder(50);
		builder.append("\n[SolvableConflictModelImpl [description=");
		builder.append(description);
		builder.append(", conflictingAssumptions=");
		builder.append(conflictingAssumptions);
		builder.append("]]");
		return builder.toString();
	}


	@Override
	public void setId(final String id)
	{
		this.id = id;
	}

	@Override
	public String getId()
	{
		return id;
	}

}
