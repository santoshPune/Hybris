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
angular.module('dragAndDropServiceInterfaceModule', [])

/**
 * @ngdoc service
 * @name dragAndDropServiceInterfaceModule.DragAndDropServiceInterface
 *
 * @description
 * Provides an abstract extensible drag and drop service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
.factory('DragAndDropServiceInterface', function() {

    function DragAndDropServiceInterface() {}

    /**
     * @ngdoc method
     * @name dragAndDropServiceInterfaceModule.DragAndDropServiceInterface#register
     * @methodOf dragAndDropServiceInterfaceModule.DragAndDropServiceInterface
     *
     * @description
     * Method used to handle drag and drop from within the iframe
     * and from outside the iframe ( from SmartEdit Container) to the iframe (SmartEdit)
     *
     * @param {Object} configuration - configuration
     * @param {Function} configuration.startCallback - Callback to be used when dragging starts
     * @param {Function} configuration.dropCallback - Callback to be used when dropped
     * @param {Function} configuration.overCallback - Callback to be used as mouse enters the scope of a sortable element
     * @param {Function} configuration.outCallback - Callback to be used as mouse exits the scope of a sortable element
     * @param {String} configuration.sourceSelector - The jQuery selector to identify items that can be dragged
     * @param {String} configuration.targetSelector - The jQuery selectot to identify locations where items can be dropped
     *
     */
    DragAndDropServiceInterface.prototype.register = function(configuration) {};


    return DragAndDropServiceInterface;
});
