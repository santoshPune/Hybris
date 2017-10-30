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
import de.hybris.platform.commerceservices.jalo.solrsearch.config.SolrSortField;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.commerceservices.jalo.solrsearch.config.SolrSort SolrSort}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSolrSort extends GenericItem
{
	/** Qualifier of the <code>SolrSort.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>SolrSort.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SolrSort.useBoost</code> attribute **/
	public static final String USEBOOST = "useBoost";
	/** Qualifier of the <code>SolrSort.indexedTypePOS</code> attribute **/
	public static final String INDEXEDTYPEPOS = "indexedTypePOS";
	/** Qualifier of the <code>SolrSort.indexedType</code> attribute **/
	public static final String INDEXEDTYPE = "indexedType";
	/** Qualifier of the <code>SolrSort.fields</code> attribute **/
	public static final String FIELDS = "fields";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INDEXEDTYPE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSolrSort> INDEXEDTYPEHANDLER = new BidirectionalOneToManyHandler<GeneratedSolrSort>(
	CommerceServicesConstants.TC.SOLRSORT,
	false,
	"indexedType",
	"indexedTypePOS",
	true,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n FIELDS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SolrSortField> FIELDSHANDLER = new OneToManyHandler<SolrSortField>(
	CommerceServicesConstants.TC.SOLRSORTFIELD,
	true,
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
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(USEBOOST, AttributeMode.INITIAL);
		tmp.put(INDEXEDTYPEPOS, AttributeMode.INITIAL);
		tmp.put(INDEXEDTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.code</code> attribute.
	 * @return the code - The unique code used to identify the solr sort.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.code</code> attribute.
	 * @return the code - The unique code used to identify the solr sort.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.code</code> attribute. 
	 * @param value the code - The unique code used to identify the solr sort.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.code</code> attribute. 
	 * @param value the code - The unique code used to identify the solr sort.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		INDEXEDTYPEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.fields</code> attribute.
	 * @return the fields
	 */
	public List<SolrSortField> getFields(final SessionContext ctx)
	{
		return (List<SolrSortField>)FIELDSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.fields</code> attribute.
	 * @return the fields
	 */
	public List<SolrSortField> getFields()
	{
		return getFields( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.fields</code> attribute. 
	 * @param value the fields
	 */
	public void setFields(final SessionContext ctx, final List<SolrSortField> value)
	{
		FIELDSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.fields</code> attribute. 
	 * @param value the fields
	 */
	public void setFields(final List<SolrSortField> value)
	{
		setFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to fields. 
	 * @param value the item to add to fields
	 */
	public void addToFields(final SessionContext ctx, final SolrSortField value)
	{
		FIELDSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to fields. 
	 * @param value the item to add to fields
	 */
	public void addToFields(final SolrSortField value)
	{
		addToFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from fields. 
	 * @param value the item to remove from fields
	 */
	public void removeFromFields(final SessionContext ctx, final SolrSortField value)
	{
		FIELDSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from fields. 
	 * @param value the item to remove from fields
	 */
	public void removeFromFields(final SolrSortField value)
	{
		removeFromFields( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedType</code> attribute.
	 * @return the indexedType
	 */
	public SolrIndexedType getIndexedType(final SessionContext ctx)
	{
		return (SolrIndexedType)getProperty( ctx, INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedType</code> attribute.
	 * @return the indexedType
	 */
	public SolrIndexedType getIndexedType()
	{
		return getIndexedType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedType</code> attribute. 
	 * @param value the indexedType
	 */
	public void setIndexedType(final SessionContext ctx, final SolrIndexedType value)
	{
		INDEXEDTYPEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedType</code> attribute. 
	 * @param value the indexedType
	 */
	public void setIndexedType(final SolrIndexedType value)
	{
		setIndexedType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedTypePOS</code> attribute.
	 * @return the indexedTypePOS
	 */
	 Integer getIndexedTypePOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, INDEXEDTYPEPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedTypePOS</code> attribute.
	 * @return the indexedTypePOS
	 */
	 Integer getIndexedTypePOS()
	{
		return getIndexedTypePOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @return the indexedTypePOS
	 */
	 int getIndexedTypePOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getIndexedTypePOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @return the indexedTypePOS
	 */
	 int getIndexedTypePOSAsPrimitive()
	{
		return getIndexedTypePOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @param value the indexedTypePOS
	 */
	 void setIndexedTypePOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, INDEXEDTYPEPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @param value the indexedTypePOS
	 */
	 void setIndexedTypePOS(final Integer value)
	{
		setIndexedTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @param value the indexedTypePOS
	 */
	 void setIndexedTypePOS(final SessionContext ctx, final int value)
	{
		setIndexedTypePOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.indexedTypePOS</code> attribute. 
	 * @param value the indexedTypePOS
	 */
	 void setIndexedTypePOS(final int value)
	{
		setIndexedTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.name</code> attribute.
	 * @return the name - The display name for this sort.
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSolrSort.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.name</code> attribute.
	 * @return the name - The display name for this sort.
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.name</code> attribute. 
	 * @return the localized name - The display name for this sort.
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.name</code> attribute. 
	 * @return the localized name - The display name for this sort.
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.name</code> attribute. 
	 * @param value the name - The display name for this sort.
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSolrSort.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.name</code> attribute. 
	 * @param value the name - The display name for this sort.
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.name</code> attribute. 
	 * @param value the name - The display name for this sort.
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.name</code> attribute. 
	 * @param value the name - The display name for this sort.
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.useBoost</code> attribute.
	 * @return the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public Boolean isUseBoost(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, USEBOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.useBoost</code> attribute.
	 * @return the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public Boolean isUseBoost()
	{
		return isUseBoost( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.useBoost</code> attribute. 
	 * @return the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public boolean isUseBoostAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isUseBoost( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSort.useBoost</code> attribute. 
	 * @return the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public boolean isUseBoostAsPrimitive()
	{
		return isUseBoostAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.useBoost</code> attribute. 
	 * @param value the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public void setUseBoost(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, USEBOOST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.useBoost</code> attribute. 
	 * @param value the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public void setUseBoost(final Boolean value)
	{
		setUseBoost( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.useBoost</code> attribute. 
	 * @param value the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public void setUseBoost(final SessionContext ctx, final boolean value)
	{
		setUseBoost( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrSort.useBoost</code> attribute. 
	 * @param value the useBoost - The property specifies whether a sort should apply SOLR boost rules.
	 */
	public void setUseBoost(final boolean value)
	{
		setUseBoost( getSession().getSessionContext(), value );
	}
	
}
