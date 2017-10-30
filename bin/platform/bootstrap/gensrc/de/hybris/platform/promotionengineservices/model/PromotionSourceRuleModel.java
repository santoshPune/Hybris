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
package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionSourceRule first defined at extension promotionengineservices.
 */
@SuppressWarnings("all")
public class PromotionSourceRuleModel extends SourceRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PromotionSourceRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionSourceRule.website</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String WEBSITE = "website";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionSourceRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionSourceRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public PromotionSourceRuleModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uuid initial attribute declared by type <code>AbstractRule</code> at extension <code>ruleengineservices</code>
	 */
	@Deprecated
	public PromotionSourceRuleModel(final String _code, final ItemModel _owner, final String _uuid)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setUuid(_uuid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.website</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the website - The website to which the promotion is associated.
	 */
	@Accessor(qualifier = "website", type = Accessor.Type.GETTER)
	public PromotionGroupModel getWebsite()
	{
		return getPersistenceContext().getPropertyValue(WEBSITE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionSourceRule.website</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the website - The website to which the promotion is associated.
	 */
	@Accessor(qualifier = "website", type = Accessor.Type.SETTER)
	public void setWebsite(final PromotionGroupModel value)
	{
		getPersistenceContext().setPropertyValue(WEBSITE, value);
	}
	
}
