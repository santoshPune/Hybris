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
 */
package de.hybris.platform.warehousing.cancellation.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultConsignmentCancellationServiceTest
{
	@InjectMocks
	private final DefaultConsignmentCancellationService cancellationService = new DefaultConsignmentCancellationService();

	@Mock
	private InventoryEventService inventoryEventService;
	@Mock
	private ModelService modelService;
	@Mock
	private WarehousingCommentService<ConsignmentEntryModel> consignmentEntryCommentService;

	@Mock
	private OrderCancelResponse orderCancelResponse;

	private ConsignmentModel consignment1;
	private ConsignmentModel consignment2;
	private ConsignmentEntryModel consignmentEntry1;
	private ConsignmentEntryModel consignmentEntry2;
	private ConsignmentEntryModel consignmentEntry3;
	private ConsignmentEntryModel consignmentEntry4;
	private OrderModel order;
	private AbstractOrderEntryModel orderEntry1;
	private AbstractOrderEntryModel orderEntry2;
	private CancellationEventModel consignmentCancellationEvent;
	private CancellationEventModel orderCancellationEvent;
	private GuidKeyGenerator guidKeyGenerator;

	@Mock
	private ProductModel mouse;
	@Mock
	private ProductModel batteries;
	@Mock
	private UserModel author;

	@Before
	public void setUp()
	{
		order = new OrderModel();
		orderEntry1 = spy(new AbstractOrderEntryModel());
		orderEntry1.setEntryNumber(Integer.valueOf(1));
		orderEntry1.setQuantity(Long.valueOf(20L));
		orderEntry1.setOrder(order);
		orderEntry2 = spy(new AbstractOrderEntryModel());
		orderEntry2.setEntryNumber(Integer.valueOf(2));
		orderEntry2.setQuantity(Long.valueOf(10L));
		orderEntry2.setOrder(order);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));
		order.setUser(author);

		consignment1 = new ConsignmentModel();
		consignment1.setCode("consignment1234_1");
		consignmentEntry1 = spy(new ConsignmentEntryModel());
		consignmentEntry1.setOrderEntry(orderEntry1);
		consignmentEntry1.setQuantity(Long.valueOf(10L));

		consignmentEntry2 = spy(new ConsignmentEntryModel());
		consignmentEntry2.setOrderEntry(orderEntry2);
		consignmentEntry2.setQuantity(Long.valueOf(5L));
		consignment1.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));

		consignment2 = new ConsignmentModel();
		consignment2.setCode("consignment1234_2");
		consignmentEntry3 = spy(new ConsignmentEntryModel());
		consignmentEntry3.setOrderEntry(orderEntry1);
		consignmentEntry3.setQuantity(Long.valueOf(10L));

		consignmentEntry4 = spy(new ConsignmentEntryModel());
		consignmentEntry4.setOrderEntry(orderEntry2);
		consignmentEntry4.setQuantity(Long.valueOf(5L));
		consignment2.setConsignmentEntries(Sets.newHashSet(consignmentEntry3, consignmentEntry4));

		order.setConsignments(Sets.newHashSet(consignment1, consignment2));
		consignmentCancellationEvent = new CancellationEventModel();
		when(modelService.create(CancellationEventModel.class)).thenReturn(consignmentCancellationEvent);
		orderCancellationEvent = new CancellationEventModel();
		when(modelService.create(CancellationEventModel.class)).thenReturn(orderCancellationEvent);

		when(orderEntry1.getProduct()).thenReturn(mouse);
		when(mouse.getName()).thenReturn("Wireless Mouse");
		when(orderEntry2.getProduct()).thenReturn(batteries);
		when(batteries.getName()).thenReturn("Rechargeable batteries");

		guidKeyGenerator = new GuidKeyGenerator();
		cancellationService.setGuidKeyGenerator(guidKeyGenerator);
	}

	@Test
	public void shouldCancelConsignment()
	{
		// Given
		final OrderCancelEntry cancellationEntry = new OrderCancelEntry(orderEntry1, 10L, "notes", CancelReason.LATEDELIVERY);
		when(orderCancelResponse.getEntriesToCancel()).thenReturn(ImmutableList.of(cancellationEntry));
		when(consignmentEntry1.getQuantityPending()).thenReturn(Long.valueOf(10L));
		when(orderCancelResponse.getCancelReason()).thenReturn(CancelReason.OTHER);

		// When
		cancellationService.cancelConsignment(consignment1, orderCancelResponse);

		// Then
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class), anyString());
	}

}
