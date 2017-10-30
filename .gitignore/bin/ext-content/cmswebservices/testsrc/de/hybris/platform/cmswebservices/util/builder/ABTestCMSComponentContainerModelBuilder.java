package de.hybris.platform.cmswebservices.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;

import java.util.List;


public class ABTestCMSComponentContainerModelBuilder
{
	private final ABTestCMSComponentContainerModel model;

	private ABTestCMSComponentContainerModelBuilder()
	{
		model = new ABTestCMSComponentContainerModel();
	}

	private ABTestCMSComponentContainerModelBuilder(ABTestCMSComponentContainerModel model)
	{
		this.model = model;
	}

	private ABTestCMSComponentContainerModel getModel()
	{
		return this.model;
	}

	public static ABTestCMSComponentContainerModelBuilder aModel()
	{
		return new ABTestCMSComponentContainerModelBuilder();
	}

	public static ABTestCMSComponentContainerModelBuilder fromModel(ABTestCMSComponentContainerModel model)
	{
		return new ABTestCMSComponentContainerModelBuilder(model);
	}

	public ABTestCMSComponentContainerModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public ABTestCMSComponentContainerModelBuilder withCatalogVersion(CatalogVersionModel cv)
	{
		getModel().setCatalogVersion(cv);
		return this;
	}

	public ABTestCMSComponentContainerModelBuilder withSimpleCMSComponent(final List<SimpleCMSComponentModel> value)
	{
		getModel().setSimpleCMSComponents(value);
		return this;
	}

	public ABTestCMSComponentContainerModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public ABTestCMSComponentContainerModel build()
	{
		return this.getModel();
	}
}
