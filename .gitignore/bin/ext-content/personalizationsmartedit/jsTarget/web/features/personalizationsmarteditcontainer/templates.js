angular.module('personalizationsmarteditcontainerTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/personalizationsmarteditcontainer/contextMenu/personalizationsmarteditAddEditActionTemplate.html',
    "<div class=\"customization-component\">\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.customization.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{selectedCustomization.name}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.mastercomponent.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{componentType}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.variation.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{selectedVariation.name}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.actions.title\"></dt>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <ui-select ng-model=\"action.selected\" theme=\"select2\" title=\"\" search-enabled=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in actions\" position=\"down\">\n" +
    "            <span ng-bind=\"item.name | translate\"></span>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "\n" +
    "    <ui-select ng-show=\"action.selected.id == 'use'\" ng-model=\"component.selected\" ng-keyup=\"componentSearchInputKeypress($event, $select.search)\" theme=\"select2\" title=\"\" reset-search-input=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.componentlist.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in components\" position=\"down\" personalization-infinite-scroll=\"addMoreComponentItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "            <div class=\"row\">\n" +
    "                <span class=\"col-md-7\" ng-bind=\"item.name | translate\"></span>\n" +
    "                <span class=\"col-md-5\" ng-bind=\"item.typeCode | translate\"></span>\n" +
    "            </div>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "\n" +
    "    <ui-select ng-show=\"action.selected.id == 'create'\" on-select=\"newComponentTypeSelectedEvent($item, $model)\" ng-model=\"newComponent.selected\" theme=\"select2\" title=\"\" search-enabled=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.componenttype.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in newComponentTypes\" position=\"down\">\n" +
    "            <span ng-bind=\"item.name | translate\"></span>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/managerView/personalizationsmarteditManagerViewTemplate.html',
    "<div id=\"editConfigurationsBody\" class=\"manage-customization\">\n" +
    "\n" +
    "    <h2 class=\"text-capitalize h2-color--grey-font\" ng-bind=\"catalogName\"></h2>\n" +
    "\n" +
    "    <div class=\"input-group customization-search\">\n" +
    "        <span class=\"input-group-addon glyphicon glyphicon-search ySESearchIcon\"></span>\n" +
    "        <input type=\"text\" class=\"form-control ng-pristine ng-untouched ng-valid\" placeholder=\"{{ 'personalization.manager.modal.search.placeholder' | translate}}\" ng-model=\"search.name\" ng-keyup=\"searchInputKeypress($event)\"></input>\n" +
    "    </div>\n" +
    "    <div ng-show=\"isFilterEnabled()\">\n" +
    "        <div class=\"search-results\">\n" +
    "            <span ng-bind=\"filteredCustomizationsCount\"></span>\n" +
    "            <span data-translate=\"personalization.manager.modal.search.result.label\"></span>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"categoryTable\">\n" +
    "        <!-- headers -->\n" +
    "        <div class=\"row row-reset-margin tablehead hidden-xs\">\n" +
    "            <div class=\"col-xs-8 text-left\" data-translate=\"personalization.manager.modal.grid.header.customization\"></div>\n" +
    "            <div class=\"col-xs-2 text-left\" data-translate=\"personalization.manager.modal.grid.header.variations\"></div>\n" +
    "            <div class=\"col-xs-2 text-left\" data-translate=\"personalization.manager.modal.grid.header.components\"></div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"row row-reset-margin\">\n" +
    "            <div class=\"col-xs-12 col-reset-padding\">\n" +
    "                <button class=\"y-add y-add-btn button-margin\" type=\"button\" data-ng-click=\"openNewModal();\">\n" +
    "                    <span class=\"hyicon hyicon-add\" data-translate=\"personalization.manager.modal.add.button\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div data-ng-repeat=\"customization in customizations\" ng-init=\"isCollapsed=true\">\n" +
    "            <div class=\"row row-reset-margin customizationLayout divider\">\n" +
    "                <div class=\"col-xs-half text-right\" ng-click=\"isCollapsed=! isCollapsed; customizationClickAction(customization)\">\n" +
    "                    <a class=\"btn btn-link category-toggle\">\n" +
    "                        <span class=\"glyphicon glyphicon-chevron-down \"></span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-8 pull-left text-left\" ng-click=\"isCollapsed=! isCollapsed; customizationClickAction(customization)\">\n" +
    "                    <p class=\"primaryData customization-rank-{{customization.rank}}-row \">\n" +
    "                        <span class=\"categoryName personalizationsmartedit-customization-code \">{{customization.name}}</span>\n" +
    "                    </p>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-2 pull-left text-left\">\n" +
    "                    <p>{{customization.variations.length || 0}}</p>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-half col-offset-1 pull-right\">\n" +
    "                    <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "                        <span class=\"hyicon hyicon-more\"></span>\n" +
    "                    </button>\n" +
    "                    <ul class=\"dropdown-menu pull-right text-left\" role=\"menu\">\n" +
    "                        <li>\n" +
    "                            <a class=\"customization-edit-button\" ng-click=\"editCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                        </li>\n" +
    "                        <li>\n" +
    "                            <a class=\"customization-delete-button\" ng-click=\"deleteCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.delete\"></a>\n" +
    "                        </li>\n" +
    "                        <li ng-class=\"isFilterEnabled() || $first ? 'disabled' : '' \">\n" +
    "                            <a class=\"customization-move-up-button\" ng-click=\"setCustomizationRank(customization, -1, $event, $first)\" data-translate=\"personalization.manager.modal.button.moveup\"></a>\n" +
    "                        </li>\n" +
    "                        <li ng-class=\"isFilterEnabled() || $last ? 'disabled' : '' \">\n" +
    "                            <a class=\"customization-move-down-button\" ng-click=\"setCustomizationRank(customization, 1, $event, $last)\" data-translate=\"personalization.manager.modal.button.movedown\"></a>\n" +
    "                        </li>\n" +
    "                    </ul>\n" +
    "                </div>\n" +
    "                <!--end col-sm-5 for contextual menu-->\n" +
    "            </div>\n" +
    "            <!--end customizationLayout-->\n" +
    "\n" +
    "            <div collapse=\"isCollapsed\">\n" +
    "                <div ng-repeat=\"variation in customization.variations\">\n" +
    "                    <div class=\"variationLayout divider\">\n" +
    "                        <div class=\"row row-reset-margin variation-rank-{{variation.rank}}-row\">\n" +
    "                            <div class=\"col-xs-half\"></div>\n" +
    "                            <div class=\"col-xs-10 text-left\">\n" +
    "                                <span>{{variation.name}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-1 text-left \">\n" +
    "                                <span>{{variation.numberOfComponents}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-half pull-right\">\n" +
    "                                <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "                                    <span class=\"hyicon hyicon-more\"></span>\n" +
    "                                </button>\n" +
    "                                <ul class=\"dropdown-menu text-left pull-right\" role=\"menu\">\n" +
    "                                    <li>\n" +
    "                                        <a class=\"customization-edit-button\" ng-click=\"editVariationAction(customization, variation)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"customization.variations.length < 2 ? 'disabled' : '' \">\n" +
    "                                        <a class=\"customization-delete-button\" ng-click=\"deleteVariationAction(customization, variation, $event)\" data-translate=\"personalization.manager.modal.button.delete\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"$first ? 'disabled' : '' \">\n" +
    "                                        <a class=\"variation-move-down-button\" ng-click=\"setVariationRank(customization, variation, -1, $event, $first)\" data-translate=\"personalization.manager.modal.button.moveup\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"$last ? 'disabled' : '' \">\n" +
    "                                        <a class=\"variation-move-down-button\" ng-click=\"setVariationRank(customization, variation, 1, $event, $last)\" data-translate=\"personalization.manager.modal.button.movedown\"></a>\n" +
    "                                    </li>\n" +
    "                                </ul>\n" +
    "                            </div>\n" +
    "                            <!--end col-sm-2 for contextual menu-->\n" +
    "                        </div>\n" +
    "                        <!--end variation-rank-->\n" +
    "                    </div>\n" +
    "                    <!--end variationLayout-->\n" +
    "                </div>\n" +
    "                <!--end variation-repeat-->\n" +
    "            </div>\n" +
    "            <!--end collapse-->\n" +
    "        </div>\n" +
    "        <!--end customization-repeat-->\n" +
    "    </div>\n" +
    "    <!--end categoryTable-->\n" +
    "    <personalizationsmartedit-pagination pages=\"pagination.pages \" current-page=\"pagination.currentPage \" page-sizes=\"pagination.pageSizes \" current-size=\"pagination.currentSize \" pages-offset=\"pagination.pagesOffset \" callback=\"paginationCallback \" />\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagMenuTemplate.html',
    "<div ng-controller=\"topToolbarMenuController\" dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button personalizationsmarteditTopToolbarButton\" dropdown-toggle ng-disabled=\"disabled\" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"../personalizationsmartedit/icons/icon_library.png\" />\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\" data-translate=\"personalization.manager.toolbar\"></span>\n" +
    "    </button>\n" +
    "    <ul ng-if=\"status.isopen\" class=\"dropdown-menu btn-block ySEComponentMenu ySEComponentMenu-customized\" role=\"menu\" ng-click=\"preventDefault($event);\">\n" +
    "        <div class=\"ySEComponentMenu-headers\">\n" +
    "            <h2 class=\"text-uppercase h2\" data-translate=\"personalization.right.toolbar.library.name\"></h2>\n" +
    "            <small class=\"small\" data-translate=\"personalization.topmenu.library.info\"></small>\n" +
    "        </div>\n" +
    "        <li class=\"divider divider-lefttoolbar\">\n" +
    "        </li>\n" +
    "        <li class=\"item ySEComponentMenuList--item\">\n" +
    "            <a class=\"ySEComponentMenu-anchor\" id=\"personalizationsmartedit-right-toolbar-customization-anchor\" data-translate=\"personalization.topmenu.library.manager.name\" data-ng-click=\"managerViewClick()\"></a>\n" +
    "        </li>\n" +
    "        <li class=\"divider divider-lefttoolbar\"></li>\n" +
    "        <li class=\"item ySEComponentMenuList--item\">\n" +
    "            <a class=\"ySEComponentMenu-anchor\" id=\"personalizationsmartedit-right-toolbar-customization-anchor\" data-translate=\"personalization.manager.modal.add.button\" data-ng-click=\"createCustomizationClick()\"></a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
    "<div>\n" +
    "    <tabset>\n" +
    "        <tab ng-repeat=\"tab in tabsArr\" select=\"selectTab(tab)\" active=\"tab.active\" disable=\"tab.disabled\" heading=\"{{tab.heading}}\">\n" +
    "            <form name=\"{{tab.formName}}\" novalidate>\n" +
    "                <div>\n" +
    "                    <ng-include src=\"tab.template\"></ng-include>\n" +
    "                </div>\n" +
    "            </form>\n" +
    "        </tab>\n" +
    "    </tabset>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagBasicInfoTemplate.html',
    "<div class=\"customization-form--size-large\">\n" +
    "    <div class=\"form-group\">\n" +
    "        <label data-translate=\"personalization.modal.customizationvariationmanagement.basicinformationtab.name\" class=\"customization-form-control-label--size\"></label>\n" +
    "        <input type=\"text\" class=\"form-control customization-form-control-input--size\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.basicinformationtab.name.placeholder' | translate}}\" name=\"{{customization.name}}_key\" data-ng-model=\"customization.name\" data-ng-required=\"true\">\n" +
    "    </div>\n" +
    "    <div class=\"form-group\">\n" +
    "        <label data-translate=\"personalization.modal.customizationvariationmanagement.basicinformationtab.details\" class=\"customization-form-control-label--size\"></label>\n" +
    "        <textarea rows=\"8\" class=\"form-control customization-form-control-input--size\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.basicinformationtab.details.placeholder' | translate}}\" name=\"{{customization.description}}_key\" data-ng-model=\"customization.description\"></textarea>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagTargetGrpTemplate.html',
    "<div ng-controller=\"personalizationsmarteditManagerTargetGrpController\" class=\"row customization-form--size-large\">\n" +
    "    <div class=\"customization-form-target-group-tab-sub-title--size--font\">\n" +
    "        <span ng-bind=\"customization.name\"></span>\n" +
    "        <span ng-if='customization.description'>:</span>\n" +
    "        <span class=\"customization-form-target-group-tab-sub-title-description--font\"></span>\n" +
    "        <span ng-bind=\"customization.description\"></span>\n" +
    "    </div>\n" +
    "    <div class=\"col-md-6\">\n" +
    "        <div ng-if='customization.variations.length === 0'>\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab\"></label>\n" +
    "            <h6 data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.notargetgroups\"></h6>\n" +
    "        </div>\n" +
    "\n" +
    "        <div ng-if='customization.variations.length > 0'>\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab\"></label>\n" +
    "\n" +
    "            <ul class=\"list-group\">\n" +
    "                <li class=\"list-group-item list-group-item-size list-group-item--hover\" data-ng-repeat=\"variation in customization.variations \">\n" +
    "\n" +
    "                    <div class=\"row target-group-list\">\n" +
    "                        <div class=\"col-md-11\">\n" +
    "                            <div class=\"customization-variation-code-font-text-style\" ng-bind=\"variation.name\"></div>\n" +
    "                            <div ng-bind=\"getSegmentCodesStrForVariation(variation)\"></div>\n" +
    "                            <div ng-if=\"getSegmentLenghtForVariation(variation) > 1\">\n" +
    "                                </br>\n" +
    "                                <span class=\"customization-criteria-font-size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.criteria.colon\"></span>\n" +
    "                                <span ng-bind=\"getCriteriaDescrForVariation(variation)\"></span>\n" +
    "                                </br>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "\n" +
    "                        <div class=\"col-md-1\">\n" +
    "                            <button type=\"button\" class=\"dropdown pull-right btn btn-link dropdown-toggle\" id=\"dropdownMenu1\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\">\n" +
    "                                <span class=\"hyicon hyicon-more hyicon-in-modal-size\"></span>\n" +
    "                            </button>\n" +
    "                            <ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu1\" role=\"menu\">\n" +
    "                                <li>\n" +
    "                                    <a ng-click=\"editVariationAction(variation)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.edit\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li>\n" +
    "                                    <a ng-click=\"removeVariationClick(variation)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.remove\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li ng-class=\"$first ? 'disabled' : '' \">\n" +
    "                                    <a ng-click=\"setVariationRank(variation, -1, $event, $first)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.moveup\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li ng-class=\"$last ? 'disabled' : '' \">\n" +
    "                                    <a ng-click=\"setVariationRank(variation, 1, $event, $last)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.movedown\" />\n" +
    "                                </li>\n" +
    "                            </ul>\n" +
    "\n" +
    "                        </div>\n" +
    "\n" +
    "                    </div>\n" +
    "\n" +
    "                </li>\n" +
    "            </ul>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"col-md-6\">\n" +
    "\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname\"></label>\n" +
    "            <input uniquetargetgroupname type=\"text\" class=\"form-control\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname.placeholder' | translate}}\" name=\"variationname_key\" data-ng-model=\"edit.name\">\n" +
    "            <span ng-show=\"{{tab.formName}}.variationname_key.$error.uniquetargetgroupname\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroup.uniquename.validation.message\"></span>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.segments\"></label>\n" +
    "            <ui-select uis-open-close=\"initUiSelect($select)\" multiple='true' ng-model=\"edit.selectedSegments\" theme=\"select2personalization\" ng-disabled=\"disabled\" class=\"form-control\" ng-keyup=\"segmentSearchInputKeypress($event, $select.search)\" on-select=\"segmentSelectedEvent($item, $select)\" on-remove=\"segmentSelectedEvent($item, $select)\">\n" +
    "                <ui-select-match placeholder=\"{{ 'personalization.modal.customizationvariationmanagement.targetgrouptab.segments.placeholder' | translate}}\">\n" +
    "                    <span class=\"customization-ui-select-match-item--colors\">{{$item.code}} &lt;{{$item.code}}&gt;</span>\n" +
    "                    <span class=\"close ui-select-match-close\" ng-hide=\"$select.disabled\" ng-click=\"$selectMultiple.removeChoice($index)\">&nbsp;×</span>\n" +
    "                </ui-select-match>\n" +
    "                <ui-select-choices repeat=\"item in edit.segments\" position=\"down\" personalization-infinite-scroll=\"addMoreSegmentItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "                    <div ng-html-bind=\"item.code\"></div>\n" +
    "                    <small>\n" +
    "                        Segment: {{item.code}}\n" +
    "                    </small>\n" +
    "                </ui-select-choices>\n" +
    "            </ui-select>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"form-group\" ng-if=\"canShowVariationSegmentationCriteria()\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.criteria\"></label>\n" +
    "            <div class=\"row\">\n" +
    "                <div class=\"col-md-6\">\n" +
    "                    <input type=\"checkbox\" data-ng-model=\"edit.allSegmentsChecked\">\n" +
    "                    <label data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments\" ng-click=\"edit.allSegmentsChecked = !edit.allSegmentsChecked\"></label>\n" +
    "                </div>\n" +
    "                <div class=\"col-md-6\">\n" +
    "                    <input type=\"checkbox\" negate data-ng-model=\"edit.allSegmentsChecked\">\n" +
    "                    <label data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments\" ng-click=\"edit.allSegmentsChecked = !edit.allSegmentsChecked\"></label>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div ng-if=\"!isVariationSelected()\">\n" +
    "            <button type=\"button\" class=\"btn btn-default add-target-group\" data-ng-click=\"addVariationClick()\" data-ng-disabled=\"!canSaveVariation()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.addvariation\"></button>\n" +
    "        </div>\n" +
    "        <div ng-if=\"isVariationSelected()\">\n" +
    "            <button type=\"button\" class=\"btn btn-default submit-target-group-edit\" data-ng-click=\"submitChangesClick()\" data-ng-disabled=\"!canSaveVariation()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.savechanges\"></button>\n" +
    "            <button type=\"button\" class=\"btn btn-subordinate cancel-target-group-edit\" data-ng-click=\"cancelChangesClick()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.cancelchanges\"></button>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemTemplate.html',
    "<div id=\"personalizationsmartedit-right-toolbar-item-template\" class=\"manage-customization\">\n" +
    "\n" +
    "    <div ng-controller=\"personalizationsmarteditRightToolbarController\" data-customizations-loaded=\"{{customizationsOnPage.length > 0}}\">\n" +
    "\n" +
    "        <div class=\"reset-padding\" ng-show=\"!search.searchCustomizationEnabled\">\n" +
    "            <button class=\"y-add y-add-btn y-add-btn-lefttoolbar\" type=\"button\" data-ng-click=\"toggleAddMoreCustomizationsClick()\">\n" +
    "                <span class=\"hyicon hyicon-add\" data-translate=\"personalization.right.toolbar.addmorecustomizations.button\" />\n" +
    "            </button>\n" +
    "        </div>\n" +
    "        <div class=\"text-left search-item-lefttoolbar\" ng-show=\"search.searchCustomizationEnabled\">\n" +
    "            <ui-select multiple class=\"form-control customizations-from-library-multi-select\" ng-model=\"search.selectedLibraryCustomizations\" theme=\"select2\" ng-disabled=\"disabled\" on-select=\"addCustomizationFromLibrary()\" ng-keyup=\"customizationSearchInputKeypress($event, $select.search)\">\n" +
    "                <ui-select-match placeholder=\"{{'personalization.right.toolbar.addmorecustomizations.customization.library.search.placeholder' | translate}}\">\n" +
    "                    {{$item.name}} &lt;{{$item.name}}&gt;\n" +
    "                </ui-select-match>\n" +
    "                <ui-select-choices repeat=\"item in search.libraryCustomizations\" personalization-infinite-scroll=\"addMoreCustomizationItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "                    <div ng-html-bind=\"item.name | highlight: $select.search\"></div>\n" +
    "                    <small>{{item.name}}</small>\n" +
    "                </ui-select-choices>\n" +
    "            </ui-select>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"overflow-wrapper-lefttoolbar\">\n" +
    "            <div class=\"categoryTable categoryTable-lefttoolbar\">\n" +
    "                <div data-ng-repeat=\"customization in customizationsOnPage | orderBy: 'rank'\" ng-init=\"initCustomization(customization)\">\n" +
    "                    <div class=\"row row-lefttoolbar\">\n" +
    "                        <div ng-class=\"{'custFromLibExtraStyling customization-from-library':customization.fromLibrary, 'customization-rank-{{customization.rank}}':true}\">\n" +
    "                            <div class=\"col-xs-1 text-right\" ng-click=\"customization.collapsed = !customization.collapsed; customizationClick(customization)\">\n" +
    "                                <a class=\"btn btn-link category-toggle\">\n" +
    "                                    <span class=\"glyphicon glyphicon-chevron-down glyphicon-down-lefttoolbar\"></span>\n" +
    "                                </a>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-9 text-left\" ng-click=\"customization.collapsed = !customization.collapsed; customizationClick(customization)\">\n" +
    "                                <span class=\"primaryData\">{{customization.name}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-1\" ng-init=\"subMenu=false\">\n" +
    "\n" +
    "                                <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right customization-rank-{{customization.rank}}-dropdown-toggle\" data-toggle=\"dropdown\" ng-click=\"subMenu = !subMenu\">{{'personalization.right.toolbar.customization.cust.onpage.action' | translate}}\n" +
    "                                    <span class=\"hyicon hyicon-more\"></span>\n" +
    "                                </button>\n" +
    "                                <ul ng-if=\"subMenu\" class=\"dropdown-menu pull-right text-left dropdown-menu-leftoolbar\" role=\"menu\">\n" +
    "                                    <li>\n" +
    "                                        <a class=\"dropdown-menu-single-item cutomization-rank-{{customization.rank}}-edit-button\" ng-click=\"editCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                                    </li>\n" +
    "                                </ul>\n" +
    "\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                    <!--end row desktopLayout-->\n" +
    "\n" +
    "                    <div collapse=\"customization.collapsed\">\n" +
    "                        <div ng-repeat=\"variation in customization.variations\">\n" +
    "                            <div class=\"row row-lefttoolbar\" ng-class=\"getSelectedVariationClass(variation)\">\n" +
    "                                <div class=\"col-xs-9 col-xs-offset-1 text-left\" ng-click=\"variationClick(customization, variation)\">\n" +
    "                                    <span>{{variation.name}}</span>\n" +
    "                                </div>\n" +
    "                                <div class=\"col-xs-1 text-left selectedVariation-icon\"></div>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "                <!--end data repeat-->\n" +
    "            </div>\n" +
    "            <!--end of categoryTable-->\n" +
    "\n" +
    "            <personalizationsmartedit-pagination pages=\"pagination.pages\" current-page=\"pagination.currentPage\" page-sizes=\"pagination.pageSizes\" current-size=\"pagination.currentSize\" pages-offset=\"pagination.pagesOffset\" fixed-page-size=\"pagination.fixedPageSize\" callback=\"paginationCallback\" template=\"personalizationsmarteditPaginationLefttoolbarTemplate.html\" />\n" +
    "        </div>\n" +
    "        <!--end of overflow-wrapper-lefttoolbar -->\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemWrapperTemplate.html',
    "<div ng-controller=\"topToolbarMenuController\" dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button personalizationsmarteditTopToolbarButton\" dropdown-toggle ng-disabled=\"disabled\" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"../personalizationsmartedit/icons/icon_customize.png\" />\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\" data-translate=\"personalization.right.toolbar\"></span>\n" +
    "    </button>\n" +
    "    <ul ng-if=\"status.isopen\" class=\"dropdown-menu dropdown-menu-left btn-block ySEComponentMenu ySEComponentMenu-customized\" role=\"menu\" ng-click=\"preventDefault($event);\">\n" +
    "        <div class=\"ySEComponentMenu-headers\">\n" +
    "            <h2 class=\"text-uppercase h2\" data-translate=\"personalization.right.toolbar.customization.heading\"></h2>\n" +
    "            <small class=\"small\" data-translate=\"personalization.right.toolbar.customization.description\"></small>\n" +
    "        </div>\n" +
    "        <li role=\"menuitem\" class=\"item menuitem-lefttoolbar\">\n" +
    "            <personalizationsmartedit-right-toolbar-item selected-item-callbacks=\"selectedItemCallbacks\" close-page-tool-menu=\"closePageToolMenu()\"></personalizationsmartedit-right-toolbar-item>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );

}]);
