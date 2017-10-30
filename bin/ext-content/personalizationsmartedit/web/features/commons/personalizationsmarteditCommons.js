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
angular.module('personalizationsmarteditCommons', [
        'alertServiceModule', 'commonsTemplates'
    ])
    .factory('personalizationsmarteditUtils', function() {
        var utils = {};

        utils.pushToArrayIfValueExists = function(array, key, value) {
            if (value) {
                array.push({
                    "key": key,
                    "value": value
                });
            }
        };

        utils.getContainerIdForElement = function(element) {
            var container = element.closest('[data-smartedit-container-id][data-smartedit-container-type="CxCmsComponentContainer"]');
            if (container.length) {
                return container.data().smarteditContainerId;
            }
            return null;
        };

        utils.getContainerIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getContainerIdForElement(element);
        };

        utils.getSlotIdForElement = function(element) {
            var slot = element.closest('[data-smartedit-component-type="ContentSlot"]');
            if (slot.length) {
                return slot.data().smarteditComponentId;
            }
            return null;
        };

        utils.getSlotIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getSlotIdForElement(element);
        };

        utils.getVariationCodes = function(variations) {
            if ((typeof variations === 'undefined') || (variations === null)) {
                return [];
            }
            var allVariationsCodes = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            });
            return allVariationsCodes;
        };

        utils.getPageId = function() {
            return /page\-([\w]+)/.exec($('iframe').contents().find('body').attr('class'))[1];
        };

        utils.getVariationKey = function(customizationId, variations) {
            if (customizationId === undefined || variations === undefined) {
                return [];
            }

            var allVariationsKeys = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            }).map(function(variationId) {
                return {
                    "variationCode": variationId,
                    "customizationCode": customizationId
                };
            });
            return allVariationsKeys;
        };

        utils.getSegmentTriggerForVariation = function(variation) {
            var triggers = variation.triggers || [];
            var segmentTriggerArr = triggers.filter(function(trigger) {
                return trigger.type === "segmentTriggerData";
            });

            if (segmentTriggerArr.length === 0) {
                return {};
            }

            return segmentTriggerArr[0];
        };

        utils.getActionIdForElement = function(element) {
            var action = element.closest('[data-smartedit-personalization-action-id]');
            if (action.length) {
                return action.data().smarteditPersonalizationActionId;
            }
            return null;
        };

        return utils;
    })
    .factory('personalizationsmarteditMessageHandler', function(alertService) {
        var sendMessage = function(message, isSuccessful) {
            alertService.pushAlerts([{
                successful: isSuccessful,
                message: message
            }]);
        };

        var messageHandler = {};
        messageHandler.sendInformation = function(informationMessage) {
            sendMessage(informationMessage, true);
        };

        messageHandler.sendError = function(errorMessage) {
            sendMessage(errorMessage, false);
        };

        return messageHandler;
    });
