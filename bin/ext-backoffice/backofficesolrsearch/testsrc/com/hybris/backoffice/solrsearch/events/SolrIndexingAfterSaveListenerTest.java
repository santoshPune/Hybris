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

package com.hybris.backoffice.solrsearch.events;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.tx.AfterSaveEvent;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;


@IntegrationTest
public class SolrIndexingAfterSaveListenerTest extends ServicelayerTransactionalTest
{
	public static final PK PK_ = PK.fromLong(1L);
	public static final String TYPE_1 = "Type1";

	@InjectMocks
	private final SolrIndexingAfterSaveListener listener = new SolrIndexingAfterSaveListener();

	private final AfterSaveEvent UPDATE_EVENT = new AfterSaveEvent(PK_, AfterSaveEvent.UPDATE);

	private final AfterSaveEvent REMOVE_EVENT = new AfterSaveEvent(PK_, AfterSaveEvent.REMOVE);

	@Mock
	private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
	@Mock
	private ModelService modelService;

	@Mock
	private TypeService typeService;
	@Mock
	private ItemModel itemModel;
	@Mock
	private SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		listener.setIgnoredTypeCodes(new TreeSet<>());
		Mockito.when(itemModel.getItemtype()).thenReturn(TYPE_1);
		Mockito.when(modelService.get(PK_)).thenReturn(itemModel);
		Mockito.when(Boolean.valueOf(typeService.isAssignableFrom(Matchers.anyString(), Matchers.anyString())))
				.thenReturn(Boolean.FALSE);
	}

	@Test
	public void testAfterSaveUpdateEvent()
	{
		Mockito.when(Boolean.valueOf(backofficeFacetSearchConfigService.isSolrSearchConfiguredForType(TYPE_1)))
				.thenReturn(Boolean.TRUE);
		Mockito.when(listener.findTypeCode(SolrIndexingAfterSaveListener.SolrIndexOperation.CHANGE, PK_)).thenReturn(TYPE_1);
		listener.afterSave(Arrays.asList(new AfterSaveEvent[]
		{ UPDATE_EVENT }));
		Mockito.verify(solrIndexSynchronizationStrategy).updateItem(Matchers.eq(TYPE_1), Matchers.eq(PK_.getLongValue()));
	}

	@Test
	public void testAfterSaveUpdateEventNonIndexedType()
	{
		Mockito.when(Boolean.valueOf(backofficeFacetSearchConfigService.isSolrSearchConfiguredForType(TYPE_1)))
				.thenReturn(Boolean.FALSE);
		listener.afterSave(Arrays.asList(new AfterSaveEvent[]
		{ UPDATE_EVENT }));
		Mockito.verifyNoMoreInteractions(solrIndexSynchronizationStrategy);
	}

	@Test
	public void testAfterSaveCreateRemoveEvents()
	{
		Mockito.when(Boolean.valueOf(backofficeFacetSearchConfigService.isSolrSearchConfiguredForType(ItemModel._TYPECODE)))
				.thenReturn(Boolean.TRUE);
		listener.afterSave(Arrays.asList(new AfterSaveEvent[]
		{ REMOVE_EVENT }));
		Mockito.verify(solrIndexSynchronizationStrategy).removeItem(Matchers.eq(ItemModel._TYPECODE),
				Matchers.eq(PK_.getLongValue()));
	}
}
