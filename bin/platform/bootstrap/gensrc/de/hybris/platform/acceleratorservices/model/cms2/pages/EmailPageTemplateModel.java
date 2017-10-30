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
package de.hybris.platform.acceleratorservices.model.cms2.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type EmailPageTemplate first defined at extension acceleratorservices.
 * <p>
 * Represents template to generate email messages.
 */
@SuppressWarnings("all")
public class EmailPageTemplateModel extends PageTemplateModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "EmailPageTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailPageTemplate.htmlTemplate</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String HTMLTEMPLATE = "htmlTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>EmailPageTemplate.subject</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SUBJECT = "subject";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EmailPageTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EmailPageTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public EmailPageTemplateModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public EmailPageTemplateModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailPageTemplate.htmlTemplate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the htmlTemplate - Contains renderer that generates body of the email message.
	 */
	@Accessor(qualifier = "htmlTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getHtmlTemplate()
	{
		return getPersistenceContext().getPropertyValue(HTMLTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EmailPageTemplate.subject</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the subject - Contains renderer that generates subject of the email message.
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public RendererTemplateModel getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailPageTemplate.htmlTemplate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the htmlTemplate - Contains renderer that generates body of the email message.
	 */
	@Accessor(qualifier = "htmlTemplate", type = Accessor.Type.SETTER)
	public void setHtmlTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(HTMLTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EmailPageTemplate.subject</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the subject - Contains renderer that generates subject of the email message.
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
}
