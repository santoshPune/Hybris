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
package de.hybris.platform.ordermanagementwebservices.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants;
import de.hybris.platform.ordermanagementwebservices.dto.returns.CancelReturnRequestWsDTO;
import de.hybris.platform.webservicescommons.webservices.AbstractWebServicesTest;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;


@IntegrationTest
@ContextConfiguration(locations = { "classpath:/ordermanagementwebservices-spring-test.xml" })
public class BaseOrderManagementWebservicesIntegrationTest extends AbstractWebServicesTest
{
	@Resource
	private EnumerationService enumerationService;

	protected static final String DEFAULT_FIELDS = "DEFAULT";
	protected static final String DEFAULT_CURRENT_PAGE = "0";
	protected static final String DEFAULT_PAGE_SIZE = "100";

	protected Response getAllOrderByDefault()
	{
		return getDefaultRestCall("orders", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getOrderByCode(final String code)
	{
		return getDefaultRestCall("orders/" + code, "FULL", DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getOrderByStatus(final String status)
	{
		return getDefaultRestCall("orders/statuses/" + status, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getOrderStatusByDefault()
	{
		return getDefaultRestCall("orders/statuses", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response getReturnCancellationReasons()
	{
		return getDefaultRestCall("returns/cancel-reasons", DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
	}

	protected Response postCancelReturnRequestByDefault(CancelReturnRequestWsDTO cancelReturnRequestWsDTO)
	{
		return postDefaultRestCall("returns/cancel", DEFAULT_FIELDS, cancelReturnRequestWsDTO);
	}

	/**
	 * Builds a GET rest call.
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param currentPage
	 * 			the current page of the request
	 * @param pageSize
	 * 			total page size
	 * @return
	 */
	protected Response getDefaultRestCall(String path, String fields, String currentPage, String pageSize)
	{
		final Response result = webResource
				.path(path)
				.queryParam("fields", fields)
				.queryParam("currentPage", currentPage)
				.queryParam("pageSize", pageSize)
				.request().accept(MediaType.APPLICATION_XML).get();
		result.bufferEntity();
		return result;
	}

	/**
	 * Builds a POST rest call with the return type <T>.
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param requestBodyWsDTO
	 * 			the dto object sent with the request
	 * @param responseType
	 * 			type of class to return
	 * @param <T>
	 *    		type of the body object
	 * @return the request class to return after the execution of the call
	 */
	protected <T> T postDefaultRestCall(String path, String fields, T requestBodyWsDTO, Class<T> responseType)
	{
		return webResource
				.path(path)
				.queryParam("fields", fields)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON), responseType);
	}

	/**
	 * Builds a POST rest call.
	 *
	 * @param path
	 * 			the url for the call
	 * @param fields
	 * 			contains pagination information
	 * @param requestBodyWsDTO
	 * 			the dto object sent with the request
	 * @param <T>
	 *    		type of the body object
	 * @return the result of the call
	 */
	protected <T> Response postDefaultRestCall(String path, String fields, T requestBodyWsDTO)
	{
		final Response result = webResource
				.path(path)
				.queryParam("fields", fields)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		return result;
	}

	/**
	 * this method is to build the rest call with null body for post with the return type Response
	 *
	 * @param path
	 * @return {@link Response}
	 */
	protected Response postEmptyBodyRestCall(final String path)
	{
		return webResource
				.path(path)
				.request()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
	}

	/**
	 * this method is use to valid the response in xpath
	 *
	 * @param result
	 * @param xpath
	 * @return
	 */
	protected String getNodeByXpath(Response result, String xpath)
	{
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(result.readEntity(String.class))));
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			return xp.evaluate(xpath, document.getDocumentElement());
		}
		catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getExtensionName()
	{
		return OrdermanagementwebservicesConstants.EXTENSIONNAME;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
