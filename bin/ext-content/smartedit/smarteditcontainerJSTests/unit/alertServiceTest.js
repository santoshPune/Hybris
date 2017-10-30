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
describe('test service alertService', function() {

    var $q, $rootScope, $httpBackend;

    beforeEach(customMatchers);
    beforeEach(module('alertServiceModule'));

    beforeEach(inject(function(_$q_, _$rootScope_, _$httpBackend_, _alertService_) {

        $q = _$q_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
        alertService = _alertService_;
        jasmine.Clock.useMock();

    }));

    it('alertService.pushAlert will trigger a digest cycle by calling $apply and bind alerts to $rootScope for 3000ms if digest cycle is not yet started', function() {

        spyOn($rootScope, '$apply').andCallThrough();
        spyOn($rootScope, '$digest').andCallThrough();

        alertService.pushAlerts([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(2000);
        expect($rootScope.alerts).toEqualData([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(1000);
        expect($rootScope.alerts).toBeUndefined();

        expect($rootScope.$apply).toHaveBeenCalled();
        expect($rootScope.$digest).toHaveBeenCalled();

    });

    it('alertService.pushAlert will not trigger a digest cycle and bind alerts to $rootScope for 3000ms if digest cycle has already started', function() {

        spyOn($rootScope, '$$phase').andReturn(true);
        spyOn($rootScope, '$apply').andCallThrough();
        spyOn($rootScope, '$digest').andCallThrough();

        alertService.pushAlerts([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        expect($rootScope.alerts).toEqualData([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(3000);
        expect($rootScope.alerts).toBeUndefined();

        expect($rootScope.$apply).not.toHaveBeenCalled();
        expect($rootScope.$digest).toHaveBeenCalled();

    });

    it('GIVEN there are multiple alerts WHEN the removeAlertById method is called THEN the service will filter out the matched alert', function() {
        // Arrange
        $rootScope.alerts = [{
            id: 'someAlert1'
        }, {
            id: 'someAlert2'
        }, {
            id: 'someAlert3'
        }];
        spyOn(alertService, 'removeAlertById').andCallThrough();

        // Act
        alertService.removeAlertById('someAlert2');

        // Assert
        expect($rootScope.alerts.length).toBe(2);
        expect($rootScope.alerts[0].id).toBe('someAlert1');
        expect($rootScope.alerts[1].id).toBe('someAlert3');
    });

    it('GIVEN there is one alert in the service WHEN the removeAlertById method is called to remove that alert THEN the service will set the alerts array empty', function() {
        $rootScope.alerts = [{
            id: 'someAlert1'
        }];
        spyOn(alertService, 'removeAlertById').andCallThrough();

        // Act
        alertService.removeAlertById('someAlert1');

        // Assert
        expect($rootScope.alerts).not.toBeNull();
        expect($rootScope.alerts.length).toBe(0);
    });

    it('GIVEN there is no alert in the service WHEN the removeAlertById method is called THEN the service will set the alerts array empty', function() {
        $rootScope.alerts = [];
        spyOn(alertService, 'removeAlertById').andCallThrough();

        // Act
        alertService.removeAlertById('someAlert1');

        // Assert
        expect($rootScope.alerts).not.toBeNull();
        expect($rootScope.alerts.length).toBe(0);
    });

});
