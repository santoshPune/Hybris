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
describe('test GenericEditor class', function() {

    var $rootScope, $q, $httpBackend, $translate;
    var smarteditComponentType, smarteditComponentId, updateCallback, GenericEditor, sharedDataService, languageService, restServiceFactory, editorStructureService, editorCRUDService;
    var editorMediaService, editorLanguageService, responseHandler, fetchMediaDataHandler, fetchEnumDataHandler, systemEventServ, seValidationErrorParser, onStructureResolved;
    var escapeHtml;
    var CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION;
    var options = [{
        code: 'code1',
        label: 'label1'
    }, {
        code: 'code2',
        label: 'label2'
    }];
    var STOREFRONT_LANGUAGES = [{
        language: 'en',
        required: true
    }, {
        language: 'pl',
        required: true
    }, {
        language: 'it'
    }];

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventServ = jasmine.createSpyObj('systemEventService', ['registerEventHandler', 'sendAsynchEvent']);
        $provide.value('systemEventService', systemEventServ);
    }));

    beforeEach(module('seValidationErrorParserModule', function($provide) {
        seValidationErrorParser = jasmine.createSpyObj('seValidationErrorParser', ['parse']);
        $provide.value('seValidationErrorParser', seValidationErrorParser);
    }));

    beforeEach(module('sharedDataServiceModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);

        sharedDataService.get.andCallFake(function() {
            return $q.when({
                siteDescriptor: {
                    uid: 'someSiteUid'
                }
            });
        });

        $provide.value('sharedDataService', sharedDataService);
    }));

    beforeEach(module('fetchMediaDataHandlerModule', function($provide) {
        fetchMediaDataHandler = jasmine.createSpyObj('fetchMediaDataHandler', ['findByMask', 'getById']);

        fetchMediaDataHandler.findByMask.andCallFake(function() {
            return $q.when(options);
        });

        fetchMediaDataHandler.getById.andCallFake(function() {
            return $q.when(options[0]);
        });

        $provide.value('fetchMediaDataHandler', fetchMediaDataHandler);
    }));

    beforeEach(module('fetchEnumDataHandlerModule', function($provide) {
        fetchEnumDataHandler = jasmine.createSpyObj('fetchEnumDataHandler', ['findByMask', 'getById']);

        fetchEnumDataHandler.findByMask.andCallFake(function() {
            return $q.when(options);
        });

        $provide.value('fetchEnumDataHandler', fetchEnumDataHandler);
    }));

    beforeEach(module('languageServiceModule', function($provide) {
        languageService = jasmine.createSpyObj('languageService', ['getLanguagesForSite', 'getBrowserLocale']);
        languageService.getLanguagesForSite.andCallFake(function() {
            return $q.when(STOREFRONT_LANGUAGES);
        });
        languageService.getBrowserLocale.andReturn('en_US');
        $provide.value('languageService', languageService);
    }));

    beforeEach(module('functionsModule', function($provide) {
        escapeHtml = jasmine.createSpy('escapeHtml');
        escapeHtml.andReturn('ESCAPED');
        $provide.value('escapeHtml', escapeHtml);
    }));

    beforeEach(module('genericEditorModule', function($provide) {

        smarteditComponentType = "smarteditComponentType";
        smarteditComponentId = "smarteditComponentId";
        updateCallback = function() {};
        onStructureResolved = function() {};

        editorStructureService = jasmine.createSpyObj('restService', ['getById', 'get', 'query', 'page', 'save', 'update', 'remove']);
        editorCRUDService = jasmine.createSpyObj('restService', ['getById', 'get', 'query', 'page', 'save', 'update', 'remove']);
        editorMediaService = jasmine.createSpyObj('restService', ['getById', 'get', 'query', 'page', 'save', 'update', 'remove']);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andCallFake(function(uri) {
            if (uri === '/cmswebservices/types/:smarteditComponentType') {
                return editorStructureService;
            } else if (uri === '/cmswebservices/cmsxdata/contentcatalog/staged/Media') {
                return editorMediaService;
            } else if (uri === '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items') {
                return editorCRUDService;
            }
        });

        $provide.value('restServiceFactory', restServiceFactory);

        responseHandler = jasmine.createSpy('responseHandler');
        $provide.value('smarteditComponentTypeGenericEditorResponseHandler', responseHandler);

        $provide.constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG");
        $provide.constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION");

        $translate = jasmine.createSpyObj('$translate', ['instant']);

        $provide.value('$translate', $translate);

    }));
    beforeEach(customMatchers);
    beforeEach(inject(function(_$rootScope_, _$q_, languageService, I18N_RESOURCE_URI, _GenericEditor_, _$httpBackend_, languageService, _CONTEXT_CATALOG_, _CONTEXT_CATALOG_VERSION_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
        GenericEditor = _GenericEditor_;
        CONTEXT_CATALOG = _CONTEXT_CATALOG_;
        CONTEXT_CATALOG_VERSION = _CONTEXT_CATALOG_VERSION_;

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({});

        searchSelector = jasmine.createSpyObj('searchSelector', ['val', 'trigger']);

        spyOn(GenericEditor.prototype, '_getSelector').andCallFake(function(selectorValue) {
            if (selectorValue === '.ui-select-search') {
                return searchSelector;
            }
        });
    }));

    it('GenericEditor fails to initialize if neither structureApi nor structure are provided', function() {

        expect(function() {
            return new GenericEditor({
                smarteditComponentType: smarteditComponentType,
                smarteditComponentId: smarteditComponentId,
                updateCallback: updateCallback
            });
        }).toThrow("genericEditor.configuration.error.no.structure");

    });

    it('GenericEditor fails to initialize if both structureApi and structure are provided', function() {

        expect(function() {
            return new GenericEditor({
                smarteditComponentType: smarteditComponentType,
                smarteditComponentId: smarteditComponentId,
                structureApi: '/cmswebservices/types/:smarteditComponentType',
                structure: 'structure',
                updateCallback: updateCallback
            });
        }).toThrow("genericEditor.configuration.error.2.structures");

    });

    it('GenericEditor fails to initialize if no contentApi provided', function() {

        expect(function() {
            return new GenericEditor({
                smarteditComponentType: smarteditComponentType,
                smarteditComponentId: smarteditComponentId,
                structureApi: '/cmswebservices/types/:smarteditComponentType',
                updateCallback: updateCallback
            });
        }).toThrow("genericEditor.configuration.error.no.contentapi");

    });


    it('GenericEditor initializes fine with structure API', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback,
            onStructureResolved: onStructureResolved
        });

        expect(editor.smarteditComponentType).toBe(smarteditComponentType);
        expect(editor.smarteditComponentId).toBe(smarteditComponentId);
        expect(editor.updateCallback).toBe(updateCallback);
        expect(editor.component).toBeNull();
        expect(editor.fields).toEqual([]);
        expect(editor.editorStructureService).toBe(editorStructureService);
        expect(editor.editorCRUDService).toBe(editorCRUDService);
        expect(editor.onStructureResolved).toBe(onStructureResolved);
    });

    it('GenericEditor initializes fine with structure', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback,
            onStructureResolved: onStructureResolved
        });

        expect(editor.smarteditComponentType).toBe(smarteditComponentType);
        expect(editor.smarteditComponentId).toBe(smarteditComponentId);
        expect(editor.updateCallback).toBe(updateCallback);
        expect(editor.component).toBeNull();
        expect(editor.fields).toEqual([]);
        expect(editor.editorStructureService).toBeUndefined();
        expect(editor.structure).toBe('structure');
        expect(editor.editorCRUDService).toBe(editorCRUDService);
        expect(editor.onStructureResolved).toBe(onStructureResolved);
    });

    it('GenericEditor fetch executes get with identifier if identifier is set', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editorCRUDService.get.andReturn($q.when("somedata"));

        editor.fetch().then(function(value) {
            expect(value).toBe("somedata");
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(editorCRUDService.get).toHaveBeenCalledWith({
            identifier: smarteditComponentId
        });

    });

    it('GenericEditor fetch executes return empty object if identifier is not set', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editor.fetch().then(function(value) {
            expect(value).toEqual({});
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(editorCRUDService.get).not.toHaveBeenCalled();

    });

    it('calling reset() set component to prior pristine state and call $setPristine on the component form if componentForm is passed and set holders if not set yet', function() {

        var pristine = {
            a: '1',
            b: '2'
        };

        var fields = [{
            field: 'field1',
            initiated: true
        }, {
            field: 'field2',
            initiated: false
        }];

        var linkToStatusObject = {
            hasBoth: function() {
                return true;
            }
        };

        var INPUT = 'input';

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
        });

        editor.fields = fields;
        editor.pristine = pristine;
        editor.linkToStatus = linkToStatusObject;

        editor.reset(componentForm);

        expect(searchSelector.val).toHaveBeenCalledWith('');
        expect(searchSelector.trigger).toHaveBeenCalledWith(INPUT);
        expect(editor.fields).toEqual([{
            field: 'field1'
        }, {
            field: 'field2'
        }]);

        expect(editor.component).not.toBe(pristine);
        expect(editor.component).toEqualData(pristine);
        expect(componentForm.$setPristine).toHaveBeenCalled();

        expect(editor.holders).toEqual([{
            editor: editor,
            field: editor.fields[0]
        }, {
            editor: editor,
            field: editor.fields[1]
        }]);

    });

    it('calling reset() set component to prior pristine state if componentForm is not passed', function() {

        var pristine = {
            a: '1',
            b: '2'
        };

        var fields = [{
            field: 'field1',
            initiated: true
        }, {
            field: 'field2',
            initiated: false
        }];

        var linkToStatusObject = {
            hasBoth: function() {
                return true;
            }
        };

        var INPUT = 'input';

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        spyOn(editor, 'removeValidationErrors').andReturn();
        editor.fields = fields;
        editor.pristine = pristine;

        editor.linkToStatus = linkToStatusObject;
        editor.reset();

        expect(searchSelector.val).toHaveBeenCalledWith('');
        expect(searchSelector.trigger).toHaveBeenCalledWith(INPUT);
        expect(editor.fields).toEqual([{
            field: 'field1'
        }, {
            field: 'field2'
        }]);

        expect(editor.removeValidationErrors).toHaveBeenCalled();

        expect(editor.component).not.toBe(pristine);
        expect(editor.component).toEqualData(pristine);
        expect(componentForm.$setPristine).not.toHaveBeenCalled();

    });

    it('successful load will set component and pristine state and call reset and "localize null" null values of localized properties', function() {

        var data = {
            a: '1',
            b: '2',
            c: null,
            d: {
                en: 'something'
            },
            e: null
        };

        editorCRUDService.get.andReturn($q.when(data));
        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editor.fields = [{
            qualifier: "c",
            localized: true
        }, {
            qualifier: "d",
            localized: true
        }];
        spyOn(editor, 'reset').andReturn();

        editor.load();
        //for promises to actually resolve :
        $rootScope.$digest();

        expect(editorCRUDService.get).toHaveBeenCalledWith({
            identifier: 'smarteditComponentId'
        });

        expect(editor.pristine).toEqualData({
            a: '1',
            b: '2',
            c: {},
            d: {
                en: 'something'
            },
            e: null
        });
        expect(editor.reset).toHaveBeenCalled();

    });

    it('merge will merge editable field values into the refreshedComponent except external and urlLink', function() {

        var genericEditor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });
        genericEditor.fields = [{
            qualifier: 'field1',
            editable: false
        }, {
            qualifier: 'field2',
            editable: true
        }];
        genericEditor.linkToStatus = {
            hasBoth: function() {
                return false;
            }
        };

        expect(genericEditor._merge({
            field1: 'a',
            field2: 'b',
            external: 'true',
            urlLink: 'someLink'
        }, {
            field1: 'c',
            field2: 'd',
            external: 'false',
            urlLink: 'someOtherLink'
        })).toEqual({
            field1: 'a',
            field2: 'd',
            external: 'true',
            urlLink: 'someLink'
        });

    });

    it('merge will merge editable field values into the refreshedComponent including external and urlLink when both present', function() {

        var genericEditor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });
        genericEditor.fields = [{
            qualifier: 'field1',
            editable: false
        }, {
            qualifier: 'field2',
            editable: true
        }];
        genericEditor.linkToStatus = {
            hasBoth: function() {
                return true;
            }
        };
        expect(genericEditor._merge({
            field1: 'a',
            field2: 'b',
            external: 'true',
            urlLink: 'someLink'
        }, {
            field1: 'c',
            field2: 'd',
            external: 'false',
            urlLink: 'someOtherLink'
        })).toEqual({
            field1: 'a',
            field2: 'd',
            external: 'false',
            urlLink: 'someOtherLink'
        });

    });

    it('submit will do nothing if componentForm is not dirty', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });
        spyOn(editor, 'updateCallback').andReturn();
        spyOn(editor, 'reset').andReturn();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, '_displayValidationErrors').andReturn();

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);
        spyOn(editor, 'isDirty').andReturn(false);
        componentForm.$valid = true;

        editor.submit(componentForm);

        // The errors should have been removed. This is necessary in case there was an associated error in a different tab.
        expect(editor.removeValidationErrors).toHaveBeenCalled();

        expect(editorCRUDService.update).not.toHaveBeenCalled();
        expect(editor.reset).not.toHaveBeenCalled();
        expect(editor.updateCallback).not.toHaveBeenCalled();
        expect(editor._displayValidationErrors).not.toHaveBeenCalled();

    });

    it('submit will do nothing if componentForm is not valid', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });
        spyOn(editor, 'updateCallback').andReturn();
        spyOn(editor, 'reset').andReturn();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, '_displayValidationErrors').andReturn();

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);
        spyOn(editor, 'isDirty').andReturn(true);
        componentForm.$valid = false;

        editor.submit(componentForm);

        // The errors should have been removed. This is necessary in case there was an associated error in a different tab.
        expect(editor.removeValidationErrors).toHaveBeenCalled();

        expect(editorCRUDService.update).not.toHaveBeenCalled();
        expect(editor.reset).not.toHaveBeenCalled();
        expect(editor.updateCallback).not.toHaveBeenCalled();
        expect(editor._displayValidationErrors).not.toHaveBeenCalled();
    });

    it('submit will refresh the non editable fields values from server, call save, set pristine state, calls removeValidationErrors, reset and updateCallback if dirty and form valid', function() {

        var refreshedData = {
            a: '1',
            b: '2',
            c: '5'
        };
        var response = {
            someField: 'someValue'
        };

        editorCRUDService.save.andReturn($q.when(response));

        responseHandler.andCallFake(function(editor, response) {
            editor.pristine.someMarker = response.someField;
        });
        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });

        var pristine = {
            a: '1',
            b: '2',
            c: '3'
        };

        var component = {
            a: '1',
            b: '4',
            c: '3'
        };

        editor.pristine = pristine;
        editor.component = component;
        spyOn(editor, 'fetch').andReturn($q.when(refreshedData));
        spyOn(editor, 'updateCallback').andReturn();
        spyOn(editor, '_merge').andReturn({
            a: '1',
            b: '4',
            c: '5'
        });
        spyOn(editor, 'reset').andReturn();
        spyOn(editor, '_displayValidationErrors').andReturn();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, 'isDirty').andReturn(true);

        var componentForm = {
            $valid: true
        };

        var promise = editor.submit(componentForm);

        expect(promise).toBeResolvedWithData({
            a: '1',
            b: '4',
            c: '5',
            someMarker: 'someValue'
        });
        expect(editorCRUDService.save).toHaveBeenCalledWith({
            a: '1',
            b: '4',
            c: '5'
        });

        expect(editor.updateCallback).toHaveBeenCalledWith(editor.pristine, response);
        expect(editor.reset).toHaveBeenCalledWith(componentForm);
        expect(editor.removeValidationErrors).toHaveBeenCalled();
        expect(editor._displayValidationErrors).not.toHaveBeenCalled();
        expect(editor.pristine).toEqual({
            a: '1',
            b: '4',
            c: '5',
            someMarker: 'someValue'
        });
        expect(editor.smarteditComponentId).toBeUndefined();
        expect(editor._merge).toHaveBeenCalledWith(refreshedData, component);
        expect(editor.fetch).toHaveBeenCalled();
        expect(responseHandler).toHaveBeenCalledWith(editor, response);
    });

    it('submit will refresh the non editable fields values from server, call update, set pristine state, calls removeValidationErrors, reset and updateCallback if dirty and form valid', function() {

        var refreshedData = {
            a: '1',
            b: '2',
            c: '5'
        };

        editorCRUDService.update.andReturn($q.when({})); //not listening to response anymore

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });

        var pristine = {
            a: '1',
            b: '2',
            c: '3'
        };

        var component = {
            a: '1',
            b: '4',
            c: '3'
        };

        editor.pristine = pristine;
        editor.component = component;
        spyOn(editor, 'fetch').andReturn($q.when(refreshedData));
        spyOn(editor, 'updateCallback').andReturn();
        spyOn(editor, '_merge').andReturn({
            a: '1',
            b: '4',
            c: '5'
        });
        spyOn(editor, 'reset').andReturn();
        spyOn(editor, '_displayValidationErrors').andReturn();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, 'isDirty').andReturn(true);


        var componentForm = {
            $valid: true
        };
        editor.submit(componentForm);
        //for promises to actually resolve :
        $rootScope.$digest();

        expect(editorCRUDService.update).toHaveBeenCalledWith({
            a: '1',
            b: '4',
            c: '5',
            identifier: 'smarteditComponentId'
        });

        expect(editor.updateCallback).toHaveBeenCalledWith(editor.pristine, {});
        expect(editor.reset).toHaveBeenCalledWith(componentForm);
        expect(editor.removeValidationErrors).toHaveBeenCalled();
        expect(editor._displayValidationErrors).not.toHaveBeenCalled();
        expect(editor.pristine).toEqual({
            a: '1',
            b: '4',
            c: '5'
        });
        expect(editor._merge).toHaveBeenCalledWith(refreshedData, component);
        expect(editor.fetch).toHaveBeenCalled();
        expect(responseHandler).not.toHaveBeenCalled();
    });

    it('successful init will assign editing structure from API, fetch storefront languages and process it and call load ', function() {

        var fields = {
            attributes: [{
                qualifier: 'property1',
                cmsStructureType: 'ShortString'
            }, {
                qualifier: 'id',
                cmsStructureType: 'ShortString'
            }, {
                qualifier: 'type',
                cmsStructureType: 'ShortString'
            }, {
                qualifier: 'activationDate',
                cmsStructureType: 'Date'
            }]
        };

        var modifiedFields = [];

        var deferred = $q.defer();
        deferred.resolve(fields);
        editorStructureService.get.andReturn(deferred.promise);

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback
        });
        var deferred2 = $q.defer();
        deferred2.resolve();
        spyOn(editor, 'load').andReturn(deferred2.promise);

        spyOn(editor, 'fieldAdaptor').andReturn(modifiedFields);

        editor.init().then(function() {
            expect(editor.fields).toEqualData(modifiedFields);
        }, function() {
            expect(editor).fail();
        });

        //for promises to actually resolve :
        $rootScope.$digest();

        expect(editorStructureService.get).toHaveBeenCalledWith({
            smarteditComponentType: 'smarteditComponentType'
        });
        expect(sharedDataService.get).toHaveBeenCalledWith('experience');
        expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('someSiteUid');

        expect(editor.languages).toEqualData(STOREFRONT_LANGUAGES);
        expect(editor.fieldAdaptor).toHaveBeenCalledWith(fields.attributes);
        expect(editor.load).toHaveBeenCalled();

    });


    it('successful init will assign editing structure from local structure and process it and call load ', function() {

        var structure = [{
            qualifier: 'property1',
            cmsStructureType: 'ShortString'
        }, {
            qualifier: 'id',
            cmsStructureType: 'ShortString'
        }, {
            qualifier: 'type',
            cmsStructureType: 'ShortString'
        }, {
            qualifier: 'activationDate',
            cmsStructureType: 'Date'
        }];

        var attributes = {
            attributes: structure
        };

        var tabId = 'testTab';

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structure: structure,
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items',
            updateCallback: updateCallback,
            onStructureResolved: onStructureResolved,
            id: tabId
        });
        var deferred2 = $q.defer();
        deferred2.resolve();
        spyOn(editor, 'load').andReturn(deferred2.promise);

        spyOn(editor, 'fieldAdaptor').andCallThrough();

        spyOn(editor, 'onStructureResolved').andReturn();

        editor.init().then(function() {
            expect(editor.fields).toEqualData(structure);
        }, function() {
            expect(editor).fail();
        });

        //for promises to actually resolve :
        $rootScope.$digest();

        expect(editor.fieldAdaptor).toHaveBeenCalledWith(structure);
        expect(editor.load).toHaveBeenCalled();
        expect(editor.onStructureResolved).toHaveBeenCalledWith(tabId, attributes);
    });


    it('fieldAdaptor will assign postfix text when a field qualifier defines a property', function() {

        var fields = [{
            qualifier: 'media',
            cmsStructureType: 'MediaContainer'
        }];

        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var result = "field can not be editable";
        $translate.instant.andReturn(result);

        var newFields = editor.fieldAdaptor(fields);

        expect(newFields[0].postfixText).toEqualData(result);
        expect($translate.instant).toHaveBeenCalledWith('simpleresponsivebannercomponent.media.postfix.text');
    });


    it('fieldAdaptor wont assign postfix text when a field qualifier does not define a property  ', function() {

        var fields = [{
            qualifier: 'media',
            cmsStructureType: 'MediaContainer'
        }];

        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var key = 'simpleresponsivebannercomponent.media.postfix.text';
        $translate.instant.andReturn(key);

        var newFields = editor.fieldAdaptor(fields);

        expect(newFields[0].postfixText).toEqualData('');
        expect($translate.instant).toHaveBeenCalledWith(key);
    });

    it('_isPrimitive returns true for "Boolean", "ShortString", "LongString", "RichText", "Date" types only', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var isPrim = [];
        isPrim.push(editor._isPrimitive('Boolean'));
        isPrim.push(editor._isPrimitive('ShortString'));
        isPrim.push(editor._isPrimitive('LongString'));
        isPrim.push(editor._isPrimitive('RichText'));
        isPrim.push(editor._isPrimitive('Date'));
        isPrim.push(editor._isPrimitive('AnyNonPrimitiveType'));

        expect(isPrim).toEqual([true, true, true, true, true, false]);

    });

    it('GIVEN that cmsStructureType is not "Enum" and field is initiated, refreshOptions will call fetchMediaDataHandler to fetch list of filtered options', function() {

        var field = {
            qualifier: 'property1',
            cmsStructureType: 'Media',
            initiated: ['property1']
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var component = {
            id: 'someid',
            property1: '1',
            property2: '2'
        };
        editor.component = component;

        editor.refreshOptions(field, 'qualifier', 'searchResult');
        $rootScope.$digest();

        expect(fetchMediaDataHandler.getById).not.toHaveBeenCalled();
        expect(fetchMediaDataHandler.findByMask).toHaveBeenCalledWith(field, 'searchResult');

        expect(field.options).toEqualData({
            property1: [{
                code: 'code1',
                label: 'label1'
            }, {
                code: 'code2',
                label: 'label2'
            }]
        });

    });

    it('GIVEN that cmsStructureType is not "Enum" and field is not initiated and an identifier exists, refreshOptions will call fetchMediaDataHandler to fetch list limited to the matching option from model and set initiated to true', function() {

        var field = {
            qualifier: 'property1',
            cmsStructureType: 'Media'
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var component = {
            id: 'someid',
            property1: '1',
            property2: '2'
        };
        editor.component = component;

        editor.refreshOptions(field, 'qualifier', '');
        $rootScope.$digest();
        expect(fetchMediaDataHandler.getById).toHaveBeenCalledWith(field, '1');
        expect(fetchMediaDataHandler.findByMask).not.toHaveBeenCalled();
        expect(field.options).toEqualData({
            property1: [{
                code: 'code1',
                label: 'label1'
            }]
        });
        expect(field.initiated).toEqual(['property1']);


    });

    it('GIVEN that cmsStructureType is not "Enum" and field is not initiated but an identifier does not exist, refreshOptions will not initialize options and will set initiated to true', function() {

        var field = {
            qualifier: 'property1',
            cmsStructureType: 'Media'
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var component = {};
        editor.component = component;

        editor.refreshOptions(field, 'qualifier', '');
        $rootScope.$digest();
        expect(fetchMediaDataHandler.getById).not.toHaveBeenCalled();
        expect(fetchMediaDataHandler.findByMask).not.toHaveBeenCalled();
        expect(field.options).toEqual({});
        expect(field.initiated).toEqual(['property1']);

    });

    it('GIVEN that cmsStructureType is "Enum", refreshOptions  will call fetchEnumDataHandler to fetchl fetch full list of enums', function() {

        var field = {
            qualifier: 'property1',
            cmsStructureType: 'Enum'
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var component = {};
        editor.component = component;

        editor.refreshOptions(field, 'qualifier', 's');
        $rootScope.$digest();
        expect(fetchEnumDataHandler.getById).not.toHaveBeenCalled();
        expect(fetchEnumDataHandler.findByMask).toHaveBeenCalledWith(field, 's');
        expect(field.options).toEqual({
            property1: [{
                code: 'code1',
                label: 'label1'
            }, {
                code: 'code2',
                label: 'label2'
            }]
        });
        expect(field.initiated).toEqual(['property1']);

    });

    it('fieldAdaptor does not transform the fields if neither external nor urlLink are found', function() {

        var fields = [{
            qualifier: 'property1',
            cmsStructureType: 'ShortString',
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        var newFields = editor.fieldAdaptor(fields);

        expect(newFields).toEqualData(fields);

    });

    it('fieldAdaptor does not transform the fields if urlLink is not found', function() {

        var fields = [{
            qualifier: 'property1',
            cmsStructureType: 'ShortString',
        }, {
            qualifier: 'external',
            cmsStructureType: 'Boolean',
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        var newFields = editor.fieldAdaptor(fields);

        expect(newFields).toEqualData(fields);

    });

    it('fieldAdaptor does not transform the fields if external is not found', function() {

        var fields = [{
            qualifier: 'property1',
            cmsStructureType: 'ShortString',
        }, {
            qualifier: 'urlLink',
            cmsStructureType: 'ShortString',
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        var newFields = editor.fieldAdaptor(fields);

        expect(newFields).toEqualData(fields);

    });

    it('fieldAdaptor removes urlLink and external and adds LinkToggle if both urlLink and external are present', function() {

        var fields = [{
            qualifier: 'property1',
            cmsStructureType: 'ShortString',
        }, {
            qualifier: 'urlLink',
            cmsStructureType: 'ShortString',
            i18nKey: 'thesmarteditComponentType_urlLink'
        }, {
            qualifier: 'external',
            cmsStructureType: 'Boolean',
            i18nKey: 'thesmarteditComponentType_external'
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        var newFields = editor.fieldAdaptor(fields);

        expect(newFields).toEqualData([{
            qualifier: 'property1',
            cmsStructureType: 'ShortString',
            editable: true
        }, {
            cmsStructureType: 'LinkToggle',
            qualifier: 'linkToggle',
            i18nKey: 'editor.linkto.label',
            externalI18nKey: 'editor.linkto.external.label',
            internalI18nKey: 'editor.linkto.internal.label'
        }]);

    });

    it('_displayValidationErrors will add errors messages and localization languages to the field', function() {
        seValidationErrorParser.parse.andCallFake(function(message) {
            return {
                message: message
            };
        });

        var validationErrors = [{
            "message": "This field cannot contain special characters",
            "reason": "missing",
            "subject": "field1",
            "subjectType": "parameter",
            "type": "ValidationError"
        }, {
            "message": "This field is required and must to be between 1 and 255 characters long.",
            "reason": "missing",
            "subject": "field2",
            "subjectType": "parameter",
            "type": "ValidationError"
        }];

        var fields = [{
            qualifier: 'field1'
        }, {
            qualifier: 'field2'
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;
        editor._displayValidationErrors(validationErrors);

        expect(fields[0].errors.length).toEqual(1);
        expect(fields[0].errors.length).toEqual(1);

        expect(fields[0].errors[0].message).toEqual("This field cannot contain special characters");
        expect(fields[0].errors[0].language).toEqual("field1");

        expect(fields[1].errors[0].message).toEqual("This field is required and must to be between 1 and 255 characters long.");
        expect(fields[1].errors[0].language).toEqual("field2");

    });

    it('_displayValidationErrors will add language from validation errors for the language property if the field is localized else will add the qualifier to the language property ', function() {
        seValidationErrorParser.parse.andCallFake(function(message) {
            var error = {};
            if (message === "This field cannot contain special characters. Language: [en]") {
                error.message = 'This field cannot contain special characters.';
                error.language = 'en';
            } else {
                error.message = message;
            }
            return error;
        });


        var validationErrors = [{
            "message": "This field cannot contain special characters. Language: [en]",
            "reason": "missing",
            "subject": "field1",
            "subjectType": "parameter",
            "type": "ValidationError"
        }, {
            "message": "This field is required and must to be between 1 and 255 characters long.",
            "reason": "missing",
            "subject": "field2",
            "subjectType": "parameter",
            "type": "ValidationError"
        }];

        var fields = [{
            qualifier: 'field1',
            localized: true
        }, {
            qualifier: 'field2'
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;
        editor._displayValidationErrors(validationErrors);

        expect(fields[0].errors.length).toEqual(1);
        expect(fields[0].errors.length).toEqual(1);

        expect(fields[0].errors[0].message).toEqual("This field cannot contain special characters.");
        expect(fields[0].errors[0].language).toEqual("en");

        expect(fields[1].errors[0].message).toEqual("This field is required and must to be between 1 and 255 characters long.");
        expect(fields[1].errors[0].language).toEqual("field2");

    });

    it('failed submit will remove existing validation errors and call _displayValidationErrors', function() {

        var failure = {
            "data": {
                "errors": [{
                    "message": "This field cannot contain special characters",
                    "reason": "missing",
                    "subject": "headline",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }, {
                    "message": "This field is required and must to be between 1 and 255 characters long.",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }]
            }
        };
        editorCRUDService.update.andReturn($q.reject(failure));

        var refreshedData = {
            a: '1',
            b: '2',
            c: '5'
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var pristine = {
            a: '0',
            b: '1'
        };

        var component = {
            a: '1',
            b: '2'
        };

        var fields = [{
            qualifier: 'a',
        }, {
            qualifier: 'b'
        }];

        editor.pristine = pristine;
        editor.component = component;
        editor.fields = fields;
        spyOn(editor, 'fetch').andReturn($q.when(refreshedData));
        spyOn(editor, 'updateCallback').andReturn();
        spyOn(editor, 'reset').andCallThrough();
        spyOn(editor, '_displayValidationErrors').andCallThrough();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, '_merge').andReturn({
            someField: 'someFieldvalue'
        });

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);
        componentForm.$dirty = true;
        componentForm.$valid = true;

        editor.submit(componentForm);
        //for promises to actually resolve :
        $rootScope.$digest();

        expect(editor.updateCallback).not.toHaveBeenCalled();
        expect(editor.reset).not.toHaveBeenCalledWith(componentForm);

        expect(editor.removeValidationErrors).toHaveBeenCalledWith();
        expect(editor._displayValidationErrors).toHaveBeenCalledWith(failure.data.errors);
        expect(editor.fetch).toHaveBeenCalled();
    });

    it('GIVEN there are errors caused by an external editor WHEN submit is called THEN the editor must raise a GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT ', function() {
        // Arrange
        var failure = {
            "data": {
                "errors": [{
                    "message": "This field cannot contain special characters",
                    "reason": "missing",
                    "subject": "headline",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }, {
                    "message": "This field is required and must to be between 1 and 255 characters long.",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }]
            }
        };

        var refreshedData = {
            a: '1',
            b: '2',
            c: '5'
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editorCRUDService.update.andReturn($q.reject(failure));
        spyOn(editor, '_merge').andReturn({
            someField: 'someFieldvalue'
        });

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);
        componentForm.$dirty = true;
        componentForm.$valid = true;

        spyOn(editor, '_displayValidationErrors').andCallThrough();
        spyOn(editor, 'removeValidationErrors').andReturn();
        spyOn(editor, 'fetch').andReturn($q.when(refreshedData));

        // Act
        editor.submit(componentForm);
        $rootScope.$digest(); //for promises to actually resolve

        // Assert
        expect(systemEventServ.sendAsynchEvent).toHaveBeenCalledWith("UnrelatedValidationErrors", failure.data.errors);
    });

    it('GIVEN there are errors in one or more fields in the current editor detected externally WHEN GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT handler is called THEN the editor must display those validation errors', function() {
        seValidationErrorParser.parse.andCallFake(function(message) {
            return {
                message: message
            };
        });

        // Arrange
        var failure = {
            "data": {
                "errors": [{
                    "message": "This field cannot contain special characters",
                    "reason": "missing",
                    "subject": "headline",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }, {
                    "message": "This field is required and must to be between 1 and 255 characters long.",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }]
            }
        };

        var fields = [{
            qualifier: 'headline'
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.id = 'some ID';
        editor.fields = fields;

        spyOn(editor, 'isDirty').andReturn(false);
        spyOn(editor, 'removeValidationErrors');
        spyOn(editor, '_displayValidationErrors').andCallThrough();

        // Act
        editor._handleUnrelatedValidationErrors("some Key", failure.data.errors);

        // Assert
        expect(editor.removeValidationErrors).not.toHaveBeenCalled();
        expect(editor._displayValidationErrors).toHaveBeenCalledWith(failure.data.errors);
        expect(systemEventServ.sendAsynchEvent).toHaveBeenCalledWith("EDITOR_IN_ERROR_EVENT", editor.id);
    });


    it('GIVEN there are errors in one or more fields in the current editor detected externally and editor is dirty WHEN GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT handler is called THEN the editor must not display those validation errors', function() {
        // Arrange
        var failure = {
            "data": {
                "errors": [{
                    "message": "This field cannot contain special characters",
                    "reason": "missing",
                    "subject": "headline",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }, {
                    "message": "This field is required and must to be between 1 and 255 characters long.",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }]
            }
        };

        var fields = [{
            qualifier: 'headline'
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.id = 'some ID';
        editor.fields = fields;

        spyOn(editor, 'isDirty').andReturn(true);
        spyOn(editor, 'removeValidationErrors');
        spyOn(editor, '_displayValidationErrors').andCallThrough();

        // Act
        editor._handleUnrelatedValidationErrors("some Key", failure.data.errors);

        // Assert
        expect(editor.removeValidationErrors).not.toHaveBeenCalled();
        expect(editor._displayValidationErrors).not.toHaveBeenCalledWith(failure.data.errors);
        expect(systemEventServ.sendAsynchEvent).not.toHaveBeenCalledWith("EDITOR_IN_ERROR_EVENT", editor.id);
    });

    it('isDirty will sanitize before checking if pristine and component HTML are equal', function() {

        var pristine = {
            a: {
                value: {
                    en: '<h2>search</h2><p>Suggestions</p><ul>	<li>The</li>	<li>The</li>	<li>Test</li></ul>',
                }
            },
            b: '1',
            c: '<h2>search</h2> \n<p>Suggestions</p><ul>\n<li>The</li><li>The</li><li>Test</li></ul>'
        };

        var component = {
            a: {
                value: {
                    en: '<h2>search</h2> \n<p>Suggestions</p><ul>\n<li>The</li><li>The</li><li>Test</li></ul>',
                }
            },
            b: '1',
            c: '<h2>search</h2><p>Suggestions</p><ul>	<li>The</li>	<li>The</li>	<li>Test</li></ul>'
        };

        var fields = [{
            cmsStructureType: 'RichText',
            qualifier: 'a',
            localized: true
        }, {
            qualifier: 'b'
        }, {
            qualifier: 'c',
            cmsStructureType: 'RichText',
            localized: false
        }];

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editor.pristine = pristine;
        editor.component = component;
        editor.fields = fields;

        var result = editor.isDirty();
        expect(result).toEqual(false);

        pristine = {
            a: {
                value: {
                    en: '<h2>test1</h2> <p>test2</p>',
                }
            }
        };

        component = {
            a: {
                value: {
                    en: '<h2>TEST2</h2> \n<p>test1</p>',
                }
            }
        };

        fields = [{
            cmsStructureType: 'RichText',
            qualifier: 'a',
            localized: true
        }];

        editor.pristine = pristine;
        editor.component = component;
        editor.fields = fields;

        result = editor.isDirty();
        expect(result).toEqual(true);

    });

    it('isDirty will return true even for properties that are not fields', function() {

        var pristine = {
            a: '123 ',
            b: '0'
        };

        var component = {
            a: '123',
            b: ''
        };

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        editor.pristine = pristine;
        editor.component = component;

        var result = editor.isDirty();
        expect(result).toEqual(true);
    });

    it('sanitizePayload will remove dangerous characters from a localized ShortString CMS component type when the user saves the form with data in the input', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var payload = {
            headline: {
                type: 'localizedValueMap',
                value: {
                    en: '<h1>Foo bar</h1>h1>'
                }
            }
        };

        var fields = [{
            qualifier: "headline",
            cmsStructureType: "ShortString",
            localized: true
        }];

        payload = editor.sanitizePayload(payload, fields);

        expect(escapeHtml.callCount).toBe(1);
        expect(escapeHtml.argsForCall[0][0]).toBe('<h1>Foo bar</h1>h1>');
        expect(payload).toEqual({
            headline: {
                type: 'localizedValueMap',
                value: {
                    en: 'ESCAPED'
                }
            }
        });

    });


    it('sanitizePayload will not remove dangerous characters from a ShortString CMS component type when the user saves the form with no data in the input', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var payload = {
            id: undefined
        };

        var fields = [{
            qualifier: "id",
            cmsStructureType: "ShortString",
        }];

        payload = editor.sanitizePayload(payload, fields);

        // The function will not be called because the qualifier is undefined
        expect(escapeHtml.callCount).toBe(0);
        expect(payload).toEqual({
            id: undefined
        });

    });

    it('sanitizePayload will remove dangerous characters from a LongString CMS component type when the user saves the form with data in the textarea', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var payload = {
            urlLink: "/pathwithxss/onclick='alert(1)'"
        };

        var fields = [{
            qualifier: "urlLink",
            cmsStructureType: "LongString"
        }];

        payload = editor.sanitizePayload(payload, fields);

        expect(escapeHtml.callCount).toBe(1);
        expect(escapeHtml.argsForCall[0][0]).toBe("/pathwithxss/onclick='alert(1)'");
        expect(payload).toEqual({
            urlLink: "ESCAPED"
        });

    });

    it('sanitizePayload will not remove dangerous characters from a LongString CMS component type when the user saves the form with no data in the textarea', function() {

        var editor = new GenericEditor({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId,
            updateCallback: updateCallback,
            structure: 'structure',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });

        var payload = {
            metaDescription: undefined
        };

        var fields = [{
            qualifier: 'metaDescription',
            cmsStructureType: 'LongString'
        }];

        payload = editor.sanitizePayload(payload, fields);

        // The function will not be called because the qualifier is undefined
        expect(escapeHtml.callCount).toBe(0);
        expect(payload).toEqual({
            metaDescription: undefined
        });

    });

    it('_fieldsAreUserChecked WILL fail validation WHEN a required checkbox field is not checked', function() {

        var fields = [{
            qualifier: 'content',
            cmsStructureType: 'Paragraph',
            requiresUserCheck: {
                content: true
            },
            isUserChecked: false
        }];
        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;
        var valid = editor._fieldsAreUserChecked();
        expect(valid).toEqual(false);

    });

    it('_fieldsAreUserChecked WILL pass validation WHEN not required checkbox field is not checked', function() {
        var fields = [{
            qualifier: 'content',
            cmsStructureType: 'Paragraph',
            requiresUserCheck: {
                content: true
            },
            isUserChecked: true
        }];
        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;
        var valid = editor._fieldsAreUserChecked();
        expect(valid).toEqual(true);
    });


    it('submit WILL fail validation WHEN submit a not checked required checkbox field', function() {
        var fields = [{
            qualifier: 'content',
            cmsStructureType: 'Paragraph',
            requiresUserCheck: {
                content: true
            },
            isUserChecked: false
        }];
        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;
        editor.submit();

        expect(editor.hasFrontEndValidationErrors).toEqual(true);
    });

    it('submit WILL pass validation WHEN submit a checked required checkbox field', function() {
        var fields = [{
            qualifier: 'content',
            cmsStructureType: 'Paragraph',
            requiresUserCheck: {
                content: true
            },
            isUserChecked: true
        }];
        var componentType = "simpleResponsiveBannerComponent";
        var editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId: smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi: '/cmswebservices/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items'
        });
        editor.fields = fields;

        var componentForm = jasmine.createSpyObj('componentForm', ['$setPristine']);
        spyOn(editor, 'isDirty').andReturn(false);

        componentForm.$valid = true;

        editor.submit(componentForm);
        expect(editor.hasFrontEndValidationErrors).toEqual(false);
    });

});
