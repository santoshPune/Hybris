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
package de.hybris.platform.ordermanagementfacade;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Abstract class containing basic facade functionality
 */
public class OmsBaseFacade
{
	private ModelService modelService;


	//TODO move to commercefacade to a utility or abstract class (OMSE-1213)
	/**
	 * Converts the result of {@link de.hybris.platform.commerceservices.search.pagedata.SearchPageData}
	 *
	 * @param source
	 *           searchPageData containing original results
	 * @param converter
	 *           converter for converting the searchPageData's results
	 * @param <S>
	 *           original type of searchPageData's results
	 * @param <T>
	 *           target type of searchPageData's results
	 * @return converted SearchPageData
	 */
	protected <S, T> SearchPageData<T> convertSearchPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
	}

	/**
	 * Discards snapshots of orders. i.e: orders with versionID equal to null
	 *
	 * @param orders
	 *           a list of orders
	 * @return a filtered list of orders
	 */
	protected List<OrderModel> discardOrderSnapshot(final List<OrderModel> orders)
	{
		if (CollectionUtils.isEmpty(orders))
		{
			return new ArrayList<>();
		}
		return orders.stream().filter(order -> Objects.isNull(order.getVersionID())).collect(Collectors.toList());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
