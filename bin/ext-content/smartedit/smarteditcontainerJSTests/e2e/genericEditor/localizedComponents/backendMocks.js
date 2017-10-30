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
    .constant('URL_FOR_ITEM', /cmswebservices\/catalogs\/electronics\/versions\/staged\/items\/thesmarteditComponentId/)
    .run(function($httpBackend, filterFilter, parseQuery, URL_FOR_ITEM, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            "genericeditor.dropdown.placeholder": "Select an image",
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "componentform.actions.replaceImage": "Replace Image",
            "type.thesmarteditComponentType.id.name": "id",
            "type.thesmarteditComponentType.headline.name": "Headline",
            "type.thesmarteditComponentType.active.name": "Activation",
            "type.thesmarteditComponentType.content.name": "Content",
            "type.thesmarteditComponentType.create.name": "Creation date",
            "type.thesmarteditComponentType.media.name": "Media",
            "type.thesmarteditComponentType.external.name": "External Link",
            "type.thesmarteditComponentType.urlLink.name": "Url Link",
            "editor.linkto.label": "Link to",
            "editor.linkto.external.label": "External Link",
            "editor.linkto.internal.label": "Existing Page"
        });

        $httpBackend.whenGET(/cmswebservices\/types\/thesmarteditComponentType/).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "ShortString",
                    qualifier: "id",
                    i18nKey: 'type.thesmarteditComponentType.id.name',
                    localized: false
                }, {
                    cmsStructureType: "LongString",
                    qualifier: "headline",
                    i18nKey: 'type.thesmarteditComponentType.headline.name',
                    localized: false
                }, {
                    cmsStructureType: "Boolean",
                    qualifier: "external",
                    i18nKey: 'type.thesmarteditcomponenttype.external.name'
                }, {
                    cmsStructureType: "Boolean",
                    qualifier: "active",
                    i18nKey: 'type.thesmarteditComponentType.active.name',
                    localized: false
                }, {
                    cmsStructureType: "RichText",
                    qualifier: "content",
                    i18nKey: 'type.thesmarteditComponentType.content.name',
                    localized: true
                }, {
                    cmsStructureType: "Media",
                    qualifier: "media",
                    i18nKey: 'type.thesmarteditComponentType.media.name',
                    localized: true
                }, {
                    cmsStructureType: "ShortString",
                    qualifier: "urlLink",
                    i18nKey: 'type.thesmarteditComponentType.urlLink.name',
                    localized: false
                }]
            };

            return [200, structure];
        });

        var component = {

            id: 'thesmarteditComponentId',
            headline: 'The Headline',
            active: true,
            content: {
                'en': 'the content to edit',
                'pl': 'tresc edytowac',
                'it': 'il contenuto da modificare',
                'hi': 'Sampaadit karanee kee liee saamagree'
            },
            media: {
                'en': '4',
                'pl': '',
                'it': '',
                'hi': ''
            },
            external: true,
            urlLink: 'http://some/external/link'
        };

        $httpBackend.whenGET(/cmswebservices\/sites\/.*\/languages/).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                required: true
            }, {
                nativeName: 'Polish',
                isocode: 'pl'
            }, {
                nativeName: 'Italian',
                isocode: 'it'
            }, {
                nativeName: 'Hindi',
                isocode: 'hi'
            }]
        });

        $httpBackend.whenGET(URL_FOR_ITEM).respond(component);
        $httpBackend.whenPUT(URL_FOR_ITEM).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            return [200, component];
        });
        var medias = [{
            id: '1',
            code: 'pencil1',
            description: 'My blue pencil',
            altText: 'pencil alttext',
            realFileName: 'edit_bckg.png',
            url: 'web/webroot/icons/edit_bckg.png'
        }, {
            id: '2',
            code: 'more2',
            description: 'More background',
            altText: 'more alttext',
            realFileName: 'more_bckg.png',
            url: 'web/webroot/icons/more_bckg.png'
        }, {
            id: '3',
            code: 'trash3',
            description: 'Trash background',
            altText: 'trash alttext',
            realFileName: 'trash_bckg.png',
            url: 'web/webroot/icons/trash_bckg.png'
        }, {
            id: '4',
            code: 'clone4',
            description: 'Clone background',
            altText: 'clone alttext',
            realFileName: 'clone_bckg.png',
            url: 'web/webroot/icons/clone_bckg.png'
        }, {
            id: '5',
            code: 'dnd5',
            description: 'Drag and drop background',
            altText: 'dnd alttext',
            realFileName: 'dnd_bckg.png',
            url: 'web/webroot/icons/dnd_bckg.png'
        }, {
            id: '6',
            code: 'roll6',
            description: 'Rollback background',
            altText: 'rollback alttext',
            realFileName: 'rollback_bckg.png',
            url: 'web/webroot/icons/rollback_bckg.png'
        }];

        $httpBackend.whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media\/(.+)/).respond(function(method, url, data, headers) {
            var identifier = /Media\/(.+)/.exec(url)[1];
            console.info("get media", identifier);
            var filtered = medias.filter(function(media) {
                return media.id == identifier;
            });
            if (filtered.length == 1) {
                return [200, filtered[0]];
            } else {
                return [404];
            }
        });

        $httpBackend.whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media/).respond(function(method, url, data, headers) {
            console.info("query medias");
            var filtered = filterFilter(medias, parseQuery(url).search);
            return [200, {
                medias: filtered
            }];
        });

        $httpBackend.whenGET(/i18n/).passThrough();
        $httpBackend.whenGET(/view/).passThrough(); //calls to storefront render API
        $httpBackend.whenPUT(/contentslots/).passThrough();
        $httpBackend.whenGET(/\.html/).passThrough();

    });
angular.module('genericEditorApp').requires.push('backendMocks');
