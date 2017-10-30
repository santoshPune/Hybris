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
package de.hybris.platform.sap.sapmodel.jalo.interceptor;

import de.hybris.platform.sap.sapmodel.model.SAPPlantLogSysOrgModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.util.localization.Localization;

public class SapPlantLogSysOrgValidationInterceptor implements
		ValidateInterceptor<SAPPlantLogSysOrgModel> {

	@Override
	public void onValidate(SAPPlantLogSysOrgModel sapPlantLogSysOrg, InterceptorContext ctx)
			throws InterceptorException {

		if (sapPlantLogSysOrg.getLogSys().getSapGlobalConfiguration() == null) {

			throw new InterceptorException(
					Localization
							.getLocalizedString("validation.Saplogicalsystem.GlobalConfigurationMissing",
									new Object[] { sapPlantLogSysOrg.getLogSys().getSapLogicalSystemName() }));

		}

	}

}
