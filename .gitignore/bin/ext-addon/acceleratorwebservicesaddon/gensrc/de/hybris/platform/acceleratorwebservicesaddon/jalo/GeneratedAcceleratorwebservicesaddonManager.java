/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 11:21:07 AM                    ---
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
 *  
 */
package de.hybris.platform.acceleratorwebservicesaddon.jalo;

import de.hybris.platform.acceleratorwebservicesaddon.constants.AcceleratorwebservicesaddonConstants;
import de.hybris.platform.acceleratorwebservicesaddon.jalo.payment.OldPaymentSubscriptionResultRemovalCronJob;
import de.hybris.platform.acceleratorwebservicesaddon.jalo.payment.PaymentSubscriptionResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>AcceleratorwebservicesaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedAcceleratorwebservicesaddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public OldPaymentSubscriptionResultRemovalCronJob createOldPaymentSubscriptionResultRemovalCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AcceleratorwebservicesaddonConstants.TC.OLDPAYMENTSUBSCRIPTIONRESULTREMOVALCRONJOB );
			return (OldPaymentSubscriptionResultRemovalCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating OldPaymentSubscriptionResultRemovalCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public OldPaymentSubscriptionResultRemovalCronJob createOldPaymentSubscriptionResultRemovalCronJob(final Map attributeValues)
	{
		return createOldPaymentSubscriptionResultRemovalCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public PaymentSubscriptionResult createPaymentSubscriptionResult(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AcceleratorwebservicesaddonConstants.TC.PAYMENTSUBSCRIPTIONRESULT );
			return (PaymentSubscriptionResult)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PaymentSubscriptionResult : "+e.getMessage(), 0 );
		}
	}
	
	public PaymentSubscriptionResult createPaymentSubscriptionResult(final Map attributeValues)
	{
		return createPaymentSubscriptionResult( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return AcceleratorwebservicesaddonConstants.EXTENSIONNAME;
	}
	
}
