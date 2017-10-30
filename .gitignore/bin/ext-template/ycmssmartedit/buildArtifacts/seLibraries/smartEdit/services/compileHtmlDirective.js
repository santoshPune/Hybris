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
    /**
     * @ngdoc overview
     * @name compileHtmlModule
     * @description
     * # The compileHtmlModule
     *
     * The compileHtmlModule provides a directive to evaluate and compile HTML markup.
     *
     */
    angular.module('compileHtmlModule', [])

    /**
     * @ngdoc directive
     * @name compileHtmlModule.directive:compileHtml
     * @scope
     * @restrict A
     * @attribute compile-html
     *
     * @description
     * Directive responsible for evaluating and compiling HTML markup.
     *
     * @param {String} String HTML string to be evaluated and compiled.
     * @example
     * <pre>
     *      <div compile-html="<a data-ng-click=\"injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>"></div>
     * </pre>
     **/
    .directive('compileHtml', function($compile) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                scope.$watch(
                    function(scope) {
                        return scope.$eval(attrs.compileHtml);
                    },
                    function(value) {
                        element.html(value);
                        $compile(element.contents())(scope);
                    }
                );
            }
        };
    });

})();
