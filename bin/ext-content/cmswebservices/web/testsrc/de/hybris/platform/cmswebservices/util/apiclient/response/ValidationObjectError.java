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


public class ValidationObjectError implements Serializable
{

	private static final long serialVersionUID = 1272681417397865503L;
	private String message;
	private String reason;
	private String subject;
	private String subjectType;
	private String type;

	public String getMessage()
	{
		return message;
	}

	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(final String reason)
	{
		this.reason = reason;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public String getSubjectType()
	{
		return subjectType;
	}

	public void setSubjectType(final String subjectType)
	{
		this.subjectType = subjectType;
	}

}
