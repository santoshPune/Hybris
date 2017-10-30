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
package ywebservicespackage.controllers;

import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Sets;

import jersey.repackaged.com.google.common.collect.Lists;
import ywebservicespackage.data.UserData;
import ywebservicespackage.data.UserDataList;
import ywebservicespackage.dto.SampleWsDTO;
import ywebservicespackage.dto.TestMapWsDTO;
import ywebservicespackage.dto.UserWsDTO;
import ywebservicespackage.dto.UsersListWsDTO;
import ywebservicespackage.facades.SampleFacades;


/**
 * Sample Controller
 */
@Controller
@RequestMapping(value = "/sample")
public class SampleController
{
	public static final String DEFAULT_FIELD_SET = "DEFAULT";

	@Resource
	private SampleFacades sampleFacades;

	@Resource(name = "sampleWsDTOValidator")
	private Validator sampleWsDTOValidator;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	/**
	 * Sample method returning Cache-Control header and using Path Variable</br>
	 * Example :</br>
	 * GET http://localhost:9001/ywebservices/sample/dto/sampleValue
	 *
	 * @param pathVariable
	 *           - sample path variable parameter
	 * @return SampleWsDTO object filled with pathVariable value
	 */
	@RequestMapping(value = "/dto/{pathVariable}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
	@ResponseBody
	public SampleWsDTO getSampleWsDTO(@PathVariable final String pathVariable)
	{
		return sampleFacades.getSampleWsDTO(YSanitizer.sanitize(pathVariable));
	}

	/**
	 * Sample method showing how to validate object given in POST body parameter<br/>
	 * Example :</br>
	 * URL : http://localhost:9001/ywebservices/sample/dto</br>
	 * Method : POST</br>
	 * Header : Content-Type=application/json</br>
	 * POST body parameter :{ "value" : "sampleValue"}</br>
	 *
	 * @param sampleWsDTO
	 *           - Request body parameter (DTO in xml or json format)</br>
	 * @return - The same object, which was send in POST body</br>
	 *
	 */
	@RequestMapping(value = "/dto", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public SampleWsDTO postSampleWsDTO(@RequestBody final SampleWsDTO sampleWsDTO)
	{
		validate(sampleWsDTO, "sampleWsDTO", sampleWsDTOValidator);
		return sampleWsDTO;
	}


	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	/**
	 * Request handler for list response. Retrieves all user from userService and maps Collection of UserModel to
	 * UserListWsDTO Mapping is done according to configuration in WEB-INF/config/field-mapping.xml Sample url's:
	 * <ul>
	 * <li>http://localhost:9001/ywebservices/sample/users
	 * <li>http://localhost:9001/ywebservices/sample/users?fields=users(info)
	 * <li>http://localhost:9001/ywebservices/sample/users?fields=users(BASIC)
	 * <li>http://localhost:9001/ywebservices/sample/users?fields=users(DEFAULT)
	 * <li>http://localhost:9001/ywebservices/sample/users?fields=users(FULL)
	 * <li>http://localhost:9001/ywebservices/sample/users?fields=users(firstName,addresses(street))
	 * </ul>
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public UsersListWsDTO getUsers(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final List<UserData> users = sampleFacades.getUsers();
		final UserDataList userList = new UserDataList();
		userList.setUsers(users);
		return dataMapper.map(userList, UsersListWsDTO.class, fields);
	}

	/**
	 * Request handler for particular user. Retrieves single user from userService and maps UserModel to UserWsDTO
	 * Mapping is done according to configuration in WEB-INF/config/field-mapping.xml Sample url's:
	 * <ul>
	 * <li>http://localhost:9001/ywebservices/sample/users/user1
	 * <li>http://localhost:9001/ywebservices/sample/users/user1?fields=info
	 * <li>http://localhost:9001/ywebservices/sample/users/user1?fields=BASIC
	 * <li>http://localhost:9001/ywebservices/sample/users/user1?fields=DEFAULT
	 * <li>http://localhost:9001/ywebservices/sample/users/user1?fields=FULL
	 * <li>http://localhost:9001/ywebservices/sample/users/user1?fields=firstName,addresses(street)
	 * </ul>
	 */
	@Secured(
	{ "ROLE_CLIENT" })
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserWsDTO getUsers(@PathVariable final String id,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserData data = sampleFacades.getUser(id);
		return dataMapper.map(data, UserWsDTO.class, fields);
	}

	/**
	 * Request handler for getting object with map. Created to test adapters for particular fields.
	 *
	 * Url: http://localhost:9001/ywebservices/sample/map
	 *
	 */
	@RequestMapping(value = "/map", method = RequestMethod.GET)
	@ResponseBody
	public TestMapWsDTO getTestMap()
	{
		return sampleFacades.getMap();
	}


	@RequestMapping(value = "plain/string", method = RequestMethod.POST)
	@ResponseBody
	public String getString(@RequestBody final String val)
	{
		return YSanitizer.sanitize(val) + "1";
	}


	@RequestMapping(value = "plain/long", method = RequestMethod.POST)
	@ResponseBody
	public Long getLong(@RequestBody final Long value)
	{
		return Long.valueOf(value.longValue() + 1);
	}


	@RequestMapping(value = "plain/double", method = RequestMethod.POST)
	@ResponseBody
	public double getDouble(@RequestBody final double value)
	{
		return value + 1;
	}


	@RequestMapping(value = "plain/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getGetList()
	{
		return Lists.newArrayList("new String", Double.valueOf(0.123d));
	}

	@RequestMapping(value = "plain/map", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getMap()
	{
		final Map<String, Object> map = new HashMap<>();
		map.put("a", "Ala");
		map.put("b", Integer.valueOf(1));
		map.put("c", Sets.newHashSet("a", "b", "c"));
		return map;
	}

}
