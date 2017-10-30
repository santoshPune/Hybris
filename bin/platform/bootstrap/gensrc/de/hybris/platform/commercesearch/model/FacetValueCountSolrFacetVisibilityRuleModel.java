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
import de.hybris.platform.commercesearch.enums.FacetSelectedState;
import de.hybris.platform.commercesearch.enums.FacetValueCountOperator;
import de.hybris.platform.commercesearch.model.AbstractSolrFacetVisibilityRuleModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import java.util.Set;

/**
 * Generated model class for type FacetValueCountSolrFacetVisibilityRule first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class FacetValueCountSolrFacetVisibilityRuleModel extends AbstractSolrFacetVisibilityRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "FacetValueCountSolrFacetVisibilityRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>FacetValueCountSolrFacetVisibilityRule.operator</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String OPERATOR = "operator";
	
	/** <i>Generated constant</i> - Attribute key of <code>FacetValueCountSolrFacetVisibilityRule.count</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String COUNT = "count";
	
	/** <i>Generated constant</i> - Attribute key of <code>FacetValueCountSolrFacetVisibilityRule.selectedStates</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SELECTEDSTATES = "selectedStates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FacetValueCountSolrFacetVisibilityRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FacetValueCountSolrFacetVisibilityRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _count initial attribute declared by type <code>FacetValueCountSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public FacetValueCountSolrFacetVisibilityRuleModel(final String _code, final Integer _count, final SolrIndexedPropertyModel _facet)
	{
		super();
		setCode(_code);
		setCount(_count);
		setFacet(_facet);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _count initial attribute declared by type <code>FacetValueCountSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public FacetValueCountSolrFacetVisibilityRuleModel(final String _code, final Integer _count, final SolrIndexedPropertyModel _facet, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setCount(_count);
		setFacet(_facet);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FacetValueCountSolrFacetVisibilityRule.count</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.GETTER)
	public Integer getCount()
	{
		return getPersistenceContext().getPropertyValue(COUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FacetValueCountSolrFacetVisibilityRule.operator</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.GETTER)
	public FacetValueCountOperator getOperator()
	{
		return getPersistenceContext().getPropertyValue(OPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FacetValueCountSolrFacetVisibilityRule.selectedStates</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the selectedStates
	 */
	@Accessor(qualifier = "selectedStates", type = Accessor.Type.GETTER)
	public Set<FacetSelectedState> getSelectedStates()
	{
		return getPersistenceContext().getPropertyValue(SELECTEDSTATES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FacetValueCountSolrFacetVisibilityRule.count</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.SETTER)
	public void setCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(COUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FacetValueCountSolrFacetVisibilityRule.operator</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.SETTER)
	public void setOperator(final FacetValueCountOperator value)
	{
		getPersistenceContext().setPropertyValue(OPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FacetValueCountSolrFacetVisibilityRule.selectedStates</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the selectedStates
	 */
	@Accessor(qualifier = "selectedStates", type = Accessor.Type.SETTER)
	public void setSelectedStates(final Set<FacetSelectedState> value)
	{
		getPersistenceContext().setPropertyValue(SELECTEDSTATES, value);
	}
	
}
