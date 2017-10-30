angular.module('cmssmarteditContainerTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentItemTemplate.html',
    "<div class=\"ySEComponentItem col-xs-6\">\n" +
    "\n" +
    "\t<div class=\"smartEditComponent\" data-smartedit-component-id=\"{{componentItem.uid}}\" data-smartedit-component-type=\"{{componentItem.typeCode}}\">\n" +
    "\t\t<img data-ng-src=\"{{imageRoot}}/images/component_default.png\" class=\" \" alt=\"{{componentItem.name}}\" />\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECustomCompTitle\" title=\"{{componentItem.name}} - {{componentItem.typeCode}}\">\n" +
    "\t\t<div class=\"ySECustCompName\" >{{componentItem.name}}</div>\n" +
    "\t\t<div class=\"ySECustCompType\" >{{componentItem.typeCode}}</div>\n" +
    "\t\t<img data-ng-if=\"!componentItem.visible\" class=\"ySEComponentVisibility\" ng-src=\"{{imageRoot}}/images/icon_visibility.png\" >\n" +
    "\t</div>\n" +
    "\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentItemWrapperTemplate.html',
    "<component-item data-component-item=\"item\"></component-item>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentMenuTemplate.html',
    "<component-menu></component-menu>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentMenuWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}{{menuIcon}}\"/>\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\">{{'componentmenu.btn.label.addcomponent' | translate}}</span>\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu dropdown-menu-left btn-block ySEComponentMenu\" role=\"menu\" ng-click=\"preventDefault($event);\" >\n" +
    "        <li role=\"menuitem \" class=\"item \">\n" +
    "            <component-search></component-search>\n" +
    "        </li>\n" +
    "        <li class=\"divider \"></li>\n" +
    "        <li role=\"menuitem \" class=\"item \">\n" +
    "            <component-tabs></component-tabs>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentSearchTemplate.html',
    "<div class=\"input-group ySEComponentSearch\">\n" +
    "    <span class=\"input-group-addon glyphicon glyphicon-search ySESearchIcon\" ng-click=\"preventDefault($event); \"></span>\n" +
    "    <input type=\"text\" class=\"form-control ySECompSearchField\" name=\"srch-term\" ng-model=\"searchTerm\" ng-keypress=\"focusOnSearch($event);\" ng-click=\"selectSearch($event); \" placeholder=\"{{'componentmenu.search.placeholder' | translate}}\">\n" +
    "    <span data-ng-show=\"showResetButton\" class=\"input-group-addon glyphicon glyphicon-remove-sign ySESearchIcon\" ng-click=\"resetSearch($event); searchTerm='';\"></span>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentTabsTemplate.html',
    "<div class=\"ySEComponentsResult\" ng-controller=\"ComponentMenuController as menuCtrl\">\n" +
    "    <tabset>\n" +
    "        <tab class=\"ySEComTypeTab\" heading=\"{{tabs[0].title | translate}}\" active=\"tabs[0].active\" disable=\"{{tabs[0].disabled}}\" ng-click=\"activateItemsTab(0); selectTab($event); \">\n" +
    "            <div class=\"ySEComponents yFixed\">\n" +
    "                <component-type ng-repeat=\"componentType in menuCtrl.types.componentTypes | nameFilter:searchTerm track by $id(componentType) \"></component-type>\n" +
    "            </div>\n" +
    "        </tab>\n" +
    "        <tab class=\"ySEComTypeTab\" heading=\"{{tabs[1].title | translate}}\" active=\"tabs[1].active\" disable=\"{{tabs[1].disabled}}\" ng-click=\"activateItemsTab(1); selectTab($event); \">\n" +
    "            <y-infinite-scrolling data-ng-if=\"tabs[1].active\"\n" +
    "                data-drop-down-class=\"ySEComponents\"\n" +
    "                data-page-size=\"10\"\n" +
    "                data-mask=\"searchTerm\"\n" +
    "                data-fetch-page=\"menuCtrl.loadComponentItems\"\n" +
    "                data-context=\"menuCtrl\">\n" +
    "                <div data-ng-repeat=\"item in menuCtrl.items\">\n" +
    "                    <component-item data-component-item=\"item\"></component-item>\n" +
    "                </div>\n" +
    "            </y-infinite-scrolling>\n" +
    "        </tab>\n" +
    "    </tabset>\n" +
    "\n" +
    "    <span class=\"label ySEBottomLabel\">{{'componentmenu.label.draganddrop' | translate}}</span>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentTypeTemplate.html',
    "<div class=\"ySEComponentItem col-xs-6\">\n" +
    "\t<div class=\"smartEditComponent\" data-smartedit-component-type=\"{{componentType.code}}\">\n" +
    "\t\t<img data-ng-src=\"{{imageRoot}}/images/component_default.png\" class=\" \" alt=\"{{componentItem.name}}\" />\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECustomCompTitle ySECompTypeTitle\">\n" +
    "\t\t<div class=\"ySECustCompType\" title=\"{{componentType.name}}\">{{componentType.name}}</div>\n" +
    "\t</div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/editorTabsetTemplate.html',
    "<y-tabset \n" +
    "\t\tdata-model=\"model\" \n" +
    "\t\ttabs-list=\"tabsList\" \n" +
    "\t\tdata-num-tabs-displayed=\"{{numTabsDisplayed}}\" \n" +
    "\t\ttab-control=\"tabControl\">\n" +
    "</y-tabset>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/admin/adminTabTemplate.html',
    "<admin-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</admin-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/basic/basicTabTemplate.html',
    "<basic-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</basic-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/generic/genericTabTemplate.html',
    "<generic-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tdata-on-structure-resolved=\"model.onStructureResolved\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</generic-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/generic/tabInnerTemplate.html',
    "<generic-editor \n" +
    "\t\tdata-id=\"tabId\" \n" +
    "\t\tdata-smartedit-component-id=\"componentId\" \n" +
    "\t\tdata-smartedit-component-type=\"componentType\" \n" +
    "\t\tdata-structure-api=\"structureApi\" \n" +
    "\t\tdata-content-api=\"contentApi\" \n" +
    "\t\tdata-submit=\"saveTab\" \n" +
    "\t\tdata-reset=\"resetTab\" \n" +
    "\t\tdata-is-dirty=\"isDirtyTab\" \n" +
    "\t\tdata-on-structure-resolved=\"onStructureResolved\">\n" +
    "</generic-editor>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
    "<generic-editor \n" +
    "\t\tdata-id=\"tabId\" \n" +
    "\t\tdata-smartedit-component-id=\"componentId\" \n" +
    "\t\tdata-smartedit-component-type=\"componentType\" \n" +
    "\t\tdata-structure=\"tabStructure\" \n" +
    "\t\tdata-content-api=\"contentApi\" \n" +
    "\t\tdata-submit=\"saveTab\" \n" +
    "\t\tdata-reset=\"resetTab\" \n" +
    "\t\tdata-is-dirty=\"isDirtyTab\">\n" +
    "</generic-editor>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/visibility/visibilityTabTemplate.html',
    "<visibility-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</visibility-tab>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListLinkDirectiveTemplate.html',
    "<div class=\"page-list-link-container\">\n" +
    "    <div data-ng-repeat=\"catalogVersion in catalog.catalogVersions\" class=\"col-xs-6 page-list-link-item\">\n" +
    "        <a data-ng-href=\"#/pages/{{catalogVersion.siteDescriptor.uid}}/{{catalogVersion.catalogId}}/{{catalogVersion.catalogVersion}}\" data-translate=\"cataloginfo.pagelist\"></a>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-12\">\n" +
    "        <hr class=\"ySECatalogDivider\" />\n" +
    "    </div>\n" +
    "</div>\n" +
    "\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListLinkTemplate.html',
    "<page-list-link></page-list-link>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListTemplate.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div ng-class=\"{'alert-overlay': true, 'ySEEmptyMessage': (!alerts || alerts.length == 0 ) }\">\n" +
    "    <alerts-box alerts=\"alerts\" />\n" +
    "</div>\n" +
    "<div class=\"pageListWrapper\">\n" +
    "    <div class=\"ySEPageListTitle\">\n" +
    "        <h1 class=\"ySEPage-list-title\" data-translate='pagelist.title'></h1>\n" +
    "        <h4 class=\"ySEPage-list-label\">{{pageListCtl.catalogName | l10n}} - {{pageListCtl.catalogVersion}}</h4>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"ySEPage-list-search input-group\">\n" +
    "        <span class=\"input-group-addon glyphicon glyphicon-search ySEPage-list-search-icon\" ng-click=\"preventDefault($event); \"></span>\n" +
    "        <input type=\"text\" class=\"form-control ySEPage-list-search-input\" placeholder=\"{{ 'pagelist.searchplaceholder' | translate }}\" data-ng-model=\"pageListCtl.query.value\" name=\"query\">\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"ySEAdd-Page-button\">\n" +
    "        <button class=\"\" data-ng-click=\"pageListCtl.openAddPageWizard()\">\n" +
    "            <img data-ng-src=\"../cmssmartedit/images/icon_add.png\" />\n" +
    "            <span class=\"\">{{'se.cms.addpagewizard.addpage' | translate}}</span>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "    <client-paged-list items=\"pageListCtl.pages\"\n" +
    "                keys=\"[{\n" +
    "                    property:'name',\n" +
    "                    i18n:'pagelist.headerpagename'\n" +
    "                    },{\n" +
    "                    property:'uid',\n" +
    "                    i18n:'pagelist.headerpageid'\n" +
    "                    },{\n" +
    "                    property:'typeCode',\n" +
    "                    i18n:'pagelist.headerpagetype'\n" +
    "                    },{\n" +
    "                    property:'template',\n" +
    "                    i18n:'pagelist.headerpagetemplate'\n" +
    "                    }]\"\n" +
    "                renderers=\"pageListCtl.renderers\"\n" +
    "                injected-context=\"pageListCtl.injectedContext\"\n" +
    "                sort-by=\"'name'\"\n" +
    "                reversed=\"false\"\n" +
    "                items-per-page=\"10\"\n" +
    "                query=\"pageListCtl.query.value\"\n" +
    "                display-count=\"true\"\n" +
    "    ></client-paged-list>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageInfoStepTemplate.html',
    "<page-info-form data-page=\"addPageWizardCtl.model.page\" data-structure=\"addPageWizardCtl.model.pageInfoStructure\" data-shared-data=\"addPageWizardCtl.model\" data-on-submit=\"addPageWizardCtl.createPage()\"></page-info-form>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTemplateStepTemplate.html',
    "<div class=\"page-type-step-template\">\n" +
    "\n" +
    "    <div class=\"page-wizard-list\">\n" +
    "        <div data-ng-repeat=\"template in addPageWizardCtl.model.pageTemplates\" data-ng-class=\"{ 'page-type-step-template__item__selected': template.isSelected}\" class=\"page-type-step-template__item\" data-ng-click=\"addPageWizardCtl.selectTemplate(template)\">\n" +
    "            <span class=\"hyicon hyicon-checked page-type-step-template__item__icon\"></span>\n" +
    "            <div class=\"page-type-step-template__item--info\">\n" +
    "                <div class=\"page-type-step-template__item--info__title\">{{template.name}}</div>\n" +
    "                <div class=\"page-type-step-template__item--info__description\"></div>\n" +
    "        \n" +
    "            </div>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTypeStepTemplate.html',
    "<div class=\"page-type-step-template\">\n" +
    "\n" +
    "    <div class=\"page-type-step-template__text ySEText\">{{'se.cms.addpagewizard.pagetype.description' | translate}}</div>\n" +
    "    <div data-ng-repeat=\"pageType in addPageWizardCtl.model.pageTypes\" data-ng-class=\"{ 'page-type-step-template__item__selected': pageType.isSelected}\" class=\"page-type-step-template__item\" data-ng-click=\"addPageWizardCtl.selectType(pageType)\">\n" +
    "        <span class=\"hyicon hyicon-checked page-type-step-template__item__icon\"></span>\n" +
    "        \n" +
    "        <div class=\"page-type-step-template__item--info\">\n" +
    "            <!--\n" +
    "                NOTE: The last translate is only needed while the temporary pageTypeService is used.\n" +
    "                This should be removed when migrating to the backend service.\n" +
    "            -->\n" +
    "            <div class=\"page-type-step-template__item--info__title\">{{addPageWizardCtl.getLocalizedValue(pageType.name) | translate}}</div>\n" +
    "            <div class=\"page-type-step-template__item--info__description\">{{addPageWizardCtl.getLocalizedValue(pageType.description) | translate}}</div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/synchronize/catalogDetailsSyncTemplate.html',
    "<synchronize-catalog data-catalog=\"catalog\"></synchronize-catalog>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/synchronize/synchronizationTemplate.html',
    "<div>\n" +
    "\t<div class=\"ySECatalogStatus col-xs-8\">\n" +
    "\t\t<div>\n" +
    "\t\t\t<label class=\"ySELastSync\" data-ng-if=\"!ctrl.syncInitiatedFlag\" data-translate=\"cataloginfo.lastsynced\" ></label>\n" +
    "\t\t\t<label class=\"ySELastSync\" data-ng-if=\"ctrl.syncInitiatedFlag\" data-translate=\"cataloginfo.sync.initiated\"></label>\n" +
    "\t\t\t<span class=\"catalog-last-synced\" data-ng-if=\"ctrl.syncInitiatedFlag\">{{ctrl.syncData.startSyncTS| date:'short'}}</span>\n" +
    "\t\t\t<span class=\"catalog-last-synced\" data-ng-if=\"!ctrl.syncInitiatedFlag\">{{ctrl.syncData.lastSyncTS| date:'short'}}</span>\n" +
    "\t\t\t<span data-ng-if=\"ctrl.syncData.showFailure\" class=\"label-error\" data-translate=\"sync.status.synced.syncfailed\"></span>\n" +
    "\t\t</div>\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECatalogSync col-xs-4 pull-right \">\n" +
    "\t\t<button data-ng-if=\"ctrl.syncData.showSyncBtn\" class=\"btn btn-default catalog-sync-btn pull-right\" data-ng-click=\"ctrl.syncCatalog()\">{{ 'cataloginfo.btn.sync' | translate}}</button>\n" +
    "\t\t<label class=\"ySESyncProgress pull-right\" data-ng-if=\"!ctrl.syncData.showSyncBtn\" data-translate=\"sync.status.synced.inprogress\"></label>\n" +
    "\t</div>\n" +
    "</div>\n"
  );

}]);
