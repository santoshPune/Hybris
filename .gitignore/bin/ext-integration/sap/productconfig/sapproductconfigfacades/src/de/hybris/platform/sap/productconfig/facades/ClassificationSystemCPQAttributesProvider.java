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
package de.hybris.platform.sap.productconfig.facades;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;

import java.util.List;
import java.util.Map;


/**
 * Provide required logic to access the characteristic / values names from hybris classification system
 */
public interface ClassificationSystemCPQAttributesProvider
{
	ClassificationSystemCPQAttributesContainer getCPQAttributes(String name,
			Map<String, ClassificationSystemCPQAttributesContainer> nameMap);

	String getDisplayName(final CsticModel csticModel, final ClassificationSystemCPQAttributesContainer hybrisNames,
			final boolean isDebugEnabled);

	String getDisplayValueName(final CsticValueModel valueModel, CsticModel csticModel,
			final ClassificationSystemCPQAttributesContainer hybrisNames, final boolean isDebugEnabled);

	String getOverviewValueName(final CsticValueModel valueModel, final CsticModel csticModel,
			final ClassificationSystemCPQAttributesContainer hybrisNames, final boolean isDebugEnabled);

	String getLongText(final CsticModel model, final ClassificationSystemCPQAttributesContainer hybrisNames,
			final boolean isDebugEnabled);

	List<ImageData> getCsticMedia(final String csticKey, final ClassificationSystemCPQAttributesContainer cpqAttributes);

	List<ImageData> getCsticValueMedia(final String csticValueKey, final ClassificationSystemCPQAttributesContainer cpqAttributes);

	boolean isDebugEnabled();

}
