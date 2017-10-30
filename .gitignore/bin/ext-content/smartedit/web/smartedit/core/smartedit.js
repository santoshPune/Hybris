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
    angular.module('smartedit', [
            'sakExecutorDecorator',
            'restServiceFactoryModule',
            'ui.bootstrap',
            'ngResource',
            'decoratorServiceModule',
            'alertsBoxModule',
            'ui.select',
            'httpAuthInterceptorModule',
            'httpErrorInterceptorModule',
            'experienceInterceptorModule',
            'gatewayFactoryModule',
            'renderServiceModule',
            'iframeClickDetectionServiceModule',
            'sanitizeHtmlInputModule',
            'perspectiveServiceModule',
            'featureServiceModule',
            'slotFixServiceModule',
            'languageServiceModule'
        ])
        .config(function($logProvider) {
            $logProvider.debugEnabled(false);
        })
        .value("NUM_SE_SLOTS", getNumSlots())
        .directive('html', function() {
            return {
                restrict: "E",
                replace: false,
                transclude: false,
                priority: 1000,
                link: function($scope, element) {
                    element.addClass('smartedit-html-container');
                }
            };
        })
        .directive('body', function(sakExecutor, gatewayFactory, perspectiveService, languageService) {
            return {
                restrict: "E",
                replace: false,
                transclude: false,
                priority: 1000,
                link: function($scope, element) {
                    perspectiveService.selectDefault();
                    languageService.registerSwitchLanguage();
                }
            };
        })
        .controller('SmartEditController', function() {})
        /*
         Do not remove renderService! It is added to the signature of the method to force loading it. Otherwise componentRender calls from
         the container into SmartEdit will fail.
         */
        .run(function(domain, systemEventService, restServiceFactory, gatewayFactory, renderService, renderGateway, decoratorService, featureService, slotFixService) {
            gatewayFactory.initListener();

            restServiceFactory.setDomain(domain);

            // Feature registration
            featureService.register({
                key: 'se.emptySlotFix',
                nameI18nKey: 'se.emptyslotfix',
                enablingCallback: function() {
                    slotFixService._resizeEmptySlots(true);
                },
                disablingCallback: function() {
                    slotFixService._resizeEmptySlots(false);
                }
            });

            featureService.addDecorator({
                key: 'se.contextualMenu',
                nameI18nKey: 'contextualMenu'
            });

            featureService.addDecorator({
                key: 'se.slotContextualMenu',
                nameI18nKey: 'se.slot.contextual.menu'
            });

            featureService.addDecorator({
                key: 'se.basicSlotContextualMenu',
                nameI18nKey: 'basic.se.slot.contextual.menu'
            });
        });

    function getNumSlots() {
        return $('.smartEditComponent[data-smartedit-component-type="ContentSlot"]').length;
    }
})();
