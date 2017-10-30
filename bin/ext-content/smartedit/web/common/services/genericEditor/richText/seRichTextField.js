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
angular.module('seRichTextFieldModule', ['ngSanitize', 'languageServiceModule'])
    .constant('seRichTextConfiguration', {
        toolbar: 'full',
        toolbar_full: [{
                name: 'basicstyles',
                items: ['Bold', 'Italic', 'Strike', 'Underline']
            }, {
                name: 'paragraph',
                items: ['BulletedList', 'NumberedList', 'Blockquote']
            }, {
                name: 'editing',
                items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']
            }, {
                name: 'links',
                items: ['Link', 'Unlink', 'Anchor']
            }, {
                name: 'tools',
                items: ['SpellChecker', 'Maximize']
            },
            '/', {
                name: 'styles',
                items: ['Format', 'FontSize', 'TextColor', 'PasteText', 'PasteFromWord', 'RemoveFormat']
            }, {
                name: 'insert',
                items: ['Image', 'Table', 'SpecialChar']
            }, {
                name: 'forms',
                items: ['Outdent', 'Indent']
            }, {
                name: 'clipboard',
                items: ['Undo', 'Redo']
            }, {
                name: 'document',
                items: ['PageBreak', 'Source']
            }
        ],
        disableNativeSpellChecker: false,
        height: '100px',
        width: '100%',
        autoParagraph: false,
        enterMode: CKEDITOR.ENTER_BR,
        shiftEnterMode: CKEDITOR.ENTER_BR,
        basicEntities: false,
        allowedContent: true,
        fillEmptyBlocks: false,
        contentsCss: 'static-resources/dist/smartedit/css/outer-styling.css'
    })
    .service('seRichTextLoaderService', function($q, $interval) {
        var loadDeferred = $q.defer();

        var checkLoadedInterval = $interval(function() {
            if (CKEDITOR.status === 'loaded') {
                loadDeferred.resolve();
                $interval.cancel(checkLoadedInterval);
            }
        }, 100);

        return {
            load: function() {
                var deferred = $q.defer();
                loadDeferred.promise.then(function() {
                    deferred.resolve();
                });
                return deferred.promise;
            }
        };
    })
    .service('genericEditorSanitizationService', function($sanitize) {
        return {
            isSanitized: function(content) {

                var sanitizedContent = $sanitize(content);
                sanitizedContent = sanitizedContent.replace(/&#10;/g, '\n').replace(/&#160;/g, "\u00a0").replace(/<br>/g, '<br />');
                content = content.replace(/&#10;/g, '\n').replace(/&#160;/g, "\u00a0").replace(/<br>/g, '<br />');
                var sanitizedContentMatchesContent = sanitizedContent === content;
                return sanitizedContentMatchesContent;
            }
        };
    })
    .controller('seRichTextFieldController', function() {})
    .directive('seRichTextField', function(seRichTextLoaderService, seRichTextConfiguration, genericEditorSanitizationService, seRichTextFieldLocalizationService) {
        return {
            restrict: 'E',
            templateUrl: 'web/common/services/genericEditor/richText/seRichTextFieldTemplate.html',
            scope: {},
            controller: 'seRichTextFieldController',
            controllerAs: 'ctrl',
            bindToController: {
                field: '=',
                qualifier: '=',
                model: '=',
                editor: '='

            },
            link: function($scope, $element) {
                seRichTextLoaderService.load().then(function() {
                    var textAreaElement = $element.find('textarea')[0];
                    var editorInstance = CKEDITOR.replace(textAreaElement, seRichTextConfiguration);

                    seRichTextFieldLocalizationService.localizeCKEditor();

                    $element.bind('$destroy', function() {
                        if (editorInstance && CKEDITOR.instances[editorInstance.name]) {
                            CKEDITOR.instances[editorInstance.name].destroy();
                        }
                    });

                    $scope.ctrl.onChange = function() {
                        $scope.$apply(function() {
                            $scope.ctrl.model[$scope.ctrl.qualifier] = editorInstance.getData();
                            $scope.ctrl.reassignUserCheck();

                        });
                    };

                    $scope.ctrl.onMode = function() {
                        if (this.mode == 'source') {
                            var editable = editorInstance.editable();
                            editable.attachListener(editable, 'input', function() {
                                editorInstance.fire('change');
                            });
                        }
                    };

                    $scope.ctrl.onInstanceReady = function(ev) {
                        ev.editor.dataProcessor.writer.setRules('br', {
                            indent: false,
                            breakBeforeOpen: false,
                            breakAfterOpen: false,
                            breakBeforeClose: false,
                            breakAfterClose: false
                        });
                    };

                    $scope.ctrl.requiresUserCheck = function() {
                        var requiresUserCheck = false;
                        for (var qualifier in this.field.requiresUserCheck) {
                            requiresUserCheck = requiresUserCheck || this.field.requiresUserCheck[qualifier];
                        }
                        return requiresUserCheck;
                    };


                    $scope.ctrl.reassignUserCheck = function() {
                        if ($scope.ctrl.model && $scope.ctrl.qualifier && $scope.ctrl.model[$scope.ctrl.qualifier]) {
                            var sanitizedContentMatchesContent = genericEditorSanitizationService.isSanitized($scope.ctrl.model[$scope.ctrl.qualifier]);
                            $scope.ctrl.field.requiresUserCheck = $scope.ctrl.field.requiresUserCheck || {};
                            $scope.ctrl.field.requiresUserCheck[$scope.ctrl.qualifier] = !sanitizedContentMatchesContent;
                        } else {
                            $scope.ctrl.field.requiresUserCheck = $scope.ctrl.field.requiresUserCheck || {};
                            $scope.ctrl.field.requiresUserCheck[$scope.ctrl.qualifier] = false;
                        }
                    };

                    editorInstance.on('change', $scope.ctrl.onChange);
                    editorInstance.on('mode', $scope.ctrl.onMode);
                    CKEDITOR.on('instanceReady', $scope.ctrl.onInstanceReady);

                });
            }
        };
    })
    .constant('resolvedLocaleToCKEDITORLocaleMap', {
        'in': 'id',
        'es_CO': 'es'
    })
    .service('seRichTextFieldLocalizationService', function(languageService, resolvedLocaleToCKEDITORLocaleMap) {

        var convertResolvedToCKEditorLocale = function(resolvedLocale) {
            var conversion = resolvedLocaleToCKEDITORLocaleMap[resolvedLocale];
            if (conversion) {
                return conversion;
            } else {
                return resolvedLocale;
            }
        };

        this.localizeCKEditor = function() {
            languageService.getResolveLocale().then(function(locale) {
                CKEDITOR.config.language = convertResolvedToCKEditorLocale(locale);
            });
        };

    });
