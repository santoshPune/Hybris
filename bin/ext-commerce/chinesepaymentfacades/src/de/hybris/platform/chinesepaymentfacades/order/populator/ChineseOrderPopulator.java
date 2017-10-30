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
package de.hybris.platform.chinesepaymentfacades.order.populator;

import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentInfoData;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.enums.ServiceType;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.PaymentModeService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class ChineseOrderPopulator extends AbstractOrderPopulator<OrderModel, OrderData>
{
	private final static String PAYMENT_SERVICE_PROVIDER_SERVICE_SUFFIX = "PaymentService";

	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;

	private EnumerationService enumerationService;

	private StoreSessionFacade storeSessionFacade;

	private PaymentModeService paymentModeService;

	@Override
	public void populate(final OrderModel source, final OrderData target) throws ConversionException
	{
		if (source.getChinesePaymentInfo() != null)
		{
			final ChinesePaymentInfoData chinesePaymentInfoData = new ChinesePaymentInfoData();
			chinesePaymentInfoData.setId(source.getChinesePaymentInfo().getCode());
			chinesePaymentInfoData.setPaymentProvider(source.getChinesePaymentInfo().getPaymentProvider());

			final String iso = storeSessionFacade.getCurrentLanguage().getIsocode();
			final Locale currentLocale = new Locale(iso);
			final String serviceType = enumerationService.getEnumerationName(
					ServiceType.valueOf(source.getChinesePaymentInfo().getServiceType().getCode()), currentLocale);
			chinesePaymentInfoData.setServiceType(serviceType);

			final String paymentProviderName = paymentModeService.getPaymentModeForCode(
					source.getChinesePaymentInfo().getPaymentProvider()).getName();
			chinesePaymentInfoData.setPaymentProviderName(paymentProviderName);

			final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy.getPaymentService(source
					.getChinesePaymentInfo().getPaymentProvider() + PAYMENT_SERVICE_PROVIDER_SERVICE_SUFFIX);
			chinesePaymentInfoData.setPaymentProviderLogo(chinesePaymentService.getPspLogoUrl());
			target.setChinesePaymentInfo(chinesePaymentInfoData);
		}
	}

	protected ChinesePaymentServicesStrategy getChinesePaymentServicesStrategy()
	{
		return chinesePaymentServicesStrategy;
	}

	@Required
	public void setChinesePaymentServicesStrategy(final ChinesePaymentServicesStrategy chinesePaymentServicesStrategy)
	{
		this.chinesePaymentServicesStrategy = chinesePaymentServicesStrategy;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	protected PaymentModeService getPaymentModeService()
	{
		return paymentModeService;
	}

	@Required
	public void setPaymentModeService(PaymentModeService paymentModeService)
	{
		this.paymentModeService = paymentModeService;
	}


}
