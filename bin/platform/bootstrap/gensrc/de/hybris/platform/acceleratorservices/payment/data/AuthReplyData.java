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

import java.math.BigDecimal;

public  class AuthReplyData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyReasonCode</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer ccAuthReplyReasonCode;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyAuthorizationCode</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyAuthorizationCode;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyCvCode</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyCvCode;

	/** <i>Generated property</i> for <code>AuthReplyData.cvnDecision</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Boolean cvnDecision;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyAvsCodeRaw</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyAvsCodeRaw;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyAvsCode</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyAvsCode;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyAmount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private BigDecimal ccAuthReplyAmount;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyProcessorResponse</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyProcessorResponse;

	/** <i>Generated property</i> for <code>AuthReplyData.ccAuthReplyAuthorizedDateTime</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String ccAuthReplyAuthorizedDateTime;
	
	public AuthReplyData()
	{
		// default constructor
	}
	
	
	
	public void setCcAuthReplyReasonCode(final Integer ccAuthReplyReasonCode)
	{
		this.ccAuthReplyReasonCode = ccAuthReplyReasonCode;
	}
	
	
	
	public Integer getCcAuthReplyReasonCode() 
	{
		return ccAuthReplyReasonCode;
	}
	
	
	
	public void setCcAuthReplyAuthorizationCode(final String ccAuthReplyAuthorizationCode)
	{
		this.ccAuthReplyAuthorizationCode = ccAuthReplyAuthorizationCode;
	}
	
	
	
	public String getCcAuthReplyAuthorizationCode() 
	{
		return ccAuthReplyAuthorizationCode;
	}
	
	
	
	public void setCcAuthReplyCvCode(final String ccAuthReplyCvCode)
	{
		this.ccAuthReplyCvCode = ccAuthReplyCvCode;
	}
	
	
	
	public String getCcAuthReplyCvCode() 
	{
		return ccAuthReplyCvCode;
	}
	
	
	
	public void setCvnDecision(final Boolean cvnDecision)
	{
		this.cvnDecision = cvnDecision;
	}
	
	
	
	public Boolean getCvnDecision() 
	{
		return cvnDecision;
	}
	
	
	
	public void setCcAuthReplyAvsCodeRaw(final String ccAuthReplyAvsCodeRaw)
	{
		this.ccAuthReplyAvsCodeRaw = ccAuthReplyAvsCodeRaw;
	}
	
	
	
	public String getCcAuthReplyAvsCodeRaw() 
	{
		return ccAuthReplyAvsCodeRaw;
	}
	
	
	
	public void setCcAuthReplyAvsCode(final String ccAuthReplyAvsCode)
	{
		this.ccAuthReplyAvsCode = ccAuthReplyAvsCode;
	}
	
	
	
	public String getCcAuthReplyAvsCode() 
	{
		return ccAuthReplyAvsCode;
	}
	
	
	
	public void setCcAuthReplyAmount(final BigDecimal ccAuthReplyAmount)
	{
		this.ccAuthReplyAmount = ccAuthReplyAmount;
	}
	
	
	
	public BigDecimal getCcAuthReplyAmount() 
	{
		return ccAuthReplyAmount;
	}
	
	
	
	public void setCcAuthReplyProcessorResponse(final String ccAuthReplyProcessorResponse)
	{
		this.ccAuthReplyProcessorResponse = ccAuthReplyProcessorResponse;
	}
	
	
	
	public String getCcAuthReplyProcessorResponse() 
	{
		return ccAuthReplyProcessorResponse;
	}
	
	
	
	public void setCcAuthReplyAuthorizedDateTime(final String ccAuthReplyAuthorizedDateTime)
	{
		this.ccAuthReplyAuthorizedDateTime = ccAuthReplyAuthorizedDateTime;
	}
	
	
	
	public String getCcAuthReplyAuthorizedDateTime() 
	{
		return ccAuthReplyAuthorizedDateTime;
	}
	


}