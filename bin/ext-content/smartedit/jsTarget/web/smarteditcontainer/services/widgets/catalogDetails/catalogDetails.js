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
    angular.module('catalogDetailsModule', ['resourceLocationsModule'])

    /**
     * @ngdoc service
     * @name catalogDetailsModule.service:catalogDetailsService
     * @description
     *
     * The catalog details Service will make it possible to add templates in form of directibe
     * to the catalog details directive 
     * 
     */
    .factory('catalogDetailsService', function() {

        var templates = [];

        /**
         * @ngdoc method
         * @name catalogDetailsModule.service:catalogDetailsService#addItems
         * @methodOf catalogDetailsModule.service:catalogDetailsService
         *
         * @description
         * This method allows to add a new item/items to the template array.
         *
         * @param {Array} Items An array that hold a list of templates .
         */
        var _addItems = function(Items) {
            templates = templates.concat(Items);
        };

        /**
         * @ngdoc method
         * @name catalogDetailsModule.service:catalogDetailsService#getItems
         * @methodOf catalogDetailsModule.service:catalogDetailsService
         *
         * @description
         * This method allows to get the list of items.
         *
         */
        var _getItems = function() {
            return templates;
        };

        return {
            addItems: _addItems,
            getItems: _getItems
        };
    })

    /**
     * @ngdoc directive
     * @name catalogDetailsModule.directive:catalogDetails
     * @scope
     * @restrict E
     * @element catalog-details
     *
     * @description
     * Directive responsible for displaying a catalog details
     *
     * redirects to the storefront page for the given default experience (siteId, catalogId, catalogVersionId)
     *
     * @param {String} catalog The catalog that needs to be displayed
     * */
    .directive('catalogDetails', ['$location', 'STOREFRONT_PATH', 'catalogDetailsService', function($location, STOREFRONT_PATH, catalogDetailsService) {
        return {
            templateUrl: 'web/smarteditcontainer/services/widgets/catalogDetails/catalogDetailsTemplate.html',
            restrict: 'E',
            transclude: false,
            scope: {
                catalog: '='
            },
            link: function(scope) {
                scope.onCatalogSelect = function(catalogVersionDescriptor) {
                    var pathWithExperience = STOREFRONT_PATH
                        .replace(":siteId", catalogVersionDescriptor.siteDescriptor.uid)
                        .replace(":catalogId", catalogVersionDescriptor.catalogId)
                        .replace(":catalogVersion", catalogVersionDescriptor.catalogVersion);
                    $location.path(pathWithExperience);
                };

                scope.templates = catalogDetailsService.getItems();
            }
        };
    }]);
})();
