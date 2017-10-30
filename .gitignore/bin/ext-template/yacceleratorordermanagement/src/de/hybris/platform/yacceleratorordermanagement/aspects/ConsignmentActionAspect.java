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
package de.hybris.platform.yacceleratorordermanagement.aspects;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterAdvice;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Aspect that will attempt to place a consignment into an "awaiting completion" state, then performStrategy a given action,
 * then place the consignment into a verification state.
 */
public class ConsignmentActionAspect implements AfterAdvice
{
	private static Logger LOGGER = LoggerFactory.getLogger(ConsignmentActionAspect.class);

	private static final String EXCEPTION_MESSAGE = "Could not process consignment with code [%s]";

	private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
	private String choice;

	/**
	 * Perform around advice.
	 *
	 * @param joinPoint
	 *           - the join point
	 * @return the remaining cancellation entries to be cancelled; never <tt>null</tt>
	 * @throws Throwable
	 *            when an exception occurs that has been cleared to be rethrown.
	 */
	public void advise(final JoinPoint joinPoint) throws Throwable
	{
		for (ConsignmentModel consignment : getConsignments(joinPoint))
		{
				LOGGER.debug("Running consignment action aspect for consignment with code: " + consignment.getCode());
				try
				{
					getConsignmentBusinessProcessService().triggerChoiceEvent(consignment,
							YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, getChoice());
				}
				catch (final BusinessProcessException e)
				{
					LOGGER.debug(EXCEPTION_MESSAGE, consignment.getCode());
					throw e;
				}
		}
	}

	/**
	 * Get the list of consignment models from the join point that have been cancelled in order to complete their
	 * processes.
	 *
	 * @param joinPoint
	 *           - the join point object
	 * @return the list of consignment models
	 */
	protected List<ConsignmentModel> getConsignments(final JoinPoint joinPoint)
	{
		OrderCancelResponse orderCancelResponse = (OrderCancelResponse) joinPoint.getArgs()[0];
		return orderCancelResponse.getOrder().getConsignments().stream()
				.filter(consignment -> consignment.getStatus().equals(ConsignmentStatus.CANCELLED) && !consignment.getConsignmentProcesses().iterator().next().isDone()).collect(Collectors.toList());
	}

	protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
	{
		return consignmentBusinessProcessService;
	}

	@Required
	public void setConsignmentBusinessProcessService(
			final WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
	{
		this.consignmentBusinessProcessService = consignmentBusinessProcessService;
	}

	protected String getChoice()
	{
		return choice;
	}

	@Required
	public void setChoice(final String choice)
	{
		this.choice = choice;
	}

}
