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
                "id": "1",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "2",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "3",
                "value": "{\"smartEditContainerLocation\":\"/path/to/some/non/existent/container/script.js\"}",
                "key": "applications.nonExistentSmartEditContainerModule"
            }, {
                "id": "3",
                "value": "{\"smartEditLocation\":\"/path/to/some/non/existent/application/script.js\"}",
                "key": "applications.nonExistentSmartEditModule"
            }, {
                "id": "3",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/bootstrapResilience/innerMocks.js\"}",
                "key": "applications.BackendMockModule"
            }, {
                "id": "4",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/bootstrapResilience/dummyCmsDecorators.js\"}",
                "key": "applications.someModule"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function() {
                    return [200, map];
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({});

            $httpBackend.whenGET(/^\w+.*/).passThrough();
        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
