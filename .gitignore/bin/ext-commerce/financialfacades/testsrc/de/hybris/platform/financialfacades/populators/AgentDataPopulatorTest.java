/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */
package de.hybris.platform.financialfacades.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.util.ConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


/**
 * Test suite for {@link AgentDataPopulator}.
 */
@UnitTest
public class AgentDataPopulatorTest
{
	private static final String CATEGORY_CODE = "catCode";

	private static final String CATEGORY_NAME = "catName";

	private AbstractPopulatingConverter<AgentModel, AgentData> agentConverter;

	@InjectMocks
	private AgentDataPopulator<AgentModel, AgentData> agentDataPopulator;
	@Mock
	private Converter<CategoryModel, CategoryData> categoryConverter;
	@Mock
	private Converter<AddressModel, AddressData> addressConverter;

	@Before
	public void setup()
	{
		agentDataPopulator = new AgentDataPopulator<>();
		MockitoAnnotations.initMocks(this);

		final CategoryData categoryData = new CategoryData();
		categoryData.setCode(CATEGORY_CODE);
		categoryData.setName(CATEGORY_NAME);
		Mockito.when(categoryConverter.convert(Mockito.any(CategoryModel.class))).thenReturn(categoryData);

		final AddressData addressData = new AddressData();
		Mockito.when(addressConverter.convert(Mockito.any(AddressModel.class))).thenReturn(addressData);
	}

	@Test
	public void testPopulate()
	{
		final AgentModel source = new AgentModel();
		final AddressModel addressModel = Mockito.mock(AddressModel.class);
		addressModel.setPhone1("(415) 512-2100");
		addressModel.setEmail("google@google.com");
		addressModel.setStreetname("1 Dr Carlton B Goodlett Pl");
		addressModel.setStreetnumber("21");
		addressModel.setPostalcode("644020");
		addressModel.setTown("Weimar");
		addressModel.setFirstname("John");
		addressModel.setLastname("Zoidberg");

		final CategoryModel category = Mockito.mock(CategoryModel.class);
		source.setCategories(Lists.<CategoryModel> newArrayList(category));
		source.setEnquiry(addressModel);
		source.getEnquiry().getPhone1();
		final MediaModel mediaModel = Mockito.mock(MediaModel.class);
		Mockito.when(mediaModel.getURL()).thenReturn("image.png");
		source.setThumbnail(mediaModel);
		Mockito.when(category.getCode()).thenReturn(CATEGORY_CODE);
		Mockito.when(category.getName()).thenReturn(CATEGORY_NAME);
		final AgentData result = new AgentData();
		agentDataPopulator.populate(source, result);

		Assert.assertEquals(source.getEnquiry().getPhone1(), result.getEnquiryData().getPhone());
		Assert.assertEquals(source.getEnquiry().getEmail(), result.getEnquiryData().getEmail());
		Assert.assertEquals(source.getEnquiry().getStreetname(), result.getEnquiryData().getLine1());
		Assert.assertEquals(source.getEnquiry().getPostalcode(), result.getEnquiryData().getPostalCode());
		Assert.assertEquals(source.getEnquiry().getTown(), result.getEnquiryData().getTown());
		Assert.assertEquals(source.getEnquiry().getFirstname(), result.getEnquiryData().getFirstName());
		Assert.assertEquals(source.getEnquiry().getLastname(), result.getEnquiryData().getLastName());

		final CategoryData resultCategory = result.<CategoryData> getCategories().get(0);
		final CategoryModel sourceCategory = source.<CategoryModel> getCategories().iterator().next();
		Assert.assertNotNull(sourceCategory.getCode());
		Assert.assertNotNull(resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getCode(), resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getName(), resultCategory.getName());
		Assert.assertNotNull(result.getThumbnail());
		Assert.assertEquals(source.getThumbnail().getURL(), result.getThumbnail().getUrl());

		Mockito.verify(addressConverter).convert(addressModel);
		Mockito.verify(categoryConverter).convert(sourceCategory);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertNull()
	{
		agentConverter = new ConverterFactory<AgentModel, AgentData, AgentDataPopulator>().create(AgentData.class,
				agentDataPopulator);
		agentConverter.convert(null);
	}

	@Test
	public void testPopulateEmptyAddress()
	{
		final AgentModel source = new AgentModel();
		final AddressModel addressModel = Mockito.mock(AddressModel.class);
		final CategoryModel category = Mockito.mock(CategoryModel.class);

		source.setCategories(Lists.<CategoryModel> newArrayList(category));
		source.setEnquiry(addressModel);
		source.getEnquiry().getPhone1();
		Mockito.when(category.getCode()).thenReturn(CATEGORY_CODE);
		Mockito.when(category.getName()).thenReturn(CATEGORY_NAME);
		final AgentData result = new AgentData();
		agentDataPopulator.populate(source, result);

		final CategoryData resultCategory = result.<CategoryData> getCategories().get(0);
		final CategoryModel sourceCategory = source.<CategoryModel> getCategories().iterator().next();
		Assert.assertNotNull(sourceCategory.getCode());
		Assert.assertNotNull(resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getCode(), resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getName(), resultCategory.getName());

		Mockito.verify(addressConverter).convert(addressModel);
		Mockito.verify(categoryConverter).convert(sourceCategory);
	}
}
