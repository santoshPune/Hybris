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
 * @name pageToolMenuModule
 * @description
 * # The pageToolMenuModule
 *
 * The Page Tool Menu module makes the page tool menu directive available. The page tool menu directive, once compiled,
 * displays the configurable page tool menu.
 *
 */
angular.module('pageToolMenuModule', [
    'toolbarModule',
    'iframeClickDetectionServiceModule',
    'pageInformationModule',
    'clonePageModule',
    'syncStatusModule'
])

/**
 * @ngdoc directive
 * @name pageToolMenuModule.directive:pageToolMenu
 * @scope
 * @restrict E
 * @element ANY
 *
 * @description
 * Once compiled, the page tool menu directive displays the configurable page tool menu. By default, the page tool menu
 * is configured to include the synchronization status of the current page. It can also be configured to include
 * customizable actions.
 *
 * The page tool menu is configured using the {@link toolbarModule.ToolbarService ToolbarService} and by specifying the
 * toolbar ID "pageToolMenu".
 *
 * Example: The following example shows how to add an item called Extended Page Information to the page tool menu, which
 * opens the content in the extendedPageInformation.html file.
*
 * <pre>
 angular.module('app', ['toolbarModule'])
    .run(function(toolbarServiceFactory) {

        var pageToolMenuService = toolbarServiceFactory.getToolbarService('pageToolMenu');
        pageToolMenuService.addItems([{
            i18nKey: 'page.tool.menu.extended.page.information.title',
            type: 'HYBRID_ACTION',
            icons: {
                'default': 'static-resources/images/icon_small_extended_info.png'
            },
            include: 'web/smarteditcontainer/core/services/pageToolMenu/extendedPageInformation.html'
        }]);
    });
 * </pre>
 *
 * @param {String} imageRoot Path to the root folder for images.
 * @param {String} toolbarName Toolbar name used by the gateway proxy service
 */
.directive('pageToolMenu', function(toolbarServiceFactory, iframeClickDetectionService, hitch, $document) {
    return {
        templateUrl: 'web/smarteditcontainer/core/services/pageToolMenu/pageToolMenuTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        scope: {
            toolbarName: '@',
            imageRoot: '=imageRoot'
        },

        link: function(scope, element, attrs) {
            // Register with Toolbar Service
            var toolbarService = toolbarServiceFactory.getToolbarService(scope.toolbarName);

            toolbarService.setOnAliasesChange(function(actions) {
                scope.actions = actions;
                scope.actionsClasses = toolbarService.actionsClasses;
            });

            // Scope Augmentation
            scope.backButtonI18nKey = "page.tool.menu.back.button";

            scope.imageRoot = scope.imageRoot || "";

            scope.actions = toolbarService.getAliases();

            scope.actionsClasses = toolbarService.actionsClasses;

            scope.triggerAction = function(item, $event) {
                $event.preventDefault();
                if (item.include) {
                    scope.showPageToolMenuSelectedItem();
                    scope.selected = {
                        include: item.include
                    };
                }
                toolbarService.triggerAction(item);
            };

            scope.selected = {};

            scope.selectedItemCallbacks = {
                onClose: function() {}
            };

            scope.clearSelectedItem = function() {
                scope.selectedItemCallbacks.onClose();
                scope.selected = {};
                scope.selectedItemCallbacks = {
                    onClose: function() {}
                };
            };

            scope.showPageToolMenuHome = function($event) {
                if ($event) {
                    $event.preventDefault();
                }

                $('#page-tool-menu-home').removeClass('hideLevel1').addClass('showLevel1');
                $('#page-tool-menu-selected-item').removeClass('showLevel2').addClass('hideLevel2');

                scope.clearSelectedItem();
            };

            scope.showPageToolMenuSelectedItem = function($event) {
                if ($event) {
                    $event.preventDefault();
                }
                $('#page-tool-menu-home').addClass('hideLevel1').removeClass('showLevel1');
                $('#page-tool-menu-selected-item').removeClass('hideLevel2').addClass('showLevel2');
            };

            scope.closePageToolMenu = function($event) {
                if ($event) {
                    $event.preventDefault();
                }
                var bodyElement = $('body');
                bodyElement.removeClass('nav-expanded-right');
                bodyElement.one('transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd', function() {
                    $('#page-tool-menu-home').removeClass('hideLevel1').addClass('showLevel1');
                    $('#page-tool-menu-selected-item').addClass('hideLevel2').removeClass('showLevel2');
                });

                scope.clearSelectedItem();
            };

            // Close Page Tool Menu on Clicking Page
            $document.on('click', function(event) {
                if (!isClickWithinPageToolMenu(event) && !isClickOnToolbarButton(event)) {
                    scope.closePageToolMenu();
                }
            });

            function isClickWithinPageToolMenu(event) {
                return $(event.target).hasClass('pageToolMenu') ||
                    $(event.target).parents('.pageToolMenu').length > 0;
            }

            function isClickOnToolbarButton(event) {
                return $(event.target).hasClass('yHybridAction') ||
                    $(event.target).parents('.btn.btn-default.yHybridAction').length > 0;
            }

            iframeClickDetectionService.registerCallback('pageToolMenuClose', function() {
                scope.closePageToolMenu();
            });
        }
    };
});
