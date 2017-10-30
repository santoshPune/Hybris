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
(function() {

    angular.module('e2eOnLoadingSetup', ['ngMockE2E', 'resourceLocationsModule'])
        .run(function(STOREFRONT_PATH, $location, $timeout, $httpBackend) {

            $httpBackend.whenGET(/smarteditcontainerJSTests/).passThrough();
            $httpBackend.whenGET(/static-resources/).passThrough();

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/catalogversiondetails/).respond({
                name: {
                    en: "Electronics"
                },
                uid: 'electronics',
                catalogVersionDetails: [{
                    name: {
                        en: "Electronics Content Catalog"
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Electronics Content Catalog"
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogversiondetails/).respond({
                name: {
                    en: "Apparels"
                },
                uid: 'apparel-uk',
                catalogVersionDetails: [{
                    name: {
                        en: "Apparel UK Content Catalog"
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Apparel UK Content Catalog"
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        en: "Electronics"
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        en: "Apparels"
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                }]
            });

            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html'
                });

            var pathWithExperience = STOREFRONT_PATH
                .replace(":siteId", "electronics")
                .replace(":catalogId", "electronicsContentCatalog")
                .replace(":catalogVersion", "Online");
            $location.path(pathWithExperience);
        });

    angular.module('smarteditcontainer').constant('perspectiveTest', true);
    angular.module('smarteditcontainer').requires.push('e2eOnLoadingSetup');

})();
