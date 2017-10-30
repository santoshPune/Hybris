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
    .run(function($httpBackend, languageService, I18N_RESOURCE_URI) {

        var map = [{
            "id": "2",
            "value": "\"thepreviewTicketURI\"",
            "key": "previewTicketURI"
        }, {
            "id": "3",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/decorators/BasicSlotContextualMenuDecorator.js\"}",
            "key": "applications.basicContextualMenuDecoratorModule"
        }, {
            "id": "4",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"}",
            "key": "applications.i18nMockModule"
        }, {
            "id": "5",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/OthersMock.js\"}",
            "key": "applications.OthersMockModule"
        }, {
            "id": "6",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/LanguagesMock.js\"}",
            "key": "applications.LanguageMockModule"
        }, {
            "id": "7",
            "value": "\"somepath\"",
            "key": "i18nAPIRoot"
        }];

        $httpBackend.whenGET(/configuration$/).respond(
            function(method, url, data, headers) {
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

        $httpBackend
            .whenGET("/smarteditwebservices\/v1\/i18n\/languages")
            .respond({});
    });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
