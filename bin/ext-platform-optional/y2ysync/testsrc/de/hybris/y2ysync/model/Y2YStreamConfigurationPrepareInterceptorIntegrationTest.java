/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.y2ysync.model;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.services.SyncConfigService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class Y2YStreamConfigurationPrepareInterceptorIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private SyncConfigService syncConfigService;
	private Y2YStreamConfigurationContainerModel testContainer;

	@Before
	public void setUp() throws Exception
	{
		testContainer = syncConfigService.createStreamConfigurationContainer("testContainer");
	}

	@Test
	public void shouldGenerateDataHubTypeWithNamespacePrefix()
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = syncConfigService.createStreamConfiguration(testContainer, typeCode,
				Collections.emptySet());
		modelService.save(streamConfig);

		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getDataHubType()).isEqualTo("testContainer_Product");
	}

	@Test
	public void shouldNotGenerateDataHubTypeForSavedObject()
	{
		// given
		final Y2YStreamConfigurationModel streamConfig = syncConfigService.createStreamConfiguration(testContainer, "Product",
				Collections.emptySet());
		modelService.save(streamConfig);

		// when
		streamConfig.setDataHubType("");
		modelService.save(streamConfig);

		// then
		assertThat(streamConfig.getDataHubType()).isEqualTo("");
	}

	@Test
	public void shouldSkipDataHubTypeGenerationIfSet()
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = syncConfigService.createStreamConfiguration(testContainer, typeCode,
				Collections.emptySet());
		streamConfig.setDataHubType("explicitType");
		modelService.save(streamConfig);

		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getDataHubType()).isEqualTo("explicitType");
	}

	@Test
	public void shouldSkipGenerationOfWhereClauseWhenAutoGenerateWhereClauseOptionIsSetToFalse() throws Exception
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfigNoAutoGenerationOfWhereClause(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getWhereClause()).isNull();
	}

	@Test
	public void shouldSkipGenerationOfInfoExpressionWhenAutoGenerateInfoExpressionOptionIsSetToFalse() throws Exception
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfigNoAutoGenerationOfInfoExpression(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getInfoExpression()).isNull();
	}

	@Test
	public void shouldSkipGenerationOfWhereClauseForCompletelyCatalogVersionUnawareModel() throws Exception
	{
		// given
		final String typeCode = "Title";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfig(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getWhereClause()).isNull();
	}

	@Test
	public void shouldAutoGenerateWhereClauseForCatalogVersionAwareModel() throws Exception
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfig(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getWhereClause()).isEqualTo("{catalogVersion}=?catalogVersion");
	}

	@Test
	public void shouldAutoGenerateInfoExpressionForCatalogVersionAwareModel() throws Exception
	{
		// given
		final String typeCode = "Product";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfig(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getInfoExpression())
				.isEqualTo("#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}");
	}

	@Test
	public void shouldAutoGenerateWhereClauseForCatalogVersionUnawareModelUsingPartOfRelation() throws Exception
	{
		// given
		final String typeCode = "ProductReference";

		// when
		final Y2YStreamConfigurationModel streamConfig = createStreamConfig(typeCode);

		// then
		assertThat(streamConfig).isNotNull();
		assertThat(streamConfig.getWhereClause())
				.isEqualTo("{item.source} IN ({{ SELECT {p.PK} FROM {Product AS p} WHERE {p.catalogVersion}=?catalogVersion }})");
	}

	private Y2YStreamConfigurationModel createStreamConfig(final String typeCode)
	{
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(testContainer, typeCode,
				Collections.emptySet());
		modelService.save(configuration);
		return configuration;
	}

	private Y2YStreamConfigurationModel createStreamConfigNoAutoGenerationOfWhereClause(final String typeCode)
	{
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(testContainer, typeCode,
				Collections.emptySet());
		configuration.setAutoGenerateWhereClause(Boolean.FALSE);
		modelService.save(configuration);
		return configuration;
	}

	private Y2YStreamConfigurationModel createStreamConfigNoAutoGenerationOfInfoExpression(final String typeCode)
	{
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(testContainer, typeCode,
				Collections.emptySet());
		configuration.setAutoGenerateInfoExpression(Boolean.FALSE);
		modelService.save(configuration);
		return configuration;
	}
}
