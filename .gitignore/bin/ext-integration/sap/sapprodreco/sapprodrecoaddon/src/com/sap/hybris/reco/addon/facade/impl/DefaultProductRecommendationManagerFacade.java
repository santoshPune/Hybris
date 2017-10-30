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
 */
package com.sap.hybris.reco.addon.facade.impl;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.addon.facade.ProductRecommendationManagerFacade;
import com.sap.hybris.reco.bo.ProductRecommendationManagerBO;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.common.util.UserIdProvider;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;


/**
 * @param <REF_TARGET>
 *
 */
public class DefaultProductRecommendationManagerFacade<REF_TARGET> implements ProductRecommendationManagerFacade
{
	private final static Logger LOG = Logger.getLogger(DefaultProductRecommendationManagerFacade.class.getName());
	private Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> referenceDataProductReferenceConverter;
	private ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> referenceProductConfiguredPopulator;

	private GenericFactory genericFactory;
	private UserService userService;
	private CartService cartService;
	private UserIdProvider userIDProvider;
	private String anonOriginOfContactId;
	private int batchSize;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "hmcConfigurationReader")
	private HMCConfigurationReader configuration;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Override
	public boolean isRecommendationFetched(final String componentUID)
	{
		if (sessionService.getAttribute(sessionService.getCurrentSession().getSessionId() + "_" + componentUID) != null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Get product recommendations based on current context
	 *
	 */
	@Override
	public List<ProductReferenceData> getProductRecommendation(final RecommendationContext context)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references = createReferenzList(context);

		final List<ProductReferenceData> result = new ArrayList<ProductReferenceData>();

		for (final ReferenceData<ProductReferenceTypeEnum, ProductModel> reference : references)
		{
			final ProductReferenceData productReferenceData = getReferenceDataProductReferenceConverter().convert(
					(ReferenceData<ProductReferenceTypeEnum, REF_TARGET>) reference);
			getReferenceProductConfiguredPopulator().populate((REF_TARGET) reference.getTarget(), productReferenceData.getTarget(),
					PRODUCT_OPTIONS);
			result.add(productReferenceData);
		}

		return result;
	}

	@Override
	public List<CMSSAPRecommendationComponentModel> getRecommendationComponentForPage(final AbstractPageModel pageModel)
	{

		final List<CMSSAPRecommendationComponentModel> prodRecoComponents = new ArrayList<CMSSAPRecommendationComponentModel>();
		final List<ContentSlotForPageModel> contentSlots = pageModel.getContentSlots();
		for (final ContentSlotForPageModel contentSlot : contentSlots)
		{
			final List<AbstractCMSComponentModel> components = contentSlot.getContentSlot().getCmsComponents();
			for (final AbstractCMSComponentModel component : components)
			{
				if (component.getItemtype().equals(CMSSAPRecommendationComponentModel._TYPECODE) && component.getVisible())
				{
					prodRecoComponents.add((CMSSAPRecommendationComponentModel) component);
				}
			}
		}
		return prodRecoComponents;
	}

	/**
	 * Post clickthrough action to backend
	 *
	 * @param context
	 *
	 */
	@Override
	public void postInteraction(final InteractionContext context)
	{
		getRecommendationManager().postInteraction(context);
	}

	/**
	 * @param context
	 *
	 */
	private List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> createReferenzList(final RecommendationContext context)
	{
		final List<ProductRecommendationData> productRecommendations = getRecommendationManager().getProductRecommendation(context);

		if (productRecommendations != null)
		{
			return convertToProductReference(productRecommendations);
		}

		return Lists.newArrayList();
	}

	/**
	 * @param productRecommendations
	 * @return
	 */
	private List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> convertToProductReference(
			final List<ProductRecommendationData> productRecommendations)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references = new ArrayList<ReferenceData<ProductReferenceTypeEnum, ProductModel>>();

		for (final ProductRecommendationData productRecommendation : productRecommendations)
		{
			addReferenceData(references, productRecommendation);
		}

		return references;
	}

	private void addReferenceData(final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references,
			final ProductRecommendationData productRecommendation)
	{
		try
		{
			final ProductModel product = productService.getProductForCode(productRecommendation.getProductCode());
			references.add(createReferenceData(product));

		}
		catch (IllegalArgumentException | UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			LOG.error("Error occurred while getting product data due to " + e.getClass().getName());
		}
	}

	private ReferenceData<ProductReferenceTypeEnum, ProductModel> createReferenceData(final ProductModel product)
	{
		final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = new ReferenceData<ProductReferenceTypeEnum, ProductModel>();
		referenceData.setQuantity(new Integer(1));
		referenceData.setReferenceType(ProductReferenceTypeEnum.OTHERS);
		referenceData.setTarget(product);
		return referenceData;
	}

	@Override
	public String getSessionUserId()
	{
		final UserModel currentUser = userService.getCurrentUser();
		return userIDProvider.getUserId(currentUser);
	}

	@Override
	public void populateContext(final String userId, final boolean isAnonymousUser, final RecommendationContext context,
			final CMSSAPRecommendationComponentModel component, final String productCode)
	{
		context.setLeadingItemId(productCode);
		context.setRecotype(component.getRecotype());
		context.setIncludeCart(BooleanUtils.isTrue(component.isIncludecart()));
		context.setIncludeRecent(BooleanUtils.isTrue(component.isIncluderecent()));
		context.setLeadingItemDSType(component.getLeadingitemdstype());
		context.setLeadingItemType(component.getLeadingitemtype());
		context.setComponentModel(component);
		context.setUserId(userId);
		if (isAnonymousUser)
		{
			context.setUserType(this.getAnonOriginOfContactId());
		}
		else
		{
			context.setUserType(configuration.getUserType());
		}
		context.setUsage(configuration.getUsage());
		context.setCartItemDSType(component.getCartitemdstype());
	}

	@Override
	public void prefetchRecommendationsForAllComponents(final String userId, final boolean isAnonymousUser,
			final AbstractPageModel pageModel, final String productCode)
	{
		LOG.debug("\n\n BATCH SIZE : " + getBatchSize() + " \n\n");
		final List<RecommendationContext> contexts = new ArrayList<RecommendationContext>();
		final List<CMSSAPRecommendationComponentModel> components = getRecommendationComponentForPage(pageModel);
		for (final CMSSAPRecommendationComponentModel component : components)
		{
			final RecommendationContext context = genericFactory.getBean("sapRecommendationContextProvider");
			populateContext(userId, isAnonymousUser, context, component, productCode);
			contexts.add(context);
			if (contexts.size() == getBatchSize())
			{
				getRecommendationManager().prefetchRecommendations(contexts);
				contexts.clear();
			}
		}
		if (contexts.size() > 0)
		{
			getRecommendationManager().prefetchRecommendations(contexts);
		}
	}

	protected ProductRecommendationManagerBO getRecommendationManager()
	{
		return genericFactory.getBean("sapProductRecommendationManagerBO");
	}

	/**
	 * @return the referenceDataProductReferenceConverter
	 */
	public Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> getReferenceDataProductReferenceConverter()
	{
		return referenceDataProductReferenceConverter;
	}

	/**
	 * @param referenceDataProductReferenceConverter
	 *           the referenceDataProductReferenceConverter to set
	 */
	public void setReferenceDataProductReferenceConverter(
			final Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> referenceDataProductReferenceConverter)
	{
		this.referenceDataProductReferenceConverter = referenceDataProductReferenceConverter;
	}

	/**
	 * @return the referenceProductConfiguredPopulator
	 */
	public ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> getReferenceProductConfiguredPopulator()
	{
		return referenceProductConfiguredPopulator;
	}

	/**
	 * @param referenceProductConfiguredPopulator
	 *           the referenceProductConfiguredPopulator to set
	 */
	public void setReferenceProductConfiguredPopulator(
			final ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> referenceProductConfiguredPopulator)
	{
		this.referenceProductConfiguredPopulator = referenceProductConfiguredPopulator;
	}

	/**
	 * @return the genericFactory
	 */
	public GenericFactory getGenericFactory()
	{
		return genericFactory;
	}

	/**
	 * @param genericFactory
	 *           the genericFactory to set
	 */
	public void setGenericFactory(final GenericFactory genericFactory)
	{
		this.genericFactory = genericFactory;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the userIDProvider
	 */
	public UserIdProvider getUserIDProvider()
	{
		return userIDProvider;
	}

	/**
	 * @param userIDProvider
	 *           the userIDProvider to set
	 */
	public void setUserIDProvider(final UserIdProvider userIDProvider)
	{
		this.userIDProvider = userIDProvider;
	}

	@Override
	public String getAnonOriginOfContactId()
	{
		return anonOriginOfContactId;
	}

	@Override
	public void setAnonOriginOfContactId(final String anonOriginOfContactId)
	{
		this.anonOriginOfContactId = anonOriginOfContactId;
	}

	/**
	 * @return batchSize
	 */
	public int getBatchSize()
	{
		return batchSize;
	}

	/**
	 * @param batchSize
	 *           the batchSize to set
	 */
	@Required
	public void setBatchSize(final int batchSize)
	{
		this.batchSize = batchSize;
	}


	@Override
	public boolean isPrefetchEnabled()
	{
		return getRecommendationManager().isPrefetchEnabled();
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the configuration
	 */
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


}
