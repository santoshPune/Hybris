/**
 *
 */
package de.hybris.platform.sap.productconfig.facades.filters;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationTestData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 *
 */
@UnitTest
public class VisibleValueFilterTest
{
	private CsticModel csticModel;

	private VisibleValueFilter visibleFilter;

	@Before
	public void setup()
	{
		visibleFilter = new VisibleValueFilter();
		csticModel = new CsticModelImpl();

	}

	@Test
	public void testVisibleFilter_visible()
	{
		final String name = "abc";
		csticModel = ConfigurationTestData.createSTRCstic();


		ConfigurationTestData.setAssignedValue(name, csticModel, CsticValueModel.AUTHOR_USER);

		final List<CsticValueModel> filterResult = visibleFilter.filter(csticModel, FilterTestData.setVisibleFilter());

		assertEquals(csticModel.getAssignedValues().size(), filterResult.size());

	}


	@Test
	public void testVisibleFilter_notVisible()
	{
		final String name = "abc";
		csticModel = ConfigurationTestData.createSTRCstic();
		csticModel.setVisible(false);
		ConfigurationTestData.setAssignedValue(name, csticModel, CsticValueModel.AUTHOR_USER);

		final List<CsticValueModel> filterResult = visibleFilter.filter(csticModel, FilterTestData.setVisibleFilter());

		assertEquals(0, filterResult.size());

	}

	@Test
	public void testVisibleFilter_visible_filterNotactive()
	{
		final String name = "abc";
		csticModel = ConfigurationTestData.createSTRCstic();


		ConfigurationTestData.setAssignedValue(name, csticModel, CsticValueModel.AUTHOR_USER);

		final List<CsticValueModel> filterResult = visibleFilter.filter(csticModel, FilterTestData.setNoFilters());

		assertEquals(csticModel.getAssignedValues().size(), filterResult.size());

	}


	@Test
	public void testVisibleFilter_notVisible_filterNotActive()
	{
		final String name = "abc";
		csticModel = ConfigurationTestData.createSTRCstic();
		csticModel.setVisible(false);
		ConfigurationTestData.setAssignedValue(name, csticModel, CsticValueModel.AUTHOR_USER);

		final List<CsticValueModel> filterResult = visibleFilter.filter(csticModel, FilterTestData.setNoFilters());

		assertEquals(csticModel.getAssignedValues().size(), filterResult.size());

	}


}
