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
package de.hybris.platform.sap.productconfig.runtime.interf;

import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;


/**
 * Create instances of all configuration model elements.
 */
public interface ConfigModelFactory
{
	/**
	 * Create an instance of the <code>ConfigModel</code>
	 *
	 * @return an instance of the configuration model
	 */
	public ConfigModel createInstanceOfConfigModel();

	/**
	 * Create an instance of the <code>InstanceModel</code>
	 *
	 * @return an instance of the instance model
	 */
	public InstanceModel createInstanceOfInstanceModel();

	/**
	 * Create an instance of the <code>CsticModel</code>
	 *
	 * @return an instance of the characteristic model
	 */
	public CsticModel createInstanceOfCsticModel();

	/**
	 * Create an instance of the <code>CsticValueModel</code>
	 *
 	 * @param valueType
	 *           The containing Cstic value type
	 * @return an instance of the characteristic value model
	 */
	public CsticValueModel createInstanceOfCsticValueModel(int valueType);

	/**
	 * Create an instance of the <code>CsticGroupModel</code>
	 *
	 * @return an instance of the characteristic group model
	 */
	public CsticGroupModel createInstanceOfCsticGroupModel();


	/**
	 * Create an instance of the <code>PriceModel</code>
	 *
	 * @return an instance of the price model
	 */
	public PriceModel createInstanceOfPriceModel();


	/**
	 * Create an instance of the <code>PriceModel</code>
	 *
	 * @return an instance of the price model
	 */
	public PriceModel getZeroPriceModel();
}
