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
package com.sap.wec.adtreco.b2b.bo.impl;

import org.apache.commons.lang.StringUtils;

import com.sap.wec.adtreco.bo.impl.SAPInitiativeReaderImpl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.sap.core.bol.backend.BackendType;

/**
 *
 */
@BackendType("CEI")
public class SAPInitiativeReaderB2BImpl extends SAPInitiativeReaderImpl
{

	public B2BCustomerService b2bCustomerService;
	public B2BUnitService b2bUnitService;
	public String b2bIdSource;
	
	protected static final String EQ_UTF8 = " eq '";
	protected static final String QUOT_UTF8 = "'";
	protected static final String B2B_UNIT_ID = "B2B_UNIT_ID";
	protected static final String B2B_CONTACT_ID = "B2B_CONTACT_ID";
	protected static final String ID_SOURCE = "sapadtrecob2b_b2bIdSource";

	protected String getBusinessPartnerFilter(final String businessPartner, final boolean isAnonymous)
	{
		if (StringUtils.isEmpty(businessPartner))
		{
			return StringUtils.EMPTY;
		}

		String unitId = StringUtils.EMPTY;

		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) b2bCustomerService.getCurrentB2BCustomer();
		if (b2bCustomer != null)
		{
			if (this.getConfiguration().getBaseStoreConfigurationProperty(ID_SOURCE).equals(B2B_UNIT_ID))
			{
				unitId = b2bUnitService.getParent(b2bCustomer).getUid();
				if (unitId != null)
				{
					unitId = StringUtils.leftPad(unitId, 10, '0');
					return super.getBusinessPartnerFilter(unitId, isAnonymous);
				}
			}
			if (this.getConfiguration().getBaseStoreConfigurationProperty(ID_SOURCE).equals(B2B_CONTACT_ID))
			{
				return super.getBusinessPartnerFilter(businessPartner, isAnonymous);
			}		
		}
		else
		{
			return super.getBusinessPartnerFilter(businessPartner, isAnonymous);
		}
		
		return "";
	}

	public B2BCustomerService getB2bCustomerService()
	{
		return b2bCustomerService;
	}

	public void setB2bCustomerService(final B2BCustomerService b2bCustomerService)
	{
		this.b2bCustomerService = b2bCustomerService;
	}

	public B2BUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}
	
	/**
	 * @return the b2bIdSource
	 */
	public String getB2bIdSource()
	{
		return b2bIdSource;
	}

	/**
	 * @param b2bIdSource the b2bIdSource to set
	 */
	public void setB2bIdSource(String b2bIdSource)
	{
		this.b2bIdSource = b2bIdSource;
	}

}
