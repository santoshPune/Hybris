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
package de.hybris.platform.printcockpitnew.services.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.printcockpitnew.exceptions.TableManagerPropertyException;
import de.hybris.platform.printcockpitnew.exceptions.UncategorizedProductsException;
import de.hybris.platform.printcockpitnew.services.CatalogNavigationDao;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class DefaultCatalogNavigationServiceTest extends ServicelayerTransactionalTest
{
	public static final Logger LOG = Logger.getLogger(DefaultCatalogNavigationServiceTest.class.getName());

	// Strings
	private static final String CATALOG_NAVIGATION_SERVICE = "catalogNavigationService";
	private static final String CATALOG_NAVIGATION_DAO = "catalogNavigationDao";
	private static final String COCKPIT_TYPE_SERVICE = "cockpitTypeService";
	private static final String FLEXIBLE_SEARCH_SERVICE = "flexibleSearchService";
	private static final String MODEL_SERVICE = "modelService";
	private static final String DEMO = "Demo";
	private static final String TEST = "Test";

	// Services
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private VariantsService variantsService;
	@Resource
	private de.hybris.platform.servicelayer.type.impl.DefaultTypeService defaultTypeService;

	private CatalogNavigationDao catalogNavigationDao;
	private de.hybris.platform.cockpit.services.meta.impl.DefaultTypeService typeService;
	private DefaultCatalogNavigationService catalogNavigationService;

	// Models
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp()
	{
		// Add bean properties
		final HashMap<String, Object> attributes = new HashMap<>();
		attributes.put(FLEXIBLE_SEARCH_SERVICE, flexibleSearchService);
		attributes.put(MODEL_SERVICE, modelService);

		// Register Service from Web Application Context to Global Application Context
		this.registerBeanToGlobalApplicationContext(CATALOG_NAVIGATION_DAO, DefaultCatalogNavigationDao.class, attributes);
		this.registerBeanToGlobalApplicationContext(CATALOG_NAVIGATION_SERVICE, DefaultCatalogNavigationService.class, null);
		this.registerBeanToGlobalApplicationContext(COCKPIT_TYPE_SERVICE,
				de.hybris.platform.cockpit.services.meta.impl.DefaultTypeService.class, null);

		typeService = (de.hybris.platform.cockpit.services.meta.impl.DefaultTypeService) Registry.getApplicationContext()
				.getBean(COCKPIT_TYPE_SERVICE);
		typeService.setTypeService(defaultTypeService);
		typeService.setModelService(modelService);

		catalogNavigationDao = (CatalogNavigationDao) Registry.getApplicationContext().getBean(CATALOG_NAVIGATION_DAO);

		catalogNavigationService = (DefaultCatalogNavigationService) Registry.getApplicationContext()
				.getBean(CATALOG_NAVIGATION_SERVICE);
		catalogNavigationService.setCatalogNavigationDao(catalogNavigationDao);
		catalogNavigationService.setTypeService(typeService);

		// Create demo catalog version
		this.createDemoCatalogVersion();
		// Create 10 uncategorized products
		this.createUncategoriedProducts();
	}

	@Test
	public void testFindProducts()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {" + ItemModel.PK + "} FROM {" + ProductModel._TYPECODE + "} WHERE {" + ProductModel.CODE + "}!='-1' AND {"
						+ CatalogVersionModel._TYPECODE + "}=?" + CatalogVersionModel._TYPECODE + "");
		query.addQueryParameter(CatalogVersionModel._TYPECODE, catalogVersion);
		final List<Object> result = flexibleSearchService.search(query).getResult();

		Assert.assertFalse(org.fest.util.Collections.isEmpty(result));
	}

	@Test
	public void testFindUncategorizedProducts()
	{
		final List<TypedObject> uncategorizedProducts = this.catalogNavigationService.findUncategorizedProducts(catalogVersion);

		Assert.assertNotNull(uncategorizedProducts);
	}

	@Test(expected = UncategorizedProductsException.class)
	public void testMoreUncategorizedProductsThenThreshold() throws UncategorizedProductsException, TableManagerPropertyException
	{
		final SearchResult<ProductModel> searchResult = catalogNavigationDao.findUncategorizedProducts(catalogVersion);
		final int maxCount = 1;
		this.catalogNavigationService.checkSizeOfSearchResult(searchResult, maxCount);
		Assert.fail("Should have thrown an UncategorizedProductsException because searchResult.getTotalCount(): ("
				+ searchResult.getTotalCount() + ") > maxCount: (" + maxCount + ")!");
	}

	@Ignore
	private void registerBeanToGlobalApplicationContext(final String beanId, final Class<? extends Object> classObj,
			final HashMap<String, Object> attributes)
	{
		final AbstractBeanDefinition validationDefinition = BeanDefinitionBuilder.rootBeanDefinition(classObj).getBeanDefinition();

		if (attributes != null)
		{
			final MutablePropertyValues propertyValues = new MutablePropertyValues();
			final Set<String> keys = attributes.keySet();

			for (final String key : keys)
			{
				propertyValues.add(key, attributes.get(key));
			}

			validationDefinition.setPropertyValues(propertyValues);
		}

		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		beanFactory.registerBeanDefinition(beanId, validationDefinition);
	}

	@Ignore
	private void createDemoCatalogVersion()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(DEMO);
		modelService.save(catalog);

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setActive(Boolean.TRUE);
		catalogVersion.setVersion(TEST);
		modelService.save(catalogVersion);

		catalogVersionService.addSessionCatalogVersion(catalogVersion);
	}

	@Ignore
	private void createUncategoriedProducts()
	{
		final VariantTypeModel variantTypeModel = variantsService.getVariantTypeForCode(VariantProductModel._TYPECODE);

		for (int i = 0; i < 10; i++)
		{
			final ProductModel productModel = modelService.create(ProductModel.class);
			productModel.setCode("" + i);
			productModel.setCatalogVersion(catalogVersion);
			productModel.setVariantType(variantTypeModel);
			modelService.save(productModel);
		}
	}
}
