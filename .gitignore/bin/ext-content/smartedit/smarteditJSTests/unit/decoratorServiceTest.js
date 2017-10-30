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
describe('test decoratorServiceModule', function() {

    var $q, $rootScope, $scope, decoratorService;

    beforeEach(customMatchers);

    beforeEach(module('decoratorServiceModule'));
    beforeEach(inject(function(_decoratorService_, _$q_, _$rootScope_) {
        decoratorService = _decoratorService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('getDecoratorsForComponent will retain a unique set of decorators for a given type', function() {

        decoratorService._activeDecorators = ['decorator0', 'decorator1', 'decorator2', 'decorator3'];
        decoratorService.addMappings({
            'type1': ['decorator1', 'decorator2'],
            'type2': ['decorator0'],
        });
        decoratorService.addMappings({
            'type1': ['decorator2', 'decorator3'],
        });

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual(['decorator1', 'decorator2', 'decorator3']);
    });


    it('getDecoratorsForComponent will retain a unique set of decorators from all matching regexps', function() {

        decoratorService._activeDecorators = ['decorator1', 'decorator2', 'decorator3', 'decorator4', 'decorator5', 'decorator6'];
        decoratorService.addMappings({
            '*Suffix': ['decorator1', 'decorator2'],
            '.*Suffix': ['decorator2', 'decorator3'],
            'TypeSuffix': ['decorator3', 'decorator4'],
            '^((?!Middle).)*$': ['decorator4', 'decorator5'],
            'PrefixType': ['decorator5', 'decorator6'],
        });

        expect(decoratorService.getDecoratorsForComponent('TypeSuffix')).toEqual(['decorator1', 'decorator2', 'decorator3', 'decorator4', 'decorator5']);

        expect(decoratorService.getDecoratorsForComponent('TypeSuffixes')).toEqual(['decorator2', 'decorator3', 'decorator4', 'decorator5']);

        expect(decoratorService.getDecoratorsForComponent('MiddleTypeSuffix')).toEqual(['decorator1', 'decorator2', 'decorator3']);
    });


    it('enable adds decorators to the Array of active decorators and can be invoked multiple times', function() {

        expect(decoratorService._activeDecorators).toEqual([]);
        decoratorService.enable('key1');
        expect(decoratorService._activeDecorators).toEqual(['key1']);
        decoratorService.enable('key2');
        expect(decoratorService._activeDecorators).toEqual(['key1', 'key2']);
        decoratorService.enable('key1');
        expect(decoratorService._activeDecorators).toEqual(['key1', 'key2']);
    });

    it('disable removes decorators from the Array of active decorators and can be invoked multiple times', function() {

        decoratorService._activeDecorators = ['key1', 'key2', 'key3'];
        decoratorService.disable('key1');
        expect(decoratorService._activeDecorators).toEqual(['key2', 'key3']);
        decoratorService.disable('key2');
        expect(decoratorService._activeDecorators).toEqual(['key3']);
        decoratorService.disable('key1');
        expect(decoratorService._activeDecorators).toEqual(['key3']);
    });

    it('getDecoratorsForComponent will filter based on activeDecorators', function() {

        decoratorService.addMappings({
            'type1': ['decorator1', 'decorator2'],
        });

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual([]);

        decoratorService.enable('decorator1');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual(['decorator1']);

        decoratorService.enable('decorator2');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual(['decorator1', 'decorator2']);

        decoratorService.enable('decorator3');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual(['decorator1', 'decorator2']);

        decoratorService.disable('decorator1');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual(['decorator2']);

        decoratorService.disable('decorator2');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual([]);

        decoratorService.disable('decorator3');

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqual([]);

    });

});
