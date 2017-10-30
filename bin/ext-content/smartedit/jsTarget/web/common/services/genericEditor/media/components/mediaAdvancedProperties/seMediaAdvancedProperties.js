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
angular.module('seMediaAdvancedPropertiesModule', ['ui.bootstrap'])
    .constant('seMediaAdvancedPropertiesConstants', {
        CONTENT_URL: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesContentTemplate.html',
        I18N_KEYS: {
            DESCRIPTION: 'media.advanced.information.description',
            CODE: 'media.advanced.information.code',
            ALT_TEXT: 'media.advanced.information.alt.text',
            ADVANCED_INFORMATION: 'media.advanced.information',
            INFORMATION: 'media.information'
        }
    })
    .controller('seMediaAdvancedPropertiesController', ['seMediaAdvancedPropertiesConstants', function(seMediaAdvancedPropertiesConstants) {
        this.i18nKeys = seMediaAdvancedPropertiesConstants.I18N_KEYS;
        this.contentUrl = seMediaAdvancedPropertiesConstants.CONTENT_URL;
    }])
    .directive('seMediaAdvancedProperties', function() {
        return {
            restrict: 'E',
            scope: {},
            bindToController: {
                code: '=',
                advInfoIcon: '=',
                description: '=',
                altText: '='
            },
            controller: 'seMediaAdvancedPropertiesController',
            controllerAs: 'ctrl',
            templateUrl: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesTemplate.html'
        };
    })
    .directive('seMediaAdvancedPropertiesCondensed', function() {
        return {
            restrict: 'E',
            scope: {},
            bindToController: {
                code: '=',
                advInfoIcon: '=',
                description: '=',
                altText: '='
            },
            controller: 'seMediaAdvancedPropertiesController',
            controllerAs: 'ctrl',
            templateUrl: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesCondensedTemplate.html'
        };
    });
