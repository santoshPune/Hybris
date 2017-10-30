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
angular.module('outerapp', ['ui.bootstrap', 'gatewayFactoryModule', 'toolbarModule', 'coretemplates', 'ngMockE2E', 'loadConfigModule', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function(gatewayFactory) {
        gatewayFactory.initListener();
    })
    .controller('defaultController', function($rootScope, $scope, toolbarServiceFactory, $httpBackend, I18N_RESOURCE_URI, languageService) {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolbar');

        var testRoot = "../../smarteditcontainerJSTests/e2e/toolbars/itemMechanism/";

        $scope.sendActions = function() {
            toolbarService.addItems([{
                key: 'toolbar.action.action5',
                type: 'ACTION',
                i18nKey: 'toolbar.action.action5',
                callback: function() {
                    console.info('called');
                    $scope.message = 'Action 5 called';
                    setTimeout(function() {
                        delete $scope.message;
                        $scope.$digest();
                    }, 2000);
                },
                icons: {
                    'default': testRoot + 'icon5.png'
                }
            }, {
                key: 'toolbar.standardTemplate',
                type: 'TEMPLATE',
                include: testRoot + 'standardTemplate.html'
            }, {
                key: 'toolbar.action.action6',
                type: 'HYBRID_ACTION',
                i18nKey: 'toolbar.action.action6',
                callback: function() {
                    console.info('called');
                    $scope.message = 'Action 6 called';
                    setTimeout(function() {
                        delete $scope.message;
                        $scope.$digest();
                    }, 2000);
                },
                icons: {
                    'default': testRoot + 'icon6.png'
                },
                include: testRoot + 'hybridActionTemplate.html'
            }]);
        };

        $scope.removeActions = function() {
            toolbarService.removeItemByKey('toolbar.standardTemplate');
            toolbarService.removeItemByKey('toolbar.action.action5');
        };

        $httpBackend.whenGET(/configuration/).respond([{
            "id": "8796289666477",
            "value": "\"somepath\"",
            "key": "i18nAPIRoot"
        }]);

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            "toolbar.action.action3": "action3",
            "toolbar.action.action4": "action4",
            "toolbar.action.action5": "action5",
            "toolbar.action.action6": "action6"
        });

        $httpBackend.whenGET(/.*/).passThrough();
    });
