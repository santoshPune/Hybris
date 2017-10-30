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
package de.hybris.platform.cmswebservices.pagescontentslotscomponents.facade.impl;

import static de.hybris.platform.cmswebservices.matchers.HamMatchers.is;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.exception.ComponentNotFoundInSlotException;
import de.hybris.platform.cmswebservices.exception.ValidationException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageContentSlotComponentFacadeTest
{
	private static final String PAGE_UID = "page-id";
	private static final String COMPONENT_UID = "testComponent";
	private static final String SECOND_COMPONENT_UID = "testSecondComponent";
	private static final String THIRD_COMPONENT_UID = "testThirdComponent";
	private static final String SLOT_UID = "slot-id";
	private static final String SECOND_SLOT_UID = "second-slot-id";
	private static final String THIRD_SLOT_UID = "third-slot-id";
	private static final String INVALID_SLOT_UID = "invalid-slot-id";
	private static final Integer INDEX = 0;

	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private AbstractCMSComponentModel secondComponent;
	@Mock
	private AbstractCMSComponentModel thirdComponent;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private ContentSlotModel secondContentSlot;
	@Mock
	private ContentSlotModel thirdContentSlot;
	@Mock
	private AbstractPageModel page;
	@Mock
	private ContentSlotData contentSlotData;
	@Mock
	private ContentSlotData secondContentSlotData;
	@Mock
	private ContentSlotData thirdContentSlotData;
	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSAdminComponentService cmsAdminComponentService;
	@Mock
	private CMSAdminPageService cmsAdminPageService;
	@Mock
	private Validator contentSlotComponentValidator;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private Validator updatePageContentSlotComponentValidator;
	@Mock
	private Validator componentExistsInSlotValidator;
	@Mock
	private PlatformTransactionManager manager;

	@InjectMocks
	private DefaultPageContentSlotComponentFacade defaultPageContentSlotComponentFacade;

	private PageContentSlotComponentData pageContentSlotComponentData;

	@Before
	public void setUp()
	{
		pageContentSlotComponentData = buildPageContentSlotComponent();

		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(cmsAdminComponentService.getDisplayedComponentsForContentSlot(contentSlot)).thenReturn(Collections.emptyList());
	}

	@Test
	public void shouldAddComponentToContentSlot() throws CMSItemNotFoundException
	{
		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);

		verify(cmsAdminContentSlotService).addCMSComponentToContentSlot(component, contentSlot, INDEX);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailAddComponentToContentSlot_ValidationException() throws CMSItemNotFoundException
	{
		doThrow(new ValidationException("exception")).when(facadeValidationService).validate(Mockito.any(), Mockito.any());

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailAddComponentToContentSlot_SlotNotFound() throws CMSItemNotFoundException
	{
		pageContentSlotComponentData.setSlotId(INVALID_SLOT_UID);
		when(cmsAdminContentSlotService.getContentSlotForId(INVALID_SLOT_UID))
		.thenThrow(new UnknownIdentifierException("slot not found"));

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailAddComponentToContentSlot_AmbiguousSlot() throws CMSItemNotFoundException
	{
		pageContentSlotComponentData.setSlotId(INVALID_SLOT_UID);
		when(cmsAdminContentSlotService.getContentSlotForId(INVALID_SLOT_UID))
		.thenThrow(new AmbiguousIdentifierException("slot not found"));

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test
	public void shouldRemoveComponent() throws CMSItemNotFoundException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(contentSlot.getCmsComponents()).thenReturn(asList(component));

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);

		verify(cmsAdminComponentService).removeCMSComponentFromContentSlot(component, contentSlot);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailRemoveComponent_NotFoundComponent() throws CMSItemNotFoundException, ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenThrow(
				new AmbiguousIdentifierException("component_not_found"));
		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}


	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailRemoveComponentFromContentSlot_AmbiguousSlot() throws CMSItemNotFoundException,
	ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenThrow(
				new AmbiguousIdentifierException("ambiguous_slot_id"));

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}

	@Test(expected = ComponentNotFoundInSlotException.class)
	public void shouldFailRemoveComponent_ComponentNotInSlot() throws CMSItemNotFoundException, ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(contentSlot.getCmsComponents()).thenReturn(Collections.emptyList());

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}

	@Test
	public void itShouldMoveAComponentFromOneSlotToAnotherOne() throws Exception
	{
		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				buildPageContentSlotComponent());

		verify(cmsAdminContentSlotService).addCMSComponentToContentSlot(any(AbstractCMSComponentModel.class),
				any(ContentSlotModel.class), any(Integer.class));

		verify(cmsAdminComponentService).removeCMSComponentFromContentSlot(any(AbstractCMSComponentModel.class),
				any(ContentSlotModel.class));
	}

	@Test(expected = ValidationException.class)
	public void itShouldThrowValidationExceptionWhenMovingAPageContentSlotComponentWithDataIssues() throws Exception
	{
		final PageContentSlotComponentData pageContentSlotComponentData = buildPageContentSlotComponent();

		doThrow(new ValidationException("exception")).when(facadeValidationService)
				.validate(updatePageContentSlotComponentValidator, pageContentSlotComponentData);

		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				pageContentSlotComponentData);
	}
	
	@Test(expected = ValidationException.class)
	public void itShouldThrowValidationExceptionWhenMovingAComponentIntoASlotThatAlreadyHasAnInstance() throws Exception
	{
		final PageContentSlotComponentData pageContentSlotComponentData = buildPageContentSlotComponent();

		doThrow(new ValidationException("exception")).when(facadeValidationService)
				.validate(componentExistsInSlotValidator, pageContentSlotComponentData);

		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void itShouldThrowCMSItemNotFoundExceptionWhenPassedInvalidComponentUid() throws Exception
	{
		final PageContentSlotComponentData pageContentSlotComponentData = buildPageContentSlotComponent();

		when(defaultPageContentSlotComponentFacade.fetchComponent(COMPONENT_UID))
				.thenThrow(new UnknownIdentifierException("message"));

		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				pageContentSlotComponentData);
	}

	@Test
	public void getPageContentSlotComponentsByPageIdShouldReturnListOfPageContentSlotComponentsForValidPageUid() throws Exception
	{
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_UID))
				.thenReturn(page);
		when(cmsAdminContentSlotService.getContentSlotsForPage(page))
				.thenReturn(asList(contentSlotData, secondContentSlotData, thirdContentSlotData));
		when(contentSlotData.getUid()).thenReturn(SLOT_UID);
		when(contentSlotData.getContentSlot()).thenReturn(contentSlot);
		when(secondContentSlotData.getUid()).thenReturn(SECOND_SLOT_UID);
		when(secondContentSlotData.getContentSlot()).thenReturn(secondContentSlot);
		when(thirdContentSlotData.getUid()).thenReturn(THIRD_SLOT_UID);
		when(thirdContentSlotData.getContentSlot()).thenReturn(thirdContentSlot);
		when(cmsAdminComponentService.getDisplayedComponentsForContentSlot(contentSlot))
				.thenReturn(asList(component, secondComponent));
		when(cmsAdminComponentService.getDisplayedComponentsForContentSlot(secondContentSlot))
				.thenReturn(singletonList(thirdComponent));
		when(cmsAdminComponentService.getDisplayedComponentsForContentSlot(thirdContentSlot)).thenReturn(emptyList());
		when(component.getUid()).thenReturn(COMPONENT_UID);
		when(secondComponent.getUid()).thenReturn(SECOND_COMPONENT_UID);
		when(thirdComponent.getUid()).thenReturn(THIRD_COMPONENT_UID);

		final List<PageContentSlotComponentData> actual = defaultPageContentSlotComponentFacade.getPageContentSlotComponentsByPageId(
				PAGE_UID);

		assertThat(actual.size(), is(3));

		assertThat(actual.get(0).getPageId(), is(PAGE_UID));
		assertThat(actual.get(0).getComponentId(), is(COMPONENT_UID));
		assertThat(actual.get(0).getSlotId(), is(SLOT_UID));
		assertThat(actual.get(0).getPosition(), is(0));

		assertThat(actual.get(1).getPageId(), is(PAGE_UID));
		assertThat(actual.get(1).getComponentId(), is(SECOND_COMPONENT_UID));
		assertThat(actual.get(1).getSlotId(), is(SLOT_UID));
		assertThat(actual.get(1).getPosition(), is(1));

		assertThat(actual.get(2).getPageId(), is(PAGE_UID));
		assertThat(actual.get(2).getComponentId(), is(THIRD_COMPONENT_UID));
		assertThat(actual.get(2).getSlotId(), is(SECOND_SLOT_UID));
		assertThat(actual.get(2).getPosition(), is(0));
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void getPageContentSlotComponentsByPageIdShouldThrowExceptionForInvalidPageUid() throws Exception
	{
		when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_UID))
				.thenThrow(new UnknownIdentifierException("message"));

		defaultPageContentSlotComponentFacade.getPageContentSlotComponentsByPageId(PAGE_UID);
	}

	protected PageContentSlotComponentData buildPageContentSlotComponent()
	{
		final PageContentSlotComponentData contentSlotComponent = new PageContentSlotComponentData();
		contentSlotComponent.setComponentId(COMPONENT_UID);
		contentSlotComponent.setPosition(INDEX);
		contentSlotComponent.setSlotId(SLOT_UID);
		return contentSlotComponent;
	}
}
