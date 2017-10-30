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
 */
package sap.hybris.integration.models.services;

import de.hybris.platform.sap.sapmodel.model.SAPLogicalSystemModel;
import de.hybris.platform.sap.sapmodel.model.SAPPlantLogSysOrgModel;
import de.hybris.platform.sap.sapmodel.model.SAPSalesOrganizationModel;
import de.hybris.platform.store.BaseStoreModel;

public interface SapPlantLogSysOrgService {
	
    /**
     * @param baseStoreModel
     * @param plantCode
     * @return SAP logical system for given base store and plant
     */
    public SAPLogicalSystemModel getSapLogicalSystemForPlant(BaseStoreModel baseStoreModel, String plantCode);
    
    /**
     * @param baseStoreModel
     * @param plantCode
     * @return SAP sales organization for given base store and plant
     */
    public SAPSalesOrganizationModel getSapSalesOrganizationForPlant(BaseStoreModel baseStoreModel, String plantCode);
    
    /**
     * @param baseStoreModel
     * @param plantCode
     * @return SAP logical system and sales organization for given base store and plant
     */
    public SAPPlantLogSysOrgModel getSapPlantLogSysOrgForPlant(BaseStoreModel baseStoreModel, String plantCode);
    
}
