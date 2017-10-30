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
package de.hybris.platform.cmswebservices.catalogversiondetails.facade;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model.ComparableCatalogVersionDetailData;
import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class CatalogVersionDetailDataCatalogIdComparatorTest {

    private static final CatalogVersionDetailData FIRST = new ComparableCatalogVersionDetailData(); {
        FIRST.setCatalogId("1");
    }

    private static final CatalogVersionDetailData SECOND = new ComparableCatalogVersionDetailData(); {
        SECOND.setCatalogId("2");
    }

    private static final CatalogVersionDetailData[] reverseOrdered = new CatalogVersionDetailData[] {SECOND, FIRST};
    private static final CatalogVersionDetailData[] ordered = new CatalogVersionDetailData[] {FIRST, SECOND};

    private final Comparator comparator = new CatalogVersionDetailDataCatalogIdComparator();

    @Test
    public void comparatorWillReturnInOrder() throws Exception {
        List<CatalogVersionDetailData> collectionToSort = newArrayList(reverseOrdered);
        sort(collectionToSort, comparator);
        assertThat(collectionToSort, contains(ordered));
    }

    @Test
    public void comparatorWillReturnInReverseOrder() throws Exception {
        List<CatalogVersionDetailData> collectionToSort = newArrayList(ordered);
        sort(collectionToSort, comparator.reversed());
        assertThat(collectionToSort, contains(reverseOrdered));
    }


}