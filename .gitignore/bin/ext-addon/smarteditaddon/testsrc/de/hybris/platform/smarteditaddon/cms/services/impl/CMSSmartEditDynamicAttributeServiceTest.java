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
package de.hybris.platform.smarteditaddon.cms.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.component.container.CMSComponentContainerStrategy;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.collections.MapUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSSmartEditDynamicAttributeServiceTest
{
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String COMPONENT_TYPE_ATTRIBUTE = "data-smartedit-component-type";
	private static final String COMPONENT_ID_ATTRIBUTE = "data-smartedit-component-id";
	private static final String SMART_EDIT_COMPONENT_CLASS = "smartEditComponent";
	private static final String CONTENT_SLOT_TYPE = "ContentSlot";
	private static final String CONTENT_SLOT_UID = "TestSlot";
	private static final String COMPONENT_TYPE = "Component";
	private static final String COMPONENT_UID = "TestComponent";

	@Mock
	private AbstractCMSComponentModel component;

	@Mock
	private AbstractCMSComponentContainerModel containerComponent;

	@Mock
	private SessionService sessionService;

	@Mock
	private CMSComponentContainerStrategy cmsComponentContainerStrategy;

	@Mock
	private ContentSlotModel contentSlot;

	@InjectMocks
	@Spy
	private CMSSmartEditDynamicAttributeService cmsSmartEditDynamicAttributeService;

	@Before
	public void setUp()
	{
		when(contentSlot.getUid()).thenReturn(CONTENT_SLOT_UID);
		when(contentSlot.getItemtype()).thenReturn(CONTENT_SLOT_TYPE);

		when(component.getUid()).thenReturn(COMPONENT_UID);
		when(component.getItemtype()).thenReturn(COMPONENT_TYPE);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldTheComponentBeWrappedThrowsExceptionIfComponentNull()
	{
		cmsSmartEditDynamicAttributeService.shouldTheComponentBeWrapped(null, contentSlot);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldTheComponentBeWrappedThrowsExceptionIfSlotNull()
	{
		cmsSmartEditDynamicAttributeService.shouldTheComponentBeWrapped(component, null);
	}

	@Test
	public void shouldTheComponentBeWrappedReturnsTrueIfDirectChild()
	{
		when(contentSlot.getCmsComponents()).thenReturn(asList(component));
		assertThat(cmsSmartEditDynamicAttributeService.shouldTheComponentBeWrapped(component, contentSlot), is(true));
	}

	@Test
	public void shouldTheComponentBeWrappedReturnsTrueIfComponentInContainer()
	{
		when(contentSlot.getCmsComponents()).thenReturn(asList(containerComponent));
		when(cmsComponentContainerStrategy.getDisplayComponentsForContainer(containerComponent)).thenReturn(asList(component));
		assertThat(cmsSmartEditDynamicAttributeService.shouldTheComponentBeWrapped(component, contentSlot), is(true));
	}

	@Test
	public void shouldTheComponentBeWrappedReturnsFalseIfComponentIsNeitherDirectChildNorThroughContainer()
	{
		when(contentSlot.getCmsComponents()).thenReturn(asList(component, containerComponent));
		when(cmsComponentContainerStrategy.getDisplayComponentsForContainer(containerComponent)).thenReturn(asList(component));
		assertThat(cmsSmartEditDynamicAttributeService.shouldTheComponentBeWrapped(mock(AbstractCMSComponentModel.class),
				contentSlot), is(false));
	}

	@Test
	public void whenComponentShouldBeWrappedAndInPreviewThenDynamicAttributesWillBeAdded()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		doReturn(true).when(cmsSmartEditDynamicAttributeService).shouldTheComponentBeWrapped(component, contentSlot);

		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				contentSlot);

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertEquals("component id attribute does not match expected value", COMPONENT_UID,
				dynamicAttributes.get(COMPONENT_ID_ATTRIBUTE));
		assertEquals("component type attribute does not match expected value", COMPONENT_TYPE,
				dynamicAttributes.get(COMPONENT_TYPE_ATTRIBUTE));
		assertEquals("class does not match the expected value", SMART_EDIT_COMPONENT_CLASS,
				dynamicAttributes.get(CLASS_ATTRIBUTE));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}

	@Test
	public void whenComponentShouldNotBeWrappedDynamicAttributesWillNotBeAddedToComponent()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		doReturn(false).when(cmsSmartEditDynamicAttributeService).shouldTheComponentBeWrapped(component, contentSlot);

		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				contentSlot);

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}

	@Test
	public void whenNotInPreviewWrappedDynamicAttributesWillNotBeAddedToComponent()
	{
		when(sessionService.getAttribute(anyString())).thenReturn(null);
		doReturn(true).when(cmsSmartEditDynamicAttributeService).shouldTheComponentBeWrapped(component, contentSlot);

		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				contentSlot);

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}

	@Test
	public void whenComponentIsNullDynamicAttributesWillNotBeAddedToComponent()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(null,
				contentSlot);

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

	}

	@Test
	public void whenSlotIsNullDynamicAttributesWillNotBeAddedToComponent()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				null);

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

	}

	@Test
	public void whenInPreviewDynamicAttributesWillBeAddedOnSlot()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService
				.getDynamicContentSlotAttributes(contentSlot, mock(PageContext.class), new HashMap<>());

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertEquals("component id attribute does not match expected value", CONTENT_SLOT_UID,
				dynamicAttributes.get(COMPONENT_ID_ATTRIBUTE));
		assertEquals("component type attribute does not match expected value", CONTENT_SLOT_TYPE,
				dynamicAttributes.get(COMPONENT_TYPE_ATTRIBUTE));
		assertEquals("class does not match the expected value", SMART_EDIT_COMPONENT_CLASS,
				dynamicAttributes.get(CLASS_ATTRIBUTE));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}

	@Test
	public void whenNotInPreviewDynamicAttributesWillNotBeAddedOnSlot()
	{
		when(sessionService.getAttribute(anyString())).thenReturn(null);
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicContentSlotAttributes(null,
				mock(PageContext.class), new HashMap<>());

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}

	@Test
	public void dynamicAttributesWillNotBeAddedOnNullSlot()
	{
		when(sessionService.getAttribute(anyString())).thenReturn("previewId");
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicContentSlotAttributes(null,
				mock(PageContext.class), new HashMap<>());

		assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));

		verify(sessionService, times(1)).getAttribute("cmsTicketId");
	}
}
