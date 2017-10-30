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
angular.module('personalizationsmarteditManagementServiceModule', ['personalizationsmarteditRestServiceModule',
        'sharedDataServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditContextServiceModule'
    ])
    .factory('personalizationsmarteditManagementService', ['personalizationsmarteditRestService', function(personalizationsmarteditRestService) {
        var ManagementService = function() {};

        ManagementService.getSegments = function(filter) {
            return personalizationsmarteditRestService.getSegments(filter);
        };

        ManagementService.getCustomization = function(customizationCode) {
            return personalizationsmarteditRestService.getCustomization(customizationCode);
        };

        ManagementService.getVariation = function(customizationCode, variationCode) {
            return personalizationsmarteditRestService.getVariation(customizationCode, variationCode);
        };

        ManagementService.createCustomization = function(customization) {
            return personalizationsmarteditRestService.createCustomization(customization);
        };

        ManagementService.updateCustomizationPackage = function(customization) {
            return personalizationsmarteditRestService.updateCustomizationPackage(customization);
        };


        ManagementService.createVariationForCustomization = function(customizationCode, variation) {
            return personalizationsmarteditRestService.createVariationForCustomization(customizationCode, variation);
        };

        ManagementService.getVariationsForCustomization = function(customizationCode, filter) {
            return personalizationsmarteditRestService.getVariationsForCustomization(customizationCode, filter);
        };

        return ManagementService;
    }]);
