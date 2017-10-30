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
package de.hybris.platform.sap.productconfig.frontend.validator;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.frontend.util.ConfigDataMergeProcessor;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class ProductConfigurationValidator implements Validator
{
	private NumericChecker numericChecker;
	private ConfigDataMergeProcessor mergeProcessor;

	@Override
	public boolean supports(final Class<?> classObj)
	{
		return ConfigurationData.class.equals(classObj);
	}

	@Override
	public void validate(final Object configurationObj, final Errors errorObj)
	{
		final ConfigurationData configuration = (ConfigurationData) configurationObj;

		mergeProcessor.completeInput(configuration);

		final List<UiGroupData> groups = configuration.getGroups();

		validateSubGroups(groups, errorObj, "groups");

	}

	protected void validateGroup(final UiGroupData group, final Errors errorObj)
	{
		final List<CsticData> cstics = group.getCstics();
		validateCstics(cstics, errorObj);

		final List<UiGroupData> subGroups = group.getSubGroups();
		validateSubGroups(subGroups, errorObj, "subGroups");
	}

	/**
	 * @param subGroups
	 * @param errorObj
	 */
	protected void validateSubGroups(final List<UiGroupData> subGroups, final Errors errorObj, final String groupListName)
	{
		if (subGroups == null)
		{
			return;
		}
		for (int ii = 0; ii < subGroups.size(); ii++)
		{
			final UiGroupData subGroup = subGroups.get(ii);
			final String prefix = groupListName + "[" + ii + "]";
			errorObj.pushNestedPath(prefix);
			validateGroup(subGroup, errorObj);
			errorObj.popNestedPath();
		}
	}

	/**
	 * @param cstics
	 * @param errorObj
	 */
	protected void validateCstics(final List<CsticData> cstics, final Errors errorObj)
	{
		if (cstics == null)
		{
			return;
		}
		for (int ii = 0; ii < cstics.size(); ii++)
		{
			final CsticData csticData = cstics.get(ii);
			final String value;
			if (validateAdditionalValue(csticData))
			{
				value = csticData.getAdditionalValue();
			}
			else
			{
				value = csticData.getFormattedValue();
			}

			final boolean nothingToValidate = isEmptyOrNull(value);
			if (nothingToValidate)
			{
				continue;
			}

			errorObj.pushNestedPath("cstics[" + ii + "]");
			validate(csticData, errorObj);
			errorObj.popNestedPath();
		}
	}


	protected void validate(final CsticData cstic, final Errors errorObj)
	{
		final UiValidationType type = cstic.getValidationType();


		if (UiValidationType.NUMERIC.equals(type))
		{
			if (validateAdditionalValue(cstic))
			{
				validateAdditionalValueNumeric(cstic, errorObj);
			}
			else
			{
				validateNumeric(cstic, errorObj);
			}
		}
	}

	protected boolean validateAdditionalValue(final CsticData cstic)
	{
		return cstic.getType() == UiType.DROPDOWN_ADDITIONAL_INPUT || cstic.getType() == UiType.RADIO_BUTTON_ADDITIONAL_INPUT;
	}

	protected void validateNumeric(final CsticData cstic, final Errors errorObj)
	{
		numericChecker.validate(cstic, errorObj);
	}

	protected void validateAdditionalValueNumeric(final CsticData cstic, final Errors errorObj)
	{
		numericChecker.validateAdditionalValue(cstic, errorObj);
	}


	protected boolean isEmptyOrNull(final String value)
	{
		return null == value || value.isEmpty();
	}

	/**
	 * @param numericChecker
	 *           the numericChecker to set
	 */
	public void setNumericChecker(final NumericChecker numericChecker)
	{
		this.numericChecker = numericChecker;
	}

	/**
	 * @param mergeProcessor
	 *           the mergeProcessor to set
	 */
	public void setMergeProcessor(final ConfigDataMergeProcessor mergeProcessor)
	{
		this.mergeProcessor = mergeProcessor;
	}

}
