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
package de.hybris.platform.cmswebservices.common.facade.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageExistsPredicateTest
{
	private static final String PAGE_ID = "test-page-id";
	private static final String INVALID = "invalid";

	@Mock
	private CMSPageService pageService;
	@Mock
	private AbstractPageModel page;

	@InjectMocks
	private PageExistsPredicate predicate;

	@Test
	public void shouldFail_PageNotFound() throws CMSItemNotFoundException
	{
		when(pageService.getPageForId(INVALID)).thenThrow(new CMSItemNotFoundException("exception"));

		final boolean result = predicate.test(INVALID);
		assertFalse(result);
	}

	@Test
	public void shouldPass_PageExists() throws CMSItemNotFoundException
	{
		when(pageService.getPageForId(PAGE_ID)).thenReturn(page);
		final boolean result = predicate.test(PAGE_ID);
		assertTrue(result);
	}

}
