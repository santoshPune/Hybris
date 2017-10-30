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
package de.hybris.platform.cmswebservices.items.facade.validator.consumer;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.BannerComponentData;
import de.hybris.platform.cmswebservices.items.facade.validator.consumer.LocalizedMediaAttributeValidationConsumer;
import de.hybris.platform.cmswebservices.items.facade.validator.consumer.LocalizedValidationData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LocalizedMediaAttributeValidationConsumerTest
{

	private static final String MEDIA_FIELD_NAME = "media";

	@Mock
	private MediaService mediaService;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@InjectMocks
	private LocalizedMediaAttributeValidationConsumer mediaAttributePredicate = new LocalizedMediaAttributeValidationConsumer();

	@Spy
	private final Errors errors = new BeanPropertyBindingResult(new BannerComponentData(), BannerComponentData.class.getSimpleName());

	@Test
	public void testValidNonLocalizedMedia()
	{
		String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(new MediaModel());

		LocalizedValidationData errorContainer = new LocalizedValidationData.Builder()
				.setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode)
				.setRequiredLanguage(false)
				.setLocale(null)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);

	}

	@Test
	public void testInvalidMedia()
	{
		String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(null);

		LocalizedValidationData errorContainer = new LocalizedValidationData.Builder()
				.setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode)
				.setRequiredLanguage(false)
				.setLocale(null)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString());
	}

	@Test
	public void testValidLocalizedMediaRequiredLanguage()
	{
		String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(new MediaModel());

		LocalizedValidationData errorContainer = new LocalizedValidationData.Builder()
				.setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode)
				.setRequiredLanguage(true)
				.setLocale(Locale.ENGLISH)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verifyZeroInteractions(errors);
	}


	@Test
	public void testInvalidLocalizedMediaRequiredLanguage()
	{
		String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenReturn(null);

		LocalizedValidationData errorContainer = new LocalizedValidationData.Builder()
				.setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode)
				.setRequiredLanguage(true)
				.setLocale(Locale.ENGLISH)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}


	@Test
	public void testInvalidLocalizedMediaExceptionThrown()
	{
		String mediaCode = "my-media-code";

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), mediaCode)).thenThrow(
				new UnknownIdentifierException(""));

		LocalizedValidationData errorContainer = new LocalizedValidationData.Builder()
				.setFieldName(MEDIA_FIELD_NAME)
				.setValue(mediaCode)
				.setRequiredLanguage(true)
				.setLocale(Locale.ENGLISH)
				.build();

		mediaAttributePredicate.accept(errorContainer, errors);
		verify(errors).rejectValue(anyString(), anyString(), any(), any());

	}
}
