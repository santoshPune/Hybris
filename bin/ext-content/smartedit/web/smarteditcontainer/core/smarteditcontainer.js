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
window.smartEditBootstrapped = {}; //storefront actually loads twice all the JS files, including webApplicationInjector.js, smartEdit must be protected against receiving twice a smartEditBootstrap event
angular.module('smarteditcontainer', [
        'ngRoute',
        'ngResource',
        'ui.bootstrap',
        'coretemplates',
        'loadConfigModule',
        'iFrameManagerModule',
        'alertsBoxModule',
        'httpAuthInterceptorModule',
        'httpErrorInterceptorModule',
        'experienceInterceptorModule',
        'bootstrapServiceModule',
        'toolbarModule',
        'leftToolbarModule',
        'pageToolMenuModule',
        'modalServiceModule',
        'dragAndDropServiceModule',
        'previewTicketInterceptorModule',
        'previewDataGenericEditorResponseHandlerModule',
        'catalogServiceModule',
        'catalogDetailsModule',
        'experienceSelectorButtonModule',
        'experienceSelectorModule',
        'sharedDataServiceModule',
        'inflectionPointSelectorModule',
        'paginationFilterModule',
        'resourceLocationsModule',
        'fetchMediaDataHandlerModule',
        'experienceServiceModule',
        'eventServiceModule',
        'dragAndDropScrollingModule',
        'urlServiceModule',
        'perspectiveServiceModule',
        'perspectiveSelectorModule',
        'authorizationModule',
        'hasOperationPermissionModule',
        'l10nModule',
        'yInfiniteScrollingModule'
    ])
    .config(function(LANDING_PAGE_PATH, STOREFRONT_PATH, STOREFRONT_PATH_WITH_PAGE_ID, $routeProvider, $logProvider, permissionKeysFactoryProvider) {
        $routeProvider.when(LANDING_PAGE_PATH, {
                templateUrl: 'web/smarteditcontainer/fragments/landingPage.html',
                controller: 'landingPageController',
                controllerAs: 'landingCtl'
            })
            .when(STOREFRONT_PATH, {
                templateUrl: 'web/smarteditcontainer/fragments/mainview.html',
                controller: 'defaultController'
            })
            .when(STOREFRONT_PATH_WITH_PAGE_ID, {
                templateUrl: 'web/smarteditcontainer/fragments/mainview.html',
                controller: 'defaultController'
            })
            .otherwise({
                redirectTo: LANDING_PAGE_PATH
            });


        permissionKeysFactoryProvider.addKeysToCheck(["smartedit.configurationcenter.read"]);
        $logProvider.debugEnabled(false);
    })
    .run(
        function($rootScope, $log, $q, toolbarServiceFactory, perspectiveService, gatewayFactory, loadConfigManagerService, bootstrapService, iFrameManager, dragAndDropService, restServiceFactory, sharedDataService, urlService, dragAndDropScrollingService, featureService, storageService) {
            gatewayFactory.initListener();

            loadConfigManagerService.loadAsObject().then(function(configurations) {
                sharedDataService.set('defaultToolingLanguage', configurations.defaultToolingLanguage);
            });

            var smartEditTitleToolbarService = toolbarServiceFactory.getToolbarService("smartEditTitleToolbar");

            smartEditTitleToolbarService.addItems([{
                key: 'topToolbar.leftToolbarTemplate',
                type: 'TEMPLATE',
                include: 'web/smarteditcontainer/core/services/topToolbars/leftToolbarWrapperTemplate.html',
                priority: 1,
                section: 'left'
            }, {
                key: 'topToolbar.logoTemplate',
                type: 'TEMPLATE',
                include: 'web/smarteditcontainer/core/services/topToolbars/logoTemplate.html',
                priority: 2,
                section: 'left'
            }, {
                key: 'topToolbar.deviceSupportTemplate',
                type: 'HYBRID_ACTION',
                include: 'web/smarteditcontainer/core/services/topToolbars/deviceSupportTemplate.html',
                priority: 1,
                section: 'right'
            }, {
                type: 'TEMPLATE',
                key: 'topToolbar.experienceSelectorTemplate',
                className: 'ySEPreviewSelector',
                include: 'web/smarteditcontainer/core/services/topToolbars/experienceSelectorWrapperTemplate.html',
                priority: 1, //first in the middle
                section: 'middle'
            }]);

            var experienceSelectorToolbarService = toolbarServiceFactory.getToolbarService("experienceSelectorToolbar");

            experienceSelectorToolbarService.addItems([{
                key: "bottomToolbar.perspectiveSelectorTemplate",
                type: 'TEMPLATE',
                section: 'right',
                priority: 1,
                include: 'web/smarteditcontainer/core/services/topToolbars/perspectiveSelectorWrapperTemplate.html'
            }]);

            function offSetStorefront() {
                // Set the storefront offset
                $('#js_iFrameWrapper').css('padding-top', $('.ySmartEditToolbars').height() + 'px');
            }

            var smartEditBootstrapGateway = gatewayFactory.createGateway('smartEditBootstrap');
            smartEditBootstrapGateway.subscribe("reloadFormerPreviewContext", function(eventId, data) {
                offSetStorefront();
                var deferred = $q.defer();
                iFrameManager.initializeCatalogPreview();
                deferred.resolve();
                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("loading", function(eventId, data) {
                var deferred = $q.defer();

                iFrameManager.setCurrentLocation(data.location);
                iFrameManager.showWaitModal();

                delete window.smartEditBootstrapped[data.location];

                perspectiveService.clearActivePerspective();

                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("bootstrapSmartEdit", function(eventId, data) {
                offSetStorefront();
                var deferred = $q.defer();
                if (!window.smartEditBootstrapped[data.location]) {
                    window.smartEditBootstrapped[data.location] = true;
                    loadConfigManagerService.loadAsObject().then(function(configurations) {
                        bootstrapService.bootstrapSEApp(configurations);
                        deferred.resolve();
                    });
                } else {
                    deferred.resolve();
                }
                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("smartEditReady", function(eventId, data) {
                var deferred = $q.defer();
                deferred.resolve();

                dragAndDropScrollingService.initialize();
                dragAndDropService.apply();

                iFrameManager.hideWaitModal();
                return deferred.promise;
            });

            gatewayFactory.createGateway('accessTokens').subscribe("get", function(eventId, data) {
                return $q.when(storageService.getAuthTokens());
            });
        })
    /**
     * @ngdoc controller
     * @name smarteditcontainer.controller:landingPageController
     * @param {Service} loadConfigManagerService The load configuration manager service
     * @param {Service} catalogService The catalog service
     * @param {Service} restServiceFactory  The REST service factory
     *
     * @description
     * When called, it will retrieve the list of catalogs to display on the landing page.
     */
    .controller(
        'landingPageController',
        function(loadConfigManagerService, catalogService, restServiceFactory) {
            var vm = this;
            vm.CATALOGS_PER_PAGE = 4;

            loadConfigManagerService.loadAsObject().then(function(configurations) {
                restServiceFactory.setDomain(configurations.domain);
                return catalogService.getAllCatalogsGroupedById();
            }).then(function(catalogs) {
                vm.catalogs = catalogs;
                vm.totalItems = catalogs.length;
                vm.currentPage = 1;
            });

            var bodyTag = angular.element(document.querySelector('body'));
            if (bodyTag.hasClass('is-storefront')) {
                bodyTag.removeClass('is-storefront');
            }
        })
    .controller(
        'defaultController',
        function($log, $routeParams, $location, LANDING_PAGE_PATH, systemEventService, iFrameManager, experienceService, sharedDataService) {

            experienceService.buildDefaultExperience($routeParams).then(function(experience) {
                sharedDataService.set('experience', experience).then(function(experience) {
                    systemEventService.sendAsynchEvent("experienceUpdate");
                    iFrameManager.applyDefault();
                    iFrameManager.initializeCatalogPreview(experience);
                });
            }, function(buildError) {
                $log.error("the provided path could not be parsed: " + $location.url());
                $log.error(buildError);
                $location.url(LANDING_PAGE_PATH);
            });

            var bodyTag = angular.element(document.querySelector('body'));
            bodyTag.addClass('is-storefront');

        });
