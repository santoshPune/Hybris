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
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.productconfig.facades.ClassificationSystemCPQAttributesProvider;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.CsticTypeMapper;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiTypeFinder;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class CsticTypeMapperImpl implements CsticTypeMapper
{
	private static final ThreadLocal<StringBuilder> keyBuilder = new ThreadLocal()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder(128);
		}
	};

	private final static Logger LOG = Logger.getLogger(CsticTypeMapperImpl.class);
	private static final String KEY_SEPARATOR = ".";
	private static final String EMPTY = "";
	private UiTypeFinder uiTypeFinder;
	private ValueFormatTranslator valueFormatTranslator;

	private ConfigPricing pricingFactory;
	private ClassificationSystemCPQAttributesProvider nameProvider;

	private static Pattern HTML_MATCHING_PATTERN = Pattern.compile(".*\\<.+?\\>.*");

	/**
	 * @deprecated Replaced by mapCsticModelToData(final CsticModel model, final String prefix, final Map<String,
	 *             HybrisCsticAndValueNames> nameMap)
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CsticData mapCsticModelToData(final CsticModel model, final String prefix)
	{
		return mapCsticModelToData(model, prefix, null);
	}

	@Override
	public CsticData mapCsticModelToData(final CsticModel model, final String prefix,
			final Map<String, ClassificationSystemCPQAttributesContainer> nameMap)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// LOG.isDebugEnabled() causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		final boolean isDebugEnabledNameProvider = getNameProvider().isDebugEnabled();
		final CsticData data = new CsticData();
		data.setKey(generateUniqueKey(model, prefix));

		final String name = model.getName();
		final ClassificationSystemCPQAttributesContainer cpqAttributes = getNameProvider().getCPQAttributes(name, nameMap);
		data.setName(name);
		data.setLangdepname(getNameProvider().getDisplayName(model, cpqAttributes, isDebugEnabledNameProvider));
		final String longText = getNameProvider().getLongText(model, cpqAttributes, isDebugEnabledNameProvider);
		data.setLongText(longText);
		data.setLongTextHTMLFormat(containsHTML(longText, isDebugEnabled));

		data.setInstanceId(model.getInstanceId());
		data.setVisible(model.isVisible());
		data.setRequired(model.isRequired());
		data.setIntervalInDomain(model.isIntervalInDomain());

		data.setMaxlength(model.getTypeLength());
		data.setEntryFieldMask(emptyIfNull(model.getEntryFieldMask()));
		data.setPlaceholder(emptyIfNull(model.getPlaceholder()));
		data.setAdditionalValue(EMPTY);
		data.setMedia(getNameProvider().getCsticMedia(name, cpqAttributes));

		final List<CsticValueData> domainValues = createDomainValues(model, cpqAttributes, isDebugEnabled);
		handlePriceData(model, data, domainValues);
		data.setDomainvalues(domainValues);

		data.setConflicts(Collections.emptyList());
		if (CsticModel.AUTHOR_USER.equals(model.getAuthor()))
		{
			data.setCsticStatus(CsticStatusType.FINISHED);
		}
		else
		{
			data.setCsticStatus(CsticStatusType.DEFAULT);
		}

		final UiType uiType = uiTypeFinder.findUiTypeForCstic(model, data);
		data.setType(uiType);
		final UiValidationType validationType = uiTypeFinder.findUiValidationTypeForCstic(model);
		data.setValidationType(validationType);

		final String singleValue = model.getSingleValue();
		final String formattedValue = valueFormatTranslator.format(model, singleValue);
		data.setValue(singleValue);
		data.setFormattedValue(formattedValue);
		data.setLastValidValue(formattedValue);

		if (UiValidationType.NUMERIC == validationType)
		{
			mapNumericSpecifics(model, data);
		}

		if (isDebugEnabled)
		{
			LOG.debug("Map CsticModel to CsticData [CSTIC_NAME='" + name + "';CSTIC_UI_KEY='" + data.getKey() + "';CSTIC_UI_TYPE='"
					+ data.getType() + "';CSTIC_VALUE='" + data.getValue() + "']");
		}

		return data;
	}

	protected String emptyIfNull(final String value)
	{
		return (value == null) ? EMPTY : value;
	}

	protected void mapNumericSpecifics(final CsticModel model, final CsticData data)
	{
		final int numFractionDigits = model.getNumberScale();
		final int typeLength = model.getTypeLength();
		data.setNumberScale(numFractionDigits);
		data.setTypeLength(typeLength);

		int maxlength = typeLength;
		if (numFractionDigits > 0)
		{
			maxlength++;
		}
		final int numDigits = typeLength - numFractionDigits;
		final int maxGroupimgSeperators = (numDigits - 1) / 3;
		maxlength += maxGroupimgSeperators;
		data.setMaxlength(maxlength);
	}

	protected List<CsticValueData> createDomainValues(final CsticModel model,
			final ClassificationSystemCPQAttributesContainer hybrisNames, final boolean isDebugEnabled)
	{
		final boolean isDebugEnabledNameProvider = getNameProvider().isDebugEnabled();
		int capa = model.getAssignableValues().size();
		if (model.isConstrained())
		{
			capa += model.getAssignedValues().size();
		}
		final List<CsticValueData> domainValues;
		if (capa == 0)
		{
			domainValues = Collections.emptyList();
		}
		else
		{
			domainValues = new ArrayList<>(capa);
		}

		for (final CsticValueModel csticValue : model.getAssignableValues())
		{
			final CsticValueData domainValue = createDomainValue(model, csticValue, hybrisNames, isDebugEnabled,
					isDebugEnabledNameProvider);
			domainValues.add(domainValue);
		}
		if (model.isConstrained())
		{
			for (final CsticValueModel assignedValue : model.getAssignedValues())
			{
				if (!model.getAssignableValues().contains(assignedValue))
				{
					final CsticValueData domainValue = createDomainValue(model, assignedValue, hybrisNames, isDebugEnabled,
							isDebugEnabledNameProvider);
					domainValues.add(domainValue);
				}
			}
		}
		return domainValues;

	}

	/**
	 * Adds zero prices with currency to the domain-values if the cstic is price relevant.
	 *
	 * @param model
	 * @param data
	 * @param domainValues
	 */
	protected void handlePriceData(final CsticModel model, final CsticData data, final List<CsticValueData> domainValues)
	{
		final String currencyIso = identifyPriceRelevanceAndCurrency(model, data);
		if (data.isPriceRelevant())
		{
			for (final CsticValueData domainValue : domainValues)
			{
				if (domainValue.getDeltaPrice() == ConfigPricing.NO_PRICE)
				{
					final PriceModel priceModel = new PriceModelImpl();
					priceModel.setCurrency(currencyIso);
					priceModel.setPriceValue(BigDecimal.ZERO);
					final PriceData priceData = pricingFactory.getPriceData(priceModel);
					domainValue.setDeltaPrice(priceData);
				}
			}
		}
	}

	protected CsticValueData createDomainValue(final CsticModel csticModel, final CsticValueModel csticValueModel,
			final ClassificationSystemCPQAttributesContainer hybrisNames, final boolean isDebugEnabled,
			final boolean isDebugEnabledNameProvider)
	{
		final CsticValueData domainValue = new CsticValueData();
		final String name = csticValueModel.getName();
		domainValue.setKey(name);
		String langDepName;
		if (CsticModel.TYPE_STRING == csticModel.getValueType())
		{
			langDepName = getNameProvider().getDisplayValueName(csticValueModel, csticModel, hybrisNames,
					isDebugEnabledNameProvider);
		}
		else
		{
			langDepName = valueFormatTranslator.format(csticModel, name);
		}
		domainValue.setLangdepname(langDepName);
		domainValue.setName(name);
		final boolean isAssigned = csticModel.getAssignedValues().contains(csticValueModel);
		domainValue.setSelected(isAssigned);

		final StringBuilder csticValueKey = new StringBuilder();
		csticValueKey.append(csticModel.getName());
		csticValueKey.append('_');
		csticValueKey.append(name);
		domainValue.setMedia(getNameProvider().getCsticValueMedia(csticValueKey.toString(), hybrisNames));

		final boolean isReadOnly = checkReadonly(csticValueModel, isDebugEnabled);
		domainValue.setReadonly(isReadOnly);

		final PriceData price = pricingFactory.getPriceData(csticValueModel.getDeltaPrice());
		domainValue.setDeltaPrice(price);

		return domainValue;
	}

	/**
	 * @deprecated use {@link ValueFormatTranslator#isNumericCsticType(CsticModel)} instead.
	 */
	@Deprecated
	protected boolean isNumericValueType(final CsticModel model, @SuppressWarnings(
	{ "unused", "squid:S1172" })
	final boolean isDebugEnabled)
	{
		return valueFormatTranslator.isNumericCsticType(model);
	}

	protected boolean checkReadonly(final CsticValueModel csticValue, final boolean isDebugEnabled)
	{
		final boolean isSystemValue = csticValue.getAuthor() != null && csticValue.getAuthor().equalsIgnoreCase(READ_ONLY_AUTHOR);

		final boolean isSelectable = csticValue.isSelectable();

		if (isDebugEnabled)
		{
			LOG.debug("CsticValueModel [CSTIC_NAME='" + csticValue.getName() + "';CSTIC_IS_SYSTEM_VALUE='" + isSystemValue
					+ "';CSTIC_IS_SELECTABLE='" + isSelectable + "']");
		}

		return isSystemValue || !isSelectable;
	}


	@Override
	public void updateCsticModelValuesFromData(final CsticData data, final CsticModel model)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// LOG.isDebugEnabled() causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		handleRetraction(data, model, isDebugEnabled);
		final UiType uiType = data.getType();
		if (UiType.CHECK_BOX_LIST == uiType || UiType.CHECK_BOX == uiType || UiType.MULTI_SELECTION_IMAGE == uiType)
		{
			for (final CsticValueData valueData : data.getDomainvalues())
			{
				final String value = valueData.getName();
				final String parsedValue = valueFormatTranslator.parse(uiType, value);
				if (valueData.isSelected())
				{
					model.addValue(parsedValue);
				}
				else
				{
					model.removeValue(parsedValue);
				}
			}
		}
		else
		{
			final String value = getValueFromCstcData(data, isDebugEnabled);
			final String parsedValue = valueFormatTranslator.parse(uiType, value);
			if ((UiType.DROPDOWN == uiType || UiType.DROPDOWN_ADDITIONAL_INPUT == uiType) && "NULL_VALUE".equals(value))
			{
				model.setSingleValue(null);
			}
			else
			{
				model.setSingleValue(parsedValue);
			}
		}

		if (isDebugEnabled)
		{
			LOG.debug("Update CsticData to CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_VALUE_TYPE='"
					+ model.getValueType() + "';CSTIC_UI_KEY='" + data.getKey() + "';CSTIC_UI_TYPE='" + data.getType()
					+ "';CSTIC_VALUE='" + data.getValue() + "']");
		}

	}

	/**
	 * Handles the retraction of a cstic which means that all user inputs to this cstic are discarded. This is needed for
	 * conflict solving
	 *
	 * @param data
	 * @param model
	 */
	protected void handleRetraction(final CsticData data, final CsticModel model, final boolean isDebugEnabled)
	{
		if (data.isRetractTriggered())
		{
			model.setRetractTriggered(true);
			if (isDebugEnabled)
			{
				LOG.debug("Cstic: " + data.getName() + " is marked as retracted");
			}
		}
	}

	/**
	 * @param data
	 * @return value of characteristic
	 */
	protected String getValueFromCstcData(final CsticData data, final boolean isDebugEnabled)
	{
		String value = data.getValue();
		final UiType uiType = data.getType();
		if (UiType.RADIO_BUTTON_ADDITIONAL_INPUT == uiType || UiType.DROPDOWN_ADDITIONAL_INPUT == uiType)
		{
			final String additionalValue = data.getAdditionalValue();
			if (additionalValue != null && !additionalValue.isEmpty())
			{
				value = additionalValue;
			}
		}
		else if (UiType.NUMERIC == uiType)
		{
			value = data.getFormattedValue();
		}

		if (isDebugEnabled)
		{
			LOG.debug("CsticData [CSTIC_NAME='" + data.getName() + "';CSTIC_VALUE='" + value + "']");
		}

		return value;
	}

	@Override
	public String generateUniqueKey(final CsticModel model, final CsticValueModel value, final String prefix)
	{
		final StringBuilder strBuilder = keyBuilder.get();
		strBuilder.setLength(0);
		if (strBuilder.capacity() > 1024)
		{
			strBuilder.trimToSize();
			strBuilder.ensureCapacity(1028);
		}
		strBuilder.append(prefix);
		strBuilder.append(KEY_SEPARATOR);
		strBuilder.append(model.getName());
		if (value != null)
		{
			strBuilder.append(KEY_SEPARATOR);
			strBuilder.append(value.getName());
		}

		return strBuilder.toString();
	}

	@Override
	public String generateUniqueKey(final CsticModel model, final String prefix)
	{
		final String key = generateUniqueKey(model, null, prefix);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_UI_KEY='" + key + "']");
		}

		return key;
	}

	protected boolean containsHTML(final String longText, final boolean isDebugEnabled)
	{
		boolean containsHTML = false;
		if (longText != null)
		{
			containsHTML = HTML_MATCHING_PATTERN.matcher(longText).matches();
		}

		if (isDebugEnabled)
		{
			LOG.debug("Long text contains HTML: '" + containsHTML + "'");
		}

		return containsHTML;
	}

	/**
	 * Checks the CsticModel for price relevance (i.e. at least one assignable value has a value price). It sets the
	 * priceRelevant-flag at csticData and returns the relevant currency (ISO) if the cstic is price relevant.
	 *
	 * @param csticModel
	 *           model to be checked
	 * @param csticData
	 *           DTO to be modified
	 * @return currency (ISO) if cstic is price relevant otherwise null
	 */
	protected String identifyPriceRelevanceAndCurrency(final CsticModel csticModel, final CsticData csticData)
	{
		String currencyIso = null;
		final List<CsticValueModel> values = csticModel.getAssignableValues();
		if (values != null)
		{
			for (final CsticValueModel value : values)
			{
				if (value.getValuePrice() != PriceModel.NO_PRICE)
				{
					csticData.setPriceRelevant(true);
					currencyIso = value.getValuePrice().getCurrency();
					break;
				}
			}
		}
		return currencyIso;
	}

	/**
	 *
	 * @param uiTypeFinder
	 */
	public void setUiTypeFinder(final UiTypeFinder uiTypeFinder)
	{
		this.uiTypeFinder = uiTypeFinder;
	}

	/**
	 * @return the uiTypeFinder
	 */
	public UiTypeFinder getUiTypeFinder()
	{
		return uiTypeFinder;
	}

	/**
	 *
	 * @param pricingFactory
	 */
	public void setPricingFactory(final ConfigPricing pricingFactory)
	{
		this.pricingFactory = pricingFactory;
	}

	/**
	 *
	 * @return pricingFactory
	 */
	protected ConfigPricing getPricingFactory()
	{
		return pricingFactory;
	}

	/**
	 * @param valueFormatTranslator
	 *           the valueFormatTranslator to set
	 */
	public void setValueFormatTranslator(final ValueFormatTranslator valueFormatTranslator)
	{
		this.valueFormatTranslator = valueFormatTranslator;
	}

	/**
	 * @return the valueFormatTranslator
	 */
	protected ValueFormatTranslator getValueFormatTranslator()
	{
		return valueFormatTranslator;
	}

	/**
	 * @return the hybris characteristic and value name provider
	 */
	protected ClassificationSystemCPQAttributesProvider getNameProvider()
	{
		return nameProvider;
	}

	/**
	 * @param nameProvider
	 *           hybris characteristic and value name provider
	 */
	public void setNameProvider(final ClassificationSystemCPQAttributesProvider nameProvider)
	{
		this.nameProvider = nameProvider;
	}
}
