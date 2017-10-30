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
jQuery(document).ready(function($) {

    var loadCSS = function(href) {
        var cssLink = $("<link rel='stylesheet' type='text/css' href='" + href + "'>");
        $("head").append(cssLink);
    };

    loadCSS("/personalizationsmartedit/css/style.css");

});

angular.module('personalizationsmarteditcontainermodule', [
        'personalizationsmarteditcontainerTemplates',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditRestServiceModule',
        'ui.bootstrap',
        'personalizationsmarteditCommons',
        'functionsModule',
        'personalizationsmarteditPreviewServiceModule',
        'personalizationsmarteditManagerModule',
        'personalizationsmarteditManagerViewModule',
        'personalizationsmarteditContextMenu',
        'featureServiceModule',
        'perspectiveServiceModule',
        'iFrameManagerModule'
    ])
    .factory('personalizationsmarteditIFrameUtils', function(iFrameManager) {
        var iframeUtils = {};
        iframeUtils.reloadPreview = function(resourcePath, previewTicketId) {
            iFrameManager.loadPreview(resourcePath, previewTicketId);
        };
        return iframeUtils;
    })
    .controller('topToolbarMenuController', function($scope, personalizationsmarteditManager, personalizationsmarteditManagerView) {
        $scope.status = {
            isopen: false
        };

        $scope.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

        $scope.createCustomizationClick = function() {
            personalizationsmarteditManager.openCreateCustomizationModal();
        };

        $scope.managerViewClick = function() {
            personalizationsmarteditManagerView.openManagerAction();
        };

    })
    .run(
        function($log, $filter, personalizationsmarteditContextService, personalizationsmarteditContextServiceReverseProxy, personalizationsmarteditManagerView, personalizationsmarteditContextModal, featureService, perspectiveService, personalizationsmarteditPreviewService, personalizationsmarteditIFrameUtils, personalizationsmarteditMessageHandler) {
            var PersonalizationviewContextServiceReverseProxy = new personalizationsmarteditContextServiceReverseProxy('PersonalizationCtxReverseGateway');

            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.right.toolbar',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'personalization.right.toolbar',
                priority: 4,
                section: 'left',
                include: 'web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemWrapperTemplate.html'
            });
            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.manager.toolbar',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'personalization.manager.toolbar',
                priority: 5,
                section: 'left',
                include: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagMenuTemplate.html'
            });
            featureService.register({
                key: 'personalizationsmartedit.context.service',
                nameI18nKey: 'personalization.context.service.name',
                descriptionI18nKey: 'personalization.context.service.description',
                enablingCallback: function() {
                    personalizationsmarteditContextService.setPersonalizationContextEnabled(true);
                },
                disablingCallback: function() {

                    var currentVariations = personalizationsmarteditContextService.selectedVariations;
                    if (angular.isObject(currentVariations) && !angular.isArray(currentVariations)) {
                        var previewTicketId = personalizationsmarteditContextService.getSePreviewData().previewTicketId;
                        personalizationsmarteditPreviewService.removePersonalizationDataFromPreview(previewTicketId).then(function successCallback() {
                            var previewData = personalizationsmarteditContextService.getSePreviewData();
                            personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
                        }, function errorCallback() {
                            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
                        });
                    }

                    personalizationsmarteditContextService.setPersonalizationContextEnabled(false);
                    personalizationsmarteditContextService.selectedCustomizations = null;
                    personalizationsmarteditContextService.selectedVariations = null;
                    personalizationsmarteditContextService.selectedComponents = null;

                }
            });

            perspectiveService.register({
                key: 'personalizationsmartedit.perspective',
                nameI18nKey: 'personalization.perspective.name',
                descriptionI18nKey: 'personalization.perspective.description',
                features: ['personalizationsmartedit.context.service',
                    'personalizationsmartedit.container.right.toolbar',
                    'personalizationsmartedit.container.manager.toolbar',
                    'personalizationsmarteditComponentLightUp',
                    'personalizationsmartedit.context.add.action',
                    'personalizationsmartedit.context.edit.action',
                    'personalizationsmartedit.context.delete.action',
                    'personalizationsmartedit.context.info.action',
                    'se.contextualMenu',
                    'se.cms.edit'
                ],
                perspectives: []
            });

        });
