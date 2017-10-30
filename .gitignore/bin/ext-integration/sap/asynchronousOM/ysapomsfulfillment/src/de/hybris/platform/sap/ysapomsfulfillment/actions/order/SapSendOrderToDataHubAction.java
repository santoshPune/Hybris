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
package de.hybris.platform.sap.ysapomsfulfillment.actions.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import sap.hybris.integration.models.enums.SAPOrderStatus;
import sap.hybris.integration.models.services.SapPlantLogSysOrgService;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubHelper;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.sap.sapmodel.model.SAPOrderModel;
import de.hybris.platform.sap.sapmodel.model.SAPPlantLogSysOrgModel;
import de.hybris.platform.sap.sapmodel.model.SAPSalesOrganizationModel;
import de.hybris.platform.sap.ysapomsfulfillment.constants.YsapomsfulfillmentConstants;
import de.hybris.platform.sap.ysapomsfulfillment.model.SapConsignmentProcessModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.task.RetryLaterException;

/**
 * Triggers sending of the order to the SAP back-end as an IDoc. Retry logic is
 * applied in case the sending did not succeed. Order export status is set to
 * EXPORTED/NOTEXPORTED accordingly.
 */
public class SapSendOrderToDataHubAction extends AbstractSimpleDecisionAction<OrderProcessModel> {

	private static final Logger LOG = Logger.getLogger(SapSendOrderToDataHubAction.class);

	private int maxRetries = YsapomsfulfillmentConstants.DEFAULT_MAX_RETRIES;
	private int retryDelay = YsapomsfulfillmentConstants.DEFAULT_RETRY_DELAY;
	private SendToDataHubHelper<OrderModel> sendOrderToDataHubHelper;
	private OrderHistoryService orderHistoryService;
	private OrderService orderService;
	private SapPlantLogSysOrgService sapPlantLogSysOrgService;
	private TimeService timeService;
	private BusinessProcessService businessProcessService;

	@Override
	public Transition executeAction(final OrderProcessModel process) throws RetryLaterException {

		final OrderModel order = process.getOrder();

		List<SendToDataHubResult> results = new ArrayList<>();
		List<OrderHistoryEntryModel> orderHistoryList = new ArrayList<>();
		List<SAPOrderModel> sapOrders = new ArrayList<>();

		groupOrderConsignments(order)//
				.stream()//
				.forEach(consignmentSet -> sendOrder(order, results, orderHistoryList, consignmentSet, sapOrders));

		if (!results.isEmpty() && results.stream().allMatch(result -> result.isSuccess())) {

			saveOrderHistory(orderHistoryList);
			saveSapOrders(order, sapOrders);
			setOrderStatus(order, ExportStatus.EXPORTED);
			startSapConsignmentSubProcess(order.getConsignments(), process);
			resetEndMessage(process);

			return Transition.OK;

		} else {

			setOrderStatus(order, ExportStatus.NOTEXPORTED);
			handleRetry(process);

			return Transition.NOK;
		}

	}

	/**
	 * Group the order consignments by SAP logical system and SAP sales
	 * organization
	 * 
	 * @param order
	 * @return
	 */
	protected Set<Set<ConsignmentModel>> groupOrderConsignments(OrderModel order) {

		Set<ConsignmentModel> orderConsignments = order.getConsignments();

		/*
		 * Sample data
		 * 
		 * { LOGSYS001={ 1000_10_00=[ConsignmentModel (8797240100819),
		 * ConsignmentModel (8797240100888)], 2000_20_00=[ConsignmentModel
		 * (8797240068051)]}
		 * 
		 * LOGSYS002={ 3000_30_00=[ConsignmentModel (8797240100777),
		 * ConsignmentModel (8797240100666)], 2000_20_00=[ConsignmentModel
		 * (8797240068555)]}
		 * 
		 * }
		 */
		Map<String, Map<String, Set<ConsignmentModel>>> mapByLogSysSalesOrg = orderConsignments.stream().collect(
				Collectors.groupingBy(consignment -> getSapLogSysName(order, consignment), Collectors.groupingBy(
						consignment -> getSapSalesOrgName(order, consignment), Collectors.toSet())));

		Set<Set<ConsignmentModel>> consignmentSets = new HashSet<Set<ConsignmentModel>>();

		mapByLogSysSalesOrg
				.entrySet()
				.stream()
				.forEach(
						entryKey -> entryKey.getValue().entrySet().stream()
								.forEach(entryValue -> consignmentSets.add(entryValue.getValue())));
		/*
		 * Sample data
		 * 
		 * [ [ConsignmentModel (8797240100819), ConsignmentModel
		 * (8797240100888)], [ConsignmentModel(8797240068051)],
		 * 
		 * [ConsignmentModel (8797240100777), ConsignmentModel (8797240100666)],
		 * [ConsignmentModel(8797240068555)], ],
		 */
		return consignmentSets;
	}

	/**
	 * @param order
	 * @param consignment
	 * @return
	 */
	protected String getSapSalesOrgName(OrderModel order, ConsignmentModel consignment) {

		SAPSalesOrganizationModel salesOrganization = getSapPlantLogSysOrgService().getSapSalesOrganizationForPlant(
				order.getStore(), consignment.getWarehouse().getCode());

		if (salesOrganization.getSalesOrganization() != null) {

			return new StringBuilder(salesOrganization.getSalesOrganization())
					.append(YsapomsfulfillmentConstants.UNDERSCORE).append(salesOrganization.getDistributionChannel())
					.append(YsapomsfulfillmentConstants.UNDERSCORE).append(salesOrganization.getDivision()).toString();
		}

		LOG.error(String.format("SAP Sales Organization is missing!"));
		return YsapomsfulfillmentConstants.MISSING_SALES_ORG;
	}

	/**
	 * @param order
	 * @param consignment
	 * @return
	 */
	protected String getSapLogSysName(OrderModel order, ConsignmentModel consignment) {
		String sapLogicalSystemName = getSapPlantLogSysOrgService().getSapLogicalSystemForPlant(order.getStore(),
				consignment.getWarehouse().getCode()).getSapLogicalSystemName();

		if (sapLogicalSystemName != null) {
			return sapLogicalSystemName;
		}

		LOG.error(String.format("SAP logical system is missing!"));
		return YsapomsfulfillmentConstants.MISSING_LOG_SYS;
	}

	/**
	 * Send SAP order to data-hub
	 * 
	 * @param order
	 * @param results
	 * @param orderHistoryList
	 * @param consignments
	 */
	protected void sendOrder(final OrderModel order, List<SendToDataHubResult> results,
			List<OrderHistoryEntryModel> orderHistoryList, final Set<ConsignmentModel> consignments,
			final List<SAPOrderModel> sapOrders) {

		// Read customizing data from the base store configuration
		SAPPlantLogSysOrgModel sapPlantLogSysOrgModel = getSapPlantLogSysOrgService().getSapPlantLogSysOrgForPlant(
				order.getStore(), consignments.stream().findFirst().get().getWarehouse().getCode());

		final OrderHistoryEntryModel orderHistoryEntry = createOrderHistory(order, sapPlantLogSysOrgModel.getLogSys()
				.getSapLogicalSystemName());

		createSapOrders(sapOrders, orderHistoryEntry, consignments);

		// Clone the order
		OrderModel clonedOrder = getOrderService().clone(null, null, order,
				orderHistoryEntry.getPreviousOrderVersion().getVersionID());

		List<AbstractOrderEntryModel> orderEntries = new ArrayList<AbstractOrderEntryModel>();

		// Copy order entries
		consignments.stream().forEach(
				consignment -> consignment.getConsignmentEntries().stream()
						.forEach(entry -> orderEntries.add(entry.getOrderEntry())));

		// Set cloned order attributes
		clonedOrder.setConsignments(consignments);
		clonedOrder.setSapLogicalSystem(sapPlantLogSysOrgModel.getLogSys().getSapLogicalSystemName());
		clonedOrder.setSapSalesOrganization(sapPlantLogSysOrgModel.getSalesOrg());
		clonedOrder.setEntries(orderEntries);
		clonedOrder.setPaymentTransactions(order.getPaymentTransactions());

		// Send cloned order to data-hub
		results.add(sendOrderToDataHubHelper.createAndSendRawItem(clonedOrder));

		// Add send SAP order action to the order history
		orderHistoryList.add(orderHistoryEntry);
	}

	/**
	 * Create SAP Orders
	 * 
	 * @param sapOrderList
	 */
	protected void createSapOrders(final List<SAPOrderModel> sapOrders, final OrderHistoryEntryModel orderHistoryEntry,
			final Set<ConsignmentModel> consignments) {

		SAPOrderModel sapOrder = getModelService().create(SAPOrderModel.class);
		sapOrder.setCode(orderHistoryEntry.getPreviousOrderVersion().getVersionID());
		sapOrder.setConsignments(consignments);
		sapOrders.add(sapOrder);
	}

	/**
	 * Save SAP orders
	 * 
	 * @param order
	 * @param sapOrders
	 */
	protected void saveSapOrders(final OrderModel order, final List<SAPOrderModel> sapOrders) {

		sapOrders.stream().forEach(sapOrder -> {
			sapOrder.setSapOrderStatus(SAPOrderStatus.SENT_TO_ERP);
			sapOrder.setOrder(order);
			getModelService().save(sapOrder);
		});

	}

	/**
	 * Save order history
	 * 
	 * @param orderHistoryList
	 */
	protected void saveOrderHistory(List<OrderHistoryEntryModel> orderHistoryList) {

		orderHistoryList.stream().forEach(entry -> {
			entry.setTimestamp(getTimeService().getCurrentTime());
			getModelService().save(entry);
		});
	}

	/**
	 * Create an entry to the order history for every SAP order sent to data-hub
	 * 
	 * @param order
	 * @param logicalSystem
	 * @return
	 */
	protected OrderHistoryEntryModel createOrderHistory(final OrderModel order, String logicalSystem) {

		final OrderModel snapshot = getOrderHistoryService().createHistorySnapshot(order);
		getOrderHistoryService().saveHistorySnapshot(snapshot);

		final OrderHistoryEntryModel historyEntry = getModelService().create(OrderHistoryEntryModel.class);

		historyEntry.setOrder(order);
		historyEntry.setPreviousOrderVersion(snapshot);

		historyEntry.setDescription(String.format("SAP sales document %s has been sent to the logical system %s",
				snapshot.getVersionID(), logicalSystem));

		return historyEntry;

	}

	/**
	 * * Start an SAP consignment process for every hybris consignment
	 * 
	 * @param consignments
	 * @param process
	 */
	protected void startSapConsignmentSubProcess(final Collection<ConsignmentModel> consignments,
			final OrderProcessModel process) {

		for (final ConsignmentModel consignment : consignments) {

			String processCode = new StringBuilder(process.getOrder().getCode())//
					.append(YsapomsfulfillmentConstants.SAP_CONS)//
					.append(consignment.getCode())//
					.toString();

			final SapConsignmentProcessModel subProcess = getBusinessProcessService()
					.<SapConsignmentProcessModel> createProcess(processCode,
							YsapomsfulfillmentConstants.CONSIGNMENT_SUBPROCESS_NAME);

			subProcess.setParentProcess(process);
			subProcess.setConsignment(consignment);
			save(subProcess);

			getBusinessProcessService().startProcess(subProcess);
			LOG.info(String.format("SAP consignment sub-process %s has started!", subProcess.getCode()));
		}
	}

	/**
	 * @param process
	 * @throws RetryLaterException
	 */
	protected void handleRetry(final OrderProcessModel process) throws RetryLaterException {
		if (process.getSendOrderRetryCount() < getMaxRetries()) {
			final OrderModel order = process.getOrder();
			process.setSendOrderRetryCount(process.getSendOrderRetryCount() + 1);
			modelService.save(process);
			final RetryLaterException ex = new RetryLaterException(String.format(
					"Sending to backend failed for order %s!", order.getCode()));
			ex.setDelay(getRetryDelay());
			ex.setRollBack(false);
			throw ex;
		}
	}

	/**
	 * @param order
	 * @param exportStatus
	 */
	protected void setOrderStatus(final OrderModel order, final ExportStatus exportStatus) {
		order.setExportStatus(exportStatus);
		save(order);
	}

	/**
	 * @param process
	 */
	protected void resetEndMessage(final OrderProcessModel process) {
		final String endMessage = process.getEndMessage();
		if (YsapomsfulfillmentConstants.ERROR_END_MESSAGE.equals(endMessage)) {
			process.setEndMessage("");
			modelService.save(process);
		}
	}

	/**
	 * @param process
	 * @throws RetryLaterException
	 */
	protected void tryAgainNextTime(final OrderProcessModel process) throws RetryLaterException {
		final OrderModel order = process.getOrder();
		process.setSendOrderRetryCount(process.getSendOrderRetryCount() + 1);
		modelService.save(process);
		final RetryLaterException ex = new RetryLaterException(String.format("Sending to backend failed for order %s!",
				order.getCode()));
		ex.setDelay(getRetryDelay());
		ex.setRollBack(false);
		throw ex;
	}

	protected TimeService getTimeService() {
		return timeService;
	}

	@Required
	public void setTimeService(TimeService timeService) {
		this.timeService = timeService;
	}

	protected SapPlantLogSysOrgService getSapPlantLogSysOrgService() {
		return sapPlantLogSysOrgService;
	}

	@Required
	public void setSapPlantLogSysOrgService(SapPlantLogSysOrgService sapPlantLogSysOrgService) {
		this.sapPlantLogSysOrgService = sapPlantLogSysOrgService;
	}

	protected OrderService getOrderService() {
		return orderService;
	}

	@Required
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	protected OrderHistoryService getOrderHistoryService() {
		return orderHistoryService;
	}

	@Required
	public void setOrderHistoryService(OrderHistoryService orderHistoryService) {
		this.orderHistoryService = orderHistoryService;
	}

	@SuppressWarnings("javadoc")
	protected SendToDataHubHelper<OrderModel> getSendOrderToDataHubHelper() {
		return sendOrderToDataHubHelper;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setSendOrderToDataHubHelper(final SendToDataHubHelper<OrderModel> sendOrderAsCSVHelper) {
		this.sendOrderToDataHubHelper = sendOrderAsCSVHelper;
	}

	@SuppressWarnings("javadoc")
	protected int getMaxRetries() {
		return maxRetries;
	}

	@SuppressWarnings("javadoc")
	public void setMaxRetries(final int maxRetries) {
		this.maxRetries = maxRetries;
	}

	@SuppressWarnings("javadoc")
	protected int getRetryDelay() {
		return retryDelay;
	}

	@SuppressWarnings("javadoc")
	public void setRetryDelay(final int retryDelay) {
		this.retryDelay = retryDelay;
	}

	protected BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

}
