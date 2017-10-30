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
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;


public class CsticValueModelImpl extends BaseModelImpl implements CsticValueModel
{
	private String name;
	private String languageDependentName;
	private boolean domainValue;
	private String author;
	private String authorExternal = null;
	private boolean selectable = true;
	private PriceModel deltaPrice = PriceModel.NO_PRICE;
	private PriceModel valuePrice = PriceModel.NO_PRICE;

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	public String getLanguageDependentName()
	{
		return languageDependentName;
	}

	@Override
	public void setLanguageDependentName(final String languageDependentName)
	{
		this.languageDependentName = languageDependentName;
	}

	@Override
	public boolean isDomainValue()
	{
		return domainValue;
	}

	@Override
	public void setDomainValue(final boolean domainValue)
	{
		this.domainValue = domainValue;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder(70);
		builder.append("\nCsticValueModelImpl [name=");
		builder.append(name);
		builder.append(", languageDependentName=");
		builder.append(languageDependentName);
		builder.append(", domainValue=");
		builder.append(domainValue);
		builder.append(", author=");
		builder.append(author);
		builder.append(", deltaPrice=");
		builder.append(deltaPrice);
		builder.append(", valuePrice=");
		builder.append(valuePrice);
		builder.append(']');
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final CsticValueModelImpl other = (CsticValueModelImpl) obj;
		if (!super.equals(other))
		{
			return false;
		}

		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
			return true;
		}

		return compareName(other);
	}

	protected boolean compareName(final CsticValueModelImpl other)
	{
		if (name.equals(other.name))
		{
			return true;
		}

		return false;
	}

	@Override
	public CsticValueModel clone() //NOSONAR
	{
		//Mock tests use clone for creating instances, therefore we don't introduce another pattern here
		CsticValueModel clonedCsticValue;
		clonedCsticValue = (CsticValueModel) super.clone();
		clonedCsticValue.setDeltaPrice(deltaPrice.clone());
		clonedCsticValue.setValuePrice(valuePrice.clone());

		return clonedCsticValue;
	}

	@Override
	public String getAuthor()
	{
		return author;
	}

	@Override
	public void setAuthor(final String author)
	{
		this.author = author;
	}

	@Override
	public boolean isSelectable()
	{
		return this.selectable;
	}

	@Override
	public void setSelectable(final boolean selectable)
	{
		this.selectable = selectable;
	}

	@Override
	public String getAuthorExternal()
	{
		return authorExternal;
	}

	@Override
	public void setAuthorExternal(final String authorExternal)
	{
		this.authorExternal = authorExternal;
	}

	@Override
	public PriceModel getDeltaPrice()
	{
		return deltaPrice;
	}

	@Override
	public void setDeltaPrice(final PriceModel deltaPrice)
	{
		PriceModel newDeltaPrice = deltaPrice;
		if (newDeltaPrice == null)
		{
			newDeltaPrice = PriceModel.NO_PRICE;
		}
		this.deltaPrice = newDeltaPrice;

	}

	@Override
	public PriceModel getValuePrice()
	{
		return valuePrice;
	}

	@Override
	public void setValuePrice(final PriceModel valuePrice)
	{
		PriceModel newValuePrice = valuePrice;
		if (newValuePrice == null)
		{
			newValuePrice = PriceModel.NO_PRICE;
		}
		this.valuePrice = newValuePrice;

	}

}
