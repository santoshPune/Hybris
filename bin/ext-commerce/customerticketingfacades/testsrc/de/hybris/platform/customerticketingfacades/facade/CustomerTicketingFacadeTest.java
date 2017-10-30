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
package de.hybris.platform.customerticketingfacades.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.jalo.AbstractTicketsystemTest;
import de.hybris.platform.ticket.service.TicketService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Test cases for the Customer Ticket Facade
 *
 */
public class CustomerTicketingFacadeTest extends AbstractTicketsystemTest
{
	private final String subject = "Ticket subject";
	private final String note = "Hello";

	@Resource(name = "userService")
	private UserService userService;

	@Resource
	private Map<String, StatusData> statusMapping;

	@Resource
	private Map<StatusData, List<StatusData>> validTransitions;

	@Autowired
	private TicketService ticketService;

	@Resource(name = "ticket_open")
	private StatusData open;

	@Resource(name = "defaultTicketFacade")
	private TicketFacade ticketFacade;

	@Resource
	private BaseSiteService baseSiteService;

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		importCsv("/customerticketingfacades/test/testCustomerTicketing.impex", "UTF-8");

		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testSite");
		baseSiteService.setCurrentBaseSite(baseSite, true);

		userService.setCurrentUser(testUser);
	}

	@Test
	public void testCreateTicket()
	{
		final TicketData ticketData = new TicketData();
		ticketData.setSubject(subject);
		ticketData.setMessage(note);
		ticketData.setTicketCategory(TicketCategory.ENQUIRY);
		ticketData.setCustomerId(testUser.getUid());
		ticketData.setStatus(open);

		final TicketData ticketData1 = ticketFacade.createTicket(ticketData);
		assertNotNull(ticketData1.getId());

		assertEquals(ticketData1.getStatus().getId(), open.getId());
		assertEquals(ticketData1.getSubject(), "Ticket subject");

		final TicketData ticket = ticketFacade.getTicket(ticketData1.getId());

		assertNotNull(ticket);
		assertEquals(ticket.getSubject(), "Ticket subject");
		if (ticket.getTicketEvents() == null || ticket.getTicketEvents().isEmpty())
		{
			assertTrue(ticket.getMessageHistory().contains(note));
		}
		else
		{
			assertTrue(ticket.getTicketEvents().get(0).getText().contains(note));
		}
	}

	@Test
	public void testGetTicketsForCustomerOrderByModifiedTime()
	{
		final TicketData ticketDataOne = new TicketData();
		ticketDataOne.setSubject(subject);
		ticketDataOne.setMessage(note);
		ticketDataOne.setTicketCategory(TicketCategory.COMPLAINT);
		ticketDataOne.setCustomerId(testUser.getUid());

		final TicketData ticketData1 = ticketFacade.createTicket(ticketDataOne);
		assertNotNull(ticketData1.getId());


		final TicketData ticketDataTwo = new TicketData();
		ticketDataTwo.setSubject(subject);
		ticketDataTwo.setMessage(note);
		ticketDataTwo.setTicketCategory(TicketCategory.ENQUIRY);
		ticketDataTwo.setCustomerId(testUser.getUid());

		final TicketData ticketData2 = ticketFacade.createTicket(ticketDataTwo);
		assertNotNull(ticketData2.getId());

		final PageableData pageableData = new PageableData();
		pageableData.setPageSize(5);
		pageableData.setCurrentPage(0);
		pageableData.setSort("LastChangeDateTime");

		final SearchPageData<TicketData> tickets = ticketFacade.getTickets(pageableData);

		// first one must be after second one and so on. So latest on bottom, newest on top
		assertTrue(tickets.getResults().get(0).getLastModificationDate()
				.after(tickets.getResults().get(1).getLastModificationDate()));
	}

	@Ignore
	@Test
	public void testUpdateTicketsForCustomerOrderByModifiedTime()
	{
		final TicketData ticketData = new TicketData();
		ticketData.setSubject(subject);
		ticketData.setMessage(note);
		ticketData.setCustomerId(testUser.getUid());
		ticketData.setTicketCategory(TicketCategory.PROBLEM);

		final TicketData createdTicket = ticketFacade.createTicket(ticketData);
		assertNotNull(createdTicket.getId());

		final StatusData statusData = new StatusData();
		statusData.setId("COMPLETED");
		createdTicket.setStatus(statusData);
		createdTicket.setMessage("Let me take a look at this product");
		ticketFacade.updateTicket(createdTicket);

		final TicketData updatedTicket = ticketFacade.getTicket(createdTicket.getId());

		assertTrue(updatedTicket.getStatus().getId().equalsIgnoreCase("COMPLETED"));
		assertTrue(updatedTicket.getMessageHistory().contains("Let me take a look at this product"));
	}
}