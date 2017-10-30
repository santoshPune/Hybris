/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 12:11:59 PM                    ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.commercesearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

/**
 * Generated model class for type SolrHeroProductDefinition first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class SolrHeroProductDefinitionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrHeroProductDefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrHeroProductDefinition.category</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CATEGORY = "category";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrHeroProductDefinition.indexedType</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrHeroProductDefinition.code</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrHeroProductDefinition.catalogVersion</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrHeroProductDefinition.products</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String PRODUCTS = "products";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrHeroProductDefinitionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrHeroProductDefinitionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _category initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _code initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _indexedType initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SolrHeroProductDefinitionModel(final CatalogVersionModel _catalogVersion, final CategoryModel _category, final String _code, final SolrIndexedTypeModel _indexedType)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCategory(_category);
		setCode(_code);
		setIndexedType(_indexedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _category initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _code initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _indexedType initial attribute declared by type <code>SolrHeroProductDefinition</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SolrHeroProductDefinitionModel(final CatalogVersionModel _catalogVersion, final CategoryModel _category, final String _code, final SolrIndexedTypeModel _indexedType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCategory(_category);
		setCode(_code);
		setIndexedType(_indexedType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrHeroProductDefinition.catalogVersion</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrHeroProductDefinition.category</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.GETTER)
	public CategoryModel getCategory()
	{
		return getPersistenceContext().getPropertyValue(CATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrHeroProductDefinition.code</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrHeroProductDefinition.indexedType</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrHeroProductDefinition.products</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public List<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrHeroProductDefinition.catalogVersion</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrHeroProductDefinition.category</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.SETTER)
	public void setCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(CATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrHeroProductDefinition.code</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrHeroProductDefinition.indexedType</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrHeroProductDefinition.products</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final List<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
}
