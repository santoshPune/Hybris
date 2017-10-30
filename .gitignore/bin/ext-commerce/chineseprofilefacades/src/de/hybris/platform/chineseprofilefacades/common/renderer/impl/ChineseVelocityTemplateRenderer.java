/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineseprofilefacades.common.renderer.impl;

import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.addressfacades.strategies.NameWithTitleFormatStrategy;
import de.hybris.platform.chineseprofilefacades.process.email.context.OrderNotificationEmailContext;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.exceptions.RendererException;
import de.hybris.platform.commons.renderer.impl.VelocityTemplateRenderer;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Required;


/**
 * Used to render Chinese email template
 */
public class ChineseVelocityTemplateRenderer extends VelocityTemplateRenderer
{
	private static final Logger LOG = Logger.getLogger(ChineseVelocityTemplateRenderer.class);

	private String nameWithTitleKey;
	private MediaService mediaService;
	private String contextName;
	private String deliveryAddressNameKey;
	private CustomerNameStrategy customerNameStrategy;
	private NameWithTitleFormatStrategy nameWithTitleFormatStrategy;


	protected NameWithTitleFormatStrategy getNameWithTitleFormatStrategy()
	{
		return nameWithTitleFormatStrategy;
	}

	@Required
	public void setNameWithTitleFormatStrategy(final NameWithTitleFormatStrategy nameWithTitleFormatStrategy)
	{
		this.nameWithTitleFormatStrategy = nameWithTitleFormatStrategy;
	}

	protected String getDeliveryAddressNameKey()
	{
		return deliveryAddressNameKey;
	}

	@Required
	public void setDeliveryAddressNameKey(final String deliveryAddressNameKey)
	{
		this.deliveryAddressNameKey = deliveryAddressNameKey;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	@Override
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected String getContextName()
	{
		return contextName;
	}

	@Required
	@Override
	public void setContextName(final String contextName)
	{
		this.contextName = contextName;
	}

	protected String getNameWithTitleKey()
	{
		return nameWithTitleKey;
	}

	@Required
	public void setNameWithTitleKey(final String nameWithTitleKey)
	{
		this.nameWithTitleKey = nameWithTitleKey;
	}

	protected CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	@Required
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	@Override
	public void render(final RendererTemplateModel template, final Object context, final Writer output)
	{
		Class clazz = null;

		try
		{
			clazz = Class.forName(template.getContextClass());
		}
		catch (final ClassNotFoundException e)
		{
			throw new RendererException("Cannot find class: " + template.getContextClass(), e);
		}

		InputStream inputStream = null;

		try
		{
			if ((context != null) && (!clazz.isAssignableFrom(context.getClass())))
			{
				throw new RendererException("The context class [" + context.getClass().getName() + "] is not correctly defined.");
			}

			final MediaModel content = template.getContent();
			if (content == null)
			{
				throw new RendererException("No content found for template " + template.getCode());
			}

			inputStream = mediaService.getStreamFromMedia(content);
			writeToOutput(output, inputStream, context);
		}
		catch (final IOException e)
		{
			throw new RendererException("Problem during rendering", e);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
	}

	protected void setNameWithTitle(final VelocityContext velocityContext, final Object context)
	{

		if (AbstractEmailContext.class.isAssignableFrom(context.getClass()))
		{
			final AbstractEmailContext emailContext = (AbstractEmailContext) context;
			final String[] names = getCustomerNameStrategy().splitName(emailContext.getDisplayName());

			velocityContext.put(getNameWithTitleKey(), getNameWithTitleFormatStrategy().getFullnameWithTitleForISOCode(names[0],
					names[1], emailContext.getTitle(), emailContext.getEmailLanguage().getIsocode()));

			if (OrderNotificationEmailContext.class.isAssignableFrom(context.getClass()))
			{
				final OrderNotificationEmailContext notificationContext = (OrderNotificationEmailContext) context;
				final AddressData deliveryAddress = notificationContext.getOrder().getDeliveryAddress();
				if (deliveryAddress != null)
				{
					if (StringUtils.isBlank(deliveryAddress.getFullname()))
					{
						velocityContext.put(getDeliveryAddressNameKey(),
								getNameWithTitleFormatStrategy().getFullnameWithTitleForISOCode(deliveryAddress.getFirstName(),
										deliveryAddress.getLastName(), notificationContext.getTitle().toString(),
										notificationContext.getEmailLanguage().getIsocode()));
					}
					else
					{
						velocityContext.put(getDeliveryAddressNameKey(),
								getNameWithTitleFormatStrategy().getFullnameWithTitleForISOCode(deliveryAddress.getFullname(),
										notificationContext.getTitle().toString(), notificationContext.getEmailLanguage().getIsocode()));
					}
				}
			}
		}
	}

	protected void writeToOutput(final Writer result, final InputStream inputStream, final Object context) throws IOException
	{
		final VelocityContext ctx = new VelocityContext();
		ctx.put(getContextName(), context);
		setNameWithTitle(ctx, context);
		final Reader reader = new InputStreamReader(inputStream, "UTF-8");

		try
		{
			evaluate(result, ctx, reader);
			result.flush();
		}
		catch (final Exception e)
		{
			throw new RendererException("Problem with get velocity stream", e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}
}
