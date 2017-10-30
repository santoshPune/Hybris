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
// In the SmartEdit Application Context
window.smartedit = {
    i18nAPIRoot: "somepath"
};

angular
    .module('editorEnablerServiceTestFixture', ['ngMockE2E', 'coretemplates', 'editorModalServiceModule', 'sharedDataServiceModule', 'renderServiceModule', 'genericEditorModule', 'backendMocks', 'eventServiceModule', 'resourceLocationsModule'])
    .controller('editorEnablerServiceE2ETestController', function(editorModalService, sharedDataService, restServiceFactory, $window, $httpBackend, $q, $log, ITEMS_RESOURCE_URI) {
        restServiceFactory.setDomain('thedomain');
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            }
        });

        this.clickEditButton = function(componentType, componentId) {
            editorModalService.open(componentType, componentId).then(function() {
                $log.debug("Editor modal closed.");
            }, function() {
                $log.debug("Editor modal dismissed.");
            });
        };

        this.mockBackendToPassSave = function() {
            $httpBackend.whenPUT(new RegExp(ITEMS_RESOURCE_URI + "/.*")).respond(function(method, url, data, headers) {
                component = JSON.parse(data);
                return [204, component];
            });
        };

        this.mockBackendToFailSave = function() {
            $httpBackend.whenPUT(new RegExp(ITEMS_RESOURCE_URI + "/.*")).respond(function(method, url, data, headers) {
                var errors = {
                    "errors": [{
                        "message": "This field cannot contain special characters",
                        "reason": "missing",
                        "subject": "headline",
                        "subjectType": "parameter",
                        "type": "ValidationError"
                    }, {
                        "message": "This field cannot contain special characters",
                        "reason": "missing",
                        "subject": "name",
                        "subjectType": "parameter",
                        "type": "ValidationError"
                    }, {
                        "message": "This field cannot contain special characters",
                        "reason": "missing",
                        "subject": "uid",
                        "subjectType": "parameter",
                        "type": "ValidationError"
                    }]
                };
                return [400, errors];
            });
        };

        this.mockBackendToTriggerUnrelatedValidationErrors = function() {
            $httpBackend.whenPUT(new RegExp(ITEMS_RESOURCE_URI + "/.*")).respond(function(method, url, data, headers) {
                var errors = {
                    "errors": [{
                        "message": "This is an unrelated validation error",
                        "reason": "missing",
                        "subject": "headline",
                        "subjectType": "parameter",
                        "type": "ValidationError"
                    }]
                };
                return [400, errors];
            });
        };
    });
