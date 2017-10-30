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
describe('unit test interceptor service', function() {

    var $q, $httpProvider, alertService, errorInterceptor, alerts, response, $httpBackend, authenticationService, storageService;

    beforeEach(customMatchers);
    beforeEach(module('httpErrorInterceptorModule', function($provide, _$httpProvider_) {

        $httpProvider = _$httpProvider_;
        authenticationService = jasmine.createSpyObj("authenticationService", ["reAuthInProgress", "isAuthEntryPoint", "filterEntryPoints", "authenticate"]);
        authenticationService.isAuthEntryPoint.andReturn(true);

        $provide.value("authenticationService", authenticationService);

        storageService = jasmine.createSpyObj("storageService", ["getAuthToken"]);
        $provide.value("storageService", storageService);

        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', alertService);

    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _$httpBackend_, _httpErrorInterceptor_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
        errorInterceptor = _httpErrorInterceptor_;
        jasmine.Clock.useMock();
    }));

    it('$httpProvider will be loaded with only one interceptor and that will be the errorInterceptor', function() {

        expect($httpProvider.interceptors).toContain('httpErrorInterceptor');

    });

    it('removeErrorsByType will remove all validation errors and return a array of non validation errors only', function() {

        var response = {
            status: 400,
            config: {
                method: 'GET',
                url: 'request1'
            },
            errors: [{
                "message": "This field cannot contain special characters",
                "reason": "missing",
                "subject": "headline",
                "subjectType": "parameter",
                "type": "ValidationError"
            }, {
                "message": "This is a non validation error",
                "reason": "missing",
                "subject": "content",
                "subjectType": "parameter",
                "type": "NonValidationError"
            }, {
                "message": "This field is required and must to be between 1 and 255 characters long.",
                "reason": "missing",
                "subject": "content",
                "subjectType": "parameter",
                "type": "ValidationError"
            }],
            data: {}
        };

        var remainingErrors = errorInterceptor.removeErrorsByType("ValidationError", response.errors);

        expect(remainingErrors).toEqualData([{
            message: 'This is a non validation error',
            reason: 'missing',
            subject: 'content',
            subjectType: 'parameter',
            type: 'NonValidationError'
        }]);

    });

    it('convertErrorToTimedAlertObject will return an object containing the response message', function() {

        var nonValidationError = {
            "message": "This is a non validation error",
            "reason": "missing",
            "subject": "content",
            "subjectType": "parameter",
            "type": "NonValidationError"
        };

        var timedAlertObject = errorInterceptor.convertErrorToTimedAlertObject(nonValidationError);

        expect(timedAlertObject).toEqualData({
            successful: false,
            message: "This is a non validation error",
            closeable: true
        });

    });

    it('when the response is a 400, will remove all validation errors and display alerts for non validation errors', function() {

        var response = {
            status: 400,
            config: {
                method: 'GET',
                url: 'request1'
            },
            data: {
                errors: [{
                    "message": "This field cannot contain special characters",
                    "reason": "missing",
                    "subject": "headline",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }, {
                    "message": "This is the first validation error",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "NonValidationError"
                }, {
                    "message": "This is the second validation error",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "NonValidationError"
                }, {
                    "message": "This field is required and must to be between 1 and 255 characters long.",
                    "reason": "missing",
                    "subject": "content",
                    "subjectType": "parameter",
                    "type": "ValidationError"
                }]
            }
        };

        spyOn(errorInterceptor, 'removeErrorsByType').andCallThrough();
        spyOn(errorInterceptor, 'convertErrorToTimedAlertObject').andCallThrough();
        errorInterceptor.responseError(response);

        expect(errorInterceptor.removeErrorsByType).toHaveBeenCalledWith('ValidationError', response.data.errors);
        expect(errorInterceptor.convertErrorToTimedAlertObject.calls.length).toBe(2);
        expect(errorInterceptor.convertErrorToTimedAlertObject.calls[0].args).toEqualData([{
            message: 'This is the first validation error',
            reason: 'missing',
            subject: 'content',
            subjectType: 'parameter',
            type: 'NonValidationError'
        }]);
        expect(errorInterceptor.convertErrorToTimedAlertObject.calls[1].args).toEqualData([{
            message: 'This is the second validation error',
            reason: 'missing',
            subject: 'content',
            subjectType: 'parameter',
            type: 'NonValidationError'
        }]);

        expect(alertService.pushAlerts).toHaveBeenCalledWith([{
            successful: false,
            message: 'This is the first validation error',
            closeable: true
        }, {
            successful: false,
            message: 'This is the second validation error',
            closeable: true
        }]);

    });

    it('will not display any alert message upon receiving a 401 error', function() {

        response = {
            status: 401,
            config: {
                url: 'url'
            }
        };

        errorInterceptor.responseError(response);
        expect(alertService.pushAlerts).not.toHaveBeenCalled();

    });

    it('a GET AJAX request with a 404 response code and text/html as content-type will be considered as failing HTML', function() {


        // Empty response
        var response1 = {
            config: {}
        };

        // Response with wrong HTTP method
        var response2 = {
            status: 404,
            config: {
                method: 'POST'
            },
            headers: function(header) {
                if (header === 'Content-type') {
                    return 'text/html';
                }
            }
        };

        // Response with wrong status code
        var response3 = {
            status: 401,
            config: {
                method: 'GET'
            },
            headers: function(header) {
                if (header === 'Content-type') {
                    return 'text/html';
                }
            }
        };

        // Response with wrong content-type header
        var response4 = {
            status: 401,
            config: {
                method: 'GET'
            },
            headers: function(header) {
                if (header === 'Content-type') {
                    return 'text/json';
                }
            }
        };

        // Response with failing HTML
        var response5 = {
            status: 404,
            config: {
                method: 'GET'
            },
            headers: function(header) {
                if (header === 'Content-type') {
                    return 'text/html';
                }
            }
        };

        spyOn(errorInterceptor, '_failingHTML').andCallThrough();

        expect(errorInterceptor._failingHTML(response1)).not.toBeTruthy();
        expect(errorInterceptor._failingHTML(response2)).not.toBeTruthy();
        expect(errorInterceptor._failingHTML(response3)).not.toBeTruthy();
        expect(errorInterceptor._failingHTML(response4)).not.toBeTruthy();
        expect(errorInterceptor._failingHTML(response5)).toBeTruthy();

    });

});
