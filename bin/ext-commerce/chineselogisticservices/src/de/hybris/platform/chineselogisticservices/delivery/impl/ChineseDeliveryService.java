/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineselogisticservices.delivery.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chineselogisticservices.delivery.dao.C2LItemZoneDeliveryModeValueDao;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.delivery.impl.DefaultDeliveryService;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.util.PriceValue;

import org.springframework.beans.factory.annotation.Required;


/**
 * Service for Delivery
 */
public class ChineseDeliveryService extends DefaultDeliveryService implements DeliveryService
{
	private C2LItemZoneDeliveryModeValueDao c2LItemZoneDeliveryModeValueDao;

	@Override
	public PriceValue getDeliveryCostForDeliveryModeAndAbstractOrder(final DeliveryModeModel deliveryMode,
			final AbstractOrderModel abstractOrder)
	{
		if (abstractOrder.getDeliveryAddress() == null)
		{
			return null;
		}
		if (!abstractOrder.getDeliveryAddress().getCountry().getIsocode().equalsIgnoreCase("CN"))
		{
			return super.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryMode, abstractOrder);
		}
		validateParameterNotNull(deliveryMode, "deliveryMode model cannot be null");
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");

		AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
		C2LItemModel[] c2lItemModels =
		{ deliveryAddress.getCityDistrict(), deliveryAddress.getCity(), deliveryAddress.getRegion(), deliveryAddress.getCountry() };

		try
		{
			for (C2LItemModel c2lItemModel : c2lItemModels)
			{
				PriceValue priceValue = getCost(abstractOrder, deliveryMode, c2lItemModel);
				if (priceValue != null)
				{
					return priceValue;
				}
			}
		}
		catch (JaloDeliveryModeException e)
		{
			return null;
		}
		return null;
	}


	public PriceValue getCost(AbstractOrderModel order, DeliveryModeModel deliveryMode, final C2LItemModel c2LItem)
			throws JaloDeliveryModeException
	{
		AddressModel addr = order.getDeliveryAddress();
		if (addr == null)
		{
			throw new JaloDeliveryModeException("getCost(): delivery address was NULL in order " + order, 0);
		}

		CountryModel country = addr.getCountry();
		if (country == null)
		{
			throw new JaloDeliveryModeException("getCost(): country of delivery address " + addr + " was NULL in order " + order, 0);
		}

		CurrencyModel curr = order.getCurrency();
		if (curr == null)
		{
			throw new JaloDeliveryModeException("getCost(): currency was NULL in order " + order, 0);
		}

		if (c2LItem != null && c2LItem.getZone() != null)
		{
			ZoneDeliveryModeValueModel bestMatch = c2LItemZoneDeliveryModeValueDao.findDeliveryModeValueByC2LItem(c2LItem, order,
					deliveryMode);

			if (bestMatch != null)
			{
				return new PriceValue(curr.getIsocode(), bestMatch.getValue(), bestMatch.getDeliveryMode().getNet());
			}
		}
		return null;
	}


	protected C2LItemZoneDeliveryModeValueDao getC2LItemZoneDeliveryModeValueDao()
	{
		return c2LItemZoneDeliveryModeValueDao;
	}

	@Required
	public void setC2LItemZoneDeliveryModeValueDao(C2LItemZoneDeliveryModeValueDao c2lItemZoneDeliveryModeValueDao)
	{
		c2LItemZoneDeliveryModeValueDao = c2lItemZoneDeliveryModeValueDao;
	}


}
