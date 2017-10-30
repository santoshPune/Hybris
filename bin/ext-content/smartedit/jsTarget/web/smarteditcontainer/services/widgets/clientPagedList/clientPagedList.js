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
     * @name clientPagedListModule
     * @description
     * # The clientPagedListModule
     *
     * The clientPagedListModule provides a directive to display a paginated list of items with custom renderers.
     * This directive also allows the user to search and sort the list.
     *
     */
    angular.module('clientPagedListModule', ['pascalprecht.translate', 'ui.bootstrap', 'ngSanitize', 'paginationFilterModule', 'iFrameManagerModule', 'compileHtmlModule'])

    /**
     * @ngdoc directive
     * @name clientPagedListModule.directive:clientPagedList
     * @scope
     * @restrict E
     * @element client-paged-list
     *
     * @description
     * Directive responsible for displaying a client-side paginated list of items with custom renderers. It allows the user to search and sort the list.
     *
     * @param {Array} items An array of item descriptors.
     * @param {Array} keys An array of object(s) with a property and an i18n key.
     * The properties must match one at least one of the descriptors' keys and will be used as the columns of the table. The related i18n keys are used for the column headers' title.
     * @param {Object} renderers An object that contains HTML renderers for specific keys property. A renderer is a function that returns a HTML string. This function has access to the current item and key.
     * @param {Object} injectedContext An object that exposes values or functions to the directive. It can be used by the custom HTML renderers to bind a function to a click event for example.
     * @param {Boolean} reversed If set to true, the list will be sorted descending.
     * @param {Number} itemsPerPage The number of items to display per page.
     * @param {Object} query The ngModel query object used to filter the list.
     * @param {Boolean} displayCount If set to true the size of the filtered collection will be displayed.
     *
     * @example
     * <pre>
     *          <client-paged-list items="pageListCtl.pages"
     *                      keys="[{
     *                              property:'title',
     *                              i18n:'pagelist.headerpagetitle'
     *                              },{
     *                              property:'uid',
     *                              i18n:'pagelist.headerpageid'
     *                              },{
     *                              property:'typeCode',
     *                              i18n:'pagelist.headerpagetype'
     *                              },{
     *                              property:'template',
     *                              i18n:'pagelist.headerpagetemplate'
     *                              }]"
     *                      renderers="pageListCtl.renderers"
     *                      injectedContext="pageListCtl.injectedContext"
     *                      sort-by="'title'"
     *                      reversed="true"
     *                      items-per-page="10"
     *                      query="pageListCtl.query.value"
     *                      display-count="true"
     *            ></paged-list>
     * </pre>
     *
     * <em>Example of a <strong>renderers</strong> object</em>
     *
     * <pre>
     *          renderers = {
     *              name: function(item, key) {
     *                  return "<a data-ng-click=\"injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>";
     *              }
     *          };
     * </pre>
     *
     * <em>Example of an <strong>injectedContext</strong> object</em>
     * <pre>
     *          injectedContext = {
     *              onLink: hitch(this, function(link) {
     *                  if (link) {
     *                      var experiencePath = this._buildExperiencePath();
     *                      iFrameManager.setCurrentLocation(link);
     *                      $location.path(experiencePath);
     *                  }
     *              })
     *          };
     * </pre>
     *
     * */
    .directive('clientPagedList', ['$filter', function($filter) {

        return {
            templateUrl: 'web/smarteditcontainer/services/widgets/clientPagedList/clientPagedList.html',
            restrict: 'E',
            transclude: false,
            scope: {
                items: '=',
                itemsPerPage: '=',
                totalItems: '=',
                keys: '=',
                renderers: "=",
                injectedContext: '=',
                identifier: '=',
                sortBy: '=',
                reversed: '=',
                query: "=",
                displayCount: "="
            },
            link: function(scope, element, attr) {

                scope.totalItems = 0;
                scope.currentPage = 1;
                scope.filteredItems = [];

                scope.columnWidth = 100 / (scope.keys.length);
                scope.columnToggleReversed = scope.reversed;

                scope.headersSortingState = {};
                scope.headersSortingState[scope.sortBy] = scope.reversed;
                scope.visibleSortingHeader = scope.sortBy;

                scope.$watch('items', function() {
                    scope.totalItems = scope.items.length;
                    scope.filteredItems = $filter('orderBy')(scope.items, scope.sortBy, scope.reversed);
                });

                scope.$watch('query', function() {
                    if (scope.query === "" && scope.items.length > 0) {
                        scope.filteredItems = $filter('orderBy')(scope.items, scope.sortBy, scope.reversed);
                        refreshPagination();
                    } else if (scope.items.length > 0) {
                        scope.filteredItems = $filter('filter')($filter('orderBy')(scope.items, scope.sortBy, scope.columnToggleReversed), scope.query);
                        refreshPagination();
                    }
                });

                scope.orderByColumn = function(columnKey) {
                    scope.columnToggleReversed = !scope.columnToggleReversed;
                    scope.headersSortingState[columnKey] = scope.columnToggleReversed;
                    scope.visibleSortingHeader = columnKey;
                    scope.filteredItems = $filter('orderBy')(scope.filteredItems, columnKey, scope.columnToggleReversed);
                };

                function refreshPagination() {
                    scope.totalItems = scope.filteredItems.length;
                }

            }
        };

    }]);

})();
