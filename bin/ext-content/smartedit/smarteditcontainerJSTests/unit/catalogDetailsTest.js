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
describe("catalog details service  - unit test", function() {

    // Service Under Test
    var catalogDetailsService;

    // Set-up Service Under Test
    beforeEach(function() {
        module('catalogDetailsModule');
        inject(function(_catalogDetailsService_) {
            catalogDetailsService = _catalogDetailsService_;
        });
    });

    it('Should have an empty list in the begining ', function() {
        expect(catalogDetailsService.getItems()).toEqual([]);
    });

    it('Should add items to the list', function() {
        var theItems = ['a', 'b', 'c'];
        catalogDetailsService.addItems(theItems);
        expect(catalogDetailsService.getItems()).toEqual(['a', 'b', 'c']);
    });

    it('Should add items to the list by sequences', function() {
        catalogDetailsService.addItems(['a', 'b']);
        catalogDetailsService.addItems(['c', 'd']);
        catalogDetailsService.addItems(['e', 'f']);

        expect(catalogDetailsService.getItems()).toEqual(['a', 'b', 'c', 'd', 'e', 'f']);
    });

});
