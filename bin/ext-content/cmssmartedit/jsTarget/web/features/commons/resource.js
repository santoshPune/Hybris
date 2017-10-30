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
     * @name resourceModule
     *
     * @description
     * The resource module provides $resource factories.  
     */
    angular.module('resourceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        .factory('itemsResource', ['restServiceFactory', 'ITEMS_RESOURCE_URI', function(restServiceFactory, ITEMS_RESOURCE_URI) {
            return restServiceFactory.get(ITEMS_RESOURCE_URI);
        }])
        .factory('pagesContentSlotsComponentsResource', ['restServiceFactory', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {
            return restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);
        }]);
})();
