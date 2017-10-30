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
package de.hybris.platform.cmswebservices.types.facade.converter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.types.service.ComponentTypeAttributeStructure;
import de.hybris.platform.cmswebservices.types.service.ComponentTypeStructure;
import de.hybris.platform.cmswebservices.types.service.ComponentTypeStructureRegistry;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ComponentTypeStructureConverterTest
{
	private static final String QUALIFIER1 = "qualifier1";
	private static final String QUALIFIER2 = "qualifier2";
	private static final String QUALIFIER3 = "qualifier3";

	private static final String CODE = "code";

	@InjectMocks
	private ComponentTypeStructureConverter converter;

	@Mock
	private ComponentTypeStructureRegistry componentTypeStructureRegistry;

	@Mock
	private ComposedTypeModel source;
	@Mock
	private AttributeDescriptorModel attribute1, attribute2, attribute3;
	@Mock
	private ComponentTypeStructure structureType;
	@Mock
	private ComponentTypeAttributeStructure structureAttribute1, structureAttribute2;
	@Mock
	private Populator<ComposedTypeModel, ComponentTypeData> popType1, popType2, defaultPopType;
	@Mock
	private Populator<AttributeDescriptorModel, ComponentTypeAttributeData> popAttribute1, popAttribute2;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		when(source.getCode()).thenReturn(CODE);
		when(source.getDeclaredattributedescriptors()).thenReturn(Sets.newHashSet(attribute1));
		when(source.getInheritedattributedescriptors()).thenReturn(Sets.newHashSet(attribute2, attribute3));
		when(attribute1.getQualifier()).thenReturn(QUALIFIER1);
		when(attribute2.getQualifier()).thenReturn(QUALIFIER2);
		when(attribute3.getQualifier()).thenReturn(QUALIFIER3);

		when(structureType.getPopulators()).thenReturn(Sets.newHashSet(popType1, popType2));
		when(structureType.getAttributes()).thenReturn(Sets.newHashSet(structureAttribute1, structureAttribute2));
		when(structureAttribute1.getQualifier()).thenReturn(QUALIFIER1);
		when(structureAttribute1.getPopulators()).thenReturn(Sets.newHashSet(popAttribute1, popAttribute2));
		when(structureAttribute2.getQualifier()).thenReturn(QUALIFIER2);
		when(structureAttribute2.getPopulators()).thenReturn(Sets.newHashSet(popAttribute1, popAttribute2));

		when(componentTypeStructureRegistry.getComponentTypeStructure(CODE)).thenReturn(structureType);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldConvertWithDefault_NoStructureTypeFoundForCode()
	{
		when(structureType.getPopulators()).thenReturn(Sets.newHashSet(defaultPopType));
		final ComponentTypeData target = new ComponentTypeData();

		converter.convert(source, target);

		verify(defaultPopType).populate(source, target);
	}

	@Test
	public void shouldPopulateComponentTypeProperties()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(source, target);

		verify(popType1).populate(source, target);
		verify(popType2).populate(source, target);
	}

	@Test
	public void shouldConvertComponentTypeAttributes()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(source, target);

		verify(popAttribute1).populate(Mockito.eq(attribute1), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2).populate(Mockito.eq(attribute1), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute1).populate(Mockito.eq(attribute2), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2).populate(Mockito.eq(attribute2), Mockito.any(ComponentTypeAttributeData.class));
	}

	@Test
	public void shouldNotConvertComponentTypeAttributesWithNoStructureAttributes()
	{
		final ComponentTypeData target = new ComponentTypeData();
		converter.convert(source, target);

		verify(popAttribute1, times(0)).populate(Mockito.eq(attribute3), Mockito.any(ComponentTypeAttributeData.class));
		verify(popAttribute2, times(0)).populate(Mockito.eq(attribute3), Mockito.any(ComponentTypeAttributeData.class));
	}
}
