/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.platform.servicelayer.model;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.AddressModel;

import org.junit.Test;


@UnitTest
public class ItemModelNullDecoratorUnitTest
{

	@Test
	public void shouldTranslateDefaultNullToDefinedValueInGetter()
	{
		// assuming that Address.duplicate is having a <nulldecorator>Boolean.FALSE</nulldecorator> in place:
		// given
		final AddressModel address = new AddressModel();

		// then
		assertThat(address.getDuplicate()).isEqualTo(Boolean.FALSE);
	}

	@Test
	public void shouldTranslateNullSetterToDefinedValueInGetter()
	{
		// assuming that Address.duplicate is having a <nulldecorator>Boolean.FALSE</nulldecorator> in place:
		// given
		final AddressModel address = new AddressModel();
		
		// when
		address.setDuplicate(null);

		// then
		assertThat(address.getDuplicate()).isEqualTo(Boolean.FALSE);
	}

}
