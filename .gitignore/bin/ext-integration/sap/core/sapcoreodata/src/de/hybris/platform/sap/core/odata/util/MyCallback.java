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
package de.hybris.platform.sap.core.odata.util;

import java.net.URI;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent;
import org.apache.olingo.odata2.api.ep.callback.WriteCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;


/**
 *
 */
public class MyCallback implements OnWriteFeedContent
{
	protected DataStore dataStore;
	protected URI serviceRoot;

	/**
	 * @param store
	 * @param serviceRoot
	 */
	public MyCallback( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent#retrieveFeedResult(org.apache.olingo.odata2.api.ep.
	 * callback.WriteFeedCallbackContext)
	 */
	/**
	 * 
	 */
	@Override
	public  WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException
	{
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param entitySetName
	 * @param navigationPropertyName
	 * @return
	 * @throws EdmException
	 */
	public boolean isNavigationFromTo(final WriteCallbackContext context, final String entitySetName,
			final String navigationPropertyName) throws EdmException
	{
		return true;
	}

	/**
	 * @return the dataStore
	 */
	public DataStore getDataStore()
	{
		return dataStore;
	}

	/**
	 * @return the serviceRoot
	 */
	public URI getServiceRoot()
	{
		return serviceRoot;
	}
	
	public void setDataStore(DataStore dataStore)
	{
		this.dataStore = dataStore;
	}

	public void setServiceRoot(URI serviceRoot)
	{
		this.serviceRoot = serviceRoot;
	}
}
