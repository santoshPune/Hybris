/**
 * @ngdoc overview
 * @name synchronizationServiceModule
 * @description
 *
 * The synchronization module contains the service and the directives necessary 
 * to perform catalog synchronization.
 *
 * The {@link synchronizationServiceModule.service:synchronizationService synchronizationService} 
 * calls backend API in order to get synchronization status or trigger a catalog synchronaization.
 *
 * The {@link synchronizationServiceModule.directive:synchronizeCatalog synchronizeCatalog} directive is used to display
 * the synchronization area in the landing page for each store.
 *
 */
angular.module('synchronizationServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule', 'alertServiceModule', 'authenticationModule'])
    /**
     * @ngdoc service
     * @name synchronizationServiceModule.service:synchronizationService
     * @description
     *
     * The synchronization service manages RESTful calls to the synchronization service's backend API.
     * 
     */
    .service('synchronizationService', function(restServiceFactory, $interval, $q, $translate, alertService, SYNC_PATH, authenticationService) {

        var INTERVAL_IN_MILLISECONDS = 5000;
        var restServiceForSync = restServiceFactory.get(SYNC_PATH, 'catalog');
        var intervalHandle = {};

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#updateCatalogSync
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method is used to synchronize a catalog.
         *
         * Note: synchronization is one-way from a staged catalog version to an online catalog version.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.updateCatalogSync = function(catalog) {
            return restServiceForSync.update({
                'catalog': catalog.catalogId
            }).then(function(response) {
                return response;
            }.bind(this), function(reason) {
                var translationErrorMsg = $translate.instant('sync.running.error.msg', {
                    catalogName: catalog.name
                });
                if (reason.statusText === 'Conflict') {
                    alertService.pushAlerts([{
                        successful: false,
                        message: translationErrorMsg,
                        closeable: true
                    }]);
                }
                return false;
            }.bind(this));
        };

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#getCatalogSyncStatus
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method is used to get the synchronization status of a catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.getCatalogSyncStatus = function(catalog) {
            return restServiceForSync.get({
                'catalog': catalog.catalogId
            });
        };


        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#startAutoGetSyncData
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method triggers the auto synchronization status update for a given catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         * @param {Function} callback The callback function that will be called upon catalog status update. 
         */
        this.startAutoGetSyncData = function(catalog, callback) {
            intervalHandle[catalog.catalogId] = $interval(function() {

                var url = SYNC_PATH.replace(":catalog", catalog.catalogId);
                authenticationService.isAuthenticated(url).then(
                    function(response) {
                        if (!response) {
                            this.stopAutoGetSyncData(catalog);
                        }
                        this.getCatalogSyncStatus(catalog)
                            .then(callback)
                            .then(function(response) {
                                if (!intervalHandle[catalog.catalogId]) {
                                    this.startAutoGetSyncData(catalog, callback);
                                }
                            }.bind(this));
                    }.bind(this));
            }.bind(this), INTERVAL_IN_MILLISECONDS);
        };

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#stopAutoGetSyncData
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method stops the auto synchronization status update for a given catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.stopAutoGetSyncData = function(catalog) {
            if (intervalHandle[catalog.catalogId]) {
                $interval.cancel(intervalHandle[catalog.catalogId]);
                intervalHandle[catalog.catalogId] = undefined;
            }
        };

    });
