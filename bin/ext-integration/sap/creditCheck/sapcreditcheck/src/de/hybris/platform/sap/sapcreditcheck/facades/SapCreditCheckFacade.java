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
package de.hybris.platform.sap.sapcreditcheck.facades;

/**
 *
 */
public interface SapCreditCheckFacade
{
	
	/**
	 * Check if the credit limit has been exceeded 
	 * @return true if credit check exceeded
	 */
	abstract boolean checkCreditLimitExceeded(String orderCode);
	 
	
	/**
	 * checks if order ir credit bocked
	 * @param orderCode
	 * @return true if order is credit blocked
	 */
	abstract boolean checkOrderCreditBlocked(String orderCode);
	
	
	
	
}
