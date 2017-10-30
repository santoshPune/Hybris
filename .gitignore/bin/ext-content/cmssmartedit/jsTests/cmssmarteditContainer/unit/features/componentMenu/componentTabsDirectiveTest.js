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
describe('ComponentTabs directive ', function() {

    var $compile, $rootScope, $httpBackend;

    var element, directiveScope, response, componentServiceModule, componentService, systemEventService, cmsDragAndDropService;

    beforeEach(function() {
        angular.module('translationServiceModule', []);
        angular.module('editorModalServiceModule', []);
        angular.module('eventServiceModule', []);
        angular.module('restServiceFactoryModule', []);
        angular.module('renderServiceModule', []);
    });

    beforeEach(module('ui.bootstrap'));
    beforeEach(module('pascalprecht.translate'));

    beforeEach(module('componentMenuModule', function($provide) {
        componentService = jasmine.createSpyObj('ComponentService', ['loadComponentTypes']);
        componentService.loadComponentTypes.andCallFake(function(component) {
            return {
                then: function() {
                    return component;
                }
            };
        });

        $provide.value('ComponentService', componentService);

        cmsDragAndDropService = jasmine.createSpyObj('cmsDragAndDropService', ['update']);
        $provide.value('cmsDragAndDropService', cmsDragAndDropService);

        $provide.value('domain', 'thedomain');
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['registerEventHandler']);
        $provide.value('systemEventService', systemEventService);
    }));

    beforeEach(customMatchers);
    beforeEach(setupDirectiveTest);
    beforeEach(inject(function(_$rootScope_, _$compile_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $compile = _$compile_;
        $httpBackend = _$httpBackend_;

        setupBackendResponse($httpBackend, '/cmswebservices/types');
        element = templateSetup('<div><component-tabs></component-tabs></div>', $compile, $rootScope);
    }));

    it('should initialize directive scope and template elements', function() {
        var tabs = $(element).find('li');
        expect(tabs.length).toEqual(2);

        var firstTab = $(element).find('li[heading*="types"]');
        expect(firstTab.attr('heading')).toBe('compomentmenu.tabs.componenttypes');
        expect(firstTab.attr('disable')).toBe('false');

        var secondTab = $(element).find('li[heading*="custom"]');
        expect(secondTab.attr('heading')).toBe('compomentmenu.tabs.customizedcomp');
        expect(secondTab.attr('disable')).toBe('false');
    });

    xit('should register event handlers', function() {
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith('ySEComponentMenuOpen', jasmine.any(Function));
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith('ySEComponentMenuClose', jasmine.any(Function));
    });

});
