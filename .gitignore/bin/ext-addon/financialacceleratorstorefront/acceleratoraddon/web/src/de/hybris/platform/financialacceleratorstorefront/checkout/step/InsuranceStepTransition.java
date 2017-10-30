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
 *
 */
package de.hybris.platform.financialacceleratorstorefront.checkout.step;

public interface InsuranceStepTransition
{
	String UNSET = "unset";
	String INVALID = "invalid";
	String VALIDATED = "validated";

	/*
	 * Return the current status of the step to be NOT_VISITED or INVALID or VALID
	 */
	String getCurrentStatus();

	/*
	 * Determine if the step is valid.
	 */
	boolean isValid();

	/*
	 * Determine if the step is visited.
	 */
	boolean isVisited();

	/*
	 * Set visited flag for the checkout step transition
	 */
	void setVisited(final boolean isVisited);

	/*
	 * Determine if the step is enabled.
	 */
	boolean getIsEnabled();
}
