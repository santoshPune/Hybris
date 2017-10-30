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
package de.hybris.platform.cmswebservices.pages.facade.impl;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.data.AbstractPageData;
import de.hybris.platform.cmswebservices.exception.ValidationException;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jersey.repackaged.com.google.common.collect.Sets;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageFacadeTest
{
	private static final String PAGE_UID_001 = "page_uid_001";
	private static final String UNIQUE_KEY = "uniqueKey";
	@InjectMocks
	private DefaultPageFacade pageFacade;
	@Mock
	private CMSAdminPageService adminPageService;
	@Mock
	private ModelService modelService;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private KeyGenerator keyGenerator;
	@Spy
	private HashMap<Class<?>, Validator> createPageValidatorFactory;
	@Spy
	private HashMap<Class<?>, AbstractPopulatingConverter<AbstractPageData, AbstractPageModel>> pageDataPopulatorFactory;
	@Spy
	private HashMap<Class<?>, AbstractPopulatingConverter<AbstractPageModel, AbstractPageData>> pageModelConverterFactory;
	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private ContentPageModel contentPageModel;
	@Mock
	private ProductPageModel productPageModel;
	@Mock
	private EmailPageModel emailPageModel;

	private Collection<AbstractPageModel> pages;

	@Mock
	private AbstractPageData pageData1;
	@Mock
	private AbstractPageData pageData2;

	@Before
	public void setup()
	{
		when(keyGenerator.generate()).thenReturn(UNIQUE_KEY);
		pageFacade.setSupportedPages(Sets.newHashSet(contentPageModel.getClass(), productPageModel.getClass()));
		pages = asList(contentPageModel, productPageModel, emailPageModel);
	}

	@Test
	public void findAllPagesReturnTransformedCollectionOfAllSupportedPagesOrderByTitleAscending()
	{
		when(pageData1.getName()).thenReturn("Bname");
		when(pageData2.getName()).thenReturn("Aname");

		when(adminPageService.getAllPages()).thenReturn(pages);

		final AbstractPopulatingConverter<AbstractPageModel, AbstractPageData> convertModel = mock(AbstractPopulatingConverter.class);
		when(convertModel.convert(contentPageModel)).thenReturn(pageData1);
		when(convertModel.convert(productPageModel)).thenReturn(pageData2);
		pageModelConverterFactory.put(contentPageModel.getClass(), convertModel);
		pageModelConverterFactory.put(productPageModel.getClass(), convertModel);

		final List<AbstractPageData> results = pageFacade.findAllPages();
		assertThat(results.size(), is(equalTo(2)));
		assertThat(results.get(0), is(pageData2));
		assertThat(results.get(1), is(pageData1));
	}

	@Test
	public void findPagesReturnsEmptyList()
	{
		when(adminPageService.getAllPages()).thenReturn(Collections.<AbstractPageModel> emptyList());

		final AbstractPopulatingConverter<AbstractPageModel, AbstractPageData> convertModel = mock(AbstractPopulatingConverter.class);
		when(convertModel.convert(contentPageModel)).thenReturn(pageData1);
		when(convertModel.convert(productPageModel)).thenReturn(pageData2);
		pageModelConverterFactory.put(contentPageModel.getClass(), convertModel);

		final List<AbstractPageData> results = pageFacade.findAllPages();
		assertThat(results.size(), is(0));
		verify(convertModel, never()).convert(any(AbstractPageModel.class));
	}

	@Test
	public void shouldCreatePage()
	{
		createPageValidatorFactory.put(pageData1.getClass(), mock(Validator.class));

		final AbstractPopulatingConverter<AbstractPageData, AbstractPageModel> convertData = mock(AbstractPopulatingConverter.class);
		when(convertData.convert(pageData1)).thenReturn(contentPageModel);
		pageDataPopulatorFactory.put(pageData1.getClass(), convertData);

		final AbstractPopulatingConverter<AbstractPageModel, AbstractPageData> convertModel = mock(AbstractPopulatingConverter.class);
		when(convertModel.convert(contentPageModel)).thenReturn(pageData1);
		pageModelConverterFactory.put(contentPageModel.getClass(), convertModel);

		final AbstractPageData result = pageFacade.createPage(pageData1);
		assertThat(result, is(pageData1));
		verify(modelService, times(1)).save(contentPageModel);
	}


	@Test(expected = IllegalArgumentException.class)
	public void shouldNotFindValidator()
	{
		pageFacade.createPage(pageData1);
	}

	@Test
	public void shouldNotFindPopulatorForPageData()
	{
		try
		{
			createPageValidatorFactory.put(pageData1.getClass(), mock(Validator.class));
			pageFacade.createPage(pageData1);
			fail();
		}
		catch (final ConversionException e)
		{
			verify(modelService, times(0)).save(contentPageModel);
		}
	}

	@Test
	public void shouldNotFindPopulatorForPageModel()
	{
		try
		{
			final AbstractPopulatingConverter<AbstractPageData, AbstractPageModel> convertData = mock(AbstractPopulatingConverter.class);
			when(convertData.convert(pageData1)).thenReturn(contentPageModel);
			pageDataPopulatorFactory.put(pageData1.getClass(), convertData);
			createPageValidatorFactory.put(pageData1.getClass(), mock(Validator.class));
			pageFacade.createPage(pageData1);
			fail();
		}
		catch (final ConversionException e)
		{
			verify(modelService, times(1)).save(contentPageModel);
		}
	}

	@Test
	public void should_generate_uid_with_prefix_when_uid_not_provided()
	{
		when(keyGenerator.generate()).thenReturn(UNIQUE_KEY);
		final AbstractPageData pageData = new AbstractPageData();
		pageFacade.generateUID(pageData);
		assertThat(pageData.getUid(), Matchers.is(DefaultPageFacade.DEFAULT_UID_PREFIX + "_" + UNIQUE_KEY));
	}

	@Test
	public void should_generate_uid_without_prefix_when_uid_not_provided()
	{
		when(keyGenerator.generate()).thenReturn(UNIQUE_KEY);
		final AbstractPageData pageData = new AbstractPageData();
		pageFacade.setUidPrefix(null);
		pageFacade.generateUID(pageData);
		assertThat(pageData.getUid(), Matchers.is(UNIQUE_KEY));
	}

	@Test
	public void should_not_generate_uid_when_uid_provided()
	{
		final AbstractPageData pageData = new AbstractPageData();
		pageData.setUid(PAGE_UID_001);
		pageFacade.generateUID(pageData);
		assertThat(pageData.getUid(), Matchers.is(PAGE_UID_001));
	}

	@Test(expected = ValidationException.class)
	public void should_thrown_exception_when_validation_fails()
	{
		final Validator validatorMock = mock(Validator.class);
		final AbstractPageData pageData = new AbstractPageData();
		doThrow(new ValidationException(null)).when(facadeValidationService).validate(validatorMock, pageData);
		createPageValidatorFactory.put(pageData.getClass(), validatorMock);
		pageFacade.validate(pageData, pageData.getClass());
	}
}
