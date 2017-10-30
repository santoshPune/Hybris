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
 */
package com.sap.hybris.reco.util;

import de.hybris.platform.sap.core.odata.util.MyCallback;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.callback.WriteCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.hybris.reco.constants.SapproductrecommendationConstants;


/**
 * Callback class for Interaction
 */
public class InteractionCallback extends MyCallback
{
	private static final Logger LOG = LoggerFactory.getLogger(InteractionCallback.class);

	/**
	 * InteractionCallback constructor
	 */
	public InteractionCallback()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent#retrieveFeedResult(org.apache.olingo.odata2.api.ep.
	 * callback.WriteFeedCallbackContext)
	 */
	/**
	 * for retrieving the data for deep entities
	 */
	@Override
	public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException
	{
		final WriteFeedCallbackResult result = new WriteFeedCallbackResult();

		try
		{
			if (isNavigationFromTo(context, SapproductrecommendationConstants.INTERACTIONS,
					SapproductrecommendationConstants.INTERACTION_ITEMS))
			{
				final EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot).omitJsonWrapper(true).responsePayload(false)
						.expandSelectTree(context.getCurrentExpandSelectTreeNode()).selfLink(context.getSelfLink()).build();

				final List<Map<String, Object>> contextParams = dataStore.getEntities(SapproductrecommendationConstants.INTERACTION_ITEMS);
				result.setFeedData(contextParams);
				result.setInlineProperties(inlineProperties);

			}
		}
		catch (final EdmException e)
		{
			LOG.error(SapproductrecommendationConstants.ERR_EXPAND);
		}
		return result;
	}

	/**
	 * Method to check the navigation from for an entitySet
	 */
	@Override
	public boolean isNavigationFromTo(final WriteCallbackContext context, final String entitySetName,
			final String navigationPropertyName) throws EdmException
	{
		if (entitySetName == null || navigationPropertyName == null)
		{
			return false;
		}
		final EdmEntitySet sourceEntitySet = context.getSourceEntitySet();
		final EdmNavigationProperty navigationProperty = context.getNavigationProperty();
		return entitySetName.equals(sourceEntitySet.getName()) && navigationPropertyName.equals(navigationProperty.getName());
	}
}
