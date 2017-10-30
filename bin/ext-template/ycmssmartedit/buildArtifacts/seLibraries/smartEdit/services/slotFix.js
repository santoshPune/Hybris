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
(function() {

    angular.module('slotFixServiceModule', ['componentHandlerServiceModule'])
        .factory('slotFixService', function(componentHandlerService, COMPONENT_CLASS, TYPE_ATTRIBUTE) {
            // Constants
            var SLOTS_SELECTOR = '.' + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot']";

            var SlotFixService = function() {
                this._resizeEmptySlots = function(showSlots) {
                    var slots = componentHandlerService.getFromSelector(SLOTS_SELECTOR);
                    if (showSlots) {
                        slots.addClass('ySEEmptySlot');
                    } else {
                        slots.removeClass('ySEEmptySlot');
                    }
                };

                this._getFromSelector = function(selector) {
                    return $(selector);
                };
            };

            return new SlotFixService();
        });

})();
