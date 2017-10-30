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
package de.hybris.platform.cmswebservices.types.facade;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;



@IntegrationTest
public class ComponentTypeFacadeIntegrationTest extends ServicelayerTest
{
	@Resource
	private ComponentTypeFacade componentTypeFacade;

	@Test
	public void shouldGetParagraphComponentTypeFromAllTypes()
	{
		final List<ComponentTypeData> componentTypes = componentTypeFacade.getAllComponentTypes();
		Assert.assertTrue(componentTypes.size() > 1);

		// Get the CmsParagraphComponent type.
		final ComponentTypeData cmsParagraphComponentType = componentTypes.stream()
				.filter(componentType -> CMSParagraphComponentModel._TYPECODE.equals(componentType.getCode())).findFirst().get();

		assertParagraphComponent(cmsParagraphComponentType);
	}

	@Test
	public void shouldGetParagraphComponentType_FromSingleType() throws ComponentTypeNotFoundException
	{
		final ComponentTypeData cmsParagraphComponentType = componentTypeFacade.getComponentTypeByCode("CMSParagraphComponent");

		assertParagraphComponent(cmsParagraphComponentType);
	}

	protected void assertParagraphComponent(final ComponentTypeData cmsParagraphComponentType)
	{
		Assert.assertEquals(CMSParagraphComponentModel._TYPECODE, cmsParagraphComponentType.getCode());
		Assert.assertNull(cmsParagraphComponentType.getName());
		Assert.assertEquals("type.cmsparagraphcomponent.name", cmsParagraphComponentType.getI18nKey());

		final List<ComponentTypeAttributeData> paragraphAttributes = cmsParagraphComponentType.getAttributes();
		Assert.assertEquals(1, paragraphAttributes.size());

		final ComponentTypeAttributeData contentAttribute = paragraphAttributes.get(0);
		Assert.assertEquals("content", contentAttribute.getQualifier());
		Assert.assertEquals(Boolean.TRUE, contentAttribute.getLocalized());
		Assert.assertEquals("RichText", contentAttribute.getCmsStructureType());
		Assert.assertEquals("type.cmsparagraphcomponent.content.name", contentAttribute.getI18nKey());
	}


}
