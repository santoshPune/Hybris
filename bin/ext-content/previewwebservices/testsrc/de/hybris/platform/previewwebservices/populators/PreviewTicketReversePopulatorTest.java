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
package de.hybris.platform.previewwebservices.populators;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PreviewTicketReversePopulatorTest extends ServicelayerTransactionalTest
{
	@Resource
	private PreviewTicketReversePopulator previewTicketReversePopulator;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void importCatalogs() throws Exception
	{
		importCsv("/previewwebservices/test/previewwebservices_testcatalogs.csv", "utf-8");
	}

	@Test
	public void shouldHaveOnlineProductAndStagedContentCatalogs()
	{
		final PreviewTicketWsDTO source = createPreviewTicketDTO();
		final PreviewDataModel target = new PreviewDataModel();
		previewTicketReversePopulator.populate(source, target);

		Assert.assertEquals("Staged", target.getActiveCatalogVersion().getVersion());
		Assert.assertEquals("testContentCatalog", target.getActiveCatalogVersion().getCatalog().getId());
		Assert.assertEquals(target.getCatalogVersions().size(), 2);
		Assert.assertTrue(
				target.getCatalogVersions().contains(catalogVersionService.getCatalogVersion("testContentCatalog", "Staged")));
		Assert.assertTrue(
				target.getCatalogVersions().contains(catalogVersionService.getCatalogVersion("testProductCatalog", "Online")));
		Assert.assertNotNull(target.getPage());
	}



	@Test
	public void shouldNotPopulateTargetWithPageWhenPageIsNotProvided()
	{
		final PreviewTicketWsDTO source = createPreviewTicketDTO();
		source.setPageId(null);
		final PreviewDataModel target = new PreviewDataModel();
		previewTicketReversePopulator.populate(source, target);
		Assert.assertNull(target.getPage());
	}

	private PreviewTicketWsDTO createPreviewTicketDTO()
	{
		final PreviewTicketWsDTO source = new PreviewTicketWsDTO();
		source.setCatalog("testContentCatalog");
		source.setCatalogVersion("Staged");
		source.setResourcePath("https://127.0.0.1:9002/yacceleratorstorefront?site=testSite");
		source.setPageId("homepage");
		return source;
	}

}
