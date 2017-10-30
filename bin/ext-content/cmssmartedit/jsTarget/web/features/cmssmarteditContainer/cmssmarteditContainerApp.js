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
angular.module('cmssmarteditContainer', [
        'experienceInterceptorModule',
        'resourceLocationsModule',
        'cmssmarteditContainerTemplates',
        'featureServiceModule',
        'componentMenuModule',
        'editorModalServiceModule',
        'genericEditorModule',
        'eventServiceModule',
        'cmsDragAndDropServiceModule',
        'catalogDetailsModule',
        'synchronizeCatalogModule',
        'perspectiveServiceModule',
        'pageListLinkModule',
        'pageListControllerModule',
        'clientPagedListModule',
        'assetsServiceModule'
    ])
    .config(['PAGE_LIST_PATH', '$routeProvider', function(PAGE_LIST_PATH, $routeProvider) {
        $routeProvider.when(PAGE_LIST_PATH, {
            templateUrl: 'web/features/cmssmarteditContainer/pageList/pageListTemplate.html',
            controller: 'pageListController',
            controllerAs: 'pageListCtl'
        });
    }])
    .run(
        ['$log', '$rootScope', 'ComponentService', 'systemEventService', 'cmsDragAndDropService', 'catalogDetailsService', 'featureService', 'perspectiveService', 'assetsService', function($log, $rootScope, ComponentService, systemEventService, cmsDragAndDropService, catalogDetailsService, featureService, perspectiveService, assetsService) {

            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'se.cms.componentMenuTemplate',
                nameI18nKey: 'cms.toolbarItem.componentMenuTemplate.name',
                descriptionI18nKey: 'cms.toolbarItem.componentMenuTemplate.description',
                type: 'HYBRID_ACTION',
                callback: function() {
                    systemEventService.sendSynchEvent('ySEComponentMenuOpen', {});
                },
                include: 'web/features/cmssmarteditContainer/componentMenu/componentMenuTemplate.html'
            });

            featureService.register({
                key: 'se.cms.dragAndDrop',
                nameI18nKey: 'se.cms.dragAndDrop.name',
                descriptionI18nKey: 'se.cms.dragAndDrop.description',
                enablingCallback: function() {
                    cmsDragAndDropService.register();
                },
                disablingCallback: function() {
                    cmsDragAndDropService.unregister();
                }
            });

            catalogDetailsService.addItems([{
                include: 'web/features/cmssmarteditContainer/pageList/pageListLinkTemplate.html'
            }]);

            catalogDetailsService.addItems([{
                include: 'web/features/cmssmarteditContainer/synchronize/catalogDetailsSyncTemplate.html'
            }]);

            perspectiveService.register({
                key: 'se.cms.perspective.basic',
                nameI18nKey: 'se.cms.perspective.basic.name',
                descriptionI18nKey: 'se,cms.perspective.basic.description',
                features: ['se.contextualMenu', 'se.cms.dragandropbutton', 'se.cms.remove', 'se.cms.edit', 'se.cms.componentMenuTemplate', 'se.cms.dragAndDrop', 'se.emptySlotFix'],
                perspectives: []
            });

            /* Note: For advance edit mode, the ordering of the entries in the features list will determine the order the buttons will show in the slot contextual menu */
            perspectiveService.register({
                key: 'se.cms.perspective.advanced',
                nameI18nKey: 'se.cms.perspective.advanced.name',
                descriptionI18nKey: 'se.cms.perspective.advanced.description',
                features: ['se.slotContextualMenu', 'se.slotSharedButton', 'se.slotContextualMenuVisibility', 'se.contextualMenu', 'se.cms.dragandropbutton', 'se.cms.remove', 'se.cms.edit', 'se.cms.componentMenuTemplate', 'se.cms.dragAndDrop', 'se.emptySlotFix'],
                perspectives: []
            });
        }]);
