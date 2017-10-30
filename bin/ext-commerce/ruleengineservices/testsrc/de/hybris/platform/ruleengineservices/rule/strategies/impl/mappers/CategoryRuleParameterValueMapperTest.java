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
 *
 *
 */
package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CategoryRuleParameterValueMapperTest
{
	private static final String ANY_STRING = "anyString";

	@Rule
	public final ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private CategoryService categoryService;

	@Mock
	private CategoryModel category;

	@InjectMocks
	private final CategoryRuleParameterValueMapper mapper = new CategoryRuleParameterValueMapper();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void nullTestFromString() throws RuleParameterValueMapperException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		mapper.fromString(null);
	}

	@Test
	public void nullTestToString() throws RuleParameterValueMapperException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		mapper.toString(null);
	}

	@Test
	public void noCategoryFoundTest() throws RuleParameterValueMapperException
	{
		//given
		BDDMockito.given(categoryService.getCategoriesForCode(Mockito.anyString())).willReturn(null);

		//expect
		expectedException.expect(RuleParameterValueMapperException.class);

		//when
		mapper.fromString(ANY_STRING);
	}

	@Test
	public void mappedCategoryTest() throws RuleParameterValueMapperException
	{
		//given
		final List<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(Mockito.mock(CategoryModel.class));
		categories.add(Mockito.mock(CategoryModel.class));

		BDDMockito.given(categoryService.getCategoriesForCode(Mockito.anyString())).willReturn(categories);

		//when
		final CategoryModel mappedCategory = mapper.fromString(ANY_STRING);

		//then
		Assert.assertTrue(categories.contains(mappedCategory));
	}

	@Test
	public void mappedCategoryIsFirstFoundTest() throws RuleParameterValueMapperException
	{
		//given
		final List<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(Mockito.mock(CategoryModel.class));
		categories.add(Mockito.mock(CategoryModel.class));

		BDDMockito.given(categoryService.getCategoriesForCode(Mockito.anyString())).willReturn(categories);

		//when
		final CategoryModel mappedCategory = mapper.fromString(ANY_STRING);

		//then
		Assert.assertEquals(categories.get(0), mappedCategory);
	}

	@Test
	public void objectToStringTest() throws RuleParameterValueMapperException
	{
		//given
		givenStringRepresentationAttribute();

		//when
		final String stringRepresentation = mapper.toString(category);

		//then
		Assert.assertEquals(ANY_STRING, stringRepresentation);
	}

	private void givenStringRepresentationAttribute()
	{
		BDDMockito.given(category.getCode()).willReturn(ANY_STRING);
	}
}
