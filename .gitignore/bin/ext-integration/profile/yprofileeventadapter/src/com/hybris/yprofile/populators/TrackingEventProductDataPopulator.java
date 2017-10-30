/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.hybris.yprofile.populators;

import com.hybris.yprofile.dto.Category;
import com.hybris.yprofile.dto.TrackingEvent;
import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

/**
 * TrackingEventProductDataPopulator provides additional data regarding the product and its category structure
 * for ProductDetailPageViewEvent
 */
public class TrackingEventProductDataPopulator implements Populator<TrackingEventCsvData , TrackingEvent> {

    public static final String PRODUCT_DETAIL_PAGE_VIEW_EVENT = "ProductDetailPageViewEvent";
    private static final Logger LOG = Logger.getLogger(TrackingEventProductDataPopulator.class);
    private BaseSiteService baseSiteService;
    private CatalogVersionService catalogVersionService;
    private CategoryService categoryService;
    private ProductService productService;
    private ProfileConfigurationService profileConfigurationService;

    @Override
    public void populate(TrackingEventCsvData source, TrackingEvent target) throws ConversionException {

        if (shouldPopulateProductData(source)){

            try {
                setupSiteAndCatalogVersion();

                ProductModel productModel = getProductService().getProductForCode(source.getProductId());

                target.setCategories(getCategories(productModel));
                target.setPrice(source.getProductPrice());
                target.setBrand(getBrand(productModel));
            } catch (Throwable t){
                LOG.error("Error retrieving product information", t);
            }
        }

    }

    private boolean shouldPopulateProductData(TrackingEventCsvData source) {
        return StringUtils.isNotBlank(source.getProductId()) &&
                PRODUCT_DETAIL_PAGE_VIEW_EVENT.equals(source.getEventType()) &&
                StringUtils.isNotBlank(getSiteId());
    }

    private List<Category> getCategories(ProductModel productModel) {

        Collection<CategoryModel> categories = productModel.getSupercategories();

        Set<Category> categoryList = new HashSet<Category>();
        categories.forEach(categoryModel -> {
            categoryList.add(getCategory(categoryModel));

            List<CategoryModel> pathForCategory = getCategoryService().getPathForCategory(categoryModel);

            pathForCategory.forEach(parentCategoryModel ->
                    categoryList.add(getCategory(parentCategoryModel))
            );
        });

        return new ArrayList(categoryList);
    }

    private Category getCategory(CategoryModel categoryModel) {
        Category category = new Category();
        category.setCode(categoryModel.getCode());
        category.setName(categoryModel.getName());
        category.setParentId(getParentCategoryId(categoryModel));
        return category;
    }

    private String getParentCategoryId(CategoryModel categoryModel){

        Iterator<CategoryModel> categoryModelIterator = categoryModel.getSupercategories().iterator();
        if (categoryModelIterator.hasNext()){
            CategoryModel parentCategory = categoryModelIterator.next();

            if (parentCategory != null){
                return parentCategory.getCode();
            }

        }
        return null;
    }

    private String getBrand(ProductModel productModel) {
        return productModel.getManufacturerName();
    }

    /**
     * Sets up the base site id in session to get the correct catalog information
     */
    private void setupSiteAndCatalogVersion() {

        getBaseSiteService().setCurrentBaseSite(getSiteId(), true);
        BaseSiteModel baseSiteModel = getBaseSiteService().getCurrentBaseSite();

        List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs(baseSiteModel);

        if (!productCatalogs.isEmpty()){

            CatalogModel catalogModel = productCatalogs.iterator().next();
            CatalogVersionModel activeCatalogVersion = catalogModel.getActiveCatalogVersion();
            getCatalogVersionService().setSessionCatalogVersion(catalogModel.getId(), activeCatalogVersion.getVersion());
        }
    }

    private String getSiteId(){
        return getProfileConfigurationService().getBaseSiteId();
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    @Required
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ProductService getProductService() {
        return productService;
    }

    @Required
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
