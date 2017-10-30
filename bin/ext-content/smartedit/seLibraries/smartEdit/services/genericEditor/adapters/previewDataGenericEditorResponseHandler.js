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
angular.module('previewDataGenericEditorResponseHandlerModule', [])
    /**
     * @ngdoc service
     * @name previewDataGenericEditorResponseHandlerModule.service:previewDataGenericEditorResponseHandler
     * @description
     * previewDataGenericEditorResponseHandler is invoked by {@link genericEditorModule.service:GenericEditor GenericEditor} to handle POST response
     * to the preview API.
     * It is responsible for retrieving ticketId and setting it into the model for use in the updateCallback
     * It must not set the smarteditComponentId into the editor so as to allow for multiple POST
     * @param {Object} editor the editor invoking the response handler
     * @param {Object} response the response from the POST call to the server
     */
    .factory('previewDataGenericEditorResponseHandler', function() {

        var PreviewDataGenericEditorResponseHandler = function(editor, response) {
            editor.pristine.ticketId = response.ticketId;
        };


        return PreviewDataGenericEditorResponseHandler;
    });
