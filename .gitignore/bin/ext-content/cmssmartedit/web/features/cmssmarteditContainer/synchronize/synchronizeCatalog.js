/**
 * @ngdoc overview
 * @name synchronizeCatalogModule
 * @description
 *
 * The synchronization module contains the service and the directives necessary 
 * to perform catalog synchronization.
 *
 * The {@link synchronizeCatalogModule.service:synchronizationService synchronizationService} 
 * calls backend API in order to get synchronization status or trigger a catalog synchronaization.
 *
 * The {@link synchronizeCatalogModule.directive:synchronizeCatalog synchronizeCatalog} directive is used to display
 * the synchronization area in the landing page for each store.
 *
 */
angular.module('synchronizeCatalogModule', ['confirmationModalServiceModule', 'synchronizationServiceModule', 'l10nModule'])

.controller('synchronizeCatalogController', function($scope, synchronizationService, $q, confirmationModalService, l10nFilter) {

    this.syncInitiatedFlag = false;
    this.syncData = {
        showSyncBtn: false,
        lastSyncTS: '',
        startSyncTS: '',
        showFailure: false
    };

    var updateSyncData = function(response) {
        this.syncInitiatedFlag = ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? true : false);
        this.syncData = {
            showSyncBtn: ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? false : true),
            lastSyncTS: (response.endDate),
            startSyncTS: (response.creationDate ? response.creationDate : "NEVER"),
            showFailure: ((response.syncResult == "ERROR" || response.syncResult == "FAILURE") ? true : false)
        };
    }.bind(this);

    // update catalog synchronization status
    var invokeGetSyncData = function() {
        synchronizationService
            .getCatalogSyncStatus(this.catalog)
            .then(function(response) {
                updateSyncData(response);
            }.bind(this));
    }.bind(this);

    var onDestroyStopAutoGetSyncData = function() {
        // on destroy, cancel auto update
        $scope.$on('$destroy', function() {
            synchronizationService.stopAutoGetSyncData(this.catalog);
        }.bind(this));
    }.bind(this);

    this.syncCatalog = function() {
        var catalogName = l10nFilter(this.catalog.name);
        confirmationModalService.confirm({
            description: 'sync.confirm.msg',
            title: 'sync.confirmation.title',
            descriptionPlaceholders: {
                catalogName: catalogName
            }
        }).then(function() {
            synchronizationService.updateCatalogSync(this.catalog).then(function(response) {
                updateSyncData(response);
            });
        }.bind(this));
    };

    // on init, start auto updating synchronization data
    synchronizationService.startAutoGetSyncData(this.catalog, updateSyncData);
    // on destroy, stop auto update
    onDestroyStopAutoGetSyncData();
    // call the update for the first time. 
    invokeGetSyncData();
})

/**
 * @ngdoc directive
 * @name synchronizationServiceModule.directive:synchronizeCatalog
 * @restrict E
 * @element synchronize-catalog
 *
 * @description
 * The Synchronize Catalog directive is used to display the synchronization area in the landing page for 
 * each catalog and to update the status of the synchronization button.
 *
 * The directive provides communication by exposing the (syncCatalog) method. When called, the directive 
 * accesses the synchronization service to synchronize a specified catalog.
 *
 * @param {Object} catalog An object that contains the catalog details. It allows the directive 
 * to load or update the synchronization data for a specified catalog. 
 * @param {Function} syncCatalog A function that triggers the synchronization of a catalog using the synchronization service. 
 * It invokes a modal pop-up to confirm the action before it calls the service layer.
 * 
 */
.directive('synchronizeCatalog', function() {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/synchronize/synchronizationTemplate.html',
        restrict: 'E',
        controller: 'synchronizeCatalogController',
        controllerAs: 'ctrl',
        scope: {},
        bindToController: {
            catalog: '='
        }
    };
});
