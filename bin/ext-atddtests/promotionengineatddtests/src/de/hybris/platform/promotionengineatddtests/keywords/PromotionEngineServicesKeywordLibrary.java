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
package de.hybris.platform.promotionengineatddtests.keywords;

import static com.google.common.base.Preconditions.checkState;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionEngineService;
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionEngineService;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.action.RuleActionService;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.TaxValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;



public class PromotionEngineServicesKeywordLibrary extends AbstractKeywordLibrary
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PromotionEngineServicesKeywordLibrary.class);

	@Autowired
	private RuleEngineService commerceRuleEngineService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private PromotionEngineService promotionEngineService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private PriceService priceService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CalculationService calculationService;

	@Autowired
	private RuleActionService ruleActionService;

	@Autowired
	private RuleEngineContextDao ruleEngineContextDao;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private UserService userService;

	@Autowired
	private ExtendedOrderDao orderDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private RuleService ruleService;

	@Autowired
	private RuleCompilerService ruleCompilerService;

	@Autowired
	private EngineRuleDao engineRuleDao;

	@Autowired
	private PromotionDao promotionsDao;

	@Autowired
	private DeliveryModeDao deliveryModeDao;

	@Autowired
	private CouponService couponService;

	public RuleEngineActionResult initializeRuleEngineWithRuleFromAndMaxAllowedRuns(final String rulesFileName,
			final int maxAllowedRuns) throws IOException
	{
		final String ruleContent = readRuleFile(rulesFileName);

		final DroolsRuleModel rule = modelService.create(DroolsRuleModel.class);
		rule.setActive(Boolean.TRUE);
		rule.setRuleContent(ruleContent);
		rule.setCode(rulesFileName);
		rule.setMaxAllowedRuns(Integer.valueOf(maxAllowedRuns));
		rule.setRuleType(RuleType.PROMOTION);
		if (rulesFileName.contains(".drl"))
		{
			rule.setUuid(rulesFileName.substring(0, rulesFileName.length() - 4));
		}
		else
		{
			rule.setUuid(rulesFileName);
		}
		setGlobals(rule);
		modelService.save(rule);

		final RuleEngineActionResult result = getCommerceRuleEngineService().initialize(
				getTestRulesModule(Collections.singleton(rule)));
		if (result.isActionFailed())
		{
			throw new IllegalStateException("error during rule initialization. Check your rules. \nDetailed error(s): "
					+ result.getMessagesAsString(MessageLevel.ERROR));
		}
		return result;
	}

	public RuleEngineActionResult initializeRuleEngineWithRulesFrom(final String rulesFileNames) throws IOException
	{
		final String[] fileNames = rulesFileNames.split("\\s+");
		final List<String> ruleNameList = Arrays.stream(fileNames).collect(Collectors.toList());
		final Map<String, String> ruleName2RuleContent = new HashMap<>();
		for (final String rulesFileName : ruleNameList)
		{
			ruleName2RuleContent.put(rulesFileName, readRuleFile(rulesFileName));
		}
		final RuleEngineActionResult result = initializeRuleEngineWithRules(ruleName2RuleContent);
		if (result.isActionFailed())
		{
			throw new IllegalStateException("error during rule initialization. Check your rules. \nDetailed error(s): "
					+ result.getMessagesAsString(MessageLevel.ERROR));
		}
		return result;
	}

	public CartModel createCart(final String cartId, final String currency)
	{
		final CartModel result = modelService.create(CartModel.class);

		result.setCode(cartId);
		result.setCurrency(commonI18NService.getCurrency(currency != null ? currency : "USD"));
		result.setDate(new Date());
		result.setUser(userService.getAnonymousUser());
		result.setNet(Boolean.TRUE);
		modelService.save(result);

		return result;
	}

	public CartModel createCart(final String cartId)
	{
		return createCart(cartId, null);
	}


	public void addProductToCart(final ProductModel product, final String cartId)
	{

		final CartModel cart = getCartByCode(cartId);
		final CartEntryModel entry = cartService.addNewEntry(cart, product, 1, product.getUnit());
		entry.setTaxValues(new ArrayList<TaxValue>());

		for (final PriceRowModel pr : product.getEurope1Prices())
		{
			if (pr.getCurrency().equals(cart.getCurrency()))
			{
				entry.setBasePrice(pr.getPrice());
				entry.setTotalPrice(pr.getPrice());
			}
		}

		modelService.save(entry);
		modelService.save(cart);
	}

	public void removeProduct(final ProductModel product, final String cartId) throws CalculationException
	{
		final CartModel cart = getCartByCode(cartId);
		final Map<Integer, Long> quantityMap = new HashMap<Integer, Long>();
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				quantityMap.put(entry.getEntryNumber(), Long.valueOf(0L));
			}
		}
		cartService.updateQuantities(cart, quantityMap);

		calculationService.calculate(cart);
	}

	public void updateCartQuantity(final int entryNo, final long quantity, final String cartId) throws CalculationException
	{

		final CartModel cart = getCartByCode(cartId);

		final Map<Integer, Long> quantityMap = new HashMap<Integer, Long>();
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getEntryNumber().intValue() == (entryNo))
			{
				quantityMap.put(entry.getEntryNumber(), Long.valueOf(quantity));
				break;
			}
		}
		cartService.updateQuantities(cart, quantityMap);

		calculationService.calculate(cart);
	}

	protected CartModel getCartByCode(final String code)
	{
		return (CartModel) orderDao.findOrderByCode(code);

	}

	public BigDecimal getTotalOfCart(final String cartId) throws CalculationException
	{

		final CartModel cart = getCartByCode(cartId);
		calculationService.calculate(cart);

		return new BigDecimal(cart.getTotalPrice().doubleValue());

	}

	public void configurePromotionEngineService(final String ruleContext)
	{
		final DefaultPromotionEngineService defaultPromotionEngineService = (DefaultPromotionEngineService) getPromotionEngineService();

		defaultPromotionEngineService.setDefaultRuleEngineContextName(ruleContext);

	}

	/**
	 * configures the promotionengineservice to use the junit sample data rule engine context
	 */
	public void configurePromotionEngineService()
	{
		final DefaultPromotionEngineService defaultPromotionEngineService = (DefaultPromotionEngineService) getPromotionEngineService();
		defaultPromotionEngineService.setDefaultRuleEngineContextName("promotions-junit-context");
	}

	protected String readRuleFile(String fileName) throws IOException
	{
		Path rulePath;
		try
		{
			rulePath = Paths.get(Registry.getApplicationContext().getResource("classpath:/rules/" + fileName).getURI());
		}
		catch (final FileNotFoundException fnf)
		{
			rulePath = Paths.get(Registry.getApplicationContext()
					.getResource("classpath:../../promotionenginesamplesaddon/resources/rules/" + fileName + ".drl").getURI());
			fileName += ".drl";
		}
		final InputStream is = Files.newInputStream(rulePath);
		final StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, Charset.forName("UTF-8"));
		return writer.toString();
	}


	protected RuleEngineActionResult initializeRuleEngineWithRules(final Map<String, String> ruleName2media)
	{
		final Set<DroolsRuleModel> rules = new LinkedHashSet<>();
		for (final Entry<String, String> entry : ruleName2media.entrySet())
		{
			final DroolsRuleModel rule = modelService.create(DroolsRuleModel.class);
			rule.setActive(Boolean.TRUE);
			rule.setRuleContent(entry.getValue());
			rule.setCode(entry.getKey());
			rule.setRuleType(RuleType.PROMOTION);
			if (entry.getKey().contains(".drl"))
			{
				rule.setUuid(entry.getKey().substring(0, entry.getKey().length() - 4));
			}
			else
			{
				rule.setUuid(entry.getKey());
			}
			setGlobals(rule);
			rules.add(rule);
			modelService.save(rule);
		}
		return getCommerceRuleEngineService().initialize(getTestRulesModule(rules));
	}

	protected List<PromotionGroupModel> getTestPromoGroups()
	{
		final List<PromotionGroupModel> list = new ArrayList();
		final PromotionGroupModel testPromo = promotionsDao.findPromotionGroupByCode("testPromoGrp");
		final PromotionGroupModel electronicsPromoGrp = promotionsDao.findPromotionGroupByCode("electronicsPromoGrp");
		list.add(testPromo);
		list.add(electronicsPromoGrp);
		return list;
	}

	// extracts the global definitions out of the drl and sets them at the rule
	protected void setGlobals(final DroolsRuleModel rule)
	{
		final String drl = rule.getRuleContent();
		final BufferedReader br = new BufferedReader(new StringReader(drl));
		final Map<String, String> globals = new HashMap<>();
		br.lines().filter(s -> s.startsWith("global")).forEach(s -> {
			final String[] split = s.split("\\s+");
			// 3rd string is the global bean
				if (split.length == 3)
				{
					final String string = split[2].substring(0, split[2].length() - 1);
					globals.put(string, string);
				}
			});
		rule.setGlobals(globals);
	}

	protected DroolsKIEModuleModel getTestRulesModule(final Set<DroolsRuleModel> rules)
	{
		final AbstractRuleEngineContextModel abstractContext = getRuleEngineContextDao().getRuleEngineContextByName(
				"promotions-junit-context");
		checkState(abstractContext instanceof DroolsRuleEngineContextModel,
				"ruleengine context must be of type DroolsRuleEngineContextModel");

		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) abstractContext;

		final DroolsKIEBaseModel kieBase = context.getKieSession().getKieBase();
		kieBase.setRules(rules);
		getModelService().saveAll();
		return context.getKieSession().getKieBase().getKieModule();
	}


	public BigDecimal evaluatePromotionForCart(final String cartId) throws CalculationException
	{
		final CartModel cart = getCartByCode(cartId);

		((DefaultPromotionEngineService) getPromotionEngineService()).updatePromotions(getTestPromoGroups(), cart);
		return new BigDecimal(cart.getTotalPrice().doubleValue()).setScale(2, RoundingMode.HALF_UP);
	}

	public String getDeliveryModeForCart(final String cartId)
	{
		final CartModel cart = getCartByCode(cartId);
		return cart.getDeliveryMode() != null ? cart.getDeliveryMode().getCode() : null;
	}

	public void setDeliveryModeForCart(final String cartId, final String deliveryModeCode)
	{
		final List<DeliveryModeModel> deliveryModesByCode = deliveryModeDao.findDeliveryModesByCode(deliveryModeCode);
		Assert.assertEquals(1, deliveryModesByCode.size());
		final CartModel cart = getCartByCode(cartId);
		cart.setDeliveryMode(deliveryModesByCode.get(0));
	}

	public AbstractRuleActionRAO evaluatePromotionForProduct(final ProductModel product)
	{
		final RuleEvaluationResult evaluatingResult = promotionEngineService.evaluate(product, getTestPromoGroups());
		final RuleEngineResultRAO result = evaluatingResult.getResult();
		final LinkedHashSet<AbstractRuleActionRAO> actions = result.getActions();
		for (final AbstractRuleActionRAO action : actions)
		{
			if (action.getAppliedToObject() instanceof ProductRAO
					&& product.getCode().equals(((ProductRAO) action.getAppliedToObject()).getCode()))
			{
				return action;
			}
		}
		return null;
	}

	public ProductModel getProduct(final String productCode) throws UnknownIdentifierException
	{
		return productService.getProductForCode(productCode);

	}


	public CategoryModel getCategory(final String categoryCode)
	{
		return categoryService.getCategoryForCode(categoryCode);
	}

	public BigDecimal getProductPrice(final ProductModel productModel)
	{
		return getProductPriceWithPromotion(productModel, null);
	}

	public BigDecimal getProductPriceWithPromotion(final ProductModel productModel, final DiscountRAO discount)
	{
		double discountAmount = 0.0;
		if (discount != null && discount.getCurrencyIsoCode() == null)
		{
			discountAmount = discount.getValue().doubleValue();
		}

		final BigDecimal priceData = BigDecimal.valueOf(getWebPriceForProduct(productModel).getPriceValue().getValue()
				* (100.0D - discountAmount) / 100.0D);
		return priceData;

	}

	public void checkProductQuantity(final ProductModel product, final String cartId, final int quantity)
	{
		final CartModel cart = getCartByCode(cartId);
		long cartQuantity = 0;
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().equals(product))
			{
				cartQuantity += entry.getQuantity().longValue();
			}
		}

		Assert.assertEquals(quantity, cartQuantity);

	}

	public void checkThatPriceLessThanFor(final BigDecimal price, final BigDecimal promotedPrice, final String discountValue)
	{

		if (discountValue.contains("%"))
		{
			final int percentage = 100 - Integer.parseInt(discountValue.replace("%", ""));
			Assert.assertTrue(
					String.format("Actual price %f, promoted price %f and discount value %s", price, promotedPrice, discountValue),
					price.multiply(new BigDecimal(percentage)).divide(BigDecimal.valueOf(100)).subtract(promotedPrice)
							.compareTo(BigDecimal.ZERO) == 0);
		}
	}

	public void checkThatDeliveryModeNotDefined(final String cartId)
	{
		final CartModel cart = getCartByCode(cartId);
		Assert.assertTrue("Delivery mode of cart with id '" + cartId + "' but was " + cart.getDeliveryMode(),
				cart.getDeliveryMode() == null);
	}

	public void setCartToUser(final String userUID, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId);
		final UserModel user = getUserService().getUserForUID(userUID);
		cart.setUser(user);
		getModelService().save(cart);
		// avoids that the cart is calculated for the wrong user
		getModelService().refresh(cart);
		JaloSession.getCurrentSession().getSessionContext().setUser((User) getModelService().getSource(user));
	}

	private PriceInformation getWebPriceForProduct(final ProductModel product)
	{
		validateParameterNotNull(product, "Product model cannot be null");
		final List<PriceInformation> prices = priceService.getPriceInformationsForProduct(product);
		if (CollectionUtils.isNotEmpty(prices))
		{
			PriceInformation minPriceForLowestQuantity = null;
			for (final PriceInformation price : prices)
			{
				if (minPriceForLowestQuantity == null
						|| (((Long) minPriceForLowestQuantity.getQualifierValue("minqtd")).longValue() > ((Long) price
								.getQualifierValue("minqtd")).longValue()))
				{
					minPriceForLowestQuantity = price;
				}
			}
			return minPriceForLowestQuantity;
		}
		return null;
	}


	public void compileSourceRule(final String ruleCode) throws RuleCompilerException
	{
		final AbstractRuleModel rule = getRuleService().getRuleForCode(ruleCode);
		getRuleCompilerService().compile(rule);

		final DroolsRuleModel droolsRule = (DroolsRuleModel) getEngineRuleDao().getRuleByCode(ruleCode);

		setGlobals(droolsRule);
		getCommerceRuleEngineService().initialize(getTestRulesModule(Collections.singleton(droolsRule)));

	}

	public void redeemCouponWithCodeForCart(final String couponCode, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId);
		getCouponService().redeemCoupon(couponCode, cart);
	}

	public void releaseCouponWithCodeForCart(final String couponCode, final String cartId)
	{
		final CartModel cart = getCartByCode(cartId);
		getCouponService().releaseCouponCode(couponCode, cart);
	}

	public void checkOrderUsesCoupon(final String cartId, final String couponCode)
	{
		if (!orderUsesCoupon(cartId, couponCode))
		{
			Assert.fail("Coupon code " + couponCode + " is not used for cart " + cartId);
		}
	}

	public void checkOrderNotUsesCoupon(final String cartId, final String couponCode)
	{
		if (orderUsesCoupon(cartId, couponCode))
		{
			Assert.fail("Coupon code " + couponCode + " is used for cart " + cartId);
		}
	}

	protected boolean orderUsesCoupon(final String cartId, final String couponCode)
	{
		final CartModel cart = getCartByCode(cartId);
		for (final PromotionResultModel result : cart.getAllPromotionResults())
		{
			for (final AbstractPromotionActionModel action : result.getActions())
			{
				if (action instanceof AbstractRuleBasedPromotionActionModel)
				{
					final AbstractRuleBasedPromotionActionModel ruleBasedPromotionAction = (AbstractRuleBasedPromotionActionModel) action;
					return CollectionUtils.isNotEmpty(ruleBasedPromotionAction.getUsedCouponCodes())
							&& ruleBasedPromotionAction.getUsedCouponCodes().contains(couponCode);
				}
			}
		}
		return false;
	}

	public BigDecimal getProductTotalFromCart(final String productId, final String cartId)
	{
		BigDecimal productTotal = BigDecimal.valueOf(0);
		final CartModel cart = getCartByCode(cartId);
		for (final AbstractOrderEntryModel entry : cart.getEntries())
		{
			if (entry.getProduct().getCode().equals(productId))
			{
				productTotal = productTotal.add(BigDecimal.valueOf(entry.getTotalPrice().doubleValue()));
			}
		}

		return productTotal;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected RuleEngineService getCommerceRuleEngineService()
	{
		return commerceRuleEngineService;
	}

	protected PromotionEngineService getPromotionEngineService()
	{
		return promotionEngineService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	protected PriceService getPriceService()
	{
		return priceService;
	}

	protected RuleEngineContextDao getRuleEngineContextDao()
	{
		return ruleEngineContextDao;
	}

	protected RuleActionService getRuleActionService()
	{
		return ruleActionService;
	}

	protected void setRuleActionService(final RuleActionService ruleActionService)
	{
		this.ruleActionService = ruleActionService;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}

	protected RuleCompilerService getRuleCompilerService()
	{
		return ruleCompilerService;
	}

	protected EngineRuleDao getEngineRuleDao()
	{
		return engineRuleDao;
	}

	protected CouponService getCouponService()
	{
		return couponService;
	}

	protected void setCouponService(final CouponService couponService)
	{
		this.couponService = couponService;
	}
}
