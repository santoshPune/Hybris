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
                "id": "8",
                "value": "\"/cmswebservices/v1/i18n/languages\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/landingPage/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }, {
                "id": "11",
                "value": "{\"smartEditContainerLocation\":\"/web/webroot/cmssmartedit/js/cmssmarteditContainer.js\"}",
                "key": "applications.cmssmarteditContainer"
            }];

            $httpBackend
                .whenGET(document.location.origin + "/cmswebservices/i18n/languages/" + languageService.getBrowserLocale())
                .respond({
                    'landingpage.title': 'Your Touchpoints',
                    'cataloginfo.pagelist': 'PAGE LIST',
                    'cataloginfo.lastsynced': 'LAST SYNCED',
                    'cataloginfo.button.sync': 'SYNC',
                });

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
                'sync.confirm.msg': 'this {{catalogName}}is a test'
            });

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

            $httpBackend.whenGET(/fragments/).passThrough();


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


            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: '2016-01-29T16:25:28',
                syncStatus: 'RUNNING',
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'


            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2015-01-29T16:25:44",
                syncStatus: "FINISHED",
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'SUCCESS',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2014-01-28T17:05:29",
                syncStatus: "FINISHED",
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2013-01-28T17:05:29",
                syncStatus: "FINISHED",
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: '2016-01-29T16:25:28',
                syncStatus: 'RUNNING',
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2016-01-29T16:25:44",
                syncStatus: "ABORTED",
                endDate: '2016-01-30T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'FAILURE',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2014-01-28T17:05:29",
                syncStatus: "FINISHED",
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: "2013-01-28T17:05:29",
                syncStatus: "FINISHED",
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        en: "Electronics"
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        en: "Apparels"
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        en: "Apparels"
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-de'
                }, {
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        en: "Toys"
                    },
                    redirectUrl: 'redirectSomeOtherSite',
                    uid: 'toys'
                }]
            });

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

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-de\/catalogversiondetails/).respond({
                name: {
                    en: "Apparels"
                },
                uid: 'apparel-de',
                catalogVersionDetails: [{
                    name: {
                        en: "Apparel DE Content Catalog"
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Apparel DE Content Catalog"
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/toys\/catalogversiondetails/).respond({
                name: {
                    en: "Toys"
                },
                uid: 'toys',
                catalogVersionDetails: [{
                    name: {
                        en: "Action Figures Content Catalog"
                    },
                    catalogId: 'actionFiguresContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Action Figures Content Catalog"
                    },
                    catalogId: 'actionFiguresContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Stuffed Toys Content Catalog"
                    },
                    catalogId: 'stuffedToysContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        en: "Stuffed Toys Content Catalog"
                    },
                    catalogId: 'stuffedToysContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
