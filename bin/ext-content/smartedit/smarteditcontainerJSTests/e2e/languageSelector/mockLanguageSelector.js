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
    .run(function($httpBackend, languageService, $log, parseQuery, I18N_RESOURCE_URI, I18N_LANGUAGES_RESOURCE_URI) {

        $httpBackend.whenGET(I18N_LANGUAGES_RESOURCE_URI).respond({
            languages: [{
                "isoCode": "en",
                "name": "English"
            }, {
                "isoCode": "fr",
                "name": "French"
            }]
        });

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/en").respond(function(method, url, data, headers) {
            return [200, {
                'authentication.form.input.username': 'Name',
                'authentication.form.input.password': 'Password',
                'authentication.form.button.submit': 'Submit',
                'perspective.none.name': 'Preview',
                'localization.field': 'I am a localized link',
                'left.toolbar.cmsuser.name': 'CM User',
                'left.toolbar.sign.out': 'Sign Out',
                'experience.selector.language': 'Language',
                'experience.selector.date.and.time': 'Date and Time',
                'experience.selector.catalog': 'Catalog'
            }];
        });

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/fr").respond(function(method, url, data, headers) {
            return [200, {
                'authentication.form.input.username': 'Nom',
                'authentication.form.input.password': 'Mot de passe',
                'authentication.form.button.submit': 'Soumettre',
                'perspective.none.name': 'Aperçu',
                'localization.field': 'Je suis localisée',
                'left.toolbar.cmsuser.name': 'Utilisateur',
                'left.toolbar.sign.out': 'Deconnexion',
                'experience.selector.language': 'Langue',
                'experience.selector.date.and.time': 'Date et Heure',
                'experience.selector.catalog': 'Catalogue'
            }];
        });

        //        var regex = new RegExp(I18N_RESOURCE_URI + "/(.*)");
        $httpBackend.whenGET(I18N_RESOURCE_URI + "/kl").respond(function(method, url, data, headers) {
            return [200, {
                'authentication.form.input.username': 'klName',
                'authentication.form.input.password': 'klPassword',
                'authentication.form.button.submit': 'Submit',
                'perspective.none.name': 'Preview',
                'localization.field': 'I am a localized link',
                'left.toolbar.cmsuser.name': 'CM User',
                'left.toolbar.sign.out': 'Sign Out',
                'experience.selector.language': 'Language',
                'experience.selector.date.and.time': 'Date and Time',
                'experience.selector.catalog': 'Catalog'
            }];
        });

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/en_US").respond(function(method, url, data, headers) {
            return [200, {
                'authentication.form.input.username': 'en_USName',
                'authentication.form.input.password': 'en_USPass',
                'authentication.form.button.submit': 'dfa',
                'perspective.none.name': 'adsa',
                'localization.field': 'I am a localizesdasd link',
                'left.toolbar.cmsuser.name': 'CM User',
                'left.toolbar.sign.out': 'Sign Out',
                'experience.selector.language': 'Language',
                'experience.selector.date.and.time': 'Date and Time',
                'experience.selector.catalog': 'Catalog'
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

        $httpBackend.whenPOST(/authorizationserver\/oauth\/token/).respond(function(method, url, data, headers) {
            var query = parseQuery(data);
            if (query.client_id === 'smartedit' && query.client_secret === undefined && query.grant_type === 'password' && query.username === 'customermanager' && query.password === '123') {
                return [200, oauthToken0];
            } else {
                return [401];
            }
        });

        var map = [{
            "id": "2",
            "value": "\"thepreviewTicketURI\"",
            "key": "previewTicketURI"
        }, {
            "id": "3",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/languageSelector/innerMocks.js\"}",
            "key": "applications.BackendMockModule"
        }, {
            "id": "4",
            "value": "\"somepath\"",
            "key": "i18nAPIRoot"
        }, {
            "id": "10446547578787",
            "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/outerMocksForPermissions.js\"}",
            "key": "applications.e2ePermissionsMocks"
        }];

        $httpBackend.whenGET(/\/configuration/).respond(
            function(method, url, data, headers) {
                if (headers.Authorization === 'bearer ' + oauthToken0.access_token) {
                    return [200, map];
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
