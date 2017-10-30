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
 */
package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.comments.model.DomainModel;

import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.warehousing.util.builder.DomainModelBuilder;
import org.springframework.beans.factory.annotation.Required;


public class Domains extends AbstractItems<DomainModel>
{
	public static final String DOMAIN_CODE = "warehousingDomain";
	public static final String DOMAIN_NAME = "Warehousing Domain";

	private CommentService commentService;

	public DomainModel warehousingDomain()
	{
		return getOrSaveAndReturn(() -> getCommentService().getDomainForCode(DOMAIN_CODE),
				() -> DomainModelBuilder.aModel()
						.withCode(DOMAIN_CODE)
						.withName(DOMAIN_NAME)
						.build());
	}

	protected CommentService getCommentService()
	{
		return commentService;
	}

	@Required
	public void setCommentService(CommentService commentService)
	{
		this.commentService = commentService;
	}

}
