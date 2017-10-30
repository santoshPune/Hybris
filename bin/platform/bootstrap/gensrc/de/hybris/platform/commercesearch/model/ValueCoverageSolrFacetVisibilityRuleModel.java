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
import de.hybris.platform.commercesearch.model.AbstractSolrFacetVisibilityRuleModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

/**
 * Generated model class for type ValueCoverageSolrFacetVisibilityRule first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class ValueCoverageSolrFacetVisibilityRuleModel extends AbstractSolrFacetVisibilityRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ValueCoverageSolrFacetVisibilityRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>ValueCoverageSolrFacetVisibilityRule.coveragePercentage</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String COVERAGEPERCENTAGE = "coveragePercentage";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ValueCoverageSolrFacetVisibilityRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ValueCoverageSolrFacetVisibilityRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _coveragePercentage initial attribute declared by type <code>ValueCoverageSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public ValueCoverageSolrFacetVisibilityRuleModel(final String _code, final Integer _coveragePercentage, final SolrIndexedPropertyModel _facet)
	{
		super();
		setCode(_code);
		setCoveragePercentage(_coveragePercentage);
		setFacet(_facet);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _coveragePercentage initial attribute declared by type <code>ValueCoverageSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _facet initial attribute declared by type <code>AbstractSolrFacetVisibilityRule</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ValueCoverageSolrFacetVisibilityRuleModel(final String _code, final Integer _coveragePercentage, final SolrIndexedPropertyModel _facet, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setCoveragePercentage(_coveragePercentage);
		setFacet(_facet);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ValueCoverageSolrFacetVisibilityRule.coveragePercentage</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the coveragePercentage
	 */
	@Accessor(qualifier = "coveragePercentage", type = Accessor.Type.GETTER)
	public Integer getCoveragePercentage()
	{
		return getPersistenceContext().getPropertyValue(COVERAGEPERCENTAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ValueCoverageSolrFacetVisibilityRule.coveragePercentage</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the coveragePercentage
	 */
	@Accessor(qualifier = "coveragePercentage", type = Accessor.Type.SETTER)
	public void setCoveragePercentage(final Integer value)
	{
		getPersistenceContext().setPropertyValue(COVERAGEPERCENTAGE, value);
	}
	
}
