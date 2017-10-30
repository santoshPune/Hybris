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

angular.module('personalizationsmarteditmodule', [
        'decoratorServiceModule',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditComponentLightUpDecorator',
        'personalizationsmarteditContextMenu',
        'personalizationsmarteditCommons',
        'featureServiceModule'
    ])
    .directive('body', ['personalizationsmarteditContextService', function(personalizationsmarteditContextService) {
        return {
            link: function(scope, element, attrs) {
                scope.$watch(
                    function() {
                        return element.attr('data-smartedit-ready');
                    },
                    function(newValue, oldValue) {
                        if (newValue !== oldValue && (newValue === true || newValue === "true")) {
                            personalizationsmarteditContextService.applySynchronization();
                        }
                    }, true);
                scope.$watch('element.class', function() {
                    var pageIdArray = element.attr('class').split(" ").filter(function(elem) {
                        return /smartedit-page-uid\-(\S+)/.test(elem);
                    });
                    if (pageIdArray.length > 0) {
                        var pageId = /smartedit-page-uid\-(\S+)/.exec(pageIdArray[0])[1];
                        personalizationsmarteditContextService.setPageId(pageId);
                        if (pageIdArray.length > 1) {
                            console.log("more than one page- class element attribute defined");
                        }
                    }
                }, true);

            }
        };
    }])
    .run(
        ['decoratorService', 'personalizationsmarteditContextService', 'personalizationsmarteditContextServiceProxy', 'personalizationsmarteditContextModal', 'featureService', 'personalizationsmarteditUtils', function(decoratorService, personalizationsmarteditContextService, personalizationsmarteditContextServiceProxy, personalizationsmarteditContextModal, featureService, personalizationsmarteditUtils) {
            var PersonalizationviewContextServiceProxy = new personalizationsmarteditContextServiceProxy('PersonalizationCtxGateway');

            decoratorService.addMappings({
                '^.*Component$': ['personalizationsmarteditComponentLightUp'],
            });

            featureService.addDecorator({
                key: 'personalizationsmarteditComponentLightUp',
                nameI18nKey: 'personalizationsmarteditComponentLightUp'
            });

            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.add.action",
                i18nKey: 'personalization.context.add.action',
                nameI18nKey: 'personalization.context.add.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuAddItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openAddAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_add_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_add_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.edit.action",
                i18nKey: 'personalization.context.edit.action',
                nameI18nKey: 'personalization.context.edit.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuEditItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openEditAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_edit_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_edit_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.delete.action",
                i18nKey: 'personalization.context.delete.action',
                nameI18nKey: 'personalization.context.delete.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuDeleteItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    var element = angular.element($event.target);
                    personalizationsmarteditContextModal.openDeleteAction(config.componentType, config.componentId, config.containerId, config.slotId, personalizationsmarteditUtils.getActionIdForElement(element));
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_delete_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_delete_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });
            featureService.addContextualMenuButton({
                key: "personalizationsmartedit.context.info.action",
                i18nKey: 'personalization.context.info.action',
                nameI18nKey: 'personalization.context.info.action',
                regexpKeys: ['^.*Component$'],
                condition: function(config) {
                    return personalizationsmarteditContextService.isContextualMenuInfoItemEnabled(config.element);
                },
                callback: function(config, $event) {
                    personalizationsmarteditContextModal.openInfoAction();
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: '/personalizationsmartedit/icons/contextualmenu_info_off.png',
                iconNonIdle: '/personalizationsmartedit/icons/contextualmenu_info_on.png',
                smallIcon: 'hyicon hyicon-remove'
            });



        }]);
