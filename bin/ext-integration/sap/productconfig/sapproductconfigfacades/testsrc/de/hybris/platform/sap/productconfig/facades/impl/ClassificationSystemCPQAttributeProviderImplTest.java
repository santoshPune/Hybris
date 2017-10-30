/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ClassificationSystemCPQAttributeProviderImplTest
{
	ClassificationSystemCPQAttributesProviderImpl classUnderTest = new ClassificationSystemCPQAttributesProviderImpl();
	private final List<CatalogModel> catalogs = new ArrayList<CatalogModel>();
	private final List<ClassificationSystemModel> availableClassificationSystems = new ArrayList<ClassificationSystemModel>();
	private final List<ClassificationSystemModel> availableClassificationSystemsMatchingPattern = new ArrayList<ClassificationSystemModel>();
	private final ClassificationSystemModel classificationSystem200 = new ClassificationSystemModel();
	private final ClassificationSystemModel classificationSystem300 = new ClassificationSystemModel();
	private final ClassificationSystemModel powertools = new ClassificationSystemModel();
	private final CatalogModel productCat = new CatalogModel();
	private final ClassificationSystemVersionModel classificationVersionModel200 = new ClassificationSystemVersionModel();
	private final ClassificationSystemVersionModel classificationVersionModel300 = new ClassificationSystemVersionModel();

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private BaseStoreModel baseStore;

	@Mock
	private ClassificationSystemService classificationService;

	@Before
	public void setUp()
	{
		classificationSystem200.setId("ERP Classification 200");
		classificationSystem300.setId("ERP Classification 300");
		powertools.setId("Powertools Classification");
		productCat.setId("Product Cat");
		MockitoAnnotations.initMocks(this);
		classUnderTest.setBaseStoreService(baseStoreService);
		Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStore);
		Mockito.when(baseStore.getCatalogs()).thenReturn(catalogs);
		classUnderTest.setClassificationService(classificationService);
		Mockito.when(classificationService.getSystemVersion(classificationSystem200.getId(), null))
				.thenReturn(classificationVersionModel200);
		Mockito.when(classificationService.getSystemVersion(classificationSystem300.getId(), null))
				.thenReturn(classificationVersionModel300);
	}

	@Test
	public void testDetermineAvailableClassificationSystemsNoClassification()
	{
		catalogs.add(productCat);
		classUnderTest.determineAvailableClassificationSystems(catalogs, availableClassificationSystems,
				availableClassificationSystemsMatchingPattern);
		assertTrue("No classification expected", availableClassificationSystems.isEmpty());
		assertTrue("No classification expected", availableClassificationSystemsMatchingPattern.isEmpty());
	}

	@Test
	public void testDetermineAvailableClassificationSystemsClassification200()
	{
		catalogs.add(productCat);
		catalogs.add(classificationSystem200);
		classUnderTest.determineAvailableClassificationSystems(catalogs, availableClassificationSystems,
				availableClassificationSystemsMatchingPattern);
		assertFalse("Classification expected", availableClassificationSystems.isEmpty());
		assertTrue("No classification expected matching pattern", availableClassificationSystemsMatchingPattern.isEmpty());
	}

	@Test
	public void testDetermineAvailableClassificationSystemsClassification300()
	{
		catalogs.add(productCat);
		catalogs.add(classificationSystem300);
		classUnderTest.determineAvailableClassificationSystems(catalogs, availableClassificationSystems,
				availableClassificationSystemsMatchingPattern);
		assertFalse("Classification expected", availableClassificationSystems.isEmpty());
		assertFalse("Classification expected matching pattern", availableClassificationSystemsMatchingPattern.isEmpty());
	}

	@Test
	public void testPattern()
	{
		final String substring = "300";
		assertEquals("Expected substring is " + substring, substring, classUnderTest.getClassificationSystemSubString());
	}

	@Test
	public void testGetSystemVersion()
	{
		catalogs.add(productCat);
		final ClassificationSystemVersionModel systemVersion = classUnderTest.getSystemVersion();
		assertNull("No classification expected", systemVersion);
	}

	@Test
	public void testGetSystemVersionClassification200()
	{
		catalogs.add(productCat);
		catalogs.add(classificationSystem200);
		final ClassificationSystemVersionModel systemVersion = classUnderTest.getSystemVersion();
		assertEquals("We expect a classification version", classificationVersionModel200, systemVersion);
	}

	@Test
	public void testGetSystemVersionClassification300()
	{
		catalogs.add(productCat);
		catalogs.add(classificationSystem200);
		catalogs.add(classificationSystem300);
		final ClassificationSystemVersionModel systemVersion = classUnderTest.getSystemVersion();
		assertEquals("We expect a classification version 300 since the corresponding catalog matches the pattern",
				classificationVersionModel300, systemVersion);
	}

}
