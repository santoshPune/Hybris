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
var setupComponentServiceMock, setupController;

var componentMenuSetup = function() {
    setupComponentServiceMock = function() {
        componentService = jasmine.createSpyObj('ComponentService', ['loadComponentTypes', 'addExistingComponent', 'addNewComponent']);
        componentService.loadComponentTypes.andCallFake(function(component) {
            return {
                then: function() {
                    return component;
                }
            };
        });
        componentService.addExistingComponent.andReturn();
        componentService.addNewComponent.andReturn();

        return componentService;
    };

    setupComponentMenuController = function($controller, $scope, componentService) {
        return $controller('ComponentMenuController as menuCtrl', {
            $scope: directiveScope,
            ComponentService: componentService,
        });

    };

};
