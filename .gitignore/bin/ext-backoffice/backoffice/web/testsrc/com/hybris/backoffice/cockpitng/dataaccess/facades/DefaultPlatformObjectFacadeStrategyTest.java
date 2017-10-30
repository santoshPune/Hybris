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
package com.hybris.backoffice.cockpitng.dataaccess.facades;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.DefaultPlatformObjectFacadeStrategy;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultPlatformObjectFacadeStrategyTest
{
	@Mock
	private ModelService modelService;
	@Mock
	private LabelService labelService;
	@Mock
	private ItemModificationHistoryService itemModificationHistoryService;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private TypeService typeService;

	@Mock
	private DataAttribute dataAttribute;
	@Mock
	private ItemModelInternalContext itemModelContext;

	@InjectMocks
	private DefaultPlatformObjectFacadeStrategy strategy;

	@Test(expected = ObjectNotFoundException.class)
	public void testLoad() throws ObjectNotFoundException
	{
		final UserModel user = new UserModel();
		user.setName("Test User");

		when(modelService.get(PK.parse("1234"))).thenReturn(user);
		when(labelService.getObjectLabel(any())).thenReturn(StringUtils.EMPTY);

		strategy.setModelService(modelService);
		strategy.setLabelService(labelService);

		// Test we get the same user
		Assert.assertEquals(user, strategy.load("1234", null));

		// Test that an unknown pk will return null
		Assert.assertNull(strategy.load("9999", null));
		Assert.assertNull(strategy.load(null, null));

		// load method should have thrown an exception
		strategy.load("", null);
	}

	@Test
	public void testDeleteSuccess() throws ObjectNotFoundException
	{
		final UserModel user = new UserModel();
		user.setName("Test User");

		doNothing().when(modelService).remove(user);
		when(labelService.getObjectLabel(any())).thenReturn(StringUtils.EMPTY);

		strategy.setModelService(modelService);
		strategy.setLabelService(labelService);
		try
		{
			strategy.delete(user, null);
		}
		catch (final ObjectDeletionException ex)
		{
			Assert.fail();
		}

		verify(modelService).remove(user);

	}

	@Test(expected = ObjectDeletionException.class)
	public void testDeleteException() throws ObjectDeletionException
	{
		final UserModel user = new UserModel();
		user.setName("Test User");
		doThrow(new ModelRemovalException("Cannot delete object: ", null)).when(modelService).remove(user);
		final DefaultPlatformObjectFacadeStrategy strategy = new DefaultPlatformObjectFacadeStrategy();
		strategy.setModelService(modelService);
		strategy.delete(user, null);
		verify(modelService).remove(user);
	}

	@Test
	public void testSavingObjectWhenSomePrivateAttributesAreAvailable() throws TypeNotFoundException
	{
		// given
		final ProductModel product = new ProductModel(itemModelContext);
		final String code = "code";
		final String catalog = "catalog";
		final String identifier = "identifier";
		when(itemModelContext.getDirtyAttributes()).thenReturn(Stream.of(code, catalog, identifier).collect(Collectors.toSet()));
		final DataType datatype = mock(DataType.class);
		when(datatype.getAttribute(code)).thenReturn(dataAttribute);
		when(datatype.getAttribute(catalog)).thenReturn(null);
		when(datatype.getAttribute(identifier)).thenReturn(dataAttribute);
		when(modelService.getModelType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(datatype);

		// when
		try
		{
			strategy.save(product, null);
		}
		catch (final ObjectSavingException e)
		{
			Assert.fail("Product should be saved successfully.");
		}

		// then
		verify(modelService).getAttributeValue(product, code);
		verify(modelService, never()).getAttributeValue(product, catalog);
		verify(modelService).getAttributeValue(product, identifier);
	}

	@Test
	public void testBulkDeletionMethod()
	{
		// given
		final Collection<UserModel> users = getUserModelCollection();

		strategy.setModelService(modelService);
		strategy.setLabelService(labelService);

		// when
		final ObjectFacadeOperationResult<UserModel> result = strategy.delete(users, null);

		// then
		verify(modelService).removeAll(users);
		assertThat(result.countSuccessfulObjects()).isEqualTo(2);
		assertThat(result.countFailureObjects()).isEqualTo(0);
	}

	@Test
	public void testBulkDeletionMethodInCaseOfFailedDeletion()
	{
		// given
		final Collection<UserModel> users = getUserModelCollection();

		strategy.setModelService(modelService);
		strategy.setLabelService(labelService);
		doThrow(new ModelRemovalException("message", null)).when(modelService).removeAll(users);

		// when
		final ObjectFacadeOperationResult<UserModel> result = strategy.delete(users, null);

		// then
		verify(modelService).removeAll(users);
		assertThat(result.countSuccessfulObjects()).isEqualTo(0);
		assertThat(result.countFailureObjects()).isEqualTo(2);
	}

	private Collection<UserModel> getUserModelCollection()
	{
		final UserModel user1 = new UserModel();
		final UserModel user2 = new UserModel();
		user1.setName("Test User1");
		user2.setName("Test User2");
		return Arrays.asList(user1, user2);
	}

}
