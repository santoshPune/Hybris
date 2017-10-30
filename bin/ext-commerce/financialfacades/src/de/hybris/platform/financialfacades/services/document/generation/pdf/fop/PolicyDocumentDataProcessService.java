/*
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
package de.hybris.platform.financialfacades.services.document.generation.pdf.fop;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;


/**
 * Policy Document Data Process Service
 */
public interface PolicyDocumentDataProcessService
{
	/**
	 * Get the Policy Document Data populated from the given item reference id.
	 */
	InsurancePolicyData getPolicyDocumentData(String itemRefId);
}
