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
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercesearch.model.AbstractSolrSortConditionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SelectedCategoryHierarchySolrSortCondition first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class SelectedCategoryHierarchySolrSortConditionModel extends AbstractSolrSortConditionModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SelectedCategoryHierarchySolrSortCondition";
	
	/** <i>Generated constant</i> - Attribute key of <code>SelectedCategoryHierarchySolrSortCondition.rootCategory</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String ROOTCATEGORY = "rootCategory";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SelectedCategoryHierarchySolrSortConditionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SelectedCategoryHierarchySolrSortConditionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _rootCategory initial attribute declared by type <code>SelectedCategoryHierarchySolrSortCondition</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SelectedCategoryHierarchySolrSortConditionModel(final CategoryModel _rootCategory)
	{
		super();
		setRootCategory(_rootCategory);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _rootCategory initial attribute declared by type <code>SelectedCategoryHierarchySolrSortCondition</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SelectedCategoryHierarchySolrSortConditionModel(final ItemModel _owner, final CategoryModel _rootCategory)
	{
		super();
		setOwner(_owner);
		setRootCategory(_rootCategory);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SelectedCategoryHierarchySolrSortCondition.rootCategory</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the rootCategory
	 */
	@Accessor(qualifier = "rootCategory", type = Accessor.Type.GETTER)
	public CategoryModel getRootCategory()
	{
		return getPersistenceContext().getPropertyValue(ROOTCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SelectedCategoryHierarchySolrSortCondition.rootCategory</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the rootCategory
	 */
	@Accessor(qualifier = "rootCategory", type = Accessor.Type.SETTER)
	public void setRootCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(ROOTCATEGORY, value);
	}
	
}
