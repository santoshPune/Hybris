/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineseprofilefacades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.chineseprofileservices.process.email.context.ChineseAbstractEmailContext;
import de.hybris.platform.commercefacades.order.data.DeliveryOrderEntryGroupData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryGroupData;
import de.hybris.platform.commercefacades.order.data.PickupOrderEntryGroupData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends ChineseAbstractEmailContext<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(OrderNotificationEmailContext.class);
	private Converter<OrderModel, OrderData> orderConverter;
	private OrderData orderData;
	private String paymentMode = "";
	private BaseSiteService baseSiteService;
	private CommerceCategoryService commerceCategoryService;
	private Locale emailLocale;

	public String getPaymentMode()
	{
		return paymentMode;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		orderData = getOrderConverter().convert(orderProcessModel.getOrder());
		OrderModel orderModel = orderProcessModel.getOrder();
		String isoCode = getEmailLanguage().getIsocode();
		emailLocale = new Locale(isoCode);
		updateBaseUrl(orderModel, isoCode);
		updateDevliverInfo(orderProcessModel);
		updateProductUrlAndName(orderModel);
		updateDeliveryAddress(orderModel);


		if (orderData.getChinesePaymentInfo() != null)
		{
			paymentMode = orderData.getChinesePaymentInfo().getPaymentProvider();
		}
	}

	protected void updateDeliveryAddress(OrderModel orderModel)
	{
		if (orderModel.getDeliveryMode() instanceof PickUpDeliveryModeModel)
		{
			return;
		}

		AddressModel addressModel = orderModel.getDeliveryAddress();
		AddressData addressData = orderData.getDeliveryAddress();

		String countryName = addressModel.getCountry().getName(emailLocale);
		addressData.getCountry().setName(countryName);

		RegionModel regionModel = addressModel.getRegion();
		RegionData regionData = addressData.getRegion();
		CityModel cityModel = addressModel.getCity();
		CityData cityData = addressData.getCity();
		DistrictModel districtModel = addressModel.getCityDistrict();
		DistrictData districtData = addressData.getDistrict();

		if (null != regionModel && null != regionData)
		{
			regionData.setName(regionModel.getName(emailLocale));
		}

		if (null != cityModel && null != cityData)
		{
			cityData.setName(cityModel.getName(emailLocale));
		}

		if (null != districtModel && null != districtData)
		{
			districtData.setName(districtModel.getName(emailLocale));
		}
	}

	protected void updateProductUrlAndName(OrderModel orderModel)
	{
		List<DeliveryOrderEntryGroupData> deliveryGroupDataList = orderData.getDeliveryOrderGroups();
		List<PickupOrderEntryGroupData> pickUpGroupDataList = orderData.getPickupOrderGroups();

		updateGroupData(orderModel, deliveryGroupDataList);
		updateGroupData(orderModel, pickUpGroupDataList);

	}

	protected void updateGroupData(OrderModel orderModel, List<? extends OrderEntryGroupData> groupDataList)
	{
		if (null == groupDataList)
		{
			return;
		}

		groupDataList.stream().forEach(groupData -> groupData.getEntries().stream().forEach(orderEntryData -> {
			String productDataCode = orderEntryData.getProduct().getCode();
			ProductModel productModel = orderModel.getEntries().stream()
					.filter(orderEntryModel -> orderEntryModel.getProduct().getCode().equals(productDataCode))
					.collect(Collectors.toList()).get(0).getProduct();
			String url = resolveInternal(productModel);
			String productName = productModel.getName(emailLocale);
			orderEntryData.getProduct().setUrl(url);
			orderEntryData.getProduct().setName(productName);
		}));
	}

	protected void updateDevliverInfo(final OrderProcessModel orderProcessModel)
	{
		String description = orderProcessModel.getOrder().getDeliveryMode().getDescription(emailLocale);
		String develiveryName = orderProcessModel.getOrder().getDeliveryMode().getName(emailLocale);
		orderData.getDeliveryMode().setDescription(description);
		orderData.getDeliveryMode().setName(develiveryName);
	}

	protected void updateBaseUrl(OrderModel orderModel, String isoCode)
	{
		String baseUrl = (String) get(BASE_URL);
		String baseSecrueUrl = (String) get(SECURE_BASE_URL);
		put(BASE_URL, baseUrl.replaceAll("/" + orderModel.getLanguage().getIsocode() + "$", "/" + isoCode));
		put(SECURE_BASE_URL, baseSecrueUrl.replaceAll("/" + orderModel.getLanguage().getIsocode() + "$", "/" + isoCode));
	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

	protected String resolveInternal(final ProductModel source)
	{

		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		String url = "/{category-path}/{product-name}/p/{product-code}";

		if (currentBaseSite != null && url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", currentBaseSite.getUid());
		}
		if (url.contains("{category-path}"))
		{
			url = url.replace("{category-path}", buildPathString(getCategoryPath(source)));
		}
		if (url.contains("{product-name}"))
		{
			url = url.replace("{product-name}", urlSafe(source.getName(emailLocale)));
		}
		if (url.contains("{product-code}"))
		{
			url = url.replace("{product-code}", source.getCode());
		}

		return url;
	}

	protected List<CategoryModel> getCategoryPath(final ProductModel product)
	{
		final CategoryModel category = getPrimaryCategoryForProduct(product);
		if (category != null)
		{
			return getCategoryPath(category);
		}
		return Collections.emptyList();
	}

	protected CategoryModel getPrimaryCategoryForProduct(final ProductModel product)
	{
		// Get the first super-category from the product that isn't a classification category
		for (final CategoryModel category : product.getSupercategories())
		{
			if (!(category instanceof ClassificationClassModel))
			{
				return category;
			}
		}
		return null;
	}

	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> paths = getCommerceCategoryService().getPathsForCategory(category);
		// Return first - there will always be at least 1
		return paths.iterator().next();
	}

	protected String buildPathString(final List<CategoryModel> path)
	{
		if (path == null || path.isEmpty())
		{
			return "c"; // Default category part of path when missing category
		}

		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); i++)
		{
			if (i != 0)
			{
				result.append('/');
			}
			result.append(urlSafe(path.get(i).getName(emailLocale)));
		}

		return result.toString();
	}

	protected String urlSafe(final String text)
	{
		if (text == null || text.isEmpty())
		{
			return "";
		}

		String encodedText;
		try
		{
			encodedText = URLEncoder.encode(text, "utf-8");
		}
		catch (final UnsupportedEncodingException encodingException)
		{
			encodedText = text;
			LOG.debug(encodingException.getMessage());
		}

		// Cleanup the text
		String cleanedText = encodedText;
		cleanedText = cleanedText.replaceAll("%2F", "/");
		cleanedText = cleanedText.replaceAll("[^%A-Za-z0-9\\-]+", "-");
		return cleanedText;
	}
}
