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
angular.module('leftToolbarModule', ['authenticationModule', 'iframeClickDetectionServiceModule', 'resourceLocationsModule', 'iFrameManagerModule', 'sharedDataServiceModule', 'languageSelectorModule', 'storageServiceModule'])
    .directive('leftToolbar', function($location, $timeout, authenticationService, iframeClickDetectionService, iFrameManager, sharedDataService, storageService, LANDING_PAGE_PATH) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/topToolbars/leftToolbarTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,

            scope: {
                imageRoot: '=imageRoot',
            },

            link: function(scope, element, attrs) {

                function resetLocation() {

                    sharedDataService.get("preview").then(function(preview) {
                        if (preview && preview.resourcePath) {
                            iFrameManager.setCurrentLocation(preview.resourcePath);
                        }
                    });
                }

                storageService.getPrincipalIdentifier().then(function(user) {
                    scope.username = user;
                });

                scope.configurationCenterReadPermissionKey = "smartedit.configurationcenter.read";

                scope.showToolbar = function($event) {
                    $event.preventDefault();
                    $('body').toggleClass('nav-expanded-left');
                };

                scope.showSites = function() {
                    scope.closeLeftToolbar();

                    resetLocation();
                    // wait for the css closing animation to be completed
                    $timeout(function() {
                        $location.url(LANDING_PAGE_PATH);
                    }, 400);
                };

                scope.showCfgCenter = function($event) {
                    $event.preventDefault();
                    //$('.left-toolbar').toggleClass('hide');

                    $('#hamburger-menu-level1').addClass('ySELeftHideLevel1').removeClass('ySELeftShowLevel1');
                    $('#hamburger-menu-level2').removeClass('ySELeftHideLevel2').addClass('ySELeftShowLevel2');
                };

                scope.goBack = function() {
                    //$('.left-toolbar').toggleClass('hide');

                    $('#hamburger-menu-level1').removeClass('ySELeftHideLevel1').addClass('ySELeftShowLevel1');
                    $('#hamburger-menu-level2').removeClass('ySELeftShowLevel2').addClass('ySELeftHideLevel2');
                };

                scope.signOut = function($event) {
                    authenticationService.logout();
                    resetLocation();
                };

                $(document).bind("click", function(event) {
                    if (!($(event.target).parents('.leftNav').length > 0 || $(event.target).parents('.navbar-header.pull-left.leftNavBtn').length > 0)) {
                        scope.closeLeftToolbar();
                    }
                });

                scope.closeLeftToolbar = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }

                    // Go back to the "home" of the menu.
                    $('body').removeClass('nav-expanded-left');
                    scope.goBack();
                };


                iframeClickDetectionService.registerCallback('leftToolbarClose', function() {
                    scope.closeLeftToolbar();
                });
            }
        };
    });
