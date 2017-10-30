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
package de.hybris.platform.sap.productconfig.frontend.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.impl.ConfigurationCartIntegrationFacadeImpl;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@UnitTest
public class AddConfigToCartControllerTest extends AbstractProductConfigControllerTest
{
	@Mock
	private ConfigurationCartIntegrationFacadeImpl configCartIntegrationFacade;
	@Mock
	private RedirectAttributes redirectAttributes;

	@Mock
	protected BindingResult bindingResults;

	@InjectMocks
	private final AddConfigToCartController addConfigToCartController = new AddConfigToCartController();

	@Mock
	private CartEntryModel cartItem;

	String cartItemKey = "123";

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		kbKey = createKbKey();
		csticList = createCsticsList();
		configData = createConfigurationDataWithGeneralGroupOnly();
		given(cartItem.getPk()).willReturn(PK.parse(cartItemKey));
	}

	@Test
	public void testAddConfigToCartIsCorrect() throws Exception
	{
		initializeFirstCall();
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		given(configCartIntegrationFacade.addConfigurationToCart(configData)).willReturn(cartItemKey);

		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);

		addConfigToCartController.addConfigToCart(kbKey.getProductCode(), configData, bindingResults, model, redirectAttributes);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
		verify(configCartIntegrationFacade, times(1)).addConfigurationToCart(any(ConfigurationData.class));
		verify(redirectAttributes, times(1)).addFlashAttribute("addedToCart", Boolean.TRUE);
	}

	@Test
	public void testAddConfigToCartProblemWithProduct() throws Exception
	{
		initializeFirstCall();
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		given(configCartIntegrationFacade.addConfigurationToCart(configData))
				.willThrow(new CommerceCartModificationException(cartItemKey));

		addConfigToCartController.addConfigToCart(kbKey.getProductCode(), configData, bindingResults, model, redirectAttributes);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
		verify(configCartIntegrationFacade, times(1)).addConfigurationToCart(any(ConfigurationData.class));

		verify(model, times(1)).addAttribute(eq(GlobalMessages.ERROR_MESSAGES_HOLDER), any(Collection.class));

	}

	@Test
	public void testAddConfigToCartFailed() throws Exception
	{
		initializeFirstCall();

		final List<FieldError> fieldErrors = new ArrayList<>();
		final FieldError error = new FieldError("config", "cstic[0].value", "a", true, null, null, null);
		fieldErrors.add(error);

		given(configFacade.getConfiguration(configData)).willReturn(configData);
		given(Integer.valueOf(bindingResults.getErrorCount())).willReturn(Integer.valueOf(1));
		given(bindingResults.getFieldErrors()).willReturn(fieldErrors);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.TRUE);

		final String targetUrl = addConfigToCartController.addConfigToCart(kbKey.getProductCode(), configData, bindingResults,
				model, redirectAttributes);
		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
		verify(configCartIntegrationFacade, times(0)).addConfigurationToCart(any(ConfigurationData.class));
		verify(redirectAttributes, times(0)).addFlashAttribute("addedToCart", Boolean.TRUE);

		final String expectedTargetUrl = "redirect:/" + kbKey.getProductCode() + "/configOverview";
		assertEquals(expectedTargetUrl, targetUrl);
	}

	@Test
	public void testAddConfigToCartTwice() throws Exception
	{
		initializeFirstCall();
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		given(configCartIntegrationFacade.addConfigurationToCart(configData)).willReturn(cartItemKey);

		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);

		addConfigToCartController.addConfigToCart(kbKey.getProductCode(), configData, bindingResults, model, redirectAttributes);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
		verify(configCartIntegrationFacade, times(1)).addConfigurationToCart(any(ConfigurationData.class));
		verify(redirectAttributes, times(1)).addFlashAttribute("addedToCart", Boolean.TRUE);

		verify(sessionAccessFacade, times(1)).setCartEntryForProduct(anyObject(), anyObject());

		configData.setCartItemPK("4711");

		addConfigToCartController.addConfigToCart(kbKey.getProductCode(), configData, bindingResults, model, redirectAttributes);

		verify(redirectAttributes, times(1)).addFlashAttribute("addedToCart", Boolean.FALSE);
	}

	@Test
	public void testReset() throws Exception
	{
		initializeFirstCall();

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setConfigId(CONFIG_ID);
		given(sessionAccessFacade.getUiStatusForProduct(kbKey.getProductCode())).willReturn(uiStatus);

		final String targetUrl = addConfigToCartController.resetConfiguration(kbKey.getProductCode(), "/");
		verify(sessionAccessFacade, times(1)).removeUiStatusForProduct(kbKey.getProductCode());

		assertEquals("redirect:/", targetUrl);
	}

	@Test
	public void testCopy() throws Exception
	{
		initializeFirstCall();
		final UiStatus uiStatus = new UiStatus();
		uiStatus.setConfigId(CONFIG_ID);
		given(sessionAccessFacade.getUiStatusForProduct(kbKey.getProductCode())).willReturn(uiStatus);
		given(configCartIntegrationFacade.copyConfiguration(CONFIG_ID)).willReturn(CONFIG_ID);

		final String targetUrl = addConfigToCartController.copyConfiguration(kbKey.getProductCode(), "/");
		verify(sessionAccessFacade, times(0)).removeUiStatusForProduct(kbKey.getProductCode());

		assertEquals("redirect:/", targetUrl);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initializeFirstCall() throws Exception
	{
		given(productData.getCode()).willReturn(PRODUCT_CODE);
		given(productModel.getCode()).willReturn(PRODUCT_CODE);

		given(sessionService.getCurrentSession()).willReturn(hybriSession);
		given(hybriSession.getSessionId()).willReturn("1");

		given(configFacade.getConfiguration(any(KBKeyData.class))).willReturn(configData);
		given(productService.getProductForCode(PRODUCT_CODE)).willReturn(productModel);
		given(productFacade.getProductForOptions(any(ProductModel.class), any(Collection.class))).willReturn(productData);

		given(storeSessionFacade.getCurrentCurrency()).willReturn(createCurrencyData());
		given(cmsPageService.getPageForProduct(any(ProductModel.class))).willReturn(new ProductPageModel());
		given(pageTitleResolver.resolveProductPageTitle(any(ProductModel.class))).willReturn("TEST");
	}
}
