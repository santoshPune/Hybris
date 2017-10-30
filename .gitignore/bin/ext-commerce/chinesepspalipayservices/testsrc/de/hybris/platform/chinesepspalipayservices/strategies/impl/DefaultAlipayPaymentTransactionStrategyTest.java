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
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionDao;
import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionEntryDao;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



@IntegrationTest
public class DefaultAlipayPaymentTransactionStrategyTest extends ServicelayerTransactionalTest
{
	@Resource(name = "alipayPaymentTransactionStrategy")
	private DefaultAlipayPaymentTransactionStrategy defaultAlipayPaymentTransactionStrategy;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	private AlipayPaymentTransactionModel alipayPaymentTransactionModel;

	private OrderModel orderModel;

	private CurrencyModel currencyModel;

	private AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntryModel;

	private AlipayRefundRequestData alipayRefundRequestData;

	@Mock
	private KeyGenerator paymentTransactionKeyGenerator;

	@Mock
	private AlipayPaymentTransactionDao alipayPaymentTransactionDao;

	@Mock
	private AlipayPaymentTransactionEntryDao alipayPaymentTransactionEntryDao;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		alipayRefundRequestData = new AlipayRefundRequestData();
		alipayRefundRequestData.setBatchNo("201601011234");

		alipayPaymentTransactionModel = new AlipayPaymentTransactionModel();
		alipayPaymentTransactionModel.setAlipayCode("10000");
		alipayPaymentTransactionModel.setRequestId("000001");

		alipayPaymentTransactionEntryModel = new AlipayPaymentTransactionEntryModel();
		alipayPaymentTransactionEntryModel.setPaymentTransaction(alipayPaymentTransactionModel);
		alipayPaymentTransactionEntryModel.setType(PaymentTransactionType.CAPTURE);
		alipayPaymentTransactionEntryModel.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		alipayPaymentTransactionEntryModel.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()) + "44");

		List<PaymentTransactionModel> paymentTransactionModels = new ArrayList<>();
		paymentTransactionModels.add(alipayPaymentTransactionModel);

		currencyModel = new CurrencyModel();
		currencyModel.setIsocode("USD");

		orderModel = new OrderModel();
		orderModel.setCode("00000001");
		orderModel.setTotalPrice(1.5);
		orderModel.setCurrency(currencyModel);
		orderModel.setPaymentTransactions(paymentTransactionModels);
		orderModel.setDate(new Date());
		orderModel.setUser(userService.getCurrentUser());

		given(paymentTransactionKeyGenerator.generate()).willReturn("00000002");

	}

	@Test
	public void test_Update_For_Refund_Request()
	{
		DefaultAlipayPaymentTransactionStrategy spy = Mockito.spy(defaultAlipayPaymentTransactionStrategy);
		spy.setPaymentTransactionKeyGenerator(paymentTransactionKeyGenerator);
		Mockito.doReturn(Optional.of(alipayPaymentTransactionModel)).when(spy).getPaymentTransactionWithCaptureEntry(Mockito.any(),
				Mockito.any());

		spy.updateTransactionForRefundRequest(orderModel, alipayRefundRequestData);

		Optional<AlipayPaymentTransactionEntryModel> alipayPaymentTransactionEntryModel = defaultAlipayPaymentTransactionStrategy
				.getPaymentTransactionEntry(orderModel, TransactionStatus.REVIEW, PaymentTransactionType.REFUND_STANDALONE);
		assertTrue(alipayPaymentTransactionEntryModel.isPresent());
		AlipayPaymentTransactionEntryModel result = alipayPaymentTransactionEntryModel.get();
		assertEquals(currencyModel.getIsocode(), result.getCurrency().getIsocode());
		assertEquals(PaymentTransactionType.REFUND_STANDALONE, result.getType());
		assertEquals(alipayPaymentTransactionModel, result.getPaymentTransaction());
		assertEquals("000001", result.getRequestId());
		assertEquals(TransactionStatus.REVIEW.name(), result.getTransactionStatus());
		assertEquals(TransactionStatusDetails.SUCCESFULL.name() + "; Refund request: 201601011234",
				result.getTransactionStatusDetails());
		assertEquals("00000002", result.getCode());

	}

	@Test
	public void test_Update_For_Refund_Notification()
	{

		DefaultAlipayPaymentTransactionStrategy spy = Mockito.spy(defaultAlipayPaymentTransactionStrategy);
		spy.setPaymentTransactionKeyGenerator(paymentTransactionKeyGenerator);

		List<AlipayRefundData> alipayRefundData = new ArrayList<AlipayRefundData>();

		AlipayRefundData refundData = new AlipayRefundData();
		refundData.setAlipayCode("123456");
		refundData.setBatchNo("20060702001");
		refundData.setPayerRefundAmount(80);
		refundData.setPayerRefundStatus("SUCCESS");
		refundData.setSellerEmail("jax_chuanhang@alipay.com");
		refundData.setSellerId("2088101003147483");
		refundData.setSellerRefundAmount(0.01);
		refundData.setSellerRefundStatus("SUCCESS");

		alipayRefundData.add(refundData);

		AlipayPaymentTransactionModel transaction = new AlipayPaymentTransactionModel();
		transaction.setAlipayCode("123456");
		transaction.setRequestId("000001");
		transaction.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
		transaction.setOrder(orderModel);

		modelService.save(transaction);

		AlipayPaymentTransactionEntryModel entry = new AlipayPaymentTransactionEntryModel();
		entry.setPaymentTransaction(transaction);
		entry.setType(PaymentTransactionType.REFUND_STANDALONE);
		entry.setTransactionStatus(TransactionStatus.REVIEW.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()) + "11");

		modelService.save(entry);
		modelService.refresh(transaction);

		Map<OrderModel, Boolean> refundStatus = spy.updateForRefundNotification(alipayRefundData);

		Optional<AlipayPaymentTransactionEntryModel> refundEntryOptional = spy.getPaymentTransactionEntry(orderModel,
				TransactionStatus.ACCEPTED, PaymentTransactionType.REFUND_STANDALONE);
		assertTrue(refundEntryOptional.isPresent());
		AlipayPaymentTransactionEntryModel refundEntry = refundEntryOptional.get();

		assertEquals(PaymentTransactionType.REFUND_STANDALONE, refundEntry.getType());
		assertEquals(transaction, refundEntry.getPaymentTransaction());
		assertEquals(TransactionStatus.ACCEPTED.name(), refundEntry.getTransactionStatus());
		assertEquals("SUCCESS" + "; Refund Batch No: " + "20060702001", refundEntry.getTransactionStatusDetails());
		assertEquals(String.valueOf(paymentTransactionKeyGenerator.generate()) + "11", refundEntry.getCode());

		assertEquals(refundStatus.size(), 1);
		assertTrue(refundStatus.get(orderModel));

	}

	@Test
	public void test_Get_Payment_Transaction_With_Capture_Entry_Successfully()
	{
		AlipayPaymentTransactionEntryModel entry = new AlipayPaymentTransactionEntryModel();
		entry.setPaymentTransaction(alipayPaymentTransactionModel);
		entry.setType(PaymentTransactionType.CAPTURE);
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()) + "22");
		modelService.save(entry);
		modelService.refresh(alipayPaymentTransactionModel);

		Optional<AlipayPaymentTransactionModel> result = defaultAlipayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(orderModel, TransactionStatus.ACCEPTED);

		assertTrue(result.isPresent());
		AlipayPaymentTransactionModel resultTransaction = result.get();
		assertEquals(alipayPaymentTransactionModel, resultTransaction);
		assertEquals("10000", resultTransaction.getAlipayCode());
		assertEquals("000001", resultTransaction.getRequestId());
	}

	@Test
	public void test_Get_Payment_Transaction_With_Capture_Entry_Failed()
	{
		AlipayPaymentTransactionEntryModel entry = new AlipayPaymentTransactionEntryModel();
		entry.setPaymentTransaction(alipayPaymentTransactionModel);
		entry.setType(PaymentTransactionType.PARTIAL_CAPTURE);
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()) + "33");
		modelService.save(entry);
		modelService.refresh(alipayPaymentTransactionModel);

		Optional<AlipayPaymentTransactionModel> result = defaultAlipayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(orderModel, TransactionStatus.ACCEPTED);

		assertFalse(result.isPresent());
	}

}
