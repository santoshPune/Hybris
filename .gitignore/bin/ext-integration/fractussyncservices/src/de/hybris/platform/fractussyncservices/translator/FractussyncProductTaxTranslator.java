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
*
*/
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.externaltax.ProductTaxCodeService;
import de.hybris.platform.externaltax.impl.DefaultProductTaxCodeService;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;


/**
 * Allows to transfer tax data from a source platform to another target platform like this:
 * <p>
 * Export:
 * <p>
 * It assembles a URL which shall be accessible be the target system. For that a 'baseURL' ImpEx property may be
 * specified. If baseURL is omitted, the platform property 'y2ysync.home.url' will be used.
 * <p>
 * Example ImpEx code:
 * <code>INSERT_UPDATE TaxInformation; @noMatter[translator=de.hybris.platform.y2yaassyncmicrosites.translator.ProductTaxTranslator, baseURL='http://public.host.url'];</code>
 */
public class FractussyncProductTaxTranslator implements SpecialValueTranslator
{
	private boolean isForExport;

	protected ModelService modelService;

	protected ProductTaxCodeService productTaxCodeService;

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		isForExport = isExport(columnDescriptor);

		modelService = (DefaultModelService) Registry.getApplicationContext().getBean("modelService");
		productTaxCodeService = (DefaultProductTaxCodeService) Registry.getApplicationContext().getBean("productTaxCodeService");
	}

	protected boolean isExport(final SpecialColumnDescriptor cd)
	{
		final EnumerationValue mode = cd.getHeader().getReader().getValidationMode();
		final String code = mode != null ? mode.getCode() : "n/a";

		return ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY.equalsIgnoreCase(code)
				|| ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_RELAXED.equalsIgnoreCase(code)
				|| ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT.equalsIgnoreCase(code);
	}

	@Override
	public void validate(final String expr) throws HeaderValidationException
	{
		//do nothing here.
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		if (item instanceof Product)
		{
			try
			{
				return getTranslatedValue((Product) item);
			}
			catch (final JaloSecurityException e)
			{
				throw new ImpExException(e);
			}
			catch (final JaloPriceFactoryException e)
			{
				throw new ImpExException(e);
			}
		}

		return null;
	}

	/**
	 * @param item
	 * @return
	 * @throws JaloSecurityException
	 * @throws JaloPriceFactoryException
	 */
	protected String getTranslatedValue(final Product item) throws JaloSecurityException, JaloPriceFactoryException
	{
		final StringBuilder sb = new StringBuilder();

		final ProductModel product = (ProductModel) modelService.get(item);

		final Collection<ProductTaxCodeModel> taxCodeList = productTaxCodeService.getTaxCodesForProduct(product.getCode());

		if (taxCodeList != null)
		{
			for (final ProductTaxCodeModel code : taxCodeList)
			{
				sb.append(code.getTaxCode()).append(ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER);
			}
		}

		return sb.toString();
	}

	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		throw new ImpExException(new UnsupportedOperationException());
	}

	@Override
	public boolean isEmpty(final String cellValue)
	{
		return StringUtils.isBlank(cellValue);
	}

	protected boolean isForExport()
	{
		return isForExport;
	}



}
