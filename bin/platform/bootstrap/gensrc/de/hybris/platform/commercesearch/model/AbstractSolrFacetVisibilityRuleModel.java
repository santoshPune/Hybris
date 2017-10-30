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
import de.hybris.platform.commercesearch.enums.FacetVisibilityRuleCondition;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

/**
 * Generated model class for type AbstractSolrFacetVisibilityRule first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class AbstractSolrFacetVisibilityRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractSolrFacetVisibilityRule";
	
	/**<i>Generated relation code constant for relation <code>SolrIndexedProperty2FacetVisibilityRules</code> defining source attribute <code>facet</code> in extension <code>commercesearch</code>.</i>*/
	public final static String _SOLRINDEXEDPROPERTY2FACETVISIBILITYRULES = "SolrIndexedProperty2FacetVisibilityRules";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrFacetVisibilityRule.code</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrFacetVisibilityRule.name</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrFacetVisibilityRule.condition</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CONDITION = "condition";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrFacetVisibilityRule.facetPOS</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String FACETPOS = "facetPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrFacetVisibilityRule.facet</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String FACET = "facet";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractSolrFacetVisibilityRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractSolrFacetVisibilityRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public AbstractSolrFacetVisibilityRuleModel(final String _code, final SolrIndexedPropertyModel _facet)
	{
		super();
		setCode(_code);
		setFacet(_facet);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractSolrFacetVisibilityRuleModel(final String _code, final SolrIndexedPropertyModel _facet, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setFacet(_facet);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrFacetVisibilityRule.code</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrFacetVisibilityRule.condition</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the condition
	 */
	@Accessor(qualifier = "condition", type = Accessor.Type.GETTER)
	public FacetVisibilityRuleCondition getCondition()
	{
		return getPersistenceContext().getPropertyValue(CONDITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrFacetVisibilityRule.facet</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getFacet()
	{
		return getPersistenceContext().getPropertyValue(FACET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrFacetVisibilityRule.name</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrFacetVisibilityRule.code</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrFacetVisibilityRule.condition</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the condition
	 */
	@Accessor(qualifier = "condition", type = Accessor.Type.SETTER)
	public void setCondition(final FacetVisibilityRuleCondition value)
	{
		getPersistenceContext().setPropertyValue(CONDITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrFacetVisibilityRule.facet</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.SETTER)
	public void setFacet(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(FACET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrFacetVisibilityRule.name</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
}
