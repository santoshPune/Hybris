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
package de.hybris.platform.acceleratorcms.services;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import javax.servlet.jsp.PageContext;
import java.util.Map;


/**
 * Service interface providing methods to resolve dynamic attributes for CMS components and content slots.
 */
public interface CMSDynamicAttributeService
{
	/**
	 * @param component
	 *           the CMS component to resolve dynamic attributes for
	 * @param contentSlot
	 *           the content slot holding the component
	 * @return {@link Map} of dynamic attributes for the given CMS component
	 */
	Map<String, String> getDynamicComponentAttributes(AbstractCMSComponentModel component, ContentSlotModel contentSlot);

	/**
	 * @param contentSlot
	 *           the content slot to resolve dynamic attributes for
	 * @return {@link Map} of dynamic attributes for the given CMS content slot
	 */
	Map<String, String> getDynamicContentSlotAttributes(final ContentSlotModel contentSlot, PageContext pageContext, Map<String, String> initialMaps);

	void afterAllItems(PageContext pageContext);

	/**
	 * @return the element to create if none provided
	 */
	String getFallbackElement(CMSItemModel cmsItemModel);
}
