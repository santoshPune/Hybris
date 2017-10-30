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

