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
package de.hybris.platform.sap.sapinvoiceaddon.facade;

import de.hybris.platform.accountsummaryaddon.document.data.B2BDocumentData;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapinvoiceaddon.exception.SapInvoiceException;
import de.hybris.platform.sap.sapinvoiceaddon.model.SapB2BDocumentModel;



/**
 *
 */
public interface B2BInvoiceFacade
{

	/**
	 * @param invoiceDocumentNumber
	 * @return byte array of PDF document
	 * @throws BackendException
	 */
	public abstract SapB2BDocumentModel getOrderForCode(String invoiceDocumentNumber) throws SapInvoiceException;

	public abstract B2BDocumentData convertInvoiceData(SapB2BDocumentModel invoice) throws SapInvoiceException;


}
