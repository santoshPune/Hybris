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
package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSAdminContentSlotServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final Integer FIRST_INDEX = new Integer(0);
	private static final Integer SECOND_INDEX = new Integer(1);
	private static final Integer THIRD_INDEX = new Integer(2);
	private static final Integer FAIL_INDEX = new Integer(-2);

	@Resource
	private ModelService modelService;
	@Resource
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Resource
	private CMSAdminComponentService cmsAdminComponentService;
	@Resource
	private CMSContentSlotDao cmsContentSlotDao;
	@Resource
	private CMSDataFactory cmsDataFactory;

	private ContentSlotModel slot;
	private ABTestCMSComponentContainerModel component;
	private CatalogModel catalog;
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp()
	{
		component = new ABTestCMSComponentContainerModel();
		catalog = new CatalogModel();
		catalog.setId("testCatalog-addComponentToSlot");
		catalogVersion = new CatalogVersionModel();
		catalogVersion.setCatalog(catalog);
		catalogVersion.setVersion("1.0");
		component.setCatalogVersion(catalogVersion);
		component.setUid("testComponent");

		slot = new ContentSlotModel();
		slot.setCatalogVersion(catalogVersion);
		slot.setUid("testSlot");
	}

	@Test
	public void shouldAddComponentToContentSlot_firstPosition()
	{
		final ArrayList<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		slot.setCmsComponents(components);
		cmsAdminContentSlotService.addCMSComponentToContentSlot(component, slot, FIRST_INDEX);

		assertFalse(slot.getCmsComponents().isEmpty());
		assertEquals(1, slot.getCmsComponents().size());
		assertEquals(component, slot.getCmsComponents().get(FIRST_INDEX.intValue()));
	}

	@Test
	public void shouldAddComponentToContentSlot_secondPosition()
	{
		final ABTestCMSComponentContainerModel component1 = new ABTestCMSComponentContainerModel();
		final ABTestCMSComponentContainerModel component2 = new ABTestCMSComponentContainerModel();

		component1.setCatalogVersion(catalogVersion);
		component1.setUid("testComponent1");
		component2.setCatalogVersion(catalogVersion);
		component2.setUid("testComponent2");

		final List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		components.add(component1);
		components.add(component2);
		slot.setCmsComponents(components);
		cmsAdminContentSlotService.addCMSComponentToContentSlot(component, slot, SECOND_INDEX);

		assertFalse(slot.getCmsComponents().isEmpty());
		assertEquals(3, slot.getCmsComponents().size());
		assertEquals(component.getUid(), slot.getCmsComponents().get(SECOND_INDEX.intValue()).getUid());
	}

	@Test
	public void shouldAddComponentToContentSlot_thirdPosition()
	{
		final ABTestCMSComponentContainerModel component1 = new ABTestCMSComponentContainerModel();
		final ABTestCMSComponentContainerModel component2 = new ABTestCMSComponentContainerModel();

		component1.setCatalogVersion(catalogVersion);
		component1.setUid("testComponent1");
		component2.setCatalogVersion(catalogVersion);
		component2.setUid("testComponent2");

		final List<AbstractCMSComponentModel> components = new ArrayList<AbstractCMSComponentModel>();
		components.add(component1);
		components.add(component2);
		slot.setCmsComponents(components);
		cmsAdminContentSlotService.addCMSComponentToContentSlot(component, slot, THIRD_INDEX);

		assertFalse(slot.getCmsComponents().isEmpty());
		assertEquals(3, slot.getCmsComponents().size());
		assertEquals(component.getUid(), slot.getCmsComponents().get(THIRD_INDEX.intValue()).getUid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullIndex()
	{
		cmsAdminContentSlotService.addCMSComponentToContentSlot(component, slot, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_negativeIndex()
	{
		cmsAdminContentSlotService.addCMSComponentToContentSlot(component, slot, FAIL_INDEX);
	}

}
