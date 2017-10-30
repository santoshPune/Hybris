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
 */

package de.hybris.platform.hmc;

import de.hybris.bootstrap.annotations.ManualTest;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


@ManualTest
public class HMCHelperTest
{

	@Test
	public void encodeHTMLPreservingIndentationShouldNotEncodeSpaces()
	{
		final String menuLabel = "&amp;nbsp;&amp;nbsp;Atomic Type";

		final String encodedLabel = HMCHelper.xssEncodeHTMLPreservingIndentation(menuLabel);
		assertThat(encodedLabel).isEqualTo("&amp;nbsp;&amp;nbsp;Atomic&#x20;Type");
	}

	@Test
	public void encodeHTMLPreservingIndentationShouldEncodeHtmlButNoSpaces()
	{
		final String menuLabel = "&amp;nbsp;&amp;nbsp;Atomic Type&lt;img src=a onerror=&quot;alert(1)”&gt;";
		final String encodedLabel = HMCHelper.xssEncodeHTMLPreservingIndentation(menuLabel);
		assertThat(encodedLabel)
				.isEqualTo(
						"&amp;nbsp;&amp;nbsp;Atomic&#x20;Type&amp;lt&#x3b;img&#x20;src&#x3d;a&#x20;onerror&#x3d;&amp;quot&#x3b;alert&#x28;1&#x29;&#x201d;&amp;gt&#x3b;");
	}
}
