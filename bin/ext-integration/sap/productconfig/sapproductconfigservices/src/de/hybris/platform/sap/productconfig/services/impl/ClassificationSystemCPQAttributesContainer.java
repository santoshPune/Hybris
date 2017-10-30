/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.core.model.media.MediaModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * Immutable Object
 *
 */
public class ClassificationSystemCPQAttributesContainer
{
	private final String code;
	private final String name;
	private final String description;
	private final Map<String, String> valueNames;
	private final Map<String, Collection<MediaModel>> csticMedia;
	private final Map<String, Collection<MediaModel>> csticValueMedia;
	private static final String NULL_CODE = "";

	public static final ClassificationSystemCPQAttributesContainer NULL_OBJ = new ClassificationSystemCPQAttributesContainer(
			NULL_CODE, null, null, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());

	public ClassificationSystemCPQAttributesContainer(final String code, final String name, final String description,
			final Map<String, String> valueNames, final Map<String, Collection<MediaModel>> csticMedia,
			final Map<String, Collection<MediaModel>> csticValueMedia)
	{
		this.code = code;
		this.name = name;
		this.description = description;
		this.valueNames = Collections.unmodifiableMap(valueNames);
		this.csticMedia = Collections.unmodifiableMap(csticMedia);
		this.csticValueMedia = Collections.unmodifiableMap(csticValueMedia);
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Map<String, String> getValueNames()
	{
		return valueNames;
	}

	public Map<String, Collection<MediaModel>> getCsticMedia()
	{
		return csticMedia;
	}

	public Map<String, Collection<MediaModel>> getCsticValueMedia()
	{
		return csticValueMedia;
	}

	@Override
	public int hashCode()
	{
		return code.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ClassificationSystemCPQAttributesContainer other = (ClassificationSystemCPQAttributesContainer) obj;
		if (code == null)
		{
			if (other.code != null)
			{
				return false;
			}
		}
		else if (!code.equals(other.code))
		{
			return false;
		}
		return true;
	}
}
