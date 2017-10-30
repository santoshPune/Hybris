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
            "genericeditor.dropdown.placeholder": "Select an image",
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "componentform.actions.replaceImage": "Replace Image",
            "type.thesmarteditcomponenttype.id.name": "id",
            "type.thesmarteditcomponenttype.headline.name": "Headline",
            "type.thesmarteditcomponenttype.active.name": "Activation",
            "type.thesmarteditcomponenttype.enabled.name": "Enabled",
            "type.thesmarteditcomponenttype.content.name": "Content",
            "type.thesmarteditcomponenttype.created.name": "Creation date",
            "type.thesmarteditcomponenttype.media.name": "Media",
            "type.thesmarteditcomponenttype.orientation.name": "Orientation",
            "type.thesmarteditcomponenttype.external.name": "External Link",
            "type.thesmarteditcomponenttype.urlLink.name": "Url Link",
            "editor.linkto.label": "Link to",
            "editor.linkto.external.label": "External Link",
            "editor.linkto.internal.label": "Existing Page"
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "Media",
                    qualifier: "media",
                    i18nKey: 'type.thesmarteditcomponenttype.media.name',
                    localized: true,
                    required: true
                }]
            };

            return [200, structure];
        });


        var component = {
            media: {
                'en': 'contextualmenu_delete_off'
            }
        };

        $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                required: true
            }]
        });

        $httpBackend.whenGET(URL_FOR_ITEM).respond(component);
        $httpBackend.whenPUT(URL_FOR_ITEM).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            return [200, component];
        });

        var medias = [{
            id: '1',
            code: 'contextualmenu_delete_off',
            description: 'contextualmenu_delete_off',
            altText: 'contextualmenu_delete_off alttext',
            realFileName: 'contextualmenu_delete_off.png',
            url: '/smarteditcontainerJSTests/e2e/genericEditor/images/contextualmenu_delete_off.png'
        }, {
            id: '2',
            code: 'contextualmenu_delete_on',
            description: 'contextualmenu_delete_on',
            altText: 'contextualmenu_delete_on alttext',
            realFileName: 'contextualmenu_delete_on.png',
            url: '/smarteditcontainerJSTests/e2e/genericEditor/images/contextualmenu_delete_on.png'
        }, {
            id: '3',
            code: 'contextualmenu_edit_off',
            description: 'contextualmenu_edit_off',
            altText: 'contextualmenu_edit_off alttext',
            realFileName: 'contextualmenu_edit_off.png',
            url: '/smarteditcontainerJSTests/e2e/genericEditor/images/contextualmenu_edit_off.png'
        }, {
            id: '3',
            code: 'contextualmenu_edit_on',
            description: 'contextualmenu_edit_on',
            altText: 'contextualmenu_edit_on alttext',
            realFileName: 'contextualmenu_edit_on.png',
            url: '/smarteditcontainerJSTests/e2e/genericEditor/images/contextualmenu_edit_on.png'
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

        $httpBackend.whenGET(/cmswebservices\/v1\/media/).respond(function(method, url, data, headers) {

            var params = parseQuery(url).params;
            var search = params.split(",")[0].split(":").pop();
            var filtered = filterFilter(medias, search);
            return [200, {
                media: filtered
            }];
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

        $httpBackend.whenGET(/i18n/).passThrough();
        $httpBackend.whenGET(/view/).passThrough(); //calls to storefront render API
        $httpBackend.whenPUT(/contentslots/).passThrough();
        $httpBackend.whenGET(/\.html/).passThrough();
    });
angular.module('genericEditorApp').requires.push('backendMocks');
