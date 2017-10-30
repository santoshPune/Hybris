/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigModelFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationParameterB2B;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingConfigurationParameter;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.ssc.ConfigurationContextAndPricingWrapper;
import de.hybris.platform.sap.productconfig.runtime.ssc.constants.SapproductconfigruntimesscConstants;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.custdev.projects.fbs.slc.cfg.IConfigSession;
import com.sap.custdev.projects.fbs.slc.cfg.client.IDocument;
import com.sap.custdev.projects.fbs.slc.cfg.client.IItemInfo;
import com.sap.custdev.projects.fbs.slc.cfg.client.IPricingAttribute;
import com.sap.custdev.projects.fbs.slc.cfg.client.ItemInfoData;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.DocumentData;
import com.sap.custdev.projects.fbs.slc.cfg.ipintegration.InteractivePricingException;
import com.sap.custdev.projects.fbs.slc.cfg.ipintegration.InteractivePricingIntegration;
import com.sap.custdev.projects.fbs.slc.helper.ConfigSessionManager;
import com.sap.custdev.projects.fbs.slc.pricing.ip.api.InteractivePricingMgr;
import com.sap.custdev.projects.fbs.slc.pricing.slc.api.ISLCDocument;
import com.sap.spe.conversion.ICurrencyValue;
import com.sap.spe.pricing.customizing.IConditionPurpose;
import com.sap.spe.pricing.transactiondata.IPricingCondition;
import com.sap.spe.pricing.transactiondata.IPricingDocument;
import com.sap.spe.pricing.transactiondata.IPricingItem;
import com.sap.sxe.sys.SAPDate;
import com.sap.sxe.sys.SAPTimestamp;


public class ConfigurationContextAndPricingWrapperImpl implements ConfigurationContextAndPricingWrapper
{
	private static final Logger LOG = Logger.getLogger(ConfigurationContextAndPricingWrapperImpl.class);

	private ConfigModelFactory configModelFactory;
	private CommonI18NService i18NService;
	private PricingConfigurationParameter pricingConfigurationParameter;
	private ConfigurationParameterB2B configurationParameterB2B;
	private ProductService productService;

	@Override
	public void preparePricingContext(final IConfigSession session, final String configId, final KBKey kbKey)
			throws InteractivePricingException
	{
		logPricingConfigurationParameters();
		if (pricingConfigurationParameter != null && pricingConfigurationParameter.isPricingSupported())
		{
			final ConfigSessionManager configSessionManager = session.getConfigSessionManager();

			configSessionManager.setPricingContext(configId, getDocumentPricingContext(), getItemPricingContext(kbKey),
					kbKey.getKbLogsys());
			configSessionManager.setInteractivePricingEnabled(configId, true);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Pricing is active for config [CONFIG_ID='" + configId + "']");
			}
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Pricing is disabled/not supported [CONFIG_ID='" + configId + "']");
			}
		}
	}

	protected IDocument getDocumentPricingContext()
	{

		final IDocument documentPricingContext = new DocumentData();

		addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_VKORG,
				pricingConfigurationParameter.getSalesOrganization());
		addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_VTWEG,
				pricingConfigurationParameter.getDistributionChannelForConditions());
		addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_SPART,
				pricingConfigurationParameter.getDivisionForConditions());
		addAttributeToDocumentPricingContext(documentPricingContext,
				SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_HEADER_SPART,
				pricingConfigurationParameter.getDivisionForConditions());

		if (configurationParameterB2B != null && configurationParameterB2B.isSupported())
		{
			addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_KUNNR,
					configurationParameterB2B.getCustomerNumber());
			addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_LAND1,
					configurationParameterB2B.getCountrySapCode());
			addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_KONDA,
					configurationParameterB2B.getCustomerPriceGroup());
		}

		documentPricingContext.setPricingProcedure(pricingConfigurationParameter.getPricingProcedure());

		final CurrencyModel currencyModel = i18NService.getCurrentCurrency();
		final String currency = pricingConfigurationParameter.retrieveCurrencySapCode(currencyModel);

		addAttributeToDocumentPricingContext(documentPricingContext, SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_KONWA,
				currency);
		documentPricingContext.setDocumentCurrencyUnit(currency);
		documentPricingContext.setLocalCurrencyUnit(currency);

		documentPricingContext.setApplication(SapproductconfigruntimesscConstants.APPLICATION_V);
		documentPricingContext.setUsage(SapproductconfigruntimesscConstants.USAGE_A);

		if (LOG.isDebugEnabled())
		{
			documentPricingContext.setPerformPricingTrace(true);
		}

		logDocumentPricingContext(documentPricingContext);

		return documentPricingContext;
	}

	protected void logDocumentPricingContext(final IDocument documentPricingContext)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Document pricing context IDocument:");
			LOG.debug(" - getPricingProcedure() = " + documentPricingContext.getPricingProcedure());
			LOG.debug(" - getDocumentCurrencyUnit() = " + documentPricingContext.getDocumentCurrencyUnit());
			LOG.debug(" - getLocalCurrencyUnit() = " + documentPricingContext.getLocalCurrencyUnit());
			LOG.debug(" - getApplication() = " + documentPricingContext.getApplication());
			LOG.debug(" - getUsage() = " + documentPricingContext.getUsage());
			LOG.debug(" - getPerformTrace() = " + documentPricingContext.getPerformTrace());

			final Map<String, IPricingAttribute> attributes = documentPricingContext.getAttributes();
			if (attributes != null)
			{
				LOG.debug(" - getAttributes() :");
				for (final Map.Entry<String, IPricingAttribute> attribute : attributes.entrySet())
				{
					LOG.debug("  -- Document Pricing Attribute " + attribute.getKey() + " -> " + attribute.getValue().getValues());
				}
			}
		}
	}

	protected IItemInfo getItemPricingContext(final KBKey kbKey)
	{
		final IItemInfo itemPricingContext = new ItemInfoData();

		itemPricingContext.addAttribute(SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_PMATN, kbKey.getProductCode());
		itemPricingContext.setProductId(kbKey.getProductCode());
		itemPricingContext.addAttribute(SapproductconfigruntimesscConstants.PRICING_ATTRIBUTE_PRSFD, "X");

		final SAPTimestamp timeStamp = new SAPTimestamp(new SAPDate(new Date()));
		itemPricingContext.addTimestamp(SapproductconfigruntimesscConstants.DET_DEFAULT_TIMESTAMP,
				timeStamp.formatyyyyMMddHHmmss());

		itemPricingContext.setQuantity(BigDecimal.ONE);
		final ProductModel product = productService.getProductForCode(kbKey.getProductCode());
		final UnitModel unitModel = product.getUnit();
		final String sapUOM = pricingConfigurationParameter.retrieveUnitSapCode(unitModel);
		itemPricingContext.setQuantityUnit(sapUOM);

		itemPricingContext.setPricingRelevant(true);

		logItemPricingContext(itemPricingContext);

		return itemPricingContext;
	}

	protected void logItemPricingContext(final IItemInfo itemPricingContext)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Item pricing context IITemInfo:");
			LOG.debug(" - getProductId() = " + itemPricingContext.getProductId());
			LOG.debug(" - getTimeStamps() = " + itemPricingContext.getTimestamps());
			LOG.debug(" - getQuantity() = " + itemPricingContext.getQuantity());
			LOG.debug(" - getQuantityUnit() = " + itemPricingContext.getQuantityUnit());
			LOG.debug(" - getPricingRelevantFlag() = " + itemPricingContext.getPricingRelevantFlag());

			final Map<String, IPricingAttribute> attributes = itemPricingContext.getAttributes();
			if (attributes != null)
			{
				LOG.debug(" - getAttributes() :");
				for (final Map.Entry<String, IPricingAttribute> attribute : attributes.entrySet())
				{
					LOG.debug("  -- Item Pricing Attribute " + attribute.getKey() + " -> " + attribute.getValue().getValues());
				}
			}
		}
	}

	@Override
	public void processPrice(final IConfigSession session, final String configId, final ConfigModel configModel)
			throws InteractivePricingException
	{
		if (pricingConfigurationParameter != null && pricingConfigurationParameter.isPricingSupported())
		{
			final ConfigSessionManager configSessionManager = session.getConfigSessionManager();

			final InteractivePricingIntegration interactivePricing = configSessionManager.getInteractivePricingIntegration(configId);
			final InteractivePricingMgr pricingManager = interactivePricing.getInteractivePricingManager();

			final ISLCDocument document = pricingManager.getDocument();
			final IPricingDocument pricingDocument = document.getPricingDocument();
			final ICurrencyValue documentNetValue = pricingDocument.getNetValueWithoutFreight();
			provideCurrentTotalPriceModel(configModel, documentNetValue);

			final String targetForBasePrice = pricingConfigurationParameter.getTargetForBasePrice();
			final String targetForSelectedOptionsPrice = pricingConfigurationParameter.getTargetForSelectedOptions();
			Map<String, ICurrencyValue> condFuncValuesMap = null;

			if ((targetForBasePrice != null && !targetForBasePrice.trim().isEmpty())
					|| (targetForSelectedOptionsPrice != null && !targetForSelectedOptionsPrice.trim().isEmpty()))
			{
				condFuncValuesMap = retrieveAccumulatedValuesForConditionsWithPurpose(pricingDocument);
			}

			retrieveBasePrice(targetForBasePrice, condFuncValuesMap, configModel);
			retrieveSelectedOptionsPrice(targetForSelectedOptionsPrice, condFuncValuesMap, configModel);

			logPrices(documentNetValue, condFuncValuesMap);
			logDocumentPricingConditions(pricingDocument);
		}
	}

	protected void provideCurrentTotalPriceModel(final ConfigModel configModel, final ICurrencyValue netValue)
	{
		final PriceModel currentTotalPriceModel = configModelFactory.createInstanceOfPriceModel();
		currentTotalPriceModel.setPriceValue(netValue.getValue());
		currentTotalPriceModel.setCurrency(netValue.getUnitName());
		configModel.setCurrentTotalPrice(currentTotalPriceModel);
	}

	protected void retrieveBasePrice(final String targetForBasePrice, final Map<String, ICurrencyValue> condFuncValuesMap,
			final ConfigModel configModel)
	{
		ICurrencyValue basePrice = null;

		if (targetForBasePrice == null || targetForBasePrice.trim().isEmpty())
		{
			LOG.debug("Target for Base Price is not maintained in the SAP Base Store Configuration");
		}

		else
		{
			if (condFuncValuesMap != null)
			{
				basePrice = condFuncValuesMap.get(targetForBasePrice);
			}
			if (basePrice == null && LOG.isDebugEnabled())
			{
				LOG.debug("No Base Price retrieved for " + configModel.getRootInstance().getName());
			}
		}

		if (basePrice != null)
		{
			final PriceModel basePriceModel = configModelFactory.createInstanceOfPriceModel();
			basePriceModel.setPriceValue(basePrice.getValue());
			basePriceModel.setCurrency(basePrice.getUnitName());
			configModel.setBasePrice(basePriceModel);
		}
		else
		{
			configModel.setBasePrice(configModelFactory.getZeroPriceModel());
		}
	}

	protected void retrieveSelectedOptionsPrice(final String targetForSelectedOptionsPrice,
			final Map<String, ICurrencyValue> condFuncValuesMap, final ConfigModel configModel)
	{
		ICurrencyValue optionPrice = null;

		if (targetForSelectedOptionsPrice == null || targetForSelectedOptionsPrice.trim().isEmpty())
		{
			LOG.debug("Target for Selected Options is not maintained in the SAP Base Store Configuration");
		}

		else
		{
			if (condFuncValuesMap != null)
			{
				optionPrice = condFuncValuesMap.get(targetForSelectedOptionsPrice);
			}
			if (optionPrice == null && LOG.isDebugEnabled())
			{
				LOG.debug("No Selected Options Price retrieved for " + configModel.getRootInstance().getName());
			}
		}

		if (optionPrice != null)
		{
			final PriceModel selectedOptionsPriceModel = configModelFactory.createInstanceOfPriceModel();
			selectedOptionsPriceModel.setPriceValue(optionPrice.getValue());
			selectedOptionsPriceModel.setCurrency(optionPrice.getUnitName());
			configModel.setSelectedOptionsPrice(selectedOptionsPriceModel);
		}
		else
		{
			configModel.setSelectedOptionsPrice(configModelFactory.getZeroPriceModel());
		}
	}

	protected void logPrices(final ICurrencyValue netValue, final Map<String, ICurrencyValue> condFuncValuesMap)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Net Price: " + netValue);

			if (condFuncValuesMap != null)
			{
				for (final Map.Entry<String, ICurrencyValue> condFuncEntry : condFuncValuesMap.entrySet())
				{
					LOG.debug("Destination (Condition Function): " + condFuncEntry.getKey() + " -> " + condFuncEntry.getValue());
				}
			}
		}

	}

	protected void logPricingConditions(final IPricingItem pricingItem)
	{
		if (LOG.isDebugEnabled())
		{
			final IPricingCondition[] pricingConditions = pricingItem.getPricingConditions();
			logConditions(pricingConditions);
		}
	}

	protected void logDocumentPricingConditions(final IPricingDocument pricingDocument)
	{
		if (LOG.isDebugEnabled())
		{
			final IPricingCondition[] pricingConditions = pricingDocument.getConditions();
			logConditions(pricingConditions);
		}
	}

	protected void logConditions(final IPricingCondition[] pricingConditions)
	{
		LOG.debug("PricingConditions  (Condition Type; Condition Rate; Condition Value; Condition Base; Purpose):");
		for (final IPricingCondition pricingCondition : pricingConditions)
		{
			String conditionType = "";
			if (pricingCondition.getConditionTypeName() != null)
			{
				conditionType = pricingCondition.getConditionTypeName();
			}
			if (pricingCondition.getDescription() != null)
			{
				conditionType = conditionType.concat(pricingCondition.getDescription());
			}

			final IConditionPurpose conditionPurpose = pricingCondition.getPurpose();
			String purposeName = "";
			if (conditionPurpose != null)
			{
				purposeName = conditionPurpose.getName();
			}
			LOG.debug(conditionType + "; " + pricingCondition.getConditionRate() + "; " + pricingCondition.getConditionValue() + "; "
					+ pricingCondition.getConditionBase() + "; " + purposeName);
		}
	}

	protected void logPricingConfigurationParameters()
	{
		if (LOG.isDebugEnabled())
		{
			if (pricingConfigurationParameter != null)
			{
				LOG.debug("Pricing Configuration Parameters:");
				LOG.debug(" - isPricingSupported: " + pricingConfigurationParameter.isPricingSupported());
				LOG.debug(" - SalesOrganization: " + pricingConfigurationParameter.getSalesOrganization());
				LOG.debug(
						" - DistributionChannelForConditions: " + pricingConfigurationParameter.getDistributionChannelForConditions());
				LOG.debug(" - DivisionForConditions: " + pricingConfigurationParameter.getDivisionForConditions());
				LOG.debug(" - PricingProcedure: " + pricingConfigurationParameter.getPricingProcedure());
				LOG.debug(" - TargetForBasePrice: " + pricingConfigurationParameter.getTargetForBasePrice());
				LOG.debug(" - TargetForSelectedOptions: " + pricingConfigurationParameter.getTargetForSelectedOptions());
			}
			if (configurationParameterB2B != null)
			{
				LOG.debug("B2B Configuration Parameters:");
				LOG.debug(" - isSupported: " + configurationParameterB2B.isSupported());
				LOG.debug(" - CustomerNumber: " + configurationParameterB2B.getCustomerNumber());
				LOG.debug(" - CustomerPriceGroup: " + configurationParameterB2B.getCustomerPriceGroup());
				LOG.debug(" - CountrySapCode: " + configurationParameterB2B.getCountrySapCode());
			}
		}
	}

	@Override
	public Hashtable<String, String> retrieveConfigurationContext(final KBKey kbKey) //NOSONAR
	{
		//Reason for Hashtable instead of Map: SSC needs context map this way
		final Hashtable<String, String> configContext = new Hashtable<>();//NOSONAR

		if (pricingConfigurationParameter != null)
		{
			addCustomerNumberToContext(configContext);
			addCountrySapCodeToContext(configContext);
			addSalesOrganisationToContext(configContext);
			addDistributionChannelToContext(configContext);
			addDivisionsForConditionsToContext(configContext);
		}

		final String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAK_ERDAT, date);
		configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAP_KWMENG, "1");
		configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAP_MATNR, kbKey.getProductCode());

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieved config context: " + configContext);
		}
		return configContext;
	}

	protected void addDivisionsForConditionsToContext(final Map<String, String> configContext)
	{
		final String division = pricingConfigurationParameter.getDivisionForConditions();
		if (division != null && !division.isEmpty())
		{
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAK_SPART, division);
		}
	}

	protected void addDistributionChannelToContext(final Map<String, String> configContext)
	{
		final String distributionChannel = pricingConfigurationParameter.getDistributionChannelForConditions();
		if (distributionChannel != null && !distributionChannel.isEmpty())
		{
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAK_VTWEG, distributionChannel);
		}
	}

	protected void addSalesOrganisationToContext(final Map<String, String> configContext)
	{
		final String salesOrganization = pricingConfigurationParameter.getSalesOrganization();
		if (salesOrganization != null && !salesOrganization.isEmpty())
		{
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAK_VKORG, salesOrganization);
		}
	}

	protected void addCountrySapCodeToContext(final Map<String, String> configContext)
	{
		String country = null;
		if (configurationParameterB2B != null && configurationParameterB2B.isSupported())
		{
			country = configurationParameterB2B.getCountrySapCode();
		}
		if (country != null && !country.isEmpty())
		{
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBPA_AG_LAND1, country);
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBPA_RG_LAND1, country);
		}
	}

	protected void addCustomerNumberToContext(final Map<String, String> configContext)
	{
		String customerNumber = null;
		if (configurationParameterB2B != null && configurationParameterB2B.isSupported())
		{
			customerNumber = configurationParameterB2B.getCustomerNumber();
		}
		if (customerNumber != null && !customerNumber.isEmpty())
		{
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBAK_KUNNR, customerNumber);
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBPA_AG_KUNNR, customerNumber);
			configContext.put(SapproductconfigruntimesscConstants.CONTEXT_ATTRIBUTE_VBPA_RG_KUNNR, customerNumber);
		}
	}

	protected void addAttributeToDocumentPricingContext(final IDocument documentPricingContext, final String attributeName,
			final String attributeValue)
	{
		if (attributeValue != null && !attributeValue.isEmpty())
		{
			documentPricingContext.addAttribute(attributeName, attributeValue);
		}
	}

	public void setI18NService(final CommonI18NService i18nService)
	{
		i18NService = i18nService;
	}

	public void setPricingConfigurationParameter(final PricingConfigurationParameter pricingConfigurationParameter)
	{
		this.pricingConfigurationParameter = pricingConfigurationParameter;
	}

	public void setConfigurationParameterB2B(final ConfigurationParameterB2B configurationParameterB2B)
	{
		this.configurationParameterB2B = configurationParameterB2B;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	protected PricingConfigurationParameter getPricingConfigurationParameter()
	{
		return pricingConfigurationParameter;
	}

	protected ConfigurationParameterB2B getConfigurationParameterB2B()
	{
		return configurationParameterB2B;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setConfigModelFactory(final ConfigModelFactory configModelFactory)
	{
		this.configModelFactory = configModelFactory;
	}

	protected ConfigModelFactory getConfigModelFactory()
	{
		return configModelFactory;
	}

	@java.lang.SuppressWarnings("accxrepository:PrivateMethodDeclaration")
	private Map<String, ICurrencyValue> retrieveAccumulatedValuesForConditionsWithPurpose(final IPricingDocument pricingDocument)
			throws InteractivePricingException
	{
		Map<String, ICurrencyValue> condFuncValuesMap = null;

		Method method = null;

		try
		{
			method = pricingDocument.getClass().getMethod("getAccumulatedValuesForConditionsWithPurpose");
		}
		catch (final NoSuchMethodException e)
		{
			LOG.warn(
					"Not possible to get prices for Base Price and Selected Options. If you are using the CPQ runtime engine 2.0 SP4, you have to download the newest patch of them ",
					e);
			return null;
		}

		try
		{
			condFuncValuesMap = (Map<String, ICurrencyValue>) method.invoke(pricingDocument);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new InteractivePricingException("Not possible to get prices for Base Price and Selected Options", e);
		}

		return condFuncValuesMap;
	}
}
