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
angular.module('fetchMediaDataHandlerModule', ['FetchDataHandlerInterfaceModule', 'experienceInterceptorModule', 'restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name fetchMediaDataHandlerModule.service:FetchMediaDataHandler
 * @description
 * implementation of {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface FetchDataHandlerInterface} for "Media" cmsStructureType
 */
.factory('fetchMediaDataHandler', ['FetchDataHandlerInterface', 'restServiceFactory', 'hitch', 'copy', 'extend', '$q', 'CONTEXT_CATALOG', 'CONTEXT_CATALOG_VERSION', 'MEDIA_PATH', 'MEDIA_RESOURCE_URI', function(FetchDataHandlerInterface, restServiceFactory, hitch, copy, extend, $q, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, MEDIA_PATH, MEDIA_RESOURCE_URI) {

    var FetchMediaDataHandler = function() {
        this.restServiceForMediaSearch = restServiceFactory.get(MEDIA_PATH);
        this.restServiceForMediaFetch = restServiceFactory.get(MEDIA_RESOURCE_URI);
    };

    FetchMediaDataHandler = extend(FetchDataHandlerInterface, FetchMediaDataHandler);

    /**
     * @ngdoc method
     * @name fetchMediaDataHandlerModule.service:FetchMediaDataHandler#findByMask
     * @methodOf fetchMediaDataHandlerModule.service:FetchMediaDataHandler
     * @description
     * implementation of {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#findBymask FetchDataHandlerInterface.findBymask} 
     * Given a field descriptor from {@link genericEditorModule.service:GenericEditor GenericEditor} and a mask
     * this method will query a REST API and returned a filtered list of media assets with the following format:
     * <ul>
     * 	<li><strong>id:</strong> Identifier of the media.</li>
     * 	<li><strong>url:</strong>  URL where the media is located.</li>
     * </ul>
     * <pre>
     * [{
     *		  id: 'someid1',
     *		  url: 'fullURLToMedia1'
     *	  }, {
     *		  id: 'someid2',
     *		  url: 'fullURLToMedia2'
     *    }]
     * </pre>
     *
     */
    FetchMediaDataHandler.prototype.findByMask = function(field, search) {
        var deferred = $q.defer();
        var params = {};
        params.namedQuery = "namedQueryMediaSearchByCodeCatalogVersion";
        params.params = "code:" + search + ",catalogId:" + CONTEXT_CATALOG + ",catalogVersion:" + CONTEXT_CATALOG_VERSION;

        this.restServiceForMediaSearch.get(params).then(
            function(response) {
                deferred.resolve(response.media);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    FetchMediaDataHandler.prototype.getById = function(field, identifier) {
        var deferred = $q.defer();
        this.restServiceForMediaFetch = restServiceFactory.get(MEDIA_RESOURCE_URI + "/" + identifier);

        this.restServiceForMediaFetch.get().then(
            function(response) {
                deferred.resolve(response);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    return new FetchMediaDataHandler();
}]);
