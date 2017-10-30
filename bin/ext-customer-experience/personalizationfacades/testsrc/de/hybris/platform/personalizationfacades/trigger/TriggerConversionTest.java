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
package de.hybris.platform.personalizationfacades.trigger;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationfacades.AbstractFacadeIntegrationTest;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.TriggerConversionOptions;
import de.hybris.platform.personalizationservices.enums.CxGroupingOperator;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxSegmentTriggerModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class TriggerConversionTest extends AbstractFacadeIntegrationTest
{
	@Resource
	private ConfigurableConverter<CxSegmentTriggerModel, SegmentTriggerData, TriggerConversionOptions> cxTriggerConfigurableConverter;

	@Resource
	private Converter<SegmentTriggerData, CxSegmentTriggerModel> cxSegmentTriggerReverseConverter;

	private CxSegmentTriggerModel trigger;
	private CxVariationModel variation;

	@Before
	public void setup()
	{
		final CxSegmentModel segment1 = new CxSegmentModel();
		segment1.setCode("segment1");

		final CxSegmentModel segment2 = new CxSegmentModel();
		segment2.setCode("segment2");

		variation = new CxVariationModel();
		variation.setCode("variation");

		final CxCustomizationModel customization = new CxCustomizationModel();
		customization.setCode("customization");
		variation.setCustomization(customization);


		trigger = new CxSegmentTriggerModel();
		trigger.setCode("trigger");
		trigger.setGroupBy(CxGroupingOperator.OR);
		trigger.setVariation(variation);
		trigger.setSegments(Lists.newArrayList(segment1, segment2));
	}

	@Test
	public void defaultTest() throws Exception
	{
		//given
		final SegmentTriggerData data = new SegmentTriggerData();

		//when
		cxTriggerConfigurableConverter.convert(trigger, data);

		//then
		Assert.assertNotNull(data);
		Assert.assertEquals(trigger.getCode(), data.getCode());
		Assert.assertNull(data.getVariation());
		Assert.assertNull(data.getSegments());
	}

	@Test
	public void baseTest() throws Exception
	{
		//given
		final SegmentTriggerData data = new SegmentTriggerData();

		//when
		cxTriggerConfigurableConverter.convert(trigger, data, Lists.newArrayList(TriggerConversionOptions.BASE));

		//then
		Assert.assertNotNull(data);
		Assert.assertEquals(trigger.getCode(), data.getCode());
		Assert.assertNull(data.getVariation());
		Assert.assertNull(data.getSegments());

	}

	@Test
	public void variationTest() throws Exception
	{
		//given
		final SegmentTriggerData data = new SegmentTriggerData();

		//when
		cxTriggerConfigurableConverter.convert(trigger, data, Lists.newArrayList(TriggerConversionOptions.FOR_SEGMENT));

		//then
		Assert.assertNotNull(data);
		Assert.assertEquals(trigger.getCode(), data.getCode());
		Assert.assertNotNull(data.getVariation());
		Assert.assertEquals(variation.getCode(), data.getVariation().getCode());
		Assert.assertNull(data.getSegments());

	}

	@Test
	public void segmentTest() throws Exception
	{
		//given
		final SegmentTriggerData data = new SegmentTriggerData();

		//when
		cxTriggerConfigurableConverter.convert(trigger, data, Lists.newArrayList(TriggerConversionOptions.FOR_VARIATION));

		//then
		Assert.assertNotNull(data);
		Assert.assertEquals(trigger.getCode(), data.getCode());
		Assert.assertEquals("OR", data.getGroupBy());
		Assert.assertNull(data.getVariation());
		Assert.assertNotNull(data.getSegments());
		Assert.assertEquals(2, data.getSegments().size());

	}

	@Test
	public void allTest() throws Exception
	{
		//given
		final SegmentTriggerData data = new SegmentTriggerData();

		//when
		cxTriggerConfigurableConverter.convert(trigger, data, Lists.newArrayList(TriggerConversionOptions.FULL));

		//then
		Assert.assertNotNull(data);
		Assert.assertEquals(trigger.getCode(), data.getCode());
		Assert.assertEquals("OR", data.getGroupBy());
		Assert.assertNotNull(data.getVariation());
		Assert.assertEquals(variation.getCode(), data.getVariation().getCode());
		Assert.assertNotNull(data.getSegments());
		Assert.assertEquals(2, data.getSegments().size());

	}

	@Test
	public void reverseTest() throws Exception
	{
		//given
		final SegmentTriggerData source = new SegmentTriggerData();
		source.setCode("trigger");
		source.setGroupBy("AND");
		source.setVariation(new VariationData());
		source.getVariation().setCode("variation");
		source.setSegments(new ArrayList<>());
		final SegmentData segment = new SegmentData();
		segment.setCode(SEGMENT_ID);
		source.getSegments().add(segment);

		//when
		final CxSegmentTriggerModel result = cxSegmentTriggerReverseConverter.convert(source);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals("trigger", result.getCode());
		Assert.assertEquals(CxGroupingOperator.AND, result.getGroupBy());
		Assert.assertNull(result.getVariation());
		Assert.assertNotNull(result.getSegments());
		Assert.assertEquals(1, result.getSegments().size());
		Assert.assertEquals(SEGMENT_ID, result.getSegments().iterator().next().getCode());
	}
}
