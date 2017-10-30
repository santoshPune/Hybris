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
package de.hybris.platform.cmswebservices.types.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeListData;
import de.hybris.platform.cmswebservices.data.OptionData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class TypeControllerWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String WIDESCREEN = "widescreen";
	private static final String DESKTOP = "desktop";
	private static final String TABLET = "tablet";
	private static final String MOBILE = "mobile";

	private static final String URI = "/v1/types";
	private static final String URI_SIMPLE_RESPONSIVE_BANNER_COMPONENT = "/v1/types/SimpleResponsiveBannerComponent";

	@Resource
	private ModelService modelService;

	@Before
	public void setupMediaFormats()
	{
		createMediaFormat(MOBILE);
		createMediaFormat(TABLET);
		createMediaFormat(DESKTOP);
		createMediaFormat(WIDESCREEN);
	}

	@Test
	public void getAllTypesTest() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentTypeListData> response = apiClient
				.request()
				.endpoint(URI)
				.acceptJson()
				.get(ComponentTypeListData.class);

		//check that we have a result
		assertStatusCode(response, 200);

		// check that we have a body
		assertNotNull(response.getBody());

		//check that we have a couple of entries
		assertTrue(response.getBody().getComponentTypes().size() > 1);

		final List<ComponentTypeData> components = response.getBody().getComponentTypes();
		ComponentTypeData paragraphComponent = new ComponentTypeData();

		for (final ComponentTypeData component : components)
		{
			if(component.getCode().equals("CMSParagraphComponent"))
			{
				paragraphComponent = component;
			}
		}

		// check that it contains the paragraph component
		assertNotNull(paragraphComponent);
	}

	@Test
	public void shouldGetSimpleResponsiveBannerComponentWithMediaFormats() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentTypeData> response = apiClient.request().endpoint(URI_SIMPLE_RESPONSIVE_BANNER_COMPONENT)
				.acceptJson().get(ComponentTypeData.class);

		assertStatusCode(response, 200);

		final ComponentTypeData component = response.getBody();
		assertThat(component.getCode(), equalTo(SimpleResponsiveBannerComponentModel._TYPECODE));
		assertThat(component.getI18nKey(), equalTo("type.simpleresponsivebannercomponent.name"));

		final ComponentTypeAttributeData urlLink = getAttribute(component.getAttributes(), "urlLink");
		assertThat(urlLink.getCmsStructureType(), equalTo("ShortString"));
		assertThat(urlLink.getQualifier(), equalTo("urlLink"));
		assertThat(urlLink.getLocalized(), equalTo(Boolean.FALSE));
		assertThat(urlLink.getOptions(), nullValue());
		assertThat(urlLink.getI18nKey(), equalTo("type.simpleresponsivebannercomponent.urllink.name"));
		assertThat(urlLink.getCmsStructureEnumType(), nullValue());

		final ComponentTypeAttributeData media = getAttribute(component.getAttributes(), "media");
		assertThat(media.getCmsStructureType(), equalTo("MediaContainer"));
		assertThat(media.getQualifier(), equalTo("media"));
		assertThat(media.getLocalized(), equalTo(Boolean.TRUE));
		assertThat(media.getI18nKey(), equalTo("type.simpleresponsivebannercomponent.media.name"));
		assertThat(media.getCmsStructureEnumType(), nullValue());

		final OptionData mobile = getOption(media.getOptions(), MOBILE);
		assertThat(mobile.getValue(), equalTo(MOBILE));
		assertThat(mobile.getLabel(), equalTo("cms.media.format.mobile"));
		final OptionData tablet = getOption(media.getOptions(), TABLET);
		assertThat(tablet.getValue(), equalTo(TABLET));
		assertThat(tablet.getLabel(), equalTo("cms.media.format.tablet"));
		final OptionData desktop = getOption(media.getOptions(), DESKTOP);
		assertThat(desktop.getValue(), equalTo(DESKTOP));
		assertThat(desktop.getLabel(), equalTo("cms.media.format.desktop"));
		final OptionData widescreen = getOption(media.getOptions(), WIDESCREEN);
		assertThat(widescreen.getValue(), equalTo(WIDESCREEN));
		assertThat(widescreen.getLabel(), equalTo("cms.media.format.widescreen"));
	}

	protected void createMediaFormat(String qualifier)
	{
		final MediaFormatModel mediaFormat = modelService.create(MediaFormatModel.class);
		mediaFormat.setQualifier(qualifier);
		modelService.save(mediaFormat);
	}

	protected ComponentTypeAttributeData getAttribute(final List<ComponentTypeAttributeData> attributes, final String qualifier)
	{
		return attributes.stream() //
				.filter(attribute -> qualifier.equals(attribute.getQualifier())) //
				.findFirst() //
				.orElseThrow(
						() -> new IllegalArgumentException("No attribute with qualifier [" + qualifier + "] in list of attributes."));
	}

	protected OptionData getOption(final List<OptionData> options, final String value)
	{
		return options.stream() //
				.filter(option -> value.equals(option.getValue())) //
				.findFirst() //
				.orElseThrow(() -> new IllegalArgumentException("No option with value [" + value + "] in list of options."));
	}
}
