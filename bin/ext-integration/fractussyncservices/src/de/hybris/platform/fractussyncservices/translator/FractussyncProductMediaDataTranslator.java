/*
* [y] hybris Platform
*
* Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.util.Config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Allows to transfer media data from a source platform to another target platform like this: Export: It's assembling a
 * URL which shall be accessible be the target system. For that a 'baseURL' ImpEx property may be specified. If that's
 * omitted the platform property 'y2ysync.home.url' will be used.
 * <code>INSERT_UPDATE Media; @noMatter[translator=de.hybris.platform.fractussyncservices.translator.ProductMediaDataTranslator, baseURL='http://public.host.url', mediaType='picture'];</code>
 */
public class FractussyncProductMediaDataTranslator implements SpecialValueTranslator
{
	private String mediaType;
	private static final String DEFAULT_MEDIA_TYPE = "picture";
	private String baseURL;
	private static final String BASE_URL_PROPERTY_KEY = "y2ysync.home.url";
	private boolean isForExport;
	private static final String DEFAULT_EMPTY_EXPR = "<empty>";

	private static final Logger LOG = Logger.getLogger(FractussyncProductMediaDataTranslator.class);

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		isForExport = isExport(columnDescriptor);
		if (StringUtils.isBlank(baseURL))
		{
			baseURL = Config.getParameter(BASE_URL_PROPERTY_KEY);
		}
		if (StringUtils.isBlank(mediaType))
		{
			mediaType = DEFAULT_MEDIA_TYPE;
		}
	}

	protected boolean isExport(final SpecialColumnDescriptor cd)
	{
		final EnumerationValue mode = cd.getHeader().getReader().getValidationMode();
		final String code = mode != null ? mode.getCode() : "n/a";

		return ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY.equalsIgnoreCase(code)
				|| ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_RELAXED.equalsIgnoreCase(code)
				|| ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT.equalsIgnoreCase(code);
	}

	@Override
	public void validate(final String expr) throws HeaderValidationException
	{
		if (isForExport())
		{
			if (StringUtils.isBlank(baseURL))
			{
				throw new HeaderValidationException("missing 'baseURL' for exporting medias as PULL-URLs", 123);
			}
			try
			{
				final URL validUrl = new URL(baseURL);
				LOG.debug("baseURL" + validUrl.toString() + " for exporting medias is valid");
			}
			catch (final MalformedURLException e)
			{
				throw new HeaderValidationException("illegal property baseURL='" + baseURL + "':" + e.getMessage(), 123);
			}
		}
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		if (item instanceof Product)
		{
			try
			{
				if (item.getAttribute(getMediaType()) instanceof Media)
				{
					return getMediaUrl(item);
				}
			}
			catch (final JaloSecurityException e)
			{
				throw new ImpExException(e);
			}
		}

		return DEFAULT_EMPTY_EXPR;
	}

	/**
	 * @param item
	 * @return
	 * @throws JaloSecurityException
	 */
	protected String getMediaUrl(final Item item) throws JaloSecurityException
	{
		final Media mediaItem = (Media) item.getAttribute(getMediaType());

		if (mediaItem.hasData())
		{
			return getBaseURL() + mediaItem.getURL();
		}
		else
		{
			if (StringUtils.isNotEmpty(mediaItem.getURL()))
			{
				return mediaItem.getURL();
			}
			else
			{
				return DEFAULT_EMPTY_EXPR;
			}
		}
	}

	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		throw new ImpExException(new UnsupportedOperationException());
	}

	@Override
	public boolean isEmpty(final String cellValue)
	{
		return StringUtils.isBlank(cellValue);
	}

	protected String getMediaType()
	{
		return mediaType;
	}

	protected String getBaseURL()
	{
		return baseURL;
	}

	protected boolean isForExport()
	{
		return isForExport;
	}

	public void setBaseURL(final String baseURL)
	{
		this.baseURL = baseURL;
	}
}
