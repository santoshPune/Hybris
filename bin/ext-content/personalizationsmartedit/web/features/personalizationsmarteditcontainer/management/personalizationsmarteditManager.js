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
angular.module('personalizationsmarteditManagerModule', [
        'modalServiceModule',
        'coretemplates',
        'ui.select',
        'confirmationModalServiceModule',
        'functionsModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditManagementServiceModule',
        'eventServiceModule'
    ])
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS', {
        BASIC_INFO_TAB_NAME: 'basicinfotab',
        BASIC_INFO_TAB_FORM_NAME: 'form.basicinfotab',
        TARGET_GROUP_TAB_NAME: 'targetgrptab',
        TARGET_GROUP_TAB_FORM_NAME: 'form.targetgrptab'
    })
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS', {
        CONFIRM_OK: 'confirmOk',
        CONFIRM_CANCEL: 'confirmCancel',
        CONFIRM_NEXT: 'confirmNext'
    })
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY', {
        CRITERIA_AND: 'AND',
        CRITERIA_OR: 'OR'
    })
    .factory('personalizationsmarteditManager', function($controller, modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS) {

        var manager = {};

        manager.openCreateCustomizationModal = function() {
            return modalService.open({
                title: 'personalization.modal.customizationvariationmanagement.title',
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.modalManager = modalManager;
                    angular.extend(this, $controller('personalizationsmarteditManagerController', {
                        $scope: $scope
                    }));
                }],
                buttons: [{
                    id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL,
                    label: 'personalization.modal.button.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY
                }],
                size: 'lg modal_bigger'
            });
        };

        manager.openEditCustomizationModal = function(customizationCode, variationCode) {
            return modalService.open({
                title: 'personalization.modal.customizationvariationmanagement.title',
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.customizationCode = customizationCode;
                    $scope.variationCode = variationCode;
                    $scope.modalManager = modalManager;
                    angular.extend(this, $controller('personalizationsmarteditManagerController', {
                        $scope: $scope
                    }));
                }],
                buttons: [{
                    id: 'confirmCancel',
                    label: 'personalization.modal.button.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY
                }],
                size: 'lg modal_bigger'
            });
        };

        return manager;
    })
    .controller('personalizationsmarteditManagerController', function($scope, hitch, $q, $log, $timeout, personalizationsmarteditManagementService, personalizationsmarteditMessageHandler, personalizationsmarteditUtils, confirmationModalService, $filter, MODAL_BUTTON_ACTIONS, CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS, CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS, systemEventService, CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY) {
        var self = this;

        var getVariationsForCustomization = function(customizationCode) {
            var filter = {
                includeTriggers: true
            };

            return personalizationsmarteditManagementService.getVariationsForCustomization(customizationCode, filter);
        };

        self.isModallDirty = false;

        $scope.form = {};

        $scope.customization = {
            code: '',
            description: '',
            rank: 0,
            variations: []
        };

        $scope.tabsArr = [{
            name: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME,
            active: true,
            disabled: false,
            heading: $filter('translate')("personalization.modal.customizationvariationmanagement.basicinformationtab"),
            template: 'web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagBasicInfoTemplate.html',
            formName: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_FORM_NAME,
            isDirty: function() {
                return $scope.form.basicinfotab && $scope.form.basicinfotab.$dirty;
            },
            setPristine: function() {
                $scope.form.basicinfotab.$setPristine();
            },
            isValid: function() {
                return $scope.form.basicinfotab && $scope.form.basicinfotab.$valid;
            },
            setEnabled: function(enabled) {
                if (enabled) {
                    $scope.tabsArr[1].disabled = false;
                    $scope.modalManager.enableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                } else {
                    $scope.tabsArr[1].disabled = true;
                    $scope.modalManager.disableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                }
            },
            onConfirm: function() {
                $scope.tabsArr[1].active = true;
            },
            onCancel: function() {
                self.onCancel();
            }
        }, {
            name: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME,
            active: false,
            disabled: true,
            heading: $filter('translate')("personalization.modal.customizationvariationmanagement.targetgrouptab"),
            template: 'web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagTargetGrpTemplate.html',
            formName: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_FORM_NAME,
            isDirty: function() {
                return ($scope.form.targetgrptab && $scope.form.targetgrptab.$dirty) || $scope.edit.variationsListDirty;
            },
            setPristine: function() {
                $scope.form.targetgrptab.$setPristine();
            },
            isValid: function() {
                var isVariationListValid = $scope.customization.variations.length > 0;
                return ($scope.form.targetgrptab && $scope.form.targetgrptab.$valid) && isVariationListValid;
            },
            setEnabled: function(enabled) {
                if (enabled) {
                    $scope.modalManager.enableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                } else {
                    $scope.modalManager.disableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                }
            },
            onConfirm: function() {
                self.onSave();
            },
            onCancel: function() {
                self.onCancel();
            }
        }];

        $scope.edit = {
            code: '',
            name: '',
            segments: [],
            selectedSegments: [],
            allSegmentsChecked: true,
            variationsListChanged: false,
            selectedTab: $scope.tabsArr[0],
            variationsLoaded: false
        };

        $scope.editMode = angular.isDefined($scope.customizationCode);

        if ($scope.editMode) {
            personalizationsmarteditManagementService.getCustomization($scope.customizationCode).then(function successCallback(response) {
                $scope.customization = response;

                if (angular.isDefined($scope.variationCode)) {

                    getVariationsForCustomization($scope.customizationCode).then(function successCallback(response) {
                        $scope.customization.variations = response.variations;
                        $scope.edit.variationsLoaded = true;

                        var filteredCollection = $scope.customization.variations.filter(function(elem) {
                            return elem.code === $scope.variationCode;
                        });

                        if (filteredCollection.length > 0) {

                            $scope.tabsArr[1].active = true;
                            $scope.edit.selectedTab = $scope.tabsArr[1];

                            var selVariation = filteredCollection[0];
                            $scope.edit.selectedVariation = selVariation;
                        }

                    }, function errorCallback(response) {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    });
                } else {
                    $scope.edit.selectedTab = $scope.tabsArr[0];
                }
            }, function errorCallback(response) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponents'));
            });
        }

        $scope.selectTab = function(tab) {
            $scope.edit.selectedTab = tab;
            tab.active = true;
            switch (tab.name) {
                case CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME:
                    $scope.modalManager.removeButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                    $scope.modalManager.addButton({
                        id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT,
                        label: 'personalization.modal.button.next'
                    });
                    break;
                case CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME:
                    $scope.modalManager.removeButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                    $scope.modalManager.addButton({
                        id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK,
                        label: 'personalization.modal.button.submit',
                        action: MODAL_BUTTON_ACTIONS.CLOSE
                    });
                    break;
                default:
                    break;
            }
        };

        self.onSave = function() {

            if ($scope.editMode) {
                personalizationsmarteditManagementService.updateCustomizationPackage($scope.customization).then(function successCallback(response) {
                    systemEventService.sendSynchEvent('CUSTOMIZATIONS_MODIFIED', {});
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.updatingcustomization'));
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingcustomization'));
                });
            } else {
                personalizationsmarteditManagementService.createCustomization($scope.customization).then(function successCallback(response) {
                    systemEventService.sendSynchEvent('CUSTOMIZATIONS_MODIFIED', {});
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.creatingcustomization'));
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingcustomization'));
                });
            }

        };

        self.onCancel = function() {
            var deferred = $q.defer();
            if (this.isModallDirty) {
                confirmationModalService.confirm({
                    description: $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.cancelconfirmation')
                }).then(function() {
                    $scope.modalManager.dismiss();
                    deferred.resolve();
                }, function() {
                    deferred.reject();
                });
            } else {
                $scope.modalManager.dismiss();
                deferred.resolve();
            }

            return deferred.promise;
        };

        var getAllSegmentSelectedForVariation = function(variation) {
            var segmentTrigger = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation);
            return segmentTrigger.groupBy === CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND;
        };

        self.init = function() {
            $scope.modalManager.setDismissCallback(hitch(this, this.onCancel));

            $scope.modalManager.setButtonHandler(hitch(this, function(buttonId) {
                switch (buttonId) {
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK:
                        return $scope.edit.selectedTab.onConfirm();
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT:
                        return $scope.edit.selectedTab.onConfirm();
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL:
                        return $scope.edit.selectedTab.onCancel();
                    default:
                        $log.error($filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.invalidbuttonid'), buttonId);
                        break;
                }
            }));

            $scope.$watch(hitch(this, function() {
                var isSelectedTabDirty = $scope.edit.selectedTab.isDirty();
                var isSelectedTabValid = $scope.edit.selectedTab.isValid();
                return {
                    isDirty: isSelectedTabDirty,
                    isValid: isSelectedTabValid
                };
            }), hitch(this, function(obj) {
                if (obj.isDirty) {
                    self.isModallDirty = true;
                    if (obj.isValid) {
                        $scope.edit.selectedTab.setEnabled(true);
                    } else {
                        $scope.edit.selectedTab.setEnabled(false);
                    }
                } else if ($scope.editMode) {
                    if (obj.isValid) {
                        $scope.edit.selectedTab.setEnabled(true);
                    } else {
                        $scope.edit.selectedTab.setEnabled(false);
                    }
                } else {
                    self.isModallDirty = false;
                    $scope.edit.selectedTab.setEnabled(false);
                }
            }), true);

            $scope.$watch('customization.variations', function() {
                $scope.edit.variationsListDirty = true;
            }, true);

            $scope.$watch('edit.selectedVariation', function(variation) {
                if (variation) {
                    $scope.edit.code = variation.code;
                    $scope.edit.name = variation.name;
                    $scope.edit.selectedSegments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments;
                    $scope.edit.allSegmentsChecked = getAllSegmentSelectedForVariation(variation);
                } else {
                    $scope.edit.code = '';
                    $scope.edit.name = '';
                    $scope.edit.selectedSegments = [];
                    $scope.edit.allSegmentsChecked = true;
                }
            }, true);

            $scope.$watch('edit.selectedTab', function() {
                if ($scope.editMode && !$scope.edit.variationsLoaded && ($scope.edit.selectedTab && $scope.edit.selectedTab.name === CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME)) {

                    getVariationsForCustomization($scope.customizationCode).then(function successCallback(response) {
                        $scope.customization.variations = response.variations;
                        $scope.edit.variationsLoaded = true;
                    }, function errorCallback(response) {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    });
                }
            }, true);
        };
    })
    .directive('negate', [
        function() {
            return {
                require: 'ngModel',
                link: function(scope, element, attribute, ngModelController) {
                    ngModelController.$isEmpty = function(value) {
                        return !!value;
                    };

                    ngModelController.$formatters.unshift(function(value) {
                        return !value;
                    });

                    ngModelController.$parsers.unshift(function(value) {
                        return !value;
                    });
                }
            };
        }
    ])
    .directive('uniquetargetgroupname',
        function(isBlank) {
            var isNameTheSameAsEditedTargetGroup = function(scope, targetGroupName) {
                return scope.edit.selectedVariation && targetGroupName === scope.edit.selectedVariation.code;
            };

            return {
                restrict: "A",
                require: "ngModel",
                scope: false,
                link: function(scope, element, attributes, ctrl) {
                    ctrl.$validators.uniquetargetgroupname = function(modelValue) {
                        if (isBlank(modelValue) || isNameTheSameAsEditedTargetGroup(scope, modelValue)) {
                            return true;
                        } else {
                            return scope.customization.variations.filter(function(e) {
                                return e.code === modelValue;
                            }).length === 0;
                        }
                    };
                }
            };
        }
    );
