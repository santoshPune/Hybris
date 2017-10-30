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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractSolrSortCondition first defined at extension commercesearch.
 */
@SuppressWarnings("all")
public class AbstractSolrSortConditionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractSolrSortCondition";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSortCondition.inverse</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String INVERSE = "inverse";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractSolrSortCondition.description</code> attribute defined at extension <code>commercesearch</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractSolrSortConditionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractSolrSortConditionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractSolrSortConditionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSortCondition.description</code> dynamic attribute defined at extension <code>commercesearch</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getDynamicValue(this,DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSolrSortCondition.inverse</code> attribute defined at extension <code>commercesearch</code>. 
	 * @return the inverse
	 */
	@Accessor(qualifier = "inverse", type = Accessor.Type.GETTER)
	public Boolean getInverse()
	{
		return getPersistenceContext().getPropertyValue(INVERSE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractSolrSortCondition.inverse</code> attribute defined at extension <code>commercesearch</code>. 
	 *  
	 * @param value the inverse
	 */
	@Accessor(qualifier = "inverse", type = Accessor.Type.SETTER)
	public void setInverse(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INVERSE, value);
	}
	
}
