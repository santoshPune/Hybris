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
package de.hybris.platform.configurablebundlefacades.order;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundlefacades.order.impl.DefaultBundleCartFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.Mockito.doReturn;

/**
 * Tests to check {@link de.hybris.platform.configurablebundlefacades.order.impl.DefaultBundleCartFacade#revertEntries(List)}.
 * <br/>
 * The method should reverse order of standalone product and bundles in whole,
 * but keep the order of products inside single bundle.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OrderEntrySequenceTest
{
    @Resource
    @Spy
    private DefaultBundleCartFacade defaultBundleCartFacade;
    private final CartData cartData = new CartData();

    @Before
    public void setUp()
    {
        doReturn(Boolean.TRUE).when(defaultBundleCartFacade).hasSessionCart();
        doReturn(cartData).when(defaultBundleCartFacade).getSessionCart();
    }

    @Test
    public void shouldGetStandaloneProductsInOriginalOrder()
    {
        final List<OrderEntryData> entries = Arrays.asList(
                getStandaloneEntry(),
                getStandaloneEntry(),
                getStandaloneEntry()
        );
        cartData.setEntries(entries);
        final CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(false);

        assertThat(flipped.getEntries(), contains(entries.get(0), entries.get(1), entries.get(2)));
    }

    @Test
    public void shouldRevertStandaloneProducts()
    {
        final List<OrderEntryData> entries = Arrays.asList(
                getStandaloneEntry(),
                getStandaloneEntry(),
                getStandaloneEntry()
        );
        cartData.setEntries(entries);
        final CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);

        assertThat(flipped.getEntries(), contains(entries.get(2), entries.get(1), entries.get(0)));
    }

    @Test
    public void shouldKeepProductOrderInBundle()
    {
        final List<OrderEntryData> entries = Arrays.asList(
                getBundledEntry(1),
                getBundledEntry(1),
                getBundledEntry(1)
        );
        cartData.setEntries(entries);
        final CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);

        assertThat(flipped.getEntries(), contains(entries.get(0), entries.get(1), entries.get(2)));
    }

    @Test
    public void shouldRevertBundleOrder()
    {
        final List<OrderEntryData> entries = Arrays.asList(
                getBundledEntry(1),
                getBundledEntry(2)
        );
        cartData.setEntries(entries);
        final CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);

        assertThat(flipped.getEntries(), contains(entries.get(1), entries.get(0)));
    }

    @Test
    public void shouldHandleCombinedEntryList()
    {
        final List<OrderEntryData> entries = Arrays.asList(
                getStandaloneEntry(),   // 0
                getStandaloneEntry(),   // 1
                getStandaloneEntry(),   // 2
                getBundledEntry(1),     // 3
                getBundledEntry(1),     // 4
                getStandaloneEntry(),   // 5
                getBundledEntry(2),     // 6
                getBundledEntry(2),     // 7
                getBundledEntry(2)      // 8
        );
        cartData.setEntries(entries);
        final CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);

        assertThat(flipped.getEntries(),
                   contains(entries.get(6), entries.get(7), entries.get(8),
                            entries.get(5), entries.get(3), entries.get(4),
                            entries.get(2), entries.get(1), entries.get(0)));
    }

    @Test
    public void shouldWorkWithEmptyEntryList()
    {
        cartData.setEntries(Collections.<OrderEntryData>emptyList());
        CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);
        assertThat(flipped.getEntries(), iterableWithSize(0));

        cartData.setEntries(Collections.<OrderEntryData>emptyList());
        flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(false);
        assertThat(flipped.getEntries(), iterableWithSize(0));
    }

    @Test
    public void shouldWorkWithNullEntryList()
    {
        cartData.setEntries(null);
        CartData flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(true);
        assertThat(flipped.getEntries(), iterableWithSize(0));

        cartData.setEntries(null);
        flipped = defaultBundleCartFacade.getSessionCartWithEntryOrdering(false);
        assertThat(flipped.getEntries(), iterableWithSize(0));
    }

    private OrderEntryData getBundledEntry(final int bundleNo)
    {
        final OrderEntryData result = getStandaloneEntry();
        result.setBundleNo(bundleNo);
        return result;
    }

    private OrderEntryData getStandaloneEntry()
    {
        final OrderEntryData result = new OrderEntryData();
        return result;
    }
}
