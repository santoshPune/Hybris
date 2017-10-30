package com.hybris.backoffice.cockpitng.dataaccess.facades.object;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.DefaultItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;


@RunWith(MockitoJUnitRunner.class)
public class DefaultItemModificationHistoryServiceTest
{

	@InjectMocks
	private DefaultItemModificationHistoryService modificationHistoryService;

	@Mock
	private TypeFacade typeFacade;
	@Mock
	private ModelService modelService;

	@Mock
	private DataAttribute dataAttribute;
	@Mock
	private ItemModelContextImpl itemModelContext;
	@Mock
	private ModelValueHistory modelValueHistory;

	@Test
	public void testCreatingModificationInfoWhenPrivateAttributesAreAccessible() throws TypeNotFoundException
	{
		// given
		final ProductModel product = new ProductModel(itemModelContext);
		final String code = "code";
		final String catalog = "catalog";
		final String identifier = "identifier";

		final DataType datatype = mock(DataType.class);
		when(datatype.getAttribute(code)).thenReturn(dataAttribute);
		when(datatype.getAttribute(catalog)).thenReturn(null);
		when(datatype.getAttribute(identifier)).thenReturn(dataAttribute);
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(datatype);
		when(itemModelContext.getValueHistory()).thenReturn(modelValueHistory);
		when(Boolean.valueOf(modelValueHistory.isDirty())).thenReturn(Boolean.TRUE);
		when(modelValueHistory.getDirtyAttributes()).thenReturn(Stream.of(code, catalog, identifier).collect(Collectors.toSet()));

		// when
		modificationHistoryService.createModificationInfo(product);

		// then
		verify(modelService).getAttributeValue(product, code);
		verify(modelService, never()).getAttributeValue(product, catalog);
		verify(modelService).getAttributeValue(product, identifier);
	}
}
