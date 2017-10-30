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
angular.module('genericEditorApp', ['genericEditorModule', 'eventServiceModule', 'localizedElementModule'])
    .service('thesmarteditComponentTypeGenericEditorResponseHandler', function() {

        var ThesmarteditComponentTypeEditorResponseHandler = function(editor, response) {
            editor.pristine.ticketId = response.ticketId;
        };
        return ThesmarteditComponentTypeEditorResponseHandler;
    })
    .controller('defaultController', function($rootScope, sharedDataService, restServiceFactory) {
        restServiceFactory.setDomain('thedomain');
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            }
        });

        this.thesmarteditComponentType = 'thesmarteditComponentType';
        this.structureApi = "cmswebservices/v1/types/:smarteditComponentType";
        this.displaySubmit = true;
        this.displayCancel = true;
        this.contentApi = "previewApi";
        this.updateCallback = function(entity) {
            console.info("successful callback on ticket id ", entity.ticketId);
            $rootScope.ticketId = entity.ticketId;
        };
    });
