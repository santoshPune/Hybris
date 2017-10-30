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
import de.hybris.platform.commercesearch.model.SolrBoostRuleModel;
import de.hybris.platform.commercesearch.model.SolrFacetReconfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;

/**
 * Generated model class for type AbstractSolrSearchProfile first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class AbstractSolrSearchProfileModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractSolrSearchProfile";
	
	/**<i>Generated relation code constant for relation <code>SolrFacetReconfiguration2SolrSearchProfile</code> defining source attribute <code>solrFacetReconfigurations</code> in extension <code>commercesearch</code>.</i>*/
	public final static String _SOLRFACETRECONFIGURATION2SOLRSEARCHPROFILE = "SolrFacetReconfiguration2SolrSearchProfile";
	
	/**<i>Generated relation code constant for relation <code>SolrBoostRule2SolrSearchProfile</code> defining source attribute <code>boostRules</code> in extension <code>commercesearch</code>.</i>*/
	public final static String _SOLRBOOSTRULE2SOLRSEARCHPROFILE = "SolrBoostRule2SolrSearchProfile";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSearchProfile.code</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSearchProfile.indexedType</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSearchProfile.description</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSearchProfile.solrFacetReconfigurations</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SOLRFACETRECONFIGURATIONS = "solrFacetReconfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSearchProfile.boostRules</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String BOOSTRULES = "boostRules";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractSolrSearchProfileModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractSolrSearchProfileModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrSearchProfile</code> at extension <code>commercesearch</code>
	 * @param _indexedType initial attribute declared by type <code>AbstractSolrSearchProfile</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public AbstractSolrSearchProfileModel(final String _code, final SolrIndexedTypeModel _indexedType)
	{
		super();
		setCode(_code);
		setIndexedType(_indexedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractSolrSearchProfile</code> at extension <code>commercesearch</code>
	 * @param _indexedType initial attribute declared by type <code>AbstractSolrSearchProfile</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractSolrSearchProfileModel(final String _code, final SolrIndexedTypeModel _indexedType, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setIndexedType(_indexedType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSearchProfile.boostRules</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the boostRules
	 */
	@Accessor(qualifier = "boostRules", type = Accessor.Type.GETTER)
	public Collection<SolrBoostRuleModel> getBoostRules()
	{
		return getPersistenceContext().getPropertyValue(BOOSTRULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSearchProfile.code</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSearchProfile.description</code> dynamic attribute defined at extension <code>commercesearch</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getDynamicValue(this,DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSearchProfile.indexedType</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSearchProfile.solrFacetReconfigurations</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the solrFacetReconfigurations
	 */
	@Accessor(qualifier = "solrFacetReconfigurations", type = Accessor.Type.GETTER)
	public Collection<SolrFacetReconfigurationModel> getSolrFacetReconfigurations()
	{
		return getPersistenceContext().getPropertyValue(SOLRFACETRECONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrSearchProfile.boostRules</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the boostRules
	 */
	@Accessor(qualifier = "boostRules", type = Accessor.Type.SETTER)
	public void setBoostRules(final Collection<SolrBoostRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(BOOSTRULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrSearchProfile.code</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrSearchProfile.indexedType</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrSearchProfile.solrFacetReconfigurations</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the solrFacetReconfigurations
	 */
	@Accessor(qualifier = "solrFacetReconfigurations", type = Accessor.Type.SETTER)
	public void setSolrFacetReconfigurations(final Collection<SolrFacetReconfigurationModel> value)
	{
		getPersistenceContext().setPropertyValue(SOLRFACETRECONFIGURATIONS, value);
	}
	
}
