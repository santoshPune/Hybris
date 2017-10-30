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
 * @name editorModalServiceModule
 * @description
 * # The editorModalServiceModule
 *
 * The editor modal service module provides a service that allows opening an editor modal for a given component type and
 * component ID. The editor modal is populated with a save and cancel button, and is loaded with the {@link
 * editorTabsetModule.directive:editorTabset editorTabset} as its content, providing a way to edit
 * various fields of the given component.
 */
angular.module('editorModalServiceModule', ['gatewayProxyModule'])

/**
 * @ngdoc service
 * @name editorModalServiceModule.service:editorModalService
 *
 * @description
 * Convenience service to open an editor modal window for a given component type and component ID.
 *
 * Example: A button is bound to the function '$scope.onClick' via the ng-click directive. Clicking the button will
 * trigger the opening of an editor modal for a CMSParagraphComponent with the ID 'termsAndConditionsParagraph'
 *
 * <pre>
    angular.module('app', ['editorModalServiceModule'])
        .controller('someController', function($scope, editorModalService) {
            $scope.onClick = function() {
                editorModalService.open('CMSParagraphComponent', 'termsAndConditionsParagraph');
            };
        });
 * </pre>
*/
.factory('editorModalService', ['gatewayProxy', function(gatewayProxy) {
    function EditorModalService() {
        this.gatewayId = 'EditorModal';
        gatewayProxy.initForService(this, ["open", "openAndRerenderSlot"]);
    }

    /**
     * @ngdoc method
     * @name editorModalServiceModule.service:editorModalService#open
     * @methodOf editorModalServiceModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {String} componentType The type of component as defined in the platform.
     * @param {String} componentId The ID of the component as defined in the database.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    EditorModalService.prototype.open = function(componentType, componentId) {};

    /**
     * @ngdoc method
     * @name editorModalServiceModule.service:editorModalService#open
     * @methodOf editorModalServiceModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {String} componentType The type of component as defined in the platform.
     * @param {String} componentId The ID of the component as defined in the database.
     * @param {String} slotId The ID of the slot as defined in the database.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    EditorModalService.prototype.openAndRerenderSlot = function(componentType, componentId) {};

    return new EditorModalService();
}]);
