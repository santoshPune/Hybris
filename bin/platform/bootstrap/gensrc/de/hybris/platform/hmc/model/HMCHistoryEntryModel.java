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
package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.enums.HistoryActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type HMCHistoryEntry first defined at extension hmc.
 */
@SuppressWarnings("all")
public class HMCHistoryEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "HMCHistoryEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>HMCHistoryEntry.user</code> attribute defined at extension <code>hmc</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>HMCHistoryEntry.timestamp</code> attribute defined at extension <code>hmc</code>. */
	public static final String TIMESTAMP = "timestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>HMCHistoryEntry.actionType</code> attribute defined at extension <code>hmc</code>. */
	public static final String ACTIONTYPE = "actionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>HMCHistoryEntry.comment</code> attribute defined at extension <code>hmc</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>HMCHistoryEntry.referencedItem</code> attribute defined at extension <code>hmc</code>. */
	public static final String REFERENCEDITEM = "referencedItem";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public HMCHistoryEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public HMCHistoryEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _user initial attribute declared by type <code>HMCHistoryEntry</code> at extension <code>hmc</code>
	 */
	@Deprecated
	public HMCHistoryEntryModel(final UserModel _user)
	{
		super();
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>HMCHistoryEntry</code> at extension <code>hmc</code>
	 */
	@Deprecated
	public HMCHistoryEntryModel(final ItemModel _owner, final UserModel _user)
	{
		super();
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>HMCHistoryEntry.actionType</code> attribute defined at extension <code>hmc</code>. 
	 * @return the actionType - type of action
	 */
	@Accessor(qualifier = "actionType", type = Accessor.Type.GETTER)
	public HistoryActionType getActionType()
	{
		return getPersistenceContext().getPropertyValue(ACTIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>HMCHistoryEntry.comment</code> attribute defined at extension <code>hmc</code>. 
	 * @return the comment - Comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>HMCHistoryEntry.referencedItem</code> attribute defined at extension <code>hmc</code>. 
	 * @return the referencedItem - Item
	 */
	@Accessor(qualifier = "referencedItem", type = Accessor.Type.GETTER)
	public ItemModel getReferencedItem()
	{
		return getPersistenceContext().getPropertyValue(REFERENCEDITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>HMCHistoryEntry.timestamp</code> attribute defined at extension <code>hmc</code>. 
	 * @return the timestamp - Timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
	public Date getTimestamp()
	{
		return getPersistenceContext().getPropertyValue(TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>HMCHistoryEntry.user</code> attribute defined at extension <code>hmc</code>. 
	 * @return the user - User
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>HMCHistoryEntry.actionType</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the actionType - type of action
	 */
	@Accessor(qualifier = "actionType", type = Accessor.Type.SETTER)
	public void setActionType(final HistoryActionType value)
	{
		getPersistenceContext().setPropertyValue(ACTIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>HMCHistoryEntry.comment</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the comment - Comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>HMCHistoryEntry.referencedItem</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the referencedItem - Item
	 */
	@Accessor(qualifier = "referencedItem", type = Accessor.Type.SETTER)
	public void setReferencedItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(REFERENCEDITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>HMCHistoryEntry.timestamp</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the timestamp - Timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
	public void setTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>HMCHistoryEntry.user</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the user - User
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
