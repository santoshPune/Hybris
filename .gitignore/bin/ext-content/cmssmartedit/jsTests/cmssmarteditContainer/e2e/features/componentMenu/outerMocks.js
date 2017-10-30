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
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "value": "\"/cmswebservices/v1/i18n/languages\"",
                "key": "i18nAPIRoot"
            }, {
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/componentMenu/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }, {
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
                    'perspective.none.name': 'PREVIEW',
                    'se.cms.perspective.basic.name': 'Basic CMS',
                    'se.cms.perspective.advanced.name': 'Advanced CMS',
                    'componentmenu.btn.label.addcomponent': 'Add Component',
                    'componentmenu.search.placeholder': 'Search Component',
                    'compomentmenu.tabs.componenttypes': 'Component Types',
                    'compomentmenu.tabs.customizedcomp': 'Components',
                    'componentmenu.label.draganddrop': 'Drag and drop components onto page',
                    'type.cmsparagraphcomponent.name': 'Paragraph Component',
                    'type.footernavigationcomponent.name': 'Footer Component',
                    'type.simplebannercomponent.name': 'Simple Banner Component'
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

            var items = {
                "componentItems": [{
                    "name": "Component1",
                    "typeCode": "Component1",
                    "uid": "Component1",
                }, {
                    "name": "Component2",
                    "typeCode": "Component2",
                    "uid": "Component2",
                }, {
                    "name": "Component3",
                    "typeCode": "Component3",
                    "uid": "Component3",
                }, {
                    "name": "Component4",
                    "typeCode": "Component4",
                    "uid": "Component4",
                }, {
                    "name": "Component5",
                    "typeCode": "Component5",
                    "uid": "Component5",
                }, {
                    "name": "Component6",
                    "typeCode": "Component6",
                    "uid": "Component6",
                }, {
                    "name": "Component7",
                    "typeCode": "Component7",
                    "uid": "Component7",
                }, {
                    "name": "Component8",
                    "typeCode": "Component8",
                    "uid": "Component8",
                }, {
                    "name": "Component9",
                    "typeCode": "Component9",
                    "uid": "Component9",
                }, {
                    "name": "Component10",
                    "typeCode": "Component10",
                    "uid": "Component10",
                }, {
                    "name": "Component11",
                    "typeCode": "Component11",
                    "uid": "Component11",
                }, {
                    "name": "Component12",
                    "typeCode": "Component12",
                    "uid": "Component12",
                }, {
                    "name": "Component13",
                    "typeCode": "Component13",
                    "uid": "Component13",
                }, {
                    "name": "Component14",
                    "typeCode": "Component14",
                    "uid": "Component14",
                }, {
                    "name": "Component15",
                    "typeCode": "Component15",
                    "uid": "Component15",
                }, {
                    "name": "Component16",
                    "typeCode": "Component16",
                    "uid": "Component16",
                }, {
                    "name": "Component17",
                    "typeCode": "Component17",
                    "uid": "Component17",
                }, {
                    "name": "Component18",
                    "typeCode": "Component18",
                    "uid": "Component18",
                }, {
                    "name": "Component19",
                    "typeCode": "Component19",
                    "uid": "Component19",
                }, {
                    "name": "Component20",
                    "typeCode": "Component20",
                    "uid": "Component20",
                }, {
                    "name": "Component21",
                    "typeCode": "Component21",
                    "uid": "Component21",
                }, {
                    "name": "Component22",
                    "typeCode": "Component22",
                    "uid": "Component22",
                }, {
                    "name": "Component23",
                    "typeCode": "Component23",
                    "uid": "Component23",
                }, ]
            };

            $httpBackend.whenGET(/\/cmswebservices\/v1\/sites\/.*\/catalogs\/.*\/versions\/.*\/items\?currentPage=.*&mask=.*&pageSize=.*&sort=.*/).respond(function(method, url, data, headers) {

                var currentPage = url.split('?')[1].split('&')[0].split('=')[1];
                var mask = url.split('?')[1].split('&')[1].split('=')[1];

                var filtered = items.componentItems.filter(function(item) {
                    return mask ? item.name.toUpperCase().indexOf(mask.toUpperCase()) > -1 : true;
                });

                var results = filtered.slice(currentPage * 10, currentPage * 10 + 10);

                var pagedResults = {
                    "pagination": {
                        "count": 10,
                        "page": currentPage,
                        "totalCount": filtered.length,
                        "totalPages": 3
                    },
                    "componentItems": results
                };

                return [200, pagedResults];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/types/).respond({
                "componentTypes": [{
                    "attributes": [{
                        "cmsStructureType": "RichText",
                        "i18nKey": "type.cmsparagraphcomponent.content.name",
                        "localized": true,
                        "qualifier": "content"
                    }],
                    "category": "COMPONENT",
                    "code": "CMSParagraphComponent",
                    "i18nKey": "type.cmsparagraphcomponent.name",
                    "name": "Paragraph"
                }, {
                    "attributes": [{
                        "cmsStructureType": "NavigationNodeSelector",
                        "i18nKey": "type.footernavigationcomponent.navigationnode.name",
                        "localized": false,
                        "qualifier": "navigationNode"
                    }],
                    "category": "COMPONENT",
                    "code": "FooterNavigationComponent",
                    "i18nKey": "type.footernavigationcomponent.name",
                    "name": "Footer Navigation Component"
                }, {
                    "attributes": [{
                        "cmsStructureType": "Media",
                        "i18nKey": "type.simplebannercomponent.media.name",
                        "localized": true,
                        "qualifier": "media"
                    }, {
                        "cmsStructureType": "ShortString",
                        "i18nKey": "type.simplebannercomponent.urllink.name",
                        "localized": false,
                        "qualifier": "urlLink"
                    }, {
                        "cmsStructureType": "Boolean",
                        "i18nKey": "type.simplebannercomponent.external.name",
                        "localized": false,
                        "qualifier": "external"
                    }],
                    "category": "COMPONENT",
                    "code": "SimpleBannerComponent",
                    "i18nKey": "type.simplebannercomponent.name",
                    "name": "Simple Banner Component"
                }]
            });

            $httpBackend.whenGET(/smarteditwebservices\/v1\/i18n\/languages/).respond({
                languages: [{
                    language: 'en',
                    isoCode: 'en',
                    required: true
                }]
            });


            // Set GET response
            $httpBackend.expectGET(/cmssmartedit\/images\/component_default.png/).respond({});


        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
