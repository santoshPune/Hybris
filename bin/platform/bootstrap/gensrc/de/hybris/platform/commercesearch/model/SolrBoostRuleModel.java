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
import de.hybris.platform.commercesearch.enums.SolrBoostConditionOperator;
import de.hybris.platform.commercesearch.model.AbstractSolrSearchProfileModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import java.util.Collection;

/**
 * Generated model class for type SolrBoostRule first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class SolrBoostRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrBoostRule";
	
	/**<i>Generated relation code constant for relation <code>SolrIndexedProperty2SolrBoostRuleRelation</code> defining source attribute <code>solrIndexedProperty</code> in extension <code>commercesearch</code>.</i>*/
	public final static String _SOLRINDEXEDPROPERTY2SOLRBOOSTRULERELATION = "SolrIndexedProperty2SolrBoostRuleRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.operator</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String OPERATOR = "operator";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.propertyValue</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String PROPERTYVALUE = "propertyValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.boostFactor</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String BOOSTFACTOR = "boostFactor";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.solrIndexedPropertyPOS</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SOLRINDEXEDPROPERTYPOS = "solrIndexedPropertyPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.solrIndexedProperty</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SOLRINDEXEDPROPERTY = "solrIndexedProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrBoostRule.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String SOLRSEARCHPROFILES = "solrSearchProfiles";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrBoostRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrBoostRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _boostFactor initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _propertyValue initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _solrIndexedProperty initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _solrSearchProfiles initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SolrBoostRuleModel(final int _boostFactor, final String _propertyValue, final SolrIndexedPropertyModel _solrIndexedProperty, final Collection<AbstractSolrSearchProfileModel> _solrSearchProfiles)
	{
		super();
		setBoostFactor(_boostFactor);
		setPropertyValue(_propertyValue);
		setSolrIndexedProperty(_solrIndexedProperty);
		setSolrSearchProfiles(_solrSearchProfiles);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _boostFactor initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _propertyValue initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _solrIndexedProperty initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 * @param _solrSearchProfiles initial attribute declared by type <code>SolrBoostRule</code> at extension <code>commercesearch</code>
	 */
	@Deprecated
	public SolrBoostRuleModel(final int _boostFactor, final ItemModel _owner, final String _propertyValue, final SolrIndexedPropertyModel _solrIndexedProperty, final Collection<AbstractSolrSearchProfileModel> _solrSearchProfiles)
	{
		super();
		setBoostFactor(_boostFactor);
		setOwner(_owner);
		setPropertyValue(_propertyValue);
		setSolrIndexedProperty(_solrIndexedProperty);
		setSolrSearchProfiles(_solrSearchProfiles);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrBoostRule.boostFactor</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the boostFactor
	 */
	@Accessor(qualifier = "boostFactor", type = Accessor.Type.GETTER)
	public int getBoostFactor()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(BOOSTFACTOR));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrBoostRule.operator</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.GETTER)
	public SolrBoostConditionOperator getOperator()
	{
		return getPersistenceContext().getPropertyValue(OPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrBoostRule.propertyValue</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the propertyValue
	 */
	@Accessor(qualifier = "propertyValue", type = Accessor.Type.GETTER)
	public String getPropertyValue()
	{
		return getPersistenceContext().getPropertyValue(PROPERTYVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrBoostRule.solrIndexedProperty</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the solrIndexedProperty
	 */
	@Accessor(qualifier = "solrIndexedProperty", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getSolrIndexedProperty()
	{
		return getPersistenceContext().getPropertyValue(SOLRINDEXEDPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrBoostRule.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the solrSearchProfiles
	 */
	@Accessor(qualifier = "solrSearchProfiles", type = Accessor.Type.GETTER)
	public Collection<AbstractSolrSearchProfileModel> getSolrSearchProfiles()
	{
		return getPersistenceContext().getPropertyValue(SOLRSEARCHPROFILES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrBoostRule.boostFactor</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the boostFactor
	 */
	@Accessor(qualifier = "boostFactor", type = Accessor.Type.SETTER)
	public void setBoostFactor(final int value)
	{
		getPersistenceContext().setPropertyValue(BOOSTFACTOR, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrBoostRule.operator</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.SETTER)
	public void setOperator(final SolrBoostConditionOperator value)
	{
		getPersistenceContext().setPropertyValue(OPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrBoostRule.propertyValue</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the propertyValue
	 */
	@Accessor(qualifier = "propertyValue", type = Accessor.Type.SETTER)
	public void setPropertyValue(final String value)
	{
		getPersistenceContext().setPropertyValue(PROPERTYVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrBoostRule.solrIndexedProperty</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the solrIndexedProperty
	 */
	@Accessor(qualifier = "solrIndexedProperty", type = Accessor.Type.SETTER)
	public void setSolrIndexedProperty(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(SOLRINDEXEDPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrBoostRule.solrSearchProfiles</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the solrSearchProfiles
	 */
	@Accessor(qualifier = "solrSearchProfiles", type = Accessor.Type.SETTER)
	public void setSolrSearchProfiles(final Collection<AbstractSolrSearchProfileModel> value)
	{
		getPersistenceContext().setPropertyValue(SOLRSEARCHPROFILES, value);
	}
	
}
