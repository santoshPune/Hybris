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
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/pageList/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }, {
                "id": "11",
                "value": "{\"smartEditContainerLocation\":\"/web/webroot/cmssmartedit/js/cmssmarteditContainer.js\"}",
                "key": "applications.cmssmarteditContainer"
            }];

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({
                    'landingpage.title': 'Your Touchpoints',
                    'cataloginfo.pagelist': 'PAGE LIST',
                    'cataloginfo.lastsynced': 'LAST SYNCED',
                    'cataloginfo.button.sync': 'SYNC',
                    'pagelist.title': 'Page list',
                    'pagelist.searchplaceholder': 'Search page',
                    'pagelist.countsearchresult': 'Pages found',
                    'pagelist.headerpagetitle': 'Page title',
                    'pagelist.headerpageid': 'Page id',
                    'pagelist.headerpagetype': 'Page type',
                    'pagelist.headerpagetemplate': 'Page template',
                    'pagelist.headerparentpage': 'Parent page'
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
                .respond(function(method, url, data, headers) {
                    var dataObject = angular.fromJson(data);
                    if (dataObject.pageId) {
                        return [200, {
                            ticketId: 'previewTicketForPageId',
                            resourcePath: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefrontAlternatelayout.html'
                        }];
                    } else {
                        return [200, {
                            ticketId: 'dasdfasdfasdfa',
                            resourcePath: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html'
                        }];
                    }
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


            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/synchronizations\/versions\/Online/).
            respond({
                creationDate: '2016-01-29T16:25:28',
                syncStatus: 'RUNNING',
                endDate: '2016-01-29T16:25:28',
                lastModifiedDate: '2016-01-29T16:25:28',
                syncResult: 'UNKNOWN',
                startDate: '2016-01-29T16:25:28'
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    name: {
                        en: "Apparels"
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
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

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogs\/apparel-ukContentCatalog\/versions\/Online\/pages/).respond({
                pages: [{
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page1TitleSuffix",
                    typeCode: "ContentPage",
                    uid: "auid1"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "ActionTemplate",
                    name: "welcomePage",
                    typeCode: "ActionPage",
                    uid: "uid2"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "Advertise",
                    typeCode: "MyCustomType",
                    uid: "uid3"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "MyCustomPageTemplate",
                    name: "page2TitleSuffix",
                    typeCode: "HomePage",
                    uid: "uid4"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "ZTemplate",
                    name: "page3TitleSuffix",
                    typeCode: "ProductPage",
                    uid: "uid5"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page3TitleSuffix",
                    typeCode: "ProductPage",
                    uid: "uid6"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page4TitleSuffix",
                    typeCode: "WallPage",
                    uid: "uid7"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page5TitleSuffix",
                    typeCode: "CheckoutPage",
                    uid: "uid8"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page6TitleSuffix",
                    typeCode: "PromoPage",
                    uid: "uid9"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page7TitleSuffix",
                    typeCode: "ProfilePage",
                    uid: "uid10"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page3TitleSuffix",
                    typeCode: "ProductPage",
                    uid: "uid11"
                }, {
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    name: "page3TitleSuffix",
                    typeCode: "ProductPage",
                    uid: "zuid12"
                }]

            });


        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
