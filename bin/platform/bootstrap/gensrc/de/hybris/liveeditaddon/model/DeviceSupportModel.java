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
package de.hybris.liveeditaddon.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type DeviceSupport first defined at extension liveeditaddon.
 */
@SuppressWarnings("all")
public class DeviceSupportModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DeviceSupport";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.code</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.name</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.width</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String WIDTH = "width";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.height</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String HEIGHT = "height";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.Orientation</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String ORIENTATION = "Orientation";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.supportedUiExperience</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String SUPPORTEDUIEXPERIENCE = "supportedUiExperience";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeviceSupport.default</code> attribute defined at extension <code>liveeditaddon</code>. */
	public static final String DEFAULT = "default";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DeviceSupportModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DeviceSupportModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeviceSupport</code> at extension <code>liveeditaddon</code>
	 * @param _supportedUiExperience initial attribute declared by type <code>DeviceSupport</code> at extension <code>liveeditaddon</code>
	 */
	@Deprecated
	public DeviceSupportModel(final String _code, final UiExperienceLevel _supportedUiExperience)
	{
		super();
		setCode(_code);
		setSupportedUiExperience(_supportedUiExperience);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DeviceSupport</code> at extension <code>liveeditaddon</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _supportedUiExperience initial attribute declared by type <code>DeviceSupport</code> at extension <code>liveeditaddon</code>
	 */
	@Deprecated
	public DeviceSupportModel(final String _code, final ItemModel _owner, final UiExperienceLevel _supportedUiExperience)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setSupportedUiExperience(_supportedUiExperience);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.code</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.height</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the height - Defines the device viewport height size
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.GETTER)
	public Integer getHeight()
	{
		return getPersistenceContext().getPropertyValue(HEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.name</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the name - Defines the name of device
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.name</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @param loc the value localization key 
	 * @return the name - Defines the name of device
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.supportedUiExperience</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the supportedUiExperience - Defines the ui experience level which is supported by this device
	 */
	@Accessor(qualifier = "supportedUiExperience", type = Accessor.Type.GETTER)
	public UiExperienceLevel getSupportedUiExperience()
	{
		return getPersistenceContext().getPropertyValue(SUPPORTEDUIEXPERIENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.width</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the width - Defines the device viewport width size
	 */
	@Accessor(qualifier = "width", type = Accessor.Type.GETTER)
	public Integer getWidth()
	{
		return getPersistenceContext().getPropertyValue(WIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.default</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the default - Defines if the device is optional for current ui experience
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.GETTER)
	public boolean isDefault()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DEFAULT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeviceSupport.Orientation</code> attribute defined at extension <code>liveeditaddon</code>. 
	 * @return the Orientation - Defines if the device has orientation support like horizontal, vertical
	 */
	@Accessor(qualifier = "Orientation", type = Accessor.Type.GETTER)
	public boolean isOrientation()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ORIENTATION));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.code</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.default</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the default - Defines if the device is optional for current ui experience
	 */
	@Accessor(qualifier = "default", type = Accessor.Type.SETTER)
	public void setDefault(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.height</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the height - Defines the device viewport height size
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.SETTER)
	public void setHeight(final Integer value)
	{
		getPersistenceContext().setPropertyValue(HEIGHT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.name</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the name - Defines the name of device
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.name</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the name - Defines the name of device
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.Orientation</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the Orientation - Defines if the device has orientation support like horizontal, vertical
	 */
	@Accessor(qualifier = "Orientation", type = Accessor.Type.SETTER)
	public void setOrientation(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ORIENTATION, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.supportedUiExperience</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the supportedUiExperience - Defines the ui experience level which is supported by this device
	 */
	@Accessor(qualifier = "supportedUiExperience", type = Accessor.Type.SETTER)
	public void setSupportedUiExperience(final UiExperienceLevel value)
	{
		getPersistenceContext().setPropertyValue(SUPPORTEDUIEXPERIENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeviceSupport.width</code> attribute defined at extension <code>liveeditaddon</code>. 
	 *  
	 * @param value the width - Defines the device viewport width size
	 */
	@Accessor(qualifier = "width", type = Accessor.Type.SETTER)
	public void setWidth(final Integer value)
	{
		getPersistenceContext().setPropertyValue(WIDTH, value);
	}
	
}
