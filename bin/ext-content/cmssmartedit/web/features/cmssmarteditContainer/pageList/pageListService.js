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
 * @name pageListServiceModule
 * @description
 * # The pageListServiceModule
 *
 * The Page List Service module provides a service that fetches pages for a specified catalog
 *
 */
angular.module('pageListServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name pageListServiceModule.service:pageListService
 *
 * @description
 * The Page List Service fetches pages for a specified catalog using REST calls to the cmswebservices pages API.
 */
.factory('pageListService', function(PAGES_LIST_RESOURCE_URI, restServiceFactory, $q) {
    var pageRestService = restServiceFactory.get(PAGES_LIST_RESOURCE_URI);

    return {
        /**
         * @ngdoc method
         * @name pageListServiceModule.service:pageListService#getPageListForCatalog
         * @methodOf pageListServiceModule.service:pageListService
         *
         * @description
         * Fetches a list of pages for the catalog that corresponds to the specified site UID, catalogId and catalogVersion. The pages are
         * retrieved using REST calls to the cmswebservices pages API.
         *
         * @param {String} siteUID The UID of the site that the pages are to be fetched.
         * @param {String} catalogId The ID of the catalog that the pages are to be fetched.
         * @param {String} catalogVersion The version of the catalog that the pages are to be fetched.
         *
         * @returns {Array} An array of pages descriptors. Each descriptor provides the following pages properties:
         * creationtime, modifiedtime, pk, template, title, typeCode, uid.
         */
        getPageListForCatalog: function(siteUID, catalogId, catalogVersion) {

            return pageRestService.get({
                siteUID: siteUID,
                catalogId: catalogId,
                catalogVersion: catalogVersion
            }).then(function(pages) {
                return pages;
            });

        }

    };

});
