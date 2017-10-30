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

import java.util.Set;

import de.hybris.platform.sap.core.configuration.model.SAPGlobalConfigurationModel;
import de.hybris.platform.sap.sapmodel.model.SAPLogicalSystemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class SapLogicalSystemValidationInterceptor implements
		ValidateInterceptor<SAPLogicalSystemModel> {

	@Override
	public void onValidate(SAPLogicalSystemModel sapLogicalSystemModel, InterceptorContext ctx)
			throws InterceptorException {
		
		SAPGlobalConfigurationModel sapGlobalConfiguration = sapLogicalSystemModel.getSapGlobalConfiguration();

		if (sapGlobalConfiguration != null)
		{

			Set<SAPLogicalSystemModel> sapLogicalSystems = sapGlobalConfiguration.getSapLogicalSystemGlobalConfig();

			if (sapLogicalSystems.stream().count() == 0L) {

				sapLogicalSystemModel.setDefaultLogicalSystem(true);

			} else if (sapLogicalSystemModel.isDefaultLogicalSystem()) {

				sapLogicalSystems.stream()
						.filter(entry -> entry != sapLogicalSystemModel &&
								entry.isDefaultLogicalSystem())
						.forEach(entry -> {
							entry.setDefaultLogicalSystem(false);
							ctx.getModelService().save(entry);
						});
			}

		}

	}

}
