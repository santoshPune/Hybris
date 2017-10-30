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
angular.module('backendMocks', ['ngMockE2E', 'functionsModule', 'resourceLocationsModule'])
    .run(function($httpBackend, filterFilter, parseQuery, I18N_RESOURCE_URI, LANGUAGE_RESOURCE_URI, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI) {
        $httpBackend.whenGET(new RegExp(I18N_RESOURCE_URI)).respond({
            "editortabset.basictab.title": "BASIC INFO",
            "editortabset.admintab.title": "ADMIN",
            "editortabset.visibilitytab.title": "VISIBILITY",
            "editortabset.generictab.title": "GENERIC",
            "editor.title.suffix": 'Editor',
            "type.Item.name.name": 'Name',
            "type.AbstractItem.creationtime.name": "Created",
            "type.AbstractItem.modifiedtime.name": "Modified",
            "type.AbstractCMSComponent.visible.name": "Component Visibility",
            "type.Item.uid.name": "ID",
            "type.AbstractItem.pk.name": "Key",
            "genericeditor.dropdown.placeholder": "Select an image",
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "componentform.actions.replaceImage": "Replace Image",
            "type.cmsparagraphcomponent.name": "Paragraph Component",
            "type.cmsparagraphcomponent.id.name": "ID",
            "type.cmsitem.name.name": "Name",
            "type.cmsparagraphcomponent.headline.name": "Headline",
            "type.item.creationtime.name": "Created",
            "type.item.modifiedtime.name": "Modified",
            "type.cmsitem.uid.name": "UID",
            "type.item.pk.name": "PK",
            "type.abstractcmscomponent.visible.name": "Visible",
        });

        $httpBackend.whenGET(new RegExp(TYPES_RESOURCE_URI + "/.*")).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "ShortString",
                    qualifier: "id",
                    i18nKey: 'type.cmsparagraphcomponent.id.name'
                }, {
                    cmsStructureType: "LongString",
                    qualifier: "headline",
                    i18nKey: 'type.cmsparagraphcomponent.headline.name'
                }]
            };
            return [200, structure];
        });

        $httpBackend.whenGET(new RegExp(ITEMS_RESOURCE_URI + "/.*")).respond(function(method, url, data, headers) {
            var component = {
                id: 'MyParagraph',
                pk: '123455667',
                uid: 'Comp_0006456345634',
                name: 'Custom Paragraph Component',
                headline: 'The Headline',
                active: true,
                content: 'the content to edit',
                activationDate: new Date().getTime(),
                creationtime: new Date().getTime(),
                modifiedtime: new Date().getTime(),
                media: '4',
                external: false,
                visible: true
            };
            return [200, component];
        });

        $httpBackend.whenGET(new RegExp(LANGUAGE_RESOURCE_URI.replace(/:\w*\//g, '.*/'))).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                name: 'English',
                required: true
            }]
        });

    });
