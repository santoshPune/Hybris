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
angular.module('seMediaFieldModule', [
        'seMediaSelectorModule',
        'seMediaUploadFormModule',
        'seFileValidationServiceModule',
        'seErrorsListModule'
    ])
    .factory('seMediaFieldConstants', function(SMARTEDIT_ROOT) {
        return {
            I18N_KEYS: {
                UPLOAD_IMAGE_TO_LIBRARY: 'upload.image.to.library'
            },
            UPLOAD_ICON_URL: 'static-resources/images/upload_image.png',
            DELETE_ICON_URL: 'static-resources/images/remove_image_small.png',
            REPLACE_ICON_URL: 'static-resources/images/replace_image_small.png',
            ADV_INFO_ICON_URL: 'static-resources/images/info_image_small.png'
        };
    })
    .controller('seMediaFieldController', function(seMediaFieldConstants, seFileValidationServiceConstants, seFileValidationService) {
        this.i18nKeys = seMediaFieldConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;
        this.uploadIconUrl = seMediaFieldConstants.UPLOAD_ICON_URL;
        this.deleteIconUrl = seMediaFieldConstants.DELETE_ICON_URL;
        this.replaceIconUrl = seMediaFieldConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = seMediaFieldConstants.ADV_INFO_ICON_URL;
        this.image = {};
        this.fileErrors = [];

        this.fileSelected = function(files, format) {
            this.resetImage();
            if (files.length === 1) {
                seFileValidationService.validate(files[0], this.fileErrors).then(function() {
                    this.image = {
                        file: files[0],
                        format: format || this.image.format
                    };
                }.bind(this));
            }
        };

        this.resetImage = function() {
            this.fileErrors = [];
            this.image = {};
        };

        this.imageUploaded = function(code) {
            this.resetImage();
            this.model[this.qualifier] = code;
            if (this.field.initiated) {
                this.field.initiated.length = 0;
            }
            this.editor.refreshOptions(this.field, this.qualifier, '');
        };
    })
    .directive('seMediaField', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaField/seMediaFieldTemplate.html',
            restrict: 'E',
            controller: 'seMediaFieldController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '='
            }
        };
    });
