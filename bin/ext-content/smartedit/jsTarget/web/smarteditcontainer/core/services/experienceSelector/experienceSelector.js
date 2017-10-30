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
angular.module('experienceSelectorModule', ['eventServiceModule', 'genericEditorModule', 'sharedDataServiceModule', 'loadConfigModule', 'iframeClickDetectionServiceModule', 'restServiceFactoryModule', 'iFrameManagerModule', 'languageServiceModule', 'siteServiceModule', 'catalogServiceModule', 'genericEditorModule', 'resourceLocationsModule', 'l10nModule'])
    .directive('experienceSelector', ['l10nFilter', 'systemEventService', 'languageService', 'catalogService', 'siteService', 'sharedDataService', 'loadConfigManagerService', 'iframeClickDetectionService', 'restServiceFactory', 'iFrameManager', 'GenericEditor', 'PREVIEW_RESOURCE_URI', 'copy', 'hitch', '$log', '$q', '$filter', '$document', 'EVENTS', function(l10nFilter, systemEventService, languageService, catalogService, siteService, sharedDataService, loadConfigManagerService, iframeClickDetectionService, restServiceFactory, iFrameManager, GenericEditor, PREVIEW_RESOURCE_URI, copy, hitch, $log, $q, $filter, $document, EVENTS) {
        return {
            templateUrl: 'web/common/services/genericEditor/genericEditorTemplate.html',
            restrict: 'E',
            transclude: true,
            scope: {
                experience: '=',
                dropdownStatus: '=',
                resetExperienceSelector: '='
            },
            link: function($scope) {
                var catalogVersionDropdownChoices = [],
                    languagesDropdownChoices = [],
                    selectedExperience = {};

                function adaptExperienceToScopeExperience(experience) {
                    selectedExperience.catalog = {
                        label: l10nFilter(experience.catalogDescriptor.name) + ' - ' + experience.catalogDescriptor.catalogVersion,
                        siteDescriptor: experience.siteDescriptor,
                        catalogDescriptor: experience.catalogDescriptor
                    };
                    selectedExperience.time = experience.time;
                    selectedExperience.language = {
                        label: experience.languageDescriptor.nativeName,
                        languageDescriptor: experience.languageDescriptor
                    };
                    selectedExperience.pageId = experience.pageId;
                }

                function initCatalogVersionDropdownChoices() {
                    catalogVersionDropdownChoices.length = 0;
                    siteService.getSites().then(function(siteDescriptors) {
                        siteDescriptors.forEach(function(siteDescriptor) {
                            catalogService.getCatalogsForSite(siteDescriptor.uid).then(function(catalogVersionDescriptors) {
                                catalogVersionDescriptors.forEach(function(catalogVersionDescriptor) {
                                    var candidateDropdownChoice = {
                                        label: l10nFilter(catalogVersionDescriptor.name) + ' - ' + catalogVersionDescriptor.catalogVersion,
                                        catalogDescriptor: catalogVersionDescriptor,
                                        siteDescriptor: siteDescriptor
                                    };

                                    var isInDropdownList = false;
                                    catalogVersionDropdownChoices.forEach(function(catalogVersionDropdownChoice) {
                                        if (catalogVersionDropdownChoice.label === candidateDropdownChoice.label) {
                                            isInDropdownList = true;
                                        }
                                    });

                                    if (!isInDropdownList) {
                                        catalogVersionDropdownChoices.push(candidateDropdownChoice);
                                    }
                                });

                                catalogVersionDropdownChoices = catalogVersionDropdownChoices.sort(function(e1, e2) {
                                    return e1.label.localeCompare(e2.label);
                                });
                            });
                        });
                    });
                }

                function initLanguageDropdownChoices() {
                    languagesDropdownChoices.length = 0;
                    sharedDataService.get('experience').then(function(experience) {
                        languageService.getLanguagesForSite(experience.siteDescriptor.uid).then(function(languages) {
                            languages.forEach(function(languageDescriptor) {
                                languagesDropdownChoices.push({
                                    label: languageDescriptor.nativeName,
                                    languageDescriptor: languageDescriptor
                                });
                            });
                        });
                    });
                }

                function initPristineExperienceState() {
                    sharedDataService.get('experience').then(function(experience) {
                        adaptExperienceToScopeExperience(experience);
                    });
                }

                $scope.resetExperienceSelector = function() {
                    initCatalogVersionDropdownChoices();
                    initLanguageDropdownChoices();
                    initPristineExperienceState();
                    $scope.editor.init();
                };

                sharedDataService.get('configuration').then(function(configuration) {
                    $scope.editor = new GenericEditor({
                        smarteditComponentType: 'DummyPreviewType',
                        smarteditComponentId: null,
                        structureApi: null,
                        structure: [{
                            cmsStructureType: "Dropdown",
                            qualifier: "catalog",
                            i18nKey: 'experience.selector.catalog',
                            options: catalogVersionDropdownChoices
                        }, {
                            cmsStructureType: "Date",
                            qualifier: "time",
                            i18nKey: 'experience.selector.date.and.time'
                        }, {
                            cmsStructureType: "Dropdown",
                            qualifier: "language",
                            i18nKey: 'experience.selector.language',
                            options: languagesDropdownChoices
                        }],
                        contentApi: configuration && configuration.previewTicketURI || PREVIEW_RESOURCE_URI,
                        updateCallback: null,
                        content: selectedExperience
                    });


                    $scope.editor.alwaysShowReset = true;
                    $scope.editor.alwaysShowSubmit = true;

                    $scope.editor.onSubmit = function() {

                        return sharedDataService.get('configuration').then(hitch(this, function(configuration) {

                            return sharedDataService.get('experience').then(hitch(this, function(experience) {

                                var payload = {
                                    catalog: this.component.catalog.catalogDescriptor.catalogId,
                                    catalogVersion: this.component.catalog.catalogDescriptor.catalogVersion,
                                    resourcePath: configuration.domain + this.component.catalog.siteDescriptor.previewUrl,
                                    language: this.component.language.languageDescriptor.isocode,
                                    pageId: experience.pageId
                                };

                                if (this.component.time) {
                                    payload.time = $filter('date')(new Date(this.component.time), 'yyyy-MM-ddTHH:mm:ssZ');
                                }

                                return this.editorCRUDService.save(payload).then(hitch(this, function(response) {
                                    return {
                                        payload: this.component,
                                        response: response
                                    };
                                }));

                            }));

                        }));
                    };

                    $scope.editor.updateCallback = function(payload, response) {
                        $scope.dropdownStatus.isopen = false;
                        sharedDataService.get('configuration').then(function(configuration) {
                            // First ensure that proper clean-up is performed before changing experiences.
                            systemEventService.sendAsynchEvent(EVENTS.CLEAR_PERSPECTIVE_FEATURES);

                            // Then perform the actual update.
                            var experience = {
                                siteDescriptor: payload.catalog.siteDescriptor,
                                catalogDescriptor: payload.catalog.catalogDescriptor,
                                languageDescriptor: payload.language.languageDescriptor,
                                time: $filter('date')(payload.time, $scope.editor.format),
                                pageId: payload.pageId
                            };
                            sharedDataService.set('experience', experience).then(function() {
                                systemEventService.sendAsynchEvent("experienceUpdate");
                                var fullPreviewUrl = configuration.domain + payload.catalog.siteDescriptor.previewUrl;
                                iFrameManager.loadPreview(fullPreviewUrl, response.ticketId);
                                var preview = {
                                    previewTicketId: response.ticketId,
                                    resourcePath: fullPreviewUrl
                                };
                                sharedDataService.set('preview', preview);
                            });
                        });
                    };
                });

                $scope.submitButtonText = 'componentform.actions.apply';
                $scope.cancelButtonText = 'componentform.actions.cancel';
                $scope.modalHeaderTitle = 'experience.selector.header';

                $scope.reset = function() {
                    $scope.editor.reset($scope.componentForm);
                    $scope.dropdownStatus.isopen = false;
                };

                $scope.submit = function() {
                    return $scope.editor.submit($scope.componentForm);
                };

                $scope.$watch(function() {
                    return $scope.editor && $scope.editor.getComponent();
                }, function(newExperience, oldExperience) {
                    if (newExperience &&
                        oldExperience &&
                        newExperience.catalog &&
                        oldExperience.catalog &&
                        newExperience.catalog.siteDescriptor.uid !== oldExperience.catalog.siteDescriptor.uid) {

                        languagesDropdownChoices.length = 0;
                        languageService.getLanguagesForSite(newExperience.catalog.siteDescriptor.uid).then(function(languageDescriptors) {

                            var languageIsoCodes = languageDescriptors.map(function(languageDescriptor) {
                                return languageDescriptor.isocode;
                            });

                            if (languageIsoCodes.indexOf($scope.editor.component.language.languageDescriptor.isocode) < 0) {
                                $scope.editor.component.language = {
                                    label: languageDescriptors[0].nativeName,
                                    languageDescriptor: languageDescriptors[0]
                                };
                            }

                            languageDescriptors.forEach(function(languageDescriptor) {
                                languagesDropdownChoices.push({
                                    label: languageDescriptor.nativeName,
                                    languageDescriptor: languageDescriptor
                                });
                            });
                        });
                    }
                }, true);

                ///////////////////////////////////////////////////////////////////////
                // Close on clicking away from the experience selector
                ///////////////////////////////////////////////////////////////////////

                $document.on('click', function(event) {
                    if ($(event.target).parents('.ySEPreviewSelector').length <= 0) {
                        if ($(event.target).parents('.ui-select-choices-row').length > 0) {
                            return;
                        }

                        if ($scope.dropdownStatus && $scope.dropdownStatus.isopen) {
                            $scope.editor.reset($scope.componentForm);
                            $scope.dropdownStatus.isopen = false;
                            $scope.$apply();
                        }
                    }
                });

                iframeClickDetectionService.registerCallback('closeExperienceSelector', function() {
                    if ($scope.dropdownStatus && $scope.dropdownStatus.isopen) {
                        $scope.editor.reset($scope.componentForm);
                        $scope.dropdownStatus.isopen = false;
                    }
                });
            }
        };
    }]);
