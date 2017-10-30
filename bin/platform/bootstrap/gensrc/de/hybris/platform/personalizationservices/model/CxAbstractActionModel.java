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
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;

/**
 * Generated model class for type CxAbstractAction first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxAbstractActionModel extends AbstractActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CxAbstractAction";
	
	/**<i>Generated relation code constant for relation <code>CxVariationToAction</code> defining source attribute <code>variation</code> in extension <code>personalizationservices</code>.</i>*/
	public final static String _CXVARIATIONTOACTION = "CxVariationToAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractAction.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractAction.affectedObjectKey</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String AFFECTEDOBJECTKEY = "affectedObjectKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractAction.variation</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String VARIATION = "variation";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxAbstractActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxAbstractActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxAbstractActionModel(final CatalogVersionModel _catalogVersion, final String _code, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxAbstractActionModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractAction.affectedObjectKey</code> dynamic attribute defined at extension <code>personalizationservices</code>. 
	 * @return the affectedObjectKey
	 */
	@Accessor(qualifier = "affectedObjectKey", type = Accessor.Type.GETTER)
	public String getAffectedObjectKey()
	{
		return getPersistenceContext().getDynamicValue(this,AFFECTEDOBJECTKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractAction.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractAction.variation</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the variation
	 */
	@Accessor(qualifier = "variation", type = Accessor.Type.GETTER)
	public CxVariationModel getVariation()
	{
		return getPersistenceContext().getPropertyValue(VARIATION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxAbstractAction.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxAbstractAction.variation</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the variation
	 */
	@Accessor(qualifier = "variation", type = Accessor.Type.SETTER)
	public void setVariation(final CxVariationModel value)
	{
		getPersistenceContext().setPropertyValue(VARIATION, value);
	}
	
}
