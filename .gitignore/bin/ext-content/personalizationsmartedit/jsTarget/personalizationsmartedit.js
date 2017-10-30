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
 *
 *
 */
angular.module('personalizationsmarteditCommons', [
        'alertServiceModule', 'commonsTemplates'
    ])
    .factory('personalizationsmarteditUtils', function() {
        var utils = {};

        utils.pushToArrayIfValueExists = function(array, key, value) {
            if (value) {
                array.push({
                    "key": key,
                    "value": value
                });
            }
        };

        utils.getContainerIdForElement = function(element) {
            var container = element.closest('[data-smartedit-container-id][data-smartedit-container-type="CxCmsComponentContainer"]');
            if (container.length) {
                return container.data().smarteditContainerId;
            }
            return null;
        };

        utils.getContainerIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getContainerIdForElement(element);
        };

        utils.getSlotIdForElement = function(element) {
            var slot = element.closest('[data-smartedit-component-type="ContentSlot"]');
            if (slot.length) {
                return slot.data().smarteditComponentId;
            }
            return null;
        };

        utils.getSlotIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getSlotIdForElement(element);
        };

        utils.getVariationCodes = function(variations) {
            if ((typeof variations === 'undefined') || (variations === null)) {
                return [];
            }
            var allVariationsCodes = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            });
            return allVariationsCodes;
        };

        utils.getPageId = function() {
            return /page\-([\w]+)/.exec($('iframe').contents().find('body').attr('class'))[1];
        };

        utils.getVariationKey = function(customizationId, variations) {
            if (customizationId === undefined || variations === undefined) {
                return [];
            }

            var allVariationsKeys = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            }).map(function(variationId) {
                return {
                    "variationCode": variationId,
                    "customizationCode": customizationId
                };
            });
            return allVariationsKeys;
        };

        utils.getSegmentTriggerForVariation = function(variation) {
            var triggers = variation.triggers || [];
            var segmentTriggerArr = triggers.filter(function(trigger) {
                return trigger.type === "segmentTriggerData";
            });

            if (segmentTriggerArr.length === 0) {
                return {};
            }

            return segmentTriggerArr[0];
        };

        utils.getActionIdForElement = function(element) {
            var action = element.closest('[data-smartedit-personalization-action-id]');
            if (action.length) {
                return action.data().smarteditPersonalizationActionId;
            }
            return null;
        };

        return utils;
    })
    .factory('personalizationsmarteditMessageHandler', ['alertService', function(alertService) {
        var sendMessage = function(message, isSuccessful) {
            alertService.pushAlerts([{
                successful: isSuccessful,
                message: message
            }]);
        };

        var messageHandler = {};
        messageHandler.sendInformation = function(informationMessage) {
            sendMessage(informationMessage, true);
        };

        messageHandler.sendError = function(errorMessage) {
            sendMessage(errorMessage, false);
        };

        return messageHandler;
    }]);

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
 *
 *
 */
angular.module('personalizationsmarteditCommons')
    .directive('personalizationInfiniteScroll', ['$rootScope', '$window', '$timeout', function($rootScope, $window, $timeout) {
        return {
            link: function(scope, elem, attrs) {
                var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
                $window = angular.element($window);
                elem.css('overflow-y', 'auto');
                elem.css('overflow-x', 'hidden');
                elem.css('height', 'inherit');
                scrollDistance = 0;
                if (attrs.personalizationInfiniteScrollDistance !== null) {
                    scope.$watch(attrs.personalizationInfiniteScrollDistance, function(value) {
                        return (scrollDistance = parseInt(value, 10));
                    });
                }
                scrollEnabled = true;
                checkWhenEnabled = false;
                if (attrs.personalizationInfiniteScrollDisabled !== null) {
                    scope.$watch(attrs.personalizationInfiniteScrollDisabled, function(value) {
                        scrollEnabled = !value;
                        if (scrollEnabled && checkWhenEnabled) {
                            checkWhenEnabled = false;
                            return handler();
                        }
                    });
                }
                $rootScope.$on('refreshStart', function() {
                    elem.animate({
                        scrollTop: "0"
                    });
                });
                handler = function() {
                    var container, elementBottom, remaining, shouldScroll, containerBottom;
                    container = $(elem.children()[0]);
                    elementBottom = elem.offset().top + elem.height();
                    containerBottom = container.offset().top + container.height();
                    remaining = containerBottom - elementBottom;
                    shouldScroll = remaining <= elem.height() * scrollDistance;
                    if (shouldScroll && scrollEnabled) {
                        if ($rootScope.$$phase) {
                            return scope.$eval(attrs.personalizationInfiniteScroll);
                        } else {
                            return scope.$apply(attrs.personalizationInfiniteScroll);
                        }
                    } else if (shouldScroll) {
                        return (checkWhenEnabled = true);
                    }
                };
                elem.on('scroll', handler);
                scope.$on('$destroy', function() {
                    return $window.off('scroll', handler);
                });
                return $timeout((function() {
                    if (attrs.personalizationInfiniteScrollImmediateCheck) {
                        if (scope.$eval(attrs.personalizationInfiniteScrollImmediateCheck)) {
                            return handler();
                        }
                    } else {
                        return handler();
                    }
                }), 0);
            }
        };
    }]);

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
 *
 *
 */
angular.module('personalizationsmarteditCommons')
    .directive('personalizationsmarteditPagination', function() {
        return {
            template: '<div ng-include="getContentUrl()"></div>',
            restrict: 'E',
            scope: {
                callback: "=",
                pages: "=",
                currentPage: "=",
                pageSizes: "=",
                currentSize: "=",
                pagesOffset: "=",
                fixedPageSize: "="
            },

            link: function($scope, element, attrs) {

                if (!$scope.callback) {
                    console.log("callback is undefined!");
                }

                $scope.pages = $scope.pages || [0, 1, 2];
                $scope.currentPage = $scope.currentPage || 0;
                $scope.pageSizes = $scope.pageSizes || [5, 10, 25, 50, 100];
                $scope.currentSize = $scope.currentSize || 10;
                $scope.pagesOffset = $scope.pagesOffset || 2;
                $scope.fixedPageSize = $scope.fixedPageSize || false; //NOSONAR

                $scope.getContentUrl = function() {
                    attrs.template = attrs.template || 'personalizationsmarteditPaginationTemplate.html';
                    return 'web/features/commons/personalizationsmarteditPagination/' + attrs.template;
                };

                $scope.pageClick = function(newValue) {
                    if ($scope.currentPage !== newValue) {
                        $scope.currentPage = newValue;
                        $scope.callback($scope);
                    }
                };

                $scope.pageSizeClick = function(newValue) {
                    if ($scope.currentSize !== newValue) {
                        $scope.currentSize = newValue;
                        $scope.currentPage = 0;
                        $scope.callback($scope);
                    }
                };

                $scope.hasPrevious = function() {
                    return $scope.currentPage > 0;
                };

                $scope.hasNext = function() {
                    return $scope.currentPage < $scope.pages.length - 1;
                };

                $scope.isActive = function(value) {
                    return $scope.currentPage === value;
                };

                $scope.nextClick = function() {
                    if ($scope.hasNext()) {
                        $scope.currentPage++;
                        $scope.callback($scope);
                    }
                };

                $scope.prevClick = function() {
                    if ($scope.hasPrevious()) {
                        $scope.currentPage--;
                        $scope.callback($scope);
                    }
                };

                $scope.pagesToDisplay = function() {
                    var numberOfPages = 2 * $scope.pagesOffset + 1;
                    if ($scope.pages.length <= numberOfPages) {
                        return $scope.pages;
                    } else {
                        var start = Math.max($scope.currentPage - $scope.pagesOffset, 0);
                        if (start + numberOfPages > $scope.pages.length) {
                            start = $scope.pages.length - numberOfPages;
                        }
                        return $scope.pages.slice(start, start + numberOfPages);
                    }
                };

                $scope.availablePageSizes = function() {
                    return $scope.pageSizes;
                };

                $scope.getCurrentPageSize = function() {
                    return $scope.currentSize;
                };

                $scope.isFixedPageSize = function() {
                    return $scope.fixedPageSize;
                };

            }
        };
    });

angular.module('commonsTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationLefttoolbarTemplate.html',
    "<div class=\"row\">\n" +
    "\n" +
    "    <div class=\"col-xs-12\">\n" +
    "        <ul class=\"pagination\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationTemplate.html',
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-4\"></div>\n" +
    "    <div class=\"col-xs-4\">\n" +
    "        <ul class=\"pagination pagination-lg\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div class=\"col-xs-2\"></div>\n" +
    "    <div class=\"col-xs-2\" ng-if=\"!isFixedPageSize()\">\n" +
    "        <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "            <span ng-bind=\"getCurrentPageSize()\"></span>\n" +
    "            <span data-translate=\"personalization.pagination.rowsperpage\"></span>\n" +
    "            <span class=\"hyicon hyicon-arrow\"></span>\n" +
    "        </button>\n" +
    "        <ul class=\"dropdown-menu pull-right text-left\" role=\"menu\">\n" +
    "            <li ng-repeat=\"i in availablePageSizes() track by $index\"><a ng-click=\"pageSizeClick(i)\">{{i}}</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );

}]);

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
 *
 *
 */
angular.module('personalizationsmarteditComponentLightUpDecorator', ['personalizationsmarteditTemplates', 'personalizationsmarteditContextServiceModule', 'personalizationsmarteditCommons'])
    .directive('personalizationsmarteditComponentLightUp', ['personalizationsmarteditContextService', 'personalizationsmarteditUtils', function(personalizationsmarteditContextService, personalizationsmarteditUtils) {
        return {
            templateUrl: 'web/features/personalizationsmartedit/componentLightUpDecorator/personalizationsmarteditComponentLightUpDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@'
            },
            link: function($scope, element, attrs) {
                var isElementSelected = function() {
                    var ctxEnabled = personalizationsmarteditContextService.isPersonalizationContextEnabled();
                    var containerId = personalizationsmarteditUtils.getContainerIdForElement(element);
                    var elementSelected = $.inArray(containerId, personalizationsmarteditContextService.selectedComponents) > -1;
                    return ctxEnabled && elementSelected;
                };

                var isComponentSelected = function() {
                    var componentSelected = isElementSelected();
                    componentSelected = componentSelected && angular.isArray(personalizationsmarteditContextService.selectedVariations);
                    return componentSelected;
                };

                var isVariationComponentSelected = function() {
                    var componentSelected = isElementSelected();
                    componentSelected = componentSelected && angular.isObject(personalizationsmarteditContextService.selectedVariations);
                    componentSelected = componentSelected && !angular.isArray(personalizationsmarteditContextService.selectedVariations);
                    return componentSelected;
                };

                $scope.getPersonalizationComponentBorderClass = function() {
                    var container = element.parent().closest('[data-smartedit-container-id][data-smartedit-container-type="CxCmsComponentContainer"]');
                    container.removeClass("personalizationsmarteditVariationComponentSelected");
                    container.removeClass("personalizationsmarteditVariationComponentSelected-icon");
                    container.removeClass("personalizationsmarteditComponentSelected");
                    if (isVariationComponentSelected()) {
                        container.addClass("personalizationsmarteditVariationComponentSelected");
                        container.addClass("personalizationsmarteditVariationComponentSelected-icon");
                    }
                    if (isComponentSelected()) {
                        container.addClass("personalizationsmarteditComponentSelected");
                    }
                };
            }
        };
    }]);

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
 *
 *
 */
angular.module('personalizationsmarteditContextMenu', ['gatewayProxyModule'])

.factory('personalizationsmarteditContextModal', ['gatewayProxy', function(gatewayProxy) {

    var PersonalizationsmarteditContextModal = function() {
        this.gatewayId = "personalizationsmarteditContextModal";
        gatewayProxy.initForService(this);
    };

    PersonalizationsmarteditContextModal.prototype.openDeleteAction = function(componentType, componentId, containerId, slotId, actionId) {};

    PersonalizationsmarteditContextModal.prototype.openAddAction = function(componentType, componentId, containerId, slotId, actionId) {};

    PersonalizationsmarteditContextModal.prototype.openEditAction = function(componentType, componentId, containerId, slotId, actionId) {};

    PersonalizationsmarteditContextModal.prototype.openInfoAction = function() {};

    return new PersonalizationsmarteditContextModal();
}]);

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
 *
 *
 */
jQuery(document).ready(function($) {

    var loadCSS = function(href) {
        var cssLink = $("<link rel='stylesheet' type='text/css' href='" + href + "'>");
        $("head").append(cssLink);
    };

    loadCSS("/personalizationsmartedit/css/style.css");

});

angular.module('personalizationsmarteditmodule', [
        'decoratorServiceModule',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditComponentLightUpDecorator',
        'personalizationsmarteditContextMenu',
        'personalizationsmarteditCommons',
        'featureServiceModule'
    ])
    .directive('body', ['personalizationsmarteditContextService', function(personalizationsmarteditContextService) {
        return {
            link: function(scope, element, attrs) {
                scope.$watch(
                    function() {
                        return element.attr('data-smartedit-ready');
                    },
                    function(newValue, oldValue) {
                        if (newValue !== oldValue && (newValue === true || newValue === "true")) {
                            personalizationsmarteditContextService.applySynchronization();
                        }
                    }, true);
                scope.$watch('element.class', function() {
                    var pageIdArray = element.attr('class').split(" ").filter(function(elem) {
                        return /smartedit-page-uid\-(\S+)/.test(elem);
                    });
                    if (pageIdArray.length > 0) {
                        var pageId = /smartedit-page-uid\-(\S+)/.exec(pageIdArray[0])[1];
                        personalizationsmarteditContextService.setPageId(pageId);
                        if (pageIdArray.length > 1) {
                            console.log("more than one page- class element attribute defined");
                        }
                    }
                }, true);

            }
        };
    }])
    .run(
        ['decoratorService', 'personalizationsmarteditContextService', 'personalizationsmarteditContextServiceProxy', 'personalizationsmarteditContextModal', 'featureService', 'personalizationsmarteditUtils', function(decoratorService, personalizationsmarteditContextService, personalizationsmarteditContextServiceProxy, personalizationsmarteditContextModal, featureService, personalizationsmarteditUtils) {
            var PersonalizationviewContextServiceProxy = new personalizationsmarteditContextServiceProxy('PersonalizationCtxGateway');

            decoratorService.addMappings({
                '^.*Component$': ['personalizationsmarteditComponentLightUp'],
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditComponentLightUp',
                nameI18nKey: 'personalizationsmarteditComponentLightUp'
            });

            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.add.action",
                i18nKey: 'personalization.context.add.action',
                nameI18nKey: 'personalization.context.add.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuAddItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openAddAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_add_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_add_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.edit.action",
                i18nKey: 'personalization.context.edit.action',
                nameI18nKey: 'personalization.context.edit.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuEditItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openEditAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_edit_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_edit_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.delete.action",
                i18nKey: 'personalization.context.delete.action',
                nameI18nKey: 'personalization.context.delete.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuDeleteItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openDeleteAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_delete_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_delete_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.info.action",
                i18nKey: 'personalization.context.info.action',
                nameI18nKey: 'personalization.context.info.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuInfoItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    personalizationsmarteditContextModal.openInfoAction();
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_info_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_info_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });



        }]);

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
 *
 *
 */
angular.module('personalizationsmarteditContextServiceModule', ['personalizationsmarteditCommons'])
    .factory('personalizationsmarteditContextService', ['personalizationsmarteditUtils', 'personalizationsmarteditContextServiceReverseProxy', function(personalizationsmarteditUtils, personalizationsmarteditContextServiceReverseProxy) {

        var ContextService = {};
        var ContextServiceReverseProxy = new personalizationsmarteditContextServiceReverseProxy('PersonalizationCtxReverseGateway');

        var isContextualMenuEnabled = function() {
            var isEnabled = ContextService.personalizationEnabled;
            isEnabled = isEnabled && angular.isObject(ContextService.selectedCustomizations);
            isEnabled = isEnabled && angular.isObject(ContextService.selectedVariations);
            isEnabled = isEnabled && !angular.isArray(ContextService.selectedVariations);

            return isEnabled;
        };

        var isElementHighlighted = function(element) {
            var containerId = personalizationsmarteditUtils.getContainerIdForElement(element);
            var elementHighlighted = $.inArray(containerId, ContextService.selectedComponents) > -1;
            return elementHighlighted;
        };

        ContextService.personalizationEnabled = false;
        ContextService.selectedCustomizations = null;
        ContextService.selectedVariations = null;
        ContextService.selectedComponents = null;
        ContextService.seExperienceData = null;
        ContextService.seConfigurationData = null;
        ContextService.sePreviewData = null;
        ContextService.pageId = null;

        ContextService.isPersonalizationContextEnabled = function() {
            return ContextService.personalizationEnabled;
        };

        ContextService.setPersonalizationContextEnabled = function(persCtxEnabled) {
            ContextService.personalizationEnabled = persCtxEnabled;
        };

        ContextService.setSelectedComponents = function(newSelectedComponents) {
            ContextService.selectedComponents = newSelectedComponents;
        };

        ContextService.setSelectedVariations = function(newSelectedVariations) {
            ContextService.selectedVariations = newSelectedVariations;
        };

        ContextService.setSelectedCustomizations = function(newSelectedCustomizations) {
            ContextService.selectedCustomizations = newSelectedCustomizations;
        };

        ContextService.isContextualMenuAddItemEnabled = function(element) {
            return isContextualMenuEnabled() && (!isElementHighlighted(element));
        };

        ContextService.isContextualMenuEditItemEnabled = function(element) {
            return isContextualMenuEnabled() && isElementHighlighted(element);
        };

        ContextService.isContextualMenuDeleteItemEnabled = function(element) {
            return isContextualMenuEnabled() && isElementHighlighted(element);
        };

        ContextService.isContextualMenuInfoItemEnabled = function(element) {
            var isEnabled = ContextService.personalizationEnabled;
            isEnabled = isEnabled && !angular.isObject(ContextService.selectedVariations);
            isEnabled = isEnabled || angular.isArray(ContextService.selectedVariations);

            return isEnabled;
        };

        ContextService.applySynchronization = function() {
            ContextServiceReverseProxy.applySynchronization();
        };

        ContextService.getSeExperienceData = function() {
            return ContextService.seExperienceData;
        };

        ContextService.setSeExperienceData = function(newSeExperienceData) {
            ContextService.seExperienceData = newSeExperienceData;
        };

        ContextService.getSeConfigurationData = function() {
            return ContextService.seConfigurationData;
        };

        ContextService.setSeConfigurationData = function(newSeConfigurationData) {
            ContextService.seConfigurationData = newSeConfigurationData;
        };

        ContextService.getSePreviewData = function() {
            return ContextService.sePreviewData;
        };

        ContextService.setSePreviewData = function(newSePreviewData) {
            ContextService.sePreviewData = newSePreviewData;
        };

        ContextService.setPageId = function(newPageId) {
            ContextService.pageId = newPageId;
            ContextServiceReverseProxy.setPageId(newPageId);
        };

        ContextService.getPageId = function() {
            return ContextService.pageId;
        };

        return ContextService;
    }])
    .factory('personalizationsmarteditContextServiceProxy', ['gatewayProxy', 'personalizationsmarteditContextService', function(gatewayProxy, personalizationsmarteditContextService) {
        var proxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };
        proxy.prototype.setPersonalizationContextEnabled = function(persCtxEnabled) {
            personalizationsmarteditContextService.setPersonalizationContextEnabled(persCtxEnabled);
        };
        proxy.prototype.setSelectedComponents = function(newSelectedComponents) {
            personalizationsmarteditContextService.setSelectedComponents(newSelectedComponents);
        };
        proxy.prototype.setSelectedVariations = function(newSelectedVariations) {
            personalizationsmarteditContextService.setSelectedVariations(newSelectedVariations);
        };
        proxy.prototype.setSelectedCustomizations = function(newSelectedCustomizations) {
            personalizationsmarteditContextService.setSelectedCustomizations(newSelectedCustomizations);
        };
        proxy.prototype.setSeExperienceData = function(newSeExperienceData) {
            personalizationsmarteditContextService.setSeExperienceData(newSeExperienceData);
        };
        proxy.prototype.setSeConfigurationData = function(newSeConfigurationData) {
            personalizationsmarteditContextService.setSeConfigurationData(newSeConfigurationData);
        };
        proxy.prototype.setSePreviewData = function(newSePreviewData) {
            personalizationsmarteditContextService.setSePreviewData(newSePreviewData);
        };

        return proxy;
    }])
    .factory('personalizationsmarteditContextServiceReverseProxy', ['gatewayProxy', function(gatewayProxy) {
        var reverseProxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };
        reverseProxy.prototype.applySynchronization = function() {};
        reverseProxy.prototype.setPageId = function(newPageId) {};

        return reverseProxy;
    }]);

angular.module('personalizationsmarteditTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/personalizationsmartedit/componentLightUpDecorator/personalizationsmarteditComponentLightUpDecoratorTemplate.html',
    "<div ng-class=\"getPersonalizationComponentBorderClass()\">\n" +
    "    <div data-ng-transclude></div>\n" +
    "</div>"
  );

}]);
