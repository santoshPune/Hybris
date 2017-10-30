/**
 *
 */
package de.hybris.platform.personalizationcms.strategy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


@UnitTest
public class DefaultCxCmsContainerDefaultComponentStrategyTest
{
	private final DefaultCxCmsContainerDefaultComponentStrategy defaultCxCmsContainerDefaultComponentStrategy = new DefaultCxCmsContainerDefaultComponentStrategy();

	@Test
	public void shouldReturnEmptyListForNull()
	{
		final List<AbstractCMSComponentModel> components = defaultCxCmsContainerDefaultComponentStrategy
				.getDisplayComponentsForContainer(null);
		Assert.assertNotNull(components);
		Assert.assertTrue(components.isEmpty());
	}

	@Test
	public void shouldReturnEmptyListForNoDefaultComponent()
	{
		final List<AbstractCMSComponentModel> components = defaultCxCmsContainerDefaultComponentStrategy
				.getDisplayComponentsForContainer(new CxCmsComponentContainerModel());
		Assert.assertNotNull(components);
		Assert.assertTrue(components.isEmpty());
	}

	@Test
	public void shouldReturnDefaultComponent()
	{
		final SimpleCMSComponentModel component = new SimpleCMSComponentModel();
		final CxCmsComponentContainerModel container = new CxCmsComponentContainerModel();
		container.setDefaultCmsComponent(component);
		final List<AbstractCMSComponentModel> components = defaultCxCmsContainerDefaultComponentStrategy
				.getDisplayComponentsForContainer(container);
		Assert.assertNotNull(components);
		Assert.assertTrue(components.size() == 1);
		Assert.assertEquals(component, components.get(0));
	}

}
