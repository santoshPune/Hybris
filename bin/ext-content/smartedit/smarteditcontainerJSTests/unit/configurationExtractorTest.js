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
describe('configurationExtractorService', function() {

    var configurationExtractorService;

    beforeEach(module('configurationExtractorServiceModule'));

    beforeEach(inject(function(_configurationExtractorService_) {
        configurationExtractorService = _configurationExtractorService_;
    }));

    describe('extractSEContainerModules', function() {
        var CONFIGURATION = {
            'smarteditroot': 'smarteditroot1',
            'domain': 'domain1',
            'authenticationMap': {
                'someKey': 'someValue'
            },
            'applications.cmssmarteditValidContainer': {
                "smartEditContainerLocation": "/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js"
            },
            'applications.cmssmarteditValidContainerFullPathHttps': {
                "smartEditContainerLocation": "https://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js"
            },
            'applications.cmssmarteditValidContainerFullPathHttp': {
                "smartEditContainerLocation": "http://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js"
            },
            'applications.cmssmarteditInvalidContainerNumberAsValue': 3,
            'applications.cmssmarteditInvalidContainerStringAsValue': "Hello",
            'applications.cmssmarteditInvalidContainerArrayAsValue': ["Hello"],
            'applications.cmssmarteditInvalidContainerObjectMissingRequiredKey': {}
        };
        var result;
        beforeEach(function() {
            result = configurationExtractorService.extractSEContainerModules(CONFIGURATION);
        });

        it('should return an object with applications, applicaiton locations, and an authentication map', function() {
            expect(result.applications).toBeDefined();
            expect(result.appLocations).toBeDefined();
            expect(result.authenticationMap).toBeDefined();
        });

        it('should keep the authentication map on the returned object as is', function() {
            expect(result.authenticationMap).toEqual({
                'someKey': 'someValue'
            });
        });

        it('should add the administration module to applications and app locations', function() {
            expect(result.applications).toContain('administration');
            expect(result.appLocations).toContain('smarteditroot1/static-resources/smarteditcontainer/modules/administrationModule.js');
        });

        it('should filter out applications that define a number as the value instead of an object specifying the location of the container script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidContainerNumberAsValue');
        });

        it('should filter out applications that define a string as the value instead of an object specifying the location of the container script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidContainerStringAsValue');
        });

        it('should filter out applications that define a array as the value instead of an object specifying the location of the container script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidContainerArrayAsValue');
        });

        it('should filter out applications that define an object as the value but the object is missing the smartedit container location key', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidContainerObjectMissingRequiredKey');
        });

        it('should accept a smartedit container application location and append the domain onto the location if the location is a subpath', function() {
            expect(result.applications).toContain('cmssmarteditValidContainer');
            expect(result.appLocations).toContain('domain1/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js');
        });

        it('should accept a smartedit container application location as is if it is a full path with the https protocol', function() {
            expect(result.applications).toContain('cmssmarteditValidContainerFullPathHttps');
            expect(result.appLocations).toContain('https://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js');
        });

        it('should accept a smartedit container application location as is if it is a full path with the http protocol', function() {
            expect(result.applications).toContain('cmssmarteditValidContainerFullPathHttp');
            expect(result.appLocations).toContain('http://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js');
        });
    });

    describe('extractSEModules', function() {
        var CONFIGURATION = {
            'smarteditroot': 'smarteditroot1',
            'domain': 'domain1',
            'authenticationMap': {
                'someKey': 'someValue'
            },
            'applications.cmssmarteditValid': {
                "smartEditLocation": "/cmssmartedit/cmssmartedit/js/cmssmartedit.js"
            },
            'applications.cmssmarteditValidFullPathHttps': {
                "smartEditLocation": "https://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js"
            },
            'applications.cmssmarteditValidFullPathHttp': {
                "smartEditLocation": "http://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js"
            },
            'applications.cmssmarteditInvalidNumberAsValue': 3,
            'applications.cmssmarteditInvalidStringAsValue': "Hello",
            'applications.cmssmarteditInvalidArrayAsValue': ["Hello"],
            'applications.cmssmarteditInvalidObjectMissingRequiredKey': {}
        };
        var result;
        beforeEach(function() {
            result = configurationExtractorService.extractSEModules(CONFIGURATION);
        });

        it('should return an object with applications, applicaiton locations, and an authentication map', function() {
            expect(result.applications).toBeDefined();
            expect(result.appLocations).toBeDefined();
            expect(result.authenticationMap).toBeDefined();
        });

        it('should keep the authentication map on the returned object as is', function() {
            expect(result.authenticationMap).toEqual({
                'someKey': 'someValue'
            });
        });

        it('should add the administration module to applications and app locations', function() {
            expect(result.applications).toContain('systemModule');
            expect(result.appLocations).toContain('smarteditroot1/static-resources/smartedit/modules/systemModule.js');
        });

        it('should filter out applications that define a number as the value instead of an object specifying the location of the  script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidNumberAsValue');
        });

        it('should filter out applications that define a string as the value instead of an object specifying the location of the  script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidStringAsValue');
        });

        it('should filter out applications that define a array as the value instead of an object specifying the location of the  script', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidArrayAsValue');
        });

        it('should filter out applications that define an object as the value but the object is missing the smartedit  location key', function() {
            expect(result.applications).not.toContain('cmssmarteditInvalidObjectMissingRequiredKey');
        });

        it('should accept a smartedit  application location and append the domain onto the location if the location is a subpath', function() {
            expect(result.applications).toContain('cmssmarteditValid');
            expect(result.appLocations).toContain('domain1/cmssmartedit/cmssmartedit/js/cmssmartedit.js');
        });

        it('should accept a smartedit  application location as is if it is a full path with the https protocol', function() {
            expect(result.applications).toContain('cmssmarteditValidFullPathHttps');
            expect(result.appLocations).toContain('https://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js');
        });

        it('should accept a smartedit  application location as is if it is a full path with the http protocol', function() {
            expect(result.applications).toContain('cmssmarteditValidFullPathHttp');
            expect(result.appLocations).toContain('http://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js');
        });
    });
});
