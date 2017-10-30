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
package de.hybris.platform.cmswebservices.items.facade.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.search.pagedata.PageableData;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.comparator.ItemModifiedTimeComparator;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.BannerComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.exception.ValidationException;
import de.hybris.platform.cmswebservices.items.facade.converter.CmsComponentConverterFactory;
import de.hybris.platform.cmswebservices.items.facade.populator.model.BasicCMSComponentModelPopulator;
import de.hybris.platform.cmswebservices.items.facade.populator.model.CmsParagraphComponentModelPopulator;
import de.hybris.platform.cmswebservices.populator.ComponentDataPopulatorFactory;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentItemFacadeTest
{
	@Spy
	@InjectMocks
	private DefaultComponentItemFacade componentFacade;
	@Mock
	private CMSAdminComponentService cmsAdminComponentService;
	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSAdminSiteService adminSiteService;
	@Mock
	private ComponentDataPopulatorFactory componentDataPopulatorFactory;;
	@Mock
	private ModelService modelService;
	@Mock
	private Populator populator;
	@Mock
	private Validator createComponentValidator;
	@Mock
	private Validator updateComponentValidator;
	@Mock
	private FacadeValidationService facadeValidationService;

	@Mock
	private PageableData pageableData;
	@Mock
	private SearchResult<AbstractCMSComponentModel> searchResultForModels;

	private AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> abstractPopulatingConverter;

	@Before
	public void setup()
	{
		abstractPopulatingConverter = new AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>()
		{

		};
		abstractPopulatingConverter.setTargetClass(AbstractCMSComponentData.class);
		abstractPopulatingConverter.setPopulators(Arrays.asList(new BasicCMSComponentModelPopulator()));
		componentFacade.setCmsItemComparator(new ItemModifiedTimeComparator());
		componentFacade.setBasicCMSComponentModelConverter(abstractPopulatingConverter);
	}


	@Test
	public void shouldGetComponentItems()
	{
		final ArrayList<AbstractCMSComponentModel> customizedComponents = new ArrayList<>();
		final AbstractCMSComponentModel component = spy(new AbstractCMSComponentModel());
		final ContentSlotModel contentSlotModel = spy(new ContentSlotModel());
		final CatalogVersionModel catalogVersionModel = spy(new CatalogVersionModel());
		when(component.getPk()).thenReturn(PK.BIG_PK);

		final String uid = "123456";
		component.setUid(uid);

		final String name = "BannerComponent";
		component.setName(name);

		customizedComponents.add(component);
		when(adminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);
		when(cmsAdminContentSlotService.getContentSlotsForCatalogVersion(catalogVersionModel))
		.thenReturn(Collections.singleton(contentSlotModel));
		when(cmsAdminComponentService.getAllCMSComponentsForCatalogVersion(catalogVersionModel)).thenReturn(customizedComponents);


		final List<AbstractCMSComponentData> allComponentItems = componentFacade.getAllComponentItems();
		final AbstractCMSComponentData cmsComponentData = allComponentItems.get(0);

		assertFalse(allComponentItems.isEmpty());
		assertEquals(uid, cmsComponentData.getUid());
		assertEquals(name, cmsComponentData.getName());
	}

	@Test
	public void shouldGetPageOfComponentItems()
	{
		final String mask = "somemask";

		final ArrayList<AbstractCMSComponentModel> customizedComponents = new ArrayList<>();
		final AbstractCMSComponentModel component = spy(new AbstractCMSComponentModel());
		final ContentSlotModel contentSlotModel = spy(new ContentSlotModel());
		final CatalogVersionModel catalogVersionModel = spy(new CatalogVersionModel());
		when(component.getPk()).thenReturn(PK.BIG_PK);

		final String uid = "123456";
		component.setUid(uid);

		final String name = "BannerComponent";
		component.setName(name);

		customizedComponents.add(component);
		when(adminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersionModel);
		when(cmsAdminContentSlotService.getContentSlotsForCatalogVersion(catalogVersionModel))
		.thenReturn(Collections.singleton(contentSlotModel));

		when(searchResultForModels.getResult()).thenReturn(customizedComponents);
		when(searchResultForModels.getTotalCount()).thenReturn(5);
		when(cmsAdminComponentService.findByCatalogVersionAndMask(catalogVersionModel, mask, pageableData))
		.thenReturn(searchResultForModels);


		final SearchResult<AbstractCMSComponentData> page = componentFacade.findComponentByMask(mask, pageableData);

		assertThat("facade did not recive totalCount from cmsAdminComponentService", page.getTotalCount(), is(5));

		assertThat("number of results in page is unexpected", page.getResult().size(), is(1));
		assertThat("single result in page has the right uid", page.getResult().get(0).getUid(), is(uid));
		assertThat("single result in page has the right name", page.getResult().get(0).getName(), is(name));

	}

	@Test
	public void shouldAddComponentItem()
	{
		final AbstractCMSComponentData componentData = new AbstractCMSComponentData();
		componentData.setSlotId("testSlotId");
		componentData.setPosition(0);

		final ContentSlotModel contentSlot = new ContentSlotModel();
		final AbstractCMSComponentModel component = Mockito.spy(new CMSParagraphComponentModel());
		when(component.getPk()).then(inv -> PK.BIG_PK);

		when(cmsAdminContentSlotService.getContentSlotForId(componentData.getSlotId())).thenReturn(contentSlot);
		when(
				cmsAdminComponentService.createCmsComponent(contentSlot, componentData.getUid(), componentData.getName(),
						componentData.getTypeCode())).thenReturn(component);

		final CmsParagraphComponentModelPopulator paragraphPopulator = Mockito.mock(CmsParagraphComponentModelPopulator.class);

		final CmsComponentConverterFactory cmsComponentConverterFactory = Mockito.mock(CmsComponentConverterFactory.class);

		final AbstractPopulatingConverter converter = new MockedAbstractPopulatingConverter();
		converter.setPopulators(Arrays.asList(new BasicCMSComponentModelPopulator(), paragraphPopulator));
		converter.setTargetClass(CMSParagraphComponentData.class);

		when(cmsComponentConverterFactory.getConverter(component.getClass())).then(inv -> Optional.of(converter));
		componentFacade.setCmsComponentConverterFactory(cmsComponentConverterFactory);


		componentFacade.addComponentItem(componentData);
		verify(cmsAdminContentSlotService).addCMSComponentToContentSlot(component, contentSlot, 0);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailAddComponentItem_ValidationErrors()
	{
		final AbstractCMSComponentData componentData = new AbstractCMSComponentData();
		doThrow(new ValidationException("exception")).when(facadeValidationService).validate(Mockito.any(), Mockito.any());

		componentFacade.addComponentItem(componentData);
	}

	@Test
	public void shouldGetACMSParagraphComponentItem() throws Exception
	{
		final String uidParam = "EmptyCartParagraphComponent";

		final AbstractCMSComponentModel abstractCMSComponentModel = Mockito.spy(new CMSParagraphComponentModel());
		when(abstractCMSComponentModel.getPk()).then(inv -> PK.BIG_PK);

		final CmsParagraphComponentModelPopulator paragraphPopulator = Mockito.mock(CmsParagraphComponentModelPopulator.class);

		when(cmsAdminComponentService.getCMSComponentForId(uidParam)).thenReturn(abstractCMSComponentModel);

		final CmsComponentConverterFactory cmsComponentConverterFactory = Mockito.mock(CmsComponentConverterFactory.class);

		final AbstractPopulatingConverter converter = new MockedAbstractPopulatingConverter();
		converter.setPopulators(Arrays.asList(new BasicCMSComponentModelPopulator(), paragraphPopulator));
		converter.setTargetClass(CMSParagraphComponentData.class);

		when(cmsComponentConverterFactory.getConverter(abstractCMSComponentModel.getClass())).then(inv -> Optional.of(converter));
		componentFacade.setCmsComponentConverterFactory(cmsComponentConverterFactory);

		final AbstractCMSComponentData abstractCMSComponentData = componentFacade.getComponentItemByUid(uidParam);

		verify(cmsAdminComponentService).getCMSComponentForId(uidParam);

		assertEquals(CMSParagraphComponentData.class, abstractCMSComponentData.getClass());
	}

	@Test
	public void shouldUpdateComponent() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();
		final BannerComponentModel component = new BannerComponentModel();

		when(cmsAdminComponentService.getCMSComponentForId(componentData.getUid())).thenReturn(component);
		when(componentDataPopulatorFactory.getPopulator(component.getClass())).thenReturn(Optional.of(populator));

		componentFacade.updateComponentItem(componentData.getUid(), componentData);

		verify(populator).populate(componentData, component);
		verify(modelService).saveAll();
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldUpdateComponent_componentNotFound() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();

		when(cmsAdminComponentService.getCMSComponentForId(componentData.getUid())).thenThrow(new UnknownIdentifierException(""));
		componentFacade.updateComponentItem(componentData.getUid(), componentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldRemoveComponent_componentNotFound() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();

		when(cmsAdminComponentService.getCMSComponentForId(componentData.getUid())).thenThrow(new UnknownIdentifierException(""));
		componentFacade.removeComponentItem(componentData.getUid());
	}

	@Test
	public void shouldRemoveComponent() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();
		final BannerComponentModel component = new BannerComponentModel();

		when(cmsAdminComponentService.getCMSComponentForId(componentData.getUid())).thenReturn(component);
		componentFacade.removeComponentItem(componentData.getUid());

		verify(modelService).remove(component);
	}

	@Test(expected = ConversionException.class)
	public void shouldUpdateComponent_populatorNotFound() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();
		final BannerComponentModel component = new BannerComponentModel();

		when(cmsAdminComponentService.getCMSComponentForId(componentData.getUid())).thenReturn(component);
		when(componentDataPopulatorFactory.getPopulator(component.getClass())).thenThrow(new ConversionException(""));

		componentFacade.updateComponentItem(componentData.getUid(), componentData);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailUpdateComponent_ValidationException() throws Exception
	{
		final BannerComponentData componentData = new BannerComponentData();
		doThrow(new ValidationException("exception")).when(facadeValidationService).validate(Mockito.any(), Mockito.any(),
				Mockito.any());

		componentFacade.updateComponentItem(componentData.getUid(), componentData);
	}

}

class MockedAbstractPopulatingConverter extends AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>
{
	//Fix the fact that AbstractPopulatingConverter is abstract
}
