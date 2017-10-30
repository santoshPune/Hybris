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
 *
 *
 */
var setupMockModules = function(container) {

    beforeEach(customMatchers);

    beforeEach(module("ngMock"));

    angular.module('gatewayFactoryModule', []);
    angular.module('gatewayProxyModule', []);
    angular.module('toolbarModule', []);
    angular.module('alertServiceModule', []);
    angular.module('sharedDataServiceModule', []);
    angular.module('loadConfigModule', []);
    angular.module('iframeClickDetectionServiceModule', []);
    angular.module('restServiceFactoryModule', []);
    angular.module('functionsModule', []);
    angular.module('contextualMenuServiceModule', []);
    angular.module('decoratorServiceModule', []);
    angular.module('modalServiceModule', []);
    angular.module('genericEditorModule', []);
    angular.module('iFrameManagerModule', []);
    angular.module('editorModalServiceModule', []);
    angular.module('confirmationModalServiceModule', []);
    angular.module('coretemplates', []);
    angular.module('perspectiveServiceModule', []);
    angular.module('featureServiceModule', []);
    angular.module('eventServiceModule', []);

    beforeEach(module('gatewayFactoryModule', function($provide) {
        container.gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', container.gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        container.gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', container.gatewayProxy);
    }));

    beforeEach(module('toolbarModule', function($provide) {
        container.toolbarServiceFactory = jasmine.createSpyObj('toolbarServiceFactory', ['getToolbarService']);
        container.experienceSelectorToolbarService = jasmine.createSpyObj('experienceSelectorToolbarService', ['getAliases', 'addItems']);
        container.experienceSelectorToolbarService.getAliases.andReturn([]);
        container.toolbarServiceFactory.getToolbarService.andReturn(container.experienceSelectorToolbarService);
        $provide.value('toolbarServiceFactory', container.toolbarServiceFactory);
    }));

    beforeEach(module('alertServiceModule', function($provide) {
        container.alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', container.alertService);
    }));

    beforeEach(module('sharedDataServiceModule', function($provide) {
        container.sharedDataService = jasmine.createSpyObj('sharedDataService', ['put', 'get']);
        $provide.value('sharedDataService', container.sharedDataService);
    }));

    beforeEach(module('loadConfigModule', function($provide) {
        container.loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', ['loadAsObject']);
        $provide.value('loadConfigManagerService', container.loadConfigManagerService);
    }));

    beforeEach(module('iframeClickDetectionServiceModule', function($provide) {
        container.iframeClickDetectionService = jasmine.createSpyObj('iframeClickDetectionService', ['click']);
        $provide.value('iframeClickDetectionService', container.iframeClickDetectionService);
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        container.restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        $provide.value('restServiceFactory', container.restServiceFactory);
    }));

    beforeEach(module('contextualMenuServiceModule', function($provide) {
        container.contextualMenuService = jasmine.createSpyObj('contextualMenuService', ['addItems']);
        $provide.value('contextualMenuService', container.contextualMenuService);
    }));

    beforeEach(module('decoratorServiceModule', function($provide) {
        container.decoratorService = jasmine.createSpyObj('decoratorService', ['addMappings']);
        $provide.value('decoratorService', container.decoratorService);
    }));

    beforeEach(module('modalServiceModule', function($provide) {
        container.modalService = jasmine.createSpyObj('modalService', ['addMappings', 'open']);
        $provide.value('modalService', container.modalService);
        $provide.constant('MODAL_BUTTON_ACTIONS', {
            NONE: "none",
            CLOSE: "close",
            DISMISS: "dismiss"
        });
        $provide.constant('MODAL_BUTTON_STYLES', {
            DEFAULT: "default",
            PRIMARY: "primary",
            SECONDARY: "default"
        });
    }));

    beforeEach(module('genericEditorModule', function($provide) {
        container.genericEditor = jasmine.createSpyObj('GenericEditor', ['addMappings']);
        $provide.value('GenericEditor', container.genericEditor);
    }));

    beforeEach(module('iFrameManagerModule', function($provide) {
        container.iFrameManager = jasmine.createSpyObj('iFrameManager', ['loadPreview']);
        $provide.value('iFrameManager', container.iFrameManager);
    }));

    beforeEach(module('editorModalServiceModule', function($provide) {
        container.editorModalService = jasmine.createSpyObj('editorModalService', ['open']);
        $provide.value('editorModalService', container.editorModalService);
    }));

    beforeEach(module('confirmationModalServiceModule', function($provide) {
        container.confirmationModalService = jasmine.createSpyObj('confirmationModalService', ['confirm']);
        $provide.value('confirmationModalService', container.confirmationModalService);
    }));

    beforeEach(module('perspectiveServiceModule', function($provide) {
        container.perspectiveService = jasmine.createSpyObj('perspectiveService', ['register']);
        $provide.value('perspectiveService', container.perspectiveService);
    }));

    beforeEach(module('featureServiceModule', function($provide) {
        container.featureService = jasmine.createSpyObj('featureService', ['register', 'addToolbarItem', 'addDecorator', 'addContextualMenuButton']);
        $provide.value('featureService', container.featureService);
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        container.systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler']);
        $provide.value('systemEventService', container.systemEventService);
    }));

};
