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

package com.hybris.backoffice.cockpitng.dataaccess.facades.object;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import com.hybris.backoffice.cockpitng.dataaccess.facades.common.PlatformFacadeStrategyHandleCache;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.labels.LabelService;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;


@IntegrationTest
public class DefaultPlatformObjectFacadeStrategyIntegrationTest extends ServicelayerTransactionalTest
{

	@InjectMocks
	protected DefaultPlatformObjectFacadeStrategy platformObjectFacadeStrategy = new DefaultPlatformObjectFacadeStrategy();

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private CatalogService catalogService;

	@Mock
	private ItemModificationHistoryService itemModificationHistoryService;
	@Mock
	private LabelService labelService;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private DataAttribute dataAttribute;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		createCoreData();
		createDefaultCatalog();
		platformObjectFacadeStrategy.setModelService(modelService);
		platformObjectFacadeStrategy.setTypeService(typeService);
		final PlatformFacadeStrategyHandleCache cache = new PlatformFacadeStrategyHandleCache();
		cache.setTypeService(typeService);
		platformObjectFacadeStrategy.setPlatformFacadeStrategyHandleCache(cache);
		final DataType dataType = mock(DataType.class);
		when(typeFacade.load(anyString())).thenReturn(dataType);
		when(dataType.getAttribute(anyString())).thenReturn(dataAttribute);
	}

	@Test
	public void testAttributesPersistence() throws ObjectSavingException
	{
		final ProductModel product = modelService.create(ProductModel._TYPECODE);
		final CatalogVersionModel activeCatalogVersion = catalogService.getDefaultCatalog().getActiveCatalogVersion();
		product.setCatalogVersion(activeCatalogVersion);
		product.setCode(Long.toString(System.currentTimeMillis(), 24));
		modelService.saveAll();

		final CatalogVersionModel newCatalogVersion = modelService.create(CatalogVersionModel._TYPECODE);
		newCatalogVersion.setCatalog(activeCatalogVersion.getCatalog());
		newCatalogVersion.setVersion("TestPersisted");

		product.setCatalogVersion(newCatalogVersion);

		modelService.detachAll();

		platformObjectFacadeStrategy.save(product, null);

		assertThat(modelService.isNew(newCatalogVersion)).isFalse();
		assertThat(modelService.isUpToDate(newCatalogVersion)).isTrue();

		final KeywordModel keyword = modelService.create(KeywordModel._TYPECODE);
		keyword.setKeyword("testKeyword");
		final LanguageModel language = modelService.create(LanguageModel._TYPECODE);
		language.setIsocode("pl");
		language.setName("Polish");
		keyword.setLanguage(language);

		product.setKeywords(ImmutableList.of(keyword));
		product.setDescription("English description", Locale.ENGLISH);


		product.setArticleStatus(Maps.newHashMap(), Locale.ENGLISH);

		newCatalogVersion.setVersion("TestNotPersisted");

		assertThat(modelService.isNew(keyword)).isTrue();
		assertThat(modelService.isNew(language)).isTrue();
		platformObjectFacadeStrategy.save(product, null);
		assertThat(modelService.isNew(keyword)).isFalse();
		assertThat(modelService.isUpToDate(keyword)).isTrue();
		assertThat(modelService.isNew(language)).isFalse();
		assertThat(modelService.isUpToDate(language)).isTrue();
		assertThat(modelService.isNew(newCatalogVersion)).isFalse();
		assertThat(modelService.isUpToDate(newCatalogVersion)).isFalse();
	}

	@Test
	public void testDeletionInPartOfRelationWhenVariantFirst() throws ObjectSavingException
	{
		// given
		final CatalogVersionModel activeCatalogVersion = catalogService.getDefaultCatalog().getActiveCatalogVersion();

		final VariantTypeModel variantTypeModel = createVariantTypeModel();
		final ProductModel product = createProductModel(activeCatalogVersion, variantTypeModel);

		modelService.saveAll();
		final VariantProductModel variantProduct = createVariantProductModel(activeCatalogVersion, variantTypeModel, product);
		modelService.saveAll();

		modelService.detachAll();

		final List<ItemModel> objectsToDelete = new LinkedList<>();
		objectsToDelete.add(variantProduct);
		objectsToDelete.add(product);

		// when
		platformObjectFacadeStrategy.delete(objectsToDelete, null);

		// then
		assertThat(modelService.isRemoved(product)).isTrue();
		assertThat(modelService.isRemoved(variantProduct)).isTrue();
	}

	@Test
	public void testDeletionInPartOfRelationWhenProductFirst() throws ObjectSavingException
	{
		// given
		final CatalogVersionModel activeCatalogVersion = catalogService.getDefaultCatalog().getActiveCatalogVersion();

		final VariantTypeModel variantTypeModel = createVariantTypeModel();
		final ProductModel product = createProductModel(activeCatalogVersion, variantTypeModel);

		modelService.saveAll();
		final VariantProductModel variantProduct = createVariantProductModel(activeCatalogVersion, variantTypeModel, product);
		modelService.saveAll();

		modelService.detachAll();

		final List<ItemModel> objectsToDelete = new LinkedList<>();
		objectsToDelete.add(product);
		objectsToDelete.add(variantProduct);

		// when
		platformObjectFacadeStrategy.delete(objectsToDelete, null);

		// then
		assertThat(modelService.isRemoved(product)).isTrue();
		assertThat(modelService.isRemoved(variantProduct)).isTrue();
	}

	@Test
	public void testDeletionAlreadyDeletedObjects() throws ObjectSavingException
	{
		// given
		final CatalogVersionModel activeCatalogVersion = catalogService.getDefaultCatalog().getActiveCatalogVersion();

		final VariantTypeModel variantTypeModel = createVariantTypeModel();
		final ProductModel product = createProductModel(activeCatalogVersion, variantTypeModel);

		modelService.saveAll();
		final VariantProductModel variantProduct = createVariantProductModel(activeCatalogVersion, variantTypeModel, product);
		modelService.saveAll();

		modelService.detachAll();

		final List<ItemModel> objectsToDelete = new LinkedList<>();
		objectsToDelete.add(product);
		objectsToDelete.add(variantProduct);

		// when
		platformObjectFacadeStrategy.delete(objectsToDelete, null);
		platformObjectFacadeStrategy.delete(objectsToDelete, null);

		// then
		assertThat(modelService.isRemoved(product)).isTrue();
		assertThat(modelService.isRemoved(variantProduct)).isTrue();
	}

	private VariantTypeModel createVariantTypeModel()
	{
		final VariantTypeModel variantTypeModel = modelService.create(VariantTypeModel._TYPECODE);
		variantTypeModel.setCode("variantTypeCode");
		variantTypeModel.setSingleton(Boolean.FALSE);
		variantTypeModel.setGenerate(Boolean.TRUE);
		variantTypeModel.setCatalogItemType(Boolean.FALSE);
		return variantTypeModel;
	}

	private ProductModel createProductModel(final CatalogVersionModel activeCatalogVersion, final VariantTypeModel variantTypeModel)
	{
		final ProductModel product = modelService.create(ProductModel._TYPECODE);
		product.setCatalogVersion(activeCatalogVersion);
		product.setCode(Long.toString(System.currentTimeMillis(), 24));

		product.setVariantType(variantTypeModel);
		return product;
	}

	private VariantProductModel createVariantProductModel(final CatalogVersionModel activeCatalogVersion,
														  final VariantTypeModel variantTypeModel, final ProductModel product)
	{
		final VariantProductModel variantProduct = modelService.create(VariantProductModel._TYPECODE);
		variantProduct.setBaseProduct(product);
		variantProduct.setVariantType(variantTypeModel);
		variantProduct.setCatalogVersion(activeCatalogVersion);
		variantProduct.setCode(Long.toString(System.currentTimeMillis(), 24));
		return variantProduct;
	}
}
