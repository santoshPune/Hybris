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
describe('test bootstrap Service class', function() {

    var bootstrapService, configurationExtractorService, sharedDataService, injectJS, frame, $httpBackend, $log;
    var configurations = {
        smarteditroot: 'smarteditroot1',
        domain: 'domain1',
        applications: 'applications1',
        'authentication.credentials': {
            key2: 'value2'
        }

    };

    beforeEach(customMatchers);
    beforeEach(module('bootstrapServiceModule'));

    beforeEach(module('configurationExtractorServiceModule', function($provide) {

        frame = jasmine.createSpyObj('frame', ['postMessage']);


        configurationExtractorService = jasmine.createSpyObj('configurationExtractorService', ['extractSEContainerModules', 'extractSEModules']);
        configurationExtractorService.extractSEContainerModules.andReturn({
            applications: ['AppA', 'AppB', 'AppC'],
            appLocations: ['SEContainerLocationForAppA', 'SEContainerLocationForAppB', 'SEContainerLocationForAppC'],
            authenticationMap: {
                key1: 'value1'
            }
        });

        configurationExtractorService.extractSEModules.andReturn({
            applications: ['AppX', 'AppY', 'AppZ'],
            appLocations: ['SELocationForAppX', 'SELocationForAppY', 'SELocationForAppZ'],
            authenticationMap: {
                key1: 'value1'
            }
        });

        $provide.value('configurationExtractorService', configurationExtractorService);
        injectJS = jasmine.createSpyObj('injectJS', ['execute']);
        $provide.value('injectJS', injectJS);

    }));

    beforeEach(inject(function(_bootstrapService_, _sharedDataService_, _configurationExtractorService_, _$q_, _$httpBackend_, _$log_) {

        $log = _$log_;
        bootstrapService = _bootstrapService_;
        sharedDataService = _sharedDataService_;
        spyOn(bootstrapService, 'bootstrapSmartEditContainer').andReturn();
        spyOn(sharedDataService, 'set').andReturn();
        spyOn($log, 'debug');
        spyOn($log, 'error');
        configurationExtractorService = _configurationExtractorService_;
        //for mocking application module call
        $httpBackend = _$httpBackend_;
        $httpBackend.whenGET('SELocationForAppX').respond(200);
        $httpBackend.whenGET('SELocationForAppY').respond(200);
        $httpBackend.whenGET('SELocationForAppZ').respond(404);



    }));

    it('addDependencyToSmartEditContainer will attach module as a SmartEdit container dependency if it exists in angular', function() {
        // GIVEN
        angular.module('ExistentApp', []);
        bootstrapService.addDependencyToSmartEditContainer('ExistentApp');
        expect($log.debug).toHaveBeenCalled();

        expect(angular.module('smarteditcontainer').requires).toContain('ExistentApp');
    });

    it('addDependencyToSmartEditContainer will log an error if the module does not exist in angular', function() {
        // WHEN
        bootstrapService.addDependencyToSmartEditContainer('NonExistentApp');
        expect($log.error).toHaveBeenCalled();
        expect(angular.module('smarteditcontainer').requires).not.toContain('NonExistentApp');
    });

    it('calling bootstrapContainerModules will invoke extractSEContainerModules and inject the javascript sources,' +
        ' push the modules to smarteditcontainer module and re-bootstrap smarteditcontainer',
        function() {
            spyOn(bootstrapService, 'addDependencyToSmartEditContainer').andReturn();

            bootstrapService.bootstrapContainerModules(configurations);

            expect(injectJS.execute).toHaveBeenCalledWith(jasmine.objectContaining({
                srcs: ['SEContainerLocationForAppA', 'SEContainerLocationForAppB', 'SEContainerLocationForAppC']
            }));

            expect(Object.keys(injectJS.execute.calls[0].args[0]).length).toBe(2);

            var callback = injectJS.execute.calls[0].args[0].callback;

            expect(bootstrapService.addDependencyToSmartEditContainer).not.toHaveBeenCalled();
            expect(bootstrapService.bootstrapSmartEditContainer).not.toHaveBeenCalled();

            callback();

            expect(bootstrapService.addDependencyToSmartEditContainer).toHaveBeenCalledWith('AppA');
            expect(bootstrapService.addDependencyToSmartEditContainer).toHaveBeenCalledWith('AppB');
            expect(bootstrapService.bootstrapSmartEditContainer).toHaveBeenCalled();

            expect(sharedDataService.set).toHaveBeenCalledWith('authenticationMap', {
                key1: 'value1'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith('credentialsMap', {
                key2: 'value2'
            });

        });


    it('calling bootstrapSEApp will invoke extractSEModules and inject the javascript sources by means of postMessage and if the module fails to load it will not be injected as module AppZ fails because of URL not found',
        function() {

            spyOn(bootstrapService, '_getIframe').andReturn(frame);
            jasmine.Clock.useMock();

            bootstrapService.bootstrapSEApp(configurations);
            jasmine.Clock.tick(1000);
            $httpBackend.flush();
            expect(configurationExtractorService.extractSEModules).toHaveBeenCalledWith(configurations);
            expect(frame.postMessage).toHaveBeenCalledWith({
                eventName: 'smarteditBootstrap',
                resources: {
                    properties: {
                        domain: 'domain1',
                        smarteditroot: 'smarteditroot1',
                        applications: ['AppX', 'AppY'],
                    },
                    js: ['smarteditroot1/static-resources/dist/smartedit/js/presmartedit.js', 'SELocationForAppX', 'SELocationForAppY', 'smarteditroot1/static-resources/dist/smartedit/js/postsmartedit.js'],
                    css: ['smarteditroot1/static-resources/dist/smartedit/css/inner-styling.css']
                }
            }, '*');


            expect(sharedDataService.set).toHaveBeenCalledWith('authenticationMap', {
                key1: 'value1'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith('credentialsMap', {
                key2: 'value2'
            });
        });

});
