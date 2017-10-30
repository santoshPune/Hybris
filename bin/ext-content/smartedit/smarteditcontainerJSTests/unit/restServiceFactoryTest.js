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
describe('restServiceFactory', function() {

    var $rootScope, $q, $resource, restServiceFactory;

    beforeEach(customMatchers);

    beforeEach(module('ngResource', function($provide) {

        service = jasmine.createSpyObj('service', ['save', 'update', 'remove', 'get', 'getById', 'query', 'page']);
        $resource = jasmine.createSpy('$resource');
        $resource.andCallFake(function(api, params, methods) {

            if (api && api.indexOf('someAPI') === 0) {
                return service;
            } else {
                return {};
            }
        });
        $provide.value('$resource', $resource);
    }));

    beforeEach(module('restServiceFactoryModule'));


    beforeEach(inject(function(_$rootScope_, _$q_, _restServiceFactory_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        restServiceFactory = _restServiceFactory_;
    }));

    it('returns singleton for a given API key', function() {

        expect(restServiceFactory.get("someAPI")).toBe(restServiceFactory.get("someAPI"));

        expect(restServiceFactory.get("someAPI")).not.toBe(restServiceFactory.get("someOtherAPI"));

    });

    it('returns an object for the given API with a list of expected methods', function() {

        var restService = restServiceFactory.get("someAPI");

        expect(Object.keys(restService)).toEqual(['save', 'remove', 'getById', 'get', 'query', 'page', 'update']);
    });

    it('calls $resource and adds getById, get, query, page, save, update and remove custom methods', function() {

        restServiceFactory.get("someAPI");
        var methodMap = $resource.calls[0].args[2];
        expect(Object.keys(methodMap)).toEqual(['getById', 'get', 'query', 'page', 'update', 'save', 'remove']);
    });

    it('calls $resource for the given API suffixed with default identifier placeholder', function() {

        var restService = restServiceFactory.get("someAPI");

        expect($resource).toHaveBeenCalledWith(
            'someAPI/:identifier', {},
            jasmine.any(Object));

    });

    it('calls $resource for the given API suffixed with custom identifier placeholder', function() {

        var restService = restServiceFactory.get("someAPI", "customidentifier");

        expect($resource).toHaveBeenCalledWith(
            'someAPI/:customidentifier', {},
            jasmine.any(Object));

    });

    it('calls $resource for the given API prefixed with domain when set', function() {

        restServiceFactory.setDomain("someDomain");

        var restService = restServiceFactory.get("someAPI", "customidentifier");

        expect($resource).toHaveBeenCalledWith(
            'someDomain/someAPI/:customidentifier', {},
            jasmine.any(Object));

    });


    it('$resource is initialized with a custom getById method', function() {

        var restService = restServiceFactory.get("someAPI");

        var methodMap = $resource.calls[0].args[2];

        expect(methodMap.getById).toEqual({
            method: 'GET',
            params: {},
            isArray: false,
            cache: false,
            headers: {
                'x-requested-with': 'Angular',
                'Pragma': 'no-cache'
            }
        });
    });

    it('$resource is initialized with a custom get method', function() {

        var restService = restServiceFactory.get("someAPI");

        var methodMap = $resource.calls[0].args[2];

        expect(methodMap.get).toEqual({
            method: 'GET',
            params: {},
            isArray: false,
            cache: false,
            headers: {
                'x-requested-with': 'Angular',
                'Pragma': 'no-cache'
            }
        });
    });

    it('$resource is initialized with a custom query method', function() {

        var restService = restServiceFactory.get("someAPI");

        var methodMap = $resource.calls[0].args[2];

        expect(methodMap.query).toEqual({
            method: 'GET',
            params: {},
            isArray: true,
            cache: false,
            headers: {
                'x-requested-with': 'Angular',
                'Pragma': 'no-cache'
            }
        });
    });

    it('$resource is initialized with a custom update method', function() {

        var restService = restServiceFactory.get("someAPI");

        var methodMap = $resource.calls[0].args[2];

        expect(methodMap.update).toEqual({
            method: 'PUT',
            cache: false,
            headers: {
                'x-requested-with': 'Angular'
            }
        });
    });

    it('$resource is initialized with a custom page method', function() {

        var restService = restServiceFactory.get("someAPI");

        var methodMap = $resource.calls[0].args[2];

        expect(methodMap.page).toEqual({
            method: 'GET',
            params: {},
            isArray: false, // due to spring Page
            cache: false,
            headers: {
                'x-requested-with': 'Angular',
                'Pragma': 'no-cache'
            },
            //without this I can't seem to access response headers
            transformResponse: jasmine.any(Function)
        });

        var transformedResponse = methodMap.page.transformResponse(undefined, function() {
            return {
                'header1': 'header1Value'
            };
        });
        expect(transformedResponse).toEqual({
            headers: {
                'header1': 'header1Value'
            }
        });

        var data = {
            a: 1,
            b: 'abc'
        };
        transformedResponse = methodMap.page.transformResponse(JSON.stringify(data), function() {
            return {
                'header1': 'header1Value'
            };
        });
        expect(transformedResponse).toEqual({
            a: 1,
            b: 'abc',
            headers: {
                'header1': 'header1Value'
            }
        });

    });


    it('getById delegates to $resource.getById and passes identifier in params object with expected key', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI", "customidentifier");

        var object = {};

        service.getById.andReturn({
            $promise: $q.when(object)
        });

        restService.getById("someId").then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.getById).toHaveBeenCalledWith({
            customidentifier: 'someId'
        }, {});
    });

    it('get delegates to $resource.get and passes passes copy of payload as params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI", "customidentifier");

        var object = {};
        var search = {
            key1: 'key1Value',
            key2: 'key2Value',
        };
        service.get.andReturn({
            $promise: $q.when(object)
        });

        restService.get(search).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.get).toHaveBeenCalledWith(search, {});
    });

    it('query delegates to $resource.query and passes passes copy of payload as params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI", "customidentifier");

        var object1 = {};
        var object2 = {};

        var search = {
            key1: 'key1Value',
            key2: 'key2Value',
        };
        service.query.andReturn({
            $promise: $q.when([object1, object2])
        });

        restService.query(search).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue.length).toBe(2);
        expect(returnedValue[0]).toBe(object1);
        expect(returnedValue[1]).toBe(object2);
        expect(service.query).toHaveBeenCalledWith(search, {});
    });



    it('page delegates to $resource.page and passes passes copy of payload as params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI", "customidentifier");

        var object = {};
        var search = {
            key1: 'key1Value',
            key2: 'key2Value',
        };

        service.page.andReturn({
            $promise: $q.when(object)
        });

        restService.page(search).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.page).toHaveBeenCalledWith(search, {});
    });

    it('save delegates to $resource.save and extract from payload identifier and placeholders to feed params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/:ph2", "customidentifier");

        var object = "readURI";
        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        };

        service.save.andReturn({
            $promise: $q.when(object)
        });

        restService.save(payload).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.save).toHaveBeenCalledWith({
            ph1: 'ph1Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        }, {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        });
    });

    it('update is rejected if identifier is missing from payload', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/:ph2", "customidentifier");

        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value'
        };

        restService.update(payload).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe("no data was found under the customidentifier field of object {\"key1\":\"key1Value\",\"ph1\":\"ph1Value\",\"key2\":\"key2Value\",\"ph2\":\"ph1Value\"}, it is necessary for update and remove operations");
        expect(service.update).not.toHaveBeenCalled();
    });

    it('update delegates to $resource.update and extract from payload identifier and placeholders to feed params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/:ph2", "customidentifier");

        var object = {};
        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        };

        service.update.andReturn({
            $promise: $q.when(object)
        });

        restService.update(payload).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.update).toHaveBeenCalledWith({
            ph1: 'ph1Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        }, {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        });
    });

    it('update delegates to $resource.update and extract from payload identifier, query params and placeholders to feed params', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/someResource?key1=:key1&key2=:key2", "customidentifier");

        var object = {};
        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        };

        service.update.andReturn({
            $promise: $q.when(object)
        });

        restService.update(payload).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(object);
        expect(service.update).toHaveBeenCalledWith({
            ph1: 'ph1Value',
            key1: 'key1Value',
            key2: 'key2Value',
            customidentifier: 'myId'
        }, {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        });
    });

    it('remove is rejected if identifier is missing from payload', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/:ph2", "customidentifier");

        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value'
        };

        restService.update(payload).then(function(response) {
            returnedValue = response;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe("no data was found under the customidentifier field of object {\"key1\":\"key1Value\",\"ph1\":\"ph1Value\",\"key2\":\"key2Value\",\"ph2\":\"ph1Value\"}, it is necessary for update and remove operations");
        expect(service.remove).not.toHaveBeenCalled();
    });

    it('remove delegates to $resource.remove and extract from payload identifier and placeholders to feed params and empties payload', function() {

        var returnedValue;
        var restService = restServiceFactory.get("someAPI/:ph1/:ph2", "customidentifier");

        var payload = {
            key1: 'key1Value',
            ph1: 'ph1Value',
            key2: 'key2Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        };

        service.remove.andReturn({
            $promise: $q.when()
        });

        restService.remove(payload).then(function(response) {
            returnedValue = true;
        }, function(error) {
            returnedValue = error;
        });

        $rootScope.$digest();

        expect(returnedValue).toBe(true);
        expect(service.remove).toHaveBeenCalledWith({
            ph1: 'ph1Value',
            ph2: 'ph1Value',
            customidentifier: 'myId'
        }, {});
    });

});
