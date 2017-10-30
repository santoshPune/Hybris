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
angular.module('genericEditorApp', [
        'genericEditorModule',
        'eventServiceModule',
        'localizedElementModule',
        'fetchMediaDataHandlerModule'
    ])
    .controller('defaultController', function(restServiceFactory, sharedDataService) {
        restServiceFactory.setDomain('thedomain');
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            },
            catalogDescriptor: {
                catalogId: 'electronics',
                catalogVersion: 'staged'
            }
        });

        this.thesmarteditComponentType = 'thesmarteditComponentType';
        this.thesmarteditComponentId = 'thesmarteditComponentId';
        this.structureApi = "cmswebservices/v1/types/:smarteditComponentType";
        this.displaySubmit = true;
        this.displayCancel = true;
        this.CONTEXT_CATALOG = "CURRENT_CONTEXT_CATALOG";
        this.CONTEXT_CATALOG_VERSION = "CURRENT_CONTEXT_CATALOG_VERSION";

        this.structureForBasic = [{
            cmsStructureType: "ShortString",
            qualifier: "name",
            i18nKey: 'type.Item.name.name'
        }, {
            cmsStructureType: "Date",
            qualifier: "creationtime",
            i18nKey: 'type.AbstractItem.creationtime.name',
            editable: false
        }, {
            cmsStructureType: "Date",
            qualifier: "modifiedtime",
            i18nKey: 'type.AbstractItem.modifiedtime.name',
            editable: false
        }];

        this.structureForVisibility = [{
            cmsStructureType: "Boolean",
            qualifier: "visible",
            i18nKey: 'type.AbstractCMSComponent.visible.name'
        }];

        this.structureForAdmin = [{
            cmsStructureType: "ShortString",
            qualifier: "uid",
            i18nKey: 'type.Item.uid.name'
        }, {
            cmsStructureType: "ShortString",
            qualifier: "pk",
            i18nKey: 'type.AbstractItem.pk.name',
            editable: false
        }];

        this.contentApi = '/cmswebservices/v1/catalogs/' + this.CONTEXT_CATALOG + '/versions/' + this.CONTEXT_CATALOG_VERSION + '/items';

    });
