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
angular.module('personalizationsmarteditRestServiceModule', ['restServiceFactoryModule', 'personalizationsmarteditCommons', 'personalizationsmarteditContextServiceModule'])
    .factory('personalizationsmarteditRestService', ['restServiceFactory', 'personalizationsmarteditUtils', 'personalizationsmarteditContextService', function(restServiceFactory, personalizationsmarteditUtils, personalizationsmarteditContextService) {

        var CUSTOMIZATIONS = "/personalizationwebservices/v1/catalogs/:catalogId/catalogVersions/:catalogVersion/customizations";
        var CUSTOMIZATION = CUSTOMIZATIONS + "/:customizationCode";

        var CUSTOMIZATION_PACKAGES = "/personalizationwebservices/v1/catalogs/:catalogId/catalogVersions/:catalogVersion/customizationpackages";
        var CUSTOMIZATION_PACKAGE = CUSTOMIZATION_PACKAGES + "/:customizationCode";

        var VARIATIONS = CUSTOMIZATION + "/variations";
        var VARIATION = VARIATIONS + "/:variationCode";

        var ACTIONS = VARIATION + "/actions";
        var ACTION = ACTIONS + "/:actionId";

        var CXCMSC_ACTIONS_FROM_VARIATIONS = "/personalizationwebservices/v1/query/cxcmscomponentsfromvariations";

        var PREVIEWTICKET = "/previewwebservices/v1/preview/:ticketId";
        var SEGMENTS = "/personalizationwebservices/v1/segments";

        var CATALOGS = "/cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:catalogVersion/items";
        var CATALOG = CATALOGS + "/:itemId";

        var ADD_CONTAINER = "/personalizationwebservices/v1/query/cxReplaceComponentWithContainer";

        var COMPONENT_TYPES = '/cmswebservices/v1/types?category=COMPONENT';

        var VARIATION_FOR_CUSTOMIZATION_FIELDS = ["active", "enabled", "code", "name", "rank"];
        var VARIATION_FOR_CUSTOMIZATION_TRIGGERS_FIELD = "triggers(code,groupBy,segments)";
        var VARIATION_FOR_CUSTOMIZATION_ACTIONS_FIELDS = "actions";

        var extendRequestParamObjWithCatalogAwarePathVariables = function(requestParam) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var catalogAwareParams = {
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion
            };
            requestParam = angular.extend(requestParam, catalogAwareParams);
            return requestParam;
        };

        var extendRequestParamObjWithCustomizatonCode = function(requestParam, customizatiodCode) {
            var customizationCodeParam = {
                customizationCode: customizatiodCode
            };
            requestParam = angular.extend(requestParam, customizationCodeParam);
            return requestParam;
        };

        var getParamsAction = function(oldComponentId, newComponentId, slotId, containerId, customizationId, variationId) {
            var entries = [];
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "oldComponentId", oldComponentId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "newComponentId", newComponentId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "slotId", slotId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "containerId", containerId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "variationId", variationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "customizationId", customizationId);
            return {
                "params": {
                    "entry": entries
                }
            };
        };

        var getPathVariablesObjForModifyingActionURI = function(customizationId, variationId, actionId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            return {
                customizationCode: customizationId,
                variationCode: variationId,
                actionId: actionId,
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion
            };
        };

        var restService = {};

        restService.getCustomizations = function(filter) {
            filter = filter || {};

            var restService = restServiceFactory.get(CUSTOMIZATIONS);
            var requestParams = {};

            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams);

            requestParams.pageSize = filter.currentSize || 10;
            requestParams.currentPage = filter.currentPage || 0;

            if (angular.isDefined(filter.code)) {
                requestParams.code = filter.code || '';
            }
            if (angular.isDefined(filter.pageId)) {
                requestParams.pageId = filter.pageId || '';
            }
            if (angular.isDefined(filter.name)) {
                requestParams.name = filter.name || '';
            }
            if (angular.isDefined(filter.negatePageId)) {
                requestParams.negatePageId = filter.negatePageId || 'false';
            }
            return restService.get(requestParams);
        };

        restService.getComponenentsIdsForVariation = function(customizationId, variationId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();

            var restService = restServiceFactory.get(CXCMSC_ACTIONS_FROM_VARIATIONS);
            var entries = [];
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "customization", customizationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "variations", variationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "catalog", experienceData.catalogDescriptor.catalogId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "catalogVersion", experienceData.catalogDescriptor.catalogVersion);
            var requestParams = {
                "params": {
                    "entry": entries
                }
            };
            return restService.save(requestParams);
        };

        restService.getPreviewTicket = function() {
            var previewTicketData = personalizationsmarteditContextService.getSePreviewData();
            var restService = restServiceFactory.get(PREVIEWTICKET, "ticketId");
            var previewTicket = {
                ticketId: previewTicketData.previewTicketId
            };
            return restService.get(previewTicket);
        };

        restService.updatePreviewTicket = function(previewTicket) {
            var restService = restServiceFactory.get(PREVIEWTICKET, "ticketId");
            return restService.update(previewTicket);
        };

        restService.createPreviewTicket = function(previewTicket) {
            var previewRESTService = restServiceFactory.get(PREVIEWTICKET);
            return previewRESTService.save(previewTicket);
        };

        restService.getSegments = function(filter) {
            var restService = restServiceFactory.get(SEGMENTS);
            return restService.get(filter);
        };

        restService.getCustomization = function(customizationCode) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");

            var requestParams = {
                customizationCode: customizationCode
            };

            return restService.get(extendRequestParamObjWithCatalogAwarePathVariables(requestParams));
        };

        restService.createCustomization = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION_PACKAGES);

            return restService.save(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.updateCustomization = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");
            customization.customizationCode = customization.code;
            return restService.update(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.updateCustomizationPackage = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION_PACKAGE, "customizationCode");
            customization.customizationCode = customization.code;
            return restService.update(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.deleteCustomization = function(customizationCode) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");

            var requestParams = {
                customizationCode: customizationCode
            };

            return restService.remove(extendRequestParamObjWithCatalogAwarePathVariables(requestParams));
        };

        restService.getVariation = function(customizationCode, variationCode) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");
            var requestParams = {
                variationCode: variationCode
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);
            return restService.get(requestParams);
        };

        restService.editVariation = function(customizationCode, variation) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");

            variation = extendRequestParamObjWithCatalogAwarePathVariables(variation);
            variation = extendRequestParamObjWithCustomizatonCode(variation, customizationCode);
            variation.variationCode = variation.code;
            return restService.update(variation);
        };

        restService.deleteVariation = function(customizationCode, variationCode) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");
            var requestParams = {
                variationCode: variationCode
            };

            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);

            return restService.remove(requestParams);
        };

        restService.createVariationForCustomization = function(customizationCode, variation) {
            var restService = restServiceFactory.get(VARIATIONS);

            variation = extendRequestParamObjWithCatalogAwarePathVariables(variation);
            variation = extendRequestParamObjWithCustomizatonCode(variation, customizationCode);

            return restService.save(variation);
        };

        restService.getVariationsForCustomization = function(customizationCode, filter) {
            var restService = restServiceFactory.get(VARIATIONS);

            var requestParams = {};
            var fieldsForRequest = [];
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);

            fieldsForRequest = fieldsForRequest.concat(VARIATION_FOR_CUSTOMIZATION_FIELDS);
            if (filter.includeActions) {
                fieldsForRequest.push(VARIATION_FOR_CUSTOMIZATION_ACTIONS_FIELDS);
            }
            if (filter.includeTriggers) {
                fieldsForRequest.push(VARIATION_FOR_CUSTOMIZATION_TRIGGERS_FIELD);
            }

            requestParams.fields = "variations(" + fieldsForRequest.join() + ")";

            return restService.get(requestParams);
        };

        restService.getComponents = function(filter) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOGS);
            var compomentsParams = {
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion,
                siteId: experienceData.siteDescriptor.uid
            };
            compomentsParams = angular.extend(compomentsParams, filter);
            return restService.get(compomentsParams);
        };

        restService.replaceComponentWithContainer = function(componentId, slotId) {
            var restService = restServiceFactory.get(ADD_CONTAINER);
            var catalogParams = extendRequestParamObjWithCatalogAwarePathVariables({});
            var requestParams = getParamsAction(componentId, null, slotId, null, null, null);
            personalizationsmarteditUtils.pushToArrayIfValueExists(requestParams.params.entry, "catalog", catalogParams.catalogId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(requestParams.params.entry, "catalogVersion", catalogParams.catalogVersion);

            return restService.save(requestParams);
        };

        restService.addActionToContainer = function(componentId, containerId, customizationId, variationId) {
            var restService = restServiceFactory.get(ACTIONS);
            var pathVariables = getPathVariablesObjForModifyingActionURI(customizationId, variationId);
            var requestParams = {
                "type": "cxCmsActionData",
                "containerId": containerId,
                "componentId": componentId
            };
            requestParams = angular.extend(requestParams, pathVariables);
            return restService.save(requestParams);
        };

        restService.editAction = function(customizationId, variationId, actionId, newComponentId) {
            var restService = restServiceFactory.get(ACTION, "actionId");

            var requestParams = getPathVariablesObjForModifyingActionURI(customizationId, variationId, actionId);

            return restService.get(requestParams).then(function successCallback(actionInfo) {
                actionInfo = angular.extend(actionInfo, requestParams);
                actionInfo.componentId = newComponentId;
                return restService.update(actionInfo);
            });
        };

        restService.deleteAction = function(customizationId, variationId, actionId) {
            var restService = restServiceFactory.get(ACTION, "actionId");

            var requestParams = getPathVariablesObjForModifyingActionURI(customizationId, variationId, actionId);

            return restService.remove(requestParams);
        };

        restService.createComponent = function(componentName, componentType, pageId, slotId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOGS);
            var requestParams = {
                name: componentName,
                slotId: slotId,
                pageId: pageId,
                position: 1,
                typeCode: componentType,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);

            return restService.save(requestParams);
        };

        restService.removeComponent = function(componentId, slotId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOG, "itemId");
            var requestParams = {
                slotId: slotId,
                itemId: componentId,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);

            return restService.remove(requestParams);
        };

        restService.getComponent = function(itemId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOG, "itemId");
            var requestParams = {
                itemId: itemId,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            return restService.get(requestParams);
        };

        restService.getNewComponentTypes = function() {
            var restService = restServiceFactory.get(COMPONENT_TYPES);
            return restService.get();
        };

        return restService;
    }]);
