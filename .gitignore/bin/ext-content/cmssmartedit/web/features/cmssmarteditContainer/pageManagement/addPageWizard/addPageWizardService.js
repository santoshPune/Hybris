(function() {
    /**
     * @ngdoc overview
     * @name addPageServiceModule
     * @description
     * # The addPageServiceModule
     *
     * The add page service module provides the functionality necessary to enable the creation of pages through a modal wizard.
     *
     * Use the {@link addPageServiceModule.addPageWizardService addPageWizardService} to open the add page wizard modal.
     *
     */
    angular.module('addPageServiceModule', ['wizardServiceModule', 'pageTypeServiceModule', 'pageTemplateServiceModule', 'languageServiceModule', 'addPageInfoDirectiveModule', 'createPageServiceModule'])
        /**
         * @ngdoc object
         * @name addPageServiceModule.object:BASE_PAGE_INFO_FIELDS
         *
         * @description
         * Injectable angular constant<br/>
         * Defines the generic editor fields that are necessary to edit any page.
         *
         */
        .constant('BASE_PAGE_INFO_FIELDS', [{
            cmsStructureType: "ShortString",
            qualifier: "name",
            i18nKey: 'se.cms.pageinfoform.name',
            required: true
        }, {
            cmsStructureType: "ShortString",
            qualifier: "title",
            i18nKey: 'se.cms.pageinfoform.title',
            localized: true,
            required: true
        }, {
            cmsStructureType: "ShortString",
            qualifier: "uid",
            i18nKey: 'se.cms.pageinfoform.id'
        }])
        /**
         * @ngdoc object
         * @name addPageServiceModule.object:PAGE_INFO_FIELDS_MAP
         *
         * @description
         * Injectable angular constant<br/>
         * Defines a map with the generic editor fields registered to edit pages of a page type.
         *
         */
        .value('PAGE_INFO_FIELDS_MAP', {
            "EmailPage": [{
                cmsStructureType: "ShortString",
                qualifier: "fromEmail",
                i18nKey: 'se.cms.pageinfoform.fromemail'
            }, {
                cmsStructureType: "ShortString",
                qualifier: "fromName",
                i18nKey: 'se.cms.pageinfoform.fromname'
            }],
            "ContentPage": [{
                cmsStructureType: "ShortString",
                qualifier: "label",
                i18nKey: 'se.cms.pageinfoform.label'
            }]
        })
        /**
         * @ngdoc service
         * @name addPageServiceModule.service:addPageWizardService
         *
         * @description
         * The add page wizard service allows opening a modal wizard to create a page.
         *
         */
        .factory('addPageWizardService', function(modalWizard, BASE_PAGE_INFO_FIELDS, PAGE_INFO_FIELDS_MAP) {
            return {
                /**
                 * @ngdoc method
                 * @name addPageServiceModule.service:addPageWizardService#openAddPageWizard
                 * @methodOf addPageServiceModule.service:addPageWizardService
                 *
                 * @description
                 * When called, this method opens a modal window containing a wizard to create new pages.
                 *
                 * @returns {Promise} A promise that will resolve when the modal wizard is closed or reject if it's canceled.
                 *
                 */
                openAddPageWizard: function() {
                    return modalWizard.open({
                        controller: 'addPageWizardController',
                        controllerAs: 'addPageWizardCtl'
                    });
                },
                /**
                 * @ngdoc method
                 * @name addPageServiceModule.service:addPageWizardService#getPageTypeFields
                 * @methodOf addPageServiceModule.service:addPageWizardService
                 *
                 * @description
                 * This method returns the list of generic editor fields used to provide all the page info necessary to create a page of a page type.
                 *
                 * @param {String} pageTypeCode The page type for which to retrieve its associated generic editor fields.
                 * @returns {Array} The list of generic editor fields associated to the provided page type. If no match is found, this method returns the set of default fields.
                 *
                 */
                getPageTypeFields: function(pageTypeCode) {
                    var registeredFields = BASE_PAGE_INFO_FIELDS;
                    var pageInfoFields = (PAGE_INFO_FIELDS_MAP[pageTypeCode]) ? PAGE_INFO_FIELDS_MAP[pageTypeCode] : [];
                    return registeredFields.concat(pageInfoFields);
                }
            };
        })
        /**
         * @ngdoc controller
         * @name addPageServiceModule.controller:addPageWizardController
         *
         * @description
         * The add page wizard controller manages the operation of the wizard used to create new pages.
         */
        .controller('addPageWizardController', function($q, $routeParams, hitch, pageTypeService, pageTemplateService, languageService, addPageWizardService, createPageService) {
            // Constants
            var ADD_PAGE_WIZARD_STEPS = {
                PAGE_TYPE: 'pageType',
                PAGE_TEMPLATE: 'pageTemplate',
                PAGE_INFO: 'pageInfo'
            };
            var DEFAULT_TOOLING_LANGUAGE = "en";

            this.model = {};

            this.siteUID = $routeParams.siteId;
            this.catalogId = $routeParams.catalogId;
            this.catalogVersion = $routeParams.catalogVersion;

            // Wizard Configuration
            this.getWizardConfig = hitch(this, function() {
                var wizardConfig = {
                    isFormValid: hitch(this, this.isFormValid),
                    onNext: hitch(this, this.onNext),
                    onDone: hitch(this, this.onDone),
                    steps: [{
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_TYPE,
                        name: 'se.cms.addpagewizard.pagetype.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTypeStepTemplate.html',
                        actions: []
                    }, {
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE,
                        name: 'se.cms.addpagewizard.pagetemplate.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTemplateStepTemplate.html',
                        actions: []
                    }, {
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_INFO,
                        name: 'se.cms.addpagewizard.pageinfo.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageInfoStepTemplate.html',
                        actions: []
                    }]
                };

                return wizardConfig;
            });

            // Wizard Navigation
            this.isFormValid = function(stepId) {
                var result = false;
                switch (stepId) {
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TYPE:
                        result = (this.model.selectedType && this.model.selectedType.isSelected);
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE:
                        result = (this.model.selectedTemplate && this.model.selectedTemplate.isSelected);
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_INFO:
                        result = (this.model.editor && this.model.editor.isDirty() === true);
                        break;
                }

                return result;
            };

            this.onNext = function(stepId) {
                var result;
                switch (stepId) {
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TYPE:
                        if (this.model.selectedType && this.model.selectedType.isSelected) {
                            result = this.getPageTemplates(this.model.selectedType.code);
                        }
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE:
                        if (this.model.selectedTemplate && this.model.selectedTemplate.isSelected) {
                            var typeCode = this.model.selectedType.code;
                            this.model.pageInfoStructure = addPageWizardService.getPageTypeFields(typeCode);

                            result = $q.when(true);
                        }
                        break;
                    default:
                        result = $q.when(false);
                }

                return result;
            };

            this.onDone = function() {
                return $q.when(this.model.editor.submit(this.model.componentForm));
            };

            this.createPage = function() {
                var page = this.model.editor.component;

                page.type = this.model.selectedType.type;
                page.typeCode = this.model.selectedType.code;
                page.template = this.model.selectedTemplate.uid;
                page.defaultPage = false;

                return createPageService.createPage(this.siteUID, this.catalogId, this.catalogVersion, page).then(hitch(this, function(response) {
                    return {
                        payload: this.model.editor.component,
                        response: response
                    };
                }));
            };

            // Page Type
            pageTypeService.getPageTypeIDs().then(hitch(this, function(pageTypes) {
                this.model.pageTypes = pageTypes;
                this.model.selectedType = null;
            }));

            this.selectType = function(pageType) {
                if (this.model.selectedType) {
                    this.model.selectedType.isSelected = false;
                }

                pageType.isSelected = true;
                this.model.selectedType = pageType;
            };

            // Page Templates
            this.getPageTemplates = function(pageType) {
                return pageTemplateService.getPageTemplatesForType(this.siteUID, this.catalogId, this.catalogVersion, pageType)
                    .then(hitch(this, function(result) {
                        this.model.selectedTemplate = null;
                        this.model.pageTemplates = result.templates;

                        return $q.when(true);
                    }));
            };

            this.selectTemplate = function(pageTemplate) {
                if (this.model.selectedTemplate) {
                    this.model.selectedTemplate.isSelected = false;
                }

                pageTemplate.isSelected = true;
                this.model.selectedTemplate = pageTemplate;
            };

            // Helper Methods
            this.model.toolingLanguage = DEFAULT_TOOLING_LANGUAGE;
            languageService.getResolveLocale().then(hitch(this, function(selectedLanguage) {
                this.model.toolingLanguage = selectedLanguage;
            }));

            this.getLocalizedValue = function(fieldLocalizationMap) {
                var localizedValue = "";
                if (fieldLocalizationMap[this.model.toolingLanguage]) {
                    localizedValue = fieldLocalizationMap[this.model.toolingLanguage];
                } else {
                    var macroLanguage = this.model.toolingLanguage.substring(0, 2);

                    for (var key in fieldLocalizationMap) {
                        if (key.indexOf(macroLanguage) === 0) {
                            localizedValue = fieldLocalizationMap[key];
                            break;
                        }
                    }
                }

                return localizedValue;
            };

        });
})();
