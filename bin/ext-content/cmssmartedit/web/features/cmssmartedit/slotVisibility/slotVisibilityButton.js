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
 * @name slotVisibilityButtonModule
 * @description
 *
 * The slot visibility button module provides a directive and controller to manage the button within the slot contextual menu 
 * and the hidden component list, which is also part of the dropdown menu associated with the directive's template.     
 */
angular.module('slotVisibilityButtonModule', ['slotVisibilityServiceModule', 'slotVisibilityComponentModule'])

/**
 * @ngdoc controller
 * @name slotVisibilityButtonModule.controller:slotVisibilityButtonController
 *
 * @description
 * The slot visibility button controller is responsible for enabling and disabling the hidden components button, 
 * as well as displaying the hidden components list. It also provides functions to open and close the hidden component list.
 *
 * @param {Object} slotVisibilityService slot visibility service instance
 * @param {Object} $scope current scope instance
 */
.controller('slotVisibilityButtonController', function(slotVisibilityService, $scope, $timeout) {
        this.buttonName = 'slotVisibilityButton';
        this.eyeOnImageUrl = '/cmssmartedit/images/visibility_slot_menu_on.png';
        this.eyeOffImageUrl = '/cmssmartedit/images/visibility_slot_menu_off.png';
        this.eyeImageUrl = this.eyeOffImageUrl;
        this.closeImageUrl = '/cmssmartedit/images/close_button.png';
        this.buttonVisible = false;
        this.hiddenComponents = [];
        this.isComponentListOpened = false;

        $scope.$watch('ctrl.isComponentListOpened', function(newValue, oldValue) {
            this.eyeImageUrl = (newValue ? this.eyeOnImageUrl : this.eyeOffImageUrl);
            if (newValue !== oldValue) {
                this.setRemainOpen({
                    button: this.buttonName,
                    remainOpen: this.isComponentListOpened
                });
            }
        }.bind(this));

        this.onInit = function() {
            slotVisibilityService.getHiddenComponents(this.slotId).then(function(hiddenComponents) {
                this.hiddenComponents = hiddenComponents;
                this.hiddenComponentCount = hiddenComponents.length;
                if (this.hiddenComponentCount > 0) {
                    this.buttonVisible = true;
                }
            }.bind(this));
        };

        this.positionHiddenComponentList = function() {
            $timeout(function() {
                var buttonElement = $('#slot-visibility-button-' + this.slotId);
                var dropdownElement = $('#slot-visibility-list-' + this.slotId);
                var overflow = dropdownElement.offset().left + dropdownElement.width() - $('body').width();
                if (overflow >= 0) {
                    dropdownElement.offset({
                        left: buttonElement.offset().left + buttonElement.width() - dropdownElement.width(),
                        top: dropdownElement.offset().top
                    });
                }
            }.bind(this));
        };

        this.openHiddenComponentList = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            this.positionHiddenComponentList();
            this.isComponentListOpened = true;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: true
            });
        };

        this.closeHiddenComponentList = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            this.isComponentListOpened = false;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: false
            });
        };

        this.onInit();
    })
    /**
     * @ngdoc directive
     * @name slotVisibilityButtonModule.directive:slotVisibilityButton
     *
     * @description
     * The slot visibility button directive is used inside the slot contextual menu and provides a button 
     * image that displays the number of hidden components, as well as a dropdown menu of hidden component.
     *
     * The directive expects that the parent, the slot contextual menu, has a setRemainOpen function and a 
     * slotId value on the parent's scope. setRemainOpen is used to send a command to the parent to leave 
     * the slot contextual menu open.
     */
    .directive('slotVisibilityButton', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotVisibility/slotVisibilityButtonTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            controller: 'slotVisibilityButtonController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                setRemainOpen: '&',
                slotId: '@',
                initButton: '@'
            }
        };
    });
