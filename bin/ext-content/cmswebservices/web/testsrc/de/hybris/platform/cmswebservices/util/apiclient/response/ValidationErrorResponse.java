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
package de.hybris.platform.cmswebservices.util.apiclient.response;

import java.io.Serializable;
import java.util.List;


public class ValidationErrorResponse implements Serializable
{

	private static final long serialVersionUID = 228642049858593462L;
	private List<ValidationObjectError> errors;

	public List<ValidationObjectError> getErrors()
	{
		return errors;
	}

	public void setErrors(final List<ValidationObjectError> errors)
	{
		this.errors = errors;
	}
}
