package de.hybris.platform.chineselogisticaddon.decorator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;


@IntegrationTest
public class ChineseLogisticCellDecoratorTest extends ServicelayerTransactionalTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void Setup() throws Exception
	{
		importCsv("/test/preparations.csv", "utf-8");
	}

	@Test
	public void testDecoratorInsertNonDuplicateComponent() throws Exception
	{
		importCsv("/test/testInsertNonDuplicate.csv", "utf-8");
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlot} WHERE {uid} = ?uid");
		final Map<String, Object> params = new HashMap<>();

		params.put("uid", "BodyContent-orderdetail");
		fQuery.addQueryParameters(params);

		final SearchResult<ContentSlotModel> result = flexibleSearchService.search(fQuery);
		final ContentSlotModel orderDetailBodyContentSlot = result.getResult().get(0);
		final List<String> orderDetailComponents = orderDetailBodyContentSlot.getCmsComponents().stream()
				.map(AbstractCMSComponentModel::getUid).collect(Collectors.toList());

		Assert.assertEquals(5, orderDetailComponents.size());
		Assert.assertTrue(orderDetailComponents.contains("testComponentG"));
		Assert.assertEquals(2, orderDetailComponents.indexOf("testComponentG"));
	}

	@Test
	public void testDecoratorInsertDuplicateComponent() throws Exception
	{
		importCsv("/test/testInsertDuplicate.csv", "utf-8");
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlot} WHERE {uid} = ?uid");
		final Map<String, Object> params = new HashMap<>();

		params.put("uid", "BodyContent-orderdetail");
		fQuery.addQueryParameters(params);

		final SearchResult<ContentSlotModel> result = flexibleSearchService.search(fQuery);
		final ContentSlotModel orderDetailBodyContentSlot = result.getResult().get(0);
		final List<String> orderDetailComponents = orderDetailBodyContentSlot.getCmsComponents().stream()
				.map(AbstractCMSComponentModel::getUid).collect(Collectors.toList());

		Assert.assertEquals(5, orderDetailComponents.size());
		Assert.assertTrue(orderDetailComponents.contains("testComponentG"));
		Assert.assertEquals(3, orderDetailComponents.indexOf("testComponentG"));
	}
}