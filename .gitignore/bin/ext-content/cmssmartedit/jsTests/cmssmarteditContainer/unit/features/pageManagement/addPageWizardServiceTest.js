(function() {
    describe('AddPageWizard ', function() {

        var addPageWizardService, languageService, pageTemplateService, createPageService, pageTypeService, modalWizard;
        var $routeParams, $q, $rootScope, scope, createController;

        beforeEach(function() {
            angular.module('wizardServiceModule', []);
            angular.module('pageTemplateServiceModule', []);
            angular.module('languageServiceModule', []);
            angular.module('addPageInfoDirectiveModule', []);

            module(function($provide) {
                modalWizard = jasmine.createSpyObj('modalWizard', ['open']);
                $provide.value('modalWizard', modalWizard);

                $routeParams = {
                    siteUID: 'someSiteID',
                    catalogId: 'someCatalogID',
                    catalogVersion: 'someCatalogVersion'
                };
                $provide.value('$routeParams', $routeParams);
            });

            module('addPageServiceModule', 'pageTypeServiceModule', function($provide) {
                var basicFieldTypes = ["BasicFields"];
                var pageInfoFields = {
                    "RegisteredType": ["some Fields"]
                };

                $provide.constant("BASE_PAGE_INFO_FIELDS", basicFieldTypes);
                $provide.value("PAGE_INFO_FIELDS_MAP", pageInfoFields);

                languageService = jasmine.createSpyObj('languageService', ['getDefaultToolingLanguages', 'getResolveLocale']);
                $provide.value('languageService', languageService);

                pageTemplateService = jasmine.createSpyObj('pageTemplateService', ['getPageTemplatesForType']);
                $provide.value('pageTemplateService', pageTemplateService);

                createPageService = jasmine.createSpyObj('createPageService', ['createPage']);
                $provide.value('createPageService', createPageService);

                pageTypeService = jasmine.createSpyObj('pageTypeService', ['getPageTypeIDs']);
                $provide.value('pageTypeService', pageTypeService);
            });
            inject(function(_$rootScope_, $controller, _$q_, _addPageWizardService_) {
                $q = _$q_;
                $rootScope = _$rootScope_;
                addPageWizardService = _addPageWizardService_;

                createController = function() {
                    scope = $rootScope.$new();
                    return $controller('addPageWizardController', {
                        '$scope': scope
                    });
                };
            });
        });

        describe('Service - ', function() {
            it('WHEN openAddPageWizard is called THEN the modal wizard is opened', function() {
                // Arrange
                var expectedWizardConfig = {
                    controller: 'addPageWizardController',
                    controllerAs: 'addPageWizardCtl'
                };
                modalWizard.open.andReturn("");

                // Act
                addPageWizardService.openAddPageWizard();

                // Assert
                expect(modalWizard.open).toHaveBeenCalledWith(expectedWizardConfig);
            });

            it('WHEN getPageTypeFields is called for a registered type THEN the basic and the extended fields are returned', function() {
                // Arrange
                var registeredType = "RegisteredType";
                var basicFieldTypes = ["BasicFields"];
                var pageInfoFields = {
                    "RegisteredType": ["some Fields"]
                };
                var expectedResult = basicFieldTypes.concat(pageInfoFields[registeredType]);

                // Act
                var result = addPageWizardService.getPageTypeFields(registeredType);

                // Assert
                expect(result).toEqual(expectedResult);
            });

            it('WHEN getPageTypeFields is called for a non-registered type THEN only the basic fields are returned', function() {
                // Arrange
                var unregisteredType = "UnregisteredType";
                var basicFieldTypes = ["BasicFields"];
                var pageInfoFields = {
                    "RegisteredType": ["some Fields"]
                };
                var expectedResult = basicFieldTypes;

                // Act
                var result = addPageWizardService.getPageTypeFields(unregisteredType);

                // Assert
                expect(result).toEqual(expectedResult);
            });

        });

        describe('Controller -', function() {
            var pageTypeIDs, currentLanguage;

            beforeEach(function() {
                pageTypeIDs = {
                    id: 'someId'
                };
                currentLanguage = "TE_LANGUAGE";

                pageTypeService.getPageTypeIDs.andReturn($q.when(pageTypeIDs));
                languageService.getResolveLocale.andReturn($q.when(currentLanguage));
            });

            it('WHEN isFormValid is called THEN it will return the right result depending on the current state', function() {
                // Arrange
                var addPageWizardController = createController();

                // Act/Assert
                // -- PAGE TYPE
                addPageWizardController.model.selectedType = {
                    isSelected: true
                };
                expect(addPageWizardController.isFormValid('pageType')).toBe(true);

                addPageWizardController.model.selectedType = {
                    isSelected: false
                };
                expect(addPageWizardController.isFormValid('pageType')).toBe(false);

                // -- PAGE TEMPLATE
                addPageWizardController.model.selectedTemplate = {
                    isSelected: true
                };
                expect(addPageWizardController.isFormValid('pageTemplate')).toBe(true);

                addPageWizardController.model.selectedTemplate = {
                    isSelected: false
                };
                expect(addPageWizardController.isFormValid('pageTemplate')).toBe(false);

                // -- PAGE INFO
                addPageWizardController.model.editor = {
                    isDirty: function() {
                        return true;
                    }
                };
                expect(addPageWizardController.isFormValid('pageInfo')).toBe(true);

                addPageWizardController.model.editor = {
                    isDirty: function() {
                        return false;
                    }
                };
                expect(addPageWizardController.isFormValid('pageInfo')).toBe(false);

                // -- ELSE
                expect(addPageWizardController.isFormValid('something else')).toBe(false);
            });

            it('WHEN onNext is called for Page Type THEN it will return true if a template is selected', function() {
                // Arrange
                var addPageWizardController = createController();
                var code = "someCode";
                addPageWizardController.model.selectedType = {
                    code: code,
                    isSelected: true
                };

                spyOn(addPageWizardController, 'getPageTemplates').andReturn($q.when(true));
                spyOn(addPageWizardService, 'getPageTypeFields').andReturn($q.when(true));

                // Act
                var promise = addPageWizardController.onNext('pageType');
                $rootScope.$apply();

                // Assert
                expect(addPageWizardController.getPageTemplates).toHaveBeenCalledWith(code);
                expect(addPageWizardService.getPageTypeFields).not.toHaveBeenCalled();
                promise.then(function(result) {
                    expect(result).toBe(true);
                });
            });

            it('WHEN onNext is called for Page Template THEN it will return true if a type is selected', function() {
                // Arrange
                var addPageWizardController = createController();
                var code = "someCode";
                addPageWizardController.model.selectedType = {
                    code: code,
                    isSelected: true
                };
                addPageWizardController.model.selectedTemplate = {
                    isSelected: true
                };

                spyOn(addPageWizardController, 'getPageTemplates').andReturn($q.when(true));
                spyOn(addPageWizardService, 'getPageTypeFields').andReturn($q.when(true));

                // Act
                var promise = addPageWizardController.onNext('pageTemplate');
                $rootScope.$apply();

                // Assert
                expect(addPageWizardController.getPageTemplates).not.toHaveBeenCalled();
                expect(addPageWizardService.getPageTypeFields).toHaveBeenCalledWith(code);
                promise.then(function(result) {
                    expect(result).toBe(true);
                });
            });

            it('WHEN onNext is called for an invalid page THEN it will return false', function() {
                // Arrange
                var addPageWizardController = createController();
                spyOn(addPageWizardController, 'getPageTemplates').andCallThrough();
                spyOn(addPageWizardService, 'getPageTypeFields').andCallThrough();

                // Act
                var promise = addPageWizardController.onNext('some invalid page');
                $rootScope.$apply();

                // Assert
                expect(addPageWizardController.getPageTemplates).not.toHaveBeenCalled();
                expect(addPageWizardService.getPageTypeFields).not.toHaveBeenCalled();
                promise.then(function(result) {
                    expect(result).toBe(false);
                });
            });

            it('WHEN controller is created THEN it is initialized with the right data', function() {
                // Arrange/Act
                var addPageWizardController = createController();
                $rootScope.$apply();

                // Assert
                expect(pageTypeService.getPageTypeIDs).toHaveBeenCalled();
                expect(addPageWizardController.model.pageTypes).toBe(pageTypeIDs);
                expect(addPageWizardController.model.selectedType).toBe(null);

                expect(languageService.getResolveLocale).toHaveBeenCalled();
                expect(addPageWizardController.model.toolingLanguage).toBe(currentLanguage);
            });

            it('WHEN createPage is called THEN it will delegate to the createPageService with the right parameters', function() {
                // Arrange
                var addPageWizardController = createController();
                var selectedType = {
                    type: 'someType',
                    code: 'someCode'
                };
                var selectedTemplate = {
                    uid: 'someUID'
                };
                var editor = {
                    component: 'some component'
                };

                addPageWizardController.model.editor = editor;
                addPageWizardController.model.selectedType = selectedType;
                addPageWizardController.model.selectedTemplate = selectedTemplate;

                var expectedPage = {
                    type: selectedType.type,
                    code: selectedType.code,
                    template: selectedTemplate.uid,
                    defaultPage: false
                };

                var response = "some response";
                var expectedPayload = {
                    payload: editor.component,
                    response: response
                };
                createPageService.createPage.andReturn($q.when(response));

                // Act
                var promise = addPageWizardController.createPage();
                $rootScope.$apply();

                // Assert
                promise.then(function(result) {
                    expect(createPageService.createPage).toHaveBeenCalledWith(
                        $routeParams.siteUID, $routeParams.catalogId,
                        $routeParams.catalogVersion, expectedPage);
                    expect(result).toEquals(expectedPayload);
                });
            });

            it('WHEN selectType is called THEN the type will be stored appropriately', function() {
                // Arrange
                var originalType = {
                    value: "Old Type",
                    isSelected: true
                };
                var newType = {
                    value: "New Type",
                    isSelected: false
                };
                var addPageWizardController = createController();

                addPageWizardController.model.selectedType = originalType;

                // Act
                addPageWizardController.selectType(newType);

                // Assert
                expect(addPageWizardController.model.selectedType).toBe(newType);
                expect(newType.isSelected).toBe(true);
                expect(originalType.isSelected).toBe(false);
            });

            it('WHEN selectTemplate is called THEN the template will be stored appropriately', function() {
                // Arrange
                var originalTemplate = {
                    value: "Old Template",
                    isSelected: true
                };
                var newTemplate = {
                    value: "New Template",
                    isSelected: false
                };
                var addPageWizardController = createController();

                addPageWizardController.model.selectedTemplate = originalTemplate;

                // Act
                addPageWizardController.selectTemplate(newTemplate);

                // Assert
                expect(addPageWizardController.model.selectedTemplate).toBe(newTemplate);
                expect(newTemplate.isSelected).toBe(true);
                expect(originalTemplate.isSelected).toBe(false);
            });

            it('GIVEN there is an exact match between the default language and a tooling language WHEN getLocalizedValue is called THEN the right value is selected', function() {
                // Arrange
                var addPageWizardController = createController();
                var localizedValue = 'some value';
                var localizationMap = {
                    'TE_LANGUAGE': localizedValue
                };
                $rootScope.$apply();

                // Act
                var result = addPageWizardController.getLocalizedValue(localizationMap);

                // Assert
                expect(result).toBe(localizedValue);
            });

            it('GIVEN there is a partial match between the default language and a tooling language WHEN getLocalizedValue is called THEN the right value is selected', function() {
                // Arrange
                var addPageWizardController = createController();
                var localizedValue = 'some value';
                var localizationMap = {
                    'TE': localizedValue
                };
                $rootScope.$apply();

                // Act
                var result = addPageWizardController.getLocalizedValue(localizationMap);

                // Assert
                expect(result).toBe(localizedValue);
            });

            it('GIVEN there is no match between the default language and a tooling language WHEN getLocalizedValue is called THEN empty is returned', function() {
                // Arrange
                var addPageWizardController = createController();
                var localizationMap = {
                    'OTHER LANGUAGE': 'bla'
                };
                $rootScope.$apply();

                // Act
                var result = addPageWizardController.getLocalizedValue(localizationMap);

                // Assert
                expect(result).toBe("");
            });
        });


    });
})();
