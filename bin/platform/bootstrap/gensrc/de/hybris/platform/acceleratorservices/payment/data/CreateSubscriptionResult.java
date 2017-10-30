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

import de.hybris.platform.acceleratorservices.payment.data.AbstractPaymentResult;

public  class CreateSubscriptionResult extends AbstractPaymentResult 
{


	/** <i>Generated property</i> for <code>CreateSubscriptionResult.authReplyData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private AuthReplyData authReplyData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.customerInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private CustomerInfoData customerInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.paymentInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private PaymentInfoData paymentInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.orderInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private OrderInfoData orderInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.signatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SignatureData signatureData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.subscriptionInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SubscriptionInfoData subscriptionInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.subscriptionSignatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SubscriptionSignatureData subscriptionSignatureData;
	
	public CreateSubscriptionResult()
	{
		// default constructor
	}
	
	
	
	public void setAuthReplyData(final AuthReplyData authReplyData)
	{
		this.authReplyData = authReplyData;
	}
	
	
	
	public AuthReplyData getAuthReplyData() 
	{
		return authReplyData;
	}
	
	
	
	public void setCustomerInfoData(final CustomerInfoData customerInfoData)
	{
		this.customerInfoData = customerInfoData;
	}
	
	
	
	public CustomerInfoData getCustomerInfoData() 
	{
		return customerInfoData;
	}
	
	
	
	public void setPaymentInfoData(final PaymentInfoData paymentInfoData)
	{
		this.paymentInfoData = paymentInfoData;
	}
	
	
	
	public PaymentInfoData getPaymentInfoData() 
	{
		return paymentInfoData;
	}
	
	
	
	public void setOrderInfoData(final OrderInfoData orderInfoData)
	{
		this.orderInfoData = orderInfoData;
	}
	
	
	
	public OrderInfoData getOrderInfoData() 
	{
		return orderInfoData;
	}
	
	
	
	public void setSignatureData(final SignatureData signatureData)
	{
		this.signatureData = signatureData;
	}
	
	
	
	public SignatureData getSignatureData() 
	{
		return signatureData;
	}
	
	
	
	public void setSubscriptionInfoData(final SubscriptionInfoData subscriptionInfoData)
	{
		this.subscriptionInfoData = subscriptionInfoData;
	}
	
	
	
	public SubscriptionInfoData getSubscriptionInfoData() 
	{
		return subscriptionInfoData;
	}
	
	
	
	public void setSubscriptionSignatureData(final SubscriptionSignatureData subscriptionSignatureData)
	{
		this.subscriptionSignatureData = subscriptionSignatureData;
	}
	
	
	
	public SubscriptionSignatureData getSubscriptionSignatureData() 
	{
		return subscriptionSignatureData;
	}
	


}