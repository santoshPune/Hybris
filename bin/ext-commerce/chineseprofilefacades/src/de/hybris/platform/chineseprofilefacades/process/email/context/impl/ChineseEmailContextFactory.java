package de.hybris.platform.chineseprofilefacades.process.email.context.impl;

import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.process.email.context.impl.DefaultEmailContextFactory;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;

import java.util.Locale;
import java.util.Map;


public class ChineseEmailContextFactory extends DefaultEmailContextFactory
{
	private static final String MEDIA = "media";

	@Override
	protected void processProperties(final AbstractCMSComponentModel component, final Map<String, Object> componentContext)
	{
		for (final String property : getCmsComponentService().getEditorProperties(component))
		{
			try
			{
				final Object value;

				if (component instanceof CMSImageComponentModel && MEDIA.equals(property))
				{

					AbstractEmailContext context = (AbstractEmailContext) componentContext.get("parentContext");

					Locale locale = new Locale(context.getEmailLanguage().getIsocode());

					value = getModelService().getAttributeValue(component, property, locale);

				}
				else
				{
					value = getModelService().getAttributeValue(component, property);
				}

				componentContext.put(property, value);
			}
			catch (final AttributeNotSupportedException ignore)
			{
				// ignore
			}
		}
	}

}
