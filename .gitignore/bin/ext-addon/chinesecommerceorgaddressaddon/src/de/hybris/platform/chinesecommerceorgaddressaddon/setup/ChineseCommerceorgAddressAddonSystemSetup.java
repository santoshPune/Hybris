/**
 *
 */
package de.hybris.platform.chinesecommerceorgaddressaddon.setup;



import de.hybris.platform.chinesecommerceorgaddressaddon.constants.ChinesecommerceorgaddressaddonConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


@SystemSetup(extension = ChinesecommerceorgaddressaddonConstants.EXTENSIONNAME)
public class ChineseCommerceorgAddressAddonSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ChineseCommerceorgAddressAddonSystemSetup.class);
	private static final String IMPORT_COMMON_DATA = "import Common Data";

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_COMMON_DATA, "Import Common Data", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{
		if (getBooleanSystemSetupParameter(context, IMPORT_COMMON_DATA))
		{
			importCommonData(context);
		}
	}

	protected void importCommonData(final SystemSetupContext context)
	{
		final String extensionName = context.getExtensionName();
		getSetupImpexService().importImpexFile(String.format("/%s/import/common/common-addon-extra.impex", extensionName), false);
	}


	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}