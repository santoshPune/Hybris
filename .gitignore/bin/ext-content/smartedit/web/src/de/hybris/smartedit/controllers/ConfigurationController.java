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
package de.hybris.smartedit.controllers;

import de.hybris.platform.smartedit.dto.ConfigurationData;
import de.hybris.platform.smartedit.dto.ConfigurationDataListWsDto;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController("configurationController")
@Component
@RequestMapping("/configuration")
public class ConfigurationController {

	private static final String HEADER_AUTHORIZATION = "Authorization";

	private final HttpGETGateway httpGETGateway;
	private final HttpPOSTGateway httpPOSTGateway;
	private final HttpPUTGateway httpPUTGateway;
	private final HttpDELETEGateway httpDELETEGateway;

	private final ObjectMapper mapper = new ObjectMapper();

	private static final String KEY = "key";
	private static final String VALUE = "value";
	private static final String SECURED = "secured";
	private static final String DUMMY_TOKEN = "dummy";

	@Autowired
	public ConfigurationController(HttpGETGateway httpGETGateway,HttpPOSTGateway httpPOSTGateway,HttpPUTGateway httpPUTGateway,HttpDELETEGateway httpDELETEGateway)
	{
		this.httpGETGateway = httpGETGateway;
		this.httpPOSTGateway = httpPOSTGateway;
		this.httpPUTGateway = httpPUTGateway;
		this.httpDELETEGateway = httpDELETEGateway;
	}

	@RequestMapping(value = "", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Collection<ConfigurationData>> getConfiguration(HttpServletRequest request) throws IOException
	{
		String data = null;
		try
		{
			data = httpGETGateway.loadAll("", getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		final ConfigurationDataListWsDto configurations = mapper.readValue(data, ConfigurationDataListWsDto.class);
		return new ResponseEntity<>(configurations.getConfigurations(), HttpStatus.OK);
	}

	@RequestMapping(value = "", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ConfigurationData> saveConfiguration(@RequestBody Map<String, String> payload,
			HttpServletRequest request) throws IOException
	{
		payload.remove(SECURED);
		String stringPayload = null;
		try
		{
			stringPayload = httpPOSTGateway.save(payload, getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(mapper.readValue(stringPayload, ConfigurationData.class), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{key:.+}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ConfigurationData> updateConfiguration(@RequestBody Map<String, String> payload,
			@PathVariable("key") String id, HttpServletRequest request) throws IOException
	{
		final Optional<ConfigurationData> optional = getConfiguration(request).getBody().stream().filter((configurationData) -> {
			return (configurationData.getKey().equals(id));
		}).findFirst();

		if (optional.isPresent())
		{
			String stringPayload = null;
			try
			{
				stringPayload = httpPUTGateway.update(payload, id, getAuthorization(request));
			}
			catch (final HttpClientErrorException e)
			{
				return new ResponseEntity<>(e.getStatusCode());
			}
			return new ResponseEntity<>(mapper.readValue(stringPayload, ConfigurationData.class), HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{key:.+}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Void> deleteConfiguration(@PathVariable("key") String id, HttpServletRequest request) throws IOException
	{
		final Map<String, String> configuration = new HashMap<>();
		getConfiguration(request).getBody().stream().forEach((configurationData) -> {
			if(configurationData.getKey().equals(id)) {
		    		configuration.put(KEY, configurationData.getKey());
		    		configuration.put(VALUE, configurationData.getValue());
		    	}
		});
		try
		{
			httpDELETEGateway.delete(configuration, id, getAuthorization(request));
		}
		catch (final HttpClientErrorException e)
		{
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	protected String getAuthorization(HttpServletRequest request)
	{
		String auth = request.getHeader(HEADER_AUTHORIZATION);
		if (auth == null)
		{
			auth = DUMMY_TOKEN;
		}
		return auth;
	}
}
