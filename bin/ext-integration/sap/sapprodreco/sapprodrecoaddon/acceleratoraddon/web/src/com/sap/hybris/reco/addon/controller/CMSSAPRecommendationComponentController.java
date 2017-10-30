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
package com.sap.hybris.reco.addon.controller;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.hybris.reco.addon.constants.SapprodrecoaddonConstants;
import com.sap.hybris.reco.addon.facade.ProductRecommendationManagerFacade;
import com.sap.hybris.reco.common.util.CookieHelper;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;


/**
 * Controller for CMS CMSSAPRecommendationComponentController.
 */
@Controller("CMSSAPRecommendationComponentController")
@RequestMapping(value = "/view/CMSSAPRecommendationComponentController")
public class CMSSAPRecommendationComponentController extends
		AbstractCMSAddOnComponentController<CMSSAPRecommendationComponentModel>
{
	@Resource(name = "sapProductRecommendationManagerFacade")
	private ProductRecommendationManagerFacade productRecommendationManagerFacade;

	@Override
	protected String getAddonUiExtensionName(final CMSSAPRecommendationComponentModel component)
	{
		return SapprodrecoaddonConstants.EXTENSIONNAME;
	}

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final CMSSAPRecommendationComponentModel component)
	{
		String productCode = null;
		final ProductModel currentProduct = getRequestContextData(request).getProduct();
		if (currentProduct != null)
		{
			productCode = currentProduct.getCode();
		}
		model.addAttribute("productCode", productCode);
		model.addAttribute("componentId", component.getUid());
		if (productRecommendationManagerFacade.isPrefetchEnabled())
		{
			final boolean isRecommendationFetched = productRecommendationManagerFacade.isRecommendationFetched(component.getUid());
			if (!isRecommendationFetched)
			{
				boolean isAnonymousUser = true;
				String userId = null;
				if (productRecommendationManagerFacade.getSessionUserId().equals("anonymous"))
				{
					userId = CookieHelper.getPiwikID(request);

				}
				else
				{
					userId = productRecommendationManagerFacade.getSessionUserId();
					isAnonymousUser = false;
				}
				final AbstractPageModel pageModel = getCmsPageContextService().getCmsPageRequestContextData(request).getPage();
				productRecommendationManagerFacade.prefetchRecommendationsForAllComponents(userId, isAnonymousUser, pageModel,
						productCode);
			}
		}
	}
}
