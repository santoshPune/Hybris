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
package de.hybris.platform.chinesepspalipayservices.payment;

import de.hybris.platform.chinesepaymentservices.enums.ServiceType;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayConfiguration;
import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayErrorInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayReturnInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;
import de.hybris.platform.chinesepspalipayservices.order.AlipayOrderService;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayCreateRequestStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayHandleResponseStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentInfoStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentTransactionStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayResponseValidationStrategy;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class DefaultAlipayPaymentService extends DefaultPaymentServiceImpl implements ChinesePaymentService
{

	private AlipayPaymentInfoStrategy alipayPaymentInfoStrategy;
	private CommerceCheckoutService commerceCheckoutService;
	private AlipayOrderService alipayOrderService;
	private AlipayCreateRequestStrategy alipayCreateRequestStrategy;
	private AlipayPaymentTransactionStrategy alipayPaymentTransactionStrategy;
	private AlipayResponseValidationStrategy alipayResponseValidationStrategy;
	private AlipayHandleResponseStrategy alipayHandleResponseStrategy;
	private MediaService mediaService;
	private ChineseOrderService chineseOrderService;
	private AlipayConfiguration alipayConfiguration;
	private ModelService modelService;
	private CMSSiteService cmsSiteService;
	private CommerceCommonI18NService commerceCommonI18NService;

	private static final Logger LOG = Logger.getLogger(DefaultAlipayPaymentService.class.getName());
	private static final String ALIPAY_LOG_NAME = "/images/theme/alipay.jpg";
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	@Override
	public void handleAsyncResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (request.getParameterMap() == null)
		{
			LOG.warn("Empty request");
			return;
		}
		
		final Map<String, String> unifyResponseMap = unifyRequestParameterValue(request.getParameterMap());

		//directPay
		if (request.getRequestURL().toString().contains(PaymentConstants.Controller.DIRECT_AND_EXPRESS_NOTIFY_URL))
		{
			final boolean verify = alipayResponseValidationStrategy.validateResponse(unifyResponseMap);
			if (!verify)
			{
				LOG.warn("Invalid notify from Alipay");
				return;
			}
			try
			{
				handleNotification(unifyResponseMap, response);
			}
			catch (final IOException e)
			{
				LOG.error("Problem in handling Alipay's direct pay notify message", e);
			}
		}
		else if (request.getRequestURL().toString().contains(PaymentConstants.Controller.ERROR_NOTIFY_URL))
		{
			handleErrorResponse(unifyResponseMap);
		}
		//refund
		else if (request.getRequestURL().toString().contains(PaymentConstants.Controller.REFUND_NOTIFY_URL))
		{
			final boolean verify = alipayResponseValidationStrategy.validateResponse(unifyResponseMap);
			if (!verify)
			{
				LOG.warn("Invalid notify from Alipay");
				return;
			}
			try
			{
				handleRefundNotification(unifyResponseMap, response);
			}
			catch (IOException e)
			{
				LOG.error("Problem in handling Alipay's refund notify message", e);
			}

		}

	}

	@Override
	public String handleSyncResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (request.getParameterMap() == null)
		{
			LOG.warn("Empty request");
			return null;
		}
		
		final Map<String, String> unifyResponseMap = unifyRequestParameterValue(request.getParameterMap());
		if (request.getRequestURL().toString().contains(PaymentConstants.Controller.DIRECT_AND_EXPRESS_RETURN_URL))
		{

			final boolean verify = alipayResponseValidationStrategy.validateResponse(unifyResponseMap);

			if (!verify)
			{
				LOG.warn("Invalid return from Alipay");
			}
			// TODO check if business process is right
			try
			{
				return handleReturnInfo(unifyResponseMap);
			}
			catch (final IOException e)
			{
				LOG.error("Handle returnInfo for alipay failed", e);
			}

		}
		return null;

	}

	@Override
	public boolean cancelPayment(final String orderCode)
	{
		final OrderModel orderModel = getOrderModelByCode(orderCode);
		if (PaymentStatus.PAID.equals(orderModel.getPaymentStatus()))
		{
			return false;
		}
		final AlipayCancelPaymentRequestData alipayCancelPaymentRequestData = createAlipayCancelPaymentRequestDataByOrder(
				orderModel);
		try
		{

			final boolean isCaputreNotExist = alipayPaymentTransactionStrategy.checkCaptureTransactionEntry(orderModel,
					TransactionStatus.ACCEPTED);

			if (isCaputreNotExist)
			{
				final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult = alipayCreateRequestStrategy
						.submitCancelPaymentRequest(alipayCancelPaymentRequestData);

				if (alipayRawCancelPaymentResult == null)
				{
					LOG.warn("Timeout / Invalid response from Alipay received for order " + orderCode);
					return false;
				}

				alipayPaymentTransactionStrategy.updateForCancelPayment(orderModel, alipayRawCancelPaymentResult);

				if (alipayRawCancelPaymentResult.getError() == null)
				{
					return true;
				}

			}
			else
			{
				orderModel.setPaymentStatus(PaymentStatus.PAID);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Cancel trade:" + orderCode + " fails", e);
		}
		return false;
	}

	@Override
	public String getPaymentRequestUrl(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		final AlipayDirectPayRequestData alipayDirectPayRequestData = createAlipayDirectPayRequestDataByOrder(orderModel);
		try
		{
			final String directPayUrl = alipayCreateRequestStrategy.createDirectPayUrl(alipayDirectPayRequestData);
			alipayPaymentTransactionStrategy.createForNewRequest(orderModel, directPayUrl);
			return directPayUrl;
		}
		catch (final Exception e)
		{
			LOG.error("Create alipay direct pay url fails", e);
			return null;
		}
	}

	@Override
	public Optional<String> getRefundRequestUrl(String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		if (PaymentStatus.REFUNDED.equals(orderModel.getPaymentStatus()))
		{
			return Optional.empty();
		}
		return createAlipayRefundRequestDataByOrder(orderModel).map(alipayRefundRequestData -> {
			try
			{
				final String refundUrl = alipayCreateRequestStrategy.createRefundUrl(alipayRefundRequestData);
				alipayPaymentTransactionStrategy.updateTransactionForRefundRequest(orderModel, alipayRefundRequestData);
				return refundUrl;
			}
			catch (final Exception e)
			{
				LOG.error("Create alipay direct pay url fails", e);
				return null;
			}
		});
	}

	@Override
	public void syncPaymentStatus(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		final AlipayPaymentStatusRequestData alipayPaymentStatusRequestData = createAlipayPaymentStatusRequestDataByOrder(
				orderModel);
		try
		{
			final AlipayRawPaymentStatus alipayRawPaymentStatus = alipayCreateRequestStrategy
					.submitPaymentStatusRequest(alipayPaymentStatusRequestData);
			if (alipayRawPaymentStatus != null)
			{
				final AlipayPaymentTransactionEntryModel entry = alipayPaymentTransactionStrategy.saveForStatusCheck(orderModel,
						alipayRawPaymentStatus);

				if (entry != null && TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
				{
					orderModel.setPaymentStatus(PaymentStatus.PAID);
					modelService.save(orderModel);
					chineseOrderService.markOrderAsPaid(orderCode);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Check alipay trade fails", e);
		}

	}

	@Override
	public boolean setPaymentInfo(final CartModel cartModel, final ChinesePaymentInfoModel chinesePaymentInfoModel)
	{
		cartModel.setChinesePaymentInfo(chinesePaymentInfoModel);
		alipayPaymentInfoStrategy.updatePaymentInfoForPayemntMethod(chinesePaymentInfoModel);
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setPaymentInfo(chinesePaymentInfoModel);
		return commerceCheckoutService.setPaymentInfo(parameter);
	}

	@Override
	public String getPspLogoUrl()
	{
		final MediaModel alipayLog = mediaService.getMedia(ALIPAY_LOG_NAME);
		return alipayLog.getURL();
	}


	/**
	 * retrieve OrderModel by order code
	 *
	 * @param orderCode
	 *           order code of OrderModel
	 * @return OrderModel
	 */
	protected OrderModel getOrderModelByCode(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		if (orderModel == null)
		{
			throw new UnknownIdentifierException(
					"Order with orderGUID " + orderCode + " not found for current user in current BaseStore");
		}

		return orderModel;

	}

	protected Map<String, String> unifyRequestParameterValue(final Map<String, String[]> params)
	{
		final Map<String, String> unifyRequestMap = new LinkedHashMap<>();
		if (params != null)
		{
			for (final String key : params.keySet())
			{
				final String[] strArray = params.get(key);
				final StringBuilder builder = new StringBuilder();
				for (final String s : strArray)
				{
					builder.append(s);
				}
				final String value = builder.toString();
				unifyRequestMap.put(key, value);
			}
		}
		return unifyRequestMap;

	}

	protected Map<String, String> convertKey2CamelCase(final Map<String, String> snakeCaseMap)
	{
		final Map<String, String> camelCaseMap = new LinkedHashMap<>();
		for (final String key : snakeCaseMap.keySet())
		{
			final String value = snakeCaseMap.get(key);
			String camelKey = WordUtils.capitalizeFully(key, new char[]
			{ '_' }).replaceAll("_", "");
			camelKey = WordUtils.uncapitalize(camelKey);
			camelCaseMap.put(camelKey, value);
		}
		return camelCaseMap;
	}

	protected void handleNotification(final Map<String, String> responseMap, final HttpServletResponse response) throws IOException
	{
		final AlipayRawDirectPayNotification alipayRawDirectPayNotification = new AlipayRawDirectPayNotification();
		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);
		try
		{
			BeanUtils.populate(alipayRawDirectPayNotification, camelCaseMap);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle notify info from Alipay fails", e);
		}
		final String orderCode = alipayRawDirectPayNotification.getOutTradeNo();
		final OrderModel orderModel = getOrderModelByCode(orderCode);
		alipayPaymentTransactionStrategy.updateForNotification(orderModel, alipayRawDirectPayNotification);
		final String tradeStatus = PaymentConstants.TransactionStatusMap.AlipayToHybris
				.get(alipayRawDirectPayNotification.getTradeStatus()).name();

		if (TransactionStatus.ACCEPTED.name().equals(tradeStatus))
		{
			orderModel.setPaymentStatus(PaymentStatus.PAID);
		}
		else
		{
			orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
		}
		modelService.save(orderModel);
		chineseOrderService.markOrderAsPaid(orderCode);

		response.getWriter().print("success");
	}

	protected String handleReturnInfo(final Map<String, String> responseMap) throws IOException
	{
		final AlipayRawDirectPayReturnInfo alipayRawDirectPayReturnInfo = new AlipayRawDirectPayReturnInfo();
		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);
		try
		{
			BeanUtils.populate(alipayRawDirectPayReturnInfo, camelCaseMap);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle notify info from Alipay fails", e);
		}
		final String orderCode = alipayRawDirectPayReturnInfo.getOutTradeNo();
		return orderCode;
	}

	protected void handleErrorResponse(final Map<String, String> responseMap)
	{
		final String orderCode = responseMap.get(PaymentConstants.ErrorHandler.OUT_TRADE_NO);
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);

		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);

		final AlipayRawDirectPayErrorInfo alipayRawDirectPayErrorInfo = new AlipayRawDirectPayErrorInfo();

		try
		{
			BeanUtils.populate(alipayRawDirectPayErrorInfo, camelCaseMap);

			alipayPaymentTransactionStrategy.updateForError(orderModel, alipayRawDirectPayErrorInfo);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle error notify failes", e);
		}
	}

	protected AlipayDirectPayRequestData createAlipayDirectPayRequestDataByOrder(final OrderModel order)
	{
		Assert.notNull(order, "Parameter source cannot be null.");
		AlipayDirectPayRequestData alipayDirectPayRequestData = new AlipayDirectPayRequestData();
		if (order.getPaymentInfo() != null)
		{
			if (order.getPaymentInfo() instanceof ChinesePaymentInfoModel)
			{
				final ChinesePaymentInfoModel paymentInfo = (ChinesePaymentInfoModel) order.getPaymentInfo();
				final ServiceType serviceType = paymentInfo.getServiceType();
				if (ServiceType.DIRECTPAY.equals(serviceType))
				{
					alipayDirectPayRequestData.setPaymethod(alipayConfiguration.getDirectayPaymethodName());
				}
				else
				{
					alipayDirectPayRequestData.setPaymethod(alipayConfiguration.getExpressPaymethodName());
				}
				alipayDirectPayRequestData.setOutTradeNo(order.getCode());
				alipayDirectPayRequestData.setSubject(alipayConfiguration.getRequestSubject() + order.getCode());
				alipayDirectPayRequestData.setTotalFee(alipayConfiguration.getRequestPrice(order.getTotalPrice().doubleValue()));

				if (alipayConfiguration.getRequestTimeout() != null)
				{
					alipayDirectPayRequestData.setItBPay(alipayConfiguration.getRequestTimeout());
				}

				alipayDirectPayRequestData.setService(alipayConfiguration.getDirectPayServiceApiName());
				alipayDirectPayRequestData.setPartner(alipayConfiguration.getWebPartner());
				alipayDirectPayRequestData.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
				alipayDirectPayRequestData.setReturnUrl(alipayConfiguration.getBasepath() + getUrlEncodePattern()
						+ PaymentConstants.Controller.DIRECT_AND_EXPRESS_RETURN_URL);
				alipayDirectPayRequestData
						.setNotifyUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.DIRECT_AND_EXPRESS_NOTIFY_URL);
				alipayDirectPayRequestData
						.setErrorNotifyUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.ERROR_NOTIFY_URL);
				alipayDirectPayRequestData.setSellerEmail(alipayConfiguration.getWebSellerEmail());
				alipayDirectPayRequestData.setPaymentType(PaymentConstants.Basic.PaymentType.BUY_PRODUCT);

				long quantity = 0;
				for (final AbstractOrderEntryModel entry : order.getEntries())
				{
					quantity += entry.getQuantity().longValue();
				}

				alipayDirectPayRequestData.setQuantity(quantity);

			}
		}
		return alipayDirectPayRequestData;
	}

	protected AlipayCancelPaymentRequestData createAlipayCancelPaymentRequestDataByOrder(final OrderModel order)
	{
		Assert.notNull(order, "Parameter source cannot be null.");
		AlipayCancelPaymentRequestData alipayCancelPaymentRequestData = new AlipayCancelPaymentRequestData();
		alipayCancelPaymentRequestData.setService(alipayConfiguration.getCloseTradeServiceApiName());
		alipayCancelPaymentRequestData.setPartner(alipayConfiguration.getWebPartner());
		alipayCancelPaymentRequestData.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
		alipayCancelPaymentRequestData.setOutOrderNo(order.getCode());
		return alipayCancelPaymentRequestData;
	}

	protected AlipayPaymentStatusRequestData createAlipayPaymentStatusRequestDataByOrder(final OrderModel order)
	{
		Assert.notNull(order, "Parameter source cannot be null.");
		AlipayPaymentStatusRequestData alipayPaymentStatusRequestData = new AlipayPaymentStatusRequestData();
		alipayPaymentStatusRequestData.setService(alipayConfiguration.getCheckTradeServiceApiName());
		alipayPaymentStatusRequestData.setPartner(alipayConfiguration.getWebPartner());
		alipayPaymentStatusRequestData.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
		alipayPaymentStatusRequestData.setOutTradeNo(order.getCode());
		return alipayPaymentStatusRequestData;
	}

	protected Optional<AlipayRefundRequestData> createAlipayRefundRequestDataByOrder(final OrderModel order)
	{
		Assert.notNull(order, "Parameter source cannot be null.");

		Optional<AlipayPaymentTransactionModel> result = alipayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.ACCEPTED);

		return result.map(alipayPaymentTransactionModel -> {
			AlipayRefundRequestData alipayRefundRequestData = new AlipayRefundRequestData();

			alipayRefundRequestData.setService(alipayConfiguration.getRefundServiceApiName());
			alipayRefundRequestData.setPartner(alipayConfiguration.getWebPartner());
			alipayRefundRequestData.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
			alipayRefundRequestData.setNotifyUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.REFUND_NOTIFY_URL);
			alipayRefundRequestData.setSellerEmail(alipayConfiguration.getWebSellerEmail());
			alipayRefundRequestData.setSellerUserId(alipayConfiguration.getWebSellerId());


			Date date = Calendar.getInstance().getTime();
			alipayRefundRequestData.setRefundDate(simpleDateFormat.format(date));
			alipayRefundRequestData.setBatchNo(dateFormat.format(date) + order.getCode());

			alipayRefundRequestData.setBatchNum(PaymentConstants.Basic.REFUND_BATCH_NUM);

			String refundDetailData = alipayPaymentTransactionModel.getAlipayCode() + "^"
					+ alipayConfiguration.getRefundPrice(order.getTotalPrice()) + "^" + alipayConfiguration.getRefundReason();
			alipayRefundRequestData.setDetailData(refundDetailData);
			return Optional.of(alipayRefundRequestData);
		}).orElse(Optional.empty());

	}


	protected void handleRefundNotification(final Map<String, String> responseMap, final HttpServletResponse response)
			throws IOException
	{
		AlipayRefundNotification alipayRefundNotification = new AlipayRefundNotification();
		alipayRefundNotification = (AlipayRefundNotification) alipayHandleResponseStrategy.camelCaseFormatter(responseMap,
				alipayRefundNotification);
		List<AlipayRefundData> alipayRefundData = alipayHandleResponseStrategy.getAlipayRefundDataList(alipayRefundNotification);
		Map<OrderModel, Boolean> refundStatus = alipayPaymentTransactionStrategy.updateForRefundNotification(alipayRefundData);


		refundStatus.keySet().forEach((orderModel) -> {
			if (refundStatus.get(orderModel))
			{
				orderModel.setPaymentStatus(PaymentStatus.REFUNDED);
			}
			chineseOrderService.updateOrderForRefund(orderModel, refundStatus.get(orderModel));
		});

		response.getWriter().print("success");
	}

	protected String getUrlEncodePattern()
	{
		StringBuffer pattern = new StringBuffer();

		CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
		LanguageModel currentLanguage = getCommerceCommonI18NService().getCurrentLanguage();

		String site = (null == currentSite ? "" : currentSite.getUid());
		String language = (null == currentLanguage ? "" : currentLanguage.getIsocode());

		return pattern.append(site).append("/").append(language).toString() + "/";

	}


	@Required
	public void setAlipayPaymentInfoStrategy(final AlipayPaymentInfoStrategy alipayPaymentInfoStrategy)
	{
		this.alipayPaymentInfoStrategy = alipayPaymentInfoStrategy;
	}

	@Required
	public void setCommerceCheckoutService(final CommerceCheckoutService commerceCheckoutService)
	{
		this.commerceCheckoutService = commerceCheckoutService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	@Required
	public void setAlipayOrderService(final AlipayOrderService alipayOrderService)
	{
		this.alipayOrderService = alipayOrderService;
	}

	@Required
	public void setAlipayCreateRequestStrategy(final AlipayCreateRequestStrategy alipayCreateRequestStrategy)
	{
		this.alipayCreateRequestStrategy = alipayCreateRequestStrategy;
	}

	@Required
	public void setAlipayPaymentTransactionStrategy(final AlipayPaymentTransactionStrategy alipayPaymentTransactionStrategy)
	{
		this.alipayPaymentTransactionStrategy = alipayPaymentTransactionStrategy;
	}

	@Required
	public void setAlipayResponseValidationStrategy(final AlipayResponseValidationStrategy alipayResponseValidationStrategy)
	{
		this.alipayResponseValidationStrategy = alipayResponseValidationStrategy;
	}

	@Required
	public void setAlipayHandleResponseStrategy(final AlipayHandleResponseStrategy alipayHandleResponseStrategy)
	{
		this.alipayHandleResponseStrategy = alipayHandleResponseStrategy;
	}


	@Required
	public void setChineseOrderService(final ChineseOrderService chineseOrderService)
	{
		this.chineseOrderService = chineseOrderService;
	}

	@Required
	public void setAlipayConfiguration(final AlipayConfiguration alipayConfiguration)
	{
		this.alipayConfiguration = alipayConfiguration;
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected AlipayPaymentInfoStrategy getAlipayPaymentInfoStrategy()
	{
		return alipayPaymentInfoStrategy;
	}

	protected CommerceCheckoutService getCommerceCheckoutService()
	{
		return commerceCheckoutService;
	}

	protected AlipayOrderService getAlipayOrderService()
	{
		return alipayOrderService;
	}

	protected AlipayCreateRequestStrategy getAlipayCreateRequestStrategy()
	{
		return alipayCreateRequestStrategy;
	}

	protected AlipayPaymentTransactionStrategy getAlipayPaymentTransactionStrategy()
	{
		return alipayPaymentTransactionStrategy;
	}

	protected AlipayResponseValidationStrategy getAlipayResponseValidationStrategy()
	{
		return alipayResponseValidationStrategy;
	}

	protected AlipayHandleResponseStrategy getAlipayHandleResponseStrategy()
	{
		return alipayHandleResponseStrategy;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	protected ChineseOrderService getChineseOrderService()
	{
		return chineseOrderService;
	}

	protected AlipayConfiguration getAlipayConfiguration()
	{
		return alipayConfiguration;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}




}
