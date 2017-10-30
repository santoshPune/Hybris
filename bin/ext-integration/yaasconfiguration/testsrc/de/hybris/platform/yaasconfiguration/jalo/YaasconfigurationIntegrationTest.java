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
package de.hybris.platform.yaasconfiguration.jalo;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * JUnit Tests for the Yaasconfiguration extension
 */
@IntegrationTest
public class YaasconfigurationIntegrationTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private YaasConfigurationService yaasConfigurationService;
	@Resource
	private FlexibleSearchService flexibleSearchService;


	@Before
	public void setUp() throws Exception
	{
		flexibleSearchService.search(String.format("select {pk} from {%s}", YaasApplicationModel._TYPECODE)).getResult().stream()
				.forEach(modelService::remove);
	}

	@Test
	public void firstModel()
	{
		final YaasApplicationModel model = modelService.create(YaasApplicationModel.class);
		model.setClientId("clientId");
		model.setClientSecret("clientSecret");
		model.setIdentifier("appId");
		modelService.save(model);
		final Optional<YaasApplicationModel> opt = yaasConfigurationService.takeFirstModel();
		assertThat(opt.isPresent()).isTrue();
		assertThat(opt.get().getClientId()).isEqualTo("clientId");
		assertThat(opt.get().getClientSecret()).isEqualTo("clientSecret");
		assertThat(opt.get().getIdentifier()).isEqualTo("appId");
	}

	@Test
	public void notFound() throws Exception
	{
		final Optional<YaasApplicationModel> opt = yaasConfigurationService.takeFirstModel();
		assertThat(opt.isPresent()).isFalse();
	}

	@Test
	public void multiRecords() throws Exception
	{
		final YaasApplicationModel model = modelService.create(YaasApplicationModel.class);
		model.setClientId("clientId");
		model.setClientSecret("clientSecret");
		model.setIdentifier("appId");
		modelService.save(model);
		final YaasApplicationModel model2 = modelService.create(YaasApplicationModel.class);
		model2.setClientId("clientId2");
		model2.setClientSecret("clientSecret2");
		model2.setIdentifier("appId2");
		modelService.save(model2);

		final Optional<YaasApplicationModel> opt = yaasConfigurationService.takeFirstModel();
		assertThat(opt.isPresent()).isTrue();
		assertThat(opt.get().getClientId()).isEqualTo("clientId");
		assertThat(opt.get().getClientSecret()).isEqualTo("clientSecret");
		assertThat(opt.get().getIdentifier()).isEqualTo("appId");
	}
}
