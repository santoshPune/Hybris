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
angular.module('BackendMockModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, languageService, I18N_RESOURCE_URI, I18N_LANGUAGES_RESOURCE_URI) {

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
                'localization.field': 'I am localized',
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

        $httpBackend.whenGET(/^\w+.*/).passThrough();
    });
