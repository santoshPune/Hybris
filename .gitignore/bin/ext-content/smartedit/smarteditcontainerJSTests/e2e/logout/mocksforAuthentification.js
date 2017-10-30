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
angular.module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(function($httpBackend, languageService, $log, parseQuery, I18N_RESOURCE_URI) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond(function(method, url, data, headers) {
            return [200, {
                "logindialogform.username.or.password.invalid": "Invalid username or password",
                "logindialogform.username.and.password.required": "Username and password required",
                "modal.administration.configuration.edit.title": "edit configuration",
                "configurationform.actions.cancel": "cancel",
                "configurationform.actions.submit": "submit",
                "configurationform.actions.close": "close",
                "actions.loadpreview": "load preview",
                'unknown.request.error': 'Your request could not be processed! Please try again later!',
                'authentication.form.input.username': 'username',
                'authentication.form.input.password': 'password',
                'authentication.form.button.submit': 'submit'
            }];
        });

        $httpBackend.whenGET(/cmswebservices\/sites\/.*\/languages/).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                name: 'English',
                required: true
            }]
        });



        var oauthToken0 = {
            access_token: 'access-token0',
            token_type: 'bearer'
        };

        var oauthToken1 = {
            access_token: 'access-token1',
            token_type: 'bearer'
        };

        var oauthToken2 = {
            access_token: 'access-token2',
            token_type: 'bearer'
        };

        $httpBackend.whenPOST(/authorizationserver\/oauth\/token/).respond(function(method, url, data, headers) {
            var query = parseQuery(data);
            if (query.client_id === 'smartedit' && query.client_secret === undefined && query.grant_type === 'password' && query.username === 'customermanager' && query.password === '123') {
                return [200, oauthToken0];
            } else {
                return [401];
            }
        });
        $httpBackend.whenPOST(/authEntryPoint1/).respond(function(method, url, data, headers) {
            var query = parseQuery(data);
            if (query.client_id === 'client_id_1' && query.client_secret === 'client_secret_1' && query.grant_type === 'password' && query.username === 'customermanager' && query.password === '1234') {
                return [200, oauthToken1];
            } else {
                return [401];
            }
        });

        $httpBackend.whenPOST(/authEntryPoint2/).respond(function(method, url, data, headers) {
            var query = parseQuery(data);
            if (query.client_id === 'client_id_2' && query.client_secret === 'client_secret_2' && query.grant_type === 'password' && query.username === 'customermanager' && query.password === '12345') {
                return [200, oauthToken2];
            } else {
                return [401];
            }
        });

        $httpBackend.whenGET(/configuration/).respond(function(method, url, data, headers) {
            if (headers.Authorization === 'bearer ' + oauthToken0.access_token) {
                return [200, [{
                    "id": "2",
                    "value": "\"/thepreviewTicketURI\"",
                    "key": "previewTicketURI"
                }, {
                    "id": "3",
                    "value": "{\"/authEntryPoint1\":{\"client_id\":\"client_id_1\",\"client_secret\":\"client_secret_1\"},\"/authEntryPoint2\":{\"client_id\":\"client_id_2\",\"client_secret\":\"client_secret_2\"}}",
                    "key": "authentication.credentials"
                }, {
                    "id": "4",
                    "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/logout/mocksforAuthentification.js\"}",
                    "key": "applications.e2eBackendMocks"
                }, {
                    "id": "8",
                    "value": "{ \"api2\":\"/authEntryPoint2\"}",
                    "key": "authenticationMap"
                }]];
            } else {
                return [401];
            }

        });


        $httpBackend.whenGET(/api1\/somepath/).respond(function(method, url, data, headers) {
            if (headers.Authorization === 'bearer ' + oauthToken1.access_token) {
                return [200, {
                    status: 'OK'
                }];
            } else {
                return [401];
            }
        });

        $httpBackend.whenGET(/api2\/someotherpath/).respond(function(method, url, data, headers) {
            if (headers.Authorization === 'bearer ' + oauthToken2.access_token) {
                return [200, {
                    status: 'OK'
                }];
            } else {
                return [401];
            }
        });

        $httpBackend.whenGET(/fragments/).passThrough();

    });
try {
    angular.module('smarteditloader').requires.push('e2eBackendMocks');
} catch (e) {} //not longer there when smarteditcontainer is bootstrapped
try {
    angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
} catch (e) {} //not there yet when smarteditloader is bootstrapped or in smartedit
