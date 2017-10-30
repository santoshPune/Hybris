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
package de.hybris.platform.sap.productconfig.frontend.constants;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;


@SuppressWarnings("PMD")
public class SapproductconfigaddonConstants extends GeneratedSapproductconfigaddonConstants
{
	public static final String EXTENSIONNAME = "ysapproductconfigaddon";
	public static final String CONFIG_ATTRIBUTE = "config";
	public static final String OVERVIEW_ATTRIBUTE = "overview";
	public static final String OVERVIEWUIDATA_ATTRIBUTE = "overviewUiData";

	/**
	 * If this method is available at the OrderEntryDTO, we assume that the UI is prepared to render the configuration
	 * link
	 *
	 * @see OrderEntryData
	 */
	public static final String CONFIGURABLE_SOM_DTO_METHOD = "isConfigurable";

	private SapproductconfigaddonConstants()
	{
		//empty
	}


}
