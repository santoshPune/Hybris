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
package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import java.util.List;

public  class CCPaymentInfoDatas  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CCPaymentInfoDatas.paymentInfos</code> property defined at extension <code>commercefacades</code>. */
		
	private List<CCPaymentInfoData> paymentInfos;
	
	public CCPaymentInfoDatas()
	{
		// default constructor
	}
	
	
	
	public void setPaymentInfos(final List<CCPaymentInfoData> paymentInfos)
	{
		this.paymentInfos = paymentInfos;
	}
	
	
	
	public List<CCPaymentInfoData> getPaymentInfos() 
	{
		return paymentInfos;
	}
	


}