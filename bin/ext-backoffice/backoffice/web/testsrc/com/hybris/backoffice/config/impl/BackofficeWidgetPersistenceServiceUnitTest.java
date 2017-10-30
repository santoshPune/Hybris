/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.backoffice.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.modules.CockpitModuleConnector;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;


public class BackofficeWidgetPersistenceServiceUnitTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private MediaService mediaService;
	@Spy
	@InjectMocks
	private BackofficeWidgetPersistenceServiceStub service;
	@Mock
	private WidgetLibUtils widgetLibUtils;
	@Mock
	private CockpitModuleConnector cockpitModuleConnector;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void loadWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doReturn(widget).when(service).loadWidgetTree("testId", inputStream);
		service.loadWidgetTree("testId");
		verify(service).getOrCreateWidgetsConfigMedia();
		verify(mediaService).getStreamFromMedia(mediaModel);
	}

	@Test
	public void testStoreWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		final Widgets widgets = mock(Widgets.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		doReturn(Boolean.TRUE).when(service).isWidgetsConfigStoredInMedia();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doNothing().when(service).storeWidgetTree(any(), any(), any());
		service.storeWidgetTree(widget);
		verify(service).isStoringEnabled();
		verify(service).isWidgetsConfigStoredInMedia();
		verify(service).loadWidgets(inputStream);
		verify(service).storeWidgetTree(any(), any(), any());
		verify(mediaService).setDataForMedia(any(), any());
	}

	@Test
	public void testDeleteWidgetTree()
	{
		final MediaModel mediaModel = mock(MediaModel.class);
		final InputStream inputStream = mock(InputStream.class);
		final Widget widget = mock(Widget.class);
		final Widgets widgets = mock(Widgets.class);
		doReturn(mediaModel).when(service).getOrCreateWidgetsConfigMedia();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		when(mediaService.getStreamFromMedia(mediaModel)).thenReturn(inputStream);
		doNothing().when(service).storeWidgetTree(any(), any(), any());
		service.deleteWidgetTree(widget);
		verify(service).loadWidgets(inputStream);
		verify(service).deleteWidgetTreeRecursive(widgets, widget);
		verify(service).deleteOrphanedConnections(widgets);
		verify(service).storeWidgets(any(), any());
		verify(mediaService).setDataForMedia(any(), any());
	}

	@Test
	public void testResetToDefaults()
	{
		final MediaModel media = mock(MediaModel.class);
		doReturn(null).when(service).getWidgetsConfigMedia();
		doReturn(media).when(service).createWidgetsConfigMedia();
		doNothing().when(service).putDefaultWidgetsConfig(media);

		when(widgetLibUtils.libDirAbsolutePath()).thenReturn(StringUtils.EMPTY);
		when(cockpitModuleConnector.getCockpitModuleUrls()).thenReturn(Collections.emptyList());

		service.resetToDefaults();
		verify(service).getWidgetsConfigMedia();
		verify(service).createWidgetsConfigMedia();
		verify(mediaService).removeDataFromMediaQuietly(media);
		verify(service).putDefaultWidgetsConfig(media);
	}

	@Test
	public void testIsWidgetsConfigStoredInMedia()
	{
		final MediaModel media = mock(MediaModel.class);
		media.setCode(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA);
		when(mediaService.getMedia(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA)).thenReturn(media);
		when(Boolean.valueOf(mediaService.hasData(media))).thenReturn(Boolean.FALSE);
		assertFalse(service.isWidgetsConfigStoredInMedia());
		when(Boolean.valueOf(mediaService.hasData(media))).thenReturn(Boolean.TRUE);
		assertTrue(service.isWidgetsConfigStoredInMedia());
	}

	@Test
	public void testGetOrCreateWidgetsConfigMedia()
	{
		final MediaModel media = mock(MediaModel.class);
		when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(media);
		when(mediaService.getMedia(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA)).thenReturn(null);
		doNothing().when(service).putDefaultWidgetsConfig(media);
		service.getOrCreateWidgetsConfigMedia();
		verify(service).createWidgetsConfigMedia();
		verify(service).putDefaultWidgetsConfig(media);
	}

	@Test
	public void testGetWidgetsConfigMedia()
	{
		final CatalogUnawareMediaModel media = new CatalogUnawareMediaModel();
		media.setCode(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA);
		when(mediaService.getMedia(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA)).thenReturn(media);
		final MediaModel widgetsConfigMedia = service.getWidgetsConfigMedia();
		assertEquals(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA, widgetsConfigMedia.getCode());
		verify(mediaService).getMedia(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA);
	}

	@Test
	public void testCreateWidgetsConfigMedia()
	{
		final CatalogUnawareMediaModel media = new CatalogUnawareMediaModel();
		when(modelService.create(CatalogUnawareMediaModel.class)).thenReturn(media);
		final MediaModel widgetsConfigMedia = service.createWidgetsConfigMedia();
		assertEquals(BackofficeWidgetPersistenceService.TEXT_XML_MIME_TYPE, widgetsConfigMedia.getMime());
		assertEquals(BackofficeWidgetPersistenceService.WIDGET_CONFIG_MEDIA, widgetsConfigMedia.getCode());
		verify(modelService).save(media);
	}

	@Test
	public void testPutDefaultWidgetsConfig()
	{
		final MediaModel media = mock(MediaModel.class);
		final Widgets widgets = mock(Widgets.class);
		final InputStream inputStream = mock(InputStream.class);
		doReturn(inputStream).when(service).getDefaultWidgetsConfigInputStream();
		doReturn(widgets).when(service).loadWidgets(inputStream);
		service.putDefaultWidgetsConfig(media);
		verify(service).loadWidgets(inputStream);
		verify(service).applyImports(widgets, new HashSet<>());
		verify(service).applyExtensions(widgets);
		verify(service).storeWidgets(any(Widgets.class), any(OutputStream.class));
		verify(mediaService).setDataForMedia(any(MediaModel.class), any(byte[].class));
	}

	/**
	 * Class to stub protected methods from super class
	 */
	public static class BackofficeWidgetPersistenceServiceStub extends BackofficeWidgetPersistenceService
	{
		@Override
		protected void applyImports(final Widgets widgets, final Set<String> alreadyImportedResources)
		{
		}

		@Override
		protected void applyExtensions(final Widgets widgets)
		{
		}

		@Override
		protected Widgets loadWidgets(final InputStream inputStream)
		{
			return new Widgets();
		}

		@Override
		protected void storeWidgets(final Widgets widgets, final OutputStream outputStream)
		{
		}

		@Override
		protected void deleteWidgetTreeRecursive(final Widgets widgets, final Widget node)
		{
		}

		@Override
		protected void deleteOrphanedConnections(final Widgets widgets)
		{
		}
	}
}
