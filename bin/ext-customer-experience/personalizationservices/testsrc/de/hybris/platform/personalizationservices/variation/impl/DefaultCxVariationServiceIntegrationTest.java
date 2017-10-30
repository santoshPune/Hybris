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
package de.hybris.platform.personalizationservices.variation.impl;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.AbstractCxServiceTest;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultCxVariationServiceIntegrationTest extends AbstractCxServiceTest
{

	@Resource
	private CxVariationService cxVariationService;

	@Resource
	private CxCustomizationService cxCustomizationService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private ModelService modelService;

	@Test
	public void testFindVariationByCode()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization(CUSTOMIZATION_CODE,
				catalogVersion);

		//when
		final Optional<CxVariationModel> variation = cxVariationService.getVariation(VARIATION_CODE, customization.get());

		//then
		assertTrue(variation.isPresent());
		assertTrue(VARIATION_CODE.equals(variation.get().getCode()));
	}

	@Test
	public void testFindNoVariationByCode()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization(CUSTOMIZATION_CODE,
				catalogVersion);

		//when
		final Optional<CxVariationModel> variation = cxVariationService.getVariation(VARIATION_CODE + "...", customization.get());

		//then
		assertFalse(variation.isPresent());
	}

	@Test
	public void testFindVariationsByKey()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final CxVariationKey key = new CxVariationKey();
		key.setCustomizationCode(CUSTOMIZATION_CODE);
		key.setVariationCode(VARIATION_CODE);

		//when
		final Collection<CxVariationModel> variations = cxVariationService.getVariations(Collections.singleton(key),
				catalogVersion);


		//then
		Assert.assertNotNull(variations);
		Assert.assertEquals(1, variations.size());
		Assert.assertEquals(VARIATION_CODE, variations.iterator().next().getCode());

	}

	@Test
	public void testCreateVariation()
	{
		//given
		final String code = "newVariationCode";
		final String name = "newVariationName";
		CxVariationModel variation = modelService.create(CxVariationModel.class);
		variation.setCode(code);
		variation.setName(name);
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization("customization1",
				catalogVersionService.getCatalogVersion("testCatalog", "Online"));
		final int expectedRank = customization.get().getVariations().size();

		//when
		variation = cxVariationService.createVariation(variation, customization.get(), null);

		//then
		Assert.assertNotNull(variation);
		Assert.assertEquals(code, variation.getCode());
		Assert.assertEquals(customization.get().getPk(), variation.getCustomization().getPk());
		Assert.assertEquals(customization.get().getCatalogVersion().getPk(), variation.getCatalogVersion().getPk());
		Assert.assertEquals(expectedRank, variation.getRank().intValue());
	}

	@Test
	public void testCreateVariationWithRank()
	{
		//given
		final Integer rank = Integer.valueOf(1);
		final String code = "newVariationCode";
		final String name = "newVariationName";
		CxVariationModel variation = modelService.create(CxVariationModel.class);
		variation.setCode(code);
		variation.setName(name);
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization("customization1",
				catalogVersionService.getCatalogVersion("testCatalog", "Online"));
		final int variationCount = customization.get().getVariations().size();

		//when
		variation = cxVariationService.createVariation(variation, customization.get(), rank);

		//then
		Assert.assertNotNull(variation);
		Assert.assertEquals(code, variation.getCode());
		Assert.assertEquals(customization.get().getPk(), variation.getCustomization().getPk());
		Assert.assertEquals(customization.get().getCatalogVersion().getPk(), variation.getCatalogVersion().getPk());
		Assert.assertEquals(rank.intValue(), variation.getRank().intValue());
		Assert.assertEquals(variationCount + 1, customization.get().getVariations().size());
	}
}
