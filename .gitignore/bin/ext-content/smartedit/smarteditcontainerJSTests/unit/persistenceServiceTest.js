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
describe('test sharedDataService', function() {

    var sharedDataService, gatewayProxy;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));


    beforeEach(module("sharedDataServiceModule"));

    beforeEach(inject(function(_sharedDataService_) {
        sharedDataService = _sharedDataService_;
    }));

    it('shared data service should validate get and set method', function() {
        sharedDataService.set('catalogVersion', '1.4');
        expect(sharedDataService.get('catalogVersion')).toEqual('1.4');
    });


    it('shared data service should override the value for a given key', function() {
        sharedDataService.set('catalogVersion', '1.4');
        sharedDataService.set('catalogVersion', '1.6');
        expect(sharedDataService.get('catalogVersion')).toEqual('1.6');
    });


    it('shared data service should check the object saved for a given key', function() {
        var obj = {
            catalog: 'apparel-ukContentCatalog',
            catalogVersion: '1.4'
        };

        sharedDataService.set('obj', obj);
        expect(sharedDataService.get('obj').catalog).toEqual('apparel-ukContentCatalog');
        expect(sharedDataService.get('obj').catalogVersion).toEqual('1.4');
    });


    it('shared data service should set the value to null for a given key', function() {
        sharedDataService.set('catalogVersion', '1.4');
        sharedDataService.set('catalogVersion', null);
        expect(sharedDataService.get('catalogVersion')).toEqual(null);
    });

    it('shared data service inits a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(sharedDataService);
    });
});
