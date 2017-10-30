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
package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CustomerRuleParameterValueMapperTest
{
	private static final String ANY_STRING = "anyString";

	@Rule
	public final ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private UserService userService;

	@Mock
	private CustomerModel customer;

	@InjectMocks
	private final CustomerRuleParameterValueMapper mapper = new CustomerRuleParameterValueMapper();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void nullTestFromString() throws RuleParameterValueMapperException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		mapper.fromString(null);
	}

	@Test
	public void nullTestToString() throws RuleParameterValueMapperException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		mapper.toString(null);
	}

	@Test
	public void noCustomerFoundTest() throws RuleParameterValueMapperException
	{
		//given
		BDDMockito.given(userService.getUserForUID(Mockito.anyString(), Mockito.eq(CustomerModel.class))).willReturn(null);

		//expect
		expectedException.expect(RuleParameterValueMapperException.class);

		//when
		mapper.fromString(ANY_STRING);
	}

	@Test
	public void mappedCustomerTest() throws RuleParameterValueMapperException
	{
		//given
		BDDMockito.given(userService.getUserForUID(Mockito.anyString(), Mockito.eq(CustomerModel.class))).willReturn(customer);

		//when
		final CustomerModel mappedCustomer = mapper.fromString(ANY_STRING);

		//then
		Assert.assertEquals(customer, mappedCustomer);
	}

	@Test
	public void objectToStringTest() throws RuleParameterValueMapperException
	{
		//given
		givenStringRepresentationAttribute();

		//when
		final String stringRepresentation = mapper.toString(customer);

		//then
		Assert.assertEquals(ANY_STRING, stringRepresentation);
	}

	private void givenStringRepresentationAttribute()
	{
		BDDMockito.given(customer.getUid()).willReturn(ANY_STRING);
	}

}
