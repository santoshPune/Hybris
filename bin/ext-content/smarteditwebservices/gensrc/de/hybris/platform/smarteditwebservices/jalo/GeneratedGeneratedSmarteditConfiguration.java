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
package de.hybris.platform.smarteditwebservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.smarteditwebservices.jalo.GeneratedSmarteditConfiguration SmarteditConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGeneratedSmarteditConfiguration extends GenericItem
{
	/** Qualifier of the <code>SmarteditConfiguration.key</code> attribute **/
	public static final String KEY = "key";
	/** Qualifier of the <code>SmarteditConfiguration.value</code> attribute **/
	public static final String VALUE = "value";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(KEY, AttributeMode.INITIAL);
		tmp.put(VALUE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.key</code> attribute.
	 * @return the key - name of the property
	 */
	public String getKey(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.key</code> attribute.
	 * @return the key - name of the property
	 */
	public String getKey()
	{
		return getKey( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SmarteditConfiguration.key</code> attribute. 
	 * @param value the key - name of the property
	 */
	public void setKey(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KEY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SmarteditConfiguration.key</code> attribute. 
	 * @param value the key - name of the property
	 */
	public void setKey(final String value)
	{
		setKey( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.value</code> attribute.
	 * @return the value - value of the property
	 */
	public String getValue(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SmarteditConfiguration.value</code> attribute.
	 * @return the value - value of the property
	 */
	public String getValue()
	{
		return getValue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SmarteditConfiguration.value</code> attribute. 
	 * @param value the value - value of the property
	 */
	public void setValue(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VALUE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SmarteditConfiguration.value</code> attribute. 
	 * @param value the value - value of the property
	 */
	public void setValue(final String value)
	{
		setValue( getSession().getSessionContext(), value );
	}
	
}
