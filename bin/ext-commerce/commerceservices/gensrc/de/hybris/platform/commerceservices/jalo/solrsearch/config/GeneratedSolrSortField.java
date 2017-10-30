/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 11:21:07 AM                    ---
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
package de.hybris.platform.commerceservices.jalo.solrsearch.config;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.jalo.solrsearch.config.SolrSort;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.solrsearch.config.SolrSortField SolrSortField}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSolrSortField extends GenericItem
{
	/** Qualifier of the <code>SolrSortField.fieldName</code> attribute **/
	public static final String FIELDNAME = "fieldName";
	/** Qualifier of the <code>SolrSortField.ascending</code> attribute **/
	public static final String ASCENDING = "ascending";
	/** Qualifier of the <code>SolrSortField.sortPOS</code> attribute **/
	public static final String SORTPOS = "sortPOS";
	/** Qualifier of the <code>SolrSortField.sort</code> attribute **/
	public static final String SORT = "sort";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SORT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSolrSortField> SORTHANDLER = new BidirectionalOneToManyHandler<GeneratedSolrSortField>(
	CommerceServicesConstants.TC.SOLRSORTFIELD,
	false,
	"sort",
	"sortPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(FIELDNAME, AttributeMode.INITIAL);
		tmp.put(ASCENDING, AttributeMode.INITIAL);
		tmp.put(SORTPOS, AttributeMode.INITIAL);
		tmp.put(SORT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.ascending</code> attribute.
	 * @return the ascending - Is this field sorted ascending.
	 */
	public Boolean isAscending(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ASCENDING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.ascending</code> attribute.
	 * @return the ascending - Is this field sorted ascending.
	 */
	public Boolean isAscending()
	{
		return isAscending( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.ascending</code> attribute. 
	 * @return the ascending - Is this field sorted ascending.
	 */
	public boolean isAscendingAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAscending( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.ascending</code> attribute. 
	 * @return the ascending - Is this field sorted ascending.
	 */
	public boolean isAscendingAsPrimitive()
	{
		return isAscendingAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.ascending</code> attribute. 
	 * @param value the ascending - Is this field sorted ascending.
	 */
	public void setAscending(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ASCENDING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.ascending</code> attribute. 
	 * @param value the ascending - Is this field sorted ascending.
	 */
	public void setAscending(final Boolean value)
	{
		setAscending( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.ascending</code> attribute. 
	 * @param value the ascending - Is this field sorted ascending.
	 */
	public void setAscending(final SessionContext ctx, final boolean value)
	{
		setAscending( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.ascending</code> attribute. 
	 * @param value the ascending - Is this field sorted ascending.
	 */
	public void setAscending(final boolean value)
	{
		setAscending( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SORTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.fieldName</code> attribute.
	 * @return the fieldName - The field name to sort with.
	 */
	public String getFieldName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, FIELDNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.fieldName</code> attribute.
	 * @return the fieldName - The field name to sort with.
	 */
	public String getFieldName()
	{
		return getFieldName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.fieldName</code> attribute. 
	 * @param value the fieldName - The field name to sort with.
	 */
	public void setFieldName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, FIELDNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.fieldName</code> attribute. 
	 * @param value the fieldName - The field name to sort with.
	 */
	public void setFieldName(final String value)
	{
		setFieldName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sort</code> attribute.
	 * @return the sort
	 */
	public SolrSort getSort(final SessionContext ctx)
	{
		return (SolrSort)getProperty( ctx, SORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sort</code> attribute.
	 * @return the sort
	 */
	public SolrSort getSort()
	{
		return getSort( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sort</code> attribute. 
	 * @param value the sort
	 */
	public void setSort(final SessionContext ctx, final SolrSort value)
	{
		SORTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sort</code> attribute. 
	 * @param value the sort
	 */
	public void setSort(final SolrSort value)
	{
		setSort( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sortPOS</code> attribute.
	 * @return the sortPOS
	 */
	 Integer getSortPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, SORTPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sortPOS</code> attribute.
	 * @return the sortPOS
	 */
	 Integer getSortPOS()
	{
		return getSortPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @return the sortPOS
	 */
	 int getSortPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getSortPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @return the sortPOS
	 */
	 int getSortPOSAsPrimitive()
	{
		return getSortPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @param value the sortPOS
	 */
	 void setSortPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, SORTPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @param value the sortPOS
	 */
	 void setSortPOS(final Integer value)
	{
		setSortPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @param value the sortPOS
	 */
	 void setSortPOS(final SessionContext ctx, final int value)
	{
		setSortPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSortField.sortPOS</code> attribute. 
	 * @param value the sortPOS
	 */
	 void setSortPOS(final int value)
	{
		setSortPOS( getSession().getSessionContext(), value );
	}
	
}
