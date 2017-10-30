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
angular.module('backendMocks', ['ngMockE2E', 'functionsModule', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('URL_FOR_ITEM', /cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/items\/thesmarteditComponentId/)
    .run(function($httpBackend, filterFilter, parseQuery, URL_FOR_ITEM, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({

        });

        $httpBackend.whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "MediaContainer",
                    qualifier: "media",
                    i18nKey: 'type.thesmarteditcomponenttype.media.name',
                    localized: true,
                    required: true,
                    options: [{
                        "value": "widescreen",
                        "label": "cms.media.format.widescreen"
                    }, {
                        "value": "desktop",
                        "label": "cms.media.format.desktop"
                    }, {
                        "value": "tablet",
                        "label": "cms.media.format.tablet"
                    }, {
                        "value": "mobile",
                        "label": "cms.media.format.mobile"
                    }]
                }]
            };

            return [200, structure];
        });


        var component = {
            media: {
                'en': {
                    'widescreen': 'clone4',
                    'desktop': 'dnd5'
                }
            }
        };

        $httpBackend.whenGET(URL_FOR_ITEM).respond(component);
        $httpBackend.whenPUT(URL_FOR_ITEM).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            return [200, component];
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                required: true
            }]
        });



        var medias = [{
            id: '0',
            code: 'clone4',
            description: 'Clone background',
            altText: 'clone alttext',
            realFileName: 'clone_bckg.png',
            url: '/web/webroot/static-resources/images/contextualmenu_more_on.png',
            format: 'widescreen'
        }, {
            id: '1',
            code: 'dnd5',
            description: 'Drag and drop background',
            altText: 'dnd alttext',
            realFileName: 'dnd_bckg.png',
            url: '/web/webroot/static-resources/images/contextualmenu_more_on.png',
            format: 'desktop'
        }];

        $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/media\/(.+)/).respond(function(method, url, data, headers) {
            var identifier = /media\/(.+)/.exec(url)[1];
            var filtered = medias.filter(function(media) {
                return media.code == identifier;
            });

            if (filtered.length == 1) {
                return [200, filtered[0]];
            } else {
                return [404];
            }
        });

        $httpBackend.whenPOST(/cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/media/).respond(function(method, url, data, headers, params) {
            var media = {
                id: medias.length + '',
                code: 'more_bckg.png',
                description: 'more_bckg.png',
                altText: 'more_bckg.png',
                realFileName: 'more_bckg.png',
                url: '/web/webroot/static-resources/images/more_bckg.png'
            };
            medias.push(media);
            return [201, media];
        });

        $httpBackend.whenGET(/^\w+.*/).passThrough();
    });
angular.module('genericEditorApp').requires.push('backendMocks');
