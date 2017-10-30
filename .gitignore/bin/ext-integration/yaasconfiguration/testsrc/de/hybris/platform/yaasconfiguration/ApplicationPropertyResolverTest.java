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
package de.hybris.platform.yaasconfiguration;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.Optional;

import org.junit.Test;


@UnitTest
public class ApplicationPropertyResolverTest
{

	@Test
	public void noAppId()
	{
		final YaasConfigurationService yaasConfig = mock(YaasConfigurationService.class);
		final ApplicationPropertyResolver apr = new ApplicationPropertyResolver(yaasConfig);
		final YaasApplicationModel model = mock(YaasApplicationModel.class);
		doReturn("clientId").when(model).getClientId();
		doReturn("clientSecret").when(model).getClientSecret();
		doReturn(Optional.of(model)).when(yaasConfig).takeFirstModel();
		assertThat(apr.contains("oauth.clientId")).isTrue();
		assertThat(apr.contains("oauth.clientSecret")).isTrue();
		assertThat(apr.contains("oauth.clientId-XXX")).isFalse();
		assertThat(apr.contains("oauth.clientSecret-XXX")).isFalse();
		assertThat(apr.lookup("oauth.clientId")).isEqualTo("clientId");
		assertThat(apr.lookup("oauth.clientSecret")).isEqualTo("clientSecret");
	}

	@Test
	public void appId()
	{
		final YaasConfigurationService yaasConfig = mock(YaasConfigurationService.class);
		final ApplicationPropertyResolver apr = new ApplicationPropertyResolver(yaasConfig, "appId");
		final YaasApplicationModel model = mock(YaasApplicationModel.class);
		doReturn("clientId").when(model).getClientId();
		doReturn("clientSecret").when(model).getClientSecret();
		when(yaasConfig.getYaasApplicationForId("appId")).thenReturn(model);
		assertThat(apr.contains("oauth.clientId")).isTrue();
		verify(yaasConfig).getYaasApplicationForId(eq("appId"));
		assertThat(apr.contains("oauth.clientSecret")).isTrue();
		assertThat(apr.contains("oauth.clientId-XXX")).isFalse();
		assertThat(apr.contains("oauth.clientSecret-XXX")).isFalse();
		assertThat(apr.lookup("oauth.clientId")).isEqualTo("clientId");
		assertThat(apr.lookup("oauth.clientSecret")).isEqualTo("clientSecret");
	}
}
