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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.sap.productconfig.facades.CPQImageType;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class UiTypeFinderImplTest
{
	private final UiTypeFinderImpl uiTypeFinder = new UiTypeFinderImpl();


	private CsticModel csticModel;





	@Before
	public void setup()
	{
		csticModel = new CsticModelImpl();
	}

	protected CsticModel createSimpleInput()
	{
		final CsticModel csticModel = new CsticModelImpl();
		csticModel.setAllowsAdditionalValues(false);
		csticModel.setAssignableValues(createAssignableValueList(0));
		csticModel.setEntryFieldMask(null);
		csticModel.setMultivalued(false);
		csticModel.setReadonly(false);
		csticModel.setValueType(CsticModel.TYPE_STRING);
		return csticModel;
	}

	protected CsticModel createNumericInput(final int valueType)
	{
		final CsticModel csticModel = new CsticModelImpl();
		csticModel.setAllowsAdditionalValues(false);
		csticModel.setAssignableValues(createAssignableValueList(0));
		csticModel.setEntryFieldMask(null);
		csticModel.setMultivalued(false);
		csticModel.setReadonly(false);
		csticModel.setValueType(valueType);
		return csticModel;
	}

	protected CsticModel createSelection(final int valueType, final int numOptions, final boolean isMultivalued)
	{
		final CsticModel csticModel = new CsticModelImpl();
		csticModel.setAllowsAdditionalValues(false);
		csticModel.setAssignableValues(createAssignableValueList(numOptions));
		csticModel.setEntryFieldMask(null);
		csticModel.setMultivalued(isMultivalued);
		csticModel.setReadonly(false);
		csticModel.setValueType(valueType);
		csticModel.setStaticDomainLength(csticModel.getAssignableValues().size());
		return csticModel;
	}

	private List<CsticValueModel> createAssignableValueList(final int size)
	{
		final List<CsticValueModel> values = new ArrayList<>(size);
		for (int ii = 0; ii < size; ii++)
		{
			final CsticValueModel value = new CsticValueModelImpl();
			values.add(value);
		}
		return values;
	}

	private List<CsticValueData> createValueDTOList(final int size)
	{
		final List<CsticValueData> values = new ArrayList<>(size);
		for (int ii = 0; ii < size; ii++)
		{
			final CsticValueData value = new CsticValueData();
			values.add(value);
		}
		return values;
	}


	@Test
	public void givenFloatThenUiTypeNumeric() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_FLOAT);
		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NUMERIC, actual);
		assertEquals("Wrong UI  type", UiValidationType.NUMERIC, actualValidationType);
	}

	@Test
	public void givenFloatReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_FLOAT);
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.READ_ONLY, actual);
		assertEquals("Wrong UI  type", UiValidationType.NONE, actualValidationType);
	}

	@Test
	public void givenIntegerThenUiTypeNumeric() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_INTEGER);
		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NUMERIC, actual);
	}

	@Test
	public void givenIntegerReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_INTEGER);
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.READ_ONLY, actual);
	}


	@Test
	public void givenStringThenUiTypeString() throws Exception
	{
		csticModel = createSimpleInput();

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.STRING, actual);
		assertEquals("Wrong UI  type", UiValidationType.NONE, actualValidationType);

	}

	@Test
	public void givenStringReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI type", UiType.READ_ONLY, actual);
	}

	@Test
	public void givenStringAndMultiValueThenUiTypeCheckbox() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 1, true);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.CHECK_BOX, actual);
	}

	@Test
	public void givenStringAndMultiValueReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 1, true);
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.READ_ONLY, actual);
	}

	@Test
	public void givenStringAnd4ValuesThenUiTypeRadio() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 4, false);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.RADIO_BUTTON, actual);
	}

	@Test
	public void givenFloatAnd4ValuesThenUiTypeRadio() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_FLOAT, 4, false);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.RADIO_BUTTON, actual);
	}

	@Test
	public void givenFloatAnd4ValuesReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_FLOAT, 4, false);
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.READ_ONLY, actual);
	}

	@Test
	public void givenStringAnd5ValuesThenUiTypeDDLB() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 5, false);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.DROPDOWN, actual);
	}

	@Test
	public void givenStringAnd5ValuesReadOnlyThenUiTypeReadOnly() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 5, false);
		csticModel.setReadonly(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.READ_ONLY, actual);
	}

	@Test
	public void givenIntAnd5ValuesThenUiTypeDDLB() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_INTEGER, 5, false);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.DROPDOWN, actual);
	}


	@Test
	public void givenStringAndMultivaluedDomainThenUiCheckboxList() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 2, true);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.CHECK_BOX_LIST, actual);
	}

	@Test
	public void givenFloatAndMultivaluedDomainThenUiCheckboxList() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_FLOAT, 2, true);
		csticModel.setConstrained(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.CHECK_BOX_LIST, actual);
	}

	@Test
	public void givenUndefinedThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setValueType(CsticModel.TYPE_UNDEFINED);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenDateThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setValueType(CsticModel.TYPE_DATE);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenCurrencyThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setValueType(CsticModel.TYPE_CURRENCY);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenClassThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setValueType(CsticModel.TYPE_CLASS);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenBooleanThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setValueType(CsticModel.TYPE_BOOLEAN);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
		assertEquals("Wrong UI  type", UiValidationType.NONE, actualValidationType);
	}

	@Test
	public void givenSingleValueAllowsAdditionalValueStringThenDropDownAdditionalInputString() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 6, false);
		csticModel.setAllowsAdditionalValues(true);


		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.DROPDOWN_ADDITIONAL_INPUT, actual);
		assertEquals("Wrong UI  type", UiValidationType.NONE, actualValidationType);
	}

	@Test
	public void givenSingleValueAllowsAdditionalValueNumericThenDropDownAdditionalInputNumeric() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_FLOAT, 6, false);
		csticModel.setAllowsAdditionalValues(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.DROPDOWN_ADDITIONAL_INPUT, actual);
		assertEquals("Wrong UI  type", UiValidationType.NUMERIC, actualValidationType);
	}

	@Test
	public void givenSingleValueAllowsAdditionalValueStringThenRadioAdditionalInputString() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 3, false);
		csticModel.setAllowsAdditionalValues(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.RADIO_BUTTON_ADDITIONAL_INPUT, actual);
		assertEquals("Wrong UI  type", UiValidationType.NONE, actualValidationType);
	}

	@Test
	public void givenSingleValueAllowsAdditionalValueNumericThenRadioAdditionalInputNumeric() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_FLOAT, 3, false);
		csticModel.setAllowsAdditionalValues(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		final UiValidationType actualValidationType = uiTypeFinder.findUiValidationTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.RADIO_BUTTON_ADDITIONAL_INPUT, actual);
		assertEquals("Wrong UI  type", UiValidationType.NUMERIC, actualValidationType);
	}

	@Test
	public void givenSingleSelectionIntervalThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_INTEGER, 0, false);
		csticModel.setIntervalInDomain(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NUMERIC, actual);
	}

	@Test
	public void givenMultiValuedIntervalThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_INTEGER, 4, true);
		csticModel.setIntervalInDomain(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenScientificThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_FLOAT);
		csticModel.setEntryFieldMask("_,____.__EE");

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenMultivaluedStringThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setMultivalued(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenStringWithTemplateThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createSimpleInput();
		csticModel.setEntryFieldMask("abcd-efg");

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}

	@Test
	public void givenIntegerWithIntervalWithoutAddValThenUiTypeNumeric() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_INTEGER);
		csticModel.setIntervalInDomain(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NUMERIC, actual);
	}

	@Test
	public void givenIntegerWithIntervalWithAddValThenUiTypeNotImplemented() throws Exception
	{
		csticModel = createNumericInput(CsticModel.TYPE_INTEGER);
		csticModel.setIntervalInDomain(true);
		csticModel.setAllowsAdditionalValues(true);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel);
		assertEquals("Wrong UI  type", UiType.NOT_IMPLEMENTED, actual);
	}


	@Test
	public void testHasValueImages() throws Exception
	{

		final CsticValueData value = new CsticValueData();

		// Test media null
		assertFalse(uiTypeFinder.hasValueImage(value));

		// test empty media-list
		final List<ImageData> images = new ArrayList<>();
		value.setMedia(images);

		assertFalse(uiTypeFinder.hasValueImage(value));

		// Second image is value image
		final ImageData image = new ImageData();
		images.add(image);
		final ImageData image2 = new ImageData();
		image2.setFormat(CPQImageType.VALUE_IMAGE.toString());
		images.add(image2);

		assertTrue(uiTypeFinder.hasValueImage(value));

	}


	@Test
	public void testHasCsticValueImages() throws Exception
	{
		final CsticData data = new CsticData();
		final List<CsticValueData> domainValues = new ArrayList();
		data.setDomainvalues(domainValues);

		//	Test:  no domain values
		assertFalse(uiTypeFinder.hasCsticValueImages(data));

		final CsticValueData value = new CsticValueData();
		domainValues.add(value);
		final CsticValueData value2 = new CsticValueData();
		domainValues.add(value2);
		final CsticValueData value3 = new CsticValueData();
		domainValues.add(value3);

		final List<ImageData> images = new ArrayList<>();
		value.setMedia(images);
		value2.setMedia(images);
		value3.setMedia(images);

		// test: empty media-lists
		assertFalse(uiTypeFinder.hasCsticValueImages(data));

		final List<ImageData> images2 = new ArrayList<>();
		final ImageData image = new ImageData();
		images2.add(image);
		final ImageData image2 = new ImageData();
		image2.setFormat(CPQImageType.VALUE_IMAGE.toString());
		images2.add(image2);
		images2.add(image);
		value2.setMedia(images2);

		// test second value has image
		assertTrue(uiTypeFinder.hasCsticValueImages(data));
	}

	@Test
	public void givenSingleSelectionImage() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 5, false);
		final CsticData data = createCsticDataWithOneValueImage(5, 2);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel, data);
		assertEquals("Wrong UI type", UiType.SINGLE_SELECTION_IMAGE, actual);
	}

	@Test
	public void givenSingleSelectionImageReadOnly() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 7, false);
		csticModel.setReadonly(true);
		final CsticData data = createCsticDataWithOneValueImage(7, 1);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel, data);
		assertEquals("Wrong UI type", UiType.READ_ONLY_SINGLE_SELECTION_IMAGE, actual);
	}

	@Test
	public void givenMultiSelectionImage() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 7, true);
		final CsticData data = createCsticDataWithOneValueImage(7, 0);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel, data);
		assertEquals("Wrong UI type", UiType.MULTI_SELECTION_IMAGE, actual);
	}

	@Test
	public void givenMultiSelectionImageReadOnly() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 5, true);
		csticModel.setReadonly(true);
		final CsticData data = createCsticDataWithOneValueImage(5, 4);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel, data);
		assertEquals("Wrong UI type", UiType.READ_ONLY_MULTI_SELECTION_IMAGE, actual);
	}

	@Test
	public void givenMultiSelectionImageOriginallySingleCheckbox() throws Exception
	{
		csticModel = createSelection(CsticModel.TYPE_STRING, 1, true);
		csticModel.setConstrained(true);
		final CsticData data = createCsticDataWithOneValueImage(1, 0);

		final UiType actual = uiTypeFinder.findUiTypeForCstic(csticModel, data);
		assertEquals("Wrong UI type", UiType.MULTI_SELECTION_IMAGE, actual);
	}

	protected CsticData createCsticDataWithOneValueImage(final int valueNumber, final int imageValuePosition)
	{
		final CsticData data = new CsticData();
		final List<CsticValueData> domainValues = createValueDTOList(valueNumber);
		data.setDomainvalues(domainValues);

		final List<ImageData> images = new ArrayList<>();
		final ImageData image = new ImageData();
		image.setFormat(CPQImageType.VALUE_IMAGE.toString());
		images.add(image);
		domainValues.get(imageValuePosition).setMedia(images);
		return data;
	}

}
