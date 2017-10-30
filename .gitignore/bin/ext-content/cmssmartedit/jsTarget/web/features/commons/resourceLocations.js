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

    var CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    var CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
    var CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

    angular.module('resourceLocationsModule')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:TYPES_RESOURCE_URI
     *
     * @description
     * Resource URI of the component types REST service.
     */
    .constant('TYPES_RESOURCE_URI', '/cmswebservices/v1/types')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:ITEMS_RESOURCE_URI
     *
     * @description
     * Resource URI of the custom components REST service.
     */
    .constant('ITEMS_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages content slot component REST service.
     */
    .constant('PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslotscomponents')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI
     *
     * @description
     * Resource URI of the content slot type restrictions REST service.
     */
    .constant('CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI', '/cmswebservices/v1/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pages/:pageUid/contentslots/:slotUid/typerestrictions')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_LIST_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages REST service.
     */
    .constant('PAGES_LIST_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pages')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_LIST_PATH
     *
     * @description
     * Path of the page list
     */
    .constant('PAGE_LIST_PATH', '/pages/:siteId/:catalogId/:catalogVersion')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_RESOURCE_URI
     *
     * @description
     * Resource URI of the page content slots REST service
     */
    .constant('PAGES_CONTENT_SLOT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslots')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_TEMPLATES_URI
     *
     * @description
     * Resource URI of the page templates REST service
     */
    .constant('PAGE_TEMPLATES_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pagetemplates');

})();
