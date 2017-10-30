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
angular.module('experienceServiceModule', ['siteServiceModule', 'catalogServiceModule', 'languageServiceModule'])

/**
 * @ngdoc service
 * @name experienceServiceModule.service:experienceService
 *
 * @description
 * The experience Service deals with building experience objects given a context.
 */
.factory('experienceService', ['$q', 'siteService', 'catalogService', 'languageService', function($q, siteService, catalogService, languageService) {

    return {
        /**
         * @ngdoc method
         * @name experienceServiceModule.service:experienceService#buildDefaultExperience
         * @methodOf experienceServiceModule.service:experienceService
         *
         * @description
         * Given an object containing a siteId, catalogId and catalogVersion, will return a reconstructed experience
         *
         * @returns {object} an experience
         */
        buildDefaultExperience: function(params) {

            var siteId = params.siteId;
            var catalogId = params.catalogId;
            var catalogVersion = params.catalogVersion;


            return siteService.getSiteById(siteId).then(function(siteDescriptor) {
                return catalogService.getCatalogsForSite(siteId).then(function(catalogVersionDescriptors) {

                    var filteredCatalogVersionDescriptors = catalogVersionDescriptors.filter(function(catalogVersionDescriptor) {
                        return catalogVersionDescriptor.catalogId == catalogId && catalogVersionDescriptor.catalogVersion == catalogVersion;
                    });
                    if (filteredCatalogVersionDescriptors.length !== 1) {
                        return $q.reject("no catalogVersionDescriptor found for _catalogId_ catalogId and _catalogVersion_ catalogVersion".replace("_catalogId_", catalogId).replace("_catalogVersion_", catalogVersion));
                    }
                    return languageService.getLanguagesForSite(siteId).then(function(languages) {
                        // Set the selected experience in the shared data service

                        var defaultExperience = {
                            siteDescriptor: siteDescriptor,
                            catalogDescriptor: filteredCatalogVersionDescriptors[0],
                            languageDescriptor: languages[0],
                            time: null
                        };

                        if (params.pageId) {
                            defaultExperience.pageId = params.pageId;
                        }

                        return defaultExperience;
                    });
                });
            });
        }
    };

}]);
