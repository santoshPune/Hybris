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
 *
 *
 */
angular.module('personalizationsmarteditcontainermodule')
    .directive('personalizationsmarteditRightToolbarItem', function() {
        return {
            templateUrl: 'web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemTemplate.html',
            restrict: 'E',
            transclude: true,
            link: function(scope, elem, attrs) {
                //none
            }
        };
    })
    .controller('personalizationsmarteditRightToolbarController', function($scope, $filter, $timeout, personalizationsmarteditRestService, personalizationsmarteditContextService, personalizationsmarteditMessageHandler, personalizationsmarteditPreviewService, personalizationsmarteditUtils, personalizationsmarteditIFrameUtils, personalizationsmarteditManager) {
        $scope.hidePersonalizationButtonI18nKey = "personalization.right.toolbar";
        $scope.customizationButtonI18nKey = "personalization.right.toolbar.customization.button";

        $scope.customizationsOnPage = [];
        $scope.allCustomizationsCount = undefined;

        $scope.search = {
            pageId: personalizationsmarteditContextService.getPageId(),
            libraryCustomizations: [],
            selectedLibraryCustomizations: [],
            searchCustomizationEnabled: false
        };

        $scope.pagination = {
            fixedPageSize: true,
            currentSize: 5
        };

        var removeArrayFromArrayByCode = function(fromArray, toRemoveArray) {
            var filteredArray = fromArray.filter(function(elem) {
                return toRemoveArray.map(function(e) {
                    return e.code;
                }).indexOf(elem.code) < 0;
            });

            return filteredArray;
        };

        var getCustomizations = function(categoryFilter) {
            personalizationsmarteditRestService.getCustomizations(categoryFilter)
                .then(function successCallback(response) {
                    if (angular.isUndefined($scope.allCustomizationsCount)) {
                        $scope.allCustomizationsCount = response.pagination.totalCount;
                    }
                    $scope.customizationsOnPage = response.customizations || [];

                    $scope.pagination.currentPage = response.pagination.page;
                    $scope.pagination.pages = Array(response.pagination.totalPages).join().split(',').map(function(item, index) {
                        return index;
                    });

                    $scope.search.libraryCustomizations.length = 0;
                    resetCustLibraryPagination($scope.libraryCustPagination);
                    $scope.addMoreCustomizationItems();
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                });
        };

        var getAndSetComponentsForVariation = function(customizationId, variationId) {
            personalizationsmarteditRestService.getComponenentsIdsForVariation(customizationId, variationId).then(function successCallback(response) {
                personalizationsmarteditContextService.setSelectedComponents(response.components);
            }, function errorCallback() {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponentsforvariation'));
            });
        };

        var updatePreviewTicket = function(customizationId, variationArray) {
            var previewTicketId = personalizationsmarteditContextService.getSePreviewData().previewTicketId;
            var variationKeys = personalizationsmarteditUtils.getVariationKey(customizationId, variationArray);
            personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicketId, variationKeys).then(function successCallback() {
                var previewData = personalizationsmarteditContextService.getSePreviewData();
                personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
            }, function errorCallback() {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
            });
        };

        var getCustomizationsFilterObject = function() {
            return {
                pageId: personalizationsmarteditContextService.getPageId(),
                currentSize: $scope.pagination.currentSize,
                currentPage: $scope.pagination.currentPage
            };
        };

        var getCustomizationsFilterObjectForLibrary = function() {
            return {
                pageId: personalizationsmarteditContextService.getPageId(),
                negatePageId: true,
                name: $scope.customizationFilter.name,
                currentSize: $scope.libraryCustPagination.count,
                currentPage: $scope.libraryCustPagination.page + 1
            };
        };

        var resetCustLibraryPagination = function(pagination) {
            pagination.count = 10;
            pagination.page = -1;
            pagination.totalPages = 1;
        };

        $scope.customizationFilter = {
            name: ''
        };

        $scope.libraryCustPagination = {};
        resetCustLibraryPagination($scope.libraryCustPagination);
        $scope.moreCustomizationsRequestProcessing = false;

        $scope.addMoreCustomizationItems = function() {
            if ($scope.libraryCustPagination.page < $scope.libraryCustPagination.totalPages - 1 && !$scope.moreCustomizationsRequestProcessing) {
                $scope.moreCustomizationsRequestProcessing = true;
                personalizationsmarteditRestService.getCustomizations(getCustomizationsFilterObjectForLibrary()).then(function successCallback(response) {
                    var filteredCategories = removeArrayFromArrayByCode(response.customizations, $scope.customizationsOnPage);
                    filteredCategories.forEach(function(customization) {
                        customization.fromLibrary = true;
                    });
                    Array.prototype.push.apply($scope.search.libraryCustomizations, filteredCategories);

                    $scope.libraryCustPagination = response.pagination;
                    $scope.moreCustomizationsRequestProcessing = false;
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                    $scope.moreCustomizationsRequestProcessing = false;
                });
            }
        };

        $scope.customizationSearchInputKeypress = function(keyEvent, searchObj) {
            resetCustLibraryPagination($scope.libraryCustPagination);
            $scope.customizationFilter.name = searchObj;
            $scope.search.libraryCustomizations.length = 0;
            $scope.addMoreCustomizationItems();
        };

        var refreshGrid = function() {
            $timeout(function() {
                getCustomizations(getCustomizationsFilterObject());
            }, 0);
        };

        $scope.variationClick = function(customization, variation) {
            personalizationsmarteditContextService.setSelectedVariations(variation);
            getAndSetComponentsForVariation(customization.code, variation.code);
            updatePreviewTicket(customization.code, [variation]);
        };

        $scope.customizationClick = function(customization) {
            var currentVariations = personalizationsmarteditContextService.selectedVariations;
            personalizationsmarteditContextService.setSelectedCustomizations(customization);
            personalizationsmarteditContextService.setSelectedVariations(customization.variations || null);
            if (customization.variations.length > 0) {
                var allVariations = personalizationsmarteditUtils.getVariationCodes(customization.variations).join(",");
                getAndSetComponentsForVariation(customization.code, allVariations);
            }
            if (angular.isObject(currentVariations) && !angular.isArray(currentVariations)) {
                updatePreviewTicket();
            }

            $scope.customizationsOnPage.filter(function(cust) {
                return customization.name !== cust.name;
            }).forEach(function(cust, index) {
                cust.collapsed = true;
            });
        };

        $scope.addCustomizationFromLibrary = function() {
            $scope.customizationsOnPage = $scope.customizationsOnPage.concat($scope.search.selectedLibraryCustomizations);
            $scope.search.libraryCustomizations = removeArrayFromArrayByCode($scope.search.libraryCustomizations, $scope.search.selectedLibraryCustomizations);
            $scope.search.selectedLibraryCustomizations = [];
            $scope.toggleAddMoreCustomizationsClick();
        };

        $scope.toggleAddMoreCustomizationsClick = function() {
            $scope.search.searchCustomizationEnabled = !$scope.search.searchCustomizationEnabled;
        };

        $scope.editCustomizationAction = function(customization) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code);
        };

        $scope.customizationClickAction = function(customization) {
            personalizationsmarteditRestService.getVariationsForCustomization(customization.code)
                .then(function successCallback(response) {
                    customization.variations = response.variations;
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        $scope.paginationCallback = function() {
            refreshGrid();
        };

        $scope.initCustomization = function(customization) {
            customization.collapsed = true;
            if ((personalizationsmarteditContextService.selectedCustomizations || {}).code === customization.code) {
                customization.collapsed = false;
            }
        };

        $scope.getSelectedVariationClass = function(variation) {
            if (angular.equals(variation, personalizationsmarteditContextService.selectedVariations)) {
                return "selectedVariation";
            }
        };

        refreshGrid();
    });
