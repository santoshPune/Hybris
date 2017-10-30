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
package de.hybris.platform.cmswebservices.types.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmswebservices.types.service.ComponentTypeStructure;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;


@UnitTest
public class DefaultComponentTypeStructureRegistryTest
{
	private static final String INVALID = "INVALID";
	private static final String TYPECODE_A = "A";
	private static final String TYPECODE_B = "B";
	private static final String TYPECODE_C = "C";

	private static final String QUALIFIER_1 = "1";
	private static final String QUALIFIER_2 = "2";
	private static final String QUALIFIER_3 = "3";

	private DefaultComponentTypeStructureRegistry registry;

	private ComponentTypeStructure defaultType;
	private ComponentTypeStructure typeA;
	private ComponentTypeStructure typeAplus;
	private ComponentTypeStructure typeB;
	private ComponentTypeStructure typeC;

	private ComponentTypeAttributeStructure attributeA1;
	private ComponentTypeAttributeStructure attributeA1plus;
	private ComponentTypeAttributeStructure attributeA2;
	private ComponentTypeAttributeStructure attributeA3;
	private ComponentTypeAttributeStructure attributeB1;
	private ComponentTypeAttributeStructure attributeB2;
	private ComponentTypeAttributeStructure attributeC1;

	private Populator<ComposedTypeModel, ComponentTypeData> pop1;
	private Populator<ComposedTypeModel, ComponentTypeData> pop2;
	private Populator<ComposedTypeModel, ComponentTypeData> pop3;
	private Populator<ComposedTypeModel, ComponentTypeData> pop4;

	private Populator<AttributeDescriptorModel, ComponentTypeAttributeData> popA;
	private Populator<AttributeDescriptorModel, ComponentTypeAttributeData> popB;
	private Populator<AttributeDescriptorModel, ComponentTypeAttributeData> popC;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		registry = new DefaultComponentTypeStructureRegistry();

		pop1 = Mockito.mock(Populator.class);
		pop2 = Mockito.mock(Populator.class);
		pop3 = Mockito.mock(Populator.class);
		pop4 = Mockito.mock(Populator.class);
		popA = Mockito.mock(Populator.class);
		popB = Mockito.mock(Populator.class);
		popC = Mockito.mock(Populator.class);

		defaultType = new DefaultComponentTypeStructure();
		defaultType.setTypecode(AbstractCMSComponentModel._TYPECODE);
		defaultType.setPopulators(Sets.newHashSet(pop1, pop2));

		typeA = new DefaultComponentTypeStructure();
		typeA.setTypecode(TYPECODE_A);
		typeA.setPopulators(Sets.newHashSet(pop1, pop2));

		typeAplus = new DefaultComponentTypeStructure();
		typeAplus.setTypecode(TYPECODE_A);
		typeAplus.setPopulators(Sets.newHashSet(pop2, pop3, pop4));

		typeB = new DefaultComponentTypeStructure();
		typeB.setTypecode(TYPECODE_B);
		typeB.setPopulators(Sets.newHashSet(pop1, pop2));

		typeC = new DefaultComponentTypeStructure();
		typeC.setTypecode(TYPECODE_C);
		typeC.setPopulators(Sets.newHashSet(pop1, pop2));


		attributeA1 = new DefaultComponentTypeAttributeStructure();
		attributeA1.setTypecode(TYPECODE_A);
		attributeA1.setQualifier(QUALIFIER_1);
		attributeA1.setPopulators(Sets.newHashSet(popA, popB));

		attributeA1plus = new DefaultComponentTypeAttributeStructure();
		attributeA1plus.setTypecode(TYPECODE_A);
		attributeA1plus.setQualifier(QUALIFIER_1);
		attributeA1plus.setPopulators(Sets.newHashSet(popA, popB, popC));

		attributeA2 = new DefaultComponentTypeAttributeStructure();
		attributeA2.setTypecode(TYPECODE_A);
		attributeA2.setQualifier(QUALIFIER_2);
		attributeA2.setPopulators(Sets.newHashSet(popA, popB));

		attributeA3 = new DefaultComponentTypeAttributeStructure();
		attributeA3.setTypecode(TYPECODE_A);
		attributeA3.setQualifier(QUALIFIER_3);
		attributeA3.setPopulators(Sets.newHashSet(popA, popB, popC));

		attributeB1 = new DefaultComponentTypeAttributeStructure();
		attributeB1.setTypecode(TYPECODE_B);
		attributeB1.setQualifier(QUALIFIER_1);
		attributeB1.setPopulators(Sets.newHashSet(popA, popB));

		attributeB2 = new DefaultComponentTypeAttributeStructure();
		attributeB2.setTypecode(TYPECODE_B);
		attributeB2.setQualifier(QUALIFIER_2);
		attributeB2.setPopulators(Sets.newHashSet(popA, popB));

		attributeC1 = new DefaultComponentTypeAttributeStructure();
		attributeC1.setTypecode(TYPECODE_C);
		attributeC1.setQualifier(QUALIFIER_1);
		attributeC1.setPopulators(Sets.newHashSet(popC));
	}

	@Test
	public void shouldBuildElementsCorrectly_AllUseCasesCovered() throws Exception
	{
		registry.setAllComponentTypeStructures(Sets.newHashSet(typeA, typeAplus, typeB, typeC));
		registry.setAllComponentTypeAttributeStructures(
				Sets.newHashSet(attributeA1, attributeA1plus, attributeA2, attributeA3, attributeB1, attributeB2, attributeC1));

		registry.afterPropertiesSet();
		final Map<String, ComponentTypeStructure> types = registry.getComponentTypeStructureMap();

		assertThat(types.size(), equalTo(3));

		assertThat(getType(TYPECODE_A).getTypecode(), equalTo(TYPECODE_A));
		assertThat(getType(TYPECODE_B).getTypecode(), equalTo(TYPECODE_B));
		assertThat(getType(TYPECODE_C).getTypecode(), equalTo(TYPECODE_C));

		assertThat(getType(TYPECODE_A).getPopulators().size(), equalTo(4));
		assertThat(getType(TYPECODE_A).getPopulators(), hasItem(pop1));
		assertThat(getType(TYPECODE_A).getPopulators(), hasItem(pop2));
		assertThat(getType(TYPECODE_A).getPopulators(), hasItem(pop3));
		assertThat(getType(TYPECODE_A).getPopulators(), hasItem(pop4));

		assertThat(getType(TYPECODE_B).getPopulators().size(), equalTo(2));
		assertThat(getType(TYPECODE_B).getPopulators(), hasItem(pop1));
		assertThat(getType(TYPECODE_B).getPopulators(), hasItem(pop2));

		assertThat(getType(TYPECODE_C).getPopulators().size(), equalTo(2));
		assertThat(getType(TYPECODE_C).getPopulators(), hasItem(pop1));
		assertThat(getType(TYPECODE_C).getPopulators(), hasItem(pop2));

		assertThat(getType(TYPECODE_A).getAttributes().size(), equalTo(3));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getTypecode(), equalTo(TYPECODE_A));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getQualifier(), equalTo(QUALIFIER_1));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getPopulators().size(), equalTo(3));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getPopulators(), hasItem(popA));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getPopulators(), hasItem(popB));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_1).getPopulators(), hasItem(popC));

		assertThat(getAttribute(TYPECODE_A, QUALIFIER_2).getTypecode(), equalTo(TYPECODE_A));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_2).getQualifier(), equalTo(QUALIFIER_2));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_2).getPopulators().size(), equalTo(2));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_2).getPopulators(), hasItem(popA));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_2).getPopulators(), hasItem(popB));

		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getTypecode(), equalTo(TYPECODE_A));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getQualifier(), equalTo(QUALIFIER_3));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getPopulators().size(), equalTo(3));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getPopulators(), hasItem(popA));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getPopulators(), hasItem(popB));
		assertThat(getAttribute(TYPECODE_A, QUALIFIER_3).getPopulators(), hasItem(popC));

		assertThat(getType(TYPECODE_B).getAttributes().size(), equalTo(2));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_1).getTypecode(), equalTo(TYPECODE_B));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_1).getQualifier(), equalTo(QUALIFIER_1));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_1).getPopulators().size(), equalTo(2));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_1).getPopulators(), hasItem(popA));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_1).getPopulators(), hasItem(popB));

		assertThat(getAttribute(TYPECODE_B, QUALIFIER_2).getTypecode(), equalTo(TYPECODE_B));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_2).getQualifier(), equalTo(QUALIFIER_2));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_2).getPopulators().size(), equalTo(2));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_2).getPopulators(), hasItem(popA));
		assertThat(getAttribute(TYPECODE_B, QUALIFIER_2).getPopulators(), hasItem(popB));

		assertThat(getType(TYPECODE_C).getAttributes().size(), equalTo(1));
		assertThat(getAttribute(TYPECODE_C, QUALIFIER_1).getTypecode(), equalTo(TYPECODE_C));
		assertThat(getAttribute(TYPECODE_C, QUALIFIER_1).getQualifier(), equalTo(QUALIFIER_1));
		assertThat(getAttribute(TYPECODE_C, QUALIFIER_1).getPopulators().size(), equalTo(1));
		assertThat(getAttribute(TYPECODE_C, QUALIFIER_1).getPopulators(), hasItem(popC));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentException_typecodeNotFoundInAttribute() throws Exception
	{
		final ComponentTypeAttributeStructure attributeInvalid = new DefaultComponentTypeAttributeStructure();
		attributeInvalid.setTypecode(INVALID);
		attributeInvalid.setQualifier(QUALIFIER_1);
		attributeInvalid.setPopulators(Collections.emptySet());

		registry.setAllComponentTypeStructures(Sets.newHashSet(typeA, defaultType));
		registry.setAllComponentTypeAttributeStructures(Sets.newHashSet(attributeInvalid));

		registry.afterPropertiesSet();
	}

	@Test
	public void shouldGetAttributeByTypecodeQualifier() throws Exception
	{
		registry.setAllComponentTypeStructures(Sets.newHashSet(typeA, defaultType));
		registry.setAllComponentTypeAttributeStructures(Sets.newHashSet(attributeA1, attributeA1plus));
		registry.afterPropertiesSet();

		final Optional<ComponentTypeAttributeStructure> attribute = registry.getComponentTypeAttributeStructure(TYPECODE_A,
				QUALIFIER_1);

		assertThat(attribute.get().getTypecode(), equalTo(TYPECODE_A));
		assertThat(attribute.get().getQualifier(), equalTo(QUALIFIER_1));
		assertThat(attribute.get().getPopulators().size(), equalTo(3));
		assertThat(attribute.get().getPopulators(), hasItem(popA));
		assertThat(attribute.get().getPopulators(), hasItem(popB));
		assertThat(attribute.get().getPopulators(), hasItem(popC));
	}

	@Test
	public void shouldGetDefaultAttribute_NoTypecodeMatch() throws Exception
	{
		registry.setAllComponentTypeStructures(Sets.newHashSet(typeA, defaultType));
		registry.setAllComponentTypeAttributeStructures(Sets.newHashSet(attributeA1));
		registry.afterPropertiesSet();

		final Optional<ComponentTypeAttributeStructure> attribute = registry.getComponentTypeAttributeStructure(INVALID,
				QUALIFIER_1);

		assertThat(attribute.isPresent(), is(Boolean.FALSE));
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldNotGetAttribute_NoQualifierMatch() throws Exception
	{
		registry.setAllComponentTypeStructures(Sets.newHashSet(typeA, defaultType));
		registry.setAllComponentTypeAttributeStructures(Sets.newHashSet(attributeA1));
		registry.afterPropertiesSet();

		final Optional<ComponentTypeAttributeStructure> attribute = registry.getComponentTypeAttributeStructure(TYPECODE_A,
				INVALID);

		attribute.get();
	}

	protected ComponentTypeStructure getType(final String typecode)
	{
		return registry.getComponentTypeStructureMap().get(typecode);
	}

	protected ComponentTypeAttributeStructure getAttribute(final String typecode, final String qualifier)
	{
		return getType(typecode).getAttributes().stream() //
				.filter(attribute -> attribute.getQualifier().equals(qualifier)) //
				.findAny()
				.get();
	}
}
