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
 * @name yInfiniteScrollingModule
 * @description
 * <h1>This module holds the base web component to perform infinite scrolling from paged backend</h1>
 */
angular.module('yInfiniteScrollingModule', ['infinite-scroll', 'functionsModule'])
    //value used by third party infinite-scroll and used here when typing new value for mask
    /**
     * @ngdoc object
     * @name yInfiniteScrollingModule.object:THROTTLE_MILLISECONDS
     *
     * @description
     * Configures the {@link yInfiniteScrollingModule.directive:yInfiniteScrolling yInfiniteScrolling} directive to throttle the page fetching with the value provided in milliseconds.
     */
    .value('THROTTLE_MILLISECONDS', 250)
    .controller('yInfiniteScrollingController', function($scope, throttle, THROTTLE_MILLISECONDS, generateIdentifier, $timeout) {

        this.initiated = false;
        this.containerId = generateIdentifier();
        this.containerIdSelector = "#" + this.containerId;

        this.context = this.context || this;

        this.reset = function() {
            this.distance = this.distance || 0;
            this.context.items = [];
            this.currentPage = -1;
            this.pagingDisabled = false;
            //necessary so that infinite-scroll directive can rely on resolved container id
            if (!this.container) {
                $timeout(function() {
                    this.container = $(this.containerIdSelector).get(0);
                    this.initiated = true;
                }.bind(this), 0);
            }
        };

        this.nextPage = function() {
            if (this.pagingDisabled) {
                return;
            }
            this.pagingDisabled = true;
            this.currentPage++;
            this.mask = this.mask || "";
            this.fetchPage(this.mask, this.pageSize, this.currentPage).then(function(page) {
                Array.prototype.push.apply(this.context.items, page.results);
                this.pagingDisabled = page.results.length === 0 || this.context.items.length == page.pagination.totalCount;
            }.bind(this));
        }.bind(this);

        this.$onChanges = throttle(function() {
            var wasInitiated = this.initiated;
            this.reset();
            if (wasInitiated) {
                this.nextPage();
            }
        }.bind(this), THROTTLE_MILLISECONDS);

        $scope.$watch(function() {
            return this.mask;
        }.bind(this), this.$onChanges);

    })
    /**
     * @ngdoc object
     * @name yInfiniteScrollingModule.object:Page
     * @description
     * An object representing the backend response to a paged query
     */
    /**
     * @ngdoc property
     * @name results
     * @propertyOf yInfiniteScrollingModule.object:Page
     * @description
     * The array containing the elements pertaining to the requested page, its size will not exceed the requested page size.
     **/
    /**
     * @ngdoc property
     * @name pagination
     * @propertyOf yInfiniteScrollingModule.object:Page
     * @description
     * The returned {@link yInfiniteScrollingModule.object:Pagination Pagination} object
     */

/**
 * @ngdoc object
 * @name yInfiniteScrollingModule.object:Pagination
 * @description
 * An object representing the returned pagination information from backend
 */

/**
 * @ngdoc property
 * @name totalCount
 * @propertyOf yInfiniteScrollingModule.object:Pagination
 * @description
 * the total of elements matching the given mask once all pages are returned
 **/

/**
 * @ngdoc directive
 * @name yInfiniteScrollingModule.directive:yInfiniteScrolling
 * @scope
 * @restrict E
 *
 * @description
 * A directive that you can use to implement infinite scrolling for an expanding content (typically with a ng-repeat) nested in it.
 * It is meant to handle paginated requests from a backend when data is expected to be large.
 * Since the expanding content is a <b>transcluded</b> element, we must specify the context to which the items will be attached:
 * If context is myContext, each pagination will push its new items to myContext.items.
 * @param {String} pageSize The maximum size of each page requested from the backend.
 * @param {String} mask A string value sent to the server upon fetching a page to further restrict the search, it is sent as query string "mask".
 * <br>The directive listens for change to mask and will reset the scroll and re-fetch data.
 * <br/>It it left to the implementers to decide what it filters on
 * @param {String} distance A number representing how close the bottom of the element must be to the bottom of the container before the expression specified by fetchPage function is triggered. Measured in multiples of the container height; for example, if the container is 1000 pixels tall and distance is set to 2, the infinite scroll expression will be evaluated when the bottom of the element is within 2000 pixels of the bottom of the container. Defaults to 0 (e.g. the expression will be evaluated when the bottom of the element crosses the bottom of the container).
 * @param {Object} context The container object to which the items of the fetched {@link yInfiniteScrollingModule.object:Page Page} will be added
 * @param {Function} fetchPage function to fetch the next page when the bottom of the element approaches the bottom of the container.
 *        fetchPage will be invoked with 3 arguments : <b>mask, pageSize, currentPage</b>. The currentPage is determined by the scrolling and starts with 0. The function must return a page of type {@link yInfiniteScrollingModule.object:Page Page}.
 * @param {String} dropDownContainerClass An optional CSS class to be added to the container of the dropdown. It would typically be used to override the default height. <b>The resolved CSS must set a height (or max-height) and overflow-y:scroll.</b>
 * @param {String} dropDownClass An optional CSS class to be added to the dropdown. <b>Neither height nor overflow should be set on the dropdown, it must be free to fill up the space and reach the container size. Failure to do so will cause the directive to call nextPage as many times as the number of available pages on the server.</b>
 */
.directive(
    'yInfiniteScrolling',
    function() {
        return {
            templateUrl: 'web/common/services/infiniteScrolling/yInfiniteScrollingTemplate.html',
            transclude: true,
            replace: true,
            controller: 'yInfiniteScrollingController',
            controllerAs: 'scroll',
            scope: {},
            bindToController: {
                pageSize: '=',
                mask: '=?',
                fetchPage: '=',
                distance: '=?',
                context: '=?',
                dropDownContainerClass: '@?',
                dropDownClass: '@?'
            }
        };
    }
);
