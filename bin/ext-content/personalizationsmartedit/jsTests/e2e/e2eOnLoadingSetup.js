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
 *
 *
 */
(function() {

    angular.module('e2eOnLoadingSetup', ['ngMockE2E', 'resourceLocationsModule'])
        .run(function(STOREFRONT_PATH, $location, $timeout, $httpBackend) {

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Electronics"
                    }
                },
                uid: 'electronics',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics Content Catalog"
                        }
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics Content Catalog"
                        }
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Apparels"
                    }
                },
                uid: 'apparel-uk',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel UK Content Catalog"
                        }
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel UK Content Catalog"
                        }
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/jsTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics"
                        }
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/jsTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
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
                    resourcePath: document.location.origin + '/jsTests/e2e/dummystorefront.html'
                });

            $httpBackend.whenGET(/smarteditwebservices\/v1\/i18n\/languages/).respond({
                languages: [{
                    "isoCode": "zh",
                    "name": "Chinese"
                }, {
                    "isoCode": "en",
                    "name": "English"
                }]
            });

            var pathWithExperience = STOREFRONT_PATH
                .replace(":siteId", "electronics")
                .replace(":catalogId", "electronicsContentCatalog")
                .replace(":catalogVersion", "Online");
            $location.path(pathWithExperience);
        });

    angular.module('smarteditcontainer').requires.push('e2eOnLoadingSetup');

})();
