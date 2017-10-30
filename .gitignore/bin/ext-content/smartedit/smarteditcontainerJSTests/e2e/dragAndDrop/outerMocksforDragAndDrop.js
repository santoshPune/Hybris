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
    .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(
        function($httpBackend, $locale, I18N_RESOURCE_URI) {

            $httpBackend.resetExpectations();

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "7",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/dragAndDrop/dummyCmsDecorators.js\"}",
                "key": "applications.CMSApp"
            }, {
                "id": "8",
                "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/dragAndDrop/dummyOuterModule.js\"}",
                "key": "applications.outerModule"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/dragAndDrop/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }, {
                "id": "10",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "11",
                "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/dragAndDrop/dragAndDropToolbarExtension.js\"}",
                "key": "applications.dragAndDropToolbar"
            }];

            $httpBackend.whenGET(/configuration/).respond(
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

            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dragAndDrop/TestStoreFront.html'
                });

            var locale = $locale.id.split("-");
            locale = locale[0] + "_" + locale[1].toUpperCase();

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + locale).respond({});


        });

angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
