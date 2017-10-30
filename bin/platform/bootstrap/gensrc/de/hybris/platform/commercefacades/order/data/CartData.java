/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:02 PM
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

import de.hybris.platform.acceleratorservices.enums.ImportStatus;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.util.Date;
import java.util.List;

public  class CartData extends AbstractOrderData 
{


	/** <i>Generated property</i> for <code>CartData.totalUnitCount</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer totalUnitCount;

	/** <i>Generated property</i> for <code>CartData.potentialOrderPromotions</code> property defined at extension <code>commercefacades</code>. */
		
	private List<PromotionResultData> potentialOrderPromotions;

	/** <i>Generated property</i> for <code>CartData.potentialProductPromotions</code> property defined at extension <code>commercefacades</code>. */
		
	private List<PromotionResultData> potentialProductPromotions;

	/** <i>Generated property</i> for <code>CartData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CartData.description</code> property defined at extension <code>commercefacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CartData.expirationTime</code> property defined at extension <code>commercefacades</code>. */
		
	private Date expirationTime;

	/** <i>Generated property</i> for <code>CartData.saveTime</code> property defined at extension <code>commercefacades</code>. */
		
	private Date saveTime;

	/** <i>Generated property</i> for <code>CartData.savedBy</code> property defined at extension <code>commercefacades</code>. */
		
	private PrincipalData savedBy;

	/** <i>Generated property</i> for <code>CartData.importStatus</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private ImportStatus importStatus;
	
	public CartData()
	{
		// default constructor
	}
	
	@Override 
	
	public void setTotalUnitCount(final Integer totalUnitCount)
	{
		this.totalUnitCount = totalUnitCount;
	}
	
	@Override 
	
	public Integer getTotalUnitCount() 
	{
		return totalUnitCount;
	}
	
	
	
	public void setPotentialOrderPromotions(final List<PromotionResultData> potentialOrderPromotions)
	{
		this.potentialOrderPromotions = potentialOrderPromotions;
	}
	
	
	
	public List<PromotionResultData> getPotentialOrderPromotions() 
	{
		return potentialOrderPromotions;
	}
	
	
	
	public void setPotentialProductPromotions(final List<PromotionResultData> potentialProductPromotions)
	{
		this.potentialProductPromotions = potentialProductPromotions;
	}
	
	
	
	public List<PromotionResultData> getPotentialProductPromotions() 
	{
		return potentialProductPromotions;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	
	
	public String getDescription() 
	{
		return description;
	}
	
	
	
	public void setExpirationTime(final Date expirationTime)
	{
		this.expirationTime = expirationTime;
	}
	
	
	
	public Date getExpirationTime() 
	{
		return expirationTime;
	}
	
	
	
	public void setSaveTime(final Date saveTime)
	{
		this.saveTime = saveTime;
	}
	
	
	
	public Date getSaveTime() 
	{
		return saveTime;
	}
	
	
	
	public void setSavedBy(final PrincipalData savedBy)
	{
		this.savedBy = savedBy;
	}
	
	
	
	public PrincipalData getSavedBy() 
	{
		return savedBy;
	}
	
	
	
	public void setImportStatus(final ImportStatus importStatus)
	{
		this.importStatus = importStatus;
	}
	
	
	
	public ImportStatus getImportStatus() 
	{
		return importStatus;
	}
	


}