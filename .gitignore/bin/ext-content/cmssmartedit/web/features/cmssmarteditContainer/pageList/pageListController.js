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
 * @name pageListModule
 * @description
 *
 * The page list module contains the controller associated to the page list view.
 *
 * This view displays the list of pages for a specific catalog and allows the user
 * to search and sort the list.
 *
 * Use the {@link pageListServiceModule.service:pageListService pageListService}
 * to call backend API in order to get the list of pages for a specific catalog
 *
 */
angular.module('pageListControllerModule', ['pageListServiceModule', 'functionsModule', 'resourceLocationsModule', 'addPageServiceModule', 'experienceServiceModule', 'eventServiceModule', 'resourceLocationsModule'])

/**
 * @ngdoc controller
 * @name pageListModule.controller:pageListController
 *
 * @description
 * The page list controller fetches pages for a specified catalog using the {@link pageListServiceModule.service:pageListService pageListService}
 */
.controller('pageListController', function($scope, $routeParams, $location, STOREFRONT_PATH_WITH_PAGE_ID, hitch, pageListService, catalogService, addPageWizardService, experienceService, sharedDataService, systemEventService, LANDING_PAGE_PATH) {

    this.siteUID = $routeParams.siteId;
    this.catalogId = $routeParams.catalogId;
    this.catalogVersion = $routeParams.catalogVersion;
    this.pages = [];
    this.catalogName = "";
    this.query = {
        value: ""
    };
    var filteredCatalog = [];

    this.reloadPages = function() {
        pageListService.getPageListForCatalog(this.siteUID, this.catalogId, this.catalogVersion).then(function(pageListResponse) {
            this.pages = pageListResponse.pages.map(function(page) {
                return {
                    name: page.name,
                    uid: page.uid,
                    typeCode: page.typeCode,
                    template: page.template
                };
            });
        }.bind(this));
    };
    this.reloadPages();

    catalogService.getCatalogsForSite(this.siteUID).then(function(catalogs) {

        filteredCatalog = catalogs.filter(hitch(this, function(catalog) {
            return catalog.catalogVersion === this.catalogVersion;
        }));

        if (filteredCatalog.length == 1) {
            this.catalogName = filteredCatalog[0].name;
        }

    }.bind(this));

    // renderers Object that contains custom HTML renderers for a given key
    this.renderers = {
        name: function(item, key) {
            return "<a data-ng-click=\"injectedContext.onLink( item.uid )\">{{ item[key.property] }}</a>";
        }
    };

    // injectedContext Object. This object is passed to the client-paged-list directive.
    this.injectedContext = {
        onLink: function(uid) {
            if (uid) {
                var experiencePath = this._buildExperiencePath(uid);
                //iFrameManager.setCurrentLocation(link);
                $location.path(experiencePath);
            }
        }.bind(this)
    };

    //needed for genericEditor invoked in addPageWizard
    experienceService.buildDefaultExperience($routeParams).then(function(experience) {
        sharedDataService.set('experience', experience).then(function(experience) {
            systemEventService.sendAsynchEvent("experienceUpdate");
        }, function(buildError) {
            $log.error("the provided path could not be parsed: " + $location.url());
            $log.error(buildError);
            $location.url(LANDING_PAGE_PATH);
        });
    });


    this._buildExperiencePath = function(uid) {
        return STOREFRONT_PATH_WITH_PAGE_ID
            .replace(":siteId", this.siteUID)
            .replace(":catalogId", this.catalogId)
            .replace(":catalogVersion", this.catalogVersion)
            .replace(":pageId", uid);
    };

    this.openAddPageWizard = function() {
        addPageWizardService.openAddPageWizard().then(function() {
            this.reloadPages();
        }.bind(this));
    };
});
