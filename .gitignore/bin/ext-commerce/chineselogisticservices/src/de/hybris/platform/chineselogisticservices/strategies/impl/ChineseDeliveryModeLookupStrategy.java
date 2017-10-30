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
package de.hybris.platform.chineselogisticservices.strategies.impl;

import de.hybris.platform.chineselogisticservices.delivery.dao.C2LItemZoneDeliveryModeDao;
import de.hybris.platform.commerceservices.strategies.impl.DefaultDeliveryModeLookupStrategy;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;




/**
 * This strategy defines the process of looking up the delivery cost.
 */
public class ChineseDeliveryModeLookupStrategy extends DefaultDeliveryModeLookupStrategy
{
	private C2LItemZoneDeliveryModeDao c2LItemZoneDeliveryModeDao;

	@Override
	public List<DeliveryModeModel> getSelectableDeliveryModesForOrder(final AbstractOrderModel abstractOrderModel)
	{
		if (isPickUpOnlyOrder(abstractOrderModel))
		{
			return new ArrayList<DeliveryModeModel>(getPickupDeliveryModeDao().findPickupDeliveryModesForAbstractOrder(
					abstractOrderModel));
		}
		else
		{
			final AddressModel deliveryAddress = abstractOrderModel.getDeliveryAddress();
			if (abstractOrderModel.getStore() == null)
			{
				return super.getSelectableDeliveryModesForOrder(abstractOrderModel);
			}
			else if (deliveryAddress != null && deliveryAddress.getCountry() != null)
			{
				if (!"CN".equalsIgnoreCase(deliveryAddress.getCountry().getIsocode()))
				{
					return super.getSelectableDeliveryModesForOrder(abstractOrderModel);
				}
			}
			else
			{
				return Collections.emptyList();
			}

			if (isPickUpOnlyOrder(abstractOrderModel))
			{
				return new ArrayList<DeliveryModeModel>(getPickupDeliveryModeDao().findPickupDeliveryModesForAbstractOrder(
						abstractOrderModel));
			}
			else
			{
				C2LItemModel[] c2lItemModels =
				{ deliveryAddress.getCityDistrict(), deliveryAddress.getCity(), deliveryAddress.getRegion(),
						deliveryAddress.getCountry() };
				final CurrencyModel currency = abstractOrderModel.getCurrency();
				if (currency != null && deliveryAddress != null)
				{
					for (C2LItemModel c2lItemModel : c2lItemModels)
					{
						List<DeliveryModeModel> deliveryModes = findDeliveryMode(abstractOrderModel, c2lItemModel);
						if (deliveryModes != null)
						{
							return deliveryModes;
						}
					}
				}
				return Collections.emptyList();
			}
		}
	}


	protected List<DeliveryModeModel> findDeliveryMode(final AbstractOrderModel abstractOrderModel, final C2LItemModel c2LItem)
	{
		if (c2LItem != null && c2LItem.getZone() != null)
		{
			final List<DeliveryModeModel> deliveryModes = new ArrayList<DeliveryModeModel>();
			Collection<DeliveryModeModel> deliveryModesResult = getC2LItemZoneDeliveryModeDao().findDeliveryModesByC2LItem(c2LItem,
					abstractOrderModel);
			if (!CollectionUtils.isEmpty(deliveryModesResult))
			{
				deliveryModes.addAll(deliveryModesResult);
				return deliveryModes.size() > 0 ? deliveryModes : null;
			}
		}
		return null;
	}

	protected C2LItemZoneDeliveryModeDao getC2LItemZoneDeliveryModeDao()
	{
		return c2LItemZoneDeliveryModeDao;
	}

	@Required
	public void setC2LItemZoneDeliveryModeDao(C2LItemZoneDeliveryModeDao c2lItemZoneDeliveryModeDao)
	{
		c2LItemZoneDeliveryModeDao = c2lItemZoneDeliveryModeDao;
	}

}
