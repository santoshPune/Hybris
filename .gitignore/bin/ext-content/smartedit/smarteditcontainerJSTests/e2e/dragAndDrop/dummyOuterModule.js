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
    angular.module('outerModule', ['dragAndDropServiceModule', 'alertServiceModule'])
        .run(configureService);

    function configureService($rootScope, dragAndDropService, alertService) {
        var configuration = {
            id: 'dragAndDropTest',
            sourceSelector: ".smartEditComponent[data-smartedit-component-type!='ContentSlot']",
            targetSelector: "[data-smartedit-component-type=ContentSlot]",
            overCallback: function(event, ui) {
                if (event.target.id == 'slot2') {
                    $(event.target).addClass("over-slot-disabled");
                    $(event.target).attr('data-content', 'NOT ALLOWED');
                } else {
                    $(event.target).addClass("over-slot-enabled");
                    $(event.target).children($('ySEDnDPlaceHolder')).attr('data-content', 'DROP HERE');
                }
            },
            startCallback: function(event, ui) {
                $(event.target).addClass("active-slot");
            },
            dropCallback: function(event, ui, cancel) {
                if ($(event.target).hasClass("over-slot-disabled")) {
                    cancel();
                    var errorMessage = "This item cannot be dropped to slot:" + event.target.id;
                    alertService.pushAlerts([{
                        successful: false,
                        message: errorMessage,
                        closeable: true
                    }]);
                } else if ($(event.target).hasClass("over-slot-enabled")) {
                    var successMessage = "This item is dropped to slot:" + event.target.id + " at index:" + ui.item.index();
                    alertService.pushAlerts([{
                        successful: true,
                        message: successMessage,
                        closeable: true
                    }]);
                } else {
                    // Nothing hovered. Cancel drop
                    cancel();
                }
            },
            outCallback: function(event, ui) {
                $(ui.helper).removeClass("over-active");
                $(event.target).removeClass("over-slot-enabled");
                $(event.target).removeClass("over-slot-disabled");
                $(event.target).removeClass("active-slot");
            }
        };

        if (useHelpers) {
            configuration.helper = function() {
                return "<img src='/smarteditcontainerJSTests/e2e/dragAndDrop/icons/drag_and_drop_helper_img.jpg' />";
            };
        }

        dragAndDropService.register(configuration);
    }

})();
