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
import de.hybris.platform.commercesearch.model.AbstractSolrSearchProfileModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrIndexedPropertyFacetType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import java.util.Collection;

/**
 * Generated model class for type SolrFacetReconfiguration first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class SolrFacetReconfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrFacetReconfiguration";
	
	/**<i>Generated relation code constant for relation <code>SolrIndexedProperty2SolrFacetReconfiguration</code> defining source attribute <code>facet</code> in extension <code>commercesearch</code>.</i>*/
	public final static String _SOLRINDEXEDPROPERTY2SOLRFACETRECONFIGURATION = "SolrIndexedProperty2SolrFacetReconfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.customFacetSortProvider</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String CUSTOMFACETSORTPROVIDER = "customFacetSortProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.facetType</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String FACETTYPE = "facetType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.priority</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.visible</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String VISIBLE = "visible";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.description</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.facetPOS</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String FACETPOS = "facetPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.facet</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String FACET = "facet";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetReconfiguration.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SOLRSEARCHPROFILES = "solrSearchProfiles";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrFacetReconfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrFacetReconfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _facet initial attribute declared by type <code>SolrFacetReconfiguration</code> at extension <code>commercesearch</code>
	 * @param _solrSearchProfiles initial attribute declared by type <code>SolrFacetReconfiguration</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SolrFacetReconfigurationModel(final SolrIndexedPropertyModel _facet, final Collection<AbstractSolrSearchProfileModel> _solrSearchProfiles)
	{
		super();
		setFacet(_facet);
		setSolrSearchProfiles(_solrSearchProfiles);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _facet initial attribute declared by type <code>SolrFacetReconfiguration</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _solrSearchProfiles initial attribute declared by type <code>SolrFacetReconfiguration</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SolrFacetReconfigurationModel(final SolrIndexedPropertyModel _facet, final ItemModel _owner, final Collection<AbstractSolrSearchProfileModel> _solrSearchProfiles)
	{
		super();
		setFacet(_facet);
		setOwner(_owner);
		setSolrSearchProfiles(_solrSearchProfiles);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.customFacetSortProvider</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the customFacetSortProvider - Facet sort values are facet being dependent
	 */
	@Accessor(qualifier = "customFacetSortProvider", type = Accessor.Type.GETTER)
	public String getCustomFacetSortProvider()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMFACETSORTPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.description</code> dynamic attribute defined at extension <code>commercesearch</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getDynamicValue(this,DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.facet</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getFacet()
	{
		return getPersistenceContext().getPropertyValue(FACET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.facetType</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyFacetType getFacetType()
	{
		return getPersistenceContext().getPropertyValue(FACETTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.priority</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the solrSearchProfiles
	 */
	@Accessor(qualifier = "solrSearchProfiles", type = Accessor.Type.GETTER)
	public Collection<AbstractSolrSearchProfileModel> getSolrSearchProfiles()
	{
		return getPersistenceContext().getPropertyValue(SOLRSEARCHPROFILES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetReconfiguration.visible</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the visible - This property is used to define whether a facet should be visible for frontend users or not.
	 */
	@Accessor(qualifier = "visible", type = Accessor.Type.GETTER)
	public boolean isVisible()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(VISIBLE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.customFacetSortProvider</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the customFacetSortProvider - Facet sort values are facet being dependent
	 */
	@Accessor(qualifier = "customFacetSortProvider", type = Accessor.Type.SETTER)
	public void setCustomFacetSortProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMFACETSORTPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.facet</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.SETTER)
	public void setFacet(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(FACET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.facetType</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.SETTER)
	public void setFacetType(final SolrIndexedPropertyFacetType value)
	{
		getPersistenceContext().setPropertyValue(FACETTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.priority</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the solrSearchProfiles
	 */
	@Accessor(qualifier = "solrSearchProfiles", type = Accessor.Type.SETTER)
	public void setSolrSearchProfiles(final Collection<AbstractSolrSearchProfileModel> value)
	{
		getPersistenceContext().setPropertyValue(SOLRSEARCHPROFILES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetReconfiguration.visible</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the visible - This property is used to define whether a facet should be visible for frontend users or not.
	 */
	@Accessor(qualifier = "visible", type = Accessor.Type.SETTER)
	public void setVisible(final boolean value)
	{
		getPersistenceContext().setPropertyValue(VISIBLE, toObject(value));
	}
	
}
