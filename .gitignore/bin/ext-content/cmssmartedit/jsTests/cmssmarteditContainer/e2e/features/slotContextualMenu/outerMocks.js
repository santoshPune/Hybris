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
    .module('OuterMocks', ['ngMockE2E', 'languageServiceModule', 'resourceLocationsModule'])
    .constant('SMARTEDIT_ROOT', 'buildArtifacts')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/jsTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {
            $httpBackend.whenGET(/jsTests/).passThrough();
            $httpBackend.whenGET(/static-resources/).passThrough();

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "3",
                "value": "\"/cmswebservices/v1/i18n/languages\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "4",
                "value": "{\"smartEditContainerLocation\":\"/web/webroot/cmssmartedit/js/cmssmarteditContainer.js\"}",
                "key": "applications.cmssmarteditContainer"
            }, {
                "id": "5",
                "value": "{\"smartEditLocation\":\"/web/webroot/cmssmartedit/js/cmssmartedit.js\"}",
                "key": "applications.cmssmartedit"
            }, {
                "id": "6",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/util/commonMockedModule/CommonSlotsMocks.js\"}",
                "key": "applications.commonSlotMocksModule"
            }, {
                "id": "7",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/util/commonMockedModule/PageContentComponentSlotsMocks.js\"}",
                "key": "applications.pagesContentSlotsComponentsMocks"
            }, {
                "id": "8",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/util/commonMockedModule/PageContentSlotsMocks.js\"}",
                "key": "applications.pagesContentSlotsMocks"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/util/commonMockedModule/LanguagesMock.js\"}",
                "key": "applications.LanguageMockModule"
            }, {
                "id": "10",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/util/commonMockedModule/i18nMock.js\"}",
                "key": "applications.i18nMockModule"
            }];


            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenPUT(/configuration/).respond(404);


            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html'
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/languages/).respond({
                languages: [{
                    language: 'en',
                    required: true
                }]
            });

            $httpBackend
                .whenGET("/cmswebservices/v1/i18n/languages/" + languageService.getBrowserLocale())
                .respond({});

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                }, {
                    nativeName: 'Polish',
                    isocode: 'pl',
                    required: true
                }, {
                    nativeName: 'Italian',
                    isocode: 'it'
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                }, {
                    nativeName: 'French',
                    isocode: 'fr'
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics"
                        }
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-de'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Toys"
                        }
                    },
                    redirectUrl: 'redirectSomeOtherSite',
                    uid: 'toys'
                }]
            });

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

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-de\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Apparels"
                    }
                },
                uid: 'apparel-de',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel DE Content Catalog"
                        }
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel DE Content Catalog"
                        }
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });




        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
