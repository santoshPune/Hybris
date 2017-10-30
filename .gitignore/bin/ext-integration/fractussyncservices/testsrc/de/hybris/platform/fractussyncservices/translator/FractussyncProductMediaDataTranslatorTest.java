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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The class of ProductMediaDataTranslatorTest.
 */
@UnitTest
public class FractussyncProductMediaDataTranslatorTest
{

	private FractussyncProductMediaDataTranslator translator;

	@Mock
	private SpecialColumnDescriptor descriptor;

	@Mock
	private HeaderDescriptor headerDescriptor;

	@Mock
	private ImpExReader impExReader;

	@Mock
	private EnumerationValue enumerationValue;

	@Mock
	private Item item;

	@Mock
	private Product product;

	@Mock
	private Media media;

	@Before
	public void setup()
	{
		translator = new FractussyncProductMediaDataTranslator();

		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = ImpExException.class)
	public void shouldNotPerformImport() throws ImpExException
	{
		translator.performImport("anyCellValue", item);
	}

	@Test
	public void shouldPerformImportWithExportOnlyModeAndHasMediaData() throws ImpExException, JaloSecurityException
	{

		final String url = "testUrlString";
		final String expectedUrl = "http://localhost:9001/testUrlString";

		translator.setBaseURL("http://localhost:9001/");

		Mockito.when(descriptor.getHeader()).thenReturn(headerDescriptor);
		Mockito.when(headerDescriptor.getReader()).thenReturn(impExReader);
		Mockito.when(impExReader.getValidationMode()).thenReturn(enumerationValue);
		Mockito.when(enumerationValue.getCode()).thenReturn(ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY);
		Mockito.when(product.getAttribute("picture")).thenReturn(media);
		Mockito.when(media.hasData()).thenReturn(true);
		Mockito.when(media.getURL()).thenReturn(url);

		translator.init(descriptor);
		translator.validate(StringUtils.EMPTY);
		final String s = translator.performExport(product);

		Assert.assertEquals(expectedUrl, s);
	}

	@Test
	public void shouldPerformImportWithExportOnlyModeAndNoMediaData() throws ImpExException, JaloSecurityException
	{

		final String expectedUrl = "<empty>";

		translator.setBaseURL("http://localhost:9001/");

		Mockito.when(descriptor.getHeader()).thenReturn(headerDescriptor);
		Mockito.when(headerDescriptor.getReader()).thenReturn(impExReader);
		Mockito.when(impExReader.getValidationMode()).thenReturn(enumerationValue);
		Mockito.when(enumerationValue.getCode()).thenReturn(ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY);
		Mockito.when(product.getAttribute("picture")).thenReturn(media);
		Mockito.when(media.hasData()).thenReturn(false);

		translator.init(descriptor);
		translator.validate(StringUtils.EMPTY);
		final String s = translator.performExport(product);

		Assert.assertEquals(expectedUrl, s);
	}

	@Test
	public void shouldPerformImportWithExportOnlyModeAndNoMediaDataButWithURLReference()
			throws ImpExException, JaloSecurityException
	{

		final String expectedUrl = "http://test.example.com/image.jpg";

		translator.setBaseURL("http://localhost:9001/");

		Mockito.when(descriptor.getHeader()).thenReturn(headerDescriptor);
		Mockito.when(headerDescriptor.getReader()).thenReturn(impExReader);
		Mockito.when(impExReader.getValidationMode()).thenReturn(enumerationValue);
		Mockito.when(enumerationValue.getCode()).thenReturn(ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY);
		Mockito.when(product.getAttribute("picture")).thenReturn(media);
		Mockito.when(media.hasData()).thenReturn(false);
		Mockito.when(media.getURL()).thenReturn(expectedUrl);

		translator.init(descriptor);
		translator.validate(StringUtils.EMPTY);
		final String s = translator.performExport(product);

		Assert.assertEquals(expectedUrl, s);
	}

	@Test
	public void testPerformExportForProductWithoutMedia() throws JaloSecurityException, ImpExException
	{
		translator.setBaseURL("http://localhost:9001/");
		final String expectedUrl = "<empty>";
		Mockito.when(descriptor.getHeader()).thenReturn(headerDescriptor);
		Mockito.when(headerDescriptor.getReader()).thenReturn(impExReader);
		Mockito.when(impExReader.getValidationMode()).thenReturn(enumerationValue);
		Mockito.when(enumerationValue.getCode()).thenReturn(ImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY);
		Mockito.when(product.getAttribute("picture")).thenReturn(null);
		translator.init(descriptor);
		translator.validate(StringUtils.EMPTY);
		final String s = translator.performExport(product);
		Assert.assertEquals(expectedUrl, s);
	}

}
