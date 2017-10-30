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
describe('test functionsModule', function() {

    var isBlank, hitch, copy, customTimeout, merge, getQueryString, parseQuery, trim, convertToArray, uniqueArray, regExpFactory, generateIdentifier;

    beforeEach(customMatchers);
    beforeEach(module('functionsModule'));

    beforeEach(inject(function(_$rootScope_, _isBlank_, _hitch_, _copy_, _customTimeout_, _merge_, _getQueryString_,
        _parseQuery_, _trim_, _convertToArray_, _uniqueArray_, _regExpFactory_, _generateIdentifier_, _escapeHtml_) {

        $rootScope = _$rootScope_;

        isBlank = _isBlank_;
        hitch = _hitch_;
        copy = _copy_;
        customTimeout = _customTimeout_;
        merge = _merge_;
        getQueryString = _getQueryString_;
        parseQuery = _parseQuery_;
        trim = _trim_;
        convertToArray = _convertToArray_;
        uniqueArray = _uniqueArray_;
        regExpFactory = _regExpFactory_;
        generateIdentifier = _generateIdentifier_;
        escapeHtml = _escapeHtml_;

        jasmine.Clock.useMock();

    }));

    it('isBlank will return true if a variable is undefined or null or empty', function() {

        expect(isBlank('')).toBe(true);
        expect(isBlank(null)).toBe(true);
        expect(isBlank('null')).toBe(true);
        expect(isBlank(undefined)).toBe(true);
        expect(isBlank('not blank')).toBe(false);

    });

    it('hitch will return a new function that will bind the given scope into a given function', function() {

        myfunc = function(arg1, arg2) {
            return arg1 + " " + this.message + " " + arg2;
        };

        var object1 = {
            message: '. This is message of object1',
            func: myfunc
        };

        var object2 = {
            message: '. This is message of object2',
        };


        object2.func = hitch(object1, myfunc, "Hello");

        expect(object2.func('. From object2')).toBe('Hello . This is message of object1 . From object2');


    });

    it('copy will do a deep copy of a given object into another object', function() {

        var JSVar = {
            'key1': 'value1',
            'key2': 'value2'
        };

        var newVar = copy(JSVar);

        expect(newVar).not.toBe(JSVar);
        expect(newVar).toEqualData({
            key1: 'value1',
            key2: 'value2'
        });


    });

    it('customTimeout will call a specified function after a specified duration (in ms)', function() {

        var duration = 5000;
        //var func = function() {};
        var func = jasmine.createSpy('func').andReturn(function() {});

        spyOn(window, 'setTimeout').andCallThrough();

        customTimeout(func, duration);
        expect(func).not.toHaveBeenCalled();

        jasmine.Clock.tick(2000);
        expect(func).not.toHaveBeenCalled();

        jasmine.Clock.tick(3000);
        expect(func).toHaveBeenCalled();

        expect(window.setTimeout).toHaveBeenCalledWith(jasmine.any(Function), 5000);

    });

    it('merge will call jquery extend and merge two objects into one', function() {

        var extendSpy = spyOn(jQuery, 'extend').andCallThrough();

        var source = {
            apple: 0,
            banana: {
                weight: 52,
                price: 100
            },
            cherry: 97
        };

        var dest = {
            banana: {
                price: 200
            },
            durian: 100
        };

        var result = merge(source, dest);

        expect(extendSpy).toHaveBeenCalledWith(source, dest);
        expect(result).toEqualData({
            apple: 0,
            banana: {
                price: 200
            },
            cherry: 97,
            durian: 100
        });

    });

    it('getQueryString will convert given object into query type', function() {

        var sampleObj = {
            key1: 'value1',
            key2: 'value2',
            key3: 'value3',
            key4: 'value4'
        };

        var queryString = getQueryString(sampleObj);

        expect(queryString).toBe('?&key1=value1&key2=value2&key3=value3&key4=value4');

    });

    it('parseQuery will convert give query into an object of params', function() {

        var query = '?abc=abc&def=def&ijk=789';

        var resultObj = parseQuery(query);

        expect(resultObj).toEqualData({
            abc: 'abc',
            def: 'def',
            ijk: '789'
        });

    });

    it('trim function removes space at the beginning and end of a given string', function() {

        var inputString = "  testStringWithSpaces ";

        expect(trim(inputString)).toBe('testStringWithSpaces');

    });

    it('convertToArray will convert a given object to an array', function() {

        var sampleObj = {

            key1: 'value1',
            key2: 'value2',
            key3: 'value3',
            key4: 'value4',

        };

        expect(convertToArray(sampleObj)).toEqualData([{
            key: 'key1',
            value: 'value1'
        }, {
            key: 'key2',
            value: 'value2'
        }, {
            key: 'key3',
            value: 'value3'
        }, {
            key: 'key4',
            value: 'value4'
        }]);

    });

    it('uniqueArray will return an array of unique items from a given pair of input arrays', function() {

        var array1 = ['item1', 'item2', 'item3'];
        var array2 = ['item4', 'item3', 'item6', 'item2'];

        var uniqueArr = uniqueArray(array1, array2);

        expect(uniqueArr).toEqualData(['item1', 'item2', 'item3', 'item4', 'item6']);

    });

    it('regExpFactory will convert a given pattern into a regular expression', function() {

        var pattern1 = '*1234';
        var regExp1 = regExpFactory(pattern1);
        expect(regExp1).toEqualData(/^.*1234$/g);

        var pattern2 = '^((?!Middle).)*$';
        var regExp2 = regExpFactory(pattern2);
        expect(regExp2).toEqualData(/^((?!Middle).)*$/g);

    });

    it('generateIdentifier will generate a unique identifier each time it is called', function() {

        var uniqueKey1 = generateIdentifier();
        var uniqueKey2 = generateIdentifier();

        expect(uniqueKey1).not.toBe(uniqueKey2);

    });

    it('escapeHtml will escape dangerous characters from a given string', function() {

        var string = escapeHtml("hello<button>&'\"");
        expect(string).toBe("hello&lt;button&gt;&amp;&apos;&quot;");

    });

    it('escapeHtml will handle numeric values correctly', function() {

        var string = escapeHtml(123456);
        expect(string).toBe(123456);

    });

});
