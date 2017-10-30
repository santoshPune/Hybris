package de.hybris.platform.recentvieweditemsaddon.interceptors.beforeview;


import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.recentvieweditemsservices.RecentViewedItemsService;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;

public class RecentViewedItemsBeforeViewHandlerAdaptee implements BeforeViewHandlerAdaptee
{

	private RecentViewedItemsService recentViewedItemsService;

	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName) throws Exception
	{
		final AbstractPageModel pageModel = (AbstractPageModel) model.get(AbstractPageController.CMS_PAGE_MODEL);
		if (pageModel != null && pageModel instanceof ProductPageModel)
		{
			final ProductData productData = (ProductData) model.get("product");			
			String categoryCode = "";
			if (productData != null)
			{
				final Collection<CategoryData> categories = productData.getCategories();
				if (categories != null)
				{
					final Iterator<CategoryData> it = categories.iterator();
					while (it.hasNext())
					{
						categoryCode = it.next().getCode();
						it.next();
					}
				}
				recentViewedItemsService.productVisited(productData.getCode(), categoryCode);
			}
		}	
		return viewName;
	}

	@Required
	public void setRecentViewedItemsService(final RecentViewedItemsService recentViewedItemsService)
	{
		this.recentViewedItemsService = recentViewedItemsService;
	}

}