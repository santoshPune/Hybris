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
describe('test Event Service Module', function() {

    var synchEventService, handlerHolderMock, $q, $rootScope;
    var id = "myId";
    var synchData = "myData_synch";
    var asynchData = "myData_asynch";
    var data = "myData";
    var deferred, deferred2;

    beforeEach(customMatchers);

    beforeEach(module('eventServiceModule'));

    beforeEach(inject(function(_$rootScope_, _$q_, EventService, EVENT_SERVICE_MODE_SYNCH) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        synchEventService = new EventService(EVENT_SERVICE_MODE_SYNCH);
    }));

    beforeEach(function() {
        deferred = $q.defer();
        deferred2 = $q.defer();
        deferred3 = $q.defer();

        handlerHolderMock = {
            handler: function(someId, someData) {},
            handler2: function(someId, someData) {},
            handler3: function(someId, someData) {},
        };
        spyOn(handlerHolderMock, 'handler').andReturn(deferred.promise);
        spyOn(handlerHolderMock, 'handler2').andReturn(deferred2.promise);
        spyOn(handlerHolderMock, 'handler3').andReturn(deferred3.promise);
    });
    it('Will use the synchronous mode given EVENT_SERVICE_MODE_SYNCH constructor argument', function() {

        var synchES;
        inject(function(EventService, EVENT_SERVICE_MODE_SYNCH) {
            synchES = new EventService(EVENT_SERVICE_MODE_SYNCH);
        });

        synchES.registerEventHandler(id, handlerHolderMock.handler);

        // --- SYNCH ---
        synchES.sendEvent(id, synchData);
        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, synchData);

    });

    // ----------------------------------------------------------
    // All the rest of the tests are using the Synchronized mode
    // ----------------------------------------------------------

    it('Will call my event handler with correct id and data', function() {

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.sendSynchEvent(id, data);
        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
    });

    it('When first handler out of 2 is successfull Will call both my event handlers registered to the same eventId - with correct id and data - and reolves the promise chain to the resolved data of the last subscriber', function() {

        deferred.resolve("firstValue");
        deferred2.resolve("secondValue");

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.registerEventHandler(id, handlerHolderMock.handler2);
        synchEventService.sendSynchEvent(id, data).then(function(resolvedData) {
            expect(resolvedData).toBe("secondValue");
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);

    });

    it('When more than 2 handlers and all are successful , resolves the promise chain to the resolved data of the last subscriber', function() {

        deferred.resolve("firstValue");
        deferred2.resolve("secondValue");
        deferred3.resolve("thirdValue");

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.registerEventHandler(id, handlerHolderMock.handler2);
        synchEventService.registerEventHandler(id, handlerHolderMock.handler3);

        synchEventService.sendSynchEvent(id, data).then(function(resolvedData) {
            expect(resolvedData).toBe("thirdValue");
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler3).toHaveBeenCalledWith(id, data);

    });


    it('When first handler is not successful Will not call second event handler and send method promise rejects', function() {

        deferred.reject();
        deferred2.resolve();

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.registerEventHandler(id, handlerHolderMock.handler2);
        synchEventService.sendSynchEvent(id, data).then(function() {
            expect().fail();
        }, function() {

        });
        $rootScope.$digest();

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).not.toHaveBeenCalled();

    });

    it('When second handler is not successful Will send method promise rejects', function() {

        deferred.resolve();
        deferred2.reject();

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.registerEventHandler(id, handlerHolderMock.handler2);
        synchEventService.sendSynchEvent(id, data).then(function() {
            expect().fail();
        }, function() {

        });
        $rootScope.$digest();

        expect(handlerHolderMock.handler).toHaveBeenCalledWith(id, data);
        expect(handlerHolderMock.handler2).toHaveBeenCalledWith(id, data);

    });


    it('Will NOT call my handler after it has been unRegistered for the specific eventId and send method promise resolves', function() {

        synchEventService.registerEventHandler(id, handlerHolderMock.handler);
        synchEventService.unRegisterEventHandler(id, handlerHolderMock.handler); // <<------ Main test
        synchEventService.sendSynchEvent(id, data);
        synchEventService.sendSynchEvent(id, data).then(function() {}, function() {
            expect().fail();
        });
        $rootScope.$digest();
        expect(handlerHolderMock.handler).not.toHaveBeenCalled();
    });

});
