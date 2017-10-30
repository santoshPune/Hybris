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
angular.module('componentServiceModule', ['restServiceFactoryModule', 'functionsModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name componentMenuModule.ComponentService
     *
     * @description
     * Service which manages component types and items
     */
    .factory('ComponentService', ['restServiceFactory', 'hitch', '$q', '$log', 'parseQuery', 'TYPES_RESOURCE_URI', 'ITEMS_RESOURCE_URI', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, hitch, $q, $log, parseQuery, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {

        var restServiceForTypes = restServiceFactory.get(TYPES_RESOURCE_URI);
        var restServiceForItems = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddNewComponent = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddExistingComponent = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);

        var _typesLoaded = false;
        var _itemsLoaded = false;
        var _listOfComponentTypes = {};
        var _listOfComponentItems = {};
        var _payload = {};

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addNewComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description given a component type and a slot id, a new componentItem is created and added to a slot
         *
         * @param {String} componenCode of the ComponentType to be created and added to the slot.
         * @param {String} componentName of the new component to be created.
         * @param {String} pageId used to identify the current page template.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addNewComponent = function(componentName, componentCode, pageId, slotId, position) {

            var deferred = $q.defer();

            _payload.name = componentName;
            _payload.slotId = slotId;
            _payload.pageId = pageId;
            _payload.position = position;
            _payload.typeCode = componentCode;

            restServiceForAddNewComponent.save(_payload).then(
                function(response) {
                    deferred.resolve(response);
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addExistingComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description add an existing component item to a slot
         *
         * @param {String} pageId used to identify the page containing the slot in the current template.
         * @param {String} componentId used to identify the existing component which will be added to the slot.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addExistingComponent = function(pageId, componentId, slotId, position) {

            var deferred = $q.defer();

            _payload.pageId = pageId;
            _payload.slotId = slotId;
            _payload.componentId = componentId;
            _payload.position = position;

            restServiceForAddExistingComponent.save(_payload).then(
                function(response) {
                    deferred.resolve();
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentTypes
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all component types are retrieved
         */
        var _loadComponentTypes = function() {

            var deferred = $q.defer();

            restServiceForTypes.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentTypes);
                    deferred.resolve(_listOfComponentTypes);
                    _typesLoaded = true;
                    return deferred.promise;

                },
                function() {
                    _typesLoaded = false;
                    deferred.reject();
                    return deferred.promise;
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItem
         * @methodOf componentMenuModule.ComponentService
         *
         * @description load a component identified by its id
         */
        var _loadComponentItem = function(id) {
            return restServiceForItems.getById(id);
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved
         */
        var _loadComponentItems = function() {

            var deferred = $q.defer();

            restServiceForItems.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentItems);
                    deferred.resolve(_listOfComponentItems);
                    _itemsLoaded = true;
                },
                function() {
                    _itemsLoaded = false;
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadPagedComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved in the form of pages
         * used for pagination especially when the result set is very large.
         * 
         * @param {String} mask the search string to filter the results.
         * @param {String} pageSize the number of elements that a page can contain.
         * @param {String} page the current page number.
         */
        var _loadPagedComponentItems = function(mask, pageSize, page) {

            return restServiceForItems.get({
                pageSize: pageSize,
                currentPage: page,
                mask: mask,
                sort: 'name'
            });
        };

        return {
            loadComponentTypes: _loadComponentTypes,
            listOfComponentTypes: _listOfComponentTypes,
            loadComponentItem: _loadComponentItem,
            loadComponentItems: _loadComponentItems,
            loadPagedComponentItems: _loadPagedComponentItems,
            listOfComponentItems: _listOfComponentItems,
            addNewComponent: _addNewComponent,
            addExistingComponent: _addExistingComponent,
        };

    }]);
