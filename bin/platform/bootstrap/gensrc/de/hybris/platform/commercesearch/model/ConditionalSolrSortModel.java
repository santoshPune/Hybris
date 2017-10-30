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
import de.hybris.platform.commercesearch.model.AbstractSolrSortConditionModel;
import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.List;

/**
 * Generated model class for type ConditionalSolrSort first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class ConditionalSolrSortModel extends SolrSortModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConditionalSolrSort";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConditionalSolrSort.conditions</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CONDITIONS = "conditions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConditionalSolrSortModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConditionalSolrSortModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SolrSort</code> at extension <code>commerceservices</code>
	 * @param _indexedType initial attribute declared by type <code>SolrSort</code> at extension <code>commerceservices</code>
	 */
	@Deprecated
	public ConditionalSolrSortModel(final String _code, final SolrIndexedTypeModel _indexedType)
	{
		super();
		setCode(_code);
		setIndexedType(_indexedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SolrSort</code> at extension <code>commerceservices</code>
	 * @param _indexedType initial attribute declared by type <code>SolrSort</code> at extension <code>commerceservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConditionalSolrSortModel(final String _code, final SolrIndexedTypeModel _indexedType, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setIndexedType(_indexedType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConditionalSolrSort.conditions</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public List<AbstractSolrSortConditionModel> getConditions()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConditionalSolrSort.conditions</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final List<AbstractSolrSortConditionModel> value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONS, value);
	}
	
}
