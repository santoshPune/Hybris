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
angular.module('ui.select')
    .run(["$templateCache", function($templateCache) {
        $templateCache.put("select2personalization/choices.tpl.html", $templateCache.get("select2/choices.tpl.html"));
        var fixedTemplate = $templateCache.get("select2/match-multiple.tpl.html").replace("$item in $select.selected", "$item in $select.selected track by $index");
        $templateCache.put("select2personalization/match-multiple.tpl.html", fixedTemplate);
        $templateCache.put("select2personalization/match.tpl.html", $templateCache.get("select2/match.tpl.html"));
        $templateCache.put("select2personalization/select-multiple.tpl.html", $templateCache.get("select2/select-multiple.tpl.html"));
        $templateCache.put("select2personalization/select.tpl.html", $templateCache.get("select2/select.tpl.html"));

    }]);

angular.module('personalizationsmarteditManagerModule')
    .controller('personalizationsmarteditManagerTargetGrpController', ['$scope', 'isBlank', '$filter', 'personalizationsmarteditUtils', 'personalizationsmarteditManagementService', 'personalizationsmarteditMessageHandler', 'confirmationModalService', 'CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY', function($scope, isBlank, $filter, personalizationsmarteditUtils, personalizationsmarteditManagementService, personalizationsmarteditMessageHandler, confirmationModalService, CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY) {

        var getSegmentGroupByForCriteria = function(allSegmentsSelected) {
            return allSegmentsSelected ? CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND : CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_OR;
        };

        $scope.addVariationClick = function() {
            $scope.customization.variations.push({
                name: $scope.edit.name,
                enabled: true,
                triggers: [{
                    type: 'segmentTriggerData',
                    groupBy: getSegmentGroupByForCriteria($scope.edit.allSegmentsChecked),
                    segments: $scope.edit.selectedSegments
                }],
                allSegmentsCriteria: $scope.edit.allSegmentsChecked,
                rank: $scope.customization.variations.length,
                isNew: true
            });

            $scope.edit.name = '';
            $scope.edit.selectedSegments = [];

            resetPagination();
            resetSegmentFiltering();
        };

        $scope.submitChangesClick = function() {
            $scope.edit.selectedVariation.triggers = $scope.edit.selectedVariation.triggers || [];
            var segmentTriggerArr = $scope.edit.selectedVariation.triggers.filter(function(trigger) {
                return trigger.type === "segmentTriggerData";
            });

            if (segmentTriggerArr.length === 0 && $scope.edit.selectedSegments.length > 0) {
                $scope.edit.selectedVariation.triggers.push({
                    type: 'segmentTriggerData',
                    groupBy: '',
                    segments: []
                });
            }

            $scope.edit.selectedVariation.name = $scope.edit.name;
            var triggerSegment = personalizationsmarteditUtils.getSegmentTriggerForVariation($scope.edit.selectedVariation);
            triggerSegment.segments = $scope.edit.selectedSegments;
            triggerSegment.groupBy = getSegmentGroupByForCriteria($scope.edit.allSegmentsChecked);
            $scope.edit.selectedVariation.allSegmentsCriteria = $scope.edit.allSegmentsChecked;
            $scope.edit.selectedVariation = undefined;
        };

        $scope.cancelChangesClick = function() {
            $scope.edit.selectedVariation = undefined;
        };

        $scope.removeVariationClick = function(variation) {
            confirmationModalService.confirm({
                description: 'personalization.manager.modal.deletevariation.content'
            }).then(function() {
                $scope.customization.variations.splice($scope.customization.variations.indexOf(variation), 1);
                $scope.edit.selectedVariation = undefined;
            });
        };

        $scope.setVariationRank = function(variation, increaseValue, $event, firstOrLast) {
            if (firstOrLast) {
                $event.stopPropagation();
            } else {
                var from = $scope.customization.variations.indexOf(variation);
                var to = from + increaseValue;
                var variationsArr = $scope.customization.variations;
                if (to >= 0 && to < variationsArr.length) {
                    variationsArr.splice(to, 0, variationsArr.splice(from, 1)[0]);
                    $scope.recalculateRanksForVariations();
                }
            }
        };

        $scope.recalculateRanksForVariations = function() {
            $scope.customization.variations.forEach(function(part, index) {
                $scope.customization.variations[index].rank = index + 1;
            });
        };

        $scope.canShowVariationSegmentationCriteria = function() {
            return $scope.edit.selectedSegments && $scope.edit.selectedSegments.length > 1;
        };

        $scope.editVariationAction = function(variation) {
            $scope.edit.selectedVariation = variation;
        };

        $scope.canSaveVariation = function() {
            return $scope.edit.selectedSegments && $scope.edit.selectedSegments.length > 0 && !isBlank($scope.edit.name);
        };


        $scope.isVariationSelected = function() {
            return angular.isDefined($scope.edit.selectedVariation);
        };

        $scope.getSegmentCodesStrForVariation = function(variation) {
            var segments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments || [];
            var segmentCodes = segments.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            });
            return segmentCodes.join(", ");
        };

        $scope.getSegmentLenghtForVariation = function(variation) {
            var segments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments || [];
            return segments.length;
        };

        $scope.getCriteriaDescrForVariation = function(variation) {
            var segmentTrigger = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation);
            if (segmentTrigger.groupBy === CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND) {
                return $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments');
            } else {
                return $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments');
            }
        };

        var getSegmentsFilterObject = function() {
            return {
                code: $scope.segmentFilter.code,
                pageSize: $scope.segmentPagination.count,
                currentPage: $scope.segmentPagination.page + 1
            };
        };

        var resetPagination = function() {
            $scope.segmentPagination.count = 10;
            $scope.segmentPagination.page = -1;
            $scope.segmentPagination.totalPages = 1;
        };

        var resetSegmentFiltering = function() {
            $scope.segmentFilter = {
                code: ''
            };
            $scope.edit.segments.length = 0;
        };

        $scope.segmentPagination = {};

        resetPagination();
        resetSegmentFiltering();

        $scope.moreSegmentRequestProcessing = false;
        $scope.addMoreSegmentItems = function() {
            if ($scope.segmentPagination.page < $scope.segmentPagination.totalPages - 1 && !$scope.moreSegmentRequestProcessing) {
                $scope.moreSegmentRequestProcessing = true;
                personalizationsmarteditManagementService.getSegments(getSegmentsFilterObject()).then(function successCallback(response) {
                    Array.prototype.push.apply($scope.edit.segments, response.segments);
                    $scope.segmentPagination = response.pagination;
                    $scope.moreSegmentRequestProcessing = false;
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    $scope.moreSegmentRequestProcessing = false;
                });
            }
        };

        $scope.segmentSearchInputKeypress = function(keyEvent, searchObj) {
            resetPagination();
            $scope.segmentFilter.code = searchObj;
            $scope.edit.segments.length = 0;
            $scope.addMoreSegmentItems();
        };

        $scope.segmentSelectedEvent = function(item, uiSelectController) {
            var valueArr = $scope.edit.selectedSegments.map(function(elem) {
                return elem.code;
            });
            var isDuplicate = valueArr.some(function(elem, idx) {
                return valueArr.indexOf(elem) != idx;
            });
            if (isDuplicate) {
                var duplicated = $scope.edit.selectedSegments.filter(function(elem) {
                    return elem.code == item.code;
                });
                $scope.edit.selectedSegments.splice($scope.edit.selectedSegments.indexOf(duplicated[1]), 1);
            }

            uiSelectController.search = '';
            $scope.segmentSearchInputKeypress(null, '');
        };

        $scope.initUiSelect = function(uiSelectController) {
            if (uiSelectController.open) {
                $scope.segmentSearchInputKeypress(null, uiSelectController.search);
            }
        };

        $scope.$watch('edit.selectedSegments', function() {
            if (($scope.edit.selectedSegments || []).length === 0)
                $scope.segmentSearchInputKeypress(null, '');
        }, true);

    }]) //To remove when angular-ui-select would be upgraded to version > 0.19
    .directive('uisOpenClose', ['$parse', '$timeout', function($parse, $timeout) {
        return {
            restrict: 'A',
            require: 'uiSelect',
            link: function(scope, element, attrs, $select) {
                $select.onOpenCloseCallback = $parse(attrs.uisOpenClose);

                scope.$watch('$select.open', function(isOpen, previousState) {
                    if (isOpen !== previousState) {
                        $timeout(function() {
                            $select.onOpenCloseCallback(scope, {
                                isOpen: isOpen
                            });
                        });
                    }
                });
            }
        };
    }]);
