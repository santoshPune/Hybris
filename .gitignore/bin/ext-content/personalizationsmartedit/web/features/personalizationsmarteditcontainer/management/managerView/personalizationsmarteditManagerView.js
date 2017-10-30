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
angular.module('personalizationsmarteditManagerViewModule', [
        'modalServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditRestServiceModule',
        'personalizationsmarteditContextServiceModule',
        'confirmationModalServiceModule',
        'personalizationsmarteditManagerModule',
        'eventServiceModule'
    ])
    .factory('personalizationsmarteditManagerView', function(modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES) {
        var manager = {};
        manager.openManagerAction = function() {
            modalService.open({
                title: "personalization.manager.modal.title",
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/managerView/personalizationsmarteditManagerViewTemplate.html',
                controller: 'personalizationsmarteditManagerViewController',
                size: 'fullscreen',
                cssClasses: 'manage-customization'
            }).then(function(result) {

            }, function(failure) {});
        };

        return manager;
    })
    .controller('personalizationsmarteditManagerViewController', function($q, $scope, $filter, $timeout, confirmationModalService, personalizationsmarteditRestService, personalizationsmarteditMessageHandler, personalizationsmarteditContextService, personalizationsmarteditManager, systemEventService) {

        var getCustomizations = function(filter) {
            personalizationsmarteditRestService.getCustomizations(filter)
                .then(function successCallback(response) {
                    if ($scope.isUndefined($scope.allCustomizationsCount)) {
                        $scope.allCustomizationsCount = response.pagination.totalCount;
                    }
                    $scope.customizations = response.customizations || [];
                    $scope.filteredCustomizationsCount = response.pagination.totalCount;
                    $scope.pagination.currentPage = response.pagination.page;
                    $scope.pagination.pages = Array(response.pagination.totalPages).join().split(',').map(function(item, index) {
                        return index;
                    });
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                });
        };

        var getVariations = function(customization, filter) {
            personalizationsmarteditRestService.getCustomization(customization.code)
                .then(function successCallback(response) {
                    customization.variations = response.variations;
                    $scope.customizationClickAction(customization);
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        var getCustomizationsFilterObject = function() {
            return {
                name: $scope.search.name,
                currentSize: $scope.pagination.currentSize,
                currentPage: $scope.pagination.currentPage
            };
        };

        var refreshGrid = function() {
            $timeout(function() {
                getCustomizations(getCustomizationsFilterObject());
            }, 0);
        };

        var currentLanguageIsocode = personalizationsmarteditContextService.getSeExperienceData().languageDescriptor.isocode;
        $scope.catalogName = personalizationsmarteditContextService.getSeExperienceData().catalogDescriptor.name[currentLanguageIsocode];
        $scope.catalogName += " - " + personalizationsmarteditContextService.getSeExperienceData().catalogDescriptor.catalogVersion;
        $scope.customizations = [];
        $scope.allCustomizationsCount = undefined;
        $scope.filteredCustomizationsCount = 0;

        $scope.search = {
            name: ''
        };

        $scope.pagination = {};

        $scope.searchInputKeypress = function(keyEvent) {
            if (keyEvent.which === 13 || $scope.search.name.length > 2 || $scope.search.name.length === 0) {
                $scope.pagination.currentPage = 0;
                refreshGrid();
            }
        };

        $scope.resetSearchInput = function() {
            $scope.search.name = "";
            refreshGrid();
        };

        $scope.editCustomizationAction = function(customization) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code);
        };
        $scope.deleteCustomizationAction = function(customization) {
            confirmationModalService.confirm({
                description: 'personalization.manager.modal.deletecustomization.content'
            }).then(function() {
                personalizationsmarteditRestService.deleteCustomization(customization.code)
                    .then(function successCallback(response) {
                        $scope.customizations.splice($scope.customizations.indexOf(customization), 1);
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.deletingcustomization'));
                    });
            });
        };
        $scope.editVariationAction = function(customization, variation) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code, variation.code);
        };
        $scope.deleteVariationAction = function(customization, variation, $event) {
            if (customization.variations.length >= 2) {
                confirmationModalService.confirm({
                    description: 'personalization.manager.modal.deletevariation.content'
                }).then(function() {
                    personalizationsmarteditRestService.deleteVariation(customization.code, variation.code)
                        .then(function successCallback(response) {
                            customization.variations.splice(customization.variations.indexOf(variation), 1);
                        }, function errorCallback() {
                            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.deletingvariation'));
                        });
                });
            } else {
                $event.stopPropagation();
            }
        };

        $scope.openNewModal = function() {
            personalizationsmarteditManager.openCreateCustomizationModal();
        };

        $scope.paginationCallback = function() {
            refreshGrid();
        };

        $scope.setCustomizationRank = function(customization, increaseValue, $event, firstOrLast) {
            if ($scope.isFilterEnabled() || firstOrLast) {
                $event.stopPropagation();
            } else {
                personalizationsmarteditRestService.getCustomization(customization.code)
                    .then(function successCallback(responseCustomization) {
                        responseCustomization.rank = responseCustomization.rank + increaseValue;
                        personalizationsmarteditRestService.updateCustomization(responseCustomization)
                            .then(function successCallback() {
                                refreshGrid();
                            }, function errorCallback() {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingcustomization'));
                            });
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                    });
            }
        };

        $scope.setVariationRank = function(customization, variation, increaseValue, $event, firstOrLast) {
            if (firstOrLast) {
                $event.stopPropagation();
            } else {
                personalizationsmarteditRestService.getVariation(customization.code, variation.code)
                    .then(function successCallback(responseVariation) {
                        responseVariation.rank = responseVariation.rank + increaseValue;
                        personalizationsmarteditRestService.editVariation(customization.code, responseVariation)
                            .then(function successCallback() {
                                getVariations(customization);
                            }, function errorCallback() {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.editingvariation'));
                            });
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingvariation'));
                    });
            }
        };

        $scope.isUndefined = function(value) {
            return value === undefined;
        };

        $scope.isFilterEnabled = function() {
            return $scope.filteredCustomizationsCount < $scope.allCustomizationsCount;
        };

        $scope.customizationClickAction = function(customization) {
            var filter = {
                includeActions: true
            };

            personalizationsmarteditRestService.getVariationsForCustomization(customization.code, filter).then(
                function successCallback(response) {
                    customization.variations = response.variations || [];
                    customization.variations.forEach(function(variation) {
                        variation.numberOfComponents = (variation.actions || []).length;
                    });
                },
                function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        systemEventService.registerEventHandler('CUSTOMIZATIONS_MODIFIED', function() {
            refreshGrid();
            return $q.when();
        });

        refreshGrid();

    });
