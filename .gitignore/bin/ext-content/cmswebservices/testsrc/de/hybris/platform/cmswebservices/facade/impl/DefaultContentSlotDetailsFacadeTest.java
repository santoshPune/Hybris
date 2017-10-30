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
package de.hybris.platform.cmswebservices.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import de.hybris.platform.cmswebservices.data.ContentSlotTypeRestrictionsData;
import de.hybris.platform.cmswebservices.pagescontentslotstyperestrictions.facade.impl.DefaultPageContentSlotTypeRestrictionsFacade;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultContentSlotDetailsFacadeTest
{
	private static final String VALID_PAGE_UID = "validPageUid";
	private static final String VALID_CONTENTSLOT_UID = "validContentSlotUid";

	@InjectMocks
	DefaultPageContentSlotTypeRestrictionsFacade defaultContentSlotDetailsFacade;

	@Mock
	CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;

	@Mock
	CMSAdminContentSlotService cmsAdminContentSlotService;

	@Mock
	CMSAdminPageService cmsAdminPageService;


	@Test
	public void getTypeRestrictionsForPageContentSlots_Returns_ContentSlotTypeRestrictionsDataList() throws Exception
	{
		when(
				cmsAdminTypeRestrictionsService.getTypeRestrictionsForContentSlot(
						cmsAdminPageService.getPageForIdFromActiveCatalogVersion(VALID_PAGE_UID),
						cmsAdminContentSlotService.getContentSlotForId(VALID_CONTENTSLOT_UID))).thenReturn(
				new HashSet<CMSComponentTypeModel>());

		final ContentSlotTypeRestrictionsData actualTypeRestrictions = defaultContentSlotDetailsFacade
				.getTypeRestrictionsForContentSlotUID(VALID_PAGE_UID, VALID_CONTENTSLOT_UID);

		assertFalse(actualTypeRestrictions.getContentSlotUid() == null);
	}
}
