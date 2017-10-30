/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.acceleratorservices.payment.data;

import de.hybris.platform.acceleratorservices.payment.data.HostedOrderPageResult;

public  class AbstractPaymentResult extends HostedOrderPageResult 
{


	/** <i>Generated property</i> for <code>AbstractPaymentResult.decision</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String decision;

	/** <i>Generated property</i> for <code>AbstractPaymentResult.decisionPublicSignature</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String decisionPublicSignature;

	/** <i>Generated property</i> for <code>AbstractPaymentResult.transactionSignatureVerified</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Boolean transactionSignatureVerified;
	
	public AbstractPaymentResult()
	{
		// default constructor
	}
	
	
	
	public void setDecision(final String decision)
	{
		this.decision = decision;
	}
	
	
	
	public String getDecision() 
	{
		return decision;
	}
	
	
	
	public void setDecisionPublicSignature(final String decisionPublicSignature)
	{
		this.decisionPublicSignature = decisionPublicSignature;
	}
	
	
	
	public String getDecisionPublicSignature() 
	{
		return decisionPublicSignature;
	}
	
	
	
	public void setTransactionSignatureVerified(final Boolean transactionSignatureVerified)
	{
		this.transactionSignatureVerified = transactionSignatureVerified;
	}
	
	
	
	public Boolean getTransactionSignatureVerified() 
	{
		return transactionSignatureVerified;
	}
	


}