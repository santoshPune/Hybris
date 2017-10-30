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
package de.hybris.platform.customerticketingc4cintegration.converters.populators;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerticketingc4cintegration.NotesComparator;
import de.hybris.platform.customerticketingc4cintegration.SitePropsHolder;
import de.hybris.platform.customerticketingc4cintegration.constants.Customerticketingc4cintegrationConstants;
import de.hybris.platform.customerticketingc4cintegration.data.Note;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.data.TicketEventData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import org.apache.commons.collections.CollectionUtils;

/**
 * ServiceRequestData -> TicketData populator, used for converting c4c data object to hybris one.
 *
 * @param <SOURCE extends ServiceRequestData>
 * @param <TARGET extends TicketData>
 */
public class C4C2YTicketPopulator<SOURCE extends ServiceRequestData, TARGET extends TicketData> implements
		Populator<SOURCE, TARGET>
{
	@Resource
	private SitePropsHolder sitePropsHolder;
	@Resource
	private Map<StatusData, List<StatusData>> validTransitions;
	@Resource
	private Map<String, StatusData> statusMapping;

	@Resource
	private CustomerFacade customerFacade;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		target.setSubject(source.getName());
		target.setId(source.getObjectID());

		target.setCreationDate(parseDate(source.getCreationDateTime()));
		target.setLastModificationDate(parseDate(source.getLastChangeDateTime()));

		if (sitePropsHolder.isB2C())
		{
			target.setCustomerId(source.getExternalCustomerID());
		}
		else
		{
			target.setCustomerId(source.getExternalContactID());
		}

		if (CollectionUtils.isNotEmpty(source.getRelatedTransactions()))
		{
			target.setCartId(source.getRelatedTransactions().get(0).getID());
		}

		final List<TicketEventData> ticketEvents = new ArrayList<TicketEventData>();
		if (CollectionUtils.isNotEmpty(source.getNotes()))
		{
			final DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
			final List<TicketEventData> collected = source
					.getNotes()
					.stream()
					.filter(n -> n.getParentObjectID() != null)
					.sorted(new NotesComparator())
					.map(note -> {
						final TicketEventData ticketEventData = new TicketEventData();
						formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
						ticketEventData.setStartDateTime(parseDate(note.getCreatedOn()));
						ticketEventData.setText(note.getText());
						ticketEventData.setAuthor(getCreatedBy(note));

						final StringBuilder textBuilder = new StringBuilder();
						textBuilder.append(getCreatedBy(note));
						textBuilder.append(" ").append(Localization.getLocalizedString("text.supporttickets.history.on")).append(" ")
								.append(note.getCreatedOn()).append("\n").append(note.getText());
						ticketEventData.setDisplayText(textBuilder.toString());

						ticketEvents.add(ticketEventData);

						return ticketEventData;
					}).collect(Collectors.toList());
			target.setTicketEvents(collected);
		}
		target.setStatus(statusMapping.get(source.getStatusCode()));
		final List<StatusData> transitionStatuuses = validTransitions.get(target.getStatus());
		target.setAvailableStatusTransitions(transitionStatuuses == null ? Collections.EMPTY_LIST : transitionStatuuses);
	}

	public Date parseDate(final String date)
	{
		return date != null && date.contains("(") && date.contains(")") ? new Date(Long.valueOf(
				date.substring(date.indexOf("(") + 1, date.indexOf(")"))).longValue()) : null;
	}

	protected String getCreatedBy(Note note)
	{
		return Config.getParameter("customerticketingc4cintegration.displayname").equalsIgnoreCase(note.getCreatedBy())
				? (customerFacade.getCurrentCustomer().getFirstName() +" "+ customerFacade.getCurrentCustomer().getLastName())
				: Customerticketingc4cintegrationConstants.DEFAULT_AGENT_NAME;
	}
}
