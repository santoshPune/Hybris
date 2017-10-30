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
     * @name pageListLinkModule
     * @description
     * # The pageListLinkModule
     *
     * The pageListLinkModule provides a directive to display a link to the list of pages of a catalogue
     *
     */
    angular.module('pageListLinkModule', [])
        /**
         * @ngdoc directive
         * @name pageListLinkModule.directive:pageListLink
         * @scope
         * @restrict E
         * @element <page-list-link></page-list-link>
         *
         * @description
         * Directive that displays a link to the list of pages of a catalogue. It can only be used if catalog.catalogVersions is in the current scope.
         */
        .directive('pageListLink', function() {
            return {
                templateUrl: 'web/features/cmssmarteditContainer/pageList/pageListLinkDirectiveTemplate.html',
                restrict: 'E',
                transclude: true,
                $scope: {},
                link: function($scope, elem, attrs) {}
            };
        });
})();
