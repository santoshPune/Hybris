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
/**
 * @ngdoc overview
 * @name slotVisibilityComponentModule
 * @description
 *
 * The slot visibility component module provides a directive and controller to display the hidden components of a specified content slot. 
 */
angular.module('slotVisibilityComponentModule', ['editorModalServiceModule'])
    /**
     * @ngdoc controller
     * @name slotVisibilityComponentModule.controller:slotVisibilityComponentController
     *
     * @description
     * The slot visibility component controller is responsible for controlling the directive, as well as adding 
     * methods to interact with other modules and directives. The controller provides a function to open the generic editor modal.
     *
     * @param {Object} editorModalService the editor modal service instance
     */
    .controller('slotVisibilityComponentController', function(editorModalService) {
        this.imageRoot = '/cmssmartedit/images';

        this.openEditorModal = function() {
            editorModalService.openAndRerenderSlot(this.component.typeCode, this.component.uid, this.slotId);
        };
    })
    /**
     * @ngdoc directive
     * @name slotVisibilityComponentModule.directive:slotVisibilityComponent
     *
     * @description
     * The slot visibility component directive is used to display information about a specified hidden component.
     * It receives the component on its scope and it binds it to its own controller.
     */
    .directive('slotVisibilityComponent', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotVisibility/slotVisibilityComponentTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,
            controller: 'slotVisibilityComponentController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                component: '=',
                slotId: '@'
            }
        };
    });
