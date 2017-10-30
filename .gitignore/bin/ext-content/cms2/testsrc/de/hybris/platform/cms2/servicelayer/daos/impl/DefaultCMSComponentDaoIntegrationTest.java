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
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cms2.search.pagedata.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSComponentDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class DefaultCMSComponentDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private CMSComponentDao cmsComponentDao;
	@Resource
	private CatalogVersionService catalogVersionService;

	private ABTestCMSComponentContainerModel component1;
	private ABTestCMSComponentContainerModel component2;
	private ABTestCMSComponentContainerModel component3;
	private CatalogVersionModel catalogVersion1;
	private CatalogVersionModel catalogVersion2;

	@Before
	public void setUp() throws Exception
	{

		importCsv("/test/cmsCatalogVersionTestData.csv", "windows-1252");
		catalogVersion1 = catalogVersionService.getCatalogVersion("cms_Catalog", "CatalogVersion1");
		catalogVersion2 = catalogVersionService.getCatalogVersion("cms_Catalog", "CatalogVersion2");

		component1 = new ABTestCMSComponentContainerModel();
		component2 = new ABTestCMSComponentContainerModel();
		component3 = new ABTestCMSComponentContainerModel();

		component1.setCatalogVersion(catalogVersion1);
		component1.setUid("testComponent1");
		component1.setName("test component");
		modelService.save(component1);
		component2.setCatalogVersion(catalogVersion1);
		component2.setUid("testComponent2");
		component2.setName("My component");
		modelService.save(component2);
		component3.setCatalogVersion(catalogVersion2);
		component3.setUid("testComponent3");
		modelService.save(component3);
	}

	@Test
	public void shouldFindCmsComponentsByCatalogVersionOnly_CatalogVersion1()
	{
		final List<AbstractCMSComponentModel> components = cmsComponentDao.findAllCMSComponentsByCatalogVersion(catalogVersion1);
		Assert.assertEquals(2, components.size());
		assertContainsComponentUid(components, "testComponent1");
		assertContainsComponentUid(components, "testComponent2");
		assertNotContainsComponentUid(components, "testComponent3");
	}


	@Test
	public void shouldFindCmsComponentsByCatalogVersionOnly_CatalogVersion2()
	{

		final List<AbstractCMSComponentModel> components = cmsComponentDao.findAllCMSComponentsByCatalogVersion(catalogVersion2);
		Assert.assertEquals(1, components.size());
		assertContainsComponentUid(components, "testComponent3");
		assertNotContainsComponentUid(components, "testComponent1");
		assertNotContainsComponentUid(components, "testComponent2");

	}

	@Test
	public void shouldFindCmsComponentsByMask()
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(2);
		pageableData.setSort("name");

		final SearchResult<AbstractCMSComponentModel> components = cmsComponentDao.findByCatalogVersionAndMask(catalogVersion1,
				"comp", pageableData);

		assertThat(components.getCount(), equalTo(2));
		assertThat(components.getTotalCount(), equalTo(2));
		assertThat(components.getResult().get(0).getUid(), equalTo("testComponent2"));
		assertThat(components.getResult().get(1).getUid(), equalTo("testComponent1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotFindCmsComponents_NoCatalogVersion()
	{
		cmsComponentDao.findAllCMSComponentsByCatalogVersion(null);
	}


	protected void assertContainsComponentUid(final List<AbstractCMSComponentModel> components1, final String componentUid)
	{
		Assert.assertTrue(components1.stream().map(component -> component.getUid()).anyMatch(uid -> uid.equals(componentUid)));
	}

	protected void assertNotContainsComponentUid(final List<AbstractCMSComponentModel> components1, final String componentUid)
	{
		Assert.assertTrue(components1.stream().map(component -> component.getUid()).noneMatch(uid -> uid.equals(componentUid)));
	}

}
