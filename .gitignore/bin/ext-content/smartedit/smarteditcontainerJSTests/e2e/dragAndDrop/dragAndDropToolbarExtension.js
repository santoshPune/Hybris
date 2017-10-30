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
angular.module('dragAndDropToolbar', ['toolbarModule'])
    .directive('addComponents', function() {
        return {
            template: "<div class='smartEditComponent' id='component0' data-smartedit-component-type='componentType0' data-smartedit-component-id='component0'>" +
                "<div class='box'>" +
                "<p>test component 0</p>" +
                "</div>" +
                "</div>",
            restrict: 'E',
            replace: false,
            link: function() {}
        };
    })
    .run(function(toolbarServiceFactory, $templateCache) {
        $templateCache.put('dummyComponents.html', '<add-components></add-components>');

        var toolbarService = toolbarServiceFactory.getToolbarService('experienceSelectorToolbar');
        var result = toolbarService.addItems([{
            key: 'dragAndDropItem',
            type: 'HYBRID_ACTION',
            icons: {
                'default': '../../smarteditcontainerJSTests/e2e/dragAndDrop/icons/icon_small_info.png'
            },
            include: 'dummyComponents.html'
        }]);
    });
