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
package de.hybris.platform.commercefacades.converter.impl;

import de.hybris.platform.commercefacades.converter.ModifiableConfigurablePopulator;
import de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModification;
import de.hybris.platform.converters.impl.DefaultModifableConfigurablePopulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * Modifiable variant of the {@link DefaultConfigurablePopulator}. Beans of this type can be modified via configuration
 * of {@link ConfigurablePopulatorModification} beans.
 *
 * @deprecated use {@link DefaultModifableConfigurablePopulator} instead. Will be removed in version 6.2. This
 *             functionally this class was replaced by
 *             {@link de.hybris.platform.converters.impl.ModifableConfigurablePopulatorPostProcessor}
 */
@Deprecated
@SuppressWarnings("deprecation")
public abstract class AbstractModifiableConfigurablePopulator<SOURCE, TARGET, OPTION> extends
		DefaultModifableConfigurablePopulator<SOURCE, TARGET, OPTION> implements
		ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>, ApplicationContextAware, BeanFactoryAware, BeanNameAware
{
	private static final Logger LOG = Logger.getLogger(AbstractModifiableConfigurablePopulator.class);

	private ApplicationContext applicationContext;
	private ConfigurableListableBeanFactory beanFactory;
	private String beanName;

	private final List<ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>> modifications = new ArrayList<>();
	private boolean modified = false;

	@Override
	public void applyModification(final ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification)
	{
		super.applyModification(modification);
	}

	@Deprecated
	@Override
	public void addModification(final ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification)
	{
		modifications.add(modification);
	}

	@Deprecated
	public List<ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>> getModifications()
	{
		return modifications;
	}

	@Deprecated
	protected List<ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>> getParentModifications()
	{
		final List<ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>> modifications = new ArrayList<>();

		if (getApplicationContext() == null || getBeanFactory() == null || getBeanName() == null)
		{
			LOG.warn("Unable to resolve parent modifications. Spring context not initialized properly.");
		}
		else
		{
			final BeanDefinition beanDefinition = getBeanFactory().getBeanDefinition(getBeanName());
			final String parentBeanName = beanDefinition.getParentName();

			if (StringUtils.hasText(parentBeanName))
			{
				final Object parentObject = getApplicationContext().getBean(parentBeanName);
				if (parentObject instanceof AbstractModifiableConfigurablePopulator)
				{
					final AbstractModifiableConfigurablePopulator parent = (AbstractModifiableConfigurablePopulator) parentObject;
					modifications.addAll(parent.getParentModifications());
					modifications.addAll(parent.getModifications());
				}
			}
		}

		return modifications;
	}

	@Deprecated
	protected void applyModifications()
	{
		// apply all modifications registered for parent beans
		for (final ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> parentModification : getParentModifications())
		{
			applyModification(parentModification);
		}

		// apply modifications registered for this bean
		for (final ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification : getModifications())
		{
			applyModification(modification);
		}

		modified = true;
	}

	@Deprecated
	protected boolean isModified()
	{
		return modified;
	}

	@Deprecated
	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Deprecated
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	protected ConfigurableListableBeanFactory getBeanFactory()
	{
		return beanFactory;
	}

	@Deprecated
	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		Assert.isTrue(beanFactory instanceof ConfigurableListableBeanFactory,
				"Parameter [beanFactory] must implement ConfigurableListableBeanFactory: " + beanFactory.getClass());

		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	@Deprecated
	protected String getBeanName()
	{
		return beanName;
	}

	@Deprecated
	@Override
	public void setBeanName(final String beanName)
	{
		this.beanName = beanName;
	}
}
