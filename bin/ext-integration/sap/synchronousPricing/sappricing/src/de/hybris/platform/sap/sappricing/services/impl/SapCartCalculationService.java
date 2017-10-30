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
package de.hybris.platform.sap.sappricing.services.impl;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.sap.sappricing.services.SapPricingCartService;
import de.hybris.platform.sap.sappricing.services.SapPricingEnablementService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;


/**
 * SapCartCalculationService
 */
public class SapCartCalculationService extends DefaultCalculationService
{

	private static final Logger LOG = Logger.getLogger(SapCartCalculationService.class);
	private SapPricingCartService sapPricingCartService;
	private SapPricingEnablementService sapPricingEnablementService;

	public SapPricingCartService getSapPricingCartService()
	{
		return sapPricingCartService;
	}

	@Required
	public void setSapPricingCartService(final SapPricingCartService sapPricingCartService)
	{
		this.sapPricingCartService = sapPricingCartService;
	}



	public SapPricingEnablementService getSapPricingEnablementService()
	{
		return sapPricingEnablementService;
	}

	@Required
	public void setSapPricingEnablementService(final SapPricingEnablementService sapPricingEnablementService)
	{
		this.sapPricingEnablementService = sapPricingEnablementService;
	}
	
	@Override
	public void calculate(final AbstractOrderModel order) throws CalculationException
	{
		
		if (sapPricingEnablementService.isCartPricingEnabled())
		{
			getSapPricingCartService().getPriceInformationForCart(order);
				
		}
		
		super.calculate(order);
	}
	
	@Override
	public void recalculate(final AbstractOrderModel order) throws CalculationException
	{
		
		if (sapPricingEnablementService.isCartPricingEnabled())
		{
			getSapPricingCartService().getPriceInformationForCart(order);
				
		}
		
		super.recalculate(order);
	}

	@Override
	public void recalculate(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (!sapPricingEnablementService.isCartPricingEnabled())
		{
			super.resetAllValues(entry);
				
		}
	}
	
	@Override
	protected Map resetAllValues(AbstractOrderModel order)
			throws CalculationException {
		
		if (!sapPricingEnablementService.isCartPricingEnabled()) {
			return super.resetAllValues(order);

		}

		// -----------------------------
		// set subtotal and get tax value map
		final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = calculateSubtotal(
				order, false);
		/*
		 * filter just relative tax values - payment and delivery prices might
		 * require conversion using taxes -> absolute taxes do not apply here
		 * TODO: ask someone for absolute taxes and how they apply to delivery
		 * cost etc. - this implementation might be wrong now
		 */
		final Collection<TaxValue> relativeTaxValues = new LinkedList<TaxValue>();
		for (final Map.Entry<TaxValue, ?> e : taxValueMap.entrySet()) {
			final TaxValue taxValue = e.getKey();
			if (!taxValue.isAbsolute()) {
				relativeTaxValues.add(taxValue);
			}
		}

		return taxValueMap;

	}
	
	@Override
	protected void resetAdditionalCosts(AbstractOrderModel order,
			Collection<TaxValue> relativeTaxValues) {
		
		if (!sapPricingEnablementService.isCartPricingEnabled())
		{
			super.resetAdditionalCosts(order, relativeTaxValues);
				
		}
		
	}
	
	@Override
	protected List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (!sapPricingEnablementService.isCartPricingEnabled())
		{
			return super.findDiscountValues(entry);
				
		}
		
		return entry.getDiscountValues();
	}

		

	@Override
	protected List<DiscountValue> findGlobalDiscounts(final AbstractOrderModel order) throws CalculationException
	{
		if (!sapPricingEnablementService.isCartPricingEnabled())
		{
			return super.findGlobalDiscounts(order);
				
		}
		
		return Collections.emptyList();
	}

}
