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
