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
 *
 */
package de.hybris.platform.personalizationcms.strategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.personalizationcms.data.CxCmsActionResult;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxCmsContainerStrategyTest
{

	private static final String DEFAULT_COMPONENT_UID = "defaultComponent";
	private static final String CX_COMPONENT1_UID = "cxComponent1";
	private static final String CX_COMPONENT2_UID = "cxComponent2";
	private static final String CONTAINER1_UID = "container1";
	private static final String CONTAINER2_UID = "container2";

	private final DefaultCxCmsContainerStrategy defaultCxCmsContainerStrategy = new DefaultCxCmsContainerStrategy();

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private CxActionResultService actionResultService;

	@Mock
	private UserService userService;

	private CxCmsComponentContainerModel container1;
	private CxCmsComponentContainerModel container2;
	private CxCmsActionResult result1;
	private CxCmsActionResult result2;

	@Before
	public void initMocks() throws CMSItemNotFoundException
	{
		MockitoAnnotations.initMocks(this);
		defaultCxCmsContainerStrategy.setCmsComponentService(cmsComponentService);
		defaultCxCmsContainerStrategy.setCxActionResultService(actionResultService);
		defaultCxCmsContainerStrategy.setUserService(userService);
		final SimpleCMSComponentModel defaultComponent = new SimpleCMSComponentModel();
		defaultComponent.setUid(DEFAULT_COMPONENT_UID);
		final SimpleCMSComponentModel cxComponent1 = new SimpleCMSComponentModel();
		cxComponent1.setUid(CX_COMPONENT1_UID);
		final SimpleCMSComponentModel cxComponent2 = new SimpleCMSComponentModel();
		cxComponent2.setUid(CX_COMPONENT2_UID);
		container1 = new CxCmsComponentContainerModel();
		container1.setUid(CONTAINER1_UID);
		container1.setDefaultCmsComponent(defaultComponent);
		container2 = new CxCmsComponentContainerModel();
		container2.setUid(CONTAINER2_UID);
		container2.setDefaultCmsComponent(defaultComponent);
		result1 = new CxCmsActionResult();
		result1.setComponentId(CX_COMPONENT1_UID);
		result1.setContainerId(CONTAINER1_UID);
		result2 = new CxCmsActionResult();
		result2.setComponentId(CX_COMPONENT2_UID);
		result2.setContainerId(CONTAINER1_UID);

		BDDMockito.given(cmsComponentService.getSimpleCMSComponent(CX_COMPONENT1_UID)).willReturn(cxComponent1);
		BDDMockito.given(cmsComponentService.getSimpleCMSComponent(CX_COMPONENT2_UID)).willReturn(cxComponent2);
	}

	@Test
	public void shouldReturnDefaultComponentWhenNoActionResults()
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any())).willReturn(Collections.EMPTY_LIST);
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container1);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(DEFAULT_COMPONENT_UID, displayedComponents.get(0).getUid());
	}

	@Test
	public void shouldReturn1CxComponentWhen1ActionResult() throws CMSItemNotFoundException
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any())).willReturn(Arrays.asList(result1));
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container1);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(CX_COMPONENT1_UID, displayedComponents.get(0).getUid());
	}

	@Test
	public void shouldReturn1CxComponentWhen2ActionResult() throws CMSItemNotFoundException
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any()))
				.willReturn(Arrays.asList(result1, result2));
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container1);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(CX_COMPONENT1_UID, displayedComponents.get(0).getUid());
	}

	@Test
	public void shouldReturn1CxComponentWhen2ActionResultOtherOrder() throws CMSItemNotFoundException
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any()))
				.willReturn(Arrays.asList(result2, result1));
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container1);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(CX_COMPONENT2_UID, displayedComponents.get(0).getUid());
	}

	@Test
	public void shouldReturnDefaultComponentWhenNoActionResultForSpecificContainer() throws CMSItemNotFoundException
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any())).willReturn(Arrays.asList(result1));
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container2);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(DEFAULT_COMPONENT_UID, displayedComponents.get(0).getUid());
	}

	@Test
	public void shouldReturnDefaultComponentWhenComponentNotFound() throws CMSItemNotFoundException
	{
		BDDMockito.given(actionResultService.getActionResults(Mockito.any(), Mockito.any())).willReturn(Arrays.asList(result1));
		BDDMockito.given(cmsComponentService.getSimpleCMSComponent(CX_COMPONENT1_UID)).willThrow(CMSItemNotFoundException.class);
		final List<AbstractCMSComponentModel> displayedComponents = defaultCxCmsContainerStrategy
				.getDisplayComponentsForContainer(container1);
		Assert.assertTrue(displayedComponents.size() == 1);
		Assert.assertEquals(DEFAULT_COMPONENT_UID, displayedComponents.get(0).getUid());
	}



}
