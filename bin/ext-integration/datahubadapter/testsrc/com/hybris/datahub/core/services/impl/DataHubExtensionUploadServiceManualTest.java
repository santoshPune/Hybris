package com.hybris.datahub.core.services.impl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.hybris.datahub.client.extension.ExtensionSource;
import com.hybris.datahub.client.extension.ExtensionStringSource;


@SuppressWarnings("javadoc")
@Ignore("Comment out to run manually when DataHub server is running. Verify URL in the setUp() method.")
public class DataHubExtensionUploadServiceManualTest
{
	private static final String XML = String
			.join(System.lineSeparator(),
					"<extension xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.hybris.com/schema/\"",
					"       xsi:schemaLocation=\"http://www.hybris.com/schema/ http://www.hybris.com/schema/datahub-metadata-schema-1.2.0.xsd\"",
					"       name=\"pcm-apparel-canonical\">",
					"	<canonicalItems>",
					"		<item>",
               "			<type>DemoItem</type>",
					"			<attributes>",
					"				<attribute>",
					"					<name>validAttr</name>",
					"					<model>",
					"						<type>String</type>",
					"						<primaryKey>true</primaryKey>",
					"					</model>",
					"				</attribute>",
					"			</attributes>",
					"		</item>",
					"	</canonicalItems>",
					"</extension>");

	private DefaultDataHubExtensionUploadService service;

	@Before
	public void setUp()
	{
		service = new DefaultDataHubExtensionUploadService();
		service.setDataHubUrl("http://localhost:9797/datahub-webapp/v1");
	}

	@Test
	public void testExtensionUploadToDataHub() throws IOException
	{
		final ExtensionSource extension = new ExtensionStringSource(XML);
		service.uploadExtension(extension);
	}
}
