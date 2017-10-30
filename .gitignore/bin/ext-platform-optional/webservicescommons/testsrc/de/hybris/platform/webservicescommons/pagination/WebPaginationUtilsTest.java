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
package de.hybris.platform.webservicescommons.pagination;

import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.pagination.impl.DefaultWebPaginationUtils;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class WebPaginationUtilsTest
{
	private WebPaginationUtils webPaginationUtils;

	@Before
	public void setup()
	{
		webPaginationUtils = new DefaultWebPaginationUtils();
	}

	@Test
	public void testPaginationOnEmptyResult()
	{
		final TestSearchResult searchResult = new TestSearchResult(0, 0, 0, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(0, pagination.getCount());
		Assert.assertEquals(0, pagination.getPage());
		Assert.assertEquals(0, pagination.getTotalPages());
		Assert.assertEquals(0, pagination.getTotalCount());
	}

	@Test
	public void testPaginationOnSinglePage()
	{
		final TestSearchResult searchResult = new TestSearchResult(3, 3, 0, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(3, pagination.getCount());
		Assert.assertEquals(0, pagination.getPage());
		Assert.assertEquals(1, pagination.getTotalPages());
		Assert.assertEquals(3, pagination.getTotalCount());
	}

	@Test
	public void testPaginationForFirstOnMultiplePages()
	{
		final TestSearchResult searchResult = new TestSearchResult(100, 973, 0, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(100, pagination.getCount());
		Assert.assertEquals(0, pagination.getPage());
		Assert.assertEquals(10, pagination.getTotalPages());
		Assert.assertEquals(973, pagination.getTotalCount());
	}


	@Test
	public void testPaginationForMiddleOnMultiplePages()
	{
		final TestSearchResult searchResult = new TestSearchResult(100, 973, 500, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(100, pagination.getCount());
		Assert.assertEquals(5, pagination.getPage());
		Assert.assertEquals(10, pagination.getTotalPages());
		Assert.assertEquals(973, pagination.getTotalCount());
	}

	@Test
	public void testPaginationForLastOnMultiplePages()
	{
		final TestSearchResult searchResult = new TestSearchResult(73, 973, 900, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(73, pagination.getCount());
		Assert.assertEquals(9, pagination.getPage());
		Assert.assertEquals(10, pagination.getTotalPages());
		Assert.assertEquals(973, pagination.getTotalCount());
	}

	@Test
	public void testPaginationForAboveLastOnMultiplePages()
	{
		final TestSearchResult searchResult = new TestSearchResult(0, 973, 1000, 100);

		final PaginationWsDTO pagination = webPaginationUtils.buildPagination(searchResult);

		Assert.assertEquals(0, pagination.getCount());
		Assert.assertEquals(10, pagination.getPage());
		Assert.assertEquals(10, pagination.getTotalPages());
		Assert.assertEquals(973, pagination.getTotalCount());
	}

	private static class TestSearchResult implements SearchResult<String>
	{

		private final int count, totalCount, requStart, requCount;

		public TestSearchResult(final int count, final int totalCount, final int requStart, final int requCount)
		{
			this.count = count;
			this.totalCount = totalCount;
			this.requStart = requStart;
			this.requCount = requCount;
		}

		@Override
		public int getCount()
		{
			return count;
		}

		@Override
		public int getTotalCount()
		{
			return totalCount;
		}

		@Override
		public List<String> getResult()
		{
			return null;
		}

		@Override
		public int getRequestedStart()
		{
			return requStart;
		}

		@Override
		public int getRequestedCount()
		{
			return requCount;
		}

	}
}
