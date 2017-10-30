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
angular.module('FakeModule', [
        'decoratorServiceModule',
        'contextualMenuServiceModule'
    ])
    .run(function(contextualMenuService, decoratorService, smarteditroot) {

        decoratorService.addMappings({
            '^((?!Slot).)*$': ['contextualMenu']
        });

        decoratorService.enable('contextualMenu');

        contextualMenuService.addItems({
            'componentType1': [{
                key: 'infoFeature',
                i18nKey: 'INFO',
                condition: function(configuration) {
                    configuration.element.addClass('conditionClass1');
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_off.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_on.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }, {
                key: 'infoFeature1',
                i18nKey: 'INFO',
                condition: function(configuration) {
                    configuration.element.addClass('conditionClass1');
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_off.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_on.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }, {
                key: 'infoFeature2',
                i18nKey: 'INFO',
                condition: function(configuration) {
                    configuration.element.addClass('conditionClass1');
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_off.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_on.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }, {
                key: 'infoFeature3',
                i18nKey: 'INFO',
                condition: function(configuration) {
                    configuration.element.addClass('conditionClass1');
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_off.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_on.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }, {
                key: 'infoFeature4',
                i18nKey: 'INFO',
                condition: function(configuration) {
                    configuration.element.addClass('conditionClass1');
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_off.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/contextualmenu_edit_on.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }],
            'componentType2': [{
                key: 'deleteFeature',
                i18nKey: 'DELETE',
                condition: function(componentType, componentId) {
                    return true;
                },
                callback: function() {
                    alert('delete for paragraph component');
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/trash_small.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/trash_small.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }],
            'componentType3': [{
                key: 'enableDisableContextualMenuItems',
                i18nKey: 'enable',
                callback: function() {
                    if (!areItemsEnabled) {
                        addContextualMenuItems();
                    } else {
                        removeContextualMenuItems();
                    }
                    areItemsEnabled = !areItemsEnabled;
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }]
        });

        var areItemsEnabled = false;
        var addContextualMenuItems = function() {
            contextualMenuService.addItems({
                'componentType4': [{
                    key: 'newFeature',
                    i18nKey: 'INFO',
                    condition: function(configuration) {
                        configuration.element.addClass('conditionClass1');
                        return true;
                    },
                    callback: function() {
                        alert('new Feature called');
                    },
                    displayClass: 'editbutton',
                    iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                    iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                    smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
                }]
            });
        };

        var removeContextualMenuItems = function() {
            contextualMenuService.removeItemByKey('newFeature');
        };
    });
