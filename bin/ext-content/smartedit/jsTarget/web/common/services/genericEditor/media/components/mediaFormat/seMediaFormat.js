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
angular.module('seMediaFormatModule', ['seMediaServiceModule', 'seFileSelectorModule', 'seFileValidationServiceModule'])
    .constant('seMediaFormatConstants', {
        I18N_KEYS: {
            UPLOAD: 'media.format.upload',
            REPLACE: 'media.format.replace',
            UNDER_EDIT: 'media.format.under.edit',
            REMOVE: 'media.format.remove'
        },
        UPLOAD_ICON_URL: 'static-resources/images/upload_image.png',
        UPLOAD_ICON_DIS_URL: 'static-resources/images/upload_image_disabled.png',
        DELETE_ICON_URL: 'static-resources/images/remove_image_small.png',
        REPLACE_ICON_URL: 'static-resources/images/replace_image_small.png',
        ADV_INFO_ICON_URL: 'static-resources/images/info_image_small.png'
    })
    .controller('seMediaFormatController', ['seMediaService', 'seMediaFormatConstants', 'seFileValidationServiceConstants', '$scope', function(seMediaService, seMediaFormatConstants, seFileValidationServiceConstants, $scope) {
        this.i18nKeys = seMediaFormatConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;

        this.uploadIconUrl = seMediaFormatConstants.UPLOAD_ICON_URL;
        this.uploadIconDisabledUrl = seMediaFormatConstants.UPLOAD_ICON_DIS_URL;

        this.deleteIconUrl = seMediaFormatConstants.DELETE_ICON_URL;
        this.replaceIconUrl = seMediaFormatConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = seMediaFormatConstants.ADV_INFO_ICON_URL;

        this.fetchMediaForCode = function() {
            seMediaService.getMediaByCode(this.mediaCode).then(function(val) {
                this.media = val;
            }.bind(this));
        };

        this.isMediaCodeValid = function() {
            return this.mediaCode && typeof this.mediaCode === 'string';
        };

        this.getErrors = function() {
            return (this.errors || []).filter(function(error) {
                return error.format === this.mediaFormat;
            }.bind(this)).map(function(error) {
                return error.message;
            });
        };

        if (this.isMediaCodeValid()) {
            this.fetchMediaForCode();
        }

        $scope.$watch(function() {
            return this.mediaCode;
        }.bind(this), function(mediaCode, oldMediaCode) {
            if (mediaCode && typeof mediaCode === 'string') {
                if (mediaCode !== oldMediaCode) {
                    this.fetchMediaForCode();
                }
            } else {
                this.media = {};
            }
        }.bind(this));
    }])
    .directive('seMediaFormat', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaFormat/seMediaFormatTemplate.html',
            restrict: 'E',
            controller: 'seMediaFormatController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                mediaCode: '=',
                mediaFormat: '=',
                isUnderEdit: '=',
                errors: '=',
                onFileSelect: '&',
                onDelete: '&'
            }
        };
    });
