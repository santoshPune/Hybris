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
angular
    .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "8",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/toolbars/pageToolMenu/innerMocksForPageToolMenu.js\"}",
                "key": "applications.InnerMocks"
            }, {
                "id": "10",
                "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/toolbars/pageToolMenu/pageToolMenuApp.js\"}",
                "key": "applications.pageToolMenuApp"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenPUT(/configuration/).respond(404);

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({
                    "modal.administration.configuration.edit.title": "edit configuration",
                    "page.tool.menu.dummy.item.1": "DUMMY ITEM 1",
                    "page.tool.menu.dummy.item.2": "DUMMY ITEM 2",
                    "page.tool.menu.page.information": "PAGE INFORMATION",
                    "page.tool.menu.clone.page": "CLONE PAGE",
                    "page.tool.menu.page.history": "PAGE HISTORY",
                    "page.tool.menu.associate.campaign": "ASSOCIATE CAMPAIGN",
                    "page.tool.menu.delete.page": "DELETE PAGE",
                    'page.tool.menu.back.button': 'BACK',
                    "template.page.information.title": "Page Information",
                    "template.clone.page.title": "Clone Page",
                    "configurationform.actions.cancel": "cancel",
                    "configurationform.actions.submit": "submit",
                    "configurationform.actions.close": "close",
                    "actions.loadpreview": "load preview",
                    'unknown.request.error': 'Your request could not be processed! Please try again later!',
                    'sync.status.synced.published': 'SYNCED - PUBLISHED'
                });


            $httpBackend.whenGET(/fragments/).passThrough();

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/items\/componentId/).respond({
                catalog: 'Some Catalog - Some Catalog Version',
                dateAndTime: 'Some Date',
                language: 'English'
            });
        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
